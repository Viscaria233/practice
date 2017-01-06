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
        //�˻������ͣ���С��������
        int type = code1.getType() == Code.PURE_DECIMAL || code2.getType() == Code.PURE_DECIMAL ?
                Code.PURE_DECIMAL : Code.PURE_INTEGER;

        //��ȡ�����Ĳ���
        Code comp1 = code1.compCode();
        Code comp2 = code2.compCode();

        //���������������
        Process process = new Process(Process.MUL, code1, code2);

        //������λ���ϸ�Ȩ-1
        int[] para1 = comp1.intArray();
        int[] para2 = comp2.intArray();
        para1[0] *= -1;
        para2[0] *= -1;

        //�ѷ���λ����Ȩ�Ĳ������������̵�0��1�У����Ҷ���
        process.putArray(para1, 0, process.col - para1.length);
        process.putArray(para2, 1, process.col - para2.length);

        int row = process.row;
        int col = process.col;
        //��ȡ������̵����鷽�����
        int[][] array = process.process;

        //���ҵ�����para2��ÿһλ��para1��һ��һ�д�������
        int r = 2;
        for (int i = col - 1; i >= col - para2.length; --i) {
            for (int j = col - 1; j >= col - para1.length; --j) {
                array[r][j - r + 2] = array[1][i] * array[0][j];
            }
            r++;
        }

        //������ĸ��н�����
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

        //����С��λ����������ӵĽ���ұ�������ȵ�λ�����ѽ�����ʣ���λ��Ϊ��Ч
        for (int i = 0; i < col - para1.length - para2.length + 1; ++i) {
            array[row - 1][i] = Process.NULL;
        }

        //��ȡ���н�������һ�У����з���λ��չ
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

        //�����ս���Բ�����ʽ��������
        process.result = new CompCode(type, process.rowToString(row - 1));

        return process;
    }

    public static Process div(Code code1, Code code2) {
        //�˻������ͣ���С��������
        int type = code1.getType() == Code.PURE_DECIMAL || code2.getType() == Code.PURE_DECIMAL ?
                Code.PURE_DECIMAL : Code.PURE_INTEGER;

        //��ȡ��������ֵ�Ĳ���
        Code comp1 = code1.isNegative() ? code1.compCode().inverseSign() : code1.compCode();
        Code comp2 = code2.isNegative() ? code2.compCode().inverseSign() : code2.compCode();

        //���������������
        Process process = new Process(Process.DIV, code1, code2);

        int[] para1 = comp1.intArray();

        //�ѱ������������������̵�0�У������2�ո������
        process.putArray(para1, 0, 2);
//        process.putArray(para2, 1, 0);

        int row = process.row;
        int col = process.col;
        //��ȡ������̵����鷽�����
        int[][] array = process.process;

        //��������
        int[] para2 = comp2.intArray();
        int[] inv2 = comp2.inverseSign().intArray();
        //������һ�����ӷ�/����
        boolean add = false;

        int move = 0;
        for (int i = 1; i < row; ++i) {
            if (i % 2 == 1) {
                int[] diver = add ? para2 : inv2;
                //�������Ž���һ�У������2�ո��ٶ���move���ո��������Ʋ�λ
                process.putArray(diver, i, 2 + move);

                //����߿ո����������ƵĲ�λ�����������2�ո�
                int fill = diver[0];
                for (int j = 0; j < move; ++j) {
                    array[i][j + 2] = fill;
                }
                move++;
            } else {
                int[] last1 = array[i - 1];
                int[] last2 = array[i - 2];
                int carry = 0;
                //���Ҹ���2�ո�
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