package entity;

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

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
