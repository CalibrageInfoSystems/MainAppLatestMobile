package com.oilpalm3f.mainapp.palmcare;

public class NotVisitedPlotsInfo {
    private String plotCode;
    private String farmerCode;
    private String FarmerName;
    private String villageName;
    private String ClusterName;
    private String TotalPalmArea;
    private String lastvisiteddate;
    private String VisitedBy;

    public String getPlotCode() {
        return plotCode;
    }

    public void setPlotCode(String plotCode) {
        this.plotCode = plotCode;
    }

    public String getFarmerCode() {
        return farmerCode;
    }

    public void setFarmerCode(String farmerCode) {
        this.farmerCode = farmerCode;
    }

    public String getFarmerName() {
        return FarmerName;
    }

    public void setFarmerName(String farmerName) {
        FarmerName = farmerName;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getClusterName() {
        return ClusterName;
    }

    public void setClusterName(String clusterName) {
        ClusterName = clusterName;
    }

    public String getTotalPalmArea() {
        return TotalPalmArea;
    }

    public void setTotalPalmArea(String totalPalmArea) {
        TotalPalmArea = totalPalmArea;
    }



    public String getVisitedBy() {
        return VisitedBy;
    }

    public void setVisitedBy(String visitedBy) {
        VisitedBy = visitedBy;
    }

    public String getLastvisiteddate() {
        return lastvisiteddate;
    }

    public void setLastvisiteddate(String lastvisiteddate) {
        this.lastvisiteddate = lastvisiteddate;
    }
}
