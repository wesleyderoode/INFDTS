package itemitem.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.text.DecimalFormat;
import java.text.NumberFormat;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Deviation {
    private double value;
    private int card;

    public void update(double value) {
        var sum = this.value * card;
        this.card += 1;
        this.value = (sum + value) / card;
    }

    @Override
    public String toString() {
        NumberFormat formatter = new DecimalFormat("#0.000");
        return formatter.format(value);
    }
}
