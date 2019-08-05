package useritem;

import useritem.math.EuclideanDistance;
import useritem.math.PearsonDistance;
import useritem.model.Rating;
import useritem.service.ClassificationService;
import useritem.service.PredictionService;
import util.Parser;

public class Main {

    public static void main(String[] args) {

        ClassificationService cfnService = new ClassificationService();
        PredictionService pdnService = new PredictionService();

        var smallDataset = new Parser().parseFile("data/small.csv", ",").getUserRatings();

        /* Pearson: user 7 compared to 1 to 6 */
        System.out.println("\nPearson: user 7 compared to 1 to 6");
        var pearson = new PearsonDistance();
        var user7 = smallDataset.get(7);
        smallDataset.forEach((k, v) -> {
            if (k == 7) return;
            var vectors = cfnService.toVectorTuple(user7, v);
            System.out.println("\tuser id: " + k + ", sim: " + pearson.between(vectors.left, vectors.right));
        });

        System.out.println("\nEuclidean: user 7 compared to 1 to 6");
        var euclidean = new EuclideanDistance();
        smallDataset.forEach((k, v) -> {
            if (k == 7) return;
            var vectors = cfnService.toVectorTuple(user7, v);
            System.out.println("\tuser id: " + k + ", sim: " + euclidean.between(vectors.left, vectors.right));
        });

        /* KNN Pearson: user=7, k=3, and threshold=0.35 */
        System.out.println("\nKNN Pearson: user=7, k=3, and threshold=0.35");
        cfnService.knn(7, smallDataset, pearson, 3, 0.35)
                .forEach((k, v) -> System.out.println("\tuser id: " + k + ", sim: " + v));

        /* Prediction for items: 106, 101 and 103 */
        System.out.println("\nPrediction for items: 106, 101 and 103");
        var knnResult = cfnService.knn(7, smallDataset, pearson, 3, 0.35);
        System.out.println("\titem id: 106, prediction: " + pdnService.singleItemPrediction(106, knnResult, smallDataset));
        System.out.println("\titem id: 101, prediction: " + pdnService.singleItemPrediction(101, knnResult, smallDataset));
        System.out.println("\titem id: 103, prediction: " + pdnService.singleItemPrediction(103, knnResult, smallDataset));

        /* Prediction for items 101 and 193 after updating item 6 for user 7 */
        System.out.println("\nPrediction for items 101 and 193 after updating item 6 for user 7");
        smallDataset.get(7).add(new Rating(106, 2.8));
        knnResult = cfnService.knn(7, smallDataset, pearson, 3, 0.35);
        System.out.println("\titem id: 101, prediction: " + pdnService.singleItemPrediction(101, knnResult, smallDataset));
        System.out.println("\titem id: 103, prediction: " + pdnService.singleItemPrediction(103, knnResult, smallDataset));


        /* KNN Pearson and prediction with Grouplens 100k */
        System.out.println("\nKNN Pearson and prediction with Grouplens 100k");
        // Test 9: top n predictions with big dataset
        System.out.println("## minimal rated=0");
        var bigDataset = new Parser().parseFile("data/big.data", "\t").getUserRatings();
        var knnResult9 = cfnService.knn(186, bigDataset, new PearsonDistance(), 25, 0.35);
        var predictions1 = pdnService.nItemPrediction(186, knnResult9, bigDataset, 8, 0);
        predictions1.forEach(rating -> System.out.println(String.format("\tPrediction item %d: %f", rating.getItemId(), rating.getValue())));
        System.out.println();

        // Test 10: top n predictions with big dataset
        System.out.println("## minimal rated=3:");
        var knnResult10 = cfnService.knn(186, bigDataset, new PearsonDistance(), 25, 0.35);
        var predictions2 = pdnService.nItemPrediction(186, knnResult10, bigDataset, 8, 3);
        predictions2.forEach(rating -> System.out.println(String.format("\tPrediction item %d: %f", rating.getItemId(), rating.getValue())));
        System.out.println();
    }
}
