package ruleChecking;

import config.ConfigReader;
import entity.AlertModel;
import kafkaFactory.KafkaLogsConsumer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class RuleEvaluator implements Runnable {
    final ConfigReader config = ConfigReader.load();
    final Consumer<String, String> consumer = KafkaLogsConsumer.createConsumer();
    String url = config.getDBUrl();
    String user = config.getDBUser();
    String password = config.getDBPassword();
    Logger logger = Logger.getLogger(RuleEvaluator.class);

    final Connection connection = DriverManager.getConnection(url, user, password);

    public RuleEvaluator() throws SQLException {
    }

    abstract void mySqlWriter(AlertModel alertModel) throws SQLException;

    public void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
