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

    public String getRate() {
        return rate;
    }

}
