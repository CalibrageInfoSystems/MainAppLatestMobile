package com.oilpalm3f.mainapp.dbmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PlotGapFillingDetails {

//    @SerializedName("Id")
//    @Expose
//    private Integer id;
    @SerializedName("PlotCode")
    @Expose
    private String plotCode;
    @SerializedName("SaplingsToBeIssued")
    @Expose
    private Integer saplingsToBeIssued;
    @SerializedName("ImportedSaplingsToBeIssued")
    @Expose
    private Integer importedSaplingsToBeIssued;
    @SerializedName("IndigenousSaplingsToBeIssued")
    @Expose
    private Integer indigenousSaplingsToBeIssued;
    @SerializedName("ExpectedDateofPickup")
    @Expose
    private String expectedDateofPickup;
    @SerializedName("GapFillingReasonTypeId")
    @Expose
    private Integer gapFillingReasonTypeId;
    @SerializedName("IsApproved")
    @Expose
    private int isApproved;
    @SerializedName("IsDeclined")
    @Expose
    private int isDeclined;
    @SerializedName("Comments")
    @Expose
    private String comments;
    @SerializedName("IsActive")
    @Expose
    private int isActive;
    @SerializedName("FileName")
    @Expose
    private String fileName;
    @SerializedName("FileLocation")
    @Expose
    private String fileLocation;
    @SerializedName("FileExtension")
    @Expose
    private String fileExtension;
    @SerializedName("CreatedByUserId")
    @Expose
    private Integer createdByUserId;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("UpdatedByUserId")
    @Expose
    private Integer updatedByUserId;
    @SerializedName("UpdatedDate")
    @Expose
    private String updatedDate;
    @SerializedName("ApprovedByUserId")
    @Expose
    private Integer approvedByUserId;
    @SerializedName("ApprovedDate")
    @Expose
    private String approvedDate;
    @SerializedName("DeclinedByUserId")
    @Expose
    private Integer declinedByUserId;
    @SerializedName("DeclinedDate")
    @Expose
    private String declinedDate;
    @SerializedName("ApprovedComments")
    @Expose
    private String approvedComments;
    @SerializedName("DeclinedComments")
    @Expose
    private String declinedComments;
    @SerializedName("IsVerified")
    @Expose
    private Integer isVerified;
    @SerializedName("GapFillingApprovedStatusTypeId")
    @Expose
    private Integer gapFillingApprovedStatusTypeId;
    @SerializedName("GapFillingApprovedComments")
    @Expose
    private String gapFillingApprovedComments;
    @SerializedName("GapFillingRejectedStatusTypeId")
    @Expose
    private Integer gapFillingRejectedStatusTypeId;
    @SerializedName("GapFillingRejectedComments")
    @Expose
    private String gapFillingRejectedComments;
    @SerializedName("ServerUpdatedStatus")
    @Expose
    private int serverUpdatedStatus;

//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }

    public String getPlotCode() {
        return plotCode;
    }

    public void setPlotCode(String plotCode) {
        this.plotCode = plotCode;
    }

    public Integer getSaplingsToBeIssued() {
        return saplingsToBeIssued;
    }

    public void setSaplingsToBeIssued(Integer saplingsToBeIssued) {
        this.saplingsToBeIssued = saplingsToBeIssued;
    }

    public Integer getImportedSaplingsToBeIssued() {
        return importedSaplingsToBeIssued;
    }

    public void setImportedSaplingsToBeIssued(Integer importedSaplingsToBeIssued) {
        this.importedSaplingsToBeIssued = importedSaplingsToBeIssued;
    }

    public Integer getIndigenousSaplingsToBeIssued() {
        return indigenousSaplingsToBeIssued;
    }

    public void setIndigenousSaplingsToBeIssued(Integer indigenousSaplingsToBeIssued) {
        this.indigenousSaplingsToBeIssued = indigenousSaplingsToBeIssued;
    }

    public String getExpectedDateofPickup() {
        return expectedDateofPickup;
    }

    public void setExpectedDateofPickup(String expectedDateofPickup) {
        this.expectedDateofPickup = expectedDateofPickup;
    }

    public Integer getGapFillingReasonTypeId() {
        return gapFillingReasonTypeId;
    }

    public void setGapFillingReasonTypeId(Integer gapFillingReasonTypeId) {
        this.gapFillingReasonTypeId = gapFillingReasonTypeId;
    }

    public int getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(int isApproved) {
        this.isApproved = isApproved;
    }

    public int getIsDeclined() {
        return isDeclined;
    }

    public void setIsDeclined(int isDeclined) {
        this.isDeclined = isDeclined;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public Integer getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(Integer createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getUpdatedByUserId() {
        return updatedByUserId;
    }

    public void setUpdatedByUserId(Integer updatedByUserId) {
        this.updatedByUserId = updatedByUserId;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getApprovedByUserId() {
        return approvedByUserId;
    }

    public void setApprovedByUserId(Integer approvedByUserId) {
        this.approvedByUserId = approvedByUserId;
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
    }

    public Integer getDeclinedByUserId() {
        return declinedByUserId;
    }

    public void setDeclinedByUserId(Integer declinedByUserId) {
        this.declinedByUserId = declinedByUserId;
    }

    public String getDeclinedDate() {
        return declinedDate;
    }

    public void setDeclinedDate(String declinedDate) {
        this.declinedDate = declinedDate;
    }

    public String getApprovedComments() {
        return approvedComments;
    }

    public void setApprovedComments(String approvedComments) {
        this.approvedComments = approvedComments;
    }

    public String getDeclinedComments() {
        return declinedComments;
    }

    public void setDeclinedComments(String declinedComments) {
        this.declinedComments = declinedComments;
    }

    public Integer getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Integer isVerified) {
        this.isVerified = isVerified;
    }

    public Integer getGapFillingApprovedStatusTypeId() {
        return gapFillingApprovedStatusTypeId;
    }

    public void setGapFillingApprovedStatusTypeId(Integer gapFillingApprovedStatusTypeId) {
        this.gapFillingApprovedStatusTypeId = gapFillingApprovedStatusTypeId;
    }

    public String getGapFillingApprovedComments() {
        return gapFillingApprovedComments;
    }

    public void setGapFillingApprovedComments(String gapFillingApprovedComments) {
        this.gapFillingApprovedComments = gapFillingApprovedComments;
    }

    public Integer getGapFillingRejectedStatusTypeId() {
        return gapFillingRejectedStatusTypeId;
    }

    public void setGapFillingRejectedStatusTypeId(Integer gapFillingRejectedStatusTypeId) {
        this.gapFillingRejectedStatusTypeId = gapFillingRejectedStatusTypeId;
    }

    public String getGapFillingRejectedComments() {
        return gapFillingRejectedComments;
    }

    public void setGapFillingRejectedComments(String gapFillingRejectedComments) {
        this.gapFillingRejectedComments = gapFillingRejectedComments;
    }

    public int getServerUpdatedStatus() {
        return serverUpdatedStatus;
    }

    public void setServerUpdatedStatus(int serverUpdatedStatus) {
        this.serverUpdatedStatus = serverUpdatedStatus;
    }

}
