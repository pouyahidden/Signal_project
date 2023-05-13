package info.vprofit.app.Model;

public class TicketModel {


    private String id;
    private String ticket_id;
    private String subject;
    private String humanDate;
    private String department;
    private String priority;
    private String message;

    public TicketModel(String id,String ticket_id, String subject,String humanDate,String department,String priority,String message) {
        this.id = id;
        this.ticket_id = ticket_id;
        this.subject = subject;
        this.humanDate = humanDate;
        this.department = department;
        this.priority = priority;
        this.message=message;
    }


    public String getId() {
        return id;
    }

    public String setId(String id) {
        this.id = id;
        return id;
    }

    public String getTicket_id() {
        return ticket_id;
    }

    public String setTicket_id(String ticket_id) {
        this.ticket_id = ticket_id;
        return ticket_id;
    }

    public String getSubject() {
        return subject;
    }

    public String setSubject(String subject) {
        this.subject = subject;
        return subject;
    }
    public String getHumanDate() {
        return humanDate;
    }

    public String setHumanDate(String humanDate) {
        this.humanDate = humanDate;
        return humanDate;
    }
    public String getDepartment() {
        return department;
    }

    public String setDepartment(String department) {
        this.department = department;
        return department;
    }

    public String getPriority() {
        return priority;
    }

    public String setPriority(String priority) {
        this.priority = priority;
        return priority;
    }

    public String getMessage() {
        return message;
    }

    public String setMessage(String message) {
        this.message = message;
        return message;
    }

}



