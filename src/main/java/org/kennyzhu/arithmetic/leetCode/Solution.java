package org.kennyzhu.arithmetic.leetCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * desc:无重复最长连续字符串
 * author: yanlongzhu
 * date:2020/8/4.
 */
public class Solution {
    private static final Logger LOGGER = LoggerFactory.getLogger(Solution.class);

    public static int lengthOfLongestSubstring(String input) {
        //<字符，index>
        Map<Character, Integer> map = new HashMap<>();
        int res = 0, index = 0, left = 0, length = input.length();
        while (index < length) {
            // map put时返回旧值
            Integer n = map.put(input.charAt(index), index);
            if (n != null && n >= left) {
                // 判断旧值是否在左指针左边
                res = Math.max(res, index - left);
                left = n + 1;
            } else if (index == length - 1) {
                // 当遍历到最后一个字符时，计算最大子字符串长度
                res = Math.max(res, index - left + 1);
            }
            index++;
        }
        return res;
    }

    public static String cal(String input) {
        Map<Character, Integer> map = new HashMap<>();
        int leftPoint = 0;
        int begin = 0;
        int index = 0;
        int result = 0;
        while (index < input.length()) {
            Integer weizhi = map.put(input.charAt(index), index);

            if (result < index - leftPoint) {//如果当前最长，起始位置为leftPoint
                begin = leftPoint;
            }
            LOGGER.info("Result=" + result + " index=" + index + " leftPoint=" + leftPoint + " begin=" + begin);
            //返回旧的位置，leftPoint从旧位置往后计算
            if (weizhi != null && weizhi >= leftPoint) {
                result = Math.max(result, index - leftPoint);
                leftPoint = weizhi + 1;
            } else if (index == input.length() - 1) {
                result = Math.max(result, index - leftPoint + 1);
            }
            LOGGER.info("###Result=" + result + " index=" + index + " leftPoint=" + leftPoint + " begin=" + begin);
            index++;
        }
        return input.substring(begin, begin + result);
    }

    public static void main(String[] args) {
        LOGGER.info("Result = " + Solution.cal("abcdefga"));
    }
}
