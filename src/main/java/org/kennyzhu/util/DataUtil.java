package org.kennyzhu.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;

import java.util.List;

@Slf4j
public final class DataUtil {

    public static void logList(String prefix, List<Integer> list, Logger logger) {
        if (CollectionUtils.isEmpty(list)) {
            log.info(prefix + " ");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Integer index : list) {
            sb.append(index).append(",");
        }
        log.info(prefix + " " + sb.toString());
    }
}
