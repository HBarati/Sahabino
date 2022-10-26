package ruleChecking;

import config.ConfigReader;
import entity.AlertModel;
import kafkaFactory.KafkaLogsConsumer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This abstract class is the father of rule types.
 */
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

    /**
     * MySqlWriter give an alert after making rules condition that check by the each methods on the ruleEvaluatorTypes,
     * and made a sql query for insert to the mysql table.
     * @param alertModel
     * @throws SQLException
     */
    abstract void mySqlWriter(AlertModel alertModel) throws SQLException;

    /**
     * This class is sleeping rules thread true while for some time.
     * This time is the interval between consecutive readings of the logModel list from the consumer.
     * @param time same time for all Rule types sleep
     */
    public void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
