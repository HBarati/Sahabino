package ingester;

import config.ConfigReader;
import entity.LogModel;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import serializer.LogSerializer;

import java.io.IOException;
import java.util.Properties;

class KafkaLogsProducer {
    private static ConfigReader config;

    static {
        try {
            config = ConfigReader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final static String TOPIC = config.getTopic();
    private final static String BOOTSTRAP_SERVERS = config.getBootStrapServer();

    private static Producer<String, LogModel> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaExampleProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, LogSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    public static void runProducer(LogModel log){
        final Producer<String, LogModel> producer = createProducer();
        try {
            final ProducerRecord<String, LogModel> record = new ProducerRecord<>(TOPIC, log.getCategory(), log);
            producer.send(record);
        } finally {
            producer.flush();
            producer.close();
        }
    }
}
