package com.oilpalm3f.mainapp.palmcare;

import android.app.Dialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.oilpalm3f.mainapp.R;
import com.oilpalm3f.mainapp.alerts.AlertsVisitsInfo;
import com.oilpalm3f.mainapp.cloudhelper.ApplicationThread;
import com.oilpalm3f.mainapp.common.CommonConstants;
import com.oilpalm3f.mainapp.common.CommonUtils;
import com.oilpalm3f.mainapp.common.PinEntryEditText;
import com.oilpalm3f.mainapp.database.DataAccessHandler;
import com.oilpalm3f.mainapp.database.DatabaseKeys;
import com.oilpalm3f.mainapp.database.Queries;
import com.oilpalm3f.mainapp.datasync.helpers.DataManager;
import com.oilpalm3f.mainapp.datasync.ui.RefreshSyncActivity;
import com.oilpalm3f.mainapp.service.APIConstantURL;
import com.oilpalm3f.mainapp.service.ApiService;
import com.oilpalm3f.mainapp.service.ServiceFactory;
import com.oilpalm3f.mainapp.ui.SplashScreen;
import com.oilpalm3f.mainapp.uihelper.ProgressBar;
import com.oilpalm3f.mainapp.utils.UiUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ClosecropMaintenanceList extends AppCompatActivity implements ClosedcropDetailsAdapter.ButtonClickListener {
    private ActionBar actionBar;
    private Toolbar toolbar;
    public static final int LIMIT = 30;
    private int offset;
    private static final String LOG_TAG = ClosecropMaintenanceList.class.getName();
    private List<ClosedDataDetails> ClosedcropInfoList = new ArrayList<>();
    private ClosedcropDetailsAdapter closedcropDetailsRecyclerAdapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView closedcrop_list;
    private DataAccessHandler dataAccessHandler;
    private Subscription mSubscription;
    String OTP;
    TextView no_text;
    Button dialogButton;
    private PinEntryEditText pinEntry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closeclosecrop_maintenance_list);
        intviews();
        setviews();
    }

    private void intviews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(" Close Crop Maintenance");
        }
        closedcrop_list =  findViewById(R.id.closedcrop_list);
        no_text = findViewById(R.id.no_text);
        dataAccessHandler = new DataAccessHandler(this);
    }
    private void setviews() {
        offset = offset + LIMIT;

        ClosedcropList();
    }


    private void ClosedcropList() {
        ProgressBar.showProgressBar(this, "Please wait...");
        ApplicationThread.bgndPost(LOG_TAG, " ClosedcropList", new Runnable() {
            @Override
            public void run() {

                ClosedcropInfoList = (List<ClosedDataDetails>) dataAccessHandler.getClosedcropInfo(Queries.getInstance().getclosedcropinfo(), 1);
                Collections.reverse(ClosedcropInfoList);

                ApplicationThread.uiPost(LOG_TAG, "", new Runnable() {
                    @Override
                    public void run() {
                        ProgressBar.hideProgressBar();
                        closedcropDetailsRecyclerAdapter = new ClosedcropDetailsAdapter(ClosecropMaintenanceList.this, ClosedcropInfoList,ClosecropMaintenanceList.this);
                        if (ClosedcropInfoList != null && !ClosedcropInfoList.isEmpty() && ClosedcropInfoList.size()!=0 ) {
                            closedcrop_list.setVisibility(View.VISIBLE);
                            no_text.setVisibility(View.GONE);
                            layoutManager = new LinearLayoutManager(ClosecropMaintenanceList.this, LinearLayoutManager.VERTICAL, false);
                            closedcrop_list.setLayoutManager(layoutManager);
                            closedcrop_list.setAdapter(closedcropDetailsRecyclerAdapter);
                            //   setTitle(alert_type, offset == 0 ? alertsVisitsInfoList.size() : offset);
                        }
                        else{
                        closedcrop_list.setVisibility(View.GONE);
                        no_text.setVisibility(View.VISIBLE);}
                    }
                });

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(ClosedDataDetails  closedcropinfo) {
        Log.e("========>",closedcropinfo.getCropCode());
        if (CommonUtils.isNetworkAvailable(ClosecropMaintenanceList.this)) {
            sendotpbycropmaintancecode(closedcropinfo.getCropCode());
        }
        else
        {

            UiUtils.showCustomToastMessage("Please check network connection", ClosecropMaintenanceList.this, 1);

        }

    }

    @Override
    public void onItemclosed(ClosedDataDetails closedcropinfo) {
        Log.e("========>",closedcropinfo.getCropCode ());
        if (CommonUtils.isNetworkAvailable(ClosecropMaintenanceList.this)) {
            otppopup(closedcropinfo.getCropCode());

        }
        else
        {

            UiUtils.showCustomToastMessage("Please check network connection", ClosecropMaintenanceList.this, 1);

        }

    }

    private void otppopup(String cropCode) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.pinentrypopup_view);
        //dialog.setCancelable(false);
        dialog.setTitle("Title...");

        //TextView farmer_name = (TextView) dialog.findViewById(R.id.farmer_Code);
        pinEntry =  dialog.findViewById(R.id.txt_pin_entry);

        ////
        dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinEntry.getText() != null & pinEntry.getText().toString().trim() != "" & !TextUtils.isEmpty(pinEntry.getText())) {

                    closedbycropmaintancecode(cropCode,pinEntry.getText().toString().trim());

                } else {
                    UiUtils.showCustomToastMessage("Please Enter OTP", ClosecropMaintenanceList.this, 1);
                    //pinEntry.setError("Please Enter Pin");
                }

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void closedbycropmaintancecode(String cropCode, String PIN) {

        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getFormerOTP(APIConstantURL.VerifyCropMaintenanceOTP + cropCode + "/"+PIN)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<OtpResponceModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onNext(OtpResponceModel ResponceModel) {

                        if (ResponceModel.getIsSuccess()) {
                        //    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
                          String whereCondition = " where Code = '"+ cropCode +"'" ;
                            List<LinkedHashMap> details = new ArrayList<>();
                            LinkedHashMap map = new LinkedHashMap();
                            map.put("UpdatedByUserId",Integer.parseInt(CommonConstants.USER_ID));
                            map.put("UpdatedDate",CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
                            map.put("IsVerified",true);
                            map.put("ServerUpdatedStatus",0);
                            details.add(map);
                            dataAccessHandler.updateData(DatabaseKeys.TABLE_CROPMAINTENANCEHISTORY, details, true, whereCondition, new ApplicationThread.OnComplete<String>() {
                                @Override
                                public void execute(boolean success, String result, String msg) {
                                    if (success) {
                                        ClosedcropList();
                                        UiUtils.showCustomToastMessage("Crop Maintenance Closed successfully ",
                                                ClosecropMaintenanceList.this, 0);

                                    } else {
                                        Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });



                        } else {
                            UiUtils.showCustomToastMessage("Please Enter Valid Otp", ClosecropMaintenanceList.this, 1);
                        }




                    }
                });
      //  UiUtils.showCustomToastMessage("Closed request", ClosecropMaintenanceList.this, 0);
    }

    private void sendotpbycropmaintancecode(String cropCode) {
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getFormerOTP(APIConstantURL.SendOTPForCropMaintenance +"/"+cropCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<OtpResponceModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onNext(OtpResponceModel ResponceModel) {




                        if (ResponceModel.getIsSuccess()) {

                            UiUtils.showCustomToastMessage("Sent OTP successfully ", ClosecropMaintenanceList.this, 0);


                        } else {

                            UiUtils.showCustomToastMessage("Invalid Otp", ClosecropMaintenanceList.this, 1);
                        }
                    }
                });

    }
}