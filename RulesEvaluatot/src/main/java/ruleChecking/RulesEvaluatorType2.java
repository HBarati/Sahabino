package ruleChecking;

import entity.AlertModel;
import entity.AlertModel2;
import entity.LogModel;
import kafkaFactory.KafkaLogsConsumer;

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
    public RulesEvaluatorType2() throws SQLException {
    }

    @Override
    public void run() {
        List<LogModel> logModelList;
        String Type2LogPriority = config.getType2LogPriority();
        String Type2LogCategory = config.getType2LogCategory();
        long duration = Long.parseLong(config.getType2Period());
        String Type2Rate = config.getType2Rate();
        LinkedList<LogModel> logQueue = new LinkedList<>();

        while (true) {
            Date now = new Date(System.currentTimeMillis());
            Date someMinuteAgo = new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(duration));
            logModelList = KafkaLogsConsumer.runConsumer(consumer);
            // made a Queue that all of the logs is in the duration
            addingLogsInDuration(logModelList, logQueue, someMinuteAgo, now, Type2LogCategory, Type2LogPriority);
            if (!logQueue.isEmpty()) {
                deleteLogsOutOfDuration(logQueue, someMinuteAgo, now);
                logger.info("Queue in alert type2 is updated! (all the logs is in duration: " + duration + ")");
                ruleType2Checker(logQueue, Type2Rate);
                sleep(Integer.parseInt(config.getRuleEvaluatingInterval()));
            } else {
                logger.fatal("alert type2 log Queue is empty!");
            }
        }
    }

    private void ruleType2Checker(LinkedList<LogModel> logQueue, String Type2Rate) {
        int rate = Integer.parseInt(Type2Rate);
        if (logQueue.size() >= rate) {
            String description = twoLastLog(logQueue);
            AlertModel2 alertModel2 = new AlertModel2(logQueue.getFirst().getCategory(),
                    description, logQueue.getFirst().getPriority(), String.valueOf(logQueue.size()));
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
        SimpleDateFormat formatter = new SimpleDateFormat(config.getLogDateFormat());
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
            SimpleDateFormat formatter = new SimpleDateFormat(config.getLogDateFormat());
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


    @Override
    void mySqlWriter(AlertModel alertModel) {
        AlertModel2 alertModel2 = (AlertModel2) alertModel;
        logger.info("insert alert type2 on table alert_type2 in logsAlert Data Base with parameters: "
                + alertModel2.getComponentName() + " and "
                + alertModel2.getDescription() + " and "
                + alertModel2.getPriority() + " and "
                + alertModel2.getRate());
        String sql = String.format("INSERT INTO alert_type2 (component_name, description, log_level , rate) VALUES ('%s','%s', '%s' ,'%s')",
                alertModel2.getComponentName(), alertModel2.getDescription(), alertModel2.getPriority(), alertModel2.getRate());
        try {
            Statement stmt = connection.createStatement();
            int rowAffected = stmt.executeUpdate(sql);
            logger.info(rowAffected + " row affected");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
