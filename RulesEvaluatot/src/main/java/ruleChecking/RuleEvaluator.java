package ruleChecking;

import config.ConfigReader;
import org.apache.log4j.Logger;
import entity.AlertModel;
import kafkaFactory.KafkaLogsConsumer;
import org.apache.kafka.clients.consumer.Consumer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class RuleEvaluator implements Runnable {
    final ConfigReader config = ConfigReader.load();
    final Consumer<String, String> consumer = KafkaLogsConsumer.createConsumer();
    //TODO configurable bug fix
    String url = config.getDBUrl();
    String user = config.getDBUser();
    String password = config.getDBPassword();
    Logger logger = Logger.getLogger(RuleEvaluator.class);
    final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/logsAlert", "hatef", "123456");

    public RuleEvaluator() throws IOException, SQLException {
    }

    abstract void mySqlWriter(AlertModel alertModel) throws SQLException;
}
