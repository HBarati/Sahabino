import java.io.IOException;

public class RulesEvaluator {
    public static void main(String[] args) throws InterruptedException, IOException {
        KafkaLogsConsumer kafkaLogsConsumer = new KafkaLogsConsumer();
        KafkaLogsConsumer.runConsumer();
    }
}
