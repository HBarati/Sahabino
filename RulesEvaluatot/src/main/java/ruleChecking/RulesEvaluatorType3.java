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

    public RulesEvaluatorType3() throws SQLException {
    }

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
                ruleType3Checker(logQueue, Type3Rate);
                sleep(Integer.parseInt(config.getRuleEvaluatingInterval()));
            } else {
                logger.fatal("alert type3 log Queue is empty!");
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

    private void deleteLogsOutOfDuration(LinkedList<LogModel> logQueue, Date someMinuteAgo, Date now) {
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
        AlertModel3 alertModel3 = (AlertModel3) alertModel;
        logger.info("insert alert type3 on table alert_type3 in logsAlert Data Base with parameters: "
                + alertModel3.getComponentName() + " and "
                + alertModel3.getRate());
        String query = String.format("INSERT INTO alert_type3 (component_name , rate) VALUES ('%s', '%s')",
                alertModel3.getComponentName(), alertModel3.getRate());
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
