package com.cis.palm360.cropmaintenance;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cis.palm360.R;
import com.cis.palm360.cloudhelper.Log;
import com.cis.palm360.common.CommonConstants;
import com.cis.palm360.database.DataAccessHandler;
import com.cis.palm360.database.Queries;
import com.cis.palm360.dbmodels.ViewRecoveryFarmerModel;
import com.cis.palm360.ui.OilPalmBaseActivity;

import java.util.ArrayList;

public class ViewRecoveryFarmers extends OilPalmBaseActivity {

    RecyclerView viewrecoveryfarmer_rcv;
    LinearLayoutManager linearLayoutManager;
    private DataAccessHandler dataAccessHandler;
    private ViewRecoveryFarmerAdapter viewrecoveryfarmerRecyclerAdapter;
    private ArrayList<ViewRecoveryFarmerModel> viewrecoveryfarmermodel = new ArrayList<>();


    @Override
    public void Initialize() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View parentView = inflater.inflate(R.layout.activity_view_recovery_farmers, null);
        baseLayout.addView(parentView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        dataAccessHandler = new DataAccessHandler(ViewRecoveryFarmers.this);
        initView();
        setViews();
        setTile("View Recovery Farmers");
    }

    public void initView(){
        viewrecoveryfarmer_rcv = findViewById(R.id.viewrecoveryfarmer_rcv);
    }
    public void setViews(){

        String selectedfarmercode = CommonConstants.FARMER_CODE;

        viewrecoveryfarmermodel = dataAccessHandler.getRecoveryFarmersofMainFarmer(Queries.getInstance().getRecoveryFarmersofMainFarmer(selectedfarmercode));
        Log.d("viewrecoveryfarmermodel", viewrecoveryfarmermodel.size() + "");

        linearLayoutManager = new LinearLayoutManager(ViewRecoveryFarmers.this);
        viewrecoveryfarmer_rcv.setLayoutManager(linearLayoutManager);
        viewrecoveryfarmerRecyclerAdapter = new ViewRecoveryFarmerAdapter(ViewRecoveryFarmers.this, viewrecoveryfarmermodel);
        viewrecoveryfarmer_rcv.setAdapter(viewrecoveryfarmerRecyclerAdapter);
    }
}