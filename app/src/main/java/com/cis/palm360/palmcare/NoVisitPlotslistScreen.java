package com.cis.palm360.palmcare;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cis.palm360.R;
import com.cis.palm360.database.DataAccessHandler;
import com.cis.palm360.utils.UiUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoVisitPlotslistScreen extends AppCompatActivity {
    public static final int LIMIT = 30;
    private int offset;
    private EditText fromDate,toDate;
    private Button searchBtn;
    private String fromDateStr = "";
    private String toDateStr = "";
    private boolean isLoading = false;
    private Calendar myCalendar = Calendar.getInstance();
    private boolean hasMoreItems = true;
    private List<NotVisitedPlotsInfo> notVisitedplotsInfoList = new ArrayList<>();
    private NoVisitsInfoAdapter noVisitsDetailsRecyclerAdapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView novisitplot_list;
    private DataAccessHandler dataAccessHandler;
    PageAdapter pageAdapter;
    TextView no_text;
    private ActionBar actionBar;
    private Toolbar toolbar;
    private static final String LOG_TAG = NoVisitPlotslistScreen.class.getName();


    private EditText searchtext;
    private ImageView clearsearch;
    private android.widget.ProgressBar searchprogress;
    private boolean isSearch = false;

    String searchKey = "";
    ViewPager viewPager;
    TabLayout tabLayout;

    TabItem tabcrop;
    TabItem tabHarvest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_visit_plotslist_screen);
        intviews();
        setviews();
    }

    private void intviews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Not Visited Plots");
        }
        fromDate = findViewById(R.id.fromDate);
        toDate = findViewById(R.id.toDate);
        searchBtn = findViewById(R.id.searchBtnT);
//        novisitplot_list =  findViewById(R.id.novisitplot_list);
        no_text = findViewById(R.id.no_text);
        dataAccessHandler = new DataAccessHandler(this);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        fromDate.setText(sdf.format(new Date()));
        toDate.setText(sdf.format(new Date()));
        tabLayout = findViewById(R.id.tabLayout);
//        tabcrop = findViewById(R.id.tabcrop);
//        tabHarvest = findViewById(R.id.tabHarvest);
//        searchtext = (EditText) findViewById(R.id.searchtext);
//        clearsearch = (ImageView) findViewById(R.id.clearsearch);
        searchprogress = (android.widget.ProgressBar) findViewById(R.id.searchprogress);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Crop Maintenance Visit"));
        tabLayout.addTab(tabLayout.newTab().setText("Harvesting Visit"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);




        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
    private void setviews() {
        offset = offset + LIMIT;

        searchBtn.setOnClickListener(v ->{
            Intent intent = new Intent("KEY");
            intent.putExtra("todate", toDateStr);
            intent.putExtra("fromdate", fromDateStr);
            sendBroadcast(intent);

//            ProgressBar.showProgressBar(this, "Please wait...");
//            notVisitedplotsInfoList = (List<NotVisitedPlotsInfo>) dataAccessHandler.getNotvisitedplotInfo(Queries.getInstance().getnotvisitedpoltlist(LIMIT, offset,fromDateStr,toDateStr, searchKey), 1);
//            Collections.reverse(notVisitedplotsInfoList);
//
//            ApplicationThread.uiPost(LOG_TAG, "", new Runnable() {
//                @Override
//                public void run() {
//                    ProgressBar.hideProgressBar();
//                    noVisitsDetailsRecyclerAdapter = new NoVisitsInfoAdapter(NoVisitPlotslistScreen.this, notVisitedplotsInfoList);
//                    if (notVisitedplotsInfoList != null && !notVisitedplotsInfoList.isEmpty() && notVisitedplotsInfoList.size()!= 0) {
//                        novisitplot_list.setVisibility(View.VISIBLE);
//                        no_text.setVisibility(View.GONE);
//                        layoutManager = new LinearLayoutManager(NoVisitPlotslistScreen.this, LinearLayoutManager.VERTICAL, false);
//                        novisitplot_list.setLayoutManager(layoutManager);
//
//                        novisitplot_list.setAdapter(noVisitsDetailsRecyclerAdapter);
//                     //   setTitle(alert_type, offset == 0 ? alertsVisitsInfoList.size() : offset);
//                    }
//                    else{
//                        novisitplot_list.setVisibility(View.GONE);
//                        no_text.setVisibility(View.VISIBLE);
//                    }
//                }
//            });
        });
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(0);
            }
        };

        final DatePickerDialog.OnDateSetListener toDateD = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(1);
            }
        };
        String dateFormatter = "yyyy-MM-dd";
        SimpleDateFormat sdf2 = new SimpleDateFormat(dateFormatter, Locale.US);
        fromDateStr = sdf2.format(myCalendar.getTime());
        toDateStr = sdf2.format(myCalendar.getTime());
        fromDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(NoVisitPlotslistScreen.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));  //date is dateSetListener as per your code in question
            datePickerDialog.show();
        });


        toDate.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(NoVisitPlotslistScreen.this, toDateD, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));  //date is dateSetListener as per your code in question
            datePickerDialog.show();
        });



//        clearsearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isSearch = false;
//                notVisitedplotsInfoList.clear();
//                searchtext.setText("");
//            }
//        });
//
//        searchtext.addTextChangedListener(mTextWatcher);
//    }

//    private TextWatcher mTextWatcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            com.oilpalm3f.mainapp.cloudhelper.Log.d("WhatisinSearch", "is :"+ s);
//            //
//            offset = 0;
//            ApplicationThread.uiPost(LOG_TAG, "search", new Runnable() {
//                @Override
//                public void run() {
//                    doSearch(s.toString().trim());
//                    if (s.toString().length() > 0) {
//                        clearsearch.setVisibility(View.VISIBLE);
//                    } else {
//                        clearsearch.setVisibility(View.GONE);
//                    }
//                }
//            }, 100);
//        }
//
//        @Override
//        public void afterTextChanged(final Editable s) {
//
//        }
//    };
//
//    public void doSearch(String searchQuery) {
//        com.oilpalm3f.mainapp.cloudhelper.Log.d("DoSearchQuery", "is :" +  searchQuery);
//        offset = 0;
//        hasMoreItems = true;
//        if (searchQuery !=null &  !TextUtils.isEmpty(searchQuery)  & searchQuery.length()  > 0) {
//
//            offset = 0;
//            isSearch = true;
//            searchKey = searchQuery.trim();
//            notvisitedplotslist(offset);
//        } else {
//            searchKey = "";
//            isSearch = false;
//            notvisitedplotslist(offset);
//        }
//    }


//    private void notvisitedplotslist(final int offset) {
//        //ProgressBar.showProgressBar(this, "Please wait...");
//
//        if (searchprogress != null) {
//            searchprogress.setVisibility(View.VISIBLE);
//        }
//        ApplicationThread.bgndPost(LOG_TAG, "notvisitedplots", new Runnable() {
//            @Override
//            public void run() {
//
//                notVisitedplotsInfoList = (List<NotVisitedPlotsInfo>) dataAccessHandler.getNotvisitedplotInfo(Queries.getInstance().getnotvisitedpoltlist(LIMIT, offset,fromDateStr,toDateStr, searchKey), 1);
//                Collections.reverse(notVisitedplotsInfoList);
//
//                ApplicationThread.uiPost(LOG_TAG, "", new Runnable() {
//                    @Override
//                    public void run() {
//                        //ProgressBar.hideProgressBar();                        searchprogress.setVisibility(View.GONE);
//                        noVisitsDetailsRecyclerAdapter = new NoVisitsInfoAdapter(NoVisitPlotslistScreen.this, notVisitedplotsInfoList);
//                        if (notVisitedplotsInfoList != null && !notVisitedplotsInfoList.isEmpty()) {
//                            novisitplot_list.setVisibility(View.VISIBLE);
//                            no_text.setVisibility(View.GONE);
//                            layoutManager = new LinearLayoutManager(NoVisitPlotslistScreen.this, LinearLayoutManager.VERTICAL, false);
//                            novisitplot_list.setLayoutManager(layoutManager);
//
//                            novisitplot_list.setAdapter(noVisitsDetailsRecyclerAdapter);
//                            //   setTitle(alert_type, offset == 0 ? alertsVisitsInfoList.size() : offset);
//                        }
//                        else{
//                            novisitplot_list.setVisibility(View.GONE);
//                            no_text.setVisibility(View.VISIBLE);
//                        }
//                    }
//                });
//
//            }
//        });
  }
    private void updateLabel(int type) {
        String myFormat = "dd-MM-yyyy";
        String dateFormatter = "yyyy-MM-dd";

        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdf2 = new SimpleDateFormat(dateFormatter, Locale.US);

        if (type == 0) {
            String selectedFromDateStr = sdf2.format(myCalendar.getTime());
            String selectedToDateStr = toDateStr != null ? toDateStr : ""; // Get the current value of toDateStr

            if (toDateStr != null && selectedFromDateStr.compareTo(selectedToDateStr) > 0) {
                // fromDateStr is greater than toDateStr, show an error or handle accordingly
                UiUtils.showCustomToastMessage("From Date cannot be greater than To Date", NoVisitPlotslistScreen.this, 1);


                return; // Abort further execution
            }

            fromDateStr = selectedFromDateStr;
            fromDate.setText(sdf.format(myCalendar.getTime()));
        } else {
            String selectedToDateStr = sdf2.format(myCalendar.getTime());
            String selectedFromDateStr = fromDateStr != null ? fromDateStr : ""; // Get the current value of fromDateStr

            if (fromDateStr != null && selectedToDateStr.compareTo(selectedFromDateStr) < 0) {
                // toDateStr is smaller than fromDateStr, show an error or handle accordingly
                UiUtils.showCustomToastMessage("To Date cannot be smaller than From Date", NoVisitPlotslistScreen.this, 1);

               // Toast.makeText(this, "To date cannot be smaller than from date", Toast.LENGTH_SHORT).show();
                return; // Abort further execution
            }

            toDateStr = selectedToDateStr;
            toDate.setText(sdf.format(myCalendar.getTime()));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private class PageAdapter extends FragmentPagerAdapter {
        String spinner_item;
        public String getSpinner_item() {
            return spinner_item;
        }

        public void setSpinner_item(String spinner_item) {
            this.spinner_item = spinner_item;
        }


        private int numOfTabs;

        PageAdapter(FragmentManager fm, int numOfTabs) {
            super(fm);
            this.numOfTabs = numOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            //  String selectedText= SharedPrefsData.getInstance(getApplicationContext()).getStringFromSharedPrefs("sitem");
            switch (position) {
                case 0:

                    return new CropmaintenceFragment();


                case 1:
                    return new HarvestingFragment();

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return numOfTabs;
        }
    }
}