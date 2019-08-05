package itemitem.service;

import itemitem.model.Matrix;
import util.MyMath;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AdCosineService {

    public double computeSimilarity(int itemIdX, int itemIdY, Map<Integer, HashMap<Integer, Double>> ratings) {
        double nominator = 0.0, denom1 = 0.0, denom2 = 0.0;

        for (var rating : ratings.entrySet()) {
            // check if user rated both
            if (!rating.getValue().containsKey(itemIdX) | !rating.getValue().containsKey(itemIdY)) continue;

            // compute average of user
            double avg = rating.getValue().values().stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);

            // compute rating
            double adRatingX = rating.getValue().get(itemIdX) - avg;
            double adRatingY = rating.getValue().get(itemIdY) - avg;

            nominator += adRatingX * adRatingY;
            denom1 += Math.pow(adRatingX, 2);
            denom2 += Math.pow(adRatingY, 2);
        }

        return (nominator / (Math.sqrt(denom1) * Math.sqrt(denom2)));
    }

    public double predictRating(int userId, int itemId, Map<Integer, HashMap<Integer, Double>> ratings, Matrix<Double> similarityMatrix) {
        double numerator = 0.0, denominator = 0.0;
        for (var entry : ratings.get(userId).entrySet()) {
            if (entry.getKey().equals(itemId)) continue;

            var sim = similarityMatrix.get(itemId, entry.getKey());
            var normRating = MyMath.normalize(1, 5, entry.getValue());
            numerator +=  sim * normRating;
            denominator += Math.abs(sim);
        }
        return MyMath.denormalize(1, 5, (numerator / denominator));
    }

    public Matrix<Double> buildSimilarityMatrix(Map<Integer, HashMap<Integer, Double>> ratings, Set<Integer> itemIds) {
        Matrix<Double> matrix = new Matrix<>();
        itemIds.forEach(id1 -> itemIds.forEach(id2 -> {
            // on self
            if (id1.equals(id2)) {
                matrix.put(id1, id2, Double.NaN);
            } else {
                matrix.put(id1, id2, computeSimilarity(id1, id2, ratings));
            }
        }));

        return matrix;
    }

}
