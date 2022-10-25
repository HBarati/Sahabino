package ruleChecking;

import entity.AlertModel;
import entity.AlertModel1;
import entity.LogModel;
import kafkaFactory.KafkaLogsConsumer;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class RulesEvaluatorType1 extends RuleEvaluator {
    //Constructor
    public RulesEvaluatorType1() throws IOException, SQLException {
    }

    @Override
    public void run() {
        List<LogModel> logModelList;
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
        AlertModel1 alertModel1 = (AlertModel1) alertModel;
        String sql = String.format("INSERT INTO alert_type1 (log_level, log_message) VALUES ('%s', '%s')",
                alertModel1.getPriority(), alertModel1.getMessage());
        try {
            Statement stmt = connection.createStatement();
            logger.info("insert alert type1 on table alert_type1 in logsAlert Data Base with parameters: "
                    +alertModel1.getPriority()+" and "
                    +alertModel1.getMessage());
            stmt.executeUpdate(sql);
            System.out.println(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
