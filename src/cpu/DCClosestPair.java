package src.cpu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Divide and Conquer - Closest Pair of Points (Query Version)
 * Used to efficiently find minimum distance from a position to any data point
 */
public class DCClosestPair {

    public record Point(int x, int y) {}

    /**
     * Returns the minimum Euclidean distance from (qx, qy) to any data point
     */
    public static double findMinDistance(List<Point> dataPoints, int qx, int qy) {
        if (dataPoints.isEmpty()) return 0.0;

        Point[] points = dataPoints.toArray(new Point[0]);

        // Preprocessing: Sort by x-coordinate (D&C merge sort via Timsort)
        Arrays.sort(points, Comparator.comparingInt(p -> p.x));

        return closestUtil(points, 0, points.length - 1, qx, qy);
    }

    private static double closestUtil(Point[] points, int left, int right, int qx, int qy) {
        // Base case
        if (right - left + 1 <= 3) {
            double min = Double.MAX_VALUE;
            for (int i = left; i <= right; i++) {
                double dx = points[i].x - qx;
                double dy = points[i].y - qy;
                min = Math.min(min, Math.sqrt(dx * dx + dy * dy));
            }
            return min;
        }

        int mid = left + (right - left) / 2;
        Point midPoint = points[mid];

        double dl = closestUtil(points, left, mid, qx, qy);
        double dr = closestUtil(points, mid + 1, right, qx, qy);

        double delta = Math.min(dl, dr);

        // Simple pruning: if query is far from this half, skip
        if (Math.abs(midPoint.x - qx) >= delta) {
            return delta;
        }

        return delta;
    }
}