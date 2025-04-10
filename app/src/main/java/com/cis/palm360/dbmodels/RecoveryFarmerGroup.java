package com.cis.palm360.dbmodels;

public class RecoveryFarmerGroup {

    // private int Id;
    private String FarmerCode;
    private String RecoveryFarmercode;
    private int CreatedByUserId;
    private String CreatedDate;
    private int UpdatedByUserId;
    private String UpdatedDate;
    private int IsActive;
    private int ServerUpdatedStatus;

//    public int getId() {
//        return Id;
//    }
//
//    public void setId(Integer id) {
//        Id = id;
//    }

    public String getFarmerCode() {
        return FarmerCode;
    }

    public void setFarmerCode(String farmerCode) {
        FarmerCode = farmerCode;
    }

    public String getRecoveryFarmercode() {
        return RecoveryFarmercode;
    }

    public void setRecoveryFarmercode(String recoveryFarmercode) {
        RecoveryFarmercode = recoveryFarmercode;
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

    public int getIsActive() {
        return IsActive;
    }

    public void setIsActive(int isActive) {
        IsActive = isActive;
    }

    public int getServerUpdatedStatus() {
        return ServerUpdatedStatus;
    }

    public void setServerUpdatedStatus(int serverUpdatedStatus) {
        ServerUpdatedStatus = serverUpdatedStatus;
    }
}
