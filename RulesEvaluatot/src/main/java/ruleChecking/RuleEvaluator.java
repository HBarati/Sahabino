package ruleChecking;

import config.ConfigReader;
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
    String url = config.getDBUrl();
    String user = config.getDBUser();
    String password = config.getDBPassword();
    final Connection connection = DriverManager.getConnection(url, user, password);

    public RuleEvaluator() throws IOException, SQLException {
    }

//    public abstract void evaluatingAlertType() throws IOException, SQLException, InterruptedException;
    abstract void mySqlWriter(AlertModel alertModel) throws SQLException;
}
