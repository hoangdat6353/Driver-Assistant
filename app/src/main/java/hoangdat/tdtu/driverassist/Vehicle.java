package hoangdat.tdtu.driverassist;

import java.io.Serializable;

public class Vehicle implements Serializable {
    private String vehID;
    private String vehBrand;
    private String vehType;
    private String vehName;
    private String vehPlate;
    private String vehNote;
    private Integer vehTankCapacity;

    public Vehicle(String vehID, String vehName, String vehBrand, String vehType, String vehPlate, String vehNote, Integer vehTankCapacity) {
        this.vehID = vehID;
        this.vehBrand = vehBrand;
        this.vehType = vehType;
        this.vehName = vehName;
        this.vehPlate = vehPlate;
        this.vehNote = vehNote;
        this.vehTankCapacity = vehTankCapacity;
    }
    public Vehicle() { }

    public Integer getVehTankCapacity() {
        return vehTankCapacity;
    }

    public void setVehTankCapacity(Integer vehTankCapacity) {
        this.vehTankCapacity = vehTankCapacity;
    }

    public String getVehID() {
        return vehID;
    }

    public void setVehID(String vehID) {
        this.vehID = vehID;
    }

    public String getVehBrand() {
        return vehBrand;
    }

    public void setVehBrand(String vehBrand) {
        this.vehBrand = vehBrand;
    }

    public String getVehType() {
        return vehType;
    }

    public void setVehType(String vehType) {
        this.vehType = vehType;
    }

    public String getVehName() {
        return vehName;
    }

    public void setVehName(String vehName) {
        this.vehName = vehName;
    }

    public String getVehPlate() {
        return vehPlate;
    }

    public void setVehPlate(String vehPlate) {
        this.vehPlate = vehPlate;
    }

    public String getVehNote() {
        return vehNote;
    }

    public void setVehNote(String vehNote) {
        this.vehNote = vehNote;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehID='" + vehID + '\'' +
                ", vehBrand='" + vehBrand + '\'' +
                ", vehType='" + vehType + '\'' +
                ", vehName='" + vehName + '\'' +
                ", vehPlate='" + vehPlate + '\'' +
                ", vehNote='" + vehNote + '\'' +
                ", vehTankCapacity=" + vehTankCapacity +
                '}';
    }
}
