package ruleChecking;

import entity.AlertModel;
import entity.AlertModel3;
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

public class RulesEvaluatorType3 extends RuleEvaluator {
    //Constructor
    public RulesEvaluatorType3() throws SQLException {
    }
    /**
     * This is the principal method for rule type 3.
     * first give the necessary parameter from config file and made a Queue as linkedList to keep the logs at duration.
     * second make 2 parameter as now and someMinuteAgo for making Queue and give the logModel list from kafka as consumer.
     * third call the addingLogsInDuration method for add new read log that has the rules(category and duration) to the Queue.
     * fourth if Queue is not empty call the deleteLogsOutOfDuration method to clean the Queue and make sure that all of the logs in Queue are in the duration.
     * fifth call the ruleType3 checker and pass to it Queue and rate. if rules was met(rate), made an alertType2.
     * sixth if alertType3 has value on it, send to the mySqlWriter method for write on table.
     * Then the second to sixth steps will be running in a thread until the code is running,
     * which will also check the new list of logs from Kafka as soon as they get them.
     */
    @Override
    public void run() {
        List<LogModel> logModelList;
        LinkedList<LogModel> logQueue = new LinkedList<>();
        String Type3LogCategory = config.getType3LogCategory();
        String Type3Rate = config.getType3Rate();
        long duration = Long.parseLong(config.getType3Period());

        while (true) {
            Date now = new Date(System.currentTimeMillis());
            Date someMinuteAgo = new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(duration));
            logModelList = KafkaLogsConsumer.runConsumer(consumer);

            addingLogsInDuration(logModelList, logQueue, someMinuteAgo, now, Type3LogCategory);
            if (!logQueue.isEmpty()) {
                deleteLogsOutOfDuration(logQueue, someMinuteAgo, now);
                logger.info("Queue in alert type3 is updated! (all the logs is in duration: " + duration + ")");
                AlertModel3 alertModel3 = ruleType3Checker(logQueue, Type3Rate);
                if (alertModel3 != null) {
                    mySqlWriter(alertModel3);
                }
                sleep(Integer.parseInt(config.getRuleEvaluatingInterval()));
            } else {
                logger.fatal("alert type3 log Queue is empty!");
            }
        }
    }
    /**
     * This method check if the size of the Queue(all of the logs inside the Queue has rules) is greater than rate,
     * made an alertType 3.
     * @param logQueue provided Queue
     * @param Type3Rate rate
     * @return an alertType 3
     */
    public AlertModel3 ruleType3Checker(LinkedList<LogModel> logQueue, String Type3Rate) {
        int rate = Integer.parseInt(Type3Rate);
        logger.info("log Queue size is: "+logQueue.size());
        if (logQueue.size() >= rate) {
            AlertModel3 alertModel3 = new AlertModel3(logQueue.getFirst().getCategory(), String.valueOf(logQueue.size()));
            return alertModel3;
        }
        return null;
    }

    /**
     * This method give the Logs and Queue and parameters to add conditional logs to the Queue.
     * @param logModelList new logModelList from kafka
     * @param logQueue provided Queue
     * @param someMinuteAgo duration time from now
     * @param now now time
     * @param Type3LogCategory category for check the logs
     */
    public void addingLogsInDuration(List<LogModel> logModelList
            , LinkedList<LogModel> logQueue
            , Date someMinuteAgo
            , Date now
            , String Type3LogCategory) {
        Date parse = null;
        SimpleDateFormat formatter = new SimpleDateFormat(config.getLogDateFormat());
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
    /**
     * This method give the duration to updated Queue logs(all of the logs out the duration are deleted).
     * @param logQueue provided Queue
     * @param someMinuteAgo duration time from now
     * @param now now time
     */
    public void deleteLogsOutOfDuration(LinkedList<LogModel> logQueue, Date someMinuteAgo, Date now) {
        while (true) {
            Date parse = null;
            SimpleDateFormat formatter = new SimpleDateFormat(config.getLogDateFormat());
            if (logQueue.size() == 0) {
                break;
            }
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
        AlertModel3 alertModel3 = (AlertModel3) alertModel;
        logger.info("insert alert type3 on table alert_type3 in logsAlert Data Base with parameters: "
                + alertModel3.getComponentName() + " and "
                + alertModel3.getRate());
        //SQL Injection!!
        String query = String.format("INSERT INTO alert_type3 (component_name , rate) VALUES ('%s', '%s')",
                alertModel3.getComponentName(), alertModel3.getRate());
        try {
            Statement stmt = connection.createStatement();
            int rowAffected = stmt.executeUpdate(query);
            logger.info(rowAffected + " row affected");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
