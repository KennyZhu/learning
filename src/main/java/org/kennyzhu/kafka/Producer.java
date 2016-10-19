package org.kennyzhu.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yanlongzhu on 2016/10/17.
 */
public class Producer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

    public static void main(String[] args) {
//        KafkaProducer producer = KafkaProducerFactory.getInstance().getProducer();
        int i = 0;
        while (true) {

            LOGGER.info("#Begin to while i:" + i);
            for (int j = 0; j < 10000; j++, i++) {
                KafkaProducerFactory.getInstance().sendMsg(null, "msg------" + i);
            }
            try {
                Thread.sleep(10000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
