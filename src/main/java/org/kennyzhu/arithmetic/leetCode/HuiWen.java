package org.kennyzhu.arithmetic.leetCode;

import lombok.extern.slf4j.Slf4j;

/**
 * desc:找到最长回文子串
 * author: yanlongzhu
 * date:2020/8/16.
 */
@Slf4j
public class HuiWen {

    /**
     * 暴力解法，时间复杂度为O(n^3)
     *
     * @param input
     * @return
     */
    public static String cal(String input) {
        if (input.length() < 2) {
            return null;
        }
        int maxSize = 1;
        int begin = 0;
        int length = input.length();
        char[] chars = input.toCharArray();
        for (int left = 0; left < length - 1; left++) {
            for (int right = left + 1; right < length; right++) {
                if (right - left + 1 > maxSize && checkIfHuiWen(chars, left, right)) {
                    maxSize = right - left + 1;
                    begin = left;
                }
            }
        }
        return input.substring(begin, begin + maxSize);

    }

    /**
     * 判断是否为回文
     *
     * @param chars
     * @param left
     * @param right
     * @return
     */
    public static boolean checkIfHuiWen(char[] chars, int left, int right) {
        while (left < right) {
            if (chars[left] != chars[right]) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    public static void main(String[] args) {
        String input = "abbacab";
        log.info(cal(input));

    }
}
