package entity;

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

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
