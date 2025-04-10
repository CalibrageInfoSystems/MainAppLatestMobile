package com.cis.palm360.cropmaintenance;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cis.palm360.R;
import com.cis.palm360.areaextension.UpdateUiListener;
import com.cis.palm360.common.CommonConstants;
import com.cis.palm360.conversion.PalmDetailsEditListener;
import com.cis.palm360.database.DataAccessHandler;
import com.cis.palm360.database.DatabaseKeys;
import com.cis.palm360.database.Queries;
import com.cis.palm360.datasync.helpers.DataManager;
import com.cis.palm360.dbmodels.Fertilizer;
import com.cis.palm360.dbmodels.Nutrient;
import com.cis.palm360.dbmodels.RecommndFertilizer;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static com.cis.palm360.common.CommonUtils.edittextEampty;
import static com.cis.palm360.common.CommonUtils.spinnerSelect;
import static com.cis.palm360.cropmaintenance.CommonUtilsNavigation.getKey;


/**
 * A simple {@link Fragment} subclass.
 */

//Used to display recommended fertilizers
//public class RecomndFertilizerFragment extends Fragment implements View.OnClickListener, PalmDetailsEditListener, UpdateUiListener {
////    private RecomNDScreen.OnFragmentInteractionListener mListener;
//    private Context mContext;
//    private View rootView;
//    private Toolbar toolbarrecomnd;
//    private ActionBar actionBar;
//    private Spinner rcmndfertilizerProductNameSpin,rcmnduomSpin;
//    private EditText rcmndosageEdt;
//    private EditText rcmndedtcmment;
//    private RecyclerView rcmndsaveList;
//    private Button rcmndsave,historyBtn;
//    private DataAccessHandler dataAccessHandler;
//    private LinkedHashMap<String, String> fertilizerDataMap, fertilizerTypeDataMap, uomDataMap, frequencyOfApplicationDataMap;
//    private ArrayList mFertilizerModelArray;
//    private RecmndGenericTypeAdapter fertilizerDataAdapter;
//    private Fertilizer  mFertilizerModel;
//    private Fertilizer mFertilizerModel1;
//    private ArrayList<Nutrient> mmNutrientModelModelArray;
//    private String screenrecmnd;
//    private String value;
//    private UpdateUiListener updateUiListener;
//
//    private ArrayList<RecommndFertilizer> recommendfertilizerlastvisitdatamap;
//
//    public RecomndFertilizerFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        rootView=inflater.inflate(R.layout.fragment_recomnd_fertilizert, container, false);
//        AppCompatActivity activity = (AppCompatActivity) getActivity();
//        toolbarrecomnd = (Toolbar) rootView.findViewById(R.id.toolbar);
//
//        activity.setSupportActionBar(toolbarrecomnd);
//        actionBar = activity.getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//
//        actionBar.setTitle("Recommended Fertilizer");
//
//        mContext = getActivity();
//        setHasOptionsMenu(true);
//        initViews();
//        setViews();
//        bindData();
//
//        return rootView;
//    }
//
//
//
//
//    private void initViews() {
//        dataAccessHandler = new DataAccessHandler(getActivity());
//
//        rcmndfertilizerProductNameSpin = (Spinner) rootView.findViewById(R.id.rcmndfertilizerProductNameSpin);
//        rcmnduomSpin = (Spinner) rootView.findViewById(R.id.rcmnduomSpin);
//        rcmndosageEdt=(EditText)rootView.findViewById(R.id.rcmndosageEdt);
//        rcmndedtcmment=(EditText)rootView.findViewById(R.id.rcmndedtcmment);
//        rcmndsaveList=(RecyclerView)rootView.findViewById(R.id.rcmndsaveList);
//        rcmndsave=(Button)rootView.findViewById(R.id.rcmndsave);
//        historyBtn = (Button) rootView.findViewById(R.id.historyBtn);
//
//    }
//    private void setViews() {
//        rcmndsave.setOnClickListener(this);
//        historyBtn.setOnClickListener(this);
//
//
//        fertilizerTypeDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getLookUpData("23"));
//        rcmndfertilizerProductNameSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Fertilizer Product Name", fertilizerTypeDataMap));
//        uomDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getUOM());
//        rcmnduomSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "UOM", uomDataMap));
//
//
//    }
//    private void bindData() {
//
//
//            mFertilizerModelArray = (ArrayList<RecommndFertilizer>) DataManager.getInstance().getDataFromManager(DataManager.RECMND_FERTILIZER);
//        if (null == mFertilizerModelArray)
//            mFertilizerModelArray = new ArrayList<RecommndFertilizer>();
//
//        fertilizerDataAdapter = new RecmndGenericTypeAdapter(getActivity(), mFertilizerModelArray, fertilizerTypeDataMap, uomDataMap, GenericTypeAdapter.TYPE_RECOM_FERTILIZER);
//
//        rcmndsaveList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//        rcmndsaveList.setAdapter(fertilizerDataAdapter);
//        fertilizerDataAdapter.setEditClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.rcmndsave:
//
//                if (validateUI()) {
//                    RecommndFertilizer mFertilizerModel=new RecommndFertilizer();
//                        mFertilizerModel.setRecommendFertilizerProviderId((Integer.parseInt(getKey(fertilizerTypeDataMap, rcmndfertilizerProductNameSpin.getSelectedItem().toString()))));
//                        mFertilizerModel.setRecommendUOMId(Integer.parseInt(getKey(uomDataMap, rcmnduomSpin.getSelectedItem().toString())));
//                        mFertilizerModel.setRecommendDosage(TextUtils.isEmpty(rcmndosageEdt.getText().toString())==true? 0.0:Double.parseDouble(rcmndosageEdt.getText().toString()));
//
//                        mFertilizerModel.setComments(rcmndedtcmment.getText().toString());
//                        mFertilizerModelArray.clear();
//                        mFertilizerModelArray.add(mFertilizerModel);
//                        DataManager.getInstance().addData(DataManager.RECMND_FERTILIZER, mFertilizerModelArray);
//
//                    clearFields();
//                    fertilizerDataAdapter.notifyDataSetChanged();
//
//                }
//                CommonUtilsNavigation.hideKeyBoard(getActivity());
//                break;
//
//            case R.id.historyBtn:
////                CropMaintainanceHistoryFragment newFragment = new CropMaintainanceHistoryFragment();
////                Bundle bundle = new Bundle();
////                bundle.putInt("screen", RECOM_FERTILIZER_DATA);
////                newFragment.setArguments(bundle);
////                newFragment.show(getActivity().getFragmentManager(), "history");
//
//                showDialog(getContext());
//                break;
//        }
//    }
//
//    public void showDialog(Context activity) {
//        final Dialog dialog = new Dialog(activity);
//        dialog.setCancelable(true);
//        dialog.setContentView(R.layout.recommendedlastvisteddata);
//
//        Toolbar titleToolbar;
//        titleToolbar = (Toolbar) dialog.findViewById(R.id.titleToolbar);
//        titleToolbar.setTitle("Recommended Fertilizer History");
//        titleToolbar.setTitleTextColor(getResources().getColor(R.color.white));
//
//        LinearLayout commentsll = (LinearLayout) dialog.findViewById(R.id.rfcommentsll);
//        LinearLayout mainLL = (LinearLayout) dialog.findViewById(R.id.rfmainlyt);
//
//        TextView recommendedfertilizer_text = (TextView) dialog.findViewById(R.id.recommendedfertilizer_text);
//        TextView recommendeduom_text = (TextView) dialog.findViewById(R.id.recommendeduom_text);
//        TextView recommendeddosage_text = (TextView) dialog.findViewById(R.id.recommendeddosage_text);
//        TextView comments_text = (TextView) dialog.findViewById(R.id.comments_text);
//
//        TextView norecords = (TextView) dialog.findViewById(R.id.rfnorecord_tv);
//
//        String lastVisitCode = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getLatestCropMaintanaceHistoryCode(CommonConstants.PLOT_CODE));
//        recommendfertilizerlastvisitdatamap = (ArrayList<RecommndFertilizer>) dataAccessHandler.getRecomFertlizerData(Queries.getInstance().getRecommndCropMaintenanceHistoryData(lastVisitCode, DatabaseKeys.TABLE_RECOMMND_FERTLIZER), 1);
//
//        if (recommendfertilizerlastvisitdatamap.size() > 0){
//            norecords.setVisibility(View.GONE);
//            mainLL.setVisibility(View.VISIBLE);
//
//            String fertilizer = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getlookupdata(recommendfertilizerlastvisitdatamap.get(0).getRecommendFertilizerProviderId()));
//            String uom = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getUOMdata(recommendfertilizerlastvisitdatamap.get(0).getRecommendUOMId()));
//
//            recommendedfertilizer_text.setText(fertilizer);
//            recommendeduom_text.setText(uom);
//            recommendeddosage_text.setText(recommendfertilizerlastvisitdatamap.get(0).getRecommendDosage() + "");
//
//            if (!TextUtils.isEmpty(recommendfertilizerlastvisitdatamap.get(0).getComments())){
//                commentsll.setVisibility(View.VISIBLE);
//                comments_text.setText(recommendfertilizerlastvisitdatamap.get(0).getComments());
//            }else{
//                commentsll.setVisibility(View.GONE);
//            }
//
//
//
//
//
//
//        }else{
//            mainLL.setVisibility(View.GONE);
//            norecords.setVisibility(View.VISIBLE);
//        }
//
//
//
//        dialog.show();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, 500);
//    }
//
//    @Override
//    public void updateUserInterface(int refreshPosition) {
//
//    }
//    private void clearFields() {
//        rcmndfertilizerProductNameSpin.setSelection(0);
//
//        rcmnduomSpin.setSelection(0);
//        rcmndosageEdt.setText("");
//        rcmndedtcmment.setText("");
//    }
//    @Override
//    public void onEditClicked(int position) {
//
//            mFertilizerModelArray.remove(position);
//
//
//
//        fertilizerDataAdapter.notifyDataSetChanged();
//    }
//    private boolean validateUI() {
//        return spinnerSelect(rcmndfertilizerProductNameSpin, "Fertilizer Product Name", mContext)
//                 && edittextEampty(rcmndosageEdt, "Dosage given", mContext) && spinnerSelect(rcmnduomSpin, "UOM", mContext);
//
//    }
//
//    public void setUpdateUiListener(UpdateUiListener updateUiListener) {
//        this.updateUiListener = updateUiListener;
//    }
//}

public class RecomndFertilizerFragment extends Fragment implements View.OnClickListener, PalmDetailsEditListener, UpdateUiListener {
    //    private RecomNDScreen.OnFragmentInteractionListener mListener;
    private Context mContext;
    private View rootView;
    private Toolbar toolbarrecomnd;
    private ActionBar actionBar;
    private Spinner rcmndfertilizerProductNameSpin,rcmnduomSpin;
    private EditText rcmndosageEdt;
    private EditText rcmndedtcmment;
    private RecyclerView rcmndsaveList;
    private Button rcmndsave,historyBtn;
    private DataAccessHandler dataAccessHandler;
    private LinkedHashMap<String, String> fertilizerDataMap, fertilizerTypeDataMap, uomDataMap, frequencyOfApplicationDataMap;
    //  private ArrayList mFertilizerModelArray;
    private RecmndGenericTypeAdapter fertilizerDataAdapter;
    private Fertilizer  mFertilizerModel;
    private Fertilizer mFertilizerModel1;
    private ArrayList<RecommndFertilizer> mFertilizerModelArray;
    private ArrayList<Nutrient> mmNutrientModelModelArray;
    private String screenrecmnd;
    private String value;
    private UpdateUiListener updateUiListener;

    private ArrayList<RecommndFertilizer> recommendfertilizerlastvisitdatamap;

    public RecomndFertilizerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_recomnd_fertilizert, container, false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        toolbarrecomnd = (Toolbar) rootView.findViewById(R.id.toolbar);

        activity.setSupportActionBar(toolbarrecomnd);
        actionBar = activity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle("Recommended Fertilizer");

        mContext = getActivity();
        setHasOptionsMenu(true);
        initViews();
        setViews();
        bindData();

        return rootView;
    }




    private void initViews() {
        dataAccessHandler = new DataAccessHandler(getActivity());

        rcmndfertilizerProductNameSpin = (Spinner) rootView.findViewById(R.id.rcmndfertilizerProductNameSpin);
        rcmnduomSpin = (Spinner) rootView.findViewById(R.id.rcmnduomSpin);
        rcmndosageEdt=(EditText)rootView.findViewById(R.id.rcmndosageEdt);
        rcmndedtcmment=(EditText)rootView.findViewById(R.id.rcmndedtcmment);
        rcmndsaveList=(RecyclerView)rootView.findViewById(R.id.rcmndsaveList);
        rcmndsave=(Button)rootView.findViewById(R.id.rcmndsave);
        historyBtn = (Button) rootView.findViewById(R.id.historyBtn);

    }
    private void setViews() {
        rcmndsave.setOnClickListener(this);
        historyBtn.setOnClickListener(this);


        fertilizerTypeDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getLookUpData("23"));
        rcmndfertilizerProductNameSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Fertilizer Product Name", fertilizerTypeDataMap));
        uomDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getUOM());
        rcmnduomSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "UOM", uomDataMap));


    }
    private void bindData() {


        mFertilizerModelArray = (ArrayList<RecommndFertilizer>) DataManager.getInstance().getDataFromManager(DataManager.RECMND_FERTILIZER);
        if ( mFertilizerModelArray != null){
            rcmndfertilizerProductNameSpin.setSelection(mFertilizerModelArray.get(0).getRecommendFertilizerProviderId() == null ? 0 :CommonUtilsNavigation.getvalueFromHashMap(fertilizerTypeDataMap,mFertilizerModelArray.get(0).getRecommendFertilizerProviderId()));
            rcmnduomSpin.setSelection(mFertilizerModelArray.get(0).getRecommendUOMId() == null ? 0 :CommonUtilsNavigation.getvalueFromHashMap(uomDataMap,mFertilizerModelArray.get(0).getRecommendUOMId() ));
//            rcmndfertilizerProductNameSpin.setSelection(mNutrientModelArray.get(0).getRecommendFertilizerProviderId() == null ? 0 :CommonUtilsNavigation.getvalueFromHashMap(fertilizerTypeDataMap,mNutrientModelArray.get(0).getRecommendFertilizerProviderId()));
            //       rcmndosageEdt.setText(mFertilizerModelArray.get(0).getRecommendDosage() == 0.0 ? "" :CommonUtilsNavigation.getvalueFromHashMap(uomDataMap,mFertilizerModelArray.get(0).getRecommendDosage()));

            double recommendDosage = mFertilizerModelArray.get(0).getRecommendDosage();

//
            if (recommendDosage == 0.0) {
                rcmndosageEdt.setText("");
            } else {
                // Convert the double value to an integer for the map key
                int intValue = (int) recommendDosage;
                //valueFromMap = CommonUtilsNavigation.getStValueFromHashMap(chemicalNameDataMap, String.valueOf(intValue));
                rcmndosageEdt.setText(String.valueOf(intValue));
            }
//
//
//
//            rcmnduomSpin.setSelection(mNutrientModelArray.get(0).getRecommendUOMId() == null ? 0 :CommonUtilsNavigation.getvalueFromHashMap(uomDataMap,mNutrientModelArray.get(0).getRecommendUOMId()));
//            rcmnduomperSpin.setSelection(mNutrientModelArray.get(0).getRecommendedUOMId() == null ? 0 :CommonUtilsNavigation.getvalueFromHashMap(rcmnduomperDatamap,mNutrientModelArray.get(0).getRecommendedUOMId()));
            rcmndedtcmment.setText(mFertilizerModelArray.get(0).getComments() == null ? "" :mFertilizerModelArray.get(0).getComments());

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rcmndsave:
                mFertilizerModelArray = new ArrayList<>();
                if (validateUI()) {
                    RecommndFertilizer mFertilizerModel=new RecommndFertilizer();
                    mFertilizerModel.setRecommendFertilizerProviderId((Integer.parseInt(getKey(fertilizerTypeDataMap, rcmndfertilizerProductNameSpin.getSelectedItem().toString()))));
                    mFertilizerModel.setRecommendUOMId(Integer.parseInt(getKey(uomDataMap, rcmnduomSpin.getSelectedItem().toString())));
                    mFertilizerModel.setRecommendDosage(TextUtils.isEmpty(rcmndosageEdt.getText().toString())==true? 0.0:Double.parseDouble(rcmndosageEdt.getText().toString()));

                    mFertilizerModel.setComments(rcmndedtcmment.getText().toString());
                    mFertilizerModelArray.clear();
                    mFertilizerModelArray.add(mFertilizerModel);
                    DataManager.getInstance().addData(DataManager.RECMND_FERTILIZER, mFertilizerModelArray);
                    getFragmentManager().popBackStack();
                    clearFields();
                    //     fertilizerDataAdapter.notifyDataSetChanged();

                }
                CommonUtilsNavigation.hideKeyBoard(getActivity());
                break;

            case R.id.historyBtn:
//                CropMaintainanceHistoryFragment newFragment = new CropMaintainanceHistoryFragment();
//                Bundle bundle = new Bundle();
//                bundle.putInt("screen", RECOM_FERTILIZER_DATA);
//                newFragment.setArguments(bundle);
//                newFragment.show(getActivity().getFragmentManager(), "history");

                showDialog(getContext());
                break;
        }
    }

    public void showDialog(Context activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.recommendedlastvisteddata);

        Toolbar titleToolbar;
        titleToolbar = (Toolbar) dialog.findViewById(R.id.titleToolbar);
        titleToolbar.setTitle("Recommended Fertilizer History");
        titleToolbar.setTitleTextColor(getResources().getColor(R.color.white));

        LinearLayout commentsll = (LinearLayout) dialog.findViewById(R.id.rfcommentsll);
        LinearLayout mainLL = (LinearLayout) dialog.findViewById(R.id.rfmainlyt);

        TextView recommendedfertilizer_text = (TextView) dialog.findViewById(R.id.recommendedfertilizer_text);
        TextView recommendeduom_text = (TextView) dialog.findViewById(R.id.recommendeduom_text);
        TextView recommendeddosage_text = (TextView) dialog.findViewById(R.id.recommendeddosage_text);
        TextView comments_text = (TextView) dialog.findViewById(R.id.comments_text);

        TextView norecords = (TextView) dialog.findViewById(R.id.rfnorecord_tv);

        String lastVisitCode = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getLatestCropMaintanaceHistoryCode(CommonConstants.PLOT_CODE));
        recommendfertilizerlastvisitdatamap = (ArrayList<RecommndFertilizer>) dataAccessHandler.getRecomFertlizerData(Queries.getInstance().getRecommndCropMaintenanceHistoryData(lastVisitCode, DatabaseKeys.TABLE_RECOMMND_FERTLIZER), 1);

        if (recommendfertilizerlastvisitdatamap.size() > 0){
            norecords.setVisibility(View.GONE);
            mainLL.setVisibility(View.VISIBLE);

            String fertilizer = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getlookupdata(recommendfertilizerlastvisitdatamap.get(0).getRecommendFertilizerProviderId()));
            String uom = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getUOMdata(recommendfertilizerlastvisitdatamap.get(0).getRecommendUOMId()));

            recommendedfertilizer_text.setText(fertilizer);
            recommendeduom_text.setText(uom);
            recommendeddosage_text.setText(recommendfertilizerlastvisitdatamap.get(0).getRecommendDosage() + "");

            if (!TextUtils.isEmpty(recommendfertilizerlastvisitdatamap.get(0).getComments())){
                commentsll.setVisibility(View.VISIBLE);
                comments_text.setText(recommendfertilizerlastvisitdatamap.get(0).getComments());
            }else{
                commentsll.setVisibility(View.GONE);
            }






        }else{
            mainLL.setVisibility(View.GONE);
            norecords.setVisibility(View.VISIBLE);
        }



        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 500);
    }

    @Override
    public void updateUserInterface(int refreshPosition) {

    }
    private void clearFields() {
        rcmndfertilizerProductNameSpin.setSelection(0);

        rcmnduomSpin.setSelection(0);
        rcmndosageEdt.setText("");
        rcmndedtcmment.setText("");
    }
    @Override
    public void onEditClicked(int position) {

        mFertilizerModelArray.remove(position);



        fertilizerDataAdapter.notifyDataSetChanged();
    }
    private boolean validateUI() {
        return spinnerSelect(rcmndfertilizerProductNameSpin, "Fertilizer Product Name", mContext)
                && edittextEampty(rcmndosageEdt, "Dosage given", mContext) && spinnerSelect(rcmnduomSpin, "UOM", mContext);

    }

    public void setUpdateUiListener(UpdateUiListener updateUiListener) {
        this.updateUiListener = updateUiListener;
    }
}

