package apriori.model;

import de.mrapp.apriori.ItemSet;
import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;
import java.text.NumberFormat;

@Getter
@Setter
public class AssociationRule {
    private ItemSet<NamedItem> body;
    private ItemSet<NamedItem> head;
    private double support;
    private double confidence;
    private double lift;

    public AssociationRule(ItemSet<NamedItem> body, ItemSet<NamedItem> head) {
        this.body = body;
        this.head = head;
    }

    @Override
    public String toString() {
        NumberFormat f = new DecimalFormat("#0.00");
        StringBuilder sb = new StringBuilder("( ");

        this.body.forEach(i -> sb.append(i.getName()).append(" "));

        sb.append(") => ( ");

        this.head.forEach(i -> sb.append(i.getName()).append(" "));

        sb.append("), support: ")
                .append(f.format(support))
                .append(", confidence: ")
                .append(f.format(confidence))
                .append(", lift: ")
                .append(f.format(lift));

        return sb.toString();
    }
}
