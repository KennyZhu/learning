package org.kennyzhu.arithmetic.leetCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * desc:input中第一次完整出现target的下标
 * author: yanlongzhu
 * date:2020/8/6.
 */
public class MaxNumEqual {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaxNumEqual.class);

    //
    public static int cal(int[] inputs, int[] targets) {
        int begin = 0;
        int end = 0;
        int targetPoint = 0;
        int index = 0;
        int targetIndex = 0;

        for (index = 0; index < inputs.length; index++) {
            int input = inputs[index];
            for (targetIndex = targetPoint; targetIndex < targets.length; targetIndex++) {
                int target = targets[targetIndex];
                if (input == target) {
                    //最后一个相等且前面也相等
                    if (targetIndex == targets.length - 1 && (end - begin) == targets.length) {
                        return begin;
                    }
                    if (targetIndex == 0) {
                        begin = index;
                        end = index;
                        targetPoint = targetIndex + 1;
                    } else {
                        end++;
                        targetPoint++;
                    }
                    break;
                } else {//复原
                    begin = 0;
                    end = 0;
                    targetPoint = 0;
                    break;
                }
            }
        }
        return begin;

    }

    public static void main(String[] args) {
        int[] inputs = {5, 2, 4, 21, 6, 4, 3, 3, 2, 3, 1, 5};
        int[] targets = {3};
        LOGGER.info("Result = " + cal(inputs, targets));
    }
}
