package com.cis.palm360.palmcare;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cis.palm360.R;
import com.cis.palm360.common.CommonConstants;
import com.cis.palm360.farmersearch.SearchFarmerScreen;

import static com.cis.palm360.common.CommonUiUtils.resetPrevRegData;

public class palmcareScreen extends AppCompatActivity {
    private LinearLayout docropMaintenanceRel,closecropMaintenanceRel,doharvestingRel,closeharvestingRel,notvisitedplotsRel;
    private Toolbar toolbar;
    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palmcare_screen);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Palm Care");
        }
        this.docropMaintenanceRel = (LinearLayout) findViewById(R.id.docropMaintenanceRel);

        this.closecropMaintenanceRel = (LinearLayout) findViewById(R.id.closecropMaintenanceRel);

        this.closeharvestingRel = (LinearLayout) findViewById(R.id.closeharvestingRel);
        this.doharvestingRel = (LinearLayout) findViewById(R.id.doharvestingRel);
        this.notvisitedplotsRel = (LinearLayout) findViewById(R.id.notvisitedplotsRel);

        docropMaintenanceRel.setOnClickListener(view -> {
            resetPrevRegData();
            CommonConstants.REGISTRATION_SCREEN_FROM = CommonConstants.REGISTRATION_SCREEN_FROM_CP_MAINTENANCE;
            startActivity(new Intent(palmcareScreen.this, SearchFarmerScreen.class));
        });

        doharvestingRel.setOnClickListener(view -> {
            resetPrevRegData();
            CommonConstants.REGISTRATION_SCREEN_FROM = CommonConstants.REGISTRATION_SCREEN_FROM_HARVESTING;
            startActivity(new Intent(palmcareScreen.this, SearchFarmerScreen.class));
        });
        closecropMaintenanceRel.setOnClickListener(view -> {
            resetPrevRegData();

            startActivity(new Intent(palmcareScreen.this, ClosecropMaintenanceList.class));
        });

        closeharvestingRel.setOnClickListener(view -> {
            resetPrevRegData();

            startActivity(new Intent(palmcareScreen.this, CloseHarvestingList.class));
        });
        notvisitedplotsRel.setOnClickListener(view -> {
            resetPrevRegData();
            startActivity(new Intent(palmcareScreen.this, NoVisitPlotslistScreen.class));
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
}