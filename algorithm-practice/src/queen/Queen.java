package queen;

/**
 * Created by Haochen on 2016/12/3.
 */
public class Queen {
    public static void main(String[] args) {
        int n = 8;
        Queen q = new Queen();
        int[] result = q.queen(n);
        if (result != null) {
            q.display(result);
        } else {
            System.out.println("No solution");
        }
    }

    protected int[] queen(int n) {
        int[] result = new int[n];
        for (int i = 0; i < n; ++i) {
            result[i] = -1;
        }
        if (queen(result, 0)) {
            return result;
        } else {
            return null;
        }
    }

    protected boolean queen(int[] result, int n) {
        while (true) {
            result[n]++;
            if (result[n] >= result.length) {
                result[n] = -1;
                return false;
            } else if (valid(result, n) && (n == result.length - 1 || queen(result, n + 1))) {
                return true;
            }
        }
    }

    protected boolean valid(int[] result, int n) {
        for (int i = 0; i < n; ++i) {
            if (result[i] == result[n]
                    || i + result[i] == n + result[n]
                    || i - result[i] == n - result[n]) {
                return false;
            }
        }
        return true;
    }

    public void display(int[] result) {
        for (int i : result) {
            for (int j = 0; j < result.length; ++j) {
                if (j == i) {
                    System.out.print("¡ñ");
                } else {
                    System.out.print("©à");
                }
            }
            System.out.println();
        }
    }
}
