package com.oilpalm3f.mainapp.areacalculator;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oilpalm3f.mainapp.R;
import com.oilpalm3f.mainapp.cloudhelper.ApplicationThread;
import com.oilpalm3f.mainapp.cloudhelper.Log;
import com.oilpalm3f.mainapp.common.CommonConstants;
import com.oilpalm3f.mainapp.common.CommonUtils;
import com.oilpalm3f.mainapp.common.OilPalmException;
import com.oilpalm3f.mainapp.conversion.ConversionMainFlowActivity;
import com.oilpalm3f.mainapp.database.DataAccessHandler;
import com.oilpalm3f.mainapp.database.DataSavingHelper;
import com.oilpalm3f.mainapp.database.Palm3FoilDatabase;
import com.oilpalm3f.mainapp.database.Queries;
import com.oilpalm3f.mainapp.datasync.helpers.DataManager;
import com.oilpalm3f.mainapp.dbmodels.GeoBoundaries;
import com.oilpalm3f.mainapp.dbmodels.Plot;
import com.oilpalm3f.mainapp.ui.OilPalmBaseActivity;
import com.oilpalm3f.mainapp.utils.UiUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import letsrock.areaviewlib.GPSCoordinate;

import static com.oilpalm3f.mainapp.areacalculator.FieldCalculatorActivity.firstFourCoordinates;
import static com.oilpalm3f.mainapp.areacalculator.FieldCalculatorActivity.recordedBoundries;
import static com.oilpalm3f.mainapp.areacalculator.FieldCalculatorActivity.totalBoundries;

//To preview the taken Geo Boundaries
public class PreViewAreaCalScreen extends OilPalmBaseActivity {

    public static String gpsArea;
    private Button saveBtn, gpsStartBtn;
    private RelativeLayout gpsPointsLayout;
    private ActionBar actionBar;
    private TextView lat1p1, lat2p2, lat3p3, lat4p4, gpsAreaText;
    private TextView long1p1, long2p2, long3p3, long4p4;
    Plot plot = null;
    private AlertDialog alertDialog;
    public String UpdatedDate, UpdatedByUserId, ServerUpdatedStatus, Is_Active;
    private Palm3FoilDatabase palm3FoilDatabase;
    LocationManager lm;
    int RESULT_OK = 222;

    @Override
    public void Initialize() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View parentView = inflater.inflate(R.layout.content_pre_view_area_cal_screen, null);
        gpsPointsLayout = (RelativeLayout) parentView.findViewById(R.id.gpsPointsLL);
        baseLayout.addView(gpsPointsLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Gps Points");

        saveBtn = (Button) parentView.findViewById(R.id.land_save_btn);
        gpsStartBtn = (Button) parentView.findViewById(R.id.start_gps_btn);
        palm3FoilDatabase = Palm3FoilDatabase.getPalm3FoilDatabase(this);
        gpsStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


                try {
                    if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        startActivityForResult(new Intent(PreViewAreaCalScreen.this, FieldCalculatorActivity.class), 100);


                    } else {
                        UiUtils.showCustomToastMessage("Please Trun On GPS......", PreViewAreaCalScreen.this, 1);

                    }
                } catch (Exception e) {
                    UiUtils.showCustomToastMessage("Please Trun On GPS......", PreViewAreaCalScreen.this, 1);
                    android.util.Log.e("FLD Preview", "Error while GPs" + android.util.Log.getStackTraceString(e));
                }

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String generatedplotCode = CommonConstants.PLOT_CODE;

//                if (CommonUtils.isNewPlotRegistration()){
//                    Log.d("FromnewPlotReg", "Yes");
//                }else{
//                    Log.d("FromnewPlotReg", "No");
//                }

                if (null != firstFourCoordinates && !firstFourCoordinates.isEmpty() && !TextUtils.isEmpty(gpsArea)) {

//                    if (generatedplotCode != null && CommonUtils.isNewRegistration()){
//                        Log.d("isNewRegistration", "I am here");
//                        DataManager.getInstance().addData(DataManager.PLOT_GEO_BOUNDARIES, getGeoBoundriesData());
//                        CommonConstants.isFromPlotDetails = true;
//                        Intent intent = new Intent();
//                        intent.putExtra("result_key", gpsArea);
//                        setResult(RESULT_OK, intent);
//                        firstFourCoordinates.clear();
//                        recordedBoundries.clear();
//                        finish();
//                    }
//
//                   else if (generatedplotCode != null && CommonUtils.isNewPlotRegistration()){
//                       Log.d("isNewPlotRegistration", "I am here");
//
//                        DataManager.getInstance().addData(DataManager.PLOT_GEO_BOUNDARIES, getGeoBoundriesData());
//                        CommonConstants.isFromPlotDetails = true;
//                        Intent intent = new Intent();
//                        intent.putExtra("result_key", gpsArea);
//                        setResult(RESULT_OK, intent);
//                        firstFourCoordinates.clear();
//                        recordedBoundries.clear();
//                        finish();
//                    }
                    if (CommonUtils.isNewPlotRegistration() || CommonUtils.isNewRegistration()){
                        Log.d("isNewRegistration", "I am here");
                        DataManager.getInstance().addData(DataManager.PLOT_GEO_BOUNDARIES, getGeoBoundriesData());
                        CommonConstants.isFromPlotDetails = true;
                        Intent intent = new Intent();
                        intent.putExtra("result_key", gpsArea);
                        setResult(RESULT_OK, intent);
                        firstFourCoordinates.clear();
                        recordedBoundries.clear();
                        finish();
                    }

                   else {
                       Log.d("InRetake", "yes");

                        plot = (Plot) DataManager.getInstance().getDataFromManager(DataManager.PLOT_DETAILS);
                        if (plot == null) {
                            DataAccessHandler dataAccessHandler = new DataAccessHandler(PreViewAreaCalScreen.this);
                            plot = (Plot) dataAccessHandler.getSelectedPlotData(Queries.getInstance().getSelectedPlot(CommonConstants.PLOT_CODE), 0);
                        }
                        try {
                            plot.setGpsplotarea(Double.parseDouble(gpsArea));
                        } catch (NumberFormatException nfe) {
                            Log.e(PreViewAreaCalScreen.class.getSimpleName(), "" + nfe.getMessage());
                        }

                        DataManager.getInstance().addData(DataManager.PLOT_DETAILS, plot);
                        DataManager.getInstance().addData(DataManager.PLOT_GEO_BOUNDARIES, getGeoBoundriesData());

                        if (CommonUtils.isPlotSplitFarmerPlots()) {
                            updateFarmerHistory();
                        }

                        if (!CommonUtils.isPlotSplitFarmerPlots()) {
                            firstFourCoordinates.clear();
                            recordedBoundries.clear();
                            gpsArea = null;
                            finish();
                        }

//                    if (CommonUtils.isNewRegistration()) {
//                        Intent intent = new Intent();
//                        intent.putExtra("result_key", gpsArea);
//                        setResult(RESULT_OK, intent);
//                        firstFourCoordinates.clear();
//                        recordedBoundries.clear();
//                        gpsArea = null;
//                        finish();
//                    }
                    }

                }
                else {
                    Log.d("nocoordinates", "I am here");
                    Log.d("generatedplotCode", generatedplotCode + "");
                    UiUtils.showCustomToastMessage("Please Calculate Area", PreViewAreaCalScreen.this, 1);
                }
            }
        });

        gpsAreaText = (TextView) parentView.findViewById(R.id.gps_text);

        lat1p1 = (TextView) parentView.findViewById(R.id.lat1p1);
        lat2p2 = (TextView) parentView.findViewById(R.id.lat2p2);
        lat3p3 = (TextView) parentView.findViewById(R.id.lat3p3);
        lat4p4 = (TextView) parentView.findViewById(R.id.lat4p4);


        long1p1 = (TextView) parentView.findViewById(R.id.long1p1);
        long2p2 = (TextView) parentView.findViewById(R.id.long2p2);
        long3p3 = (TextView) parentView.findViewById(R.id.long3p3);
        long4p4 = (TextView) parentView.findViewById(R.id.long4p4);
    }

    private void updateFarmerHistory() {

        UpdatedDate = CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS);
        ServerUpdatedStatus = CommonConstants.ServerUpdatedStatus;
        UpdatedByUserId = String.valueOf(Integer.parseInt(CommonConstants.USER_ID));
        Is_Active = "0";
        palm3FoilDatabase.UpdateFarmerhistory(UpdatedByUserId, UpdatedDate, Is_Active);
        displayDialogWindow("GeoBoundaries are  saving in db", alertDialog, PreViewAreaCalScreen.this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null == data) return;
        try {
            updateGpsData(data);
        } catch (Exception e) {
            Log.pushExceptionToCrashlytics(new OilPalmException(e.getMessage()));
            UiUtils.showCustomToastMessage("Retake The Geo Boundaries", PreViewAreaCalScreen.this, 1);

            e.printStackTrace();
        }
    }

    public void updateGpsData(Intent data) throws Exception {

        gpsArea = String.valueOf(data.getDoubleExtra("area", 0));
        gpsAreaText.setText("" + gpsArea + " Ha");

        if (null != firstFourCoordinates && firstFourCoordinates.size() > 4) {
            lat1p1.setText("" + firstFourCoordinates.get(0).latitude);
            lat2p2.setText("" + firstFourCoordinates.get(1).latitude);
            lat3p3.setText("" + firstFourCoordinates.get(2).latitude);
            lat4p4.setText("" + firstFourCoordinates.get(3).latitude);

            long1p1.setText("" + firstFourCoordinates.get(0).longitude);
            long2p2.setText("" + firstFourCoordinates.get(1).longitude);
            long3p3.setText("" + firstFourCoordinates.get(2).longitude);
            long4p4.setText("" + firstFourCoordinates.get(3).longitude);
        } else {
            lat1p1.setText("");
            lat2p2.setText("");
            lat3p3.setText("");
            lat4p4.setText("");

            long1p1.setText("");
            long2p2.setText("");
            long3p3.setText("");
            long4p4.setText("");
        }
    }

//    public List<GeoBoundaries> getGeoBoundriesData() {
//        List<GeoBoundaries> geoBoundaries = new ArrayList<>();
//
//                HashSet<GPSCoordinate> hashSet = new HashSet<GPSCoordinate>();
//                hashSet.addAll(recordedBoundries);
//                recordedBoundries.clear();
//                recordedBoundries.addAll(hashSet);
//
//        if (null != recordedBoundries && !recordedBoundries.isEmpty()) {
//            for (int i = 0; i < recordedBoundries.size(); i++) {
//                GeoBoundaries geoBoundary = new GeoBoundaries();
//                geoBoundary.setLatitude(recordedBoundries.get(i).latitude);
//                geoBoundary.setLongitude(recordedBoundries.get(i).longitude);
//                if (CommonUtils.isNewPlotRegistration() || CommonUtils.isNewRegistration()) {
//                    geoBoundary.setGeocategorytypeid(384);
//                }else{
//                    geoBoundary.setGeocategorytypeid(206);
//                }
//                geoBoundaries.add(geoBoundary);
//            }
////            for (GPSCoordinate gpsCoordinate : recordedBoundries) {
////
////                geoBoundary.setLatitude(gpsCoordinate.latitude);
////                geoBoundary.setLongitude(gpsCoordinate.longitude);
////                geoBoundary.setGeocategorytypeid(206);
////                geoBoundaries.add(geoBoundary);
////            }
//            if (CommonUtils.isPlotSplitFarmerPlots()) {
//                GeoBoundaries geoBoundary = new GeoBoundaries();
//                geoBoundary.setLatitude(recordedBoundries.get(0).latitude);
//                geoBoundary.setLongitude(recordedBoundries.get(0).longitude);
//                geoBoundary.setGeocategorytypeid(207);
//                geoBoundaries.add(geoBoundary);
//            }
//        }
//        return geoBoundaries;
//    }

    public List<GeoBoundaries> getGeoBoundriesData() {
        List<GeoBoundaries> geoBoundaries = new ArrayList<>();
        Set<String> coordinateSet = new HashSet<>();

        if (null != recordedBoundries && !recordedBoundries.isEmpty()) {
            for (GPSCoordinate gpsCoordinate : recordedBoundries) {
                String coordinateKey = getCoordinateKey(gpsCoordinate);

                // Check if the coordinate already exists in the set
                if (!coordinateSet.contains(coordinateKey)) {
                    GeoBoundaries geoBoundary = new GeoBoundaries();
                    geoBoundary.setLatitude(gpsCoordinate.latitude);
                    geoBoundary.setLongitude(gpsCoordinate.longitude);

                    if (CommonUtils.isNewPlotRegistration() || CommonUtils.isNewRegistration()) {
                        geoBoundary.setGeocategorytypeid(384);
                    } else {
                        geoBoundary.setGeocategorytypeid(206);
                    }

                    geoBoundaries.add(geoBoundary);
                    Log.d("Addedtothelistlat", geoBoundaries.get(0).getLatitude() +"" );
                    Log.d("Addedtothelistlong", geoBoundaries.get(0).getLongitude() +"" );

                    Log.d("Addedtothelistboundarylat", geoBoundary.getLatitude() +"" );
                    Log.d("Addedtothelistboundarylong", geoBoundary.getLongitude() +"" );


                    // Add the coordinate key to the set
                    coordinateSet.add(coordinateKey);
                }
            }

            if (CommonUtils.isPlotSplitFarmerPlots()) {
                GPSCoordinate firstCoordinate = recordedBoundries.get(0);
                String firstCoordinateKey = getCoordinateKey(firstCoordinate);

                // Check if the first coordinate already exists in the set
                if (coordinateSet.contains(firstCoordinateKey)) {
                    GeoBoundaries geoBoundary = new GeoBoundaries();
                    geoBoundary.setLatitude(firstCoordinate.latitude);
                    geoBoundary.setLongitude(firstCoordinate.longitude);
                    geoBoundary.setGeocategorytypeid(207);
                    geoBoundaries.add(geoBoundary);
                }
            }
        }

        return geoBoundaries;
    }

    private String getCoordinateKey(GPSCoordinate coordinate) {
        return coordinate.latitude + ":" + coordinate.longitude;
    }


    public void displayDialogWindow(String s, AlertDialog alertDialog, final Context context) {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View dialogRootView = layoutInflater.inflate(R.layout.dialog_custom, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(dialogRootView);
        this.alertDialog = alertDialogBuilder.create();
        final TextView textView = (TextView) dialogRootView.findViewById(R.id.description);
        final TextView okBtn = (TextView) dialogRootView.findViewById(R.id.okBtn);
        final AlertDialog finalAlertDialog = this.alertDialog;
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataSavingHelper.updatePlotSliptFarmerGeoboundaries(context, new ApplicationThread.OnComplete<String>() {
                    @Override
                    public void execute(boolean success, String result, String msg) {
                        super.execute(success, result, msg);
                        if (success) {
                            firstFourCoordinates.clear();
                            recordedBoundries.clear();
                            totalBoundries.clear();
                            gpsArea = null;
                            Toast.makeText(PreViewAreaCalScreen.this, "sucess", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                finalAlertDialog.dismiss();
                finish();
            }
        });
        textView.setText(s);
        // create an alert dialog

//        alertDialog.getWindow()
//                .getAttributes().windowAnimations = R.style.DialogAnimation;
        this.alertDialog.show();
        this.alertDialog.setCanceledOnTouchOutside(false);
        this.alertDialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                }
                return true;
            }
        });

    }

}
