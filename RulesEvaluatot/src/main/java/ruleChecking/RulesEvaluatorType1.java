package ruleChecking;

import config.ConfigReader;
import entity.AlertModel;
import entity.AlertModel1;
import entity.LogModel;
import kafkaFactory.KafkaLogsConsumer;
import org.apache.kafka.clients.consumer.Consumer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class RulesEvaluatorType1 extends RuleEvaluator {
    //Constructor
    public RulesEvaluatorType1() throws IOException, SQLException {
    }

    @Override
    public void run() {
        List<LogModel> logModelList = null;
        String RuleType1ModePriority = config.getType1LogPriority();
        while (true) {
            logModelList = KafkaLogsConsumer.runConsumer(consumer);
            for (LogModel logModel : logModelList) {
                if (logModel.getPriority().equals(RuleType1ModePriority)) {
                    AlertModel1 alertModel1 = new AlertModel1(logModel.getPriority(), logModel.getMessage());
                    mySqlWriter(alertModel1);
                }
            }

        }
    }

    @Override
    void mySqlWriter(AlertModel alertModel){
        Statement stmt = null;
        AlertModel1 alertModel1 = (AlertModel1) alertModel;
        String sql = String.format("INSERT INTO alert_type1 (log_level, log_message) VALUES ('%s', '%s')",
                alertModel1.getPriority(), alertModel1.getMessage());
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            System.out.println(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}