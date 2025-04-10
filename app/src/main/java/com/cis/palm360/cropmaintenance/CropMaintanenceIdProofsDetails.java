package com.cis.palm360.cropmaintenance;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cis.palm360.R;
import com.cis.palm360.areaextension.UpdateUiListener;
import com.cis.palm360.common.CommonConstants;
import com.cis.palm360.database.DataAccessHandler;
import com.cis.palm360.database.Queries;
import com.cis.palm360.dbmodels.IdentityProof;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.cis.palm360.cropmaintenance.CommonUtilsNavigation.getvalueFromHashMap;

/**
 * Created by latitude on 15-07-2017.
 */

//Displays Id Proofs of the selected farmer
public class CropMaintanenceIdProofsDetails extends Fragment {

    private TextView idCardDetails;
    private View rootView;
    private Context mContext;
    private DataAccessHandler dataAccessHandler;
    private UpdateUiListener updateUiListener;
    private ArrayList<IdentityProof> mIdentityProofList = new ArrayList<>();
    private LinkedHashMap<String, String> idProofsData;
    private ActionBar actionBar;

    public CropMaintanenceIdProofsDetails() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.crop_maintanence_id_proofs_details, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        actionBar = activity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle("ID Proof Details");

        initViews();

        return rootView;
    }



    private void initViews() {
        dataAccessHandler = new DataAccessHandler(getActivity());
        idCardDetails = (TextView) rootView.findViewById(R.id.idCardDetails);

        mIdentityProofList = (ArrayList<IdentityProof>) dataAccessHandler.getSelectedIdProofsData(Queries.getInstance().getFarmerIdentityProof(CommonConstants.FARMER_CODE), 1);
        if (null != mIdentityProofList) {
            bindData();
        }
    }

    private void bindData() {
        idProofsData =  dataAccessHandler.getGenericData(Queries.getInstance().getTypeCdDmtData("12"));
        List<String> list = new ArrayList<String>(idProofsData.values());
        for (int i = 0; i < mIdentityProofList.size(); i++){
            idCardDetails.append("\n"+list.get(getvalueFromHashMap(idProofsData,mIdentityProofList.get(i).getIdprooftypeid())-1) +":   "+ mIdentityProofList.get(i).getIdproofnumber());
        }

    }


}
