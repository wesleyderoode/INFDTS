package itemitem.service;

import itemitem.model.Deviation;
import itemitem.model.Matrix;

import java.util.*;
import java.util.stream.Collectors;

public class OneSlopeService {

    public Matrix<Deviation> buildDeviationMatrix(Map<Integer, HashMap<Integer, Double>> ratings) {
        var result = new Matrix<Deviation>(ratings.size());

        // differences between ratings
        Map<Integer, Map<Integer, Double>> diffs = new HashMap<>();

        // frequencies of items rated
        Map<Integer, Map<Integer, Integer>> freqs = new HashMap<>();

        ratings.values().forEach(itemRating -> {
            // for each (item, rating)
            itemRating.forEach((i1, r1) -> {
                if (!diffs.containsKey(i1)) {
                    diffs.put(i1, new HashMap<>());
                    freqs.put(i1, new HashMap<>());
                }

                itemRating.forEach((i2, r2) -> {
                    // get current frequency
                    int currFreq = 0;
                    if (freqs.get(i1).containsKey(i2)) {
                        currFreq = freqs.get(i1).get(i2);
                    }
                    // get current difference
                    double currDiff = 0.0;
                    if (diffs.get(i1).containsKey(i2)) {
                        currDiff = diffs.get(i1).get(i2);
                    }
                    // update difference
                    double newDiff = r1 - r2;
                    diffs.get(i1).put(i2, currDiff + newDiff);
                    freqs.get(i1).put(i2, currFreq + 1);
                });
            });
        });

        // compute deviations
        diffs.forEach((k1, v1) -> v1.forEach((k2, v2) -> {
            double diff = v1.get(k2);
            int freq = freqs.get(k1).get(k2);
            result.put(k2, k1, new Deviation(diff / freq, freq));
        }));

        return result;
    }

    public void updateDeviationMatrix(Matrix<Deviation> deviationMatrix, Map<Integer, HashMap<Integer, Double>> ratings, int userId, int itemId, double rating) {
        var userRatings = ratings.get(userId);

        // add new rating to given users ratings
        if (!userRatings.containsKey(itemId)) {
            userRatings.put(itemId, rating);

            userRatings.forEach((k, v) -> {
                if (k.equals(itemId)) return;

                deviationMatrix.get(k, itemId).update(rating - v);
                deviationMatrix.get(itemId, k).update((rating - v) * -1);
            });
        }
    }

    public double predictRating(Matrix<Deviation> deviationMatrix, Map<Integer, HashMap<Integer, Double>> ratings, int userId, int itemId) {
        double numerator = 0.0; int denominator = 0;

        var userRatings = ratings.get(userId);
        for (var entry : userRatings.entrySet()) {
            var dev = deviationMatrix.get(entry.getKey(), itemId);
            if(dev == null) continue;
            numerator += (entry.getValue() + dev.getValue()) * dev.getCard();
            denominator += dev.getCard();
        }

        return numerator / denominator;
    }

    public Map<Integer, Double> topRecommendations(Matrix<Deviation> deviationMatrix, Map<Integer, HashMap<Integer, Double>> ratings, int userId, int n) {
        // unrated by user
        Set<Integer> unrated = new HashSet<>(deviationMatrix.keySet());
        unrated.removeAll(ratings.get(userId).keySet());

        // tree map for faster sorting
        Map<Integer, Double> predictions = new TreeMap<>();
        unrated.forEach(item -> predictions.put(item, predictRating(deviationMatrix, ratings, userId, item)));

        return predictions.entrySet()
                .stream()
                .sorted((Map.Entry.<Integer, Double>comparingByValue().reversed()))
                .limit(n)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
