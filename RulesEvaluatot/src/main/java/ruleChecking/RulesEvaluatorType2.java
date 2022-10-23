package ruleChecking;

import entity.AlertModel;
import entity.AlertModel2;
import entity.AlertModel3;
import entity.LogModel;
import kafkaFactory.KafkaLogsConsumer;
import org.apache.kafka.common.protocol.types.Field;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;


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
        LinkedList<LogModel> logQueue = new LinkedList<>();

        Long duration = Long.parseLong(Type2Period);

        while (true) {
            Date now = new Date(System.currentTimeMillis());
            Date someMinuteAgo = new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(duration));
            logModelList = KafkaLogsConsumer.runConsumer(consumer);

            addingLogsInDuration(logModelList, logQueue, someMinuteAgo, now, Type2LogCategory, Type2LogPriority);
            if (!logQueue.isEmpty()) {
                deleteLogsOutOfDuration(logQueue, someMinuteAgo, now);
                ruleType2Checker(logQueue, Type2Rate);
                sleep(1500);
            }
        }
    }

    private void ruleType2Checker(LinkedList<LogModel> logQueue, String Type2Rate) {
        int rate = Integer.parseInt(Type2Rate);
        System.out.println(logQueue.size());
        if (logQueue.size() >= rate) {
            String description = twoLastLog(logQueue);
            AlertModel2 alertModel2 = new AlertModel2(logQueue.getFirst().getCategory(), description, logQueue.getFirst().getPriority(), String.valueOf(logQueue.size()));
            mySqlWriter(alertModel2);
        }
    }

    private String twoLastLog(LinkedList<LogModel> logQueue) {
        String description;
        LogModel logModel = logQueue.peekFirst();
        if (logQueue.size() == 1) {
            description = logModel.toString();
        } else {
            logQueue.removeFirst();
            description = logModel.toString() + logQueue.getFirst();
            logQueue.addFirst(logModel);
        }
        return description;
    }

    private void addingLogsInDuration(List<LogModel> logModelList
            , LinkedList<LogModel> logQueue
            , Date someMinuteAgo
            , Date now
            , String Type2LogCategory
            , String Type2LogPriority) {
        Date parse = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        for (LogModel logModel : logModelList) {
            String LogDateTime = logModel.getDate() + " " + logModel.getTime();
            try {
                parse = formatter.parse(LogDateTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (parse.after(someMinuteAgo)
                    && parse.before(now)
                    && logModel.getCategory().equals(Type2LogCategory)
                    && logModel.getPriority().equals(Type2LogPriority)) {
                logQueue.addFirst(logModel);
            }
        }
    }

    private void deleteLogsOutOfDuration(LinkedList<LogModel> logQueue
            , Date someMinuteAgo
            , Date now) {
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
        AlertModel2 alertModel2 = (AlertModel2) alertModel;
        String sql = String.format("INSERT INTO alert_type2 (component_name, description, log_level , rate) VALUES ('%s','%s', '%s' ,'%s')",
                alertModel2.getComponentName(), alertModel2.getDescription(), alertModel2.getPriority(), alertModel2.getRate());
        System.out.println(sql);
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            System.out.println(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
