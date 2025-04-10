package com.cis.palm360.dbmodels;

public class PlantationAuditQuestionsModel {

    private int Id;
    private String Question;
    private int QuestionTypeId;
    private int IsActive;;
    private String CreatedDate;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public int getQuestionTypeId() {
        return QuestionTypeId;
    }

    public void setQuestionTypeId(int questionTypeId) {
        QuestionTypeId = questionTypeId;
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
