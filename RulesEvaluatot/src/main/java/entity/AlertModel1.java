package entity;

/**
 * Alert model type 1 use when see a log from kafka and has priority and message for make alert in Data Base
 */
public class AlertModel1 implements AlertModel{

    private String priority ;
    private String message ;

    public AlertModel1(String priority, String message) {
        this.priority = priority;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getPriority() {
        return priority;
    }
}
