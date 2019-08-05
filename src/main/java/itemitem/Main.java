package itemitem;


import itemitem.service.AdCosineService;
import itemitem.service.OneSlopeService;
import itemitem.util.Parser;

public class Main {
    public static void main(String[] args) {
//        adCosine();
        oneSlope();
    }

    public static void adCosine() {
        var parseResult = new Parser().parseFile("data/cosine.csv", ",");
        var itemIds = parseResult.getItemIds();
        var smallDataset = parseResult.getResult();
        var adCosineService = new AdCosineService();

        System.out.println("\n\nAdjusted Cosine Similarity for items 103, 104");
        var sim = adCosineService.computeSimilarity(103, 104, smallDataset);
        System.out.println("\t" + sim);

        System.out.println("\n\nSimilarity matrix:\n");
        var matrix = adCosineService.buildSimilarityMatrix(smallDataset, itemIds);
        matrix.printMatrix();

        System.out.println("\npredicted rating for user 1 item 103");
        var pred1 = adCosineService.predictRating(1, 103, smallDataset, matrix);
        System.out.println("\t" + pred1);
    }

    public static void oneSlope() {
        var parseResult = new Parser().parseFile("data/oneslope.csv", ",");
        var smallDataset = parseResult.getResult();

        var oneSlopeService = new OneSlopeService();
        var matrix = oneSlopeService.buildDeviationMatrix(smallDataset);

        System.out.println(smallDataset);
        System.out.println("\nDeviation matrix: \n");
        matrix.printMatrix();

        System.out.println("\n\nOneSlope: prediction for user 2 item 109");
        System.out.println("\titem id: 109, prediction: " + oneSlopeService.predictRating(matrix, smallDataset, 2, 109));

        var bigDataset = new Parser().parseFile("data/big.data", "\t").getResult();
        System.out.println("\ngrouplens top 5 recommendations for user 186");
        var bigMatrix = oneSlopeService.buildDeviationMatrix(bigDataset);
        var topRecommendationsGl = oneSlopeService.topRecommendations(bigMatrix, bigDataset, 186, 5);
        topRecommendationsGl.forEach((k, v) -> System.out.println("\titem id: " + k + ", prediction: " + v));
    }
}
