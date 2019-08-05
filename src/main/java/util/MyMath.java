package util;

public class MyMath {
    public static double normalize(double min, double max, double value) {
        return 2 * ((value - min) / (max - min)) - 1;
    }

    public static double denormalize(double min, double max, double value) {
        return ((value + 1) / 2) * (max - min) + min;
    }
}
