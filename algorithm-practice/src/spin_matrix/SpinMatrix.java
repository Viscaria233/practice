package spin_matrix;

/**
 * Created by Haochen on 2016/12/2.
 */
public class SpinMatrix {
    public static void main(String[] args) {
        int n = 5;
        int[][] matrix = getMatrix(n);
        for (int[] i : matrix) {
            for (int j : i) {
                System.out.printf("%5d", j);
            }
            System.out.println();
        }
    }

    private static int[][] getMatrix(int n) {
        int[][] matrix = new int[n][n];
        int[] dx = {0, 1, 0, -1};
        int[] dy = {1, 0, -1, 0};
        int turn = 0;
        int x = 0, y = 0;
        for (int i = 1; i <= n * n; ++i) {
            if (x < n && y < n && x >= 0 && y >= 0 && matrix[x][y] == 0) {
                matrix[x][y] = i;
            } else {
                x -= dx[turn % 4];
                y -= dy[turn % 4];
                ++turn;
                --i;
            }
            x += dx[turn % 4];
            y += dy[turn % 4];
        }
        return matrix;
    }
}
