package com.cis.palm360.cropmaintenance;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.cis.palm360.R;
import com.cis.palm360.areaextension.UpdateUiListener;
import com.cis.palm360.cloudhelper.Log;
import com.cis.palm360.common.CommonConstants;
import com.cis.palm360.common.CommonUtils;
import com.cis.palm360.conversion.PalmDetailsEditListener;
import com.cis.palm360.database.DataAccessHandler;
import com.cis.palm360.database.DatabaseKeys;
import com.cis.palm360.database.Queries;
import com.cis.palm360.datasync.helpers.DataManager;
import com.cis.palm360.dbmodels.CropMaintenanceDocs;
import com.cis.palm360.dbmodels.MainPestModel;
import com.cis.palm360.dbmodels.Pest;
import com.cis.palm360.dbmodels.PestChemicalXref;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static com.cis.palm360.common.CommonUtils.spinnerSelect;
import static com.cis.palm360.cropmaintenance.CommonUtilsNavigation.adapterSetFromHashmap;
import static com.cis.palm360.cropmaintenance.CommonUtilsNavigation.getKey;


/**
 * A simple {@link Fragment} subclass.
 */

//Used to enter Pest releated details during Crop maintenance
public class PestDetailsFragment extends Fragment implements View.OnClickListener, PalmDetailsEditListener, UpdateUiListener, OnPageChangeListener, OnLoadCompleteListener {


    private View rootView;
    private Spinner pestNameSpin, nameOfChemicalUsedSpin, rcmnduomSpin, percentageOfTreeSpin, controlMeasureSpin;
    private EditText ObservationsEdt;
    private LinearLayout parentLayout;
    private Button saveBtn, historyBtn, pestpdfBtn;
    private RecyclerView pestDetailsList;
    private LinkedHashMap<String, String> pestNameDataMap, chemicalNameDataMap, percentageMap, recmChemicalMap, rcmnduomperDatamap;
    private DataAccessHandler dataAccessHandler;
    private ArrayList<Pest> mPestModelArray ;
    private ArrayList<PestChemicalXref> mPestChemicalXrefModelArray ;
    private ArrayList<MainPestModel> mainPestModelList;
    private GenericTypeAdapter pestDataAdapter;
    private ArrayList<Character> ratingList;
    private char prc_tree = ' ';
    private Context mContext;
    //    public static final String MAIN_PEST_DETAIL = "main_pest_detail";
    private UpdateUiListener updateUiListener;
    private static int PEST_DATA = 3;
    private Button complaintsBtn;
    private Spinner nameOfChemicalUsedSpinRecmnd, rcmnduomperSpin;
    private LinkedHashMap uomDataMap;
    private EditText rcmndosageEdt;
    Toolbar toolbar;
    private ActionBar actionBar;

    private ArrayList<Pest> pestlastvisitdatamap;
    private ArrayList<PestChemicalXref> pestchemicllastvisitdatamap;

    File fileToDownLoad;
    CropMaintenanceDocs cropMaintenanceDocs;


    public PestDetailsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_pest_details, container, false);

        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        actionBar = activity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().onBackPressed();
//            }
//        });
      //  actionBar.setTitle(getString(R.string.nutrient_details));

//        AppCompatActivity activity = (AppCompatActivity) getActivity();
//        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
//
//        //toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
//        AppCompatActivity activity = (AppCompatActivity) getActivity();
//        activity.setSupportActionBar(toolbar);
//        actionBar = activity.getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        ratingList = new ArrayList<>();

        actionBar.setTitle(getActivity().getResources().getString(R.string.pest_details));
        mContext = getActivity();
        initViews();
        setViews();
        //setHasOptionsMenu(true);
        dataAccessHandler = new DataAccessHandler(mContext);

        bindData();

        return rootView;
    }

    private void bindData() {
        // Retrieve data from DataManager
        mPestModelArray = (ArrayList<Pest>) DataManager.getInstance().getDataFromManager(DataManager.PEST_DETAILS);
        mainPestModelList = (ArrayList<MainPestModel>) DataManager.getInstance().getDataFromManager(DataManager.MAIN_PEST_DETAIL);
        mPestChemicalXrefModelArray = (ArrayList<PestChemicalXref>) DataManager.getInstance().getDataFromManager(DataManager.CHEMICAL_DETAILS);
        ratingList = (ArrayList<Character>) DataManager.getInstance().getDataFromManager("RATING_LIST_pest");

        // Null checks for each list and initialization if needed
        if (mPestModelArray == null) {
            mPestModelArray = new ArrayList<Pest>();
            Log.e("bindData", "mPestModelArray is null, initializing as empty list.");
        } else {
            Log.v("bindData", "mPestModelArray: " + mPestModelArray.toString());
        }

        if (mainPestModelList == null) {
            mainPestModelList = new ArrayList<MainPestModel>();
            Log.e("bindData", "mainPestModelList is null, initializing as empty list.");
        } else {
            Log.v("bindData", "mainPestModelList: " + mainPestModelList.toString());
        }

        if (mPestChemicalXrefModelArray == null) {
            mPestChemicalXrefModelArray = new ArrayList<PestChemicalXref>();
            Log.e("bindData", "mPestChemicalXrefModelArray is null, initializing as empty list.");
        } else {
            Log.v("bindData", "mPestChemicalXrefModelArray: " + mPestChemicalXrefModelArray.toString());
        }

        if (ratingList == null) {
            ratingList = new ArrayList<Character>();
            Log.e("bindData", "ratingList is null, initializing as empty list.");
        } else {
            Log.v("bindData", "ratingList: " + ratingList.toString());
        }

        // Log the final state of the lists
        Log.v("bindData", "Final mPestModelArray: " + mPestModelArray.size());
        Log.v("bindData", "Final mainPestModelList: " + mainPestModelList.size());
        Log.v("bindData", "Final mPestChemicalXrefModelArray: " + mPestChemicalXrefModelArray.size());
        Log.v("bindData", "Final ratingList: " + ratingList.size());

        // Set up the adapter and UI
        pestDataAdapter = new GenericTypeAdapter(getActivity(), mainPestModelList, pestNameDataMap, chemicalNameDataMap, percentageMap, uomDataMap, GenericTypeAdapter.TYPE_PEST);
        pestDetailsList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        pestDetailsList.setAdapter(pestDataAdapter);
        pestDataAdapter.setEditClickListener(this);
    }

//    private void bindData() {
//        mPestModelArray = (ArrayList<Pest>) DataManager.getInstance().getDataFromManager(DataManager.PEST_DETAILS);
//
//        if (mPestModelArray != null){
//
//        if (mPestModelArray.get(0).getPestid() == 223){
//            pestNameSpin.setSelection(CommonUtilsNavigation.getvalueFromHashMap(pestNameDataMap,mPestModelArray.get(0).getPestid()));
//        }
//        else{
//
//            mainPestModelList = (ArrayList<MainPestModel>) DataManager.getInstance().getDataFromManager(DataManager.MAIN_PEST_DETAIL);
//            if (mainPestModelList != null){
//                pestNameSpin.setSelection(CommonUtilsNavigation.getvalueFromHashMap(pestNameDataMap,mainPestModelList.get(0).getPest().getPestid()));
//                nameOfChemicalUsedSpin.setSelection(mainPestModelList.get(0).getmPestChemicalXref().getChemicalId() == null ? 0 :CommonUtilsNavigation.getvalueFromHashMap(chemicalNameDataMap,mainPestModelList.get(0).getmPestChemicalXref().getChemicalId()));
//                percentageOfTreeSpin.setSelection(mainPestModelList.get(0).getPest().getPercTreesId() == null ? 0 :CommonUtilsNavigation.getvalueFromHashMap(percentageMap,mainPestModelList.get(0).getPest().getPercTreesId()));
//                controlMeasureSpin.setSelection(mainPestModelList.get(0).getPest().getIsControlMeasure() == null ? 0 : mainPestModelList.get(0).getPest().getIsControlMeasure() == 1 ? 1 : 2);
//                nameOfChemicalUsedSpinRecmnd.setSelection(mainPestModelList.get(0).getPest().getRecommendFertilizerProviderId() == null ? 0 :CommonUtilsNavigation.getvalueFromHashMap(chemicalNameDataMap,mainPestModelList.get(0).getPest().getRecommendFertilizerProviderId()));
//
//                double recommendDosage = mainPestModelList.get(0).getPest().getRecommendDosage();
//                String valueFromMap;
//
//                if (recommendDosage == 0.0) {
//                    rcmndosageEdt.setText("");
//                } else {
//                    // Convert the double value to an integer for the map key
//                    int intValue = (int) recommendDosage;
//                    //valueFromMap = CommonUtilsNavigation.getStValueFromHashMap(chemicalNameDataMap, String.valueOf(intValue));
//                    rcmndosageEdt.setText(String.valueOf(intValue));
//                }
//
//
//
//                rcmnduomSpin.setSelection(mainPestModelList.get(0).getPest().getRecommendUOMId() == null ? 0 :CommonUtilsNavigation.getvalueFromHashMap(uomDataMap,mainPestModelList.get(0).getPest().getRecommendUOMId()));
//                rcmnduomperSpin.setSelection(mainPestModelList.get(0).getPest().getRecommendedUOMId() == null ? 0 :CommonUtilsNavigation.getvalueFromHashMap(rcmnduomperDatamap,mainPestModelList.get(0).getPest().getRecommendedUOMId()));
//                ObservationsEdt.setText(mainPestModelList.get(0).getPest().getComments() == null ? "" :mainPestModelList.get(0).getPest().getComments());
//
//            }
//        }
//
//
//        }
//
////        mainPestModelList = (ArrayList<MainPestModel>) DataManager.getInstance().getDataFromManager(DataManager.MAIN_PEST_DETAIL);
//////        if (null == mainPestModelList)
//////            mainPestModelList = new ArrayList<MainPestModel>();
//////
//////        pestDataAdapter = new GenericTypeAdapter(getActivity(), mainPestModelList, pestNameDataMap, chemicalNameDataMap, percentageMap, uomDataMap, GenericTypeAdapter.TYPE_PEST);
//////        pestDetailsList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//////        pestDetailsList.setAdapter(pestDataAdapter);
//////        pestDataAdapter.setEditClickListener(this);
////
////        if (mainPestModelList != null){
////            pestNameSpin.setSelection(CommonUtilsNavigation.getvalueFromHashMap(pestNameDataMap,mainPestModelList.get(0).getPest().getPestid()));
////            nameOfChemicalUsedSpin.setSelection(mainPestModelList.get(0).getmPestChemicalXref().getChemicalId() == null ? 0 :CommonUtilsNavigation.getvalueFromHashMap(chemicalNameDataMap,mainPestModelList.get(0).getmPestChemicalXref().getChemicalId()));
////            percentageOfTreeSpin.setSelection(mainPestModelList.get(0).getPest().getPercTreesId() == null ? 0 :CommonUtilsNavigation.getvalueFromHashMap(percentageMap,mainPestModelList.get(0).getPest().getPercTreesId()));
////            controlMeasureSpin.setSelection(mainPestModelList.get(0).getPest().getIsControlMeasure() == null ? 0 : mainPestModelList.get(0).getPest().getIsControlMeasure() == 1 ? 1 : 2);
////            nameOfChemicalUsedSpinRecmnd.setSelection(mainPestModelList.get(0).getPest().getRecommendFertilizerProviderId() == null ? 0 :CommonUtilsNavigation.getvalueFromHashMap(chemicalNameDataMap,mainPestModelList.get(0).getPest().getRecommendFertilizerProviderId()));
////
////            double recommendDosage = mainPestModelList.get(0).getPest().getRecommendDosage();
////            String valueFromMap;
////
////            if (recommendDosage == 0.0) {
////                rcmndosageEdt.setText("");
////            } else {
////                // Convert the double value to an integer for the map key
////                int intValue = (int) recommendDosage;
////                //valueFromMap = CommonUtilsNavigation.getStValueFromHashMap(chemicalNameDataMap, String.valueOf(intValue));
////                rcmndosageEdt.setText(String.valueOf(intValue));
////            }
////
////
////
////            rcmnduomSpin.setSelection(mainPestModelList.get(0).getPest().getRecommendUOMId() == null ? 0 :CommonUtilsNavigation.getvalueFromHashMap(uomDataMap,mainPestModelList.get(0).getPest().getRecommendUOMId()));
////            rcmnduomperSpin.setSelection(mainPestModelList.get(0).getPest().getRecommendedUOMId() == null ? 0 :CommonUtilsNavigation.getvalueFromHashMap(rcmnduomperDatamap,mainPestModelList.get(0).getPest().getRecommendedUOMId()));
////            ObservationsEdt.setText(mainPestModelList.get(0).getPest().getComments() == null ? "" :mainPestModelList.get(0).getPest().getComments());
////
////        }
//
//
//    }

    private void initViews() {
        dataAccessHandler = new DataAccessHandler(getActivity());
        pestNameSpin = (Spinner) rootView.findViewById(R.id.pestNameSpin);
        nameOfChemicalUsedSpin = (Spinner) rootView.findViewById(R.id.nameOfChemicalUsedSpin);
        nameOfChemicalUsedSpinRecmnd = (Spinner) rootView.findViewById(R.id.nameOfChemicalUsedSpinRecmnd);

        rcmnduomSpin = (Spinner) rootView.findViewById(R.id.rcmnduomSpin);
        percentageOfTreeSpin = (Spinner) rootView.findViewById(R.id.percentageOfTreeSpin);
        controlMeasureSpin = (Spinner) rootView.findViewById(R.id.controlMeasureSpin);
        rcmndosageEdt = (EditText) rootView.findViewById(R.id.rcmndosageEdt);
        ObservationsEdt = (EditText) rootView.findViewById(R.id.ObservationsEdt);
        saveBtn = (Button) rootView.findViewById(R.id.saveBtn);
        historyBtn = (Button) rootView.findViewById(R.id.historyBtn);
        pestpdfBtn = (Button) rootView.findViewById(R.id.pestpdfBtn);
        pestDetailsList = (RecyclerView) rootView.findViewById(R.id.pestDetailsList);
        parentLayout = (LinearLayout) rootView.findViewById(R.id.pestParentLayout);
        complaintsBtn = (Button) rootView.findViewById(R.id.complaintsBtn);
        complaintsBtn.setEnabled(false);
        complaintsBtn.setVisibility(View.GONE);

        cropMaintenanceDocs = (CropMaintenanceDocs) dataAccessHandler.getCMDocsData(Queries.getInstance().getPestPDFfile(), 0);

        if (cropMaintenanceDocs != null) {
            fileToDownLoad = new File(CommonUtils.get3FFileRootPath() + "3F_CMDocs/" + cropMaintenanceDocs.getFileName() + cropMaintenanceDocs.getFileExtension());


            if (null != fileToDownLoad && fileToDownLoad.exists()) {

                pestpdfBtn.setVisibility(View.VISIBLE);

            } else {
                pestpdfBtn.setVisibility(View.GONE);
            }
        }

      //  complaintsBtn.setVisibility((CommonUiUtils.isComplaintsDataEntered()) ? View.GONE : View.VISIBLE);
        rcmnduomperSpin = (Spinner) rootView.findViewById(R.id.rcmnduomperSpin);

        pestpdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPestPDFDialog(getContext());
            }
        });

        complaintsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle dataBundle = new Bundle();
                dataBundle.putBoolean(CommonConstants.KEY_NEW_COMPLAINT, true);
                ComplaintDetailsFragment complaintDetailsFragment = new ComplaintDetailsFragment();
                complaintDetailsFragment.setArguments(dataBundle);
                complaintDetailsFragment.setUpdateUiListener(PestDetailsFragment.this);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(android.R.id.content, complaintDetailsFragment).addToBackStack(null)
                        .commit();
            }
        });
    }

    public void showPestPDFDialog(Context activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.pdfdialog);

        Toolbar titleToolbar;
        titleToolbar = (Toolbar) dialog.findViewById(R.id.titleToolbar);
        titleToolbar.setTitle("Pest PDF");
        titleToolbar.setTitleTextColor(getResources().getColor(R.color.white));

        PDFView fertpfdview;

        fertpfdview = dialog.findViewById(R.id.fertpdfview);

        fertpfdview.fromFile(fileToDownLoad)
                .defaultPage(0)
                .enableAnnotationRendering(true)
                .onPageChange(this)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(getActivity()))
                .load();

        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 500);
    }

    private void setViews() {

        parentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CommonUtilsNavigation.hideKeyBoard(getActivity());
                return false;
            }
        });
        saveBtn.setOnClickListener(this);
        //historyBtn.setOnClickListener(this);
        nameOfChemicalUsedSpin.setOnItemSelectedListener(spinListener);
        nameOfChemicalUsedSpinRecmnd.setOnItemSelectedListener(spinListener);
        pestNameSpin.setOnItemSelectedListener(spinListener);
        pestNameDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getLookUpData("6"));
        chemicalNameDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getLookUpData("7"));
        percentageMap = dataAccessHandler.getGenericData(Queries.getInstance().getLookUpData("570"));


        pestNameSpin.setAdapter(adapterSetFromHashmap(getActivity(), "Pest Name", pestNameDataMap));
        nameOfChemicalUsedSpin.setAdapter(adapterSetFromHashmap(getActivity(), "Name of Chemical", chemicalNameDataMap));
        percentageOfTreeSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Percentage of tree", percentageMap));

        nameOfChemicalUsedSpinRecmnd.setAdapter(adapterSetFromHashmap(getActivity(), "Name of Chemical", chemicalNameDataMap));
        uomDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getUOM());
        rcmnduomSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "UOM", uomDataMap));

        rcmnduomperDatamap = dataAccessHandler.getGenericData(Queries.getInstance().getUOMper());
        rcmnduomperSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "UOM Per", rcmnduomperDatamap));

        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(getContext());
            }
        });

    }

    public void showDialog(Context activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.pestlastvisteddata);

        Toolbar titleToolbar;
        titleToolbar = (Toolbar) dialog.findViewById(R.id.titleToolbar);
        titleToolbar.setTitle("Pest History");
        titleToolbar.setTitleTextColor(getResources().getColor(R.color.white));

        LinearLayout mainLL = (LinearLayout) dialog.findViewById(R.id.pestmainlyt);
        RecyclerView recyclerView = dialog.findViewById(R.id.pestRecyclerView);

        TextView norecords = (TextView) dialog.findViewById(R.id.pestnorecord_tv);

        String lastVisitCode = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getLatestCropMaintanaceHistoryCode(CommonConstants.PLOT_CODE));
        pestlastvisitdatamap = (ArrayList<Pest>) dataAccessHandler.getPestData(Queries.getInstance().getRecommndCropMaintenanceHistoryData(lastVisitCode, DatabaseKeys.TABLE_PEST), 1);
        pestchemicllastvisitdatamap = (ArrayList<PestChemicalXref>) dataAccessHandler.getPestChemicalXrefData(Queries.getInstance().getPestchemical(lastVisitCode), 1);
        int pesticideusedid = 0;


        if (pestlastvisitdatamap.size() > 0){
            norecords.setVisibility(View.GONE);
            mainLL.setVisibility(View.VISIBLE);
            PestHistoryAdapter adapter = new PestHistoryAdapter(activity, pestlastvisitdatamap,pestchemicllastvisitdatamap);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
            recyclerView.setAdapter(adapter);
            String pestname = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getlookupdata(pestlastvisitdatamap.get(0).getPestid()));

//
//            pestsseen.setText(pestname);
//            Log.d("pestname", pestname + "");
//            Log.d("pestid", pestlastvisitdatamap.get(0).getPestid() + "");
//
//            if (pestlastvisitdatamap.get(0).getPestid() != 223){
//                pesticideusedid = dataAccessHandler.getOnlyOneIntValueFromDb(Queries.getInstance().getPesticideId(lastVisitCode));
//                Log.d("PesticideUsedId", pesticideusedid + "");
//                pestchemicalusedll.setVisibility(View.VISIBLE);
//                String chemicalused = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getlookupdata(pesticideusedid));
//                Log.d("PesticideUsedIdName", chemicalused + "");
//                pestchemicalused.setText(chemicalused);
//            }else{
//                pestchemicalusedll.setVisibility(View.GONE);
//            }
//
//            if (pestlastvisitdatamap.get(0).getPercTreesId() != 0){
//                pestpercentageoftreesll.setVisibility(View.VISIBLE);
//                String percentageoftress = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getlookupdata(pestlastvisitdatamap.get(0).getPercTreesId()));
//                pestpercentageoftrees.setText(percentageoftress);
//            }else{
//                pestpercentageoftreesll.setVisibility(View.GONE);
//            }
//
//            if (pestlastvisitdatamap.get(0).getRecommendFertilizerProviderId() != null){
//                pestrecommendedchemicall.setVisibility(View.VISIBLE);
//                String fertilizerrec = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getlookupdata(pestlastvisitdatamap.get(0).getRecommendFertilizerProviderId()));
//                pestrecommendedchemical.setText(fertilizerrec);
//            }else{
//                pestrecommendedchemicall.setVisibility(View.GONE);
//            }
//
//            if (pestlastvisitdatamap.get(0).getRecommendDosage() != 0.0){
//                pestrecommendeddosagell.setVisibility(View.VISIBLE);
//                pestrecommendeddosage.setText(pestlastvisitdatamap.get(0).getRecommendDosage() + "");
//            }else{
//                pestrecommendeddosagell.setVisibility(View.GONE);
//            }
//
//            if (pestlastvisitdatamap.get(0).getRecommendUOMId() != null){
//                pestrecommendeduomll.setVisibility(View.VISIBLE);
//                String recommendedUOM = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getUOMdata(pestlastvisitdatamap.get(0).getRecommendUOMId()));
//                pestrecommendeduom.setText(recommendedUOM);
//            }else{
//                pestrecommendeduomll.setVisibility(View.GONE);
//            }
//
//            if (pestlastvisitdatamap.get(0).getRecommendedUOMId() != null){
//                pestrecommenduomperll.setVisibility(View.VISIBLE);
//                String recommendedUOMper = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getUOMdata(pestlastvisitdatamap.get(0).getRecommendedUOMId()));
//                pestrecommenduomper.setText(recommendedUOMper);
//            }else{
//                pestrecommenduomperll.setVisibility(View.GONE);
//            }
//
////            if (!(TextUtils.isEmpty(pestlastvisitdatamap.get(0).getComments()))){
////                pestcommentsll.setVisibility(View.VISIBLE);
////                pestcomments.setText(pestlastvisitdatamap.get(0).getComments() + "");
////            }else{
////                pestcomments.setVisibility(View.GONE);
////            }
//
//            if (TextUtils.isEmpty(pestlastvisitdatamap.get(0).getComments()) || pestlastvisitdatamap.get(0).getComments().equalsIgnoreCase("")  || pestlastvisitdatamap.get(0).getComments().equalsIgnoreCase(null) || pestlastvisitdatamap.get(0).getComments().equalsIgnoreCase("null")){
//
//                pestcomments.setVisibility(View.GONE);
//            }else{
//                pestcommentsll.setVisibility(View.VISIBLE);
//               pestcomments.setText(pestlastvisitdatamap.get(0).getComments() + "");
//            }

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
    public void addingValues() {
        if (CommonConstants.perc_tree_pest == ' ') {
            if ((percentageOfTreeSpin.getSelectedItem().toString().equals("No Deficiency")) && (controlMeasureSpin.getSelectedItemPosition() == 1)) {
                CommonConstants.perc_tree_pest = 'A';
                ratingList.add('A');
            } else if ((percentageOfTreeSpin.getSelectedItem().toString().equals("Less than 5%")) && (controlMeasureSpin.getSelectedItemPosition() == 1)) {
                CommonConstants.perc_tree_pest = 'A';
                ratingList.add('A');
            } else if (percentageOfTreeSpin.getSelectedItem().toString().equals("5 to 25%") && (controlMeasureSpin.getSelectedItemPosition() == 1)) {
                CommonConstants.perc_tree_pest = 'B';
                ratingList.add('B');
            } else {
                CommonConstants.perc_tree_pest = 'C';
                ratingList.add('C');
            }
            Log.v("@@@pestRatingEmpty", "" + CommonConstants.perc_tree_pest);
        } else {
            if ((percentageOfTreeSpin.getSelectedItem().toString().equals("No Deficiency")) && (controlMeasureSpin.getSelectedItemPosition() == 1)) {
                ratingList.add('A');
            } else if (((percentageOfTreeSpin.getSelectedItem().toString()).equals("Less than 5%")) && (controlMeasureSpin.getSelectedItemPosition() == 1)) {
                ratingList.add('A');
            } else if ((percentageOfTreeSpin.getSelectedItem().toString().equals("5 to 25%")) && (controlMeasureSpin.getSelectedItemPosition() == 1)) {
                prc_tree = 'B';
                if (prc_tree > CommonConstants.perc_tree_pest) {
                    ratingList.add('B');
                    CommonConstants.perc_tree_pest = prc_tree;
                } else {
                    ratingList.add('B');
                    CommonConstants.perc_tree_pest = 'C';
                }
            } else {
                CommonConstants.perc_tree_pest = 'C';
                ratingList.add('C');
            }
        }

        // Store ratingList in DataManager for persistence
        DataManager.getInstance().addData("RATING_LIST_pest", ratingList);

        Log.v("@@@pestRating", "" + CommonConstants.perc_tree_pest);
        Log.v("@@@ratingList", "Saved rating list: " + ratingList.toString());
    }


//    public void addingValues() {
//
//        if (CommonConstants.perc_tree_pest == ' ') {
//
//
//            if ((percentageOfTreeSpin.getSelectedItem().toString().equals("No Deficiency")) && (controlMeasureSpin.getSelectedItemPosition() == 1)) {
//                CommonConstants.perc_tree_pest = 'A';
//                ratingList.add('A');
//
//
//            } else if ((percentageOfTreeSpin.getSelectedItem().toString().equals("Less than 5%")) && (controlMeasureSpin.getSelectedItemPosition() == 1)) {
//                CommonConstants.perc_tree_pest = 'A';
//                ratingList.add('A');
//
//
//            } else if (percentageOfTreeSpin.getSelectedItem().toString().equals("5 to 25%") && (controlMeasureSpin.getSelectedItemPosition() == 1)) {
//                CommonConstants.perc_tree_pest = 'B';
//                ratingList.add('B');
//
//            } else {
//                CommonConstants.perc_tree_pest = 'C';
//                ratingList.add('C');
//
//            }
//            Log.v("@@@pestRatingEmpty", "" + CommonConstants.perc_tree_pest);
//        } else {
//            if ((percentageOfTreeSpin.getSelectedItem().toString().equals("No Deficiency")) && (controlMeasureSpin.getSelectedItemPosition() == 1)) {
//                ratingList.add('A');
//            } else if (((percentageOfTreeSpin.getSelectedItem().toString()).equals("Less than 5%")) && (controlMeasureSpin.getSelectedItemPosition() == 1)) {
//                ratingList.add('A');
//
//
//            } else if ((percentageOfTreeSpin.getSelectedItem().toString().equals("5 to 25%")) && (controlMeasureSpin.getSelectedItemPosition() == 1)) {
//                prc_tree = 'B';
//                if (prc_tree > CommonConstants.perc_tree_pest) {
//                    ratingList.add('B');
//
//                    CommonConstants.perc_tree_pest = prc_tree;
//                } else {
//                    ratingList.add('B');
//                    CommonConstants.perc_tree_pest = 'C';
//                }
//            } else {
//                CommonConstants.perc_tree_pest = 'C';
//                ratingList.add('C');
//            }
//        }
//
//
//        Log.v("@@@pestRating", "" + CommonConstants.perc_tree_pest);
//
//
//    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveBtn:

                String selectedPestName = pestNameSpin.getSelectedItem().toString();

                // Loop through existing records to check for duplicates
                boolean isDuplicate = false;
                for (MainPestModel existingModel : mainPestModelList) {
                    if (existingModel.getPest().getPestid() == Integer.parseInt(getKey(pestNameDataMap, selectedPestName))) {
                        isDuplicate = true;
                        break;
                    }
                }

                if (isDuplicate) {
                    // Show error message
                    Toast.makeText(getContext(), "Selected Pest already added. Please select a different Pest", Toast.LENGTH_SHORT).show();
                    return;
                }

                MainPestModel mainPestModel = new MainPestModel();
                Pest mPestModel = new Pest();
                PestChemicalXref mPestChemicalXref = new PestChemicalXref();

                if (pestNameSpin.getSelectedItem().toString().equals("No Pest Deficiency")) {

                    if (validateUI1()) {
                        mPestModel.setPestid(pestNameSpin.getSelectedItemPosition() == 0 ? 0 : Integer.parseInt(getKey(pestNameDataMap, pestNameSpin.getSelectedItem().toString())));
                        mPestModel.setComments(ObservationsEdt.getText().toString());
                        mPestModel.setCreateddate(CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
                        //  mPestModel.setIsControlMeasure(controlMeasureSpin.getSelectedItemPosition() == 1 ? 1 : 0);

                        mainPestModel.setPest(mPestModel);

                        mPestModel.setCreateddate(CommonUtils.getcurrentDateTime(CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS)));

                        mPestModelArray.add(mPestModel);
                        mainPestModelList.add(mainPestModel);

                        DataManager.getInstance().addData(DataManager.PEST_DETAILS, mPestModelArray);
                        DataManager.getInstance().addData(DataManager.MAIN_PEST_DETAIL, mainPestModelList);
                        ratingList.add('A');
                        DataManager.getInstance().addData("RATING_LIST_pest", ratingList);
                        pestNameSpin.setSelection(0);
                        controlMeasureSpin.setSelection(0);
                        pestDataAdapter.notifyDataSetChanged();
                        updateUiListener.updateUserInterface(0);
                    }
                } else {
                    if (validateUI()) {

                        mPestModel.setPestid(pestNameSpin.getSelectedItemPosition() == 0 ? 0 : Integer.parseInt(getKey(pestNameDataMap, pestNameSpin.getSelectedItem().toString())));
                        mPestModel.setComments(ObservationsEdt.getText().toString());
                        mPestModel.setCreateddate(CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
                        mPestModel.setRecommendFertilizerProviderId(nameOfChemicalUsedSpinRecmnd.getSelectedItemPosition() == 0 ? null : Integer.parseInt(getKey(chemicalNameDataMap, nameOfChemicalUsedSpinRecmnd.getSelectedItem().toString())));
                        mPestModel.setRecommendUOMId(rcmnduomSpin.getSelectedItemPosition() == 0 ? null : Integer.parseInt(getKey(uomDataMap, rcmnduomSpin.getSelectedItem().toString())));
                        mPestModel.setRecommendedUOMId(rcmnduomperSpin.getSelectedItemPosition() == 0 ? null : Integer.parseInt(getKey(rcmnduomperDatamap, rcmnduomperSpin.getSelectedItem().toString())));

                        // mPestModel.setRecommendDosage(Double.parseDouble(rcmndosageEdt.getText().toString()));
                        mPestModel.setRecommendDosage(TextUtils.isEmpty(rcmndosageEdt.getText().toString()) == true ? 0.0 : Double.parseDouble(rcmndosageEdt.getText().toString()));
                        mPestModel.setPercTreesId(Integer.parseInt(getKey(percentageMap, percentageOfTreeSpin.getSelectedItem().toString())));
                        mPestModel.setIsControlMeasure(controlMeasureSpin.getSelectedItemPosition() == 1 ? 1 : 0);

                        mainPestModel.setPest(mPestModel);

                        mPestModel.setCreateddate(CommonUtils.getcurrentDateTime(CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS)));
                        mPestModelArray.add(mPestModel);
                        mPestChemicalXref.setChemicalId(nameOfChemicalUsedSpin.getSelectedItemPosition() == 0 ? 0 : Integer.parseInt(getKey(chemicalNameDataMap, nameOfChemicalUsedSpin.getSelectedItem().toString())));

                        mainPestModel.setmPestChemicalXref(mPestChemicalXref);
                        mainPestModelList.add(mainPestModel);
                        mPestChemicalXrefModelArray.add(mPestChemicalXref);
                        DataManager.getInstance().addData(DataManager.PEST_DETAILS, mPestModelArray);
                        DataManager.getInstance().addData(DataManager.CHEMICAL_DETAILS, mPestChemicalXrefModelArray);
                        DataManager.getInstance().addData(DataManager.MAIN_PEST_DETAIL, mainPestModelList);
                        addingValues();

                        pestNameSpin.setSelection(0);
                        nameOfChemicalUsedSpinRecmnd.setSelection(0);
                        percentageOfTreeSpin.setSelection(0);
                        controlMeasureSpin.setSelection(0);
                        rcmnduomSpin.setSelection(0);
                        rcmnduomperSpin.setSelection(0);
                        rcmndosageEdt.setText("");
                        pestDataAdapter.notifyDataSetChanged();
                        updateUiListener.updateUserInterface(0);

                    }
                }
                CommonUtilsNavigation.hideKeyBoard(getActivity());
                break;
//            case R.id.historyBtn:
//                CropMaintainanceHistoryFragment newFragment = new CropMaintainanceHistoryFragment();
//                Bundle bundle = new Bundle();
//                bundle.putInt("screen", PEST_DATA);
//                newFragment.setArguments(bundle);
//                newFragment.show(getActivity().getFragmentManager(), "history");
//                break;
        }
    }
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.saveBtn:
//
//                mainPestModelList = new ArrayList<MainPestModel>();
//                MainPestModel mainPestModel = new MainPestModel();
//                Pest mPestModel = new Pest();
//                PestChemicalXref mPestChemicalXref = new PestChemicalXref();
//                mPestModelArray = new ArrayList<Pest>();
//                mPestChemicalXrefModelArray = new ArrayList<>();
//
//                if (pestNameSpin.getSelectedItem().toString().equals("No Pest Deficiency")) {
//
//                    if (validateUI1()) {
//                        mPestModel.setPestid(pestNameSpin.getSelectedItemPosition() == 0 ? 0 : Integer.parseInt(getKey(pestNameDataMap, pestNameSpin.getSelectedItem().toString())));
//                        mPestModel.setComments(ObservationsEdt.getText().toString());
//                        mPestModel.setCreateddate(CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
//                      //  mPestModel.setIsControlMeasure(controlMeasureSpin.getSelectedItemPosition() == 1 ? 1 : 0);
//                        mainPestModel.setPest(mPestModel);
//                        mPestModel.setCreateddate(CommonUtils.getcurrentDateTime(CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS)));
//                        mainPestModelList.clear();
//                        mPestChemicalXrefModelArray.clear();
//                        DataManager.getInstance().deleteData(DataManager.CHEMICAL_DETAILS);
//                        mPestModelArray.clear();
//                        mPestModelArray.add(mPestModel);
//                        mainPestModelList.add(mainPestModel);
//                        DataManager.getInstance().addData(DataManager.PEST_DETAILS, mPestModelArray);
//                        ratingList.add('A');
//                        getFragmentManager().popBackStack();
//
//                        pestNameSpin.setSelection(0);
//                        controlMeasureSpin.setSelection(0);
//                        //pestDataAdapter.notifyDataSetChanged();
//                        updateUiListener.updateUserInterface(0);
//                    }
//                } else {
//                    if (validateUI()) {
//
//                        mPestModelArray.clear();
//                        mPestChemicalXrefModelArray.clear();
//                        mainPestModelList.clear();
//
//                        mPestModel.setPestid(pestNameSpin.getSelectedItemPosition() == 0 ? 0 : Integer.parseInt(getKey(pestNameDataMap, pestNameSpin.getSelectedItem().toString())));
//                        mPestModel.setComments(ObservationsEdt.getText().toString());
//                        mPestModel.setCreateddate(CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
//                        mPestModel.setRecommendFertilizerProviderId(nameOfChemicalUsedSpinRecmnd.getSelectedItemPosition() == 0 ? null : Integer.parseInt(getKey(chemicalNameDataMap, nameOfChemicalUsedSpinRecmnd.getSelectedItem().toString())));
//                        mPestModel.setRecommendUOMId(rcmnduomSpin.getSelectedItemPosition() == 0 ? null : Integer.parseInt(getKey(uomDataMap, rcmnduomSpin.getSelectedItem().toString())));
//                        mPestModel.setRecommendedUOMId(rcmnduomperSpin.getSelectedItemPosition() == 0 ? null : Integer.parseInt(getKey(rcmnduomperDatamap, rcmnduomperSpin.getSelectedItem().toString())));
//
//                        // mPestModel.setRecommendDosage(Double.parseDouble(rcmndosageEdt.getText().toString()));
//                        mPestModel.setRecommendDosage(TextUtils.isEmpty(rcmndosageEdt.getText().toString()) == true ? 0.0 : Double.parseDouble(rcmndosageEdt.getText().toString()));
//                        mPestModel.setPercTreesId(Integer.parseInt(getKey(percentageMap, percentageOfTreeSpin.getSelectedItem().toString())));
//                        mPestModel.setIsControlMeasure(controlMeasureSpin.getSelectedItemPosition() == 1 ? 1 : 0);
//
//                        mainPestModel.setPest(mPestModel);
//
//                        mPestModel.setCreateddate(CommonUtils.getcurrentDateTime(CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS)));
//                        mPestModelArray.add(mPestModel);
//                        mPestChemicalXref.setChemicalId(nameOfChemicalUsedSpin.getSelectedItemPosition() == 0 ? 0 : Integer.parseInt(getKey(chemicalNameDataMap, nameOfChemicalUsedSpin.getSelectedItem().toString())));
//                        mainPestModel.setmPestChemicalXref(mPestChemicalXref);
//                        mainPestModelList.add(mainPestModel);
//                        mPestChemicalXrefModelArray.add(mPestChemicalXref);
//                        DataManager.getInstance().addData(DataManager.PEST_DETAILS, mPestModelArray);
//                        DataManager.getInstance().addData(DataManager.CHEMICAL_DETAILS, mPestChemicalXrefModelArray);
//                        DataManager.getInstance().addData(DataManager.MAIN_PEST_DETAIL, mainPestModelList);
//                        addingValues();
//                        getFragmentManager().popBackStack();
//                        pestNameSpin.setSelection(0);
//                        nameOfChemicalUsedSpinRecmnd.setSelection(0);
//                        percentageOfTreeSpin.setSelection(0);
//                        controlMeasureSpin.setSelection(0);
//                        rcmnduomSpin.setSelection(0);
//                        rcmnduomperSpin.setSelection(0);
//                        rcmndosageEdt.setText("");
//                        //pestDataAdapter.notifyDataSetChanged();
//                        updateUiListener.updateUserInterface(0);
//
//                    }
//                }
//                CommonUtilsNavigation.hideKeyBoard(getActivity());
//                break;
////            case R.id.historyBtn:
////                CropMaintainanceHistoryFragment newFragment = new CropMaintainanceHistoryFragment();
////                Bundle bundle = new Bundle();
////                bundle.putInt("screen", PEST_DATA);
////                newFragment.setArguments(bundle);
////                newFragment.show(getActivity().getFragmentManager(), "history");
////                break;
//        }
//    }

    AdapterView.OnItemSelectedListener spinListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId())

            {

                case R.id.pestNameSpin:
                    if (position == 0) {
                        nameOfChemicalUsedSpin.setSelection(0);
                        nameOfChemicalUsedSpin.setEnabled(false);
                        percentageOfTreeSpin.setSelection(0);
                        percentageOfTreeSpin.setEnabled(false);
                        nameOfChemicalUsedSpinRecmnd.setSelection(0);
                        nameOfChemicalUsedSpinRecmnd.setEnabled(false);
                        controlMeasureSpin.setSelection(0);
                        controlMeasureSpin.setEnabled(false);
                        rcmndosageEdt.setEnabled(false);
                        rcmnduomSpin.setSelection(0);
                        rcmnduomSpin.setEnabled(false);
                        rcmnduomperSpin.setSelection(0);
                        rcmnduomperSpin.setEnabled(false);
                        ObservationsEdt.setEnabled(false);
                        saveBtn.setEnabled(false);
                        saveBtn.setAlpha(0.5f);
                        rcmndosageEdt.setText("");
                        ObservationsEdt.setText("");
                    } else if (position == 13) {

                        nameOfChemicalUsedSpin.setSelection(0);
                        nameOfChemicalUsedSpin.setEnabled(false);
                        percentageOfTreeSpin.setSelection(0);
                        percentageOfTreeSpin.setEnabled(false);
                        nameOfChemicalUsedSpinRecmnd.setSelection(0);
                        nameOfChemicalUsedSpinRecmnd.setEnabled(false);
                        controlMeasureSpin.setSelection(0);
                        controlMeasureSpin.setEnabled(false);
                        rcmndosageEdt.setEnabled(false);
                        rcmnduomSpin.setSelection(0);
                        rcmnduomSpin.setEnabled(false);
                        rcmnduomperSpin.setSelection(0);
                        rcmnduomperSpin.setEnabled(false);
                        ObservationsEdt.setEnabled(true);
                        rcmndosageEdt.setText("");
                        ObservationsEdt.setText("");
                        saveBtn.setEnabled(true);
                        saveBtn.setAlpha(1.0f);


                    } else {
                        nameOfChemicalUsedSpin.setEnabled(true);
                        nameOfChemicalUsedSpinRecmnd.setEnabled(true);
                        controlMeasureSpin.setEnabled(true);
                        percentageOfTreeSpin.setEnabled(true);
                        rcmnduomSpin.setEnabled(true);
                        rcmnduomperSpin.setEnabled(true);
                        ObservationsEdt.setEnabled(true);
                        rcmndosageEdt.setEnabled(true);
                        saveBtn.setEnabled(true);
                        saveBtn.setAlpha(1.0f);
                    }
                    break;
            }


        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private boolean validateUI() {
        return spinnerSelect(pestNameSpin, "Source of Pest", mContext)
                && spinnerSelect(nameOfChemicalUsedSpin, "Name of Pesticide Used", mContext)
                && spinnerSelect(percentageOfTreeSpin, "Percentage of Tree", mContext) && spinnerSelect(controlMeasureSpin, "Control Measure", mContext);

    }

    private boolean validateUI1() {
        return spinnerSelect(pestNameSpin, "Source of Pest", mContext);

    }

    public void onEditClicked(int position) {

        // Safety check for position validity
        if (position < 0) {
            Log.e("onEditClicked", "Invalid position: " + position);
            return;
        }

        // Remove the item from mainPestModelList
        if (mainPestModelList != null && position < mainPestModelList.size()) {
            mainPestModelList.remove(position);
        } else {
            Log.w("onEditClicked", "mainPestModelList is null or position out of bounds");
        }

        // Remove the item from ratingList
        if (ratingList != null && position < ratingList.size()) {
            ratingList.remove(position);
        } else {
            Log.w("onEditClicked", "ratingList is null or position out of bounds");
        }

        // Handle mPestModelArray
        if (mPestModelArray != null && position < mPestModelArray.size()) {
            String pestname = dataAccessHandler.getOnlyOneValueFromDb(
                    Queries.getInstance().getlookupdata(mPestModelArray.get(position).getPestid())
            );

            Log.d("pestname====>880", "Pest Name: " + pestname);

            // Check if the pest name is "Ganoderma"
            if (pestname != null && pestname.equalsIgnoreCase("Ganoderma")) {
                Log.d("PestCheck", "Ganoderma (pestId == 349) found. Deleting related data...");
                DataManager.getInstance().deleteData(DataManager.GANODERMA_DETAILS);
            }

            mPestModelArray.remove(position);
        } else {
            Log.w("onEditClicked", "mPestModelArray is null or position out of bounds");
        }

        // Handle mPestChemicalXrefModelArray
        if (mPestChemicalXrefModelArray != null && position < mPestChemicalXrefModelArray.size()) {
            mPestChemicalXrefModelArray.remove(position);
        } else {
            Log.w("onEditClicked", "mPestChemicalXrefModelArray is null or position out of bounds");
        }

        // Log current list sizes for debugging
        Log.v("onEditClicked", "List Sizes -> mainPestModelList: " + mainPestModelList.size()
                + ", mPestModelArray: " + (mPestModelArray != null ? mPestModelArray.size() : "null")
                + ", mPestChemicalXrefModelArray: " + (mPestChemicalXrefModelArray != null ? mPestChemicalXrefModelArray.size() : "null")
                + ", ratingList: " + ratingList.size());

        // Update CommonConstants.perc_tree_pest
        if (mPestModelArray == null || mPestModelArray.isEmpty()) {
            CommonConstants.perc_tree_pest = ' ';
        } else if (mPestModelArray.size() == 1) {
            CommonConstants.perc_tree_pest = ratingList.get(0);
        } else if (mPestModelArray.size() > 1) {
            // Find the maximum value in the ratingList
            int maxRating = Integer.MIN_VALUE; // Default minimum value
            for (int i = 0; i < ratingList.size(); i++) {
                if (ratingList.get(i) > maxRating) {
                    CommonConstants.perc_tree_pest  = ratingList.get(i);
                }
            }
          //  CommonConstants.perc_tree_pest = maxRating;
        }

        // Refresh UI
        if (pestDataAdapter != null) {
            pestDataAdapter.notifyDataSetChanged();
        } else {
            Log.w("onEditClicked", "pestDataAdapter is null, cannot notify changes");
        }

        if (updateUiListener != null) {
            updateUiListener.updateUserInterface(0);
        } else {
            Log.w("onEditClicked", "updateUiListener is null");
        }
    }
//    @Override
//    public void onEditClicked(int position) {
//
//        // Ensure the pest ID and name are checked before removing the item
//        if (null != mPestModelArray && mPestModelArray.size() > position) {
//            String pestname = dataAccessHandler.getOnlyOneValueFromDb(
//                    Queries.getInstance().getlookupdata(mPestModelArray.get(position).getPestid())
//            );
//
//            Log.d("pestname====>880", pestname);
//
//            // Check if the pest name is "Ganoderma"
//            if (pestname != null && pestname.equalsIgnoreCase("Ganoderma")) {
//                Log.d("PestCheck", "Ganoderma (pestId == 349) found. Deleting related data...");
//                DataManager.getInstance().deleteData(DataManager.GANODERMA_DETAILS);
//            }
//
//            // Remove the pest from the mPestModelArray
//            mPestModelArray.remove(position);
//            mainPestModelList.remove(position);
//        }
//
//        // Remove the main pest model
//        if (mainPestModelList.size() > position) {
//            mainPestModelList.remove(position);
//        }
//
//        // Remove from the rating list
//        if (ratingList.size() > position) {
//            ratingList.remove(position);
//        }
//
//        // Remove from the chemical xref model array
//        if (null != mPestChemicalXrefModelArray && mPestChemicalXrefModelArray.size() > position) {
//            mPestChemicalXrefModelArray.remove(position);
//        }
//
//        Log.v("mainpestModel", "pestMain   " + mainPestModelList.size()
//                + " pest " + mPestModelArray.size()
//                + " chemical  " + mPestChemicalXrefModelArray.size()
//                + " rating  " + ratingList.size());
//
//        // Update CommonConstants.perc_tree_pest based on the size of mPestModelArray
//        if (mPestModelArray.isEmpty()) {
//            CommonConstants.perc_tree_pest = ' ';
//        } else if (mPestModelArray.size() == 1) {
//            CommonConstants.perc_tree_pest = ratingList.get(0);
//        } else if (mPestModelArray.size() > 1) {
//            for (int i = 0; i < ratingList.size() - 1; i++) {
//                for (int j = i + 1; j < ratingList.size(); j++) {
//                    if (ratingList.get(i) > ratingList.get(j)) {
//                        CommonConstants.perc_tree_pest = ratingList.get(i);
//                    } else {
//                        CommonConstants.perc_tree_pest = ratingList.get(j);
//                    }
//                }
//            }
//        }
//
//        // Notify the adapter about data changes
//        pestDataAdapter.notifyDataSetChanged();
//        updateUiListener.updateUserInterface(0);
//    }


    public void setUpdateUiListener(UpdateUiListener updateUiListener) {
        this.updateUiListener = updateUiListener;
    }

    @Override
    public void updateUserInterface(int refreshPosition) {
        complaintsBtn.setVisibility(View.GONE);
    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }

    /**
     * Created by Calibrage11 on 7/22/2017.
     */


}
