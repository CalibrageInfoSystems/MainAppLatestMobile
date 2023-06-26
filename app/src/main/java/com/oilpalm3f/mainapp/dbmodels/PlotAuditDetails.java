package com.oilpalm3f.mainapp.dbmodels;

public class PlotAuditDetails {

    private double TotalPalmArea;
    private String CropVareity;
    private String DateofPlanting;
    private String ClusterName;
    private String VillageName;
    private String MandalName;
    private String DistrictName;

    public double getTotalPalmArea() {
        return TotalPalmArea;
    }

    public void setTotalPalmArea(double totalPalmArea) {
        TotalPalmArea = totalPalmArea;
    }

    public String getCropVareity() {
        return CropVareity;
    }

    public void setCropVareity(String cropVareity) {
        CropVareity = cropVareity;
    }

    public String getDateofPlanting() {
        return DateofPlanting;
    }

    public void setDateofPlanting(String dateofPlanting) {
        DateofPlanting = dateofPlanting;
    }

    public String getClusterName() {
        return ClusterName;
    }

    public void setClusterName(String clusterName) {
        ClusterName = clusterName;
    }

    public String getVillageName() {
        return VillageName;
    }

    public void setVillageName(String villageName) {
        VillageName = villageName;
    }

    public String getMandalName() {
        return MandalName;
    }

    public void setMandalName(String mandalName) {
        MandalName = mandalName;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    public void setDistrictName(String districtName) {
        DistrictName = districtName;
    }
}
