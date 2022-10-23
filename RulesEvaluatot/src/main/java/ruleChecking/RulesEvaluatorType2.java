package ruleChecking;

import entity.AlertModel;
import entity.AlertModel2;
import entity.LogModel;
import kafkaFactory.KafkaLogsConsumer;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class RulesEvaluatorType2 extends RuleEvaluator {
    //Constructor
    public RulesEvaluatorType2() throws IOException, SQLException {
    }

    @Override
    public void run() {
        List<LogModel> logModelList = null;
        String Type2LogPriority = config.getType2LogPriority();
        String Type2LogCategory = config.getType2LogCategory();
        String Type2Period = config.getType2Period();
        String Type2Rate = config.getType2Rate();
        while (true) {
            try {
                logModelList = KafkaLogsConsumer.runConsumer(consumer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (LogModel logModel : logModelList) {
                //manteqh
                if (logModel.getPriority().equals("")) {





                    AlertModel2 alertModel2 = new AlertModel2();
                    //write to mysql
                    try {
                        mySqlWriter(alertModel2);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    void mySqlWriter(AlertModel alertModel) throws SQLException {
        Statement stmt = connection.createStatement();
        AlertModel2 alertModel2 = (AlertModel2) alertModel;
        String sql = String.format("INSERT INTO alert_type2 (component_name, description, log_level , rate) VALUES ('%s','%s', '%s' ,'%d')",
                alertModel2.getComponentName(), alertModel2.getDescription() ,alertModel2.getPriority(), alertModel2.getRate());
        stmt.executeUpdate(sql);
        System.out.println(sql);
    }
}
