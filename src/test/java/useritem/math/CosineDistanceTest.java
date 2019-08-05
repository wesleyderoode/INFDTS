package useritem.math;

import org.junit.Test;

import static java.lang.Double.NaN;

import static org.assertj.core.api.Assertions.*;

public class CosineDistanceTest {

    private final CosineDistance func = new CosineDistance();

    @Test
    public void between_WithValidVectors_ReturnsDistance() {
        double[] v1 = {4.75, 4.5, 5, 4.25, 4};
        double[] v2 = {4, 3, 5, 2, 1};

        var actual = func.between(v1, v2);

        assertThat(actual).isEqualTo(0.935, withPrecision(0.001));
    }

    @Test
    public void between_WithNaNValues_ReturnsDistance() {
        double[] v1 = {3.0, NaN, NaN};
        double[] v2 = {5.0, 2.5, 2.0};

        var actual = func.between(v1, v2);

        assertThat(actual).isEqualTo(0.842, withPrecision(0.001));
    }
}