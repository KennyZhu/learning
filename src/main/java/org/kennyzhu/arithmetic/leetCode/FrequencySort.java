package org.kennyzhu.arithmetic.leetCode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by yanlongzhu on 2016/12/25.
 */
public class FrequencySort {
    /**
     * @param s
     * @return
     */
    public static String frequencySort(String s) {
        if (s == null || s.isEmpty())
            return s;

        int[] occurency = new int[256];

        for (int i = 0; i < s.length(); i++) {
            System.out.println("i is " + i + " s.charAt(i) is " + s.charAt(i) + " result is " + occurency[s.charAt(i)]);
            //数组下标代表字符，值代表次数  字符相减，代表ascii的差值
            //字符和数字对应，a==97
            occurency[s.charAt(i)]++;
        }

        int max = 0;
        int charAtMax = 0;
        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < 256; j++) {
            for (int t = j; t < 256; t++) {
                if (occurency[t] > max) {
                    max = occurency[t];
                    charAtMax = t;
                }
            }
            while (max > 0) {
                builder.append((char) charAtMax);
                max--;
            }
            int tmp = occurency[j];
            occurency[j] = max;
            occurency[charAtMax] = tmp;
        }

        return builder.toString();
    }


    public static void main(String[] args) {
        try {
            File file = new File("/Users/mfhj-dz-001-506/Documents/text");
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String cc = null;
            String input = "baa";
            //            while ((cc = reader.readLine()) != null) {
            //                input = input + cc;
            //            }
            long beginTime = System.currentTimeMillis();
            String outPut = frequencySort(input);
            System.out.print("Cost is :" + (System.currentTimeMillis() - beginTime) + " output is " + outPut);
            int[] ar = new int[100];
            System.out.println(ar["a".charAt(0)]);

            char c1 = '*';
            char c2 = '1';
            System.out.println(c1 - c2);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
