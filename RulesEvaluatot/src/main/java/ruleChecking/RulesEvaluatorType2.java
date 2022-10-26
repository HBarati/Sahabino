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
    /**
     * This is the principal method for rule type 2.
     * first give the necessary parameter from config file and made a Queue as linkedList to keep the logs at duration.
     * second make 2 parameter as now and someMinuteAgo for making Queue and give the logModel list from kafka as consumer.
     * third call the addingLogsInDuration method for add new read log that has the rules(category, priority and duration) to the Queue.
     * fourth if Queue is not empty call the deleteLogsOutOfDuration method to clean the Queue and make sure that all of the logs in Queue are in the duration.
     * fifth call the ruleType2 checker and pass to it Queue and rate. if rules was met(rate), made an alertType2.
     * sixth if alertType2 has value on it, send to the mySqlWriter method for write on table.
     * Then the second to sixth steps will be running in a thread until the code is running,
     * which will also check the new list of logs from Kafka as soon as they get them.
     */
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
                AlertModel2 alertModel2 = ruleType2Checker(logQueue, Type2Rate);
                if (alertModel2 != null) {
                    mySqlWriter(alertModel2);
                }
                sleep(Integer.parseInt(config.getRuleEvaluatingInterval()));
            } else {
                logger.fatal("alert type2 log Queue is empty!");
            }
        }
    }

    /**
     * This method check if the size of the Queue(all of the logs inside the Queue has rules) is greater than rate,
     * made an alertType 2.
     * @param logQueue provided Queue
     * @param Type2Rate rate
     * @return an alertType 2
     */
    public AlertModel2 ruleType2Checker(LinkedList<LogModel> logQueue, String Type2Rate) {
        int rate = Integer.parseInt(Type2Rate);
        if (logQueue.size() >= rate) {
            String description = twoLastLog(logQueue);
            return new AlertModel2(logQueue.getFirst().getCategory(),
                    description, logQueue.getFirst().getPriority(), String.valueOf(logQueue.size()));
        }
        return null;
    }

    /**
     * This method give two last log from the Queue for alertType2 description.
     * @param logQueue provided Queue
     * @return description
     */
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
    /**
     * This method give the Logs and Queue and parameters to add conditional logs to the Queue.
     * @param logModelList new logModelList from kafka
     * @param logQueue provided Queue
     * @param someMinuteAgo duration time from now
     * @param now now time
     * @param Type2LogCategory category for check the logs
     * @param Type2LogPriority priority for check the logs
     */
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

    /**
     * This method give the duration to updated Queue logs(all of the logs out the duration are deleted).
     * @param logQueue provided Queue
     * @param someMinuteAgo duration time from now
     * @param now now time
     */
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
