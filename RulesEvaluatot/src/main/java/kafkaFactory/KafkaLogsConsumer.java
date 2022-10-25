package kafkaFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.ConfigReader;
import entity.LogModel;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.Logger;
import ruleChecking.RuleEvaluator;

import java.time.Duration;
import java.util.*;

public class KafkaLogsConsumer {
    private static ConfigReader config = ConfigReader.load();
    private final static String TOPIC = config.getTopic();
    private final static String BOOTSTRAP_SERVERS = config.getBootStrapServer();

    public static Consumer<String, String> createConsumer() {
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
//        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, 1024);

        final Consumer<String, String> consumer = new KafkaConsumer<>(props);
        Logger logger = Logger.getLogger(RuleEvaluator.class);
        logger.info("Kafka consumer is using topic: " + config.getTopic() + "and BootStrap server: "
                + config.getBootStrapServer() + " in " + consumer.toString());
        consumer.subscribe(Collections.singletonList(TOPIC));
        return consumer;
    }

    public static List<LogModel> runConsumer(Consumer<String, String> consumer) {
        Logger logger = Logger.getLogger(RuleEvaluator.class);
        ObjectMapper objectMapper = new ObjectMapper();
        List<LogModel> logModelList = new ArrayList<>();

        final ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(500));
        System.out.println(consumerRecords.count());
        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
            LogModel log = null;
            try {
                log = objectMapper.readValue(consumerRecord.value(), LogModel.class);
            } catch (JsonProcessingException e) {
                logger.error("logs in the kafka are not valid type");
                e.printStackTrace();
            }
            logger.info("read log: " + log.toString() + " from kafka and add to the log list logModelList as " + (logModelList.size() + 2) + "s log");
            logModelList.add(log);
        }
        consumer.commitAsync();
        return logModelList;
    }
}
