package com.cis.palm360.dbmodels;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AnnualTagetsKRA implements Serializable {

    @SerializedName("KRACode")
    private String kraCode;

    @SerializedName("KRAName")
    private String kraName;

    @SerializedName("UOM")
    private String uom;

    @SerializedName("EmployeeName")
    private String employeeName;

    @SerializedName("EmployeeCode")
    private String employeeCode;

    @SerializedName("AchievedTarget")
    private double achievedTarget;

    @SerializedName("Role")
    private String role;

    @SerializedName("Manager")
    private String manager;

    @SerializedName("States")
    private String states;

    @SerializedName("AnnualTarget")
    private double annualTarget;

    @SerializedName("FinancialYear")
    private String financialYear;

    @SerializedName("UserId")
    private int userId;

    @SerializedName("AnnualRating")
    private int annualRating;

    // Getters and Setters
//    public int getId() { return id; }
//    public void setId(int id) { this.id = id; }

    public String getKraCode() { return kraCode; }
    public void setKraCode(String kraCode) { this.kraCode = kraCode; }

    public String getKraName() { return kraName; }
    public void setKraName(String kraName) { this.kraName = kraName; }

    public String getUom() { return uom; }
    public void setUom(String uom) { this.uom = uom; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public String getEmployeeCode() { return employeeCode; }
    public void setEmployeeCode(String employeeCode) { this.employeeCode = employeeCode; }

    public double getAchievedTarget() { return achievedTarget; }
    public void setAchievedTarget(double achievedTarget) { this.achievedTarget = achievedTarget; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getManager() { return manager; }
    public void setManager(String manager) { this.manager = manager; }

    public String getStates() { return states; }
    public void setStates(String states) { this.states = states; }

    public double getAnnualTarget() { return annualTarget; }
    public void setAnnualTarget(double annualTarget) { this.annualTarget = annualTarget; }

    public String getFinancialYear() { return financialYear; }
    public void setFinancialYear(String financialYear) { this.financialYear = financialYear; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getAnnualRating() { return annualRating; }
    public void setAnnualRating(int annualRating) { this.annualRating = annualRating; }



}
