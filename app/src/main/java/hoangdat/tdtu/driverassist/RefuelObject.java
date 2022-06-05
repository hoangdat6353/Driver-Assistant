package hoangdat.tdtu.driverassist;

import java.io.Serializable;

public class RefuelObject implements Serializable {
    String refuelID;
    String vehID;
    String vehName;
    String vehOwner;
    Integer refuelPrice;
    Integer refuelCapacity;
    Integer refuelKM;
    String refuelAddress;
    String refuelNote;
    String refuelDate;

    public RefuelObject(String refuelID,String vehID, String vehName, String vehOwner, Integer refuelPrice, Integer refuelCapacity, Integer refuelKM, String refuelAddress, String refuelNote, String refuelDate) {
        this.refuelID = refuelID;
        this.vehID = vehID;
        this.vehName = vehName;
        this.vehOwner = vehOwner;
        this.refuelPrice = refuelPrice;
        this.refuelCapacity = refuelCapacity;
        this.refuelKM = refuelKM;
        this.refuelAddress = refuelAddress;
        this.refuelNote = refuelNote;
        this.refuelDate = refuelDate;
    }
    public RefuelObject() {}

    public String getRefuelID() {
        return refuelID;
    }

    public void setRefuelID(String refuelID) {
        this.refuelID = refuelID;
    }

    public String getRefuelDate() {
        return refuelDate;
    }

    public void setRefuelDate(String refuelDate) {
        this.refuelDate = refuelDate;
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

    public Integer getRefuelPrice() {
        return refuelPrice;
    }

    public void setRefuelPrice(Integer refuelPrice) {
        this.refuelPrice = refuelPrice;
    }

    public Integer getRefuelCapacity() {
        return refuelCapacity;
    }

    public void setRefuelCapacity(Integer refuelCapacity) {
        this.refuelCapacity = refuelCapacity;
    }

    public Integer getRefuelKM() {
        return refuelKM;
    }

    public void setRefuelKM(Integer refuelKM) {
        this.refuelKM = refuelKM;
    }

    public String getRefuelAddress() {
        return refuelAddress;
    }

    public void setRefuelAddress(String refuelAddress) {
        this.refuelAddress = refuelAddress;
    }

    public String getRefuelNote() {
        return refuelNote;
    }

    public void setRefuelNote(String refuelNote) {
        this.refuelNote = refuelNote;
    }
}
