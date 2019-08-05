package useritem.math;

public class CosineDistance implements Distance {
    @Override
    public double between(double[] array, double[] other) {
        double xy = 0, sx = 0, sy = 0;

        for (int i = 0; i < array.length; i++) {
            double a = convertNan(array[i]);
            double o = convertNan(other[i]);

            xy += a * o;
            sx += Math.pow(a, 2);
            sy += Math.pow(o, 2);
        }

        return xy / (Math.sqrt(sx) * Math.sqrt(sy));
    }

    private double convertNan(double value) {
        if(Double.isNaN(value)) {
            return 0.0;
        }

        return value;
    }
}
