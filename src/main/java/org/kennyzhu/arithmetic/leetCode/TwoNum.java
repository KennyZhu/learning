package org.kennyzhu.arithmetic.leetCode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yanlongzhu on 2016/11/23.
 */
public class TwoNum {
    public static int[] twoNum(int[] input, int target) {
        double half = target / 2;
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < input.length; i++) {
            int left = input[i];
            int sub;//差值计算
            if (left < half) {
                sub = target - left;//存大的一半
            } else {
                sub = left;
            }
            if (map.containsKey(sub)) {
                if ((input[map.get(sub)] + input[i] == target)) {
                    return new int[]{map.get(sub), i};
                }
            } else {
                map.put(sub, i);
            }

        }

        return null;
    }

    public static void main(String[] args) {
        int[] input = new int[]{81, 19, 29, 19, 7, 1, 0, -1, 0, 200, 67, 55, 34, 30, 38, 78};
        int target = -10;
        long beginTime = System.currentTimeMillis();
        int[] output = TwoNum.twoNum(input, target);
        System.out.println("######Cost:" + (System.currentTimeMillis() - beginTime));
        if (output != null) {
            for (int i : output) {
                System.out.println(i);
            }
        }
    }

}
