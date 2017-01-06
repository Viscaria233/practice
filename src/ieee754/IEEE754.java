package ieee754;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Haochen on 2016/12/31.
 */
public class IEEE754 {
    public static void main(String[] args) {
        float[] f = {100.625f, 24.1f, 3.25f, 0.125f, 1f};
        IEEE754 ieee = new IEEE754();

        for (float i : f) {
            System.out.println(i + ": " + ieee.floatToIEEE754(i));
        }
    }

    /**
     * ��ȡfloat��IEEE754�洢��ʽ
     * @param value
     * @return
     */
    public String floatToIEEE754(float value) {
        //����λ
        String sflag = value > 0 ? "0" : "1";

        //��������
        int fz = (int) Math.floor(value);
        //�������ֶ�����
        String fzb = Integer.toBinaryString(fz);

        //С�����֣���ʽ�� 0.02
        String valueStr = String.valueOf(value);
        String fxStr = "0" + valueStr.substring(valueStr.indexOf("."));
        float fx = Float.parseFloat(fxStr);
        //С�����ֶ�����
        String fxb = toBin(fx);

        //ָ��λ
        String e = Integer.toBinaryString(127 + fzb.length() - 1);
        //β��λ
        String m = fzb.substring(1) + fxb;

        String result = sflag + e + m;

        while (result.length() < 32) {
            result += "0";
        }
        if (result.length() > 32) {
            result = result.substring(0, 32);
        }
        StringBuilder builder = new StringBuilder(result);
        builder.insert(1, ' ');
        builder.insert(10, ' ');
        return builder.toString();
    }

    private String toBin(float f) {
        List<Integer> list = new ArrayList<>();
        Set<Float> set = new HashSet<>();
        int MAX = 24; // ���8λ

        int bits = 0;
        while (true) {
            f = calc(f, set, list);
            bits++;
            if (f == -1 || bits >= MAX)
                break;
        }
        String result = "";
        for (Integer i : list) {
            result += i;
        }
        return result;
    }

    private float calc(float f, Set<Float> set, List<Integer> list) {
        if (f == 0 || set.contains(f))
            return -1;
        float t = f * 2;
        if (t >= 1) {
            list.add(1);
            return t - 1;
        } else {
            list.add(0);
            return t;
        }
    }
}
