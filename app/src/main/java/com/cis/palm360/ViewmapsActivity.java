package com.cis.palm360;


import static java.util.Calendar.YEAR;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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
import com.cis.palm360.common.CommonConstants;
import com.cis.palm360.database.DataAccessHandler;
import com.cis.palm360.database.Queries;

import com.cis.palm360.dbmodels.PlotInfo;
import com.cis.palm360.uihelper.ProgressBar;
//import com.karumi.dexter.Dexter;
//import com.karumi.dexter.PermissionToken;
//import com.karumi.dexter.listener.PermissionDeniedResponse;
//import com.karumi.dexter.listener.PermissionGrantedResponse;
//import com.karumi.dexter.listener.PermissionRequest;
//import com.karumi.dexter.listener.single.PermissionListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
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
    private PlotInfo plotinfo,plotinfo_color;
    Date parsedDate;
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
//        plotinfo = (PlotInfo) dataAccessHandler.getplotinfoData(Queries.getInstance().getplotinfo(selectedPlotCode), 0);
//        Log.e("======selectedplotinfo", plotinfo.getFarmerName());

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
                        @RequiresApi(api = Build.VERSION_CODES.O)
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
                                    .icon(bitmapDescriptorFromVector(ViewmapsActivity.this, R.drawable.humen));
                            googleMap.addMarker(markerOptions);
                            Marker nonClickableMarker1 = googleMap.addMarker(markerOptions);
                            nonClickableMarker1.setTag("non_clickable_marker");

                            List<String> plotCodes = dataAccessHandler.getSingleListData(Queries.getInstance().getplotslist(CommonConstants.SelectedvillageIds));

                            List<Plot_maps> plots = getPlots(selectedPlotCode, plotCodes);
                            for (Plot_maps plot : plots) {
                                plotinfo_color = (PlotInfo) dataAccessHandler.getplotinfoData(
                                        Queries.getInstance().getplotinfo(plot.getPlotCode()), 0);

                                Log.e("======plotCodes161", plotinfo_color.getPlotCode() + " " + plotinfo_color.getDateOfPlanting());

                                // Parse the DateOfPlanting from the plotinfo_color object
                                String dateOfPlanting = plotinfo_color.getDateOfPlanting(); // Assuming this returns a date string
                                if (dateOfPlanting != null && !dateOfPlanting.isEmpty()) {

                                        // Replace 'T' with a space for consistent date parsing
                                        String dop = dateOfPlanting.replace("T", " ");

                                        // Convert Date to LocalDate for ChronoUnit calculation
                                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        try {
                                             parsedDate = inputFormat.parse(dop);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        Date currentDate = new Date();
                                        Calendar a = getCalendar(parsedDate);
                                        Calendar b = getCalendar(currentDate);
                                        long yearsDifference = b.get(YEAR) - a.get(YEAR);
                                        Log.e("diff====", yearsDifference + "");

                                        // Calculate the difference in years
                                      //  long yearsDifference = ChronoUnit.YEARS.between(plantingDate, currentDate);

                                        // Determine the polygon border color
                                        int borderColor;
                                    if (yearsDifference >= 3) {
                                        borderColor = getResources().getColor(R.color.darkgreen); // Dark green
                                    } else {
                                        borderColor = getResources().getColor(R.color.lightgreen); // Semi-transparent green
                                    }

                                        // Pass the border color to the drawPlot method
                                        drawPlot(googleMap, plot, borderColor);


                                } else {
                                    Log.e("DateError", "DateOfPlanting is null or empty for PlotCode: " + plot.getPlotCode());
                                }
                            }

//                            for (Plot_maps plot : plots) {
//                                plotinfo_color = (PlotInfo) dataAccessHandler.getplotinfoData(Queries.getInstance().getplotinfo(plot.getPlotCode()), 0);
//                                Log.e("======plotCodes161", plotinfo_color.getPlotCode() + plotinfo_color.getDateOfPlanting() + "");
//                                drawPlot(googleMap, plot);
//                            }

                            for (Plot_maps plot : plots) {
                                if (plot.isHighlighted()) {
                                    LatLng centroid = calculateCentroid(plot.getCoordinates());
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centroid, 18.0f));
                                    break;
                                }
                            }

                            // Hide the progress bar after drawing the plots
                            ProgressBar.hideProgressBar();

                            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {

                                    // Log the marker's tag
                                    Log.d("MarkerClick", "Marker Tag: " + marker.getTag());

                                    if ("non_clickable_marker".equals(marker.getTag())) {
                                        return true; // Consumes the click event
                                    } else {
                                        // Retrieve the plotCode from the marker's tag
                                        String plotCode = (String) marker.getTag();

                                        if (plotCode != null) {
                                            plotinfo = (PlotInfo) dataAccessHandler.getplotinfoData(Queries.getInstance().getplotinfo(plotCode), 0);

                                            if (plotinfo != null) {
                                                Log.d("MarkerClick", "Plot Code: " + plotinfo.getPlotCode());
                                                marker.showInfoWindow();
                                                return false; // Show info window
                                            } else {
                                                Log.e("MarkerClick", "No plot information found for plotCode: " + plotCode);
                                            }
                                        } else {
                                            Log.e("MarkerClick", "Plot code is null");
                                        }

                                        return true; // Consumes the click event in case of an error
                                    }
                                }
                            });

//                            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                                @Override
//                                public boolean onMarkerClick(Marker marker) {
//
//                                    Log.d("MarkerClick", "Marker Tag: " + marker.getTag());
//
//                                    if ("non_clickable_marker".equals(marker.getTag())) {
//                                        return true;
//                                    } else {
//                                        // Retrieve the plotCode from the marker's tag
//                                        String plotCode = (String) marker.getTag();
//
//                                        // Print the plotCode to the log
//                                        Log.d("MarkerClick", "Plot Code: " + plotCode);
//                                        plotinfo = (PlotInfo) dataAccessHandler.getplotinfoData(Queries.getInstance().getplotinfo(plotCode), 0);
//                                        Log.e("plotinfo:198 ", plotinfo.getPlotCode());
//                                        // Show the info window
//                                        marker.showInfoWindow();
//
//                                        return false; // Returning false ensures the default behavior of showing the info window
//                                    }
//                                }
//                            });

                        }
                    });
                } else {
                    // Hide the progress bar if location is null
                    ProgressBar.hideProgressBar();
                }
            }
        });

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    smf.getMapAsync(new OnMapReadyCallback() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            mMap = googleMap;
                            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                            mMap.getUiSettings().setZoomControlsEnabled(true);
                            mMap.getUiSettings().setZoomGesturesEnabled(true);

                            // Check location permissions and enable MyLocation
                            if (ActivityCompat.checkSelfPermission(ViewmapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                    ActivityCompat.checkSelfPermission(ViewmapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            mMap.setMyLocationEnabled(true);

                            // Set the camera to the current location with initial zoom level
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18.0f));

                            // Adjust the zoom preferences
                            mMap.setMinZoomPreference(10.0f);  // Allows zooming out
                            mMap.setMaxZoomPreference(20.0f);  // Adjust max zoom level as needed

                            // Add a marker at the current location
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(currentLocation)
                                    .title("You are here")
                                    .icon(bitmapDescriptorFromVector(ViewmapsActivity.this, R.drawable.humen));
                            googleMap.addMarker(markerOptions);
                            Marker nonClickableMarker2 = googleMap.addMarker(markerOptions);
                            nonClickableMarker2.setTag("non_clickable_marker");

                            // Draw the plots
                            List<String> plotCodes = dataAccessHandler.getSingleListData(Queries.getInstance().getplotslist(CommonConstants.SelectedvillageIds));
                            Log.e("======plotCodes", plotCodes.size() + "");
                            List<Plot_maps> plots = getPlots(selectedPlotCode, plotCodes);
                            for (Plot_maps plot : plots) {
                                plotinfo_color = (PlotInfo) dataAccessHandler.getplotinfoData(Queries.getInstance().getplotinfo(plot.getPlotCode()), 0);
                                Log.e("======plotCodes161", plotinfo_color.getPlotCode() + " " + plotinfo_color.getDateOfPlanting());

                                // Parse the DateOfPlanting from the plotinfo_color object
                                String dateOfPlanting = plotinfo_color.getDateOfPlanting(); // Assuming this returns a date string
                                if (dateOfPlanting != null && !dateOfPlanting.isEmpty()) {

                                    // Replace 'T' with a space for consistent date parsing
                                    String dop = dateOfPlanting.replace("T", " ");

                                    // Convert Date to LocalDate for ChronoUnit calculation
                                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    try {
                                        parsedDate = inputFormat.parse(dop);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    Date currentDate = new Date();
                                    Calendar a = getCalendar(parsedDate);
                                    Calendar b = getCalendar(currentDate);
                                    long yearsDifference = b.get(YEAR) - a.get(YEAR);
                                    Log.e("diff====", yearsDifference + "");

                                    // Calculate the difference in years
                                    //  long yearsDifference = ChronoUnit.YEARS.between(plantingDate, currentDate);

                                    // Determine the polygon border color
                                    int borderColor;
                                    if (yearsDifference >= 3) {
                                        borderColor = getResources().getColor(R.color.darkgreen); // Dark green
                                    } else {
                                        borderColor = getResources().getColor(R.color.lightgreen); // Semi-transparent green
                                    }

                                    // Pass the border color to the drawPlot method
                                    drawPlot(googleMap, plot, borderColor);


                                } else {
                                    Log.e("DateError", "DateOfPlanting is null or empty for PlotCode: " + plot.getPlotCode());
                                }
                            }
//                            for (Plot_maps plot : plots) {
//                            for (Plot_maps plot : plots) {
//                                drawPlot(googleMap, plot, borderColor);
//                            }

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

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }


    private void drawPlot(GoogleMap googleMap, Plot_maps plot, int borderColor) {
        Log.e("======plotCode182", plot.getPlotCode() + "color  " + borderColor);
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
            polylineColor = borderColor;
            polygonFillColor = Color.argb(100, 0, 255, 0);
        } else {
            polylineColor = borderColor;
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


        BitmapDescriptor markerIcon = BitmapDescriptorFactory.defaultMarker();

        if (plot.isHighlighted()) {
            markerIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN); // Use HUE_GREEN for highlighted plots
        } else {
            markerIcon = BitmapDescriptorFactory.fromBitmap(createColoredMarker(borderColor)); // Use the same border color
        }

         marker = googleMap.addMarker(new MarkerOptions()
                .position(centroid)
                .title(plot.getPlotCode())
                .icon(BitmapDescriptorFactory.fromBitmap(createColoredMarker(borderColor)))
                .anchor(0.5f, 1.0f) // Default marker anchor
                .alpha(plot.isHighlighted() ? 1.0f : 0.5f));


//        if (plot.isHighlighted()) {
//            marker = googleMap.addMarker(new MarkerOptions()
//                    .position(centroid)
//                    .title(plot.getPlotCode())
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
//                    .infoWindowAnchor(0.5f, 0.6f));
//        } else {
//            marker = googleMap.addMarker(new MarkerOptions()
//                    .position(centroid)
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
//                    .anchor(0.5f, 0.5f)
//                    .alpha(0.5f));
//        }

        // Set the plotCode as the tag for the marker
        marker.setTag(plot.getPlotCode());

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

                if (plotinfo.getDateOfPlanting() != null && !plotinfo.getDateOfPlanting().equalsIgnoreCase("null")) {
                    dateofplantation.setVisibility(View.VISIBLE);
                    try {
                        // Replace 'T' with space in the date string
                        String dop = plotinfo.getDateOfPlanting().replace("T", " ");

                        // Parse the input date string (assuming it is in yyyy-MM-dd format)
                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        Date parsedDate = inputFormat.parse(dop);

                        // Format the parsed date to dd/MM/yyyy
                        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String formattedDate = outputFormat.format(parsedDate);

                        // Set the formatted date in the TextView
                        dateofplantation.setVisibility(View.VISIBLE);
                        dateofplantation.setText(Html.fromHtml("Date of Plantation: <b>" + formattedDate + "</b>"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        // In case of an error, bind the original date or display a default message
                        dateofplantation.setVisibility(View.VISIBLE);
                        dateofplantation.setText(Html.fromHtml("Date of Plantation: <b>" + plotinfo.getDateOfPlanting() + "</b>"));
                    }


//                    dateofplantation.setText(Html.fromHtml("Date of Plantation: <b>" + plotinfo.getDateOfPlanting() + "</b>"));
                } else {
                    dateofplantation.setVisibility(View.GONE);
                }

                return infoWindow;
            }
        });
    }

    private Bitmap createColoredMarker(int color) {
        // Inflate the default marker drawable
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_default_marker);
        assert drawable != null;
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Apply the color filter to the drawable
        drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<Plot_maps> getPlots(String selectedPlotCode, List<String> plotCodes) {
        List<Plot_maps> plots = new ArrayList<>();
        LinkedHashMap<String, List<LatLng>> latlongDataMap;

        try {
            String query = Queries.getInstance().getLatlongs(plotCodes);
            latlongDataMap = dataAccessHandler.getGenericDataa(query);
            Log.e("======plotCodes", plotCodes.size() + "");
            Log.d("getPlots", "Raw data map: " + latlongDataMap.size());

            if (latlongDataMap != null && !latlongDataMap.isEmpty()) {
                for (Map.Entry<String, List<LatLng>> entry : latlongDataMap.entrySet()) {
                    String plotCode = entry.getKey();
                    List<LatLng> coordinates = entry.getValue();
                    Plot_maps plot = new Plot_maps();

                    plot.setPlotCode(plotCode); // Correctly set the plotCode

                    for (LatLng coordinate : coordinates) {
                        plot.addCoordinate(coordinate);
                    }

                    if (plotCode.equals(selectedPlotCode)) {
                        plot.setHighlighted(true);
                    } else {
                        plot.setHighlighted(false);
                    }

                    plots.add(plot);
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
        Intent intent = new Intent(ViewmapsActivity.this, FiltermapsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

}
