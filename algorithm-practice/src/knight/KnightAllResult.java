package knight;

/**
 * Created by Haochen on 2016/12/3.
 */
public class KnightAllResult extends Knight {
    public static void main(String[] args) {
        int n = 5;
        int x = 0, y = 0;
        KnightAllResult k = new KnightAllResult();
        k.knight(n, x, y);
        System.out.println("count = " + k.count);
    }

    int count = 0;

    @Override
    protected boolean knight(int[][] result, int x, int y) {
        result[x][y] = ++step;
        if (step == result.length * result.length) {
            display(result);
            System.out.println("-------------------------------");
            count++;
            return false;
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
}
