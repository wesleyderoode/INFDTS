package useritem.math;

import org.junit.Test;

import static java.lang.Double.NaN;

import static org.assertj.core.api.Assertions.*;

public class EuclideanDistanceTest {

    private final EuclideanDistance func = new EuclideanDistance();

    @Test
    public void between_WithValidVectors_ReturnsDistance() {
        double[] v1 = {2, 4, 1};
        double[] v2 = {0, 3, 2};

        var actual = func.between(v1, v2);

        assertThat(actual).isEqualTo(0.289, withPrecision(0.001));
    }

    @Test
    public void between_WithNaNValues_ReturnsDistance() {
        double[] v1 = {4, 2, 4, 1, NaN};
        double[] v2 = {NaN, 0, 3, 2, 9};

        var actual = func.between(v1, v2);

        assertThat(actual).isEqualTo(0.289, withPrecision(0.001));
    }
}