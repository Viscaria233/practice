package long_common_sequence;

/**
 * Created by Haochen on 2016/12/4.
 */
public class CommonSequence {
    public static void main(String[] args) {
        char[] str1 = "gouliguojiashengsiyi".toCharArray();
        char[] str2 = "qiyinhuofubiquzhi".toCharArray();
        String seq = commonSequence(str1, str2);
        System.out.println(seq);
    }

    private static String commonSequence(char[] str1, char[] str2) {
        char[] shorter = str1.length < str2.length ? str1 : str2;
        char[] longer = str1.length >= str2.length ? str1 : str2;
        int[] len = new int[shorter.length];
        int[] next = new int[shorter.length];
        int[] same = new int[shorter.length];
        for (int i = 0; i < same.length; i++) {
            same[i] = longer.length;
        }
        for (int i = longer.length - 1; i >= 0; --i) {
            if (longer[i] == shorter[shorter.length - 1]) {
                same[shorter.length - 1] = i;
                len[shorter.length - 1] = 1;
            }
        }
        for (int i = shorter.length - 2; i >= 0; --i) {
            for (int j = same[i + 1] - 1; j >= 0; --j) {
                if (longer[j] == shorter[i]) {
                    same[i] = j;
                    len[i] = len[i + 1] + 1;
                }
            }
        }
        return null;
    }
}
