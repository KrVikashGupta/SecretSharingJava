

import java.util.*;

public class Secret_Sharing {

    static class Point {
        long x, y;
        Point(long x, long y) {
            this.x = x;
            this.y = y;
        }
    }

    public static long interpolateAtZero(List<Point> points) {
        double secret = 0;
        int k = points.size();

        for (int i = 0; i < k; i++) {
            double xi = points.get(i).x;
            double yi = points.get(i).y;

            double term = yi;
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    double xj = points.get(j).x;
                    term *= (0 - xj) / (xi - xj);
                }
            }
            secret += term;
        }

        return Math.round(secret);
    }

    public static List<List<Point>> combinations(Point[] points, int k) {
        List<List<Point>> result = new ArrayList<>();
        combineHelper(points, k, 0, new ArrayList<>(), result);
        return result;
    }

    private static void combineHelper(Point[] points, int k, int start, List<Point> current, List<List<Point>> result) {
        if (current.size() == k) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = start; i < points.length; i++) {
            current.add(points[i]);
            combineHelper(points, k, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }

    public static void main(String[] args) {
        int n = 6;
        int k = 5;

        Point[] points = {
            new Point(1, 1234),
            new Point(2, 2345),
            new Point(3, 3456),
            new Point(4, 4567),
            new Point(5, 5678),
            new Point(6, 10000) // bomb
        };

        Map<Long, Integer> freqMap = new HashMap<>();
        List<List<Point>> allCombos = combinations(points, k);

        for (List<Point> combo : allCombos) {
            long secret = interpolateAtZero(combo);
            freqMap.put(secret, freqMap.getOrDefault(secret, 0) + 1);
        }

        long correctSecret = 0;
        int maxFreq = 0;
        for (Map.Entry<Long, Integer> entry : freqMap.entrySet()) {
            if (entry.getValue() > maxFreq) {
                maxFreq = entry.getValue();
                correctSecret = entry.getKey();
            }
        }

        System.out.println("The reconstructed secret is: " + correctSecret);
    }
}
