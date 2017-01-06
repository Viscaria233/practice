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
     * 获取float的IEEE754存储格式
     * @param value
     * @return
     */
    public String floatToIEEE754(float value) {
        //符号位
        String sflag = value > 0 ? "0" : "1";

        //整数部分
        int fz = (int) Math.floor(value);
        //整数部分二进制
        String fzb = Integer.toBinaryString(fz);

        //小数部分，格式： 0.02
        String valueStr = String.valueOf(value);
        String fxStr = "0" + valueStr.substring(valueStr.indexOf("."));
        float fx = Float.parseFloat(fxStr);
        //小数部分二进制
        String fxb = toBin(fx);

        //指数位
        String e = Integer.toBinaryString(127 + fzb.length() - 1);
        //尾数位
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
        int MAX = 24; // 最多8位

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
