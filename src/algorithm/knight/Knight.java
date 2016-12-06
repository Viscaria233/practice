package algorithm.knight;

/**
 * Created by Haochen on 2016/12/3.
 */
public class Knight {
    public static void main(String[] args) {
        int n = 5;
        int x = 0, y = 0;
        Knight k = new Knight();
        int[][] result = k.knight(n, x, y);
        if (result != null) {
            k.display(result);
        } else {
            System.out.println("No solution");
        }
    }

    int[] dx = {-2, -2, -1, 1, 2, 2, 1, -1};
    int[] dy = {-1, 1, 2, 2, 1, -1, -2, -2};
    int step = 0;

    protected int[][] knight(int n, int x, int y) {
        int[][] result = new int[n][n];
        if (knight(result, x, y)) {
            return result;
        } else {
            return null;
        }
    }

    protected boolean knight(int[][] result, int x, int y) {
        result[x][y] = ++step;
        if (step == result.length * result.length) {
            return true;
        }
        for (int i = 0; i < 8; ++i) {
            int xx = x + dx[i];
            int yy = y + dy[i];
            if (xx < result.length && xx >= 0 && yy < result.length && yy >= 0) {
                if (result[xx][yy] == 0) {
                    if (knight(result, xx, yy)) {
                        return true;
                    } else {
                        result[xx][yy] = 0;
                        --step;
                    }
                }
            }
        }
        return false;
    }

    protected void display(int[][] result) {
        for (int i = 0; i < result.length; ++i) {
            for (int j = 0; j < result.length; ++j) {
                System.out.printf("%4d", result[i][j]);
            }
            System.out.println();
        }
    }
}
