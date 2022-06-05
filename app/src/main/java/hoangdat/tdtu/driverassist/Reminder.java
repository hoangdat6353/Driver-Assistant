package hoangdat.tdtu.driverassist;

public class Reminder {
    String reminderId;
    String vehOwner;
    String vehName;
    String serviceName;
    String serviceNote;
    String serviceDay;
    String serviceTime;

    public Reminder(String reminderId, String vehOwner, String vehName, String serviceName, String serviceNote, String serviceDay, String serviceTime) {
        this.reminderId = reminderId;
        this.vehOwner = vehOwner;
        this.vehName = vehName;
        this.serviceName = serviceName;
        this.serviceNote = serviceNote;
        this.serviceDay = serviceDay;
        this.serviceTime = serviceTime;
    }


    public Reminder() {}

    public String getReminderId() {
        return reminderId;
    }

    public void setReminderId(String reminderId) {
        this.reminderId = reminderId;
    }

    public String getVehOwner() {
        return vehOwner;
    }

    public void setVehOwner(String vehOwner) {
        this.vehOwner = vehOwner;
    }

    public String getVehName() {
        return vehName;
    }

    public void setVehName(String vehName) {
        this.vehName = vehName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceNote() {
        return serviceNote;
    }

    public void setServiceNote(String serviceNote) {
        this.serviceNote = serviceNote;
    }

    public String getServiceDay() {
        return serviceDay;
    }

    public void setServiceDay(String serviceDay) {
        this.serviceDay = serviceDay;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }
}
