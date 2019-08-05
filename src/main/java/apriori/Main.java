package apriori;

import apriori.model.AssociationRule;
import apriori.model.NamedItem;
import apriori.service.AprioriService;
import apriori.util.DataIterator;
import de.mrapp.apriori.ItemSet;
import de.mrapp.apriori.Transaction;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

public class Main {
    /*
    Apriori algorithm used: Michael Rapp @ https://github.com/michael-rapp/Apriori
     */
    public static void main(String[] args) {
        File inputFile = Paths.get("data/apriori.txt").toFile();
        Iterable<Transaction<NamedItem>> iterable = () -> new DataIterator(inputFile);

        AprioriService aprioriService = new AprioriService(iterable);

        // print transactions
        aprioriService.printTransactionTable();

        // get association rules
        List<AssociationRule> rules = aprioriService.getAssociationRules();

        // compute support, confidence and lift for all rules
        computeSupport(rules, aprioriService.getTransactions());
        computeConfidence(rules, aprioriService.getTransactions());
        computeLift(rules, aprioriService.getTransactions());

        System.out.println("\nAssociation rules: \n");
        rules.forEach(System.out::println);

        System.out.println("\nRules computed from library (to check own implementation): \n");
        System.out.println(aprioriService.getRulesFromApi());
    }

    public static void computeSupport(List<AssociationRule> rules, List<ItemSet<NamedItem>> transactions) {
        for (var rule : rules) {
            double sumXY = 0.0;
            for (var trans : transactions) {
                if (trans.containsAll(rule.getBody())
                        & trans.containsAll(rule.getHead())) {
                    sumXY += 1;
                }
            }
            rule.setSupport(sumXY / transactions.size());
        }
    }

    public static void computeConfidence(List<AssociationRule> rules, List<ItemSet<NamedItem>> transactions) {
        for (var rule : rules) {
            double sumXY = 0.0;
            double sumX = 0.0;
            for (var trans : transactions) {
                if (trans.containsAll(rule.getBody())
                        & trans.containsAll(rule.getHead())) {
                    sumXY += 1;
                }
                if (trans.containsAll(rule.getBody())) {
                    sumX += 1;
                }
            }
            rule.setConfidence(sumXY / sumX);
        }
    }

    public static void computeLift(List<AssociationRule> rules, List<ItemSet<NamedItem>> transactions) {
        for (var rule : rules) {
            double sumXY = 0.0;
            double sumX = 0.0;
            double sumY = 0.0;
            for (var trans : transactions) {
                if (trans.containsAll(rule.getBody())
                        & trans.containsAll(rule.getHead())) {
                    sumXY += 1;
                }
                if (trans.containsAll(rule.getBody())) {
                    sumX += 1;
                }
                if (trans.containsAll(rule.getHead())) {
                    sumY += 1;
                }
            }

            sumXY = sumXY / transactions.size();
            sumX = sumX / transactions.size();
            sumY = sumY / transactions.size();
            double lift = sumXY / (sumX * sumY);
            rule.setLift(lift);
        }
    }

}
