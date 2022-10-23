import ruleChecking.RuleEvaluator;
import ruleChecking.RulesEvaluatorType1;
import ruleChecking.RulesEvaluatorType2;
import ruleChecking.RulesEvaluatorType3;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class main {
    public static void main(String[] args) throws IOException, SQLException, InterruptedException, ParseException {
        Executor executor = Executors.newCachedThreadPool();
        List<RuleEvaluator> ruleEvaluators = new ArrayList<RuleEvaluator>();
        ruleEvaluators.add(new RulesEvaluatorType1());
        ruleEvaluators.add(new RulesEvaluatorType2());
        ruleEvaluators.add(new RulesEvaluatorType3());
        for (RuleEvaluator ruleEvaluator : ruleEvaluators) {
            executor.execute(new Thread(ruleEvaluator));
        }
    }
}
