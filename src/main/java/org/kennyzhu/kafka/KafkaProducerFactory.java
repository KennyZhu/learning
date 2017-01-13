package org.kennyzhu.kafka;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.Future;

/**
 * Desc:
 * <p/>Date: 2016-03-03
 * <br/>Time: 18:09
 * <br/>User: ylzhu
 */
public class KafkaProducerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerFactory.class);
    private static final String DEFAULT_TOPIC = "default";
    private static final String CONFIG_NAME = "kafka.properties";
    private static final String CONFIG_DIR = "";
    private static KafkaProducerFactory instance = new KafkaProducerFactory();
    private Properties properties = new Properties();
    private KafkaProducer<String, String> producer;
    private volatile static boolean isInit = false;


    private KafkaProducerFactory() {

    }

    public static KafkaProducerFactory getInstance() {
        instance.init();
        return instance;
    }

    public void init() {
        if (isInit) {
//            LOGGER.info("#Kafka config inited.");
            return;
        }
        LOGGER.info("#Begin to init kafka config.");
        try {
            String filePath = KafkaProducerFactory.class.getClassLoader().getResource(CONFIG_DIR).getPath();
            filePath = filePath + "/" + CONFIG_NAME;
            properties.load(new FileInputStream(filePath));
            producer = new KafkaProducer<String, String>(properties);
            isInit = true;
        } catch (Exception e) {
            LOGGER.error("create kafka producer error!use default setting!", e);
        }

    }

    /**
     * @param topic
     * @param msg
     */
    public void sendMsg(String topic, String msg) {
        sendMsg(topic, msg, null);
    }

    /**
     * kafka会根据zone来对消息进行分区。一个分区内的消息是可以保证顺序的
     */
    public void sendMsg(String topic, String msg, String key) {
        if (StringUtils.isBlank(key)) {
            key = msg;
        }
        if (StringUtils.isBlank(topic)) {
            topic = DEFAULT_TOPIC;
        }
        if (StringUtils.isNotBlank(msg)) {
            ProducerRecord<String, String> data = new ProducerRecord<>(topic, key, msg);
            Future<RecordMetadata> future = producer.send(data);
            try {
                RecordMetadata recordMetadata = future.get();
                LOGGER.info(recordMetadata.toString());
            } catch (Exception e) {

            }
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public KafkaProducer<String, String> getProducer() {
        return producer;
    }
}
