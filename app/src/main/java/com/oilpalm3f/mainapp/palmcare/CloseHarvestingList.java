package com.oilpalm3f.mainapp.palmcare;

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

import com.oilpalm3f.mainapp.R;
import com.oilpalm3f.mainapp.alerts.AlertsVisitsInfo;
import com.oilpalm3f.mainapp.cloudhelper.ApplicationThread;
import com.oilpalm3f.mainapp.common.CommonConstants;
import com.oilpalm3f.mainapp.common.CommonUtils;
import com.oilpalm3f.mainapp.common.PinEntryEditText;
import com.oilpalm3f.mainapp.database.DataAccessHandler;
import com.oilpalm3f.mainapp.database.DatabaseKeys;
import com.oilpalm3f.mainapp.database.Queries;
import com.oilpalm3f.mainapp.dbmodels.HarvestorVisitHistory;
import com.oilpalm3f.mainapp.service.APIConstantURL;
import com.oilpalm3f.mainapp.service.ApiService;
import com.oilpalm3f.mainapp.service.ServiceFactory;
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

public class CloseHarvestingList extends AppCompatActivity implements ClosedHarvestDetailsAdapter.ButtonClickListener {
    private ActionBar actionBar;
    private Toolbar toolbar;
    private static final String LOG_TAG = CloseHarvestingList.class.getName();
    private List<ClosedDataDetails> ClosedharvestInfoList = new ArrayList<>();
    private ClosedHarvestDetailsAdapter closedharvestDetailsRecyclerAdapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView closedharvest_list;
    private DataAccessHandler dataAccessHandler;
    public static final int LIMIT = 30;
    private int offset;
    private Subscription mSubscription;
    private PinEntryEditText pinEntry;
    Button dialogButton;
    TextView no_text;

    private EditText searchtext;
    private ImageView clearsearch;
    private android.widget.ProgressBar searchprogress;
    private boolean isSearch = false;
    private boolean hasMoreItems = true;

    String searchKey = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_harvesting_list);
        intviews();
        setviews();
    }

    private void intviews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Close Harvesting Visit");
        }
        no_text = findViewById(R.id.no_text);
        closedharvest_list =  findViewById(R.id.closedharvest_list);
        dataAccessHandler = new DataAccessHandler(this);

        searchtext = (EditText) findViewById(R.id.searchtext);
        clearsearch = (ImageView) findViewById(R.id.clearsearch);
        searchprogress = (android.widget.ProgressBar) findViewById(R.id.searchprogress);
    }

    private void setviews() {
        offset = offset + LIMIT;

        ClosedharvestList();

        clearsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSearch = false;
                ClosedharvestInfoList.clear();
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

            com.oilpalm3f.mainapp.cloudhelper.Log.d("WhatisinSearch", "is :"+ s);
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
        com.oilpalm3f.mainapp.cloudhelper.Log.d("DoSearchQuery", "is :" +  searchQuery);
        offset = 0;
        hasMoreItems = true;
        if (searchQuery !=null &  !TextUtils.isEmpty(searchQuery)  & searchQuery.length()  > 0) {

            offset = 0;
            isSearch = true;
            searchKey = searchQuery.trim();
            ClosedharvestList();
        } else {
            searchKey = "";
            isSearch = false;
            ClosedharvestList();
        }
    }

    private void ClosedharvestList() {
        //ProgressBar.showProgressBar(this, "Please wait...");

        if (searchprogress != null) {
            searchprogress.setVisibility(View.VISIBLE);
        }

        ApplicationThread.bgndPost(LOG_TAG, "renderAlerts", new Runnable() {
            @Override
            public void run() {

                ClosedharvestInfoList = (List<ClosedDataDetails>) dataAccessHandler.getClosedcropInfo(Queries.getInstance().getclosedharvestinfo(searchKey), 1);
                Collections.reverse(ClosedharvestInfoList);

                ApplicationThread.uiPost(LOG_TAG, "", new Runnable() {
                    @Override
                    public void run() {
                        //ProgressBar.hideProgressBar();
                        searchprogress.setVisibility(View.GONE);
                        closedharvestDetailsRecyclerAdapter = new ClosedHarvestDetailsAdapter(CloseHarvestingList.this, ClosedharvestInfoList,CloseHarvestingList.this);
                        if (ClosedharvestInfoList != null && !ClosedharvestInfoList.isEmpty()&& ClosedharvestInfoList.size()!=0) {
                            closedharvest_list.setVisibility(View.VISIBLE);
                            no_text.setVisibility(View.GONE);
                            layoutManager = new LinearLayoutManager(CloseHarvestingList.this, LinearLayoutManager.VERTICAL, false);
                            closedharvest_list.setLayoutManager(layoutManager);
                            closedharvest_list.setAdapter(closedharvestDetailsRecyclerAdapter);
                            //   setTitle(alert_type, offset == 0 ? alertsVisitsInfoList.size() : offset);
                        }
                        else{
                            closedharvest_list.setVisibility(View.GONE);
                            no_text.setVisibility(View.VISIBLE);
                        }

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
    public void onItemSelected(ClosedDataDetails harvestingInfo) {
        Log.e("========>",harvestingInfo.getCropCode());
        if (CommonUtils.isNetworkAvailable(CloseHarvestingList.this)) {
            sendotpbyharvestingcode(harvestingInfo.getCropCode());
        }
        else
        {

            UiUtils.showCustomToastMessage("Please check network connection", CloseHarvestingList.this, 1);

        }

    }


        private void sendotpbyharvestingcode(String harvestCode) {
            ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
            mSubscription = service.getFormerOTP(APIConstantURL.SendOTPForHarvestorVisit +"/"+harvestCode)
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
                                UiUtils.showCustomToastMessage("Sent OTP successfully ", CloseHarvestingList.this, 0);


                            } else {
                                UiUtils.showCustomToastMessage("Invalid Otp", CloseHarvestingList.this, 1);
                            }
                        }
                    });

        }

    @Override
    public void onItemclosed(ClosedDataDetails harvestingInfo) {
        Log.e("========>",harvestingInfo.getCropCode());
        if (CommonUtils.isNetworkAvailable(CloseHarvestingList.this)) {
            otppopup(harvestingInfo.getCropCode());
        }
        else
        {

            UiUtils.showCustomToastMessage("Please check network connection", CloseHarvestingList.this, 1);

        }

    }

    private void otppopup(String harvestcode) {
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

                    closedbyharvesting(harvestcode,pinEntry.getText().toString().trim());

                } else {
                    UiUtils.showCustomToastMessage("Please Enter OTP", CloseHarvestingList.this, 1);
                    //pinEntry.setError("Please Enter Pin");
                }

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void closedbyharvesting(String harvestcode, String PIN) {
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getFormerOTP(APIConstantURL.VerifyForHarvestorOTP + harvestcode + "/"+PIN)
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
//

                        if (ResponceModel.getIsSuccess()) {
                           // Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
                            String whereCondition = " where Code = '"+ harvestcode +"'" ;
                            List<LinkedHashMap> details = new ArrayList<>();
                            LinkedHashMap map = new LinkedHashMap();
                            map.put("UpdatedByUserId",Integer.parseInt(CommonConstants.USER_ID));
                            map.put("UpdatedDate",CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
                            map.put("IsVerified",true);
                            map.put("ServerUpdatedStatus",0);
                            details.add(map);
                            dataAccessHandler.updateData(DatabaseKeys.TABLE_HarvestorVisitHistory, details, true, whereCondition, new ApplicationThread.OnComplete<String>() {
                                @Override
                                public void execute(boolean success, String result, String msg) {
                                    if (success) {
                                        ClosedharvestList();
                                        UiUtils.showCustomToastMessage("Harvesting Visit Closed successfully ", CloseHarvestingList.this, 0);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });



                        } else {
                            UiUtils.showCustomToastMessage("Please Enter Valid Otp", CloseHarvestingList.this, 1);
                        }
                    }
                });


    }
}