package algorithm.queen;

/**
 * Created by Haochen on 2016/12/3.
 */
public class QueenAllResult extends Queen {
    public static void main(String[] args) {
        int n = 8;
        QueenAllResult a = new QueenAllResult();
        a.queen(n);
        System.out.println("count = " + a.count);
    }

    int count = 0;

    @Override
    protected boolean queen(int[] result, int n) {
        while (true) {
            result[n]++;
            if (result[n] >= result.length) {
                result[n] = -1;
                return false;
            } else if (valid(result, n)) {
                if (n == result.length - 1) {
                    display(result);
                    System.out.println("-----------------------------------");
                    count++;
                    result[n] = -1;
                    return false;
                } else if (queen(result, n + 1)) {
                    return true;
                }
            }
        }
    }
}
