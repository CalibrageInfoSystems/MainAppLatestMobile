package com.oilpalm3f.mainapp.dbmodels;

import java.util.Date;

public class PlotInfo {
    private String farmerCode;
    private String farmerName;
    private String plotCode;
    private String dateOfPlanting;
    private String status;
    private double totalPlotArea;
    private double totalPalmArea;
    private double gpsPlotArea;
    private double leftOutArea;
    private double plotDifference;
    private int villageId;
    private String villageCode;
    private String villageName;
    private int mandalId;
    private String mandalCode;
    private String mandalName;
    private int districtId;
    private String districtCode;
    private String districtName;
    private int stateId;
    private String stateCode;
    private String stateName;

    // Getters and Setters
    // ...
    // Example:
    public String getFarmerCode() { return farmerCode; }
    public void setFarmerCode(String farmerCode) { this.farmerCode = farmerCode; }

    public String getFarmerName() { return farmerName; }
    public void setFarmerName(String farmerName) { this.farmerName = farmerName; }

    public String getPlotCode() { return plotCode; }
    public void setPlotCode(String plotCode) { this.plotCode = plotCode; }

    public String getDateOfPlanting() { return dateOfPlanting; }
    public void setDateOfPlanting(String dateOfPlanting) { this.dateOfPlanting = dateOfPlanting; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getTotalPlotArea() { return totalPlotArea; }
    public void setTotalPlotArea(double totalPlotArea) { this.totalPlotArea = totalPlotArea; }

    public double getTotalPalmArea() { return totalPalmArea; }
    public void setTotalPalmArea(double totalPalmArea) { this.totalPalmArea = totalPalmArea; }

    public double getGpsPlotArea() { return gpsPlotArea; }
    public void setGpsPlotArea(double gpsPlotArea) { this.gpsPlotArea = gpsPlotArea; }

    public double getLeftOutArea() { return leftOutArea; }
    public void setLeftOutArea(double leftOutArea) { this.leftOutArea = leftOutArea; }

    public double getPlotDifference() { return plotDifference; }
    public void setPlotDifference(double plotDifference) { this.plotDifference = plotDifference; }

    public int getVillageId() { return villageId; }
    public void setVillageId(int villageId) { this.villageId = villageId; }

    public String getVillageCode() { return villageCode; }
    public void setVillageCode(String villageCode) { this.villageCode = villageCode; }

    public String getVillageName() { return villageName; }
    public void setVillageName(String villageName) { this.villageName = villageName; }

    public int getMandalId() { return mandalId; }
    public void setMandalId(int mandalId) { this.mandalId = mandalId; }

    public String getMandalCode() { return mandalCode; }
    public void setMandalCode(String mandalCode) { this.mandalCode = mandalCode; }

    public String getMandalName() { return mandalName; }
    public void setMandalName(String mandalName) { this.mandalName = mandalName; }

    public int getDistrictId() { return districtId; }
    public void setDistrictId(int districtId) { this.districtId = districtId; }

    public String getDistrictCode() { return districtCode; }
    public void setDistrictCode(String districtCode) { this.districtCode = districtCode; }

    public String getDistrictName() { return districtName; }
    public void setDistrictName(String districtName) { this.districtName = districtName; }

    public int getStateId() { return stateId; }
    public void setStateId(int stateId) { this.stateId = stateId; }

    public String getStateCode() { return stateCode; }
    public void setStateCode(String stateCode) { this.stateCode = stateCode; }

    public String getStateName() { return stateName; }
    public void setStateName(String stateName) { this.stateName = stateName; }

    @Override
    public String toString() {
        return "PlotInfo{" +
                "farmerCode='" + farmerCode + '\'' +
                ", farmerName='" + farmerName + '\'' +
                ", plotCode='" + plotCode + '\'' +
                ", dateOfPlanting=" + dateOfPlanting +
                ", status='" + status + '\'' +
                ", totalPlotArea=" + totalPlotArea +
                ", totalPalmArea=" + totalPalmArea +
                ", gpsPlotArea=" + gpsPlotArea +
                ", leftOutArea=" + leftOutArea +
                ", plotDifference=" + plotDifference +
                ", villageId=" + villageId +
                ", villageCode='" + villageCode + '\'' +
                ", villageName='" + villageName + '\'' +
                ", mandalId=" + mandalId +
                ", mandalCode='" + mandalCode + '\'' +
                ", mandalName='" + mandalName + '\'' +
                ", districtId=" + districtId +
                ", districtCode='" + districtCode + '\'' +
                ", districtName='" + districtName + '\'' +
                ", stateId=" + stateId +
                ", stateCode='" + stateCode + '\'' +
                ", stateName='" + stateName + '\'' +
                '}';
    }
}
