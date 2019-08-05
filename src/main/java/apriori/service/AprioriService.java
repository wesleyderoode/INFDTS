package apriori.service;

import apriori.model.AssociationRule;
import apriori.model.NamedItem;
import de.mrapp.apriori.*;
import lombok.Getter;

import java.util.*;

@Getter
public class AprioriService {
    private Apriori<NamedItem> apriori;
    private Output<NamedItem> output;
    private Iterable<Transaction<NamedItem>> iterable;
    private List<ItemSet<NamedItem>> transactions;

    public AprioriService(Iterable<Transaction<NamedItem>> iterable) {
        this.apriori = new Apriori.Builder<NamedItem>(0.5).generateRules(0.1).create();
        this.output = apriori.execute(iterable);
        this.iterable = iterable;
        this.transactions = initTransactions();
    }

    public List<AssociationRule> getAssociationRules() {
        List<AssociationRule> rules = new ArrayList<>();

        // transform api rules
        Objects.requireNonNull(output.getRuleSet())
                .forEach(rule -> rules.add(new AssociationRule(rule.getBody(), rule.getHead())));

        return rules;
    }

    public List<ItemSet<NamedItem>> getTransactions() {
        return this.transactions;
    }

    public RuleSet<NamedItem> getRulesFromApi() {
        return output.getRuleSet();
    }

    public void printTransactionTable() {
        int count = 1;
        System.out.println("\nTransaction table: \n");
        for (var transaction : iterable) {
            System.out.print("t" + count++ + " | ");

            for (var item : transaction) {
                System.out.print(item.getName() + " | ");
            }
            System.out.println();
        }
    }

    private List<ItemSet<NamedItem>> initTransactions() {
        List<ItemSet<NamedItem>> result = new ArrayList<>();

        for(var entry : iterable) {
            ItemSet<NamedItem> transaction = new ItemSet<>();
            entry.forEach(transaction::add);
            result.add(transaction);
        }

        return result;
    }
}
