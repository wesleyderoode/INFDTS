package useritem.math;


public class EuclideanDistance implements Distance {
    @Override
    public double between(double[] array, double[] other) {
        double xy = 0;

        for(int i = 0; i < array.length; i++) {
            if(Double.isNaN(array[i]) || Double.isNaN(other[i])) {
                continue;
            }

            xy += Math.pow(array[i] - other[i], 2);
        }

        return 1 / (1 + Math.sqrt(xy));
    }
}
