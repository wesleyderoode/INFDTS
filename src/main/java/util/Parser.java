package util;

import lombok.AllArgsConstructor;

import useritem.model.Rating;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Parser {
    public ParseResult parseFile(String filepath, String delimiter) {
        Path file = Paths.get(filepath);

        if (!Files.exists(file)) {
            throw new ParseException(String.format("File with path '%s' cannot be found.", filepath));
        }

        Map<Integer, Set<Rating>> result = new HashMap<>();
        Set<String> failedToParse = new HashSet<>();
        List<String> lines;

        try {
            lines = Files.readAllLines(file);
        } catch (IOException e) {
            throw new ParseException(String.format("%s cannot be parsed.", file));
        }

        for (String line : lines) {
            if (line.contains("userId")) continue;

            String[] split = line.split(delimiter);

            int userId;
            int itemId;
            double rating;

            try {
                userId = Integer.parseInt(split[0]);
                itemId = Integer.parseInt(split[1]);
                rating = Double.parseDouble(split[2]);

                if (!result.containsKey(userId)) {
                    result.put(userId, new HashSet<>());
                }

                result.get(userId).add(new Rating(itemId, rating));
            } catch (NumberFormatException e) {
                failedToParse.add(line);
            }
        }

        return new ParseResult(result, failedToParse);
    }

    @AllArgsConstructor
    public class ParseResult {
        private Map<Integer, Set<Rating>> userRatings;
        private Set<String> failedToParse;

        public Map<Integer, Set<Rating>> getUserRatings() {
            return userRatings;
        }

        public Set<String> getFailedToParse() {
            return Collections.unmodifiableSet(failedToParse);
        }
    }

    class ParseException extends RuntimeException {
        ParseException(String message) {
            super(message);
        }
    }
}
