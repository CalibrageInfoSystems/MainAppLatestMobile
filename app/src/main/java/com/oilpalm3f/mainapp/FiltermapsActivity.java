package com.oilpalm3f.mainapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.oilpalm3f.mainapp.common.CommonConstants;
import com.oilpalm3f.mainapp.common.CommonUtils;
import com.oilpalm3f.mainapp.common.MultipleSelectionSpinner;
import com.oilpalm3f.mainapp.database.DataAccessHandler;
import com.oilpalm3f.mainapp.database.Queries;
import com.oilpalm3f.mainapp.farmersearch.SearchFarmerScreen;
import com.oilpalm3f.mainapp.ui.HomeScreen;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

 public class FiltermapsActivity extends AppCompatActivity {
        private static final String LOG_TAG = FiltermapsActivity.class.getName();

        private ActionBar actionBar;
        private RelativeLayout parentPanel;
        private DataAccessHandler dataAccessHandler;
        MultipleSelectionSpinner multiSelectionSpinner_state, multiSelectionSpinner_district, multiSelectionSpinner_mandal, multiSelectionSpinner_village;
        StringBuilder selectedStateIds = new StringBuilder(), selectedDistrictIds = new StringBuilder(), selectedMandalIds = new StringBuilder(), selectedVillageIds = new StringBuilder();
        Button submitBtn;
        List<String> list = new ArrayList<>();

        private LinkedHashMap<String, Pair> stateDataMap = null, districtDataMap, mandalDataMap, villagesDataMap;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_filtermaps);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Geo Map location");
       // Disable village spinner initially
            CommonUtils.currentActivity = FiltermapsActivity.this;
            dataAccessHandler = new DataAccessHandler(FiltermapsActivity.this);
            parentPanel = (RelativeLayout) findViewById(R.id.parentPanel);

            multiSelectionSpinner_state = (MultipleSelectionSpinner) findViewById(R.id.spinner_State);
            multiSelectionSpinner_district = (MultipleSelectionSpinner) findViewById(R.id.spinner_district);
            multiSelectionSpinner_mandal = (MultipleSelectionSpinner) findViewById(R.id.spinner_Mandal);
            multiSelectionSpinner_village = (MultipleSelectionSpinner) findViewById(R.id.spinner_vilage);
            multiSelectionSpinner_district.setEnabled(false); // Disable district spinner initially
            multiSelectionSpinner_mandal.setEnabled(false); // Disable mandal spinner initially
            multiSelectionSpinner_village.setEnabled(false);
            populateSpinnerData();

            multiSelectionSpinner_state.setOnItemSelectedListener(new MultipleSelectionSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(View view, int position, LinkedHashMap<String, Integer> selectedItemsWithIds, boolean isSelected) {
                    selectedStateIds.setLength(0); // Clear previous selections
                    int count = 0;
                    for (Map.Entry<String, Integer> entry : selectedItemsWithIds.entrySet()) {
                        if (count > 0) {
                            selectedStateIds.append(", ");
                        }
                        selectedStateIds.append(entry.getValue());
                        count++;
                    }
                    Log.v(LOG_TAG, "@@@ Selected " + "== " + selectedStateIds.toString());

                    if (selectedStateIds.length() > 0) {
                        districtDataMap = dataAccessHandler.getPairData(Queries.getInstance().getDistrictQuery2(selectedStateIds.toString()));
                        updateDistrictSpinner();
                        multiSelectionSpinner_district.setEnabled(true); // Enable district spinner after selecting a state
                    } else {
                        if (districtDataMap != null) {
                            districtDataMap.clear();
                        }
                        updateDistrictSpinner();
                        multiSelectionSpinner_district.setEnabled(false); // Disable district spinner if no state is selected
                        multiSelectionSpinner_mandal.setEnabled(false); // Disable mandal spinner if no state is selected
                        multiSelectionSpinner_village.setEnabled(false); // Disable village spinner if no state is selected
                    }
                }
            });

            multiSelectionSpinner_district.setOnItemSelectedListener(new MultipleSelectionSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(View view, int position, LinkedHashMap<String, Integer> selectedItemsWithIds, boolean isSelected) {
                    selectedDistrictIds.setLength(0); // Clear previous selections
                    int count = 0;
                    for (Map.Entry<String, Integer> entry : selectedItemsWithIds.entrySet()) {
                        if (count > 0) {
                            selectedDistrictIds.append(", ");
                        }
                        selectedDistrictIds.append(entry.getValue());
                        count++;
                    }
                    Log.v(LOG_TAG, "@@@ Selected " + "== " + selectedDistrictIds.toString());

                    if (selectedDistrictIds.length() > 0) {
                        mandalDataMap = dataAccessHandler.getPairData(Queries.getInstance().getMandalsQuery2(selectedDistrictIds.toString()));
                        updateMandalSpinner();
                        multiSelectionSpinner_mandal.setEnabled(true);
                    } else {
                        if (mandalDataMap != null) {
                            mandalDataMap.clear();
                        }
                        updateMandalSpinner();
                        multiSelectionSpinner_mandal.setEnabled(false); // Disable mandal spinner if no state is selected
                        multiSelectionSpinner_village.setEnabled(false); // Disable village spinner if no state is selected
                    }
                }
            });

            multiSelectionSpinner_mandal.setOnItemSelectedListener(new MultipleSelectionSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(View view, int position, LinkedHashMap<String, Integer> selectedItemsWithIds, boolean isSelected) {
                    selectedMandalIds.setLength(0); // Clear previous selections
                    int count = 0;
                    for (Map.Entry<String, Integer> entry : selectedItemsWithIds.entrySet()) {
                        if (count > 0) {
                            selectedMandalIds.append(", ");
                        }
                        selectedMandalIds.append(entry.getValue());
                        count++;
                    }
                    Log.v(LOG_TAG, "@@@ Selected " + "== " + selectedMandalIds.toString());

                    if (selectedMandalIds.length() > 0) {
                        villagesDataMap = dataAccessHandler.getPairData(Queries.getInstance().getVillagesQuery2(selectedMandalIds.toString()));
                        updateVillagesSpinner();
                        multiSelectionSpinner_village.setEnabled(true); // Disable village spinner if no state is selected
                    } else {
                        if (villagesDataMap != null) {
                            villagesDataMap.clear();
                        }
                        updateVillagesSpinner();
                        multiSelectionSpinner_village.setEnabled(false); // Disable village spinner if no state is selected
                    }
                }
            });

            multiSelectionSpinner_village.setOnItemSelectedListener(new MultipleSelectionSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(View view, int position, LinkedHashMap<String, Integer> selectedItemsWithIds, boolean isSelected) {
                    selectedVillageIds.setLength(0); // Clear previous selections
                    int count = 0;
                    for (Map.Entry<String, Integer> entry : selectedItemsWithIds.entrySet()) {
                        if (count > 0) {
                            selectedVillageIds.append(", ");
                        }
                        selectedVillageIds.append(entry.getValue());
                        count++;
                    }
                    Log.v(LOG_TAG, "@@@ Selected " + "== " + selectedVillageIds.toString());
                }
            });

            submitBtn = (Button) findViewById(R.id.submitBtn);
            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedStateIds.length() == 0) {
                        Toast.makeText(FiltermapsActivity.this, "Please Select State", Toast.LENGTH_SHORT).show();
                    } else if (selectedDistrictIds.length() == 0) {
                        Toast.makeText(FiltermapsActivity.this, "Please Select District", Toast.LENGTH_SHORT).show();
                    } else if (selectedMandalIds.length() == 0) {
                        Toast.makeText(FiltermapsActivity.this, "Please Select Mandal", Toast.LENGTH_SHORT).show();
                    } else if (selectedVillageIds.length() == 0) {
                        Toast.makeText(FiltermapsActivity.this, "Please Select Village", Toast.LENGTH_SHORT).show();
                    } else {
                        CommonConstants.REGISTRATION_SCREEN_FROM = CommonConstants.REGISTRATION_SCREEN_FROM_Viewonmaps;
                        Intent intent = new Intent(FiltermapsActivity.this, SearchFarmerScreen.class);
                        CommonConstants.SelectedvillageIds = selectedVillageIds.toString();
                        Log.v(LOG_TAG, "@@@ Selected vilageids " + "== " +   CommonConstants.SelectedvillageIds);
                        intent.putExtra("selectedVillageIds",    CommonConstants.SelectedvillageIds);
                        startActivity(intent);
//                             startActivity(new Intent(FiltermapsActivity.this, RegistrationFlowScreen.class));
//                           finish();
                    }
                }
            });
        }

        private void updateDistrictSpinner() {
            // Lists to hold district names and their corresponding IDs
            List<String> districtNames = new ArrayList<>();
            List<Integer> districtIds = new ArrayList<>();
            districtNames.add("Select All");
            districtIds.add(0);
            // Populating the lists with district names and IDs
            if (districtDataMap != null) {
                for (Map.Entry<String, Pair> entry : districtDataMap.entrySet()) {
                    districtNames.add(String.valueOf(entry.getValue().second)); // Assuming the second value in Pair represents the district name
                    districtIds.add(Integer.parseInt(entry.getKey())); // Assuming the key represents the district ID
                }
            }
            multiSelectionSpinner_district.setItems(districtNames, districtIds);
        }

        private void updateVillagesSpinner() {
            List<String> villageNames = new ArrayList<>();
            List<Integer> villageIds = new ArrayList<>();
            villageNames.add("Select All");
            villageIds.add(0);

            if (villagesDataMap != null) {
                for (Map.Entry<String, Pair> entry : villagesDataMap.entrySet()) {
                    villageNames.add(String.valueOf(entry.getValue().second));
                    villageIds.add(Integer.parseInt(entry.getKey()));
                }
            }

            multiSelectionSpinner_village.setItems(villageNames, villageIds);
        }

        private void updateMandalSpinner() {
            List<String> mandalNames = new ArrayList<>();
            List<Integer> mandalIds = new ArrayList<>();
            mandalNames.add("Select All");
            mandalIds.add(0);

            if (mandalDataMap != null) {
                for (Map.Entry<String, Pair> entry : mandalDataMap.entrySet()) {
                    mandalNames.add(String.valueOf(entry.getValue().second));
                    mandalIds.add(Integer.parseInt(entry.getKey()));
                }
            }

            multiSelectionSpinner_mandal.setItems(mandalNames, mandalIds);
        }

        private void populateSpinnerData() {
            LinkedHashMap<String, Pair> stateDataMap = dataAccessHandler.getPairData(Queries.getInstance().getStatesMasterQuery());

            List<String> stateNames = new ArrayList<>();
            List<Integer> stateIds = new ArrayList<>();
            stateNames.add("Select All");
            stateIds.add(0);

            if (stateDataMap != null) {
                for (Map.Entry<String, Pair> entry : stateDataMap.entrySet()) {
                    stateNames.add(String.valueOf(entry.getValue().second));
                    stateIds.add(Integer.parseInt(entry.getKey()));
                }
            }

            multiSelectionSpinner_state.setItems(stateNames, stateIds);
        }
    }