package com.cis.palm360;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Plot_maps {
    private List<LatLng> coordinates;
    private boolean isHighlighted;
    private String plotCode; // Add plotCode attribute

    public Plot_maps() {
        coordinates = new ArrayList<>();
    }

    public void addCoordinate(LatLng coordinate) {
        coordinates.add(coordinate);
    }

    public List<LatLng> getCoordinates() {
        return coordinates;
    }

    public void setHighlighted(boolean highlighted) {
        isHighlighted = highlighted;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public String getPlotCode() { // Add getter for plotCode
        return plotCode;
    }

    public void setPlotCode(String plotCode) { // Add setter for plotCode
        this.plotCode = plotCode;
    }

    @Override
    public String toString() {
        return "Plot_maps{" +
                "coordinates=" + coordinates +
                ", isHighlighted=" + isHighlighted +
                ", plotCode='" + plotCode + '\'' + // Include plotCode in toString
                '}';
    }
}
