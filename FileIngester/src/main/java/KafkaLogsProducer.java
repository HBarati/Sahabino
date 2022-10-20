import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaLogsProducer {
    //TODO configurable
    private final static String TOPIC = "logs";
    private final static String BOOTSTRAP_SERVERS = "localhost:9092";

    private static Producer<String, Log> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaExampleProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, LogSerializer   .class.getName());
        return new KafkaProducer<>(props);
    }

    static void runProducer(Log log) throws Exception {
        final Producer<String , Log> producer = createProducer();

        try {
//            for (long index = time; index < time + sendMessageCount; index++) {
                final ProducerRecord<String, Log> record = new ProducerRecord<>(TOPIC, log.getCategory(), log);
                RecordMetadata metadata = producer.send(record).get();
//                long elapsedTime = System.currentTimeMillis() - time;
//                System.out.printf("sent record(key=%s value=%s) " + "meta(partition=%d, offset=%d) time=%d\n",
//                        record.key(), record.value(), metadata.partition(),
//                        metadata.offset(), elapsedTime);
//            }
        } finally {
            producer.flush();
            producer.close();
        }
    }
}
