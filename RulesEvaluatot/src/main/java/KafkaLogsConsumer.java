import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

public class KafkaLogsConsumer {
    private final static String TOPIC = "log22222";
    private final static String BOOTSTRAP_SERVERS = "localhost:9092";

    private static Consumer<String, String> createConsumer() {
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        final Consumer<String, String> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Collections.singletonList(TOPIC));
        return consumer;
    }

    static void runConsumer() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        final Consumer<String, String> consumer = createConsumer();

        while (true) {
            final ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
            System.out.println(consumerRecords.count());
            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                LogModel log = objectMapper.readValue(consumerRecord.value(), LogModel.class);
                System.out.println(log.toString());

            }

//            if (consumerRecords.count()==0) {
//                noRecordsCount++;
//                if (noRecordsCount > giveUp) break;
//                else continue;
//            }

//            consumerRecords.forEach(record -> {
//                System.out.printf("Consumer Record:(%s, %s, %d, %d)\n",
//                        record.key(), record.value(),
//                        record.partition(), record.offset());
//            });

            consumer.commitAsync();
        }
    }
}
