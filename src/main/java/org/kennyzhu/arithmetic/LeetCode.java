package org.kennyzhu.arithmetic;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Created by yanlongzhu on 2016/11/7.
 */
public class LeetCode {
    public static String frequencySort(String s) {
        if (StringUtils.isBlank(s)) {
            return s;
        }
        char[] chars = s.toCharArray();
        Map<String, Integer> map = new TreeMap<String, Integer>();
        for (char subC : chars) {
            String subS = String.valueOf(subC);
            if (!map.containsKey(subS)) {
                map.put(subS + "", 0);
            }
            map.put(subS, map.get(subS) + 1);
        }

        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(map.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                if (o1.getValue() > o2.getValue()) {
                    return -1;
                } else if (o1.getValue().equals(o2.getValue())) {
                    return 0;
                } else return 1;
            }
        });

        String output = "";
        for (Map.Entry<String, Integer> entry : entryList) {
            String key = entry.getKey();
            int value = entry.getValue();
            for (int i = 0; i < value; i++) {
                output = output + key;
            }
        }

        return output;

    }

    public static void main(String[] args) {

        String s = "bbbcccccaaaaAAAA";
        System.out.println(frequencySort(s));
    }

}
