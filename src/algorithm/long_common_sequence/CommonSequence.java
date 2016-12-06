package algorithm.long_common_sequence;

/**
 * Created by Haochen on 2016/12/4.
 */
public class CommonSequence {
    public static void main(String[] args) {
        char[] str1 = "gouliguojiashengsiyi".toCharArray();
        char[] str2 = "qiyinhuofubiquzhi".toCharArray();
        str1 = "ABCDEFGHIJKLMN".toCharArray();
        str2 = "KLMNJKLMHIJKFGHIDEFGCDEFBCDEABCD".toCharArray();
        str1 = "ABABABCDE".toCharArray();
        str2 = "ABBBBB".toCharArray();
        String seq = commonSequence(str1, str2);
        System.out.println(seq);
    }

    private static String commonSequence(char[] str1, char[] str2) {
        char[] l = str1.length >= str2.length ? str1 : str2;
        char[] s = str1.length < str2.length ? str1 : str2;
        int[][] len = new int[l.length + 1][s.length + 1];
        int maxLen = 0;
        for (int i = l.length - 1; i >= 0; --i) {
            for (int j = s.length - 1; j >= 0; --j) {
                if (l[i] == s[j]) {
                    len[i][j] = len[i + 1][j + 1] + 1;
                } else {
                    if (len[i + 1][j] > len[i][j + 1]) {
                        len[i][j] = len[i + 1][j];
                    } else {
                        len[i][j] = len[i][j + 1];
                    }
                }
                if (len[i][j] > maxLen) {
                    maxLen = len[i][j];
                }
            }
        }

        String result = "";
//        for (int i = 0; maxLen > 0 && i < l.length; ++i) {
//            for (int j = 0; j < s.length; ++j) {
//                if (len[i][j] == maxLen && l[i] == s[j]) {
//                    result = result + l[i];
//                    --maxLen;
//                    break;
//                }
//            }
//        }

        int i = 0, j = 0;
        while (i < l.length && j < s.length) {
            if (l[i] == s[j]) {
                result = result + l[i];
                ++i;
                ++j;
            } else {
                if (len[i][j] == len[i][j + 1]) {
                    ++j;
                } else {
                    ++i;
                }
            }
        }
        return result;
    }
}
