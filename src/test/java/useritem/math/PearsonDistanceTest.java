package useritem.math;

import org.junit.Test;

import static java.lang.Double.NaN;

import static org.assertj.core.api.Assertions.*;

public class PearsonDistanceTest {

    private final PearsonDistance func = new PearsonDistance();

    @Test
    public void between_WithValidVector_ReturnsDistance() {
        double[] v1 = {4.75, 4.5, 5, 4.25, 4};
        double[] v2 = {4, 3, 5, 2, 1};

        var actual = func.between(v1, v2);

        assertThat(actual).isEqualTo(1, withPrecision(0.001));
    }

    @Test
    public void between_WithNaNValues_ReturnsDistance() {
        double[] v1 = {3, 4.75, 4.5, 5, 4.25, 4, 8};
        double[] v2 = {NaN, 4, 3, 5, 2, 1, NaN};

        var actual = func.between(v1, v2);

        assertThat(actual).isEqualTo(1, withPrecision(0.001));
    }
}