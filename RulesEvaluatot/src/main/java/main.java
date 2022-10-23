import ruleChecking.RuleEvaluator;
import ruleChecking.RulesEvaluatorType1;
import ruleChecking.RulesEvaluatorType3;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class main {
    public static void main(String[] args) throws IOException, SQLException, InterruptedException, ParseException {
        Thread thread = new Thread(new RulesEvaluatorType3());
        thread.start();
        thread.join();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
//        String s1 = "2022-10-23 20:55:42,114";
//        Date parse = formatter.parse(s1);
//        Date fiveMinuteAgo = new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(5));
//        Date now = new Date(System.currentTimeMillis());
//        System.out.println(parse);
//        System.out.println(fiveMinuteAgo);
//        System.out.println(now);
//
//        if (parse.after(fiveMinuteAgo) && parse.before(now)){
//            System.out.println("yes");
//        }
    }
}
