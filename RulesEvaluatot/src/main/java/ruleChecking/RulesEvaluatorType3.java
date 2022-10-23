package ruleChecking;

import com.mysql.cj.log.Log;
import entity.AlertModel;
import entity.AlertModel3;
import entity.LogModel;
import kafkaFactory.KafkaLogsConsumer;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class RulesEvaluatorType3 extends RuleEvaluator {

    public RulesEvaluatorType3() throws IOException, SQLException {
    }

    @Override
    public void run() {
        List<LogModel> logModelList = null;
        String Type3LogCategory = config.getType3LogCategory();
        String Type3Period = config.getType3Period();
        String Type3Rate = config.getType3Rate();

        LinkedList<LogModel> logQueue = new LinkedList<>();
        Long duration = Long.parseLong(Type3Period);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        while (true) {
            Date now = new Date(System.currentTimeMillis());
            Date someMinuteAgo = new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(duration));
            try {
                logModelList = KafkaLogsConsumer.runConsumer(consumer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //adding log to the q in our duration
            Date parse = null;
            for (LogModel logModel : logModelList) {
                String LogDateTime = logModel.getDate() + " " + logModel.getTime();
                try {
                    parse = formatter.parse(LogDateTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (parse.after(someMinuteAgo) && parse.before(now) && logModel.getCategory().equals(Type3LogCategory)) {
                    logQueue.addFirst(logModel);
                }
            }
            if (!logQueue.isEmpty()) {
                //deleting log out of our duration
                while (true) {
                    LogModel lastLog = logQueue.getLast();
                    String lastLogDateTime = lastLog.getDate() + " " + lastLog.getTime();
                    try {
                        parse = formatter.parse(lastLogDateTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (parse.after(someMinuteAgo) && parse.before(now)) {
                        break;
                    } else {
                        logQueue.removeLast();
                    }
                }
                int rate = Integer.parseInt(Type3Rate);
                System.out.println(logQueue.size());
                if (logQueue.size() >= rate) {
                    AlertModel3 alertModel3 = new AlertModel3(logQueue.getFirst().getCategory(), String.valueOf(logQueue.size()));
                    try {
                        mySqlWriter(alertModel3);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    void mySqlWriter(AlertModel alertModel) throws SQLException {
        Statement stmt = connection.createStatement();
        AlertModel3 alertModel3 = (AlertModel3) alertModel;
        String sql = String.format("INSERT INTO alert_type3 (component_name , rate) VALUES ('%s', '%s')",
                alertModel3.getComponentName(), alertModel3.getRate());
        stmt.executeUpdate(sql);
        System.out.println(sql);
    }
}
