package org.kennyzhu.juc;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by yanlongzhu on 2017/1/13.
 */
public class ConcurrentSkipListMapTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConcurrentSkipListMapTest.class);

    private static void test() {
        ConcurrentSkipListMap<Integer, String> map = new ConcurrentSkipListMap<>();
        map.put(4, "bbbbb");
        map.put(3, "aaaaaa");

        for (Map.Entry entry : map.entrySet()) {
            LOGGER.info(entry.getKey() + "====" + entry.getValue());
        }

    }

    public static void main(String[] args) {
        test();
    }
}
