package entity;
/**
 * Alert model type 3 use when we have more logs that has rate number of log in duration as componentName
 * and has componentName and rate for make alert in Data Base
 */
public class AlertModel3 implements AlertModel{
    private String componentName;
    private String rate;

    public AlertModel3(String componentName, String rate) {
        this.componentName = componentName;
        this.rate = rate;
    }

    public String getComponentName() {
        return componentName;
    }

    public String getRate() {
        return rate;
    }

}
