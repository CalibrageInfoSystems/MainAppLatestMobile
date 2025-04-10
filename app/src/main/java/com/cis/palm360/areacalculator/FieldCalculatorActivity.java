package com.cis.palm360.areacalculator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cis.palm360.R;
import com.cis.palm360.common.CommonUtils;
import com.cis.palm360.uihelper.ProgressBar;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import letsrock.areaviewlib.AreaView;
import letsrock.areaviewlib.GPSCoordinate;


////To calculate the Geo Boundaries using Location
//public class FieldCalculatorActivity extends AppCompatActivity {
//
//    private static final String LOG_TAG = FieldCalculatorActivity.class.getName();
//    private static final int PERMISSION_REQUEST_CODE = 1;
//    public static List<GPSCoordinate> firstFourCoordinates = new ArrayList<>();
//    public static ArrayList<GPSCoordinate> recordedBoundries = new ArrayList<>();
//    public static ArrayList<GPSCoordinate> totalBoundries = new ArrayList<>();
//    private AreaView measureView;
//    private Button startStopButton, saveBtn, resetBtn;
//    private Context c;
//    private LocationManager locationManager;
//    private LinkedHashMap<String, String> latLongMap = new LinkedHashMap<>();
//    private Button recordBtn;
//    private TextView count;
//    private RecyclerView recordsList;
//
//    private BroadcastReceiver locationReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent != null && intent.getAction() != null && intent.getExtras() != null) {
//                latLongMap.put(String.valueOf(intent.getExtras().getDouble("latitude")), String.valueOf(intent.getExtras().getDouble("longitude")));
//
//                if (null != firstFourCoordinates && firstFourCoordinates.size() <= 4) {
//                    firstFourCoordinates.add(new GPSCoordinate(intent.getExtras().getDouble("latitude"), intent.getExtras().getDouble("longitude"), 0));
//                }
//
//
//                totalBoundries.add(new GPSCoordinate(intent.getExtras().getDouble("latitude"), intent.getExtras().getDouble("longitude"), 0));
//            }
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // Add static test boundaries
//        addStaticTestBoundaries();
//        c = this;
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !isLocationPermissionGranted()) {
//            Log.v(LOG_TAG, "Location Permissions Not Granted");
//            requestLocationPermissions();
//        } else {
//            initViews();
//        }
//
//        LocalBroadcastManager.getInstance(this).registerReceiver(locationReceiver, new IntentFilter("location_receiver"));
//    }
//
////        try {
////            // Access the 'textPaint' field from the AreaView class
////            Field textPaintField = measureView.getClass().getDeclaredField("textPaint");
////            textPaintField.setAccessible(true);  // Allow access to the private field
////
////            // Get the Paint object
////            Paint textPaint = (Paint) textPaintField.get(measureView);
////
////            // Set the color to white
////            textPaint.setColor(0xFFFFFFFF);  // White color
////            textPaint.setTextSize(30.0F);
////
////            // Optionally print the updated color
////            System.out.println(String.format("Updated textPaint Color: #%08X", textPaint.getColor()));
////
////        } catch (NoSuchFieldException | IllegalAccessException e) {
////            e.printStackTrace();
////        }
////
////        LocalBroadcastManager.getInstance(this).registerReceiver(locationReceiver, new IntentFilter("location_receiver"));
//
//
//
//    private void addStaticTestBoundaries() {
//        recordedBoundries.add(new GPSCoordinate(17.454775, 78.3870316666667, 5.0));
//        recordedBoundries.add(new GPSCoordinate(17.4552233333333, 78.38699, 600.0));
//        recordedBoundries.add(new GPSCoordinate(17.45556, 78.3868933333333, 500.0));
//        recordedBoundries.add(new GPSCoordinate(17.4558783333333, 78.3869233333333, 50.0));
//        recordedBoundries.add(new GPSCoordinate(17.4563733333333, 78.38693, 75.0));
//        recordedBoundries.add(new GPSCoordinate(17.4563916666667, 78.3872916666667, 80.0));
//        recordedBoundries.add(new GPSCoordinate(17.4563966666667, 78.38776, 100.0));
//        recordedBoundries.add(new GPSCoordinate(17.456415, 78.3878933333333, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.456375, 78.3879916666667, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.4555066666667, 78.3880666666667, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.4550416666667, 78.388095, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.4542933333333, 78.3880416666667, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.4542033333333, 78.3879933333333, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.4542116666667, 78.3874633333333, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.45428, 78.386935, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.4540966666667, 78.38669, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.454775, 78.3870316666667, 0.0)); // Duplicate to close the boundary
//
//
//
//        recordedBoundries.add(new GPSCoordinate(17.453675, 78.3863483333333, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.454185, 78.3864916666667, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.4545016666667, 78.3862733333333, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.45425, 78.3861816666667, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.4535366666667, 78.3860383333333, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.4529533333333, 78.3859733333333, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.4526616666667, 78.3859733333333, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.4525783333333, 78.3863516666667, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.4525266666667, 78.38669, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.4524766666667, 78.3872066666667, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.4524283333333, 78.3873566666667, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.4523966666667, 78.3879216666667, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.45232, 78.3883333333333, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.4522766666667, 78.3886366666667, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.4525316666667, 78.3887183333333, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.4533466666667, 78.3888866666667, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.45349, 78.3889033333333, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.453875, 78.3887166666667, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.453925, 78.3882416666667, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.4540683333333, 78.3879466666667, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.4540916666667, 78.3877716666667, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.45417, 78.3875733333333, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.454225, 78.3872133333333, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.4542233333333, 78.3869133333333, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.4542433333333, 78.3867816666667, 0.0));
//        recordedBoundries.add(new GPSCoordinate(17.454345, 78.3866733333333, 0.0));
//
//    }
//
//    @SuppressLint("MissingInflatedId")
//    public void initViews() {
//
//        setContentView(R.layout.activity_field_caluculator);
//        //recordedBoundries.clear();//Todo  Remove this
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        recordBtn = (Button) findViewById(R.id.recordBtn);
//        saveBtn = (Button) findViewById(R.id.saveBtn);
//        resetBtn = (Button) findViewById(R.id.reset);
//        recordsList = (RecyclerView) findViewById(R.id.gpsRecords);
//        count = findViewById(R.id.count);
//        recordsList.setLayoutManager(new LinearLayoutManager(FieldCalculatorActivity.this, LinearLayoutManager.VERTICAL, false));
//        recordsList.setAdapter(new RecordedCoordinatesAdapter(FieldCalculatorActivity.this, recordedBoundries));
//        count.setText("Count: " + recordedBoundries.size());
//
//
//
//        recordBtn.setOnClickListener(view -> {
//            if (measureView.isRunning()) {
//                GPSCoordinate pointsToRecord = null;
//                if (recordedBoundries.size() > 0) {
//                    double distance = CommonUtils.distance(
//                            recordedBoundries.get(recordedBoundries.size() - 1).latitude,
//                            recordedBoundries.get(recordedBoundries.size() - 1).longitude,
//                            AreaView.latitude, AreaView.longitude, 'M'
//                    );
//                    Log.d("distancefrompttopt", distance + "");
//                    pointsToRecord = new GPSCoordinate(AreaView.latitude, AreaView.longitude, distance);
//                    recordedBoundries.add(pointsToRecord);
//                    Log.d("RecordedPoint", pointsToRecord.latitude +","+ pointsToRecord.longitude + "");
//                } else {
//                    pointsToRecord = new GPSCoordinate(AreaView.latitude, AreaView.longitude, 0.0);
//                    recordedBoundries.add(pointsToRecord);
//                    Log.d("InitialRecordedPoint", pointsToRecord.latitude +","+ pointsToRecord.longitude + "");
//                }
//
//                // Update the RecyclerView adapter only if pointsToRecord is not null
//                if (pointsToRecord != null) {
//                    recordsList.setAdapter(new RecordedCoordinatesAdapter(FieldCalculatorActivity.this, recordedBoundries));
//                }
//
//                Log.d("RecordedPointsSize",recordedBoundries.size() + "");
//                count.setText("Count: " + recordedBoundries.size());
//            }
//        });
//
//        resetBtn.setOnClickListener(view -> {
//            measureView.reset();
//            measureView.invalidate();
//            recordedBoundries.clear();
//            recordsList.setAdapter(new RecordedCoordinatesAdapter(FieldCalculatorActivity.this, recordedBoundries));
//
//        });
//
//        saveBtn.setOnClickListener(view -> {
//            Log.d("recorderPointssizee", recordedBoundries.size() + "");
//
//            //  double measuredArea = Math.round(100 * measureView.getArea()) / (double) 100; // TODO ROJA
//            double measuredArea = calculateArea(recordedBoundries); // Implement this method
//            //      measuredArea.setText("Measured Area: " + staticMeasuredArea + " Ha")
//            if (measureView.isRunning()) {
//                Toast.makeText(FieldCalculatorActivity.this, "Please stop area measuring and save", Toast.LENGTH_SHORT).show();
//                return;
//            }
////            if (!measureView.isReadyToStart()) {
////                Toast.makeText(FieldCalculatorActivity.this, "Gps signal not received", Toast.LENGTH_SHORT).show();
////                return;
////            }
//            if (measuredArea > 0) {
//                Log.d("recorderPointssize", recordedBoundries.size() + "");
//                if (recordedBoundries.size() > 3){
//                    displayAreaAreaDialog();
//                }else{
//                    Toast.makeText(FieldCalculatorActivity.this, "Minimum of 4 Points has to be recorded to Calculate the GPS Area", Toast.LENGTH_SHORT).show();
//                }
//
//            } else {
//                Toast.makeText(FieldCalculatorActivity.this, "Area is not measured", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//        if (!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
//            displayGpsDialog();
//        }
//
//        measureView = (AreaView) findViewById(R.id.measureView);
//        measureView.setLengthUnits(AreaView.LENGTH_UNITS_KILOMETER);
//        measureView.setAreaUnits(AreaView.AREA_UNITS_HECTARE);
//        startStopButton = (Button) findViewById(R.id.startBtn);
//        startStopButton.setOnClickListener(view -> {
//
//            if (measureView.isReadyToStart()) {
//                measureView.start();
//                startStopButton.setText(c.getString(R.string.stop));
//                startStopButton.postInvalidate();
//            } else if (measureView.isRunning()) {
//                measureView.stop();
//                startStopButton.setText(c.getString(R.string.start));
//                startStopButton.postInvalidate();
//            } else {
//                Toast.makeText(FieldCalculatorActivity.this, "Waiting for gps signal", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
//
//    private double calculateArea(List<GPSCoordinate> boundaryPoints) {
//        int n = boundaryPoints.size();
//        if (n < 3) return 0.0; // Not enough points for a polygon
//
//        double area = 0.0;
//        for (int i = 0; i < n - 1; i++) {
//            area += boundaryPoints.get(i).latitude * boundaryPoints.get(i + 1).longitude;
//            area -= boundaryPoints.get(i + 1).latitude * boundaryPoints.get(i).longitude;
//        }
//
//        // Include the last point connecting to the first
//        area += boundaryPoints.get(n - 1).latitude * boundaryPoints.get(0).longitude;
//        area -= boundaryPoints.get(0).latitude * boundaryPoints.get(n - 1).longitude;
//
//        area = Math.abs(area) / 2.0;
//        return area; // Area in square degrees (convert as needed)
//    }
//
//
//    public boolean isLocationPermissionGranted() {
//        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
//        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
//        return hasFineLocationPermission == PackageManager.PERMISSION_GRANTED
//                && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED;
//    }
//
//    public void requestLocationPermissions() {
//        if (!isLocationPermissionGranted()) {
//            String[] perms = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
//            ActivityCompat.requestPermissions(this, perms, PERMISSION_REQUEST_CODE);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case PERMISSION_REQUEST_CODE:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    initViews();
//
//                    if (!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
//                        displayGpsDialog();
//                    }
//
//                } else {
//
//                }
//                break;
//        }
//    }
//
//    private void displayGpsDialog() {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("GPS is turned off ,Please Enable GPS").setCancelable(false)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        startActivity(settingsIntent);
//                    }
//                });
//        builder.show();
//
//    }
//
//
//
//
//    private void displayAreaAreaDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        double measuredArea = calculateArea(recordedBoundries);
//        //    double measuredArea = Math.round(100 * measureView.getArea()) / (double) 100; //TODO ROJA
////        double diffPercentage = CommonUtils.getPercentage(measuredArea, ConversionLandDetailsFragment.plotEnteredArea);
//        double diffPercentage = 0;
//        double roundedValue = 0.0;
//
//        try {
//            DecimalFormat f = new DecimalFormat("##.000000");
//            String formattedValue = f.format(diffPercentage);
//            if (!TextUtils.isEmpty(formattedValue)) {
//                roundedValue = Double.parseDouble(formattedValue);
//            }
//        } catch (Exception e) {
//            roundedValue = 0;
//        }
//
//
//        String message = "Total field area is : " + measuredArea + " " + measureView.getAreaUnit();
//
//        if (diffPercentage >= 60 && roundedValue != Double.POSITIVE_INFINITY && diffPercentage != Double.NEGATIVE_INFINITY) {
//            message = message + "\n Variation between Plot area and Gps area is " + roundedValue + " %";
//        }
//        builder.setMessage(message).setCancelable(false);
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                dialog.dismiss();
//                saveLatLongData();
//            }
//        });
//        builder.show();
//
//    }
//
//    public void saveLatLongData() {
//        ProgressBar.showProgressBar(FieldCalculatorActivity.this, "Saving Gps data");
//        try {
//
//            if (recordedBoundries.isEmpty()) {
//                recordedBoundries.addAll(totalBoundries);
//            }
//            Log.d("recorderPointssizee346", recordedBoundries.size() + "");
//
//            //  double measuredArea = Math.round(100 * measureView.getArea()) / (double) 100; // TODO ROJA
//            double measuredArea =  2.3; // Implement this method
//            //    double measuredArea = Math.round(100 * measureView.getArea()) / (double) 100;
//            Intent intent = new Intent();
//            intent.putExtra("area", measuredArea);
//            setResult(RESULT_OK, intent);
//            finish();
//
//        } catch (Exception e) {
//            Log.v(LOG_TAG, "@@@@ Error while saving lat longs");
//        }
//
//    }
//
//
//
//
//    @Override
//    public void onBackPressed() {
//        totalBoundries.clear();
//        recordedBoundries.clear();
//        firstFourCoordinates.clear();
//        Intent intent = new Intent();
//        intent.putExtra("area", 0.0);
//        setResult(RESULT_OK, intent);
//        finish();
//    }
//
//    public class RecordedCoordinatesAdapter extends RecyclerView.Adapter<RecordedCoordinatesAdapter.MyHolder> {
//        private Context mContext;
//        private List<GPSCoordinate> gpsCoordinates;
//
//
//        public RecordedCoordinatesAdapter(Context mContext, List<GPSCoordinate> gpsCoordinates) {
//            this.mContext = mContext;
//            this.gpsCoordinates = gpsCoordinates;
//        }
//
//        @Override
//        public RecordedCoordinatesAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View bookingView = inflater.inflate(R.layout.records_list_item, null);
//            MyHolder myHolder = new MyHolder(bookingView);
//            return myHolder;
//        }
//
//
//        @Override
//        public void onBindViewHolder(RecordedCoordinatesAdapter.MyHolder holder, int position) {
//
//            holder.latitude.setText("" + gpsCoordinates.get(position).latitude);
//            holder.longitude.setText("" + gpsCoordinates.get(position).longitude);
//            if (gpsCoordinates != null && gpsCoordinates.size() > 1) {
//                holder.distance.setText("" + gpsCoordinates.get(position).altitude + " Meters");
//
//            } else {
//                holder.distance.setText("0 " + "Meters");
//            }
//        }
//
//        @Override
//        public int getItemCount() {
//            return gpsCoordinates.size();
//        }
//
//        public class MyHolder extends RecyclerView.ViewHolder {
//            TextView latitude, longitude, distance;
//            public MyHolder(View itemView) {
//                super(itemView);
//                latitude = (TextView) itemView.findViewById(R.id.latitude);
//                longitude = (TextView) itemView.findViewById(R.id.longitude);
//                distance = (TextView) itemView.findViewById(R.id.distance);
//            }
//        }
//    }
//
//}


//
//To calculate the Geo Boundaries using Location
public class FieldCalculatorActivity extends AppCompatActivity {

    private static final String LOG_TAG = FieldCalculatorActivity.class.getName();
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static List<GPSCoordinate> firstFourCoordinates = new ArrayList<>();
    public static ArrayList<GPSCoordinate> recordedBoundries = new ArrayList<>();
    public static ArrayList<GPSCoordinate> totalBoundries = new ArrayList<>();
    private AreaView measureView;
    private Button startStopButton, saveBtn, resetBtn;
    private Context c;
    private LocationManager locationManager;
    private LinkedHashMap<String, String> latLongMap = new LinkedHashMap<>();
    private Button recordBtn;
    private TextView count;
    private RecyclerView recordsList;

    private BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null && intent.getExtras() != null) {
                latLongMap.put(String.valueOf(intent.getExtras().getDouble("latitude")), String.valueOf(intent.getExtras().getDouble("longitude")));

                if (null != firstFourCoordinates && firstFourCoordinates.size() <= 4) {
                    firstFourCoordinates.add(new GPSCoordinate(intent.getExtras().getDouble("latitude"), intent.getExtras().getDouble("longitude"), 0));
                }


                totalBoundries.add(new GPSCoordinate(intent.getExtras().getDouble("latitude"), intent.getExtras().getDouble("longitude"), 0));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        c = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !isLocationPermissionGranted()) {
            Log.v(LOG_TAG, "Location Permissions Not Granted");
            requestLocationPermissions();
        } else {
            initViews();
        }


//        try {
//            // Access the 'textPaint' field from the AreaView class
//            Field textPaintField = measureView.getClass().getDeclaredField("textPaint");
//            textPaintField.setAccessible(true);  // Allow access to the private field
//
//            // Get the Paint object
//            Paint textPaint = (Paint) textPaintField.get(measureView);
//
//            // Access its properties
//            int color = textPaint.getColor();  // Get the color
//            float textSize = textPaint.getTextSize();  // Get the text size
//            boolean isAntiAlias = textPaint.isAntiAlias();  // Check if anti-aliasing is enabled
//
//            // Print the details
//            System.out.println("textPaint Color: " + color);
//            System.out.println("textPaint Text Size: " + textSize);
//            System.out.println("textPaint AntiAlias: " + isAntiAlias);
//
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
        try {
            // Access the 'textPaint' field from the AreaView class
            Field textPaintField = measureView.getClass().getDeclaredField("textPaint");
            textPaintField.setAccessible(true);  // Allow access to the private field

            // Get the Paint object
            Paint textPaint = (Paint) textPaintField.get(measureView);

            // Set the color to white
            textPaint.setColor(0xFFFFFFFF);  // White color
            textPaint.setTextSize(30.0F);

            // Optionally print the updated color
            System.out.println(String.format("Updated textPaint Color: #%08X", textPaint.getColor()));

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(locationReceiver, new IntentFilter("location_receiver"));

    }

    @SuppressLint("MissingInflatedId")
    public void initViews() {

        setContentView(R.layout.activity_field_caluculator);
        recordedBoundries.clear();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        recordBtn = (Button) findViewById(R.id.recordBtn);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        resetBtn = (Button) findViewById(R.id.reset);
        recordsList = (RecyclerView) findViewById(R.id.gpsRecords);
        count = findViewById(R.id.count);
        recordsList.setLayoutManager(new LinearLayoutManager(FieldCalculatorActivity.this, LinearLayoutManager.VERTICAL, false));

//        recordBtn.setOnClickListener(view -> {
//
//            if (measureView.isRunning()) {
//                GPSCoordinate pointsToRecord = null;
//                if (null != recordedBoundries && recordedBoundries.size() > 0) {
//                    double distance = CommonUtils.distance(recordedBoundries.get(recordedBoundries.size() - 1).latitude,
//                            recordedBoundries.get(recordedBoundries.size() - 1).longitude, AreaView.latitude, AreaView.longitude, 'M');
//                    Log.d("distancefrompttopt", distance + "");
//                    if (distance >= 200){
//                        pointsToRecord = new GPSCoordinate(AreaView.latitude, AreaView.longitude, distance);
//                    }else{
//                        Toast.makeText(FieldCalculatorActivity.this, "Coordinates will be added only after 200m of distance from the previous point", Toast.LENGTH_SHORT).show();
//                    }
//
//                } else {
//                    pointsToRecord = new GPSCoordinate(AreaView.latitude, AreaView.longitude, 0.0);
//                }
//
//                recordedBoundries.add(pointsToRecord);
//                recordsList.setAdapter(new RecordedCoordinatesAdapter(FieldCalculatorActivity.this, recordedBoundries));
//            }
//
//        });

        recordBtn.setOnClickListener(view -> {
            if (measureView.isRunning()) {
                GPSCoordinate pointsToRecord = null;
                if (recordedBoundries.size() > 0) {
                    double distance = CommonUtils.distance(
                            recordedBoundries.get(recordedBoundries.size() - 1).latitude,
                            recordedBoundries.get(recordedBoundries.size() - 1).longitude,
                            AreaView.latitude, AreaView.longitude, 'M'
                    );
                    Log.d("distancefrompttopt", distance + "");
                    pointsToRecord = new GPSCoordinate(AreaView.latitude, AreaView.longitude, distance);
                    recordedBoundries.add(pointsToRecord);
                    Log.d("RecordedPoint", pointsToRecord.latitude +","+ pointsToRecord.longitude + "");
                } else {
                    pointsToRecord = new GPSCoordinate(AreaView.latitude, AreaView.longitude, 0.0);
                    recordedBoundries.add(pointsToRecord);
                    Log.d("InitialRecordedPoint", pointsToRecord.latitude +","+ pointsToRecord.longitude + "");
                }

                // Update the RecyclerView adapter only if pointsToRecord is not null
                if (pointsToRecord != null) {
                    recordsList.setAdapter(new RecordedCoordinatesAdapter(FieldCalculatorActivity.this, recordedBoundries));
                }

                Log.d("RecordedPointsSize",recordedBoundries.size() + "");
                count.setText("Count: " + recordedBoundries.size());
            }
        });

        resetBtn.setOnClickListener(view -> {
            measureView.reset();
            measureView.invalidate();
            recordedBoundries.clear();
            recordsList.setAdapter(new RecordedCoordinatesAdapter(FieldCalculatorActivity.this, recordedBoundries));

        });

        saveBtn.setOnClickListener(view -> {
            Log.d("recorderPointssizee", recordedBoundries.size() + "");
            double measuredArea = Math.round(100 * measureView.getArea()) / (double) 100;
            if (measureView.isRunning()) {
                Toast.makeText(FieldCalculatorActivity.this, "Please stop area measuring and save", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!measureView.isReadyToStart()) {
                Toast.makeText(FieldCalculatorActivity.this, "Gps signal not received", Toast.LENGTH_SHORT).show();
                return;
            }
            if (measuredArea > 0) {
                Log.d("recorderPointssize", recordedBoundries.size() + "");
                if (recordedBoundries.size() > 3){
                    displayAreaAreaDialog();
                }else{
                    Toast.makeText(FieldCalculatorActivity.this, "Minimum of 4 Points has to be recorded to Calculate the GPS Area", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(FieldCalculatorActivity.this, "Area is not measured", Toast.LENGTH_SHORT).show();
            }
        });
//        saveBtn.setOnClickListener(view -> {
//            Log.d("recorderPointssizee", recordedBoundries.size() + "");
//            double measuredArea = Math.round(100 * measureView.getArea()) / (double) 100;
//            if (measureView.isRunning()) {
//                Toast.makeText(FieldCalculatorActivity.this, "Please stop area measuring and save", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (!measureView.isReadyToStart()) {
//                Toast.makeText(FieldCalculatorActivity.this, "GPS signal not received", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            if (measuredArea > 0) {
//                Log.d("recorderPointssize", recordedBoundries.size() + "");
//                if (recordedBoundries.size() >= 4) {
//                    if (isValidPolygon(recordedBoundries)) {
//                        displayAreaAreaDialog();
//                    } else {
//                        Toast.makeText(FieldCalculatorActivity.this, "The polygon is not valid. Please check the points.", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(FieldCalculatorActivity.this, "At least 4 points must be recorded to calculate the GPS area.", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(FieldCalculatorActivity.this, "Area is not measured", Toast.LENGTH_SHORT).show();
//            }
//        });


        if (!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            displayGpsDialog();
        }

        measureView = (AreaView) findViewById(R.id.measureView);
        measureView.setLengthUnits(AreaView.LENGTH_UNITS_KILOMETER);
        measureView.setAreaUnits(AreaView.AREA_UNITS_HECTARE);
        startStopButton = (Button) findViewById(R.id.startBtn);
        startStopButton.setOnClickListener(view -> {

            if (measureView.isReadyToStart()) {
                measureView.start();
                startStopButton.setText(c.getString(R.string.stop));
                startStopButton.postInvalidate();
            } else if (measureView.isRunning()) {
                measureView.stop();
                startStopButton.setText(c.getString(R.string.start));
                startStopButton.postInvalidate();
            } else {
                Toast.makeText(FieldCalculatorActivity.this, "Waiting for gps signal", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public boolean isLocationPermissionGranted() {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        return hasFineLocationPermission == PackageManager.PERMISSION_GRANTED
                && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED;
    }

    public void requestLocationPermissions() {
        if (!isLocationPermissionGranted()) {
            String[] perms = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, perms, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    initViews();

                    if (!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
                        displayGpsDialog();
                    }

                } else {

                }
                break;
        }
    }

    private void displayGpsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("GPS is turned off ,Please Enable GPS").setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(settingsIntent);
                    }
                });
        builder.show();

    }

    private void displayAreaDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        double measuredArea = Math.round(100 * measureView.getArea()) / (double) 100;
        builder.setMessage("Total field area is : " + measuredArea + " " + measureView.getAreaUnit()).setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                saveLatLongData();
            }
        });
        builder.show();

    }


    private void displayAreaAreaDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        double measuredArea = Math.round(100 * measureView.getArea()) / (double) 100;
//        double diffPercentage = CommonUtils.getPercentage(measuredArea, ConversionLandDetailsFragment.plotEnteredArea);
        double diffPercentage = 0;
        double roundedValue = 0.0;

        try {
            DecimalFormat f = new DecimalFormat("##.000000");
            String formattedValue = f.format(diffPercentage);
            if (!TextUtils.isEmpty(formattedValue)) {
                roundedValue = Double.parseDouble(formattedValue);
            }
        } catch (Exception e) {
            roundedValue = 0;
        }


        String message = "Total field area is : " + measuredArea + " " + measureView.getAreaUnit();

        if (diffPercentage >= 60 && roundedValue != Double.POSITIVE_INFINITY && diffPercentage != Double.NEGATIVE_INFINITY) {
            message = message + "\n Variation between Plot area and Gps area is " + roundedValue + " %";
        }
        builder.setMessage(message).setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                saveLatLongData();
            }
        });
        builder.show();

    }

    public void saveLatLongData() {
        ProgressBar.showProgressBar(FieldCalculatorActivity.this, "Saving Gps data");
        try {

            if (recordedBoundries.isEmpty()) {
                recordedBoundries.addAll(totalBoundries);
            }

            double measuredArea = Math.round(100 * measureView.getArea()) / (double) 100;
            Intent intent = new Intent();
            intent.putExtra("area", measuredArea);
            setResult(RESULT_OK, intent);
            finish();

        } catch (Exception e) {
            Log.v(LOG_TAG, "@@@@ Error while saving lat longs");
        }

    }

    private boolean isValidPolygon(List<GPSCoordinate> coordinates) {
        if (coordinates.size() < 4) return false;

        // Ensure the polygon is closed
        if (!coordinates.get(0).equals(coordinates.get(coordinates.size() - 1))) {
            coordinates.add(coordinates.get(0));
        }

        int n = coordinates.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n - 1; j++) {
                if (i != j && doIntersect(coordinates.get(i), coordinates.get(i + 1),
                        coordinates.get(j), coordinates.get(j + 1))) {
                    return false;
                }
            }
        }
        return true;
    }

    // Method to check if two line segments intersect
    private boolean doIntersect(GPSCoordinate p1, GPSCoordinate q1, GPSCoordinate p2, GPSCoordinate q2) {
        // Find the four orientations needed for the general and special cases
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        // General case
        if (o1 != o2 && o3 != o4) {
            return true;
        }

        // Special cases
        // p1, q1 and p2 are collinear and p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1, p2, q1)) return true;

        // p1, q1 and q2 are collinear and q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1, q2, q1)) return true;

        // p2, q2 and p1 are collinear and p1 lies on segment p2q2
        if (o3 == 0 && onSegment(p2, p1, q2)) return true;

        // p2, q2 and q1 are collinear and q1 lies on segment p2q2
        if (o4 == 0 && onSegment(p2, q1, q2)) return true;

        return false;
    }

    // Utility function to find the orientation of the ordered triplet (p, q, r)
    private int orientation(GPSCoordinate p, GPSCoordinate q, GPSCoordinate r) {
        double val = (q.longitude - p.longitude) * (r.latitude - q.latitude) -
                (q.latitude - p.latitude) * (r.longitude - q.longitude);

        if (val == 0) return 0;  // Collinear
        return (val > 0) ? 1 : 2; // Clockwise or counterclockwise
    }

    // Function to check if a point q lies on line segment 'pr'
    private boolean onSegment(GPSCoordinate p, GPSCoordinate q, GPSCoordinate r) {
        if (q.longitude <= Math.max(p.longitude, r.longitude) && q.longitude >= Math.min(p.longitude, r.longitude) &&
                q.latitude <= Math.max(p.latitude, r.latitude) && q.latitude >= Math.min(p.latitude, r.latitude))
            return true;
        return false;
    }

    @Override
    public void onBackPressed() {
        totalBoundries.clear();
        recordedBoundries.clear();
        firstFourCoordinates.clear();
        Intent intent = new Intent();
        intent.putExtra("area", 0.0);
        setResult(RESULT_OK, intent);
        finish();
    }

    public class RecordedCoordinatesAdapter extends RecyclerView.Adapter<RecordedCoordinatesAdapter.MyHolder> {
        private Context mContext;
        private List<GPSCoordinate> gpsCoordinates;


        public RecordedCoordinatesAdapter(Context mContext, List<GPSCoordinate> gpsCoordinates) {
            this.mContext = mContext;
            this.gpsCoordinates = gpsCoordinates;
        }

        @Override
        public RecordedCoordinatesAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View bookingView = inflater.inflate(R.layout.records_list_item, null);
            MyHolder myHolder = new MyHolder(bookingView);
            return myHolder;
        }


        @Override
        public void onBindViewHolder(RecordedCoordinatesAdapter.MyHolder holder, int position) {

            holder.latitude.setText("" + gpsCoordinates.get(position).latitude);
            holder.longitude.setText("" + gpsCoordinates.get(position).longitude);
            if (gpsCoordinates != null && gpsCoordinates.size() > 1) {
                holder.distance.setText("" + gpsCoordinates.get(position).altitude + " Meters");

            } else {
                holder.distance.setText("0 " + "Meters");
            }
        }

        @Override
        public int getItemCount() {
            return gpsCoordinates.size();
        }

        public class MyHolder extends RecyclerView.ViewHolder {
            TextView latitude, longitude, distance;
            public MyHolder(View itemView) {
                super(itemView);
                latitude = (TextView) itemView.findViewById(R.id.latitude);
                longitude = (TextView) itemView.findViewById(R.id.longitude);
                distance = (TextView) itemView.findViewById(R.id.distance);
            }
        }
    }

}
