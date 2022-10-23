package ruleChecking;

import entity.AlertModel;
import entity.AlertModel3;
import entity.LogModel;
import kafkaFactory.KafkaLogsConsumer;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
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
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

        while (true) {
            Date now = new Date(System.currentTimeMillis());
            Date someMinuteAgo = new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(duration));
            logModelList = KafkaLogsConsumer.runConsumer(consumer);

            addingLogsInDuration(logModelList, logQueue, someMinuteAgo, now, Type3LogCategory);
            if (!logQueue.isEmpty()) {
                deleteLogsOutOfDuration(logQueue, someMinuteAgo, now);
                ruleType3Checker(logQueue, Type3Rate);
                sleep(1500);
            }
        }
    }

    private void ruleType3Checker(LinkedList<LogModel> logQueue, String Type3Rate) {
        int rate = Integer.parseInt(Type3Rate);
        System.out.println(logQueue.size());
        if (logQueue.size() >= rate) {
            AlertModel3 alertModel3 = new AlertModel3(logQueue.getFirst().getCategory(), String.valueOf(logQueue.size()));
            mySqlWriter(alertModel3);
        }
    }

    private void addingLogsInDuration(List<LogModel> logModelList
            , LinkedList<LogModel> logQueue
            , Date someMinuteAgo
            , Date now
            , String Type3LogCategory) {
        Date parse = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
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
    }

    private void deleteLogsOutOfDuration(LinkedList<LogModel> logQueue, Date someMinuteAgo, Date now) {
        while (true) {
            Date parse = null;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
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
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    void mySqlWriter(AlertModel alertModel) {
        Statement stmt = null;
        AlertModel3 alertModel3 = (AlertModel3) alertModel;
        String sql = String.format("INSERT INTO alert_type3 (component_name , rate) VALUES ('%s', '%s')",
                alertModel3.getComponentName(), alertModel3.getRate());
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            System.out.println(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
