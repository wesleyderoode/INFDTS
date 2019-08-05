package useritem.service;

import useritem.model.Rating;
import useritem.math.Distance;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;

public class ClassificationService {
    public Map<Integer, Double> knn(int userId, Map<Integer, Set<Rating>> userRatings, Distance function, int k, double threshold) {
        Set<Rating> target = userRatings.get(userId);

        var distances = new HashMap<Integer, Double>(userRatings.size());
        for (var entry : userRatings.entrySet()) {

            if (entry.getKey().equals(userId)) {
                continue;
            }

            if (noAdditionRatings(target, entry.getValue())) {
                continue;
            }

            ImmutablePair<double[], double[]> vectors = toVectorTuple(target, entry.getValue());

            double distance = function.between(vectors.getLeft(), vectors.getRight());

            if (distance < threshold | Double.isNaN(distance)) {
                continue;
            }

            if (distances.size() < k) {
                distances.put(entry.getKey(), distance);
            } else {
                Map.Entry<Integer, Double> farthest = distances.entrySet().stream()
                        .min(Comparator.comparing(Map.Entry::getValue))
                        .orElseThrow(NullPointerException::new);

                if (farthest.getValue() < distance) {
                    distances.remove(farthest.getKey());
                    distances.put(entry.getKey(), distance);
                }

                threshold = distances.values().stream()
                        .min(Comparator.naturalOrder())
                        .orElseThrow(NullPointerException::new);
            }
        }

        return distances;
    }

    private boolean noAdditionRatings(Set<Rating> ratings, Set<Rating> other) {
        return ratings.containsAll(other);
    }

    private HashSet<Rating> mergeRatings(Set<Rating> ratings, Set<Rating> other) {
        return new HashSet<>() {{
            addAll(ratings);
            addAll(other);
        }};
    }

    public ImmutablePair<double[], double[]> toVectorTuple(Set<Rating> ratings, Set<Rating> other) {
        Set<Rating> merged = mergeRatings(ratings, other);

        double[] v1 = new double[merged.size()];
        double[] v2 = new double[merged.size()];

        int index = 0;
        for (Rating rating : merged) {
            v1[index] = ratings.stream()
                    .filter(x -> x.equals(rating))
                    .findFirst().map(Rating::getValue)
                    .orElse(Double.NaN);

            v2[index] = other.stream()
                    .filter(x -> x.equals(rating))
                    .findFirst().map(Rating::getValue)
                    .orElse(Double.NaN);

            index++;
        }

        return new ImmutablePair<>(v1, v2);
    }
}
