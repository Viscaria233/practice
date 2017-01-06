package code.calculate;

import code.Code;

/**
 * Created by Haochen on 2017/1/5.
 */
public class Process {
    public static final int ADD = 1;
    public static final int SUB = 2;
    public static final int MUL = 3;
    public static final int DIV = 4;

    public static final int NULL = -2;

    private Code code1;
    private Code code2;
    Code result;
    Code remainder;
    private int type;
    int[][] process;
    int row;
    int col;

    public Process(int type, int row, int col) {
        this.type = type;
        this.row = row;
        this.col = col;
        init();
    }

    public Process(int type, int[][] process){
        this.type = type;
        this.process = process;
        this.row = process.length;
        this.col = process[0].length;
    }

    public Process(int type, Code code1, Code code2) {
        this.type = type;

        this.code1 = code1;
        this.code2 = code2;
        Code comp1 = code1.compCode();
        Code comp2 = code2.compCode();
        int len1 = comp1.length();
        int len2 = comp2.length();

        switch (type) {
            case MUL:
                this.row = len2 + 3;
                this.col = len1 + len2;
                break;
            case DIV:
                this.row = 1 + 2 * (Math.abs(len1 - len2) + 1);
                this.col = 4 + (len1 > len2 ? len1 : len2);
                break;
            default:
                this.row = 3;
                this.col = (len1 > len2 ? len1 : len2) + 1;
                break;
        }
        init();
    }

    private void init() {
        this.process = new int[row][col];
        for (int i = 0; i < row; ++i) {
            for (int j = 0; j < col; ++j) {
                this.process[i][j] = NULL;
            }
        }
    }

    public void putArray(int[] array, int toRow, int beginAtCol) {
        for (int i : array) {
            this.process[toRow][beginAtCol] = i;
            beginAtCol++;
        }
    }


    public void print() {
        String sep = "";
        for (int j = 0; j < 5 * col / 2; ++j) {
            sep += "¡ª";
        }

        String ope = "";
        if (type == ADD) {
            ope = "£«";
        } else
        if (type == SUB) {
            ope = "£­";
        } else
        if (type == MUL) {
            ope = "¡Á";
        } else
        if (type == DIV) {
            ope = "¡Â";
        }

        String longSep = "¡ª¡ª¡ª¡ª¡ª¡ª¡ª¡ª¡ª¡ª¡ª¡ª¡ª¡ª¡ª¡ª¡ª¡ª¡ª¡ª¡ª¡ª¡ª¡ª¡ª¡ª¡ª¡ª¡ª¡ª¡ª¡ª¡ª¡ª";
        System.out.println(longSep);
        System.out.println(code1.getCode() + ope + code2.getCode());
        System.out.println("value: " + code1.valueCode().getCode() + ope + code2.valueCode().getCode());
        System.out.println("dec: " + code1.value() + ope + code2.value());

        boolean addInDiv = false;
        for (int i = 0; i < row; ++i) {
            for (int j = 0; j < col; ++j) {
                if (process[i][j] == NULL) {
                    System.out.printf("%5s", "");
                } else if (process[i][j] < 0) {
                    System.out.printf(" (%d) ", -process[i][j]);
                } else {
                    System.out.printf("  %d  ", process[i][j]);
                }
            }
            System.out.println();
            if (type == DIV) {
                if (i % 2 == 0) {
                    if (i > 0) {
                        for (int j = 0; j < col; ++j) {
                            if (process[i - 1][j] != NULL) {
                                addInDiv = process[i - 1][j] == 1;
                                break;
                            }
                        }
                    }
                } else {
                    if (addInDiv) {
                        System.out.print("£«");
                    } else {
                        System.out.print("£­");
                    }
                    System.out.println(sep);
                }
            } else {
                if (i == 1) {
                    if (type == ADD) {
                        System.out.print("£«");
                    } else
                    if (type == SUB) {
                        System.out.print("£­");
                    } else
                    if (type == MUL) {
                        System.out.print("¡Á");
                    }
                    System.out.println(sep);
                } else if (i == row - 2) {
                    if (type == MUL) {
                        System.out.print("£«");
                    } else {
                        System.out.printf("¡ª");
                    }
                    System.out.println(sep);
                }
            }
        }

        System.out.println("result:");
        if (result != null) {
            result.print();
        }
        if (type == DIV) {
            System.out.println();
            System.out.println("remainder:");
            if (remainder != null) {
                remainder.print();
            }
        }
        System.out.println(longSep);
    }

    public Code getResult() {
        return result;
    }

    public Code getRemainder() {
        return remainder;
    }

    public String rowToString(int row) {
        String str = "";
        int[] array = this.process[row];
        for (int i : array) {
            if (i != NULL) {
                str += Math.abs(i);
            }
        }
        return str;
    }
}
