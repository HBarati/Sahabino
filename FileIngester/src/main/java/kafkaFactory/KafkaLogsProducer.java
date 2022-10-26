package kafkaFactory;

import config.ConfigReader;
import entity.LogModel;
import ingester.LogIngester;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;
import serializer.LogSerializer;

import java.util.Properties;

public class KafkaLogsProducer {
    private static ConfigReader config = ConfigReader.load();

    /**
     * This method create a kafka producer with our given producer config.
     * @return Kafka Producer
     */
    private static Producer<String, LogModel> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootStrapServer());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaClientID");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, LogSerializer.class.getName());
        Logger logger = Logger.getLogger(LogIngester.class);
        logger.info("Kafka consumer is using topic: " + config.getTopic() + "and BootStrap server: " + config.getBootStrapServer());
        return new KafkaProducer<>(props);
    }

    /**
     *This method run the producer.
     * give a log and made record on producer and send it to kafka.
     *
     * @param log pass a log to producer to write that to kafka
     */
    public static void runProducer(LogModel log) {
        final Producer<String, LogModel> producer = createProducer();
        final ProducerRecord<String, LogModel> record = new ProducerRecord<>(config.getTopic(), log.getCategory(), log);
        producer.send(record);
        producer.flush();
        producer.close();
    }
}
