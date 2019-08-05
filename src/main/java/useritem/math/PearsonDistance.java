package useritem.math;

public class PearsonDistance implements Distance {
    @Override
    public double between(double[] array, double[] other) {
        double sx = 0, sy = 0, xy = 0, px = 0, py = 0;

        int n = 0;
        for (int i = 0; i < array.length; i++) {
            if(Double.isNaN(array[i]) || Double.isNaN(other[i])) {
                continue;
            }

            xy += array[i] * other[i];
            sx += array[i];
            sy += other[i];
            px += Math.pow(array[i], 2);
            py += Math.pow(other[i], 2);
            n++;
        }

        xy -= sx * sy / n;
        px = Math.sqrt(px - Math.pow(sx, 2) / n);
        py = Math.sqrt(py - Math.pow(sy, 2) / n);
        xy = xy / (px * py);

        return xy;
    }
}
