package com.oilpalm3f.mainapp;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.oilpalm3f.mainapp.database.DataAccessHandler;
import com.oilpalm3f.mainapp.database.Queries;
//import com.karumi.dexter.Dexter;
//import com.karumi.dexter.PermissionToken;
//import com.karumi.dexter.listener.PermissionDeniedResponse;
//import com.karumi.dexter.listener.PermissionGrantedResponse;
//import com.karumi.dexter.listener.PermissionRequest;
//import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class ViewmapsActivity extends AppCompatActivity implements OnMapReadyCallback, SeekBar.OnSeekBarChangeListener {

    FusedLocationProviderClient fusedLocationProviderClient;
    SupportMapFragment smf;

    private GoogleMap mMap;
    float polylineWidth = 10f;
    ArrayList<LatLng> arrayList = new ArrayList<>();
    private DataAccessHandler dataAccessHandler;
    private LinkedHashMap LatlongDataMap;
    String selectedPlotCode;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewmaps);

        // Ensure the correct fragment is used
        smf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        dataAccessHandler = new DataAccessHandler(this);
        selectedPlotCode = getIntent().getStringExtra("plotcode");
        Log.e("======selectedPlotCode",selectedPlotCode);
        if (smf != null) {
            smf.getMapAsync(this);
        }
        getmylocation();

    }

    private void getmylocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    smf.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
//                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                            for (LatLng point : arrayList) {
//                                googleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
//                            }
//                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
//                            drawPolyline(googleMap);
                            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//                        googleMap.setOnInfoWindowClickListener(this);

                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18.0f));

                            //   LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            //     MarkerOptions markerOptions = new MarkerOptions().position(currentLocation).title("You are here....!");
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(currentLocation)
                                    .title("Your are here")
                                    .icon(bitmapDescriptorFromVector(ViewmapsActivity.this, R.drawable.male_2));
                            //     for (int i = 0; i < arrayList.size(); i++) {
                            //    googleMap.addMarker(new MarkerOptions().position(arrayList.get(i)).title("Marker"));
                            //  googleMap.animateCamera(CameraUpdateFactory.zoomTo(19.0f));
                            //   googleMap.moveCamera(CameraUpdateFactory.newLatLng(arrayList.get(i)));
                            //  }
                            googleMap.addMarker(markerOptions);
                            // googleMap.addMarker(markerOptions);
                           // List<Plot_maps> plots = getPlots();

                          //  String[] plotCodes = {"APT02923262001", "APT30523355001",};
                            List<String> plotCodes=  dataAccessHandler.getSingleListData(Queries.getInstance().getplotslist());
                            Log.e("======plotCodes",plotCodes.size()+"");
                            List<Plot_maps> plots = getPlots(selectedPlotCode, plotCodes);
                            // Draw each plot on the map
                            for (Plot_maps plot : plots) {
                                drawPlot(googleMap, plot);
                            }


                            //  googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                            //  drawPolyline(googleMap);
                            //  drawPolylineAndPolygon(googleMap);

                        }
                    });
                }
            }
        });

    }




    private void drawPlot(GoogleMap googleMap, Plot_maps plot) {
        if (googleMap == null || plot == null || plot.getCoordinates() == null || plot.getCoordinates().isEmpty()) {
            return;
        }

        // Create a list to store the LatLng points
        List<LatLng> latLngList = new ArrayList<>();
        for (LatLng coordinate : plot.getCoordinates()) {
            latLngList.add(new com.google.android.gms.maps.model.LatLng(coordinate.latitude, coordinate.longitude));
        }

        // Add the first point again to close the polyline loop
        latLngList.add(latLngList.get(0));

        // Define a dashed pattern
        List<PatternItem> pattern = Arrays.asList(
                new Dash(10), new Gap(5)  // Adjust the length and gap as needed
        );

        // Determine the colors based on whether the plot is highlighted
        int polylineColor;
        int polygonFillColor;

        if (plot.isHighlighted()) {
            polylineColor = Color.WHITE;
            polygonFillColor = Color.argb(100, 0, 255, 0); // Semi-transparent green
        } else {
            polylineColor = Color.WHITE;
            polygonFillColor = Color.argb(150, 0, 0, 0); // Semi-transparent black

        }

        // Create and add the polyline with the dashed pattern
        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(latLngList)
                .color(polylineColor)  // Set the polyline color
                .width(5)  // Set the polyline width
                .pattern(pattern);  // Set the pattern to dashed

        googleMap.addPolyline(polylineOptions);

        // Create and add the polygon with the determined fill color
        PolygonOptions polygonOptions = new PolygonOptions()
                .addAll(latLngList)
                .strokeColor(Color.TRANSPARENT)  // Set the border color of the polygon
                .strokeWidth(5)  // Set the border width
                .fillColor(polygonFillColor);  // Set the fill color with transparency

        LatLng centroid = calculateCentroid(latLngList);
        googleMap.addMarker(new MarkerOptions().position(centroid).infoWindowAnchor(0.5f, 0.6f));

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                // Use the default InfoWindow frame
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View infoWindow = getLayoutInflater().inflate(R.layout.close_crop_item, null);
                try {
                    if (marker.isInfoWindowShown()) {
                        marker.hideInfoWindow();
                    } else {
                        TextView title = infoWindow.findViewById(R.id.Cropcode);
                        TextView snippet = infoWindow.findViewById(R.id.plotid);

                        title.setText(marker.getTitle());
                        snippet.setText("This is a custom popup!");

                        // marker.showInfoWindow();
                    }
                } catch (NullPointerException e) {
                    // Log.e(TAG, "onClick: NullPointerException: " + e.getMessage() );
                }

                // Inflate the custom layout
                return infoWindow;
            }
        });

        googleMap.addPolygon(polygonOptions);
    }



    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    private LatLng calculateCentroid(List<LatLng> latLngList) {
        double latitudeSum = 0;
        double longitudeSum = 0;
        int n = latLngList.size();

        for (LatLng latLng : latLngList) {
            latitudeSum += latLng.latitude;
            longitudeSum += latLng.longitude;
        }

        return new LatLng(latitudeSum / n, longitudeSum / n);
    }
    private List<Plot_maps> getPlots(String selectedPlotCode, List<String> plotCodes) {
        List<Plot_maps> plots = new ArrayList<>();
        LinkedHashMap<String, List<LatLng>> latlongDataMap;

        try {
            // Execute the query to get latitude and longitude data
            String query = Queries.getInstance().getLatlongs(plotCodes);
            latlongDataMap = dataAccessHandler.getGenericDataa(query);

            // Log the raw data map
            Log.d("getPlots", "Raw data map: " + latlongDataMap);

            // Check if the result is not empty
            if (latlongDataMap != null && !latlongDataMap.isEmpty()) {
                for (Map.Entry<String, List<LatLng>> entry : latlongDataMap.entrySet()) {
                    String plotCode = entry.getKey();
                    List<LatLng> coordinates = entry.getValue();
                    Plot_maps plot = new Plot_maps();

                    for (LatLng coordinate : coordinates) {
                        plot.addCoordinate(coordinate);
                    }

                    // Check if the current plot is the selected one
                    if (plotCode.equals(selectedPlotCode)) {
                        plot.setHighlighted(true);  // Custom method to set the plot as highlighted
                    } else {
                        plot.setHighlighted(false);  // Custom method to set the plot as shaded out
                    }

                    plots.add(plot);
                    Log.d("getPlots", "New plot added with coordinates: " + plot.getCoordinates());
                }
            } else {
                Log.e("getPlots", "latlongDataMap is null or empty.");
            }
        } catch (Exception e) {
            Log.e("getPlots", "Exception occurred: " + e.getMessage(), e);
        }

        Log.d("getPlots", "Total plots: " + plots.size());
        return plots;
    }

//    private List<Plot_maps> getPlots() {
//        List<Plot_maps> plots = new ArrayList<>();
//         LinkedHashMap LatlongDataMap;
//        LatlongDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getlatlongs());
//        // Example data for multiple plots
//        double[][][] plotData = {
//                {
//                        {17.456467, 78.386942}, {17.456453, 78.387367}, {17.456438, 78.387822},
//                        {17.456605, 78.388034}, {17.457112, 78.387974}, {17.457379, 78.387875},
//                        {17.457358, 78.387503}, {17.457372, 78.386965}, {17.457387, 78.386472},
//                        {17.456931, 78.386388}, {17.456453, 78.386927},
//                },
//                {
//                        {17.454617, 78.3866588}, {17.45395154, 78.38611736}, {17.45328097, 78.38603959},
//                        {17.45274825, 78.38598421}, {17.4525477, 78.38647058}, {17.45251001, 78.38700861},
//                        {17.45242317, 78.38780234}, {17.45236059, 78.38848883}, {17.45275582, 78.38884636},
//                        {17.45324637, 78.38889032}, {17.45378583, 78.38896504}, {17.45402878, 78.38833395},
//                        {17.45418307, 78.38759149}, {17.45439311, 78.38698147}
//                }
//                // Add more plot data as needed
//        };
//
//        for (double[][] plot : plotData) {
//            Plot_maps newPlot = new Plot_maps();
//            for (double[] coordinate : plot) {
//                newPlot.addCoordinate(new LatLng(coordinate[0], coordinate[1]));
//            }
//            plots.add(newPlot);
//        }
//
//        return plots;
//    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                // Handle map click
            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // Handle progress change
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Handle start tracking touch
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // Handle stop tracking touch
    }
}



