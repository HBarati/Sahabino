package ruleChecking;

import DGIM.DGIMAlgo;
import entity.AlertModel;
import entity.LogModel;
import entity.LogPriority;
import kafkaFactory.KafkaLogsConsumer;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class RulesEvaluatorDGIM extends RuleEvaluator {

    public RulesEvaluatorDGIM() throws SQLException {
    }

    @Override
    public void run() {
        List<LogModel> logModelList;
        DGIMAlgo FATALList = new DGIMAlgo();
        DGIMAlgo WARNList = new DGIMAlgo();
        DGIMAlgo DEBUGList = new DGIMAlgo();
        DGIMAlgo INFOList = new DGIMAlgo();
        DGIMAlgo ERRORList = new DGIMAlgo();
        int PosCnt = 0;
        while (true) {
            logModelList = KafkaLogsConsumer.runConsumer(consumer);
            for (LogModel logModel : logModelList) {
                switch (LogPriority.valueOf(logModel.getPriority())) {
                    case FATAL:
                        FATALList.addBit(PosCnt);
                    case WARN:
                        WARNList.addBit(PosCnt);
                    case DEBUG:
                        DEBUGList.addBit(PosCnt);
                    case INFO:
                        INFOList.addBit(PosCnt);
                    case ERROR:
                        ERRORList.addBit(PosCnt);
                }
                PosCnt++;
            }
            int a = INFOList.displayMap();
//            System.out.println(INFOList.displayMap());
            mySqlWriter(FATALList.displayMap(), LogPriority.FATAL);
            mySqlWriter(WARNList.displayMap(), LogPriority.WARN);
            mySqlWriter(DEBUGList.displayMap(), LogPriority.DEBUG);
            mySqlWriter(INFOList.displayMap(), LogPriority.INFO);
            mySqlWriter(ERRORList.displayMap(), LogPriority.ERROR);
            sleep(Integer.parseInt(config.getRuleEvaluatingInterval()));
        }
    }

    void mySqlWriter(int onesCount, LogPriority logPriority) {
        String query = String.format("INSERT INTO %s (one_count) VALUES ('%s')",
                logPriority.toString().toLowerCase(), onesCount);
        System.out.println(query);
        try {
            Statement stmt = connection.createStatement();
            logger.info("insert onesNumber of" + logPriority.toString().toLowerCase());
            int rowAffected = stmt.executeUpdate(query);
            logger.info(rowAffected + " row affected");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    void mySqlWriter(AlertModel alertModel) throws SQLException {
    }
}
