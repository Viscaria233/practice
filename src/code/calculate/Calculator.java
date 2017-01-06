package code.calculate;

import code.Code;
import code.CompCode;

/**
 * Created by Haochen on 2017/1/5.
 */
public class Calculator {
    public static Process add(Code code1, Code code2) {
        int type = code1.getType() == Code.PURE_DECIMAL || code2.getType() == Code.PURE_DECIMAL ?
                Code.PURE_DECIMAL : Code.PURE_INTEGER;

        Code comp1 = code1.compCode();
        Code comp2 = code2.compCode();
        int len = comp1.length() > comp2.length() ? comp1.length(): comp2.length();
        Process process = new Process(Process.ADD, code1, code2);
        comp1.expand(len);
        comp2.expand(len);

        int[][] array = process.process;
        int[] adder1 = comp1.intArray();
        int[] adder2 = comp2.intArray();
        process.putArray(adder1, 0, process.col - adder1.length);
        process.putArray(adder2, 1, process.col - adder2.length);

        int carry = 0;
        for (int i = process.col - 1; i >= 0; --i) {
            if (array[0][i] != Process.NULL && array[1][i] != Process.NULL) {
                int n = array[0][i] + array[1][i] + carry;
                array[2][i] = n % 2;
                carry = n / 2;
            }
        }

        process.result = new CompCode(type, process.rowToString(2));

        return process;
    }

    public static Process sub(Code code1, Code code2) {
        Code comp1 = code1.compCode();
        Code comp2 = code2.compCode();
        Process process = new Process(Process.SUB, code1, code2);
        Process add = add(comp1, comp2.inverseSign());
        process.row = add.row;
        process.col = add.col;
        process.process = add.process;
        process.result = add.result;
        return process;
    }

    public static Process mul(Code code1, Code code2) {
        //乘积的类型，纯小数或纯整数
        int type = code1.getType() == Code.PURE_DECIMAL || code2.getType() == Code.PURE_DECIMAL ?
                Code.PURE_DECIMAL : Code.PURE_INTEGER;

        //获取两数的补码
        Code comp1 = code1.compCode();
        Code comp2 = code2.compCode();

        //用来保存运算过程
        Process process = new Process(Process.MUL, code1, code2);

        //给符号位乘上负权-1
        int[] para1 = comp1.intArray();
        int[] para2 = comp2.intArray();
        para1[0] *= -1;
        para2[0] *= -1;

        //把符号位带负权的补码放入运算过程第0、1行，靠右对齐
        process.putArray(para1, 0, process.col - para1.length);
        process.putArray(para2, 1, process.col - para2.length);

        int row = process.row;
        int col = process.col;
        //获取保存过程的数组方便操作
        int[][] array = process.process;

        //从右到左用para2的每一位乘para1，一行一行存入数组
        int r = 2;
        for (int i = col - 1; i >= col - para2.length; --i) {
            for (int j = col - 1; j >= col - para1.length; --j) {
                array[r][j - r + 2] = array[1][i] * array[0][j];
            }
            r++;
        }

        //将上面的各行结果相加
        int carry = 0;
        for (int i = col - 1; i >= 0; --i) {
            int sum = 0;
            for (int j = 2; j < row - 1; ++j) {
                if (array[j][i] != Process.NULL) {
                    sum += array[j][i];
                }
            }
            sum += carry;
            carry = sum / 2;
            array[row - 1][i] = sum % 2;
        }

        //计算小数位数，上面相加的结果右边留下相等的位数，把结果左边剩余的位设为无效
        for (int i = 0; i < col - para1.length - para2.length + 1; ++i) {
            array[row - 1][i] = Process.NULL;
        }

        //获取存有结果的最后一行，进行符号位扩展
        int[] lastRow = array[row - 1];
        boolean flag;
        do {
            flag = false;
            for (int i = 0; i < lastRow.length - 1; ++i) {
                if (lastRow[i + 1] == -1) {
                    if (lastRow[i] == 0) {
                        lastRow[i] = -1;
                        lastRow[i + 1] = 1;
                        flag = true;
                    } else if (lastRow[i] == 1) {
                        lastRow[i] = 0;
                        lastRow[i + 1] = 1;
                        flag = true;
                    }
                }
            }
        } while (flag);

        //把最终结果以补码形式保存下来
        process.result = new CompCode(type, process.rowToString(row - 1));

        return process;
    }

    public static Process div(Code code1, Code code2) {
        //乘积的类型，纯小数或纯整数
        int type = code1.getType() == Code.PURE_DECIMAL || code2.getType() == Code.PURE_DECIMAL ?
                Code.PURE_DECIMAL : Code.PURE_INTEGER;

        //获取两数绝对值的补码
        Code comp1 = code1.isNegative() ? code1.compCode().inverseSign() : code1.compCode();
        Code comp2 = code2.isNegative() ? code2.compCode().inverseSign() : code2.compCode();

        //用来保存运算过程
        Process process = new Process(Process.DIV, code1, code2);

        int[] para1 = comp1.intArray();

        //把被除数补码放入运算过程第0行，左边留2空格靠左对齐
        process.putArray(para1, 0, 2);
//        process.putArray(para2, 1, 0);

        int row = process.row;
        int col = process.col;
        //获取保存过程的数组方便操作
        int[][] array = process.process;

        //除数反号
        int[] para2 = comp2.intArray();
        int[] inv2 = comp2.inverseSign().intArray();
        //控制下一步做加法/减法
        boolean add = false;

        int move = 0;
        for (int i = 1; i < row; ++i) {
            if (i % 2 == 1) {
                int[] diver = add ? para2 : inv2;
                //被除数放进下一行，左边留2空格，再多留move个空格用于右移补位
                process.putArray(diver, i, 2 + move);

                //在左边空格中填入右移的补位，最左边留出2空格
                int fill = diver[0];
                for (int j = 0; j < move; ++j) {
                    array[i][j + 2] = fill;
                }
                move++;
            } else {
                int[] last1 = array[i - 1];
                int[] last2 = array[i - 2];
                int carry = 0;
                //左右各留2空格
                for (int j = col - 3; j >= 2; --j) {
                    int n = carry + (last1[j] == Process.NULL ? 0 : last1[j])
                            + (last2[j] == Process.NULL ? 0 : last2[j]);
                    array[i][j] = n % 2;
                    carry = n / 2;
                }
                array[i][col - 1] = 1 - array[i][2];
                add = array[i][2] == 1;
            }
        }

        String quotient = "";
        for (int i = 2; i < row; i += 2) {
            quotient += array[i][col - 1];
        }
        process.result = new CompCode(type, quotient);
        if (code1.isNegative() ^ code2.isNegative()) {
            process.result = process.result.inverseSign();
        }

        String remainder = "";
        for (int i = 2; i <= col - 3; ++i) {
            remainder += array[row - 1][i];
        }
        process.remainder = new CompCode(type, remainder);


        return process;
    }
}