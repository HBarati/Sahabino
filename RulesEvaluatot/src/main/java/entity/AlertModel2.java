package entity;
/**
 * Alert model type 2 use when we have more logs that has rate number of log in duration as same componentName and priority
 * and has componentName and description and priority and rate for make alert in Data Base
 */
public class AlertModel2 implements AlertModel {
    private String componentName;
    private String description;
    private String priority ;
    private String rate;

    public AlertModel2(String componentName, String description, String priority, String rate) {
        this.componentName = componentName;
        this.description = description;
        this.priority = priority;
        this.rate = rate;
    }

    public String getComponentName() {
        return componentName;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }

    public String getRate() {
        return rate;
    }

}
