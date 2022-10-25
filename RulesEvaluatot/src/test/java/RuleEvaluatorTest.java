import entity.LogModel;
import org.junit.Test;
import ruleChecking.RulesEvaluatorType3;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RuleEvaluatorTest {
    RulesEvaluatorType3 rulesEvaluatorType3 = new RulesEvaluatorType3();

    public RuleEvaluatorTest() throws SQLException {
    }

    @Test
    public void addingLogsInDurationType3_TestCase() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        Date date = new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(2));
        String nowDate = dateFormat.format(date);
        String[] dateTime = nowDate.split(" ");
        LogModel logModel = new LogModel(dateTime[0], dateTime[1], "114", "FATAL", "TestLog4jServlet", "message");
        LogModel logMode2 = new LogModel(dateTime[0], dateTime[1], "114", "FATAL", "TestLog4jServlet", "message");
        LogModel logMode3 = new LogModel(dateTime[0], dateTime[1], "114", "FATAL", "TestLog4jServlet", "message");

        List<LogModel> logModelList = Arrays.asList(logModel, logMode2, logMode3);
        LinkedList<LogModel> logQueue = new LinkedList<>();
        Date now = new Date(System.currentTimeMillis());
        Date someMinuteAgo = new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(5));
        String Type3LogCategory = "TestLog4jServlet";
        rulesEvaluatorType3.addingLogsInDuration(logModelList, logQueue, someMinuteAgo, now, Type3LogCategory);

        assert (logQueue.size() == 3);
    }

    @Test
    public void deleteLogsOutOfDuration_TestCaseBeforeSomeMinuteAgo() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        Date date = new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(10));
        String nowDate = dateFormat.format(date);
        String[] dateTime = nowDate.split(" ");
        LogModel logModel = new LogModel(dateTime[0], dateTime[1], "114", "FATAL", "TestLog4jServlet", "message");
        LogModel logMode2 = new LogModel(dateTime[0], dateTime[1], "114", "FATAL", "TestLog4jServlet", "message");
        LogModel logMode3 = new LogModel(dateTime[0], dateTime[1], "114", "FATAL", "TestLog4jServlet", "message");
        LinkedList<LogModel> logQueue = new LinkedList<>(Arrays.asList(logMode2, logMode3, logModel));
        Date someMinuteAgo = new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(5));
        Date now = new Date(System.currentTimeMillis());
        rulesEvaluatorType3.deleteLogsOutOfDuration(logQueue, someMinuteAgo, now);
        assert(logQueue.size() == 0);
    }

}
