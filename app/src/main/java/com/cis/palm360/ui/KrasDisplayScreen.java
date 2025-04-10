package com.cis.palm360.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.cis.palm360.ExpandableListview.HeaderCategory;
import com.cis.palm360.ExpandableListview.HeaderCategoryAdapter;
import com.cis.palm360.R;
import com.cis.palm360.cloudhelper.ApplicationThread;
import com.cis.palm360.cloudhelper.Log;
import com.cis.palm360.common.CommonConstants;
import com.cis.palm360.database.DataAccessHandler;
import com.cis.palm360.database.Queries;
import com.cis.palm360.datasync.helpers.DataManager;
import com.cis.palm360.dbmodels.AnnualTagetsKRA;
import com.cis.palm360.dbmodels.MonthlyTagetsKRA;
import com.cis.palm360.dbmodels.UserDetails;
import com.cis.palm360.kras.KraAdapterData;
import com.cis.palm360.kras.StickyHeaderViewAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.cis.palm360.datasync.helpers.DataManager.USER_DETAILS;


//Displaying KRA(User Targets)
public class KrasDisplayScreen extends AppCompatActivity {
    public static final String LOG_TAG = KrasDisplayScreen.class.getName();
    LinkedHashMap<String, List<AnnualTagetsKRA>> AnnualkrasListToDisplay = null;
    LinkedHashMap<String, List<MonthlyTagetsKRA>> MonathlykrasList = null;
    private RecyclerView rvKrasList;
    private StickyHeaderViewAdapter krasAdapter;
    private LinearLayoutManager layoutManager;
    private Button refreshBtn;
    private DataAccessHandler dataAccessHandler;
    private LinearLayout usersLinearLayout;
    private Spinner usersSpinner;
    private List<UserDetails> userDetailsList = null;
    protected boolean userSelect = true;
    final List<HeaderCategory> kraHeaderCategories = new ArrayList<>();
    private HeaderCategoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kras_display_screen);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("KrasDisplayScreen");

        rvKrasList =  findViewById(R.id.rvKras);
        layoutManager = new LinearLayoutManager(this);
      //   refreshBtn = findViewById(R.id.refreshBtn);
//        usersLinearLayout =  findViewById(R.id.usersLinearLayout);
//        usersSpinner =  findViewById(R.id.userNameSpin);
//        usersSpinner.setSelection(0, false);

        dataAccessHandler = new DataAccessHandler(KrasDisplayScreen.this);


//        List userNames = new ArrayList();
//
//        String username = "";
//
//        username = dataAccessHandler.getOnlyO neValueFromDb(Queries.getInstance().getusernamequery(CommonConstants.USER_ID));
//
//        userNames.add(username);
//
//        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter<>(KrasDisplayScreen.this, android.R.layout.simple_spinner_item, userNames.toArray());
//        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        usersSpinner.setAdapter(spinnerArrayAdapter);

      updateUI(Integer.parseInt(CommonConstants.USER_ID));
        //updateUI(1);




     //   updateUI(0);
    }


    public void bindUserInfo() {
        final UserDetails userDetails = (UserDetails) DataManager.getInstance().getDataFromManager(USER_DETAILS);

        userDetailsList = (List<UserDetails>) dataAccessHandler.getUserDetails(Queries.getInstance().getUserDetailsForKrasQuery(Integer.parseInt(userDetails.getId())), 1);

        List userNames = new ArrayList();
        //userNames.add("All");
        for (int i = 0; i < userDetailsList.size(); i++) {
            userNames.add(userDetailsList.get(i).getUserName());
        }

        String username = "";

        username = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getusernamequery(CommonConstants.USER_ID));

        userNames.add(username);

        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter<>(KrasDisplayScreen.this, android.R.layout.simple_spinner_item,
                userNames.toArray());
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        usersSpinner.setAdapter(spinnerArrayAdapter);

        usersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (userSelect) {
                    userSelect = false;
                } else {
                    if (i == 0) {
                        AnnualkrasListToDisplay.clear();
                        kraHeaderCategories.clear();
                        rvKrasList.setVisibility(View.GONE);
                        updateUI(0);
                    } else {
                        AnnualkrasListToDisplay.clear();
                        kraHeaderCategories.clear();
                        rvKrasList.setVisibility(View.GONE);
                        updateUI(Integer.parseInt(CommonConstants.USER_ID));
                        updateUI(1);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public List<KraAdapterData> getKrasDisplayItems() {
        List<KraAdapterData> krasListWithStickyHeader = new ArrayList<>();
        kraHeaderCategories.clear(); // Ensure we clear old data

        for (Object o : AnnualkrasListToDisplay.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            String key = (String) entry.getKey();
            List<AnnualTagetsKRA> value = (List<AnnualTagetsKRA>) entry.getValue();

            // Convert Annual Targets into Monthly Targets (Modify as per your data structure)
            List<MonthlyTagetsKRA> monthlyTargets = new ArrayList<>();
            for (AnnualTagetsKRA annualTarget : value) {
                monthlyTargets =  dataAccessHandler.getMonthlyTargetsForKRA(annualTarget.getKraCode());
Log.e("=======>monthlyTargets",monthlyTargets.size()+"");
            // Ensure you fetch monthly data
            }

            HeaderCategory kraHeader = new HeaderCategory(

                    value.get(0).getKraCode()
                            + "\n" + value.get(0).getKraName()
                            + "\n" + value.get(0).getAnnualTarget()
                            + "\n" + value.get(0).getUom()
                            + "\n" + value.get(0).getAchievedTarget()
                            + "\n" + value.get(0).getUom(),
                    monthlyTargets
            );

            kraHeaderCategories.add(kraHeader);
        }

        return krasListWithStickyHeader;
    }

//    public List<KraAdapterData> getKrasDisplayItems() {
//
//        List<KraAdapterData> krasListWithStickyHeader = new ArrayList<>();
//        for (Object o : AnnualkrasListToDisplay.entrySet()) {
//            Map.Entry entry = (Map.Entry) o;
//            String key = (String) entry.getKey();
//            List<AnnualTagetsKRA> value = (List<AnnualTagetsKRA>) entry.getValue();
//            HeaderCategory kraHeader = new HeaderCategory(
//                    value.get(0).getKraCode()
//                            + "\n" + value.get(0).getKraName()
//                            + "\n" + value.get(0).getAnnualTarget()
//                            + "\n" + value.get(0).getUom()
//                            + "\n" + value.get(0).getAchievedTarget()
//                            + "\n" + value.get(0).getUom(), value);
//            System.out.println("Key = " + key + ", Value = " + value);
//            kraHeaderCategories.add(kraHeader);
//            krasListWithStickyHeader.add(new KraItemHeader(value));
//            krasListWithStickyHeader.addAll(KraAdapterData.createKrasDisplayList(value));
//        }
//        return krasListWithStickyHeader;
//    }


    private void updateUI(int userId) {
        AnnualkrasListToDisplay = dataAccessHandler.getKrasDataToDisplay(Queries.getInstance().getuserAnnualTarget());
        final List<KraAdapterData> krasListWithStickyHeader = getKrasDisplayItems();
        Log.v(LOG_TAG, "@@@ kras data " + AnnualkrasListToDisplay);

        ApplicationThread.uiPost(LOG_TAG, "### populateList, updating list", new Runnable() {
            @Override
            public void run() {
                if (kraHeaderCategories != null && !kraHeaderCategories.isEmpty()) {
                    rvKrasList.setVisibility(View.VISIBLE);

                    // Set Expandable Adapter
                    mAdapter = new HeaderCategoryAdapter(KrasDisplayScreen.this, kraHeaderCategories);
                    rvKrasList.setAdapter(mAdapter);
                    rvKrasList.setLayoutManager(new LinearLayoutManager(KrasDisplayScreen.this));
                } else {
                    rvKrasList.setVisibility(View.GONE);
                }
            }
        });
    }


//    private void updateUI(int userId) {
//        AnnualkrasListToDisplay = dataAccessHandler.getKrasDataToDisplay(Queries.getInstance().getuserAnnualTarget());
//        final List<KraAdapterData> krasListWithStickyHeader = getKrasDisplayItems();
//        Log.v(LOG_TAG, "@@@ kras data " + AnnualkrasListToDisplay);
//        ApplicationThread.uiPost(LOG_TAG, "### populateList, updating list", new Runnable() {
//            @Override
//            public void run() {
//                if (krasListWithStickyHeader != null && krasListWithStickyHeader.size() > 0) {
//                    rvKrasList.setVisibility(View.VISIBLE);
//                    /*krasAdapter = new StickyHeaderViewAdapter(krasListWithStickyHeader, KrasDisplayScreen.this)
//                            .RegisterItemType(new KrasItemHeaderViewBinder())
//                            .RegisterItemType(new KraItemViewBinder());*/
//
//                    //rvKrasList.setAdapter(krasAdapter);
//                    mAdapter = new HeaderCategoryAdapter(KrasDisplayScreen.this, kraHeaderCategories);
//                    rvKrasList.setAdapter(mAdapter);
//                    rvKrasList.setLayoutManager(layoutManager);
//                } else {
//                    rvKrasList.setVisibility(View.GONE);
//                }
//            }
//        });
//    }



}
