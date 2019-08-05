package itemitem.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Matrix<T> {
    private Map<Integer, Map<Integer, T>> values;

    public Matrix() {
        this.values = new HashMap<>();
    }

    public Matrix(int capacity) {
        this.values = new HashMap<>(capacity);
    }

    public void put(int row, int col, T value) {
        if (!values.containsKey(row)) {
            values.put(row, new HashMap<>());
        }

        values.get(row).put(col, value);
    }

    public T get(int row, int col) {
        return values.get(row).get(col);
    }

    public Set<Integer> keySet() {
        return values.keySet();
    }

    public void printMatrix() {
        LinkedHashMap<Integer, LinkedHashMap<Integer, T>> sorted = new LinkedHashMap<>();
        // sort outer map
        values.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(x -> sorted.put(x.getKey(), new LinkedHashMap<>()));
        // sort inner map
        values.forEach((key, value) -> value.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .forEachOrdered(x -> sorted.get(key).put(x.getKey(), x.getValue())));
        // print sorted map
        sorted.forEach((k1, v1) -> {
            System.out.print(k1 + ": \n\t| ");
            v1.forEach((k2, v2) -> System.out.print(k2 + ": " + v2 + " | "));
            System.out.println();
        });
    }
}
