package com.cis.palm360.palmcare;

import android.app.Dialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cis.palm360.R;
import com.cis.palm360.cloudhelper.ApplicationThread;
import com.cis.palm360.common.CommonConstants;
import com.cis.palm360.common.CommonUtils;
import com.cis.palm360.common.PinEntryEditText;
import com.cis.palm360.database.DataAccessHandler;
import com.cis.palm360.database.DatabaseKeys;
import com.cis.palm360.database.Queries;
import com.cis.palm360.service.APIConstantURL;
import com.cis.palm360.service.ApiService;
import com.cis.palm360.service.ServiceFactory;
import com.cis.palm360.utils.UiUtils;

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

    private EditText searchtext;
    private ImageView clearsearch;
    private android.widget.ProgressBar searchprogress;
    private boolean isSearch = false;
    private boolean hasMoreItems = true;

    String searchKey = "";
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

        searchtext = (EditText) findViewById(R.id.searchtext);
        clearsearch = (ImageView) findViewById(R.id.clearsearch);
        searchprogress = (android.widget.ProgressBar) findViewById(R.id.searchprogress);

        dataAccessHandler = new DataAccessHandler(this);
    }
    private void setviews() {
        offset = offset + LIMIT;

        ClosedcropList();

        clearsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSearch = false;
                ClosedcropInfoList.clear();
                searchtext.setText("");
            }
        });

        searchtext.addTextChangedListener(mTextWatcher);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            com.cis.palm360.cloudhelper.Log.d("WhatisinSearch", "is :"+ s);
            //
            offset = 0;
            ApplicationThread.uiPost(LOG_TAG, "search", new Runnable() {
                @Override
                public void run() {
                    doSearch(s.toString().trim());
                    if (s.toString().length() > 0) {
                        clearsearch.setVisibility(View.VISIBLE);
                    } else {
                        clearsearch.setVisibility(View.GONE);
                    }
                }
            }, 100);
        }

        @Override
        public void afterTextChanged(final Editable s) {

        }
    };

    public void doSearch(String searchQuery) {
        com.cis.palm360.cloudhelper.Log.d("DoSearchQuery", "is :" +  searchQuery);
        offset = 0;
        hasMoreItems = true;
        if (searchQuery !=null &  !TextUtils.isEmpty(searchQuery)  & searchQuery.length()  > 0) {

            offset = 0;
            isSearch = true;
            searchKey = searchQuery.trim();
            ClosedcropList();
        } else {
            searchKey = "";
            isSearch = false;
            ClosedcropList();
        }
    }


    private void ClosedcropList() {
        //ProgressBar.showProgressBar(this, "Please wait...");

        if (searchprogress != null) {
            searchprogress.setVisibility(View.VISIBLE);
        }

        ApplicationThread.bgndPost(LOG_TAG, " ClosedcropList", new Runnable() {
            @Override
            public void run() {

                ClosedcropInfoList = (List<ClosedDataDetails>) dataAccessHandler.getClosedcropInfo(Queries.getInstance().getclosedcropinfo(searchKey), 1);
                Collections.reverse(ClosedcropInfoList);

                ApplicationThread.uiPost(LOG_TAG, "", new Runnable() {
                    @Override
                    public void run() {
                        searchprogress.setVisibility(View.GONE);
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