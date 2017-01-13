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
        for (int i = 0; i < 1000000; i++) {
            for (int j = 0; j < 1000000; j++, i++) {
                KafkaProducerFactory.getInstance().sendMsg(null, "###########msg------" + i + "==" + j);
                try {
                    Thread.sleep(100);
                } catch (Exception e) {

                }
            }
        }

    }
}
