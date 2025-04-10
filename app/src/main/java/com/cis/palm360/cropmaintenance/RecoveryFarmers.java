package com.cis.palm360.cropmaintenance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cis.palm360.R;
import com.cis.palm360.cloudhelper.ApplicationThread;
import com.cis.palm360.cloudhelper.Log;
import com.cis.palm360.common.CommonConstants;
import com.cis.palm360.common.CommonUtils;
import com.cis.palm360.database.DataAccessHandler;
import com.cis.palm360.database.DatabaseKeys;
import com.cis.palm360.database.Queries;
import com.cis.palm360.dbmodels.RecoveryFarmerModel;
import com.cis.palm360.dbmodels.RecoveryFarmerModelnew;
import com.cis.palm360.ui.OilPalmBaseActivity;
import com.cis.palm360.utils.UiUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class RecoveryFarmers extends OilPalmBaseActivity implements RecoveryFarmerAdapter.recoveryfarmerListListner {

    private DataAccessHandler dataAccessHandler;
    TextView mainfarmercode,mainfarmername;
    RecyclerView recoveryfarmerslist;
    LinearLayout mainlayout,addrecoveryfarmer1,palmArealyt;
    Button addfarmer,addnewfarmer,save;
    int addcount = 1;
    TextView recovery_village,recovery_mandal,recovery_district,recovery_state,recovery_palmarea;
    private RecoveryFarmerAdapter recoveryfarmerRecyclerAdapter;
    private AutoCompleteTextView autoCompletefarmer1;
    private List<RecoveryFarmerModel> recoveryfarmermodel = new ArrayList<>();
    private ArrayList<RecoveryFarmerModelnew> recoveryfarmers = new ArrayList<RecoveryFarmerModelnew>();
    LinearLayoutManager linearLayoutManager;
    RecoveryFarmerModelnew a;
    @Override
    public void Initialize() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View parentView = inflater.inflate(R.layout.activity_recovery_farmers, null);
        baseLayout.addView(parentView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        dataAccessHandler = new DataAccessHandler(RecoveryFarmers.this);
        initView();
        setViews();
        setTile("Farmers Recovery Screen");
    }

    public void initView(){

        mainfarmercode = findViewById(R.id.mainfarmercode);
        mainfarmername = findViewById(R.id.mainfarmername);
        mainlayout = findViewById(R.id.mainlayout);
        addrecoveryfarmer1 = findViewById(R.id.addrecoveryfarmer1);
        recoveryfarmerslist = findViewById(R.id.recoveryfarmerslist);
        addnewfarmer = findViewById(R.id.addnewfarmer);
        addfarmer = findViewById(R.id.addfarmer);
        recovery_village = findViewById(R.id.recovery_village);
        recovery_mandal=  findViewById(R.id.recovery_mandal);
        autoCompletefarmer1 = findViewById(R.id.autoCompletefarmer1);
        recovery_district=  findViewById(R.id.recovery_district);
        recovery_state=  findViewById(R.id.recovery_state);
        recovery_palmarea = findViewById(R.id.recovery_palmarea);
        palmArealyt = findViewById(R.id.palmArealyt);
        save = findViewById(R.id.save);
    }
    public void setViews(){


        Bundle extras = getIntent().getExtras();
        String userName = "";

        if (extras != null) {
            userName = extras.getString("fullname");
        }

        Log.d("Selectedfarmer", CommonConstants.FARMER_CODE +"");
        String selectedfarmercode = CommonConstants.FARMER_CODE;
        mainfarmercode.setText(" : " + selectedfarmercode);
        mainfarmername.setText(" : " + userName);

        recoveryfarmermodel = dataAccessHandler.getFarmerDetailsforRecovery(Queries.getInstance().getFarmersDataRecovery(selectedfarmercode));

        int count = 0;
        for (RecoveryFarmerModel recoveryfarmer : recoveryfarmermodel) {
            Log.d("Arun", "=====> Farmer Code :" + recoveryfarmer.getCode());
            count++;
        }
        CustomFarmerAdapter adapter = new CustomFarmerAdapter(RecoveryFarmers.this,
                R.layout.autocompletefarmeritem, recoveryfarmermodel);
        autoCompletefarmer1.setAdapter(adapter);
        autoCompletefarmer1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String fullname = "", middleName = "";

                if (!TextUtils.isEmpty(adapter.getItem(i).getMiddleName()) && !
                        adapter.getItem(i).getMiddleName().equalsIgnoreCase("null")) {
                    middleName = adapter.getItem(i).getMiddleName();
                }

                fullname  = adapter.getItem(i).getFirstName().trim() + " " + middleName + " " + adapter.getItem(i).getLastName().trim();

                autoCompletefarmer1.setText(adapter.getItem(i).getCode() + " (" + fullname + ")");
                recovery_village.setText("  :  " +adapter.getItem(i).getVillageName());
                recovery_mandal.setText("  :  " +adapter.getItem(i).getMandalName());
                recovery_district.setText("  :  " +adapter.getItem(i).getDistrictName());
                recovery_state.setText("  :  " +adapter.getItem(i).getStateName());
                recovery_palmarea.setText("  :  " +adapter.getItem(i).getPalmArea());

                a = new RecoveryFarmerModelnew(adapter.getItem(i).getCode(),fullname,adapter.getItem(i).getVillageName(),adapter.getItem(i).getMandalName(),
                        adapter.getItem(i).getDistrictName(),adapter.getItem(i).getStateName(),adapter.getItem(i).getPalmArea()
                );
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);

            }
        });

        addfarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addrecoveryfarmer1.setVisibility(View.VISIBLE);

            }
        });
        addnewfarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(a != null){
                    recoveryfarmers.add(a);
                    addrecoveryfarmer1.setVisibility(View.GONE);
                    autoCompletefarmer1.setText("");
                    recovery_village.setText("");
                    recovery_mandal.setText("");
                    recovery_district.setText("");
                    recovery_state.setText("");
                    recovery_palmarea.setText("");

                    recoveryfarmerslist.setVisibility(View.VISIBLE);

                    linearLayoutManager = new LinearLayoutManager(RecoveryFarmers.this);
                    recoveryfarmerslist.setLayoutManager(linearLayoutManager);
                    recoveryfarmerRecyclerAdapter = new RecoveryFarmerAdapter(RecoveryFarmers.this, recoveryfarmers,RecoveryFarmers.this);
                    recoveryfarmerslist.setAdapter(recoveryfarmerRecyclerAdapter);
                    Log.e("Size=========>",recoveryfarmers.size()+"");

                    if (recoveryfarmers.size() == 5) {
                        addfarmer.setVisibility(View.GONE);
                    }
                    a = null;

                }else{
                    UiUtils.showCustomToastMessage("Please select a Recovery Farmer", RecoveryFarmers.this, 1);
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> list = new ArrayList<String>();
                for (int i = 0; i < recoveryfarmers.size(); i++) {
                    list.add(recoveryfarmers.get(i).getCode());
                }
                Set<String> s = new HashSet<String>();

                if (recoveryfarmers.size() > 0) {
                    for(String name : list) {
                        if(s.add(name) == false){
                            System.out.println(name + "is duplicated");
                            UiUtils.showCustomToastMessage("Same recovery farmer cannot be selected", RecoveryFarmers.this, 1);
                            return;
                        }
                    }

                    List<LinkedHashMap> details = new ArrayList<>();

                    for (int i = 0; i < recoveryfarmers.size(); i++) {
                        LinkedHashMap map = new LinkedHashMap();
                        map.put("FarmerCode",selectedfarmercode);
                        map.put("RecoveryFarmerCode",recoveryfarmers.get(i).getCode());
                        map.put("CreatedByUserId",Integer.parseInt(CommonConstants.USER_ID));
                        map.put("CreatedDate", CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
                        map.put("UpdatedByUserId",Integer.parseInt(CommonConstants.USER_ID));
                        map.put("UpdatedDate",CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
                        map.put("IsActive",1);
                        map.put("ServerUpdatedStatus",0);
                        details.add(map);
                    }

                    dataAccessHandler.saveData(DatabaseKeys.TABLE_Recovery_Farmer_Group, details, new ApplicationThread.OnComplete<String>() {
                        @Override
                        public void execute(boolean success, String result, String msg) {

                            if (success) {
                                Log.d(HarvestingActivity.class.getSimpleName(), "==>  Analysis ==> TABLE_RecoveryFarmerGroup INSERT COMPLETED");
                                Toast.makeText(RecoveryFarmers.this, "Submit Successfully", Toast.LENGTH_SHORT).show();
                                //CommonConstants.ISReoveryFarmersubmitted = "Yes";
                                //int recoveryfarmerexists = 0;
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("result",result);
                                setResult(RecoveryFarmers.RESULT_OK,returnIntent);
                                finish();

                            } else {
                                Toast.makeText(RecoveryFarmers.this, "Submit Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    UiUtils.showCustomToastMessage("Recovery Farmers Added successfully", RecoveryFarmers.this, 0);
                    finish();
                }else{
                    UiUtils.showCustomToastMessage("Please Add atleast 1 Recovery Farmer", RecoveryFarmers.this, 1);
                }
            }
        });
    }

    @Override
    public void onItemDelete(int po) {
        recoveryfarmers.remove(po);
        if (recoveryfarmers.size() != 5){
            addfarmer.setVisibility(View.VISIBLE);
        }
        recoveryfarmerRecyclerAdapter.notifyDataSetChanged();
        recoveryfarmerRecyclerAdapter = new RecoveryFarmerAdapter(RecoveryFarmers.this, recoveryfarmers,RecoveryFarmers.this);
        recoveryfarmerslist.setAdapter(recoveryfarmerRecyclerAdapter);
    }

}