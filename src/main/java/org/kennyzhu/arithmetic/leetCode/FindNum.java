package org.kennyzhu.arithmetic.leetCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yanlongzhu on 2016/12/5.
 */
public class FindNum {

    /**
     * 找到缺失的数据
     * @param input
     * @return
     */
    public static List find(int[] input) {
        if (input == null || input.length == 0) {
            return null;
        }

        int length = input.length;
        Map<Integer, Integer> hashMap = new HashMap<>(length);

        for (int i = 0; i < length; i++) {
            hashMap.put(input[i], input[i]);
        }
        List<Integer> arr = new ArrayList<>();
        for (int i = 1; i <= length; i++) {
            if (!hashMap.containsKey(i)) {
                arr.add(i);
            }
        }
        return arr;
    }

    public static void main(String[] args) {
        int[] input = new int[]{1, 3, 2, 4, 4, 4, 6};
        List<Integer> output = find(input);

        for (int i : output) {
            System.out.print(i + ",");
        }
    }
}
