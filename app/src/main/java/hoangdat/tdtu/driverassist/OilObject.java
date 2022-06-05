package hoangdat.tdtu.driverassist;

import java.io.Serializable;

public class OilObject implements Serializable {
    String changeOilID;
    String vehID;
    String vehName;
    String vehOwner;
    Integer Price;
    Integer Capacity;
    Integer KM;
    String Address;
    String Note;
    String Date;

    public OilObject(String changeOilID, String vehID, String vehName, String vehOwner, Integer price, Integer capacity, Integer KM, String address, String note, String date) {
        this.changeOilID = changeOilID;
        this.vehID = vehID;
        this.vehName = vehName;
        this.vehOwner = vehOwner;
        Price = price;
        Capacity = capacity;
        this.KM = KM;
        Address = address;
        Note = note;
        Date = date;
    }
    public OilObject() {}

    public String getChangeOilID() {
        return changeOilID;
    }

    public void setChangeOilID(String changeOilID) {
        this.changeOilID = changeOilID;
    }

    public String getVehID() {
        return vehID;
    }

    public void setVehID(String vehID) {
        this.vehID = vehID;
    }

    public String getVehName() {
        return vehName;
    }

    public void setVehName(String vehName) {
        this.vehName = vehName;
    }

    public String getVehOwner() {
        return vehOwner;
    }

    public void setVehOwner(String vehOwner) {
        this.vehOwner = vehOwner;
    }

    public Integer getPrice() {
        return Price;
    }

    public void setPrice(Integer price) {
        Price = price;
    }

    public Integer getCapacity() {
        return Capacity;
    }

    public void setCapacity(Integer capacity) {
        Capacity = capacity;
    }

    public Integer getKM() {
        return KM;
    }

    public void setKM(Integer KM) {
        this.KM = KM;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
