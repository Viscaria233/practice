package algorithm.long_increasing_subsequence;

/**
 * Created by Haochen on 2016/12/4.
 */
public class SubSequence {
    public static void main(String[] args) {
        int[] seq = {0, 4, 8, 3, 4, 2, 6, 7, 5, 1, 6, 5, 6, 7, 6, 2, 7, 5, 7, 5, 4, 8, 9, 6};
        int[] sub = subSequence(seq);
        for (int i : sub) {
            System.out.printf("%4d", i);
        }
        System.out.println();
    }

    private static int[] subSequence(int[] seq) {
        int[] len = new int[seq.length];
        int[] next = new int[seq.length];
        len[len.length - 1] = 1;
        for (int i = seq.length - 2; i >= 0; --i) {
            for (int j = i + 1; j < seq.length; ++j) {
                if (seq[j] > seq[i] && len[j] > len[i]) {
                    len[i] = len[j];
                    next[i] = j;
                }
            }
            len[i]++;
        }

        int start = 0;
        for (int i = 1; i < len.length; ++i) {
            if (len[i] > len[start]) {
                start = i;
            }
        }
        int[] result = new int[len[start]];
        int n = start;
        for (int i = 0; i < result.length; ++i) {
            result[i] = seq[n];
            n = next[n];
        }
        return result;
    }
}
