import org.apache.kafka.clients.consumer.Consumer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.util.Random;


public class RulesEvaluator {
    public static void main(String[] args) throws InterruptedException, IOException, SQLException {
        KafkaLogsConsumer kafkaLogsConsumer = new KafkaLogsConsumer();
        List<LogModel> logModelList = new ArrayList<>();
        final Consumer<String, String> consumer = KafkaLogsConsumer.createConsumer();
        ConfigReader config = ConfigReader.load();
        String RuleTypeModeName = config.getType1LogName();
        while (true) {
            logModelList = KafkaLogsConsumer.runConsumer(consumer);
            for (LogModel logModel : logModelList) {
                if (logModel.getPriority().equals(RuleTypeModeName)){
                mySqlWriter(logModel);
                }
//                System.out.println(logModel.toString());

            }

        }
    }

    static void mySqlWriter(LogModel logModel) throws SQLException {
        Connection connection;
        Statement stmt;
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/logsAlert","hatef","123456");
        stmt = connection.createStatement();
        Random rd = new Random();
        String sql = String.format("INSERT INTO log_model VALUES ('%d','%s', '%s')", rd.nextLong() ,logModel.getPriority(), logModel.getMessage());
        stmt.executeUpdate(sql);

        System.out.println(sql);
    }
}
