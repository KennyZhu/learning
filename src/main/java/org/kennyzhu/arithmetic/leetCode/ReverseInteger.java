package org.kennyzhu.arithmetic.leetCode;

import lombok.extern.slf4j.Slf4j;

/**
 * 整数翻转
 * Created by yanlongzhu on 2016/11/23.
 */
@Slf4j
public class ReverseInteger {

    /**
     * @param x
     * @return
     */
    public static int reverse2(int x) {
        int result = 0;
        while (x != 0) {
            int tail = x % 10;//取余
            int newResult = result * 10 + tail;
            if ((newResult - tail) / 10 != result) {
                return 0;
            }
            result = newResult;
            x = x / 10;
        }

        return result;
    }

    /**
     * @param x
     * @return
     */
    public static int reverse(int x) {
        if (x == 0) {
            return 0;
        }
        String str = x + "";
        char[] chars = str.toCharArray();
        int begin = 0;
        int end = chars.length;
        boolean isNev = false;
        if (chars[0] == '-') {
            isNev = true;
            begin = begin + 1;
        }
        int result = 0;
        int j = 0;
        for (int i = begin; i < end; i++) {
            Integer index = Integer.parseInt(chars[i] + "");
            Long jc = (index * jc(j));
            if (jc > Integer.MAX_VALUE || (result + jc) > Integer.MAX_VALUE) {
                return 0;
            }
            result = result + jc.intValue();
            j++;
        }
        return isNev ? -1 * result : result;

    }

    private static long jc(int input) {
        if (input == 0) {
            return 1;
        }
        long result = 10;
        for (int i = 1; i < input; i++) {
            if (result * 10 > Integer.MAX_VALUE) {
                return 0;
            }
            result = result * 10;
        }
        return result;
    }

    public static int reverse3(int input) {
        if (input == 0) {
            return 0;
        }
        int result = 0;
        while (input != 0) {
            //每循环一次，result*10
            int tail = input % 10;
            result = result * 10 + tail;
            input = input / 10;
        }
        return result;

    }

    public static void main(String[] args) {
        long begin = System.currentTimeMillis();
        int result = ReverseInteger.reverse3(120);
        System.out.println(result);
    }
}

