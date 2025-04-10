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
import com.cis.palm360.dbmodels.Disease;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Locale;

import static com.cis.palm360.common.CommonUtils.spinnerSelect;
import static com.cis.palm360.cropmaintenance.CommonUtilsNavigation.adapterSetFromHashmap;
import static com.cis.palm360.cropmaintenance.CommonUtilsNavigation.getKey;


/**
 * A simple {@link Fragment} subclass.
 */

//Used to diseases found while crop maintenance visit
public class DiseaseDetailsFragment extends Fragment implements View.OnClickListener, PalmDetailsEditListener, UpdateUiListener, OnPageChangeListener, OnLoadCompleteListener {
    private static int DISEASE_DATA = 4;
    ArrayList<Disease> mDiseaseModelArray = new ArrayList<>();
    private View rootView;
    private Spinner diseaseNameSpin, nameOfChemicalUsedSpin,percOfTreeSpin,controlMeasureSpin;
    private EditText observationsEdt;
    private LinearLayout parentLayout;
    private LinearLayout headerLL;
    private RecyclerView diaseaseList;
    private ArrayList<Character> ratingList;
    private char prc_tree = ' ' ;

    private ArrayList<Disease> diseaselastvisitdatamap;


    private Button saveBtn, historyBtn, diseasepdfBtn;
    private Button complaintsBtn;
    private Spinner nameOfChemicalUsedSpinRecmnd, rcmnduomperSpin;
    private LinkedHashMap uomDataMap, rcmnduomperDatamap;
    private EditText rcmndosageEdt;
    AdapterView.OnItemSelectedListener spinListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId())
            {

                case R.id.diseaseNameSpin:
                    if (position == 0) {
                        nameOfChemicalUsedSpin.setSelection(0);
                        nameOfChemicalUsedSpin.setEnabled(false);
                        percOfTreeSpin.setSelection(0);
                        percOfTreeSpin.setEnabled(false);
                        controlMeasureSpin.setSelection(0);
                        controlMeasureSpin.setEnabled(false);
                        nameOfChemicalUsedSpinRecmnd.setSelection(0);
                        nameOfChemicalUsedSpinRecmnd.setEnabled(false);
                        rcmndosageEdt.setEnabled(false);
                        rcmnduomSpin.setSelection(0);
                        rcmnduomSpin.setEnabled(false);
                        rcmnduomperSpin.setSelection(0);
                        rcmnduomperSpin.setEnabled(false);
                        observationsEdt.setEnabled(false);
                        observationsEdt.setText("");
                        rcmndosageEdt.setText("");
                        saveBtn.setEnabled(false);
                        saveBtn.setAlpha(0.5f);
                    }else if(position == 14){
                        nameOfChemicalUsedSpin.setSelection(0);
                        nameOfChemicalUsedSpin.setEnabled(false);
                        percOfTreeSpin.setSelection(0);
                        percOfTreeSpin.setEnabled(false);
                        controlMeasureSpin.setSelection(0);
                        controlMeasureSpin.setEnabled(false);
                        nameOfChemicalUsedSpinRecmnd.setSelection(0);
                        nameOfChemicalUsedSpinRecmnd.setEnabled(false);
                        rcmndosageEdt.setEnabled(false);
                        rcmnduomSpin.setSelection(0);
                        rcmnduomSpin.setEnabled(false);
                        rcmnduomperSpin.setSelection(0);
                        rcmnduomperSpin.setEnabled(false);
                        observationsEdt.setEnabled(true);
                        observationsEdt.setText("");
                        rcmndosageEdt.setText("");
                        saveBtn.setEnabled(true);
                        saveBtn.setAlpha(1.0f);

                    }
                    else {
                        nameOfChemicalUsedSpin.setEnabled(true);
                        nameOfChemicalUsedSpinRecmnd.setEnabled(true);
                        controlMeasureSpin.setEnabled(true);
                        rcmndosageEdt.setEnabled(true);
                        percOfTreeSpin.setEnabled(true);
                        rcmnduomSpin.setEnabled(true);
                        rcmnduomperSpin.setEnabled(true);
                        observationsEdt.setEnabled(true);
                        saveBtn.setEnabled(true);
                        saveBtn.setAlpha(1.0f);
                    }

                    break;
                case R.id.percOfTreeSpin:


                    break;
            }
//            {
//
//                case R.id.diseaseNameSpin:
//                    if (position == 0) {
//                        nameOfChemicalUsedSpin.setSelection(0);
//                        nameOfChemicalUsedSpin.setEnabled(false);
//                        percOfTreeSpin.setSelection(0);
//                        percOfTreeSpin.setEnabled(false);
//                        controlMeasureSpin.setSelection(0);
//                        controlMeasureSpin.setEnabled(false);
//                        nameOfChemicalUsedSpinRecmnd.setSelection(0);
//                        nameOfChemicalUsedSpinRecmnd.setEnabled(false);
//                        rcmndosageEdt.setEnabled(false);
//                        rcmnduomSpin.setSelection(0);
//                        rcmnduomSpin.setEnabled(false);
//                        rcmnduomperSpin.setSelection(0);
//                        rcmnduomperSpin.setEnabled(false);
//                        observationsEdt.setEnabled(false);
//                        saveBtn.setEnabled(false);
//                        saveBtn.setAlpha(0.5f);
//                    }else if(position == 14){
//                        nameOfChemicalUsedSpin.setSelection(0);
//                        nameOfChemicalUsedSpin.setEnabled(false);
//                        percOfTreeSpin.setSelection(0);
//                        percOfTreeSpin.setEnabled(false);
//                        controlMeasureSpin.setSelection(0);
//                        controlMeasureSpin.setEnabled(false);
//                        nameOfChemicalUsedSpinRecmnd.setSelection(0);
//                        nameOfChemicalUsedSpinRecmnd.setEnabled(false);
//                        rcmndosageEdt.setEnabled(false);
//                        rcmnduomSpin.setSelection(0);
//                        rcmnduomSpin.setEnabled(false);
//                        rcmnduomperSpin.setSelection(0);
//                        rcmnduomperSpin.setEnabled(false);
//                        observationsEdt.setEnabled(true);
//                        saveBtn.setEnabled(true);
//                        saveBtn.setAlpha(1.0f);
//
//                    }
//                    else {
//                        nameOfChemicalUsedSpin.setEnabled(true);
//                        nameOfChemicalUsedSpinRecmnd.setEnabled(true);
//                        controlMeasureSpin.setEnabled(true);
//                        rcmndosageEdt.setEnabled(true);
//                        percOfTreeSpin.setEnabled(true);
//                        rcmnduomSpin.setEnabled(true);
//                        rcmnduomperSpin.setEnabled(true);
//                        observationsEdt.setEnabled(true);
//                        saveBtn.setEnabled(true);
//                        saveBtn.setAlpha(1.0f);
//                    }
//
//                    break;
//                case R.id.percOfTreeSpin:
//
//
//                    break;
//            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
    private LinkedHashMap<String, String> diseaseNameDataMap, chemicalNameDataMap,percentageMap;
    private DataAccessHandler dataAccessHandler;
    private GenericTypeAdapter diseaseDataAdapter;
    private Context mContext;
    private UpdateUiListener updateUiListener;
    private Calendar myCalendar = Calendar.getInstance();
    private Spinner rcmnduomSpin;
    Toolbar toolbar;
    private ActionBar actionBar;

    File fileToDownLoad;
    CropMaintenanceDocs cropMaintenanceDocs;

    public DiseaseDetailsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView=inflater.inflate(R.layout.fragment_disease_details, container, false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

        activity.setSupportActionBar(toolbar);
        actionBar = activity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ratingList = new ArrayList<>();


        actionBar.setTitle(getActivity().getResources().getString(R.string.disease_details_screen));

        mContext = getActivity();

        setHasOptionsMenu(true);
        initViews();
        setViews();
        dataAccessHandler = new DataAccessHandler(mContext);



        bindData();

        return rootView;
    }


    private void bindData() {
        mDiseaseModelArray = (ArrayList<Disease>) DataManager.getInstance().getDataFromManager(DataManager.DISEASE_DETAILS);
//        if (mDiseaseModelArray == null && (isFromFollowUp() || isFromCropMaintenance() || isFromConversion())) {
//            mDiseaseModelArray = (ArrayList<Disease>) dataAccessHandler.getDiseaseData(Queries.getInstance().getDiseaseData(CommonConstants.PLOT_CODE), 1);
//        }
        ratingList = (ArrayList<Character>) DataManager.getInstance().getDataFromManager("RATING_LIST_disease");
        if (ratingList == null) {
            ratingList = new ArrayList<>();
        }
        Log.v("@@@retrievedRatingList", "Retrieved rating list: " + ratingList.toString());

        if (null == mDiseaseModelArray)
            mDiseaseModelArray = new ArrayList<Disease>();


        diseaseDataAdapter = new GenericTypeAdapter(getActivity(), mDiseaseModelArray, diseaseNameDataMap, chemicalNameDataMap,percentageMap,uomDataMap, GenericTypeAdapter.TYPE_DISEASE);
        diaseaseList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        diaseaseList.setAdapter(diseaseDataAdapter);
        diseaseDataAdapter.setEditClickListener(this);
    }

//    private void bindData() {
//        mDiseaseModelArray = (ArrayList<Disease>) DataManager.getInstance().getDataFromManager(DataManager.DISEASE_DETAILS);
////        if (mDiseaseModelArray == null && (isFromFollowUp() || isFromCropMaintenance() || isFromConversion())) {
////            mDiseaseModelArray = (ArrayList<Disease>) dataAccessHandler.getDiseaseData(Queries.getInstance().getDiseaseData(CommonConstants.PLOT_CODE), 1);
////        }
//
////        if (null == mDiseaseModelArray)
////            mDiseaseModelArray = new ArrayList<Disease>();
////
////        diseaseDataAdapter = new GenericTypeAdapter(getActivity(), mDiseaseModelArray, diseaseNameDataMap, chemicalNameDataMap,percentageMap,uomDataMap, GenericTypeAdapter.TYPE_DISEASE);
////        diaseaseList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
////        diaseaseList.setAdapter(diseaseDataAdapter);
////        diseaseDataAdapter.setEditClickListener(this);
//
//        if (mDiseaseModelArray != null) {
//
//            diseaseNameSpin.setSelection(CommonUtilsNavigation.getvalueFromHashMap(diseaseNameDataMap, mDiseaseModelArray.get(0).getDiseaseid()));
//            nameOfChemicalUsedSpin.setSelection(mDiseaseModelArray.get(0).getChemicalid() == null ? 0 : CommonUtilsNavigation.getvalueFromHashMap(chemicalNameDataMap, mDiseaseModelArray.get(0).getChemicalid()));
//            percOfTreeSpin.setSelection(mDiseaseModelArray.get(0).getPercTreesId() == null ? 0 : CommonUtilsNavigation.getvalueFromHashMap(percentageMap, mDiseaseModelArray.get(0).getPercTreesId()));
//            controlMeasureSpin.setSelection(mDiseaseModelArray.get(0).getIsControlMeasure() == null ? 0 : mDiseaseModelArray.get(0).getIsControlMeasure() == 1 ? 1 : 2);
//            nameOfChemicalUsedSpinRecmnd.setSelection(mDiseaseModelArray.get(0).getRecommendFertilizerProviderId() == null ? 0 : CommonUtilsNavigation.getvalueFromHashMap(chemicalNameDataMap, mDiseaseModelArray.get(0).getRecommendFertilizerProviderId()));
//
//            double recommendDosage = mDiseaseModelArray.get(0).getRecommendDosage();
//            String valueFromMap;
//
//            if (recommendDosage == 0.0) {
//                rcmndosageEdt.setText("");
//            } else {
//                // Convert the double value to an integer for the map key
//                int intValue = (int) recommendDosage;
//                //valueFromMap = CommonUtilsNavigation.getStValueFromHashMap(chemicalNameDataMap, String.valueOf(intValue));
//                rcmndosageEdt.setText(String.valueOf(intValue));
//            }
//
//
//            rcmnduomSpin.setSelection(mDiseaseModelArray.get(0).getRecommendUOMId() == null ? 0 : CommonUtilsNavigation.getvalueFromHashMap(uomDataMap, mDiseaseModelArray.get(0).getRecommendUOMId()));
//            rcmnduomperSpin.setSelection(mDiseaseModelArray.get(0).getRecommendedUOMId() == null ? 0 : CommonUtilsNavigation.getvalueFromHashMap(rcmnduomperDatamap, mDiseaseModelArray.get(0).getRecommendedUOMId()));
//            observationsEdt.setText(mDiseaseModelArray.get(0).getComments() == null ? "" : mDiseaseModelArray.get(0).getComments());
//        }
//    }

    private void initViews() {
        dataAccessHandler = new DataAccessHandler(getActivity());
        diseaseNameSpin = (Spinner) rootView.findViewById(R.id.diseaseNameSpin);
        nameOfChemicalUsedSpin = (Spinner) rootView.findViewById(R.id.nameOfChemicalUsedSpin);
        nameOfChemicalUsedSpinRecmnd = (Spinner) rootView.findViewById(R.id.nameOfChemicalUsedSpinRecmnd);
        percOfTreeSpin = (Spinner) rootView.findViewById(R.id.percOfTreeSpin);
        controlMeasureSpin= (Spinner) rootView.findViewById(R.id.controlMeasureSpin);

        rcmnduomSpin = (Spinner) rootView.findViewById(R.id.rcmnduomSpin);
        rcmnduomperSpin = (Spinner) rootView.findViewById(R.id.rcmnduomperSpin);
        rcmndosageEdt=(EditText)rootView.findViewById(R.id.rcmndosageEdt);
        observationsEdt = (EditText) rootView.findViewById(R.id.ObservationsEdt);
        diaseaseList = (RecyclerView) rootView.findViewById(R.id.diaseaseList);
        saveBtn = (Button) rootView.findViewById(R.id.saveBtn);
        historyBtn = (Button) rootView.findViewById(R.id.diseasehistoryBtn);
        diseasepdfBtn = (Button) rootView.findViewById(R.id.diseasepdfBtn);
        parentLayout = (LinearLayout) rootView.findViewById(R.id.diseaseParentLayout);
        headerLL = (LinearLayout) rootView.findViewById(R.id.headerLL);
        complaintsBtn = (Button) rootView.findViewById(R.id.complaintsBtn);
        complaintsBtn.setEnabled(false);
        complaintsBtn.setVisibility(View.GONE);
       // complaintsBtn.setVisibility((CommonUiUtils.isComplaintsDataEntered()) ? View.GONE : View.VISIBLE);
        complaintsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle dataBundle = new Bundle();
                dataBundle.putBoolean(CommonConstants.KEY_NEW_COMPLAINT, true);
                ComplaintDetailsFragment complaintDetailsFragment = new ComplaintDetailsFragment();
				complaintDetailsFragment.setArguments(dataBundle);
				complaintDetailsFragment.setUpdateUiListener(DiseaseDetailsFragment.this);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(android.R.id.content, complaintDetailsFragment).addToBackStack(null)
                        .commit();
            }
        });

        cropMaintenanceDocs = (CropMaintenanceDocs) dataAccessHandler.getCMDocsData(Queries.getInstance().getDiseasePDFfile(), 0);

        if (cropMaintenanceDocs != null){

        fileToDownLoad = new File(CommonUtils.get3FFileRootPath() + "3F_CMDocs/" + cropMaintenanceDocs.getFileName() + cropMaintenanceDocs.getFileExtension());

        if (null != fileToDownLoad && fileToDownLoad.exists()) {

            diseasepdfBtn.setVisibility(View.VISIBLE);

        }else{
            diseasepdfBtn.setVisibility(View.GONE);
        }

        }

        diseasepdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDiseasePDFDialog(getContext());
            }
        });
    }

    public void showDiseasePDFDialog(Context activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.pdfdialog);

        Toolbar titleToolbar;
        titleToolbar = (Toolbar) dialog.findViewById(R.id.titleToolbar);
        titleToolbar.setTitle("Disease PDF");
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

//    private void setViews() {
//        parentLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                CommonUtilsNavigation.hideKeyBoard(getActivity());
//                return false;
//            }
//        });
//        saveBtn.setOnClickListener(this);
//        //historyBtn.setOnClickListener(this);
//        nameOfChemicalUsedSpin.setOnItemSelectedListener(spinListener);
//        percOfTreeSpin.setOnItemSelectedListener(spinListener);
//
//        diseaseNameSpin.setOnItemSelectedListener(spinListener);
//        diseaseNameDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getLookUpData("5"));
//        chemicalNameDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getLookUpData("647"));
//        chemicalNameDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getLookUpData("647"));
//        percentageMap=dataAccessHandler.getGenericData(Queries.getInstance().getLookUpData("571"));
//       // controlMeasureMap=dataAccessHandler.getGenericData(Queries.getInstance().getLookUpData(""));
//
//
//        diseaseNameSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Disease Name", diseaseNameDataMap));
//        nameOfChemicalUsedSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Name of Fungicide", chemicalNameDataMap));
//        nameOfChemicalUsedSpinRecmnd.setAdapter(adapterSetFromHashmap(getActivity(), "Name of Fungicide", chemicalNameDataMap));
//        percOfTreeSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Percentage of Tree", percentageMap));
//      //  controlMeasureSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Control Measure", controlMeasureMap));
//
//        uomDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getUOM());
//        rcmnduomSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "UOM", uomDataMap));
//        rcmnduomperDatamap = dataAccessHandler.getGenericData(Queries.getInstance().getUOMper());
//        rcmnduomperSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "UOM Per", rcmnduomperDatamap));
//
//        historyBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showDialog(getContext());
//            }
//        });
//    }

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
        percOfTreeSpin.setOnItemSelectedListener(spinListener);

        diseaseNameSpin.setOnItemSelectedListener(spinListener);
        diseaseNameDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getLookUpData("5"));
        chemicalNameDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getLookUpData("7"));
        chemicalNameDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getLookUpData("7"));
        percentageMap=dataAccessHandler.getGenericData(Queries.getInstance().getLookUpData("571"));
        // controlMeasureMap=dataAccessHandler.getGenericData(Queries.getInstance().getLookUpData(""));


        diseaseNameSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Disease Name", diseaseNameDataMap));
        nameOfChemicalUsedSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Name of chemical", chemicalNameDataMap));
        nameOfChemicalUsedSpinRecmnd.setAdapter(adapterSetFromHashmap(getActivity(), "Name of Chemical", chemicalNameDataMap));
        percOfTreeSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Percentage of Tree", percentageMap));
        //  controlMeasureSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Control Measure", controlMeasureMap));

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
    public void addingValues() {
        if (CommonConstants.perc_tree_disease == ' ') {
            if ((percOfTreeSpin.getSelectedItem().toString().equals("No Deficiency")) && (controlMeasureSpin.getSelectedItemPosition() == 1)) {
                CommonConstants.perc_tree_disease = 'A';
                ratingList.add('A');
            } else if ((percOfTreeSpin.getSelectedItem().toString().equals("Less than 5%")) && (controlMeasureSpin.getSelectedItemPosition() == 1)) {
                CommonConstants.perc_tree_disease = 'A';
                ratingList.add('A');
            } else if ((percOfTreeSpin.getSelectedItem().toString().equals("5 to 25%")) && (controlMeasureSpin.getSelectedItemPosition() == 1)) {
                CommonConstants.perc_tree_disease = 'B';
                ratingList.add('B');
            } else {
                CommonConstants.perc_tree_disease = 'C';
                ratingList.add('C');
            }
            Log.v("@@@rating", "" + ratingList.size());
        } else {
            if ((percOfTreeSpin.getSelectedItem().toString().equals("No Deficiency")) && (controlMeasureSpin.getSelectedItemPosition() == 1)) {
                ratingList.add('A');
            } else if (((percOfTreeSpin.getSelectedItem().toString()).equals("Less than 5%")) && (controlMeasureSpin.getSelectedItemPosition() == 1)) {
                ratingList.add('A');
            } else if ((percOfTreeSpin.getSelectedItem().toString().equals("5 to 25%")) && (controlMeasureSpin.getSelectedItemPosition() == 1)) {
                prc_tree = 'B';
                if (prc_tree > CommonConstants.perc_tree_disease) {
                    ratingList.add('B');
                    CommonConstants.perc_tree_disease = prc_tree;
                } else {
                    CommonConstants.perc_tree_disease = 'C';
                    ratingList.add('B');
                }
            } else {
                CommonConstants.perc_tree_disease = 'C';
                ratingList.add('C');
            }
        }

        // Store ratingList in DataManager for persistence
        DataManager.getInstance().addData("RATING_LIST_disease", ratingList);

        Log.v("@@@diseaseRating", "" + CommonConstants.perc_tree_disease);
        Log.v("@@@ratingList", "Saved rating list: " + ratingList.toString());
    }


    public void showDialog(Context activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.diseaselastvisteddata);

        Toolbar titleToolbar;
        titleToolbar = (Toolbar) dialog.findViewById(R.id.titleToolbar);
        titleToolbar.setTitle("Disease History");
        titleToolbar.setTitleTextColor(getResources().getColor(R.color.white));

        LinearLayout mainLL = (LinearLayout) dialog.findViewById(R.id.diseasemainlyt);
        RecyclerView recyclerView = dialog.findViewById(R.id.DiseaseRecyclerView);
      TextView norecords = (TextView) dialog.findViewById(R.id.diseasenorecord_tv);

        String lastVisitCode = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getLatestCropMaintanaceHistoryCode(CommonConstants.PLOT_CODE));
        diseaselastvisitdatamap = (ArrayList<Disease>) dataAccessHandler.getDiseaseData(Queries.getInstance().getRecommndCropMaintenanceHistoryData(lastVisitCode, DatabaseKeys.TABLE_DISEASE), 1);

        if (diseaselastvisitdatamap.size() > 0){
            norecords.setVisibility(View.GONE);
            mainLL.setVisibility(View.VISIBLE);

            DiseaseHistoryAdapter adapter = new DiseaseHistoryAdapter(activity, diseaselastvisitdatamap);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
            recyclerView.setAdapter(adapter);

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveBtn:
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                String selectedDiseaseName = diseaseNameSpin.getSelectedItem().toString();

                // Loop through existing records to check for duplicates
                boolean isDuplicate = false;
                for (Disease existingModel : mDiseaseModelArray) {
                    if (existingModel.getDiseaseid() == Integer.parseInt(getKey(diseaseNameDataMap, selectedDiseaseName))) {
                        isDuplicate = true;
                        break;
                    }
                }

                if (isDuplicate) {
                    // Show error message
                    Toast.makeText(getContext(), "Selected Disease already added. Please select a different Disease", Toast.LENGTH_SHORT).show();
                    return;
                }

                Disease mDiseaseModel = new Disease();

                if(diseaseNameSpin.getSelectedItem().toString().equals("No Diseases")){
                    if(validateUI1()) {
                        mDiseaseModel.setDiseaseid(diseaseNameSpin.getSelectedItemPosition() == 0 ? null : Integer.parseInt(getKey(diseaseNameDataMap, diseaseNameSpin.getSelectedItem().toString())));
                        //  mDiseaseModel.setIsControlMeasure(controlMeasureSpin.getSelectedItemPosition() == 1 ? 1 : 0);
                        mDiseaseModel.setComments(observationsEdt.getText().toString());
                        mDiseaseModel.setCreateddate(CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
                        mDiseaseModelArray.add(mDiseaseModel);
                        ratingList.add('A');
                        diseaseNameSpin.setSelection(0);
                        controlMeasureSpin.setSelection(0);
                        DataManager.getInstance().addData(DataManager.DISEASE_DETAILS, mDiseaseModelArray);
                        DataManager.getInstance().addData("RATING_LIST_disease", ratingList);
                        diseaseDataAdapter.notifyDataSetChanged();
                        updateUiListener.updateUserInterface(0);
                    }
                }else {

                    if (validateUI()) {
                        mDiseaseModel.setDiseaseid(diseaseNameSpin.getSelectedItemPosition() == 0 ? null : Integer.parseInt(getKey(diseaseNameDataMap, diseaseNameSpin.getSelectedItem().toString())));
                        mDiseaseModel.setChemicalid(nameOfChemicalUsedSpin.getSelectedItemPosition() == 0 ? null : Integer.parseInt(getKey(chemicalNameDataMap, nameOfChemicalUsedSpin.getSelectedItem().toString())));
                        //mDiseaseModel.setPercTreesId(percOfTreeSpin.getSelectedItemPosition() == 0 ? null :Integer.parseInt(getKey(percentageMap, percOfTreeSpin.getSelectedItem().toString())));

                        mDiseaseModel.setPercTreesId(Integer.parseInt(getKey(percentageMap, percOfTreeSpin.getSelectedItem().toString())));
                        mDiseaseModel.setComments(observationsEdt.getText().toString());
                        mDiseaseModel.setCreateddate(CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
                        mDiseaseModel.setRecommendFertilizerProviderId(nameOfChemicalUsedSpinRecmnd.getSelectedItemPosition() == 0 ? null : Integer.parseInt(getKey(chemicalNameDataMap, nameOfChemicalUsedSpinRecmnd.getSelectedItem().toString())));
                        mDiseaseModel.setRecommendUOMId(rcmnduomSpin.getSelectedItemPosition() == 0 ? null : Integer.parseInt(getKey(uomDataMap, rcmnduomSpin.getSelectedItem().toString())));
                        mDiseaseModel.setRecommendedUOMId(rcmnduomperSpin.getSelectedItemPosition() == 0 ? null : Integer.parseInt(getKey(rcmnduomperDatamap, rcmnduomperSpin.getSelectedItem().toString())));

                        mDiseaseModel.setRecommendDosage(TextUtils.isEmpty(rcmndosageEdt.getText().toString()) == true ? 0.0 : Double.parseDouble(rcmndosageEdt.getText().toString()));
                        mDiseaseModel.setIsControlMeasure(controlMeasureSpin.getSelectedItemPosition() == 1 ? 1 : 0);
                        mDiseaseModelArray.add(mDiseaseModel);
                        addingValues();
                        diseaseNameSpin.setSelection(0);
                        nameOfChemicalUsedSpinRecmnd.setSelection(0);
                        percOfTreeSpin.setSelection(0);
                        controlMeasureSpin.setSelection(0);
                        rcmnduomSpin.setSelection(0);
                        rcmnduomperSpin.setSelection(0);
                        rcmndosageEdt.setText("");
                        DataManager.getInstance().addData(DataManager.DISEASE_DETAILS, mDiseaseModelArray);

                        diseaseDataAdapter.notifyDataSetChanged();
                        updateUiListener.updateUserInterface(0);

                    }
                }
                CommonUtilsNavigation.hideKeyBoard(getActivity());
                break;
//            case R.id.historyBtn:
//                CropMaintainanceHistoryFragment newFragment = new CropMaintainanceHistoryFragment();
//                Bundle bundle = new Bundle();
//                bundle.putInt("screen", DISEASE_DATA);
//                newFragment.setArguments(bundle);
//                newFragment.show(getActivity().getFragmentManager(), "history");
//
//                break;

        }
    }
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.saveBtn:
//                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
//                Disease mDiseaseModel = new Disease();
//                mDiseaseModelArray = new ArrayList<Disease>();
//
//                if(diseaseNameSpin.getSelectedItem().toString().equals("No Diseases")){
//                    if(validateUI1()) {
//                        mDiseaseModel.setDiseaseid(diseaseNameSpin.getSelectedItemPosition() == 0 ? null : Integer.parseInt(getKey(diseaseNameDataMap, diseaseNameSpin.getSelectedItem().toString())));
//                      //  mDiseaseModel.setIsControlMeasure(controlMeasureSpin.getSelectedItemPosition() == 1 ? 1 : 0);
//                        mDiseaseModel.setComments(observationsEdt.getText().toString());
//                        mDiseaseModel.setCreateddate(CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
//                        mDiseaseModelArray.clear();
//                        mDiseaseModelArray.add(mDiseaseModel);
//                        ratingList.add('A');
//                        diseaseNameSpin.setSelection(0);
//                        controlMeasureSpin.setSelection(0);
//                        DataManager.getInstance().addData(DataManager.DISEASE_DETAILS, mDiseaseModelArray);
//                        getFragmentManager().popBackStack();
//                       // diseaseDataAdapter.notifyDataSetChanged();
//                        updateUiListener.updateUserInterface(0);
//                    }
//                }else {
//
//                    if (validateUI()) {
//                        mDiseaseModel.setDiseaseid(diseaseNameSpin.getSelectedItemPosition() == 0 ? null : Integer.parseInt(getKey(diseaseNameDataMap, diseaseNameSpin.getSelectedItem().toString())));
//                        mDiseaseModel.setChemicalid(nameOfChemicalUsedSpin.getSelectedItemPosition() == 0 ? null : Integer.parseInt(getKey(chemicalNameDataMap, nameOfChemicalUsedSpin.getSelectedItem().toString())));
//                        //mDiseaseModel.setPercTreesId(percOfTreeSpin.getSelectedItemPosition() == 0 ? null :Integer.parseInt(getKey(percentageMap, percOfTreeSpin.getSelectedItem().toString())));
//
//                        mDiseaseModel.setPercTreesId(Integer.parseInt(getKey(percentageMap, percOfTreeSpin.getSelectedItem().toString())));
//                        mDiseaseModel.setComments(observationsEdt.getText().toString());
//                        mDiseaseModel.setCreateddate(CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
//                        mDiseaseModel.setRecommendFertilizerProviderId(nameOfChemicalUsedSpinRecmnd.getSelectedItemPosition() == 0 ? null : Integer.parseInt(getKey(chemicalNameDataMap, nameOfChemicalUsedSpinRecmnd.getSelectedItem().toString())));
//                        mDiseaseModel.setRecommendUOMId(rcmnduomSpin.getSelectedItemPosition() == 0 ? null : Integer.parseInt(getKey(uomDataMap, rcmnduomSpin.getSelectedItem().toString())));
//                        mDiseaseModel.setRecommendedUOMId(rcmnduomperSpin.getSelectedItemPosition() == 0 ? null : Integer.parseInt(getKey(rcmnduomperDatamap, rcmnduomperSpin.getSelectedItem().toString())));
//
//                        mDiseaseModel.setRecommendDosage(TextUtils.isEmpty(rcmndosageEdt.getText().toString()) == true ? 0.0 : Double.parseDouble(rcmndosageEdt.getText().toString()));
//                        mDiseaseModel.setIsControlMeasure(controlMeasureSpin.getSelectedItemPosition() == 1 ? 1 : 0);
//                        mDiseaseModelArray.clear();
//                        mDiseaseModelArray.add(mDiseaseModel);
//                        addingValues();
//                        diseaseNameSpin.setSelection(0);
//                        nameOfChemicalUsedSpinRecmnd.setSelection(0);
//                        percOfTreeSpin.setSelection(0);
//                        controlMeasureSpin.setSelection(0);
//                        rcmnduomSpin.setSelection(0);
//                        rcmnduomperSpin.setSelection(0);
//                        rcmndosageEdt.setText("");
//                        DataManager.getInstance().addData(DataManager.DISEASE_DETAILS, mDiseaseModelArray);
//                        getFragmentManager().popBackStack();
//                        //diseaseDataAdapter.notifyDataSetChanged();
//                        updateUiListener.updateUserInterface(0);
//
//                    }
//                }
//                CommonUtilsNavigation.hideKeyBoard(getActivity());
//                break;
////            case R.id.historyBtn:
////                CropMaintainanceHistoryFragment newFragment = new CropMaintainanceHistoryFragment();
////                Bundle bundle = new Bundle();
////                bundle.putInt("screen", DISEASE_DATA);
////                newFragment.setArguments(bundle);
////                newFragment.show(getActivity().getFragmentManager(), "history");
////
////                break;
//
//        }
//    }

    private boolean validateUI() {
        return spinnerSelect(diseaseNameSpin, "Source of Disease", mContext) && spinnerSelect(nameOfChemicalUsedSpin, "Name of Fungicide Used", mContext)
                &&spinnerSelect(percOfTreeSpin,"Percentage Of Tree",mContext)&&spinnerSelect(controlMeasureSpin,"Control Measure",mContext);

    }

    private boolean validateUI1() {
        return spinnerSelect(diseaseNameSpin, "Source of Disease", mContext);

    }

    @Override
    public void onEditClicked(int position) {
        // Ensure the position is within valid range for mDiseaseModelArray and ratingList
        if (position < 0 || position >= mDiseaseModelArray.size() || position >= ratingList.size()) {
            Log.e("@@@Tree", "Invalid position: " + position);
            return;
        }

        // Remove the element at the given position in both lists

        Log.v("@@@Tree", "Position removed: " + position);

        // Reset or update perc_tree_disease based on the size of mDiseaseModelArray
        if (mDiseaseModelArray.isEmpty()) {
            CommonConstants.perc_tree_disease = ' ';
        } else if (mDiseaseModelArray.size() == 1) {
            CommonConstants.perc_tree_disease = ratingList.get(0);
        } else  if (mDiseaseModelArray.size() > 1) {
            double maxRating = Double.MIN_VALUE;
            for (int i = 0; i < ratingList.size(); i++) {
                CommonConstants.perc_tree_disease = ratingList.get(i);
            }
        }

        // Handle additional logic for mDiseaseModelArray
        if (!mDiseaseModelArray.isEmpty() && position < mDiseaseModelArray.size()) {
            String diseaseName = dataAccessHandler.getOnlyOneValueFromDb(
                    Queries.getInstance().getlookupdata(mDiseaseModelArray.get(position).getDiseaseid())
            );

            Log.d("DiseaseName====>880", "Disease Name: " + diseaseName);

            // Check if the disease name is "Ganoderma" and delete related data if found
            if ("Ganoderma".equalsIgnoreCase(diseaseName)) {
                Log.d("DiseaseCheck", "Ganoderma (DiseaseId == 349) found. Deleting related data...");
                DataManager.getInstance().deleteData(DataManager.GANODERMA_DETAILS);
            }
            mDiseaseModelArray.remove(position);
            ratingList.remove(position);
        //    mDiseaseModelArray.remove(position);
        } else {
            Log.w("onEditClicked", "mDiseaseModelArray is empty or position out of bounds");
        }

        // Notify adapter about data changes
        diseaseDataAdapter.notifyDataSetChanged();

        // Update UI using the listener
        if (updateUiListener != null) {
            updateUiListener.updateUserInterface(0);
        }
    }


//
//    @Override
//    public void onEditClicked(int position) {
//        // Ensure position is within the valid range for mDiseaseModelArray and ratingList
//        if (position >= 0 && position < mDiseaseModelArray.size() && position < ratingList.size()) {
//            // Remove the element at the given position in both lists
//            mDiseaseModelArray.remove(position);
//            ratingList.remove(position);
//            Log.v("@@@Tree", "" + position);
//
//            // If mDiseaseModelArray is empty, reset the perc_tree_disease value
//            if (mDiseaseModelArray.isEmpty()) {
//                CommonConstants.perc_tree_disease = ' ';
//            }
//
//            // Check if there's only one item left
//            if (mDiseaseModelArray.size() == 1) {
//                CommonConstants.perc_tree_disease = ratingList.get(0);
//            }
//            // Handle mPestModelArray
//            if (mDiseaseModelArray != null && position < mDiseaseModelArray.size()) {
//                String pestname = dataAccessHandler.getOnlyOneValueFromDb(
//                        Queries.getInstance().getlookupdata(mDiseaseModelArray.get(position).getDiseaseid())
//                );
//
//                Log.d("DiseaseNAme====>880", "Disease Name: " + pestname);
//
//                // Check if the pest name is "Ganoderma"
//                if (pestname != null && pestname.equalsIgnoreCase("Ganoderma")) {
//                    Log.d("DiseaseCheck", "Ganoderma (DiseasetId == 349) found. Deleting related data...");
//                    DataManager.getInstance().deleteData(DataManager.GANODERMA_DETAILS);
//                }
//
//                mDiseaseModelArray.remove(position);
//            } else {
//                Log.w("onEditClicked", "mPestModelArray is null or position out of bounds");
//            }
//            // If there are more than one item, find the max value from ratingList
//             if (mDiseaseModelArray.size() > 1) {
//                double maxRating = Double.MIN_VALUE;
//                for (int i = 0; i < ratingList.size(); i++) {
//                    CommonConstants.perc_tree_disease = ratingList.get(i);
//                }
//
//            }
//
//            // Notify adapter about data changes
//            diseaseDataAdapter.notifyDataSetChanged();
//            updateUiListener.updateUserInterface(0);
//
//        } else {
//            // Log a message if the position is invalid
//            Log.e("@@@Tree", "Invalid position: " + position);
//        }
//    }

//    @Override
//    public void onEditClicked(int position) {
//
//         mDiseaseModelArray.remove(position);
//        Log.v("@@@Tree",""+position);
//
//        ratingList.remove(position);
//
//        if (mDiseaseModelArray.isEmpty()){
//            CommonConstants.perc_tree_disease =' ';
//        }
//
//        if(mDiseaseModelArray.size() == 1)
//        {
//            CommonConstants.perc_tree_disease = ratingList.get(0);
//        }
//        else if(  mDiseaseModelArray.size() > 1)
//        {
//            for(int i=0;i<  ratingList.size()-1;i++){
//                for(int j=i+1;j<  ratingList.size();j++){
//
//                    if(ratingList.get(i) > ratingList.get(j))
//                    {
//                        CommonConstants.perc_tree_disease = ratingList.get(i);
//                    }
//
//
//                    else{
//                        CommonConstants.perc_tree_disease=ratingList.get(j);
//                    }
//                }
//            }
//        }
//
//        diseaseDataAdapter.notifyDataSetChanged();
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
}