package test;

import code.Code;
import code.ValueCode;
import code.calculate.Calculator;
import code.calculate.Process;

/**
 * Created by Haochen on 2017/1/5.
 */
public class Test {
    public static void main(String[] args) {
        Code[] codes = {
                new ValueCode("0.101"),
                new ValueCode("-0.11"),
                new ValueCode("-1011"),
                new ValueCode("1101"),
                new ValueCode("-0.101001"),
                new ValueCode("0.11101"),
                new ValueCode("0.101001"),
                new ValueCode("0.111"),
                new ValueCode("-0.111"),
        };
        for (Code c : codes) {
            c.print();
        }

        Process[] processes = {
                Calculator.add(codes[0], codes[1]),
//                Calculator.add(codes[2], codes[3]),
//                Calculator.sub(codes[0], codes[1]),
                Calculator.sub(codes[2], codes[3]),
//                Calculator.mul(codes[0], codes[1]),
//                Calculator.mul(codes[2], codes[3]),
                Calculator.mul(codes[4], codes[5]),
//                Calculator.mul(codes[5], codes[4]),
                Calculator.div(codes[6], codes[7]),
                Calculator.div(codes[6], codes[8]),
        };
        for (Process p : processes) {
            p.print();
        }
    }
}
