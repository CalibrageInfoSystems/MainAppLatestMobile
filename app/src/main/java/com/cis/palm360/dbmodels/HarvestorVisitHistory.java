package com.cis.palm360.dbmodels;

public class HarvestorVisitHistory {

    private int Id;
    private String Code;
    private String Name;
    private String PlotCode;
    private int CreatedByUserId;
    private String CreatedDate;
    private int ServerUpdatedStatus;
    private boolean IsVerified;
    private String OTP;
    private int UpdatedByUserId;
    private String UpdatedDate;
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPlotCode() {
        return PlotCode;
    }

    public void setPlotCode(String plotCode) {
        PlotCode = plotCode;
    }

    public int getCreatedByUserId() {
        return CreatedByUserId;
    }

    public void setCreatedByUserId(int createdByUserId) {
        CreatedByUserId = createdByUserId;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public int getServerUpdatedStatus() {
        return ServerUpdatedStatus;
    }

    public void setServerUpdatedStatus(int serverUpdatedStatus) {
        ServerUpdatedStatus = serverUpdatedStatus;
    }

    public boolean getIsVerified() {
        return IsVerified;
    }

    public void setIsVerified(boolean isVerified) {
        IsVerified = isVerified;
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    public int getUpdatedByUserId() {
        return UpdatedByUserId;
    }

    public void setUpdatedByUserId(int updatedByUserId) {
        UpdatedByUserId = updatedByUserId;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }
}
