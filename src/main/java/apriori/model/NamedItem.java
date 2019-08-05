package apriori.model;

import de.mrapp.apriori.Item;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
public class NamedItem implements Item {
    private String name;

    public NamedItem(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Item o) {
        return this.toString().compareTo(o.toString());
    }
}
