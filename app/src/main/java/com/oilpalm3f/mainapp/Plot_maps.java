package com.oilpalm3f.mainapp;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class Plot_maps {
    private List<LatLng> coordinates;
    private boolean isHighlighted;

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

    @Override
    public String toString() {
        return "Plot_maps{" +
                "coordinates=" + coordinates +
                ", isHighlighted=" + isHighlighted +
                '}';
    }
}


