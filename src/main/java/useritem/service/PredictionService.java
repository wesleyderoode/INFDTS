package useritem.service;

import useritem.model.Rating;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PredictionService {
    public double singleItemPrediction(int itemId, Map<Integer, Double> neighbours, Map<Integer, Set<Rating>> ratings) {
        double result = 0.0, weight = 0.0;

        for (var neighbour : neighbours.entrySet()) {
            // check if neighbour has rated item
            if (ratings.get(neighbour.getKey()).contains(new Rating(itemId, 0.0))) {
                double rating = ratings.get(neighbour.getKey()).stream()
                        .filter(r -> r.getItemId() == itemId)
                        .findFirst().orElseThrow(NullPointerException::new)
                        .getValue(); // get rating by neighbour for item

                result += (neighbour.getValue() * rating);
                weight += neighbour.getValue();
            }
        }
        return result / weight;
    }

    public Set<Rating> nItemPrediction(int userId, Map<Integer, Double> neighbours, Map<Integer, Set<Rating>> ratings, int n, int minimalRated) {
        Set<Rating> predictions = new HashSet<>();

        // get items rated by neighbours
        Set<Rating> itemsRated = ratings.entrySet().stream()
                .filter(e -> neighbours.containsKey(e.getKey()))
                .flatMap(e -> e.getValue().stream())
                .collect(Collectors.toSet());

        // remove items already rated by user
        itemsRated.removeAll(ratings.get(userId));
        itemsRated.forEach(item -> {
            int itemId = item.getItemId();
            long timesRated = ratings.entrySet().stream()
                    .filter(e -> neighbours.containsKey(e.getKey()))
                    .filter(e -> e.getValue().contains(new Rating(itemId, 0)))
                    .count();
            if (timesRated >= minimalRated) {
                predictions.add(new Rating(itemId, singleItemPrediction(itemId, neighbours, ratings)));
            }
        });

        return predictions.stream()
                .sorted(Comparator.comparingDouble(Rating::getValue).reversed())
                .limit(n).collect(Collectors.toSet());
    }
}
