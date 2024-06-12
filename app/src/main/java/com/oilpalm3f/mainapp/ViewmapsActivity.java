package com.oilpalm3f.mainapp;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
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
import com.oilpalm3f.mainapp.common.CommonConstants;
import com.oilpalm3f.mainapp.database.DataAccessHandler;
import com.oilpalm3f.mainapp.database.Queries;

import com.oilpalm3f.mainapp.dbmodels.PlotInfo;
import com.oilpalm3f.mainapp.dbmodels.SoilResource;
import com.oilpalm3f.mainapp.ui.HomeScreen;
import com.oilpalm3f.mainapp.uihelper.ProgressBar;
//import com.karumi.dexter.Dexter;
//import com.karumi.dexter.PermissionToken;
//import com.karumi.dexter.listener.PermissionDeniedResponse;
//import com.karumi.dexter.listener.PermissionGrantedResponse;
//import com.karumi.dexter.listener.PermissionRequest;
//import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class ViewmapsActivity extends AppCompatActivity implements OnMapReadyCallback, SeekBar.OnSeekBarChangeListener {

    FusedLocationProviderClient fusedLocationProviderClient;
    SupportMapFragment smf;
    private GoogleMap mMap;
    private android.widget.ProgressBar progressBar;

    float polylineWidth = 10f;
    ArrayList<LatLng> arrayList = new ArrayList<>();
    private DataAccessHandler dataAccessHandler;
    private LinkedHashMap LatlongDataMap;
    String selectedPlotCode ,Selectedids;
    private PlotInfo plotinfo;
    private DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private  DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
    SearchView searchView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewmaps);

        // Initialize ProgressBar
        progressBar = findViewById(R.id.progress_bar);

        // Ensure the correct fragment is used
        smf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        dataAccessHandler = new DataAccessHandler(this);
        selectedPlotCode = getIntent().getStringExtra("plotcode");
    //    Selectedids = getIntent().getStringExtra("Villageids");
        Log.e("======selectedPlotCode", selectedPlotCode);
        Log.e("Selectedids", CommonConstants.SelectedvillageIds);
        plotinfo = (PlotInfo) dataAccessHandler.getplotinfoData(Queries.getInstance().getplotinfo(selectedPlotCode), 0);
        Log.e("======selectedplotinfo", plotinfo.getFarmerName());

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

        // Show the progress bar
        progressBar.setVisibility(View.VISIBLE);
        ProgressBar.showProgressBar(this, "Please wait...");
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    smf.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            mMap = googleMap;
                            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                            mMap.getUiSettings().setZoomControlsEnabled(true);

                            if (ActivityCompat.checkSelfPermission(ViewmapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ViewmapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            mMap.setMyLocationEnabled(true);
                            mMap.setMinZoomPreference(18.0f);
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(currentLocation)
                                    .title("You are here")
                                    .icon(bitmapDescriptorFromVector(ViewmapsActivity.this, R.drawable.male_2));
                            googleMap.addMarker(markerOptions);

                            List<String> plotCodes = dataAccessHandler.getSingleListData(Queries.getInstance().getplotslist(CommonConstants.SelectedvillageIds));
                            Log.e("======plotCodes", plotCodes.size() + "");
                            List<Plot_maps> plots = getPlots(selectedPlotCode, plotCodes);

                            for (Plot_maps plot : plots) {
                                drawPlot(googleMap, plot);
                            }

                            for (Plot_maps plot : plots) {
                                if (plot.isHighlighted()) {
                                    LatLng centroid = calculateCentroid(plot.getCoordinates());
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centroid, 18.0f));
                                    break;
                                }
                            }

                            // Hide the progress bar after drawing the plots
                            ProgressBar.hideProgressBar();
                        }
                    });
                } else {
                    // Hide the progress bar if location is null
                    ProgressBar.hideProgressBar();
                }
            }
        });
    }

    private void drawPlot(GoogleMap googleMap, Plot_maps plot) {
        if (googleMap == null || plot == null || plot.getCoordinates() == null || plot.getCoordinates().isEmpty()) {
            return;
        }

        List<LatLng> latLngList = new ArrayList<>();
        for (LatLng coordinate : plot.getCoordinates()) {
            latLngList.add(new com.google.android.gms.maps.model.LatLng(coordinate.latitude, coordinate.longitude));
        }

        latLngList.add(latLngList.get(0));

        List<PatternItem> pattern = Arrays.asList(
                new Dash(10), new Gap(5)
        );

        int polylineColor;
        int polygonFillColor;

        if (plot.isHighlighted()) {
            polylineColor = Color.WHITE;
            polygonFillColor = Color.argb(100, 0, 255, 0);
        } else {
            polylineColor = Color.WHITE;
            polygonFillColor = Color.argb(150, 0, 0, 0);
        }

        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(latLngList)
                .color(polylineColor)
                .width(5)
                .pattern(pattern);

        googleMap.addPolyline(polylineOptions);

        PolygonOptions polygonOptions = new PolygonOptions()
                .addAll(latLngList)
                .strokeColor(Color.TRANSPARENT)
                .strokeWidth(5)
                .fillColor(polygonFillColor);

        googleMap.addPolygon(polygonOptions);

        LatLng centroid = calculateCentroid(latLngList);

        Marker marker;
        if (plot.isHighlighted()) {
            marker = googleMap.addMarker(new MarkerOptions()
                    .position(centroid)
                    .title(selectedPlotCode)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .infoWindowAnchor(0.5f, 0.6f));
            marker.setTag(true);
        } else {
            marker = googleMap.addMarker(new MarkerOptions()
                    .position(centroid)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .anchor(0.5f, 0.5f)
                    .alpha(0.5f));
            marker.setTag(false);
        }

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View infoWindow = getLayoutInflater().inflate(R.layout.plotinfo, null);

                TextView landarea = infoWindow.findViewById(R.id.landarea);
                TextView plotid = infoWindow.findViewById(R.id.plotid);
                TextView plotdiff = infoWindow.findViewById(R.id.plotdiff);
                TextView farmername = infoWindow.findViewById(R.id.farmername);
                TextView Districtname = infoWindow.findViewById(R.id.Districtname);
                TextView villagename = infoWindow.findViewById(R.id.villagename);
                TextView gpsarea = infoWindow.findViewById(R.id.gpsarea);
                TextView mandalname = infoWindow.findViewById(R.id.mandalname);
                TextView palmarea = infoWindow.findViewById(R.id.palmarea);
                TextView arealeftout = infoWindow.findViewById(R.id.arealeftout);
                TextView statename = infoWindow.findViewById(R.id.statename);
                TextView dateofplantation = infoWindow.findViewById(R.id.dateofplantation);
                TextView status = infoWindow.findViewById(R.id.status);
                gpsarea.setText(Html.fromHtml("<font color='#000000'>Plot Area as per GPS (in Ha):</font> <b>" + plotinfo.getGpsPlotArea() + "</b>"));

               // gpsarea.setText(Html.fromHtml("<b>Plot Area as per GPS (in Ha):</b> " + plotinfo.getGpsPlotArea()));
                farmername.setText(Html.fromHtml("Farmer Name: <b>" + plotinfo.getFarmerName() + "</b>"));
                arealeftout.setText(Html.fromHtml("Area Left Out (in Ha): <b>" + plotinfo.getLeftOutArea() + "</b>"));
                villagename.setText(Html.fromHtml("Village Name: <b>" + plotinfo.getVillageName() + "</b>"));
                Districtname.setText(Html.fromHtml("District Name: <b>" + plotinfo.getDistrictName() + "</b>"));
                palmarea.setText(Html.fromHtml("Palm Area (in Ha): <b>" + plotinfo.getTotalPalmArea() + "</b>"));
                mandalname.setText(Html.fromHtml("Mandal Name: <b>" + plotinfo.getMandalName() + "</b>"));
                statename.setText(Html.fromHtml("State Name: <b>" + plotinfo.getStateName() + "</b>"));
                landarea.setText(Html.fromHtml("Land Area (in Ha): <b>" + plotinfo.getTotalPlotArea() + "</b>"));
                plotid.setText(Html.fromHtml("Plot Code: <b>" + plotinfo.getPlotCode() + "</b>"));
                status.setText(Html.fromHtml("Status: <b>" + plotinfo.getStatus() + "</b>"));
                plotdiff.setText(Html.fromHtml("Plot Difference (in Ha): <b>" + plotinfo.getPlotDifference() + "</b>"));


                if (plotinfo.getDateOfPlanting() != null) {
                    String dateofPlanting = plotinfo.getDateOfPlanting().replace("T", " ");
                    Date date = null;
                    try {
                        date = inputFormat.parse(dateofPlanting);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String outputDate = outputFormat.format(date);
                    dateofplantation.setText(Html.fromHtml("Date of Planting:<b>" + outputDate + "</b>"));
                }
                return infoWindow;
            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Boolean isClickable = (Boolean) marker.getTag();
                return isClickable == null || !isClickable;
            }
        });
    }

    private LatLng calculateCentroid(List<LatLng> latLngList) {
        double latitude = 0;
        double longitude = 0;
        int count = latLngList.size();

        for (LatLng latLng : latLngList) {
            latitude += latLng.latitude;
            longitude += latLng.longitude;
        }

        return new LatLng(latitude / count, longitude / count);
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private List<Plot_maps> getPlots(String selectedPlotCode, List<String> plotCodes) {
        List<Plot_maps> plots = new ArrayList<>();
        LinkedHashMap<String, List<LatLng>> latlongDataMap;

        try {
            String query = Queries.getInstance().getLatlongs(plotCodes);
            latlongDataMap = dataAccessHandler.getGenericDataa(query);

            Log.d("getPlots", "Raw data map: " + latlongDataMap);

            if (latlongDataMap != null && !latlongDataMap.isEmpty()) {
                for (Map.Entry<String, List<LatLng>> entry : latlongDataMap.entrySet()) {
                    String plotCode = entry.getKey();
                    List<LatLng> coordinates = entry.getValue();
                    Plot_maps plot = new Plot_maps();

                    for (LatLng coordinate : coordinates) {
                        plot.addCoordinate(coordinate);
                    }

                    if (plotCode.equals(selectedPlotCode)) {
                        plot.setHighlighted(true);
                    } else {
                        plot.setHighlighted(false);
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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
     //   ProgressBar.showProgressBar(this, "Please wait...");
        // Show the progress bar
        progressBar.setVisibility(View.VISIBLE);

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                // Hide the progress bar once the map is fully loaded
                ProgressBar.hideProgressBar();
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                // Handle map click
            }
        });

        // Continue with your other map-related operations...
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
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ViewmapsActivity.this, HomeScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

}
