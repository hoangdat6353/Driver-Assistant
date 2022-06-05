package hoangdat.tdtu.driverassist;

import java.io.Serializable;

public class PartObject implements Serializable {
    String changeOilID;
    String vehID;
    String vehName;
    String vehOwner;
    Integer Price;
    String Gear;
    Integer Quantily;
    String Address;
    String Note;
    String Date;

    public PartObject(String changeOilID, String vehID, String vehName, String vehOwner, Integer price, String gear, Integer quantily, String address, String note, String date) {
        this.changeOilID = changeOilID;
        this.vehID = vehID;
        this.vehName = vehName;
        this.vehOwner = vehOwner;
        Price = price;
        Gear = gear;
        Quantily = quantily;
        Address = address;
        Note = note;
        Date = date;
    }
    public PartObject () {}

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

    public String getGear() {
        return Gear;
    }

    public void setGear(String gear) {
        Gear = gear;
    }

    public Integer getQuantily() {
        return Quantily;
    }

    public void setQuantily(Integer quantily) {
        Quantily = quantily;
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
