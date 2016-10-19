package org.kennyzhu.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * Created by yanlongzhu on 2016/10/17.
 */
public class Consumer {
    public static void main(String[] args) {
        Properties props = KafkaProducerFactory.getInstance().getProperties();
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("default"));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records)
                System.out.printf(";offset=%d, key=%s,value=%s", record.offset(), record.key(), record.value());
            System.out.println();
        }
    }
}
