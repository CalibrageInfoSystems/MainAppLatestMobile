package com.cis.palm360.dbmodels;

public class RecoveryFarmerModelnew {

    private String Code;
    private String FirstName;
    private String LastName;
    private String MiddleName;
    private String ContactNumber;
    private String MandalName;
    private String VillageName;
    private String DistrictName;
    private String StateName;
    private String PalmArea;

    public RecoveryFarmerModelnew(String code,String name ,String VillageName, String MandalName,String DistrictName,String StateName,String PalmArea) {
        this.Code = code;
        this.VillageName = VillageName;
        this.FirstName = name;
        this.MandalName = MandalName;
        this.DistrictName = DistrictName;
        this.StateName  = StateName;
        this.PalmArea = PalmArea;

    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public String getMandalName() {
        return MandalName;
    }

    public void setMandalName(String mandalName) {
        MandalName = mandalName;
    }

    public String getVillageName() {
        return VillageName;
    }

    public void setVillageName(String villageName) {
        VillageName = villageName;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    public void setDistrictName(String districtName) {
        DistrictName = districtName;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }

    public String getPalmArea() {
        return PalmArea;
    }

    public void setPalmArea(String palmArea) {
        PalmArea = palmArea;
    }
}
