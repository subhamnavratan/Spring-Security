package com.Subham.PRASAG.utils;

import java.util.*;

public class RankUtils {

    private RankUtils() {} // utility class (no object)

    public static Map<String, Integer> calculateRanks(Map<String, Integer> points) {

        // Sort unique points in descending order
        List<Integer> sortedUniquePoints = points.values().stream()
                .distinct()
                .sorted(Comparator.reverseOrder())
                .toList();

        // point -> rank
        Map<Integer, Integer> rankMap = new HashMap<>();
        int rank = 1;

        for (int p : sortedUniquePoints) {
            rankMap.put(p, rank);
            rank++;
        }

        // candidate -> rank
        Map<String, Integer> result = new HashMap<>();
        points.forEach((candidate, score) ->
                result.put(candidate, rankMap.get(score))
        );

        return result;
    }
}

