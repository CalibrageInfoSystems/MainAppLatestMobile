package com.oilpalm3f.mainapp.dbmodels;

import java.util.Date;

public class PlotGapFillingDetails {

//        private int id;
        private String plotCode;
        private int saplingsToBeIssued;
        private Integer importedSaplingsToBeIssued;
        private Integer indigenousSaplingsToBeIssued;
        private String expectedDateofPickup;
        private Integer gapFillingReasonTypeId;
        private Integer isApproved;
        private Integer isDeclined;
        private String comments;
        private int isActive;
        private String fileName;
        private String fileLocation;
        private String fileExtension;
        private int createdByUserId;
        private String createdDate;
        private int updatedByUserId;
        private String updatedDate;
        private Integer approvedByUserId;
        private String approvedDate;
        private Integer declinedByUserId;
        private String declinedDate;
        private String approvedComments;
        private String declinedComments;
        private int isVerified;
        private Integer gapFillingApprovedStatusTypeId;
        private String gapFillingApprovedComments;
        private Integer gapFillingRejectedStatusTypeId;
        private String gapFillingRejectedComments;
        private int serverUpdatedStatus;


//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }

        public String getPlotCode() {
            return plotCode;
        }

        public void setPlotCode(String plotCode) {
            this.plotCode = plotCode;
        }

        public int getSaplingsToBeIssued() {
            return saplingsToBeIssued;
        }

        public void setSaplingsToBeIssued(int saplingsToBeIssued) {
            this.saplingsToBeIssued = saplingsToBeIssued;
        }

        // Repeat the same pattern for other fields

        public int getServerUpdatedStatus() {
            return serverUpdatedStatus;
        }

        public void setServerUpdatedStatus(int serverUpdatedStatus) {
            this.serverUpdatedStatus = serverUpdatedStatus;
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

    public Integer getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Integer isApproved) {
        this.isApproved = isApproved;
    }

    public Integer getIsDeclined() {
        return isDeclined;
    }

    public void setIsDeclined(Integer isDeclined) {
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

    public int getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(int createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getUpdatedByUserId() {
        return updatedByUserId;
    }

    public void setUpdatedByUserId(int updatedByUserId) {
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

    public int getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(int isVerified) {
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
}
