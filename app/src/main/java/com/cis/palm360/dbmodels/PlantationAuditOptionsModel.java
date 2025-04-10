package com.cis.palm360.dbmodels;

public class PlantationAuditOptionsModel {

    private int Id;
    private int QuestionId;
    private String Option;
    private int IsActive;
    private String CreatedDate;



    public PlantationAuditOptionsModel(int Id, int QuestionId, String Option, int IsActive, String CreatedDate) {
        this.Id = Id;
        this.QuestionId = QuestionId;
        this.Option = Option;
        this.IsActive = IsActive;
        this.CreatedDate = CreatedDate;
    }


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(int questionId) {
        QuestionId = questionId;
    }

    public String getOption() {
        return Option;
    }

    @Override
    public String toString() {
        return Option; // Return the name property of the Option object
    }

    public void setOption(String option) {
        Option = option;
    }

    public int getIsActive() {
        return IsActive;
    }

    public void setIsActive(int isActive) {
        IsActive = isActive;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }
}
