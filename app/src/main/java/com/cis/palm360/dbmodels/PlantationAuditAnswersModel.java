package com.cis.palm360.dbmodels;

public class PlantationAuditAnswersModel {

    private int Id;
    private String PlotCode;
    private int QuestionId;
    private int OptionId;
    private String Value;
    private int IsActive;
    private int CreatedByUserId;
    private String CreatedDate;
    private int ServerUpdatedStatus;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getPlotCode() {
        return PlotCode;
    }

    public void setPlotCode(String plotCode) {
        PlotCode = plotCode;
    }

    public int getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(int questionId) {
        QuestionId = questionId;
    }

    public int getOptionId() {
        return OptionId;
    }

    public void setOptionId(int optionId) {
        OptionId = optionId;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public int getIsActive() {
        return IsActive;
    }

    public void setIsActive(int isActive) {
        IsActive = isActive;
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
}
