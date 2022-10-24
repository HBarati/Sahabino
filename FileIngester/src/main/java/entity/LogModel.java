package entity;

public class LogModel {

    private String date;
    private String time ;
    private String threadId ;
    private String priority ;
    private String category ;
    private String message ;

    public LogModel() {
    }

    public LogModel(String date, String time, String threadId, String priority, String category, String message) {
        this.date = date;
        this.time = time;
        this.threadId = threadId;
        this.priority = priority;
        this.category = category;
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
