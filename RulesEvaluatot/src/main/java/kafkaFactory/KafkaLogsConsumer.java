package kafkaFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.LogModel;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;


import java.io.IOException;
import java.time.Duration;
import java.util.*;

public class KafkaLogsConsumer {
    private final static String TOPIC = "log22222";
    private final static String BOOTSTRAP_SERVERS = "localhost:9092";

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

        consumer.subscribe(Collections.singletonList(TOPIC));
        return consumer;
    }

    public static List<LogModel> runConsumer(Consumer<String, String> consumer) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<LogModel> logModelList = new ArrayList<>();

        final ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(500));
        System.out.println(consumerRecords.count());
        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
            LogModel log = null;
            try {
                log = objectMapper.readValue(consumerRecord.value(), LogModel.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            logModelList.add(log);
        }
        consumer.commitAsync();
        return logModelList;
    }
}
