package itemitem.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Parser {
    public ParseResult parseFile(String filepath, String delimiter) {
        Path file = Paths.get(filepath);

        HashMap<Integer, HashMap<Integer, Double>> result = new HashMap<>();
        Set<Integer> itemIds = new HashSet<>();
        List<String> lines;

        try {
            lines = Files.readAllLines(file);
        } catch (IOException e) {
            throw new RuntimeException("Could not parse the given file");
        }

        for (String line : lines) {
            String[] split = line.split(delimiter);

            int userId;
            int itemId;
            double rating;

            try {
                userId = Integer.parseInt(split[0]);
                itemId = Integer.parseInt(split[1]);
                rating = Double.parseDouble(split[2]);

                if (!result.containsKey(userId)) {
                    result.put(userId, new HashMap<>());
                }

                itemIds.add(itemId);
                result.get(userId).put(itemId, rating);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Number could not be formatted");
            }
        }

        return new ParseResult(result, itemIds);
    }

    @Getter
    @AllArgsConstructor
    public class ParseResult {
        private HashMap<Integer, HashMap<Integer, Double>> result;
        private Set<Integer> itemIds;
    }
}
