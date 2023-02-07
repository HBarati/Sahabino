import ruleChecking.RuleEvaluator;
import ruleChecking.RulesEvaluatorDGIM;
import ruleChecking.RulesEvaluatorType1;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * The Main method make thread to the number of rules and run them multi threaded with executor in thread pools.
 */
public class Main {
    public static void main(String[] args) {
        Executor executor = Executors.newCachedThreadPool();
        List<RuleEvaluator> ruleEvaluators = new ArrayList<>();
        try {
            ruleEvaluators.add(new RulesEvaluatorDGIM());
            ruleEvaluators.add(new RulesEvaluatorType1());
//            ruleEvaluators.add(new RulesEvaluatorType2());
//            ruleEvaluators.add(new RulesEvaluatorType3());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (RuleEvaluator ruleEvaluator : ruleEvaluators) {
            executor.execute(new Thread(ruleEvaluator));
        }
    }
}
