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
import com.cis.palm360.dbmodels.Nutrient;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static com.cis.palm360.R.id.diaseaseList;
import static com.cis.palm360.R.id.saveBtn;
import static com.cis.palm360.common.CommonUtils.spinnerSelect;
import static com.cis.palm360.cropmaintenance.CommonUtilsNavigation.getKey;

//Used to enter nutrient deficiency details during crop maintenance
public class NDScreen extends Fragment implements View.OnClickListener, PalmDetailsEditListener, UpdateUiListener, OnPageChangeListener, OnLoadCompleteListener {

    private Context mContext;
    private View rootView;
    private Spinner nutritionSpin, nameOfChemicalUsedSpin, percentageOfTreesSpn;
    private EditText commentsEdt;
    private Button saveBtn, historyBtn;
    private RecyclerView nutrientList;
    private LinkedHashMap<String, String> nutritionDataMap, chemicalNameDataMap, percentageMap;
    private ArrayList<Nutrient> mNutrientModelArray;
    private GenericTypeAdapter nutrientDataAdapter;
    private DataAccessHandler dataAccessHandler;
    private UpdateUiListener updateUiListener;
    private Button complaintsBtn, nutrientpdfBtn;
    private Spinner rcmndfertilizerProductNameSpin, rcmnduomSpin,rcmnduomperSpin;
    private EditText rcmndosageEdt;
    private LinkedHashMap fertilizerTypeDataMap, uomDataMap, rcmnduomperDatamap;
    private char prc_tree = ' ';
    private ArrayList<Character> ratingList;
    private ActionBar actionBar;
    private Toolbar toolbar;

    private ArrayList<Nutrient> nutrientlastvisitdatamap;

    File fileToDownLoad;
    CropMaintenanceDocs cropMaintenanceDocs;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.nutrient_details_layout, container, false);

        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        actionBar = activity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.nutrient_details));

        ratingList = new ArrayList<>();
        mContext = getActivity();

        initView();
        setViews();
        bindData();

        return rootView;
    }


    private void initView() {
        dataAccessHandler = new DataAccessHandler(getActivity());



        nutritionSpin = (Spinner) rootView.findViewById(R.id.nutritionSpin);
        nameOfChemicalUsedSpin = (Spinner) rootView.findViewById(R.id.nameOfChemicalUsedSpin);
        commentsEdt = (EditText) rootView.findViewById(R.id.commentsEdt);
        nutrientList = (RecyclerView) rootView.findViewById(R.id.nutrientList);
        saveBtn = (Button) rootView.findViewById(R.id.saveBtn);
        historyBtn = (Button) rootView.findViewById(R.id.historyBtn);
        nutrientpdfBtn = (Button) rootView.findViewById(R.id.nutrientpdfBtn);
        rcmndfertilizerProductNameSpin = (Spinner) rootView.findViewById(R.id.rcmndfertilizerProductNameSpin);
        rcmnduomSpin = (Spinner) rootView.findViewById(R.id.rcmnduomSpin);
        rcmndosageEdt = (EditText) rootView.findViewById(R.id.rcmndosageEdt);
        percentageOfTreesSpn = rootView.findViewById(R.id.percentageSpn);
        rcmnduomperSpin = (Spinner) rootView.findViewById(R.id.rcmnduomperSpin);

        cropMaintenanceDocs = (CropMaintenanceDocs) dataAccessHandler.getCMDocsData(Queries.getInstance().getNutrientPDFfile(), 0);

        if (cropMaintenanceDocs != null) {

            fileToDownLoad = new File(CommonUtils.get3FFileRootPath() + "3F_CMDocs/" + cropMaintenanceDocs.getFileName() + cropMaintenanceDocs.getFileExtension());

            if (null != fileToDownLoad && fileToDownLoad.exists()) {

                nutrientpdfBtn.setVisibility(View.VISIBLE);

            } else {
                nutrientpdfBtn.setVisibility(View.GONE);
            }
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        nutrientpdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNutrientPDFDialog(getContext());
            }
        });


        complaintsBtn = (Button) rootView.findViewById(R.id.complaintsBtn);

        complaintsBtn.setEnabled(false);
        complaintsBtn.setVisibility(View.GONE);
        //complaintsBtn.setVisibility((CommonUiUtils.isComplaintsDataEntered()) ? View.GONE : View.VISIBLE);
        complaintsBtn.setOnClickListener(v -> {
            Bundle dataBundle = new Bundle();
            dataBundle.putBoolean(CommonConstants.KEY_NEW_COMPLAINT, true);
            ComplaintDetailsFragment complaintDetailsFragment = new ComplaintDetailsFragment();
            complaintDetailsFragment.setArguments(dataBundle);
            complaintDetailsFragment.setUpdateUiListener(NDScreen.this);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, complaintDetailsFragment).addToBackStack(null)
                    .commit();
        });
    }

    public void showNutrientPDFDialog(Context activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.pdfdialog);

        Toolbar titleToolbar;
        titleToolbar = (Toolbar) dialog.findViewById(R.id.titleToolbar);
        titleToolbar.setTitle("Nutrient PDF");
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
    private void bindData() {
        // Retrieve mNutrientModelArray from DataManager
        mNutrientModelArray = (ArrayList<Nutrient>) DataManager.getInstance().getDataFromManager(DataManager.NUTRIENT_DETAILS);
        if (mNutrientModelArray == null) {
            mNutrientModelArray = new ArrayList<>();
        }


        // Retrieve ratingList from DataManager
        ratingList = (ArrayList<Character>) DataManager.getInstance().getDataFromManager("RATING_LIST");
        if (ratingList == null) {
            ratingList = new ArrayList<>();
        }

        // Log the sizes of the lists for debugging
        Log.v("@@@ adda value mNutrientModelArray size", "" + mNutrientModelArray.size());
        Log.v("@@@ adda value ratingList size", "" + ratingList.size());

        // Set up the adapter for the RecyclerView
        nutrientDataAdapter = new GenericTypeAdapter(
                getActivity(),
                mNutrientModelArray,
                nutritionDataMap,
                chemicalNameDataMap,
                percentageMap,
                uomDataMap,
                GenericTypeAdapter.TYPE_NUTRIENT
        );
        nutrientList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        nutrientList.setAdapter(nutrientDataAdapter);
        nutrientDataAdapter.setEditClickListener(this);
    }

//    private void bindData() {
//        mNutrientModelArray = (ArrayList<Nutrient>) DataManager.getInstance().getDataFromManager(DataManager.NUTRIENT_DETAILS);
//        ratingList = (ArrayList<Character>) DataManager.getInstance().getDataFromManager("RATING_LIST");
//        if (null == mNutrientModelArray)
//            mNutrientModelArray = new ArrayList<Nutrient>();
//        ratingList = new ArrayList<>();
//        Log.v("@@@ adda value 2143", "" + ratingList.size());
//        Log.v("@@@ adda value mNutrientModelArray", "" + mNutrientModelArray.size());
//        nutrientDataAdapter = new GenericTypeAdapter(getActivity(), mNutrientModelArray, nutritionDataMap, chemicalNameDataMap, percentageMap, uomDataMap, GenericTypeAdapter.TYPE_NUTRIENT);
//        nutrientList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//        nutrientList.setAdapter(nutrientDataAdapter);
//        nutrientDataAdapter.setEditClickListener(this);
//    }

    private void setViews() {

        saveBtn.setOnClickListener(this);
        //historyBtn.setOnClickListener(this);
        nutritionDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getLookUpData("21"));
        chemicalNameDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getLookUpData("645"));
        percentageMap = dataAccessHandler.getGenericData(Queries.getInstance().getLookUpData("569"));

       nutritionSpin.setOnItemSelectedListener(spinListener);
        nutritionSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Nutrient Name", nutritionDataMap));
        nameOfChemicalUsedSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Name of Fertilizer", chemicalNameDataMap));
        percentageOfTreesSpn.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Percentage of Tree", percentageMap));
        fertilizerTypeDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getLookUpData("645"));
        uomDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getUOM());
        rcmnduomperDatamap = dataAccessHandler.getGenericData(Queries.getInstance().getUOMper());

        rcmndfertilizerProductNameSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Fertilizer Product Name", fertilizerTypeDataMap));
        rcmnduomSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "UOM", uomDataMap));
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
        dialog.setContentView(R.layout.nutrientlastvisteddata);

        Toolbar titleToolbar = dialog.findViewById(R.id.titleToolbar);
        titleToolbar.setTitle("Nutrient History");
        titleToolbar.setTitleTextColor(activity.getResources().getColor(R.color.white));

        RecyclerView recyclerView = dialog.findViewById(R.id.nutrientRecyclerView);
        LinearLayout mainLL = dialog.findViewById(R.id.nutrientmainlyt);
        TextView norecords = dialog.findViewById(R.id.nutrientnorecord_tv);

        String lastVisitCode = dataAccessHandler.getOnlyOneValueFromDb(
                Queries.getInstance().getLatestCropMaintanaceHistoryCode(CommonConstants.PLOT_CODE)
        );
        nutrientlastvisitdatamap = (ArrayList<Nutrient>) dataAccessHandler.getNutrientData(
                Queries.getInstance().getRecommndCropMaintenanceHistoryData(lastVisitCode, DatabaseKeys.TABLE_NUTRIENT),
                1
        );

        if (nutrientlastvisitdatamap != null && nutrientlastvisitdatamap.size() > 0) {
            norecords.setVisibility(View.GONE);
            mainLL.setVisibility(View.VISIBLE);

            NutrientHistoryAdapter adapter = new NutrientHistoryAdapter(activity, nutrientlastvisitdatamap);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
            recyclerView.setAdapter(adapter);

        } else {
            mainLL.setVisibility(View.GONE);
            norecords.setVisibility(View.VISIBLE);
        }

        dialog.show();

        new Handler().postDelayed(() -> {
            // Placeholder for any post-delay action
        }, 500);
    }

    public void addingValues() {
        char prc_tree;

        // Determine `prc_tree` based on the selected percentage of trees
        if (percentageOfTreesSpn.getSelectedItem().toString().equals("No Deficiency")) {
            prc_tree = 'A';
        } else if (percentageOfTreesSpn.getSelectedItem().toString().equals("Less than 10%")) {
            prc_tree = 'B';
        } else {
            prc_tree = 'C';
        }

        // Update `CommonConstants.perc_tree` and `ratingList`
        if (CommonConstants.perc_tree == ' ') {
            CommonConstants.perc_tree = prc_tree;
            ratingList.add(prc_tree);
        } else {
            // Check if the new `prc_tree` has a higher value
            if (prc_tree > CommonConstants.perc_tree) {
                CommonConstants.perc_tree = prc_tree;
            }
            ratingList.add(prc_tree);
        }

        // Save the updated `ratingList` to `DataManager`
        DataManager.getInstance().addData("RATING_LIST", ratingList);

        Log.v("@@@Tree", "CommonConstants.perc_tree: " + CommonConstants.perc_tree);
    }

//    public void addingValues() {
//        if (CommonConstants.perc_tree == ' ') {
//
//            if (percentageOfTreesSpn.getSelectedItem().toString().equals("No Deficiency")) {
//                CommonConstants.perc_tree = 'A';
//                ratingList.add('A');
//
//
//            } else if (percentageOfTreesSpn.getSelectedItem().toString().equals("Less than 10%")) {
//                CommonConstants.perc_tree = 'B';
//                ratingList.add('B');
//
//            } else {
//                CommonConstants.perc_tree = 'C';
//                ratingList.add('C');
//
//            }
//        } else {
//            if ((percentageOfTreesSpn.getSelectedItem().toString()).equals("No Deficiency")) {
//                ratingList.add('A');
//
//            } else if (percentageOfTreesSpn.getSelectedItem().toString().equals("Less than 10%")) {
//                prc_tree = 'B';
//                if (prc_tree > CommonConstants.perc_tree) {
//                    ratingList.add('B');
//
//                    CommonConstants.perc_tree = prc_tree;
//                } else {
//
//                    CommonConstants.perc_tree = 'C';
//                    ratingList.add('B');
//
//                }
//            } else {
//                CommonConstants.perc_tree = 'C';
//                ratingList.add('C');
//            }
//        }
//
//        DataManager.getInstance().addData("RATING_LIST", ratingList);
//
//        Log.v("@@@Tree", "" + CommonConstants.perc_tree);
//
//    }

    @Override

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveBtn:
                String selectedNutrientName = nutritionSpin.getSelectedItem().toString();

                // Loop through existing records to check for duplicates
                boolean isDuplicate = false;
                for (Nutrient existingModel : mNutrientModelArray) {
                    if (existingModel.getNutrientid() == Integer.parseInt(getKey(nutritionDataMap, selectedNutrientName))) {
                        isDuplicate = true;
                        break;
                    }
                }

                if (isDuplicate) {
                    // Show error message
                    Toast.makeText(getContext(), " Selected Nutrient Deficiency already added. Please select a different Nutrient Deficiency.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Nutrient mNutrientModel = new Nutrient();

                if(nutritionSpin.getSelectedItem().toString().equals("No Nutrient Deficiency")){
                    if(validateFields1()) {
                        mNutrientModel.setNutrientid(nutritionSpin.getSelectedItemPosition() == 0 ? 0 : Integer.parseInt(getKey(nutritionDataMap, nutritionSpin.getSelectedItem().toString())));
                        mNutrientModel.setComments(commentsEdt.getText().toString());
                        mNutrientModel.setCreateddate(CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
                        mNutrientModelArray.add(mNutrientModel);
                        ratingList.add('C');
                        nutritionSpin.setSelection(0);
                        DataManager.getInstance().addData(DataManager.NUTRIENT_DETAILS, mNutrientModelArray);
                        DataManager.getInstance().addData("RATING_LIST", ratingList);
                        CommonUtilsNavigation.hideKeyBoard(getActivity());
                        nutrientDataAdapter.notifyDataSetChanged();
                        commentsEdt.setText("");
                        rcmndosageEdt.setText("");
                        updateUiListener.updateUserInterface(0);

                    }
                    Log.v("@@@ adda value357", "" + ratingList.size());
                }else {
                    if (validateFields()) {
                        mNutrientModel.setNutrientid(nutritionSpin.getSelectedItemPosition() == 0 ? 0 : Integer.parseInt(getKey(nutritionDataMap, nutritionSpin.getSelectedItem().toString())));
                        mNutrientModel.setChemicalid(nameOfChemicalUsedSpin.getSelectedItemPosition() == 0 ? 0 : Integer.parseInt(getKey(chemicalNameDataMap, nameOfChemicalUsedSpin.getSelectedItem().toString())));
                        mNutrientModel.setPercTreesId(Integer.parseInt(getKey(percentageMap, percentageOfTreesSpn.getSelectedItem().toString())));
                        mNutrientModel.setComments(commentsEdt.getText().toString());
                        mNutrientModel.setCreateddate(CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
                        mNutrientModel.setRecommendFertilizerProviderId(rcmndfertilizerProductNameSpin.getSelectedItemPosition() == 0 ? 0 : (Integer.parseInt(getKey(fertilizerTypeDataMap, rcmndfertilizerProductNameSpin.getSelectedItem().toString()))));
                        mNutrientModel.setRecommendUOMId(rcmnduomSpin.getSelectedItemPosition() == 0 ? null : Integer.parseInt(getKey(uomDataMap, rcmnduomSpin.getSelectedItem().toString())));
                        mNutrientModel.setRecommendedUOMId(rcmnduomperSpin.getSelectedItemPosition() == 0 ? null : Integer.parseInt(getKey(rcmnduomperDatamap, rcmnduomperSpin.getSelectedItem().toString())));

                        mNutrientModel.setRecommendDosage(TextUtils.isEmpty(rcmndosageEdt.getText().toString()) == true ? 0.0 : Double.parseDouble(rcmndosageEdt.getText().toString()));

                        mNutrientModelArray.add(mNutrientModel);

                        addingValues();

                        nutritionSpin.setSelection(0);
                        nameOfChemicalUsedSpin.setSelection(0);
                        percentageOfTreesSpn.setSelection(0);
                        rcmnduomSpin.setSelection(0);
                        rcmnduomperSpin.setSelection(0);
                        rcmndfertilizerProductNameSpin.setSelection(0);
                        DataManager.getInstance().addData(DataManager.NUTRIENT_DETAILS, mNutrientModelArray);
                        CommonUtilsNavigation.hideKeyBoard(getActivity());
                        nutrientDataAdapter.notifyDataSetChanged();
                        commentsEdt.setText("");
                        rcmndosageEdt.setText("");
                        updateUiListener.updateUserInterface(0);


                    }
                    Log.v("@@@ adda value 390", "" + ratingList.size());
                }

                break;
//            case R.id.historyBtn:
//                CropMaintainanceHistoryFragment newFragment = new CropMaintainanceHistoryFragment();
//                Bundle bundle = new Bundle();
//                bundle.putInt("screen", NUTRIENT_DATA);
//                newFragment.setArguments(bundle);
//                newFragment.show(getActivity().getFragmentManager(), "history");
//
//                break;

        }
    }

    public boolean validateFields() {
        return spinnerSelect(nutritionSpin, "Name of Nutrient", mContext)
                && spinnerSelect(nameOfChemicalUsedSpin, "Name of Fertilizer Used", mContext)
                && spinnerSelect(percentageOfTreesSpn, "Percentage of tree", mContext);

    }

    public boolean validateFields1() {
        return spinnerSelect(nutritionSpin, "Name of Nutrient", mContext);

    }

    AdapterView.OnItemSelectedListener spinListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId())

            {
                case R.id.nutritionSpin:
                    if (position == 0) {
                        nameOfChemicalUsedSpin.setSelection(0);
                        nameOfChemicalUsedSpin.setEnabled(false);
                        percentageOfTreesSpn.setSelection(0);
                        percentageOfTreesSpn.setEnabled(false);
                        rcmndfertilizerProductNameSpin.setSelection(0);
                        rcmndfertilizerProductNameSpin.setEnabled(false);
                        rcmndosageEdt.setEnabled(false);
                        rcmnduomSpin.setSelection(0);
                        rcmnduomSpin.setEnabled(false);
                        rcmnduomperSpin.setSelection(0);
                        rcmnduomperSpin.setEnabled(false);
                        commentsEdt.setEnabled(false);
                        commentsEdt.setText("");
                        rcmndosageEdt.setText("");
                        saveBtn.setEnabled(false);
                        saveBtn.setAlpha(0.5f);
                    }else if (position == 9) {
                        nameOfChemicalUsedSpin.setSelection(0);
                        nameOfChemicalUsedSpin.setEnabled(false);
                        percentageOfTreesSpn.setSelection(0);
                        percentageOfTreesSpn.setEnabled(false);
                        rcmndfertilizerProductNameSpin.setSelection(0);
                        rcmndfertilizerProductNameSpin.setEnabled(false);
                        rcmndosageEdt.setEnabled(false);
                        rcmnduomSpin.setSelection(0);
                        rcmnduomSpin.setEnabled(false);
                        rcmnduomperSpin.setSelection(0);
                        rcmnduomperSpin.setEnabled(false);
                        commentsEdt.setEnabled(true);
                        saveBtn.setEnabled(true);
                        commentsEdt.setText("");
                        rcmndosageEdt.setText("");
                        saveBtn.setAlpha(1.0f);

                    } else {
                        nameOfChemicalUsedSpin.setEnabled(true);
                        percentageOfTreesSpn.setEnabled(true);
                        rcmndfertilizerProductNameSpin.setEnabled(true);
                        rcmndosageEdt.setEnabled(true);
                        rcmnduomSpin.setEnabled(true);
                        rcmnduomperSpin.setEnabled(true);
                        commentsEdt.setEnabled(true);
                        saveBtn.setEnabled(true);
                        saveBtn.setAlpha(1.0f);
                    }

                    break;
//                case R.id.percOfTreeSpin:
//
//
//                    break;
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
    @Override
    public void onEditClicked(int position) {
        Log.v("@@@ adda value", "" + ratingList.size());

        if (position >= 0 && position < mNutrientModelArray.size() && position < ratingList.size()) {
            mNutrientModelArray.remove(position);
            ratingList.remove(position);

            if (mNutrientModelArray.isEmpty()) {
                CommonConstants.perc_tree = ' ';
            }

            // Ensure ratingList is not empty before accessing elements
            if (!ratingList.isEmpty()) {
                if (mNutrientModelArray.size() == 1) {
                    CommonConstants.perc_tree = ratingList.get(0); // Access only if list is not empty
                } else if (mNutrientModelArray.size() > 1) {
                    Log.v("@@@ adda value", "" + ratingList.size());
                    for (int i = 0; i < ratingList.size() - 1; i++) {
                        for (int j = i + 1; j < ratingList.size(); j++) {
                            Log.v("@@@ adda value", "" + ratingList.get(i));

                            if (ratingList.get(i) > ratingList.get(j)) {
                                CommonConstants.perc_tree = ratingList.get(i);
                                Log.v("@@@ adda value", "" + CommonConstants.perc_tree);
                            } else {
                                CommonConstants.perc_tree = ratingList.get(j);
                                Log.v("@@@ add value", "" + CommonConstants.perc_tree);
                            }
                        }
                    }
                }
            } else {
                // Handle the case where ratingList is empty
                Log.e("@@@ Error", "ratingList is empty, cannot process values");
            }

            Log.v("@@@cheecking the Values", "" + CommonConstants.perc_tree + " size " + mNutrientModelArray.size());

            // Notify adapter about the change
            nutrientDataAdapter.notifyDataSetChanged();
            updateUiListener.updateUserInterface(0);

        }
    }


//    @Override
//    public void onEditClicked(int position) {
//        Log.v("@@@ adda value", "" + ratingList.size());
//        if (position >= 0 && position < mNutrientModelArray.size() && position < ratingList.size()) {
//        mNutrientModelArray.remove(position);
//     ratingList.remove(position);
//
//        if (mNutrientModelArray.isEmpty()) {
//            CommonConstants.perc_tree = ' ';
//        }
//
//        if (mNutrientModelArray.size() == 1) {
//            CommonConstants.perc_tree = ratingList.get(0);
//        } else if (mNutrientModelArray.size() > 1) {
//
//            Log.v("@@@ adda value", "" + ratingList.size());
//            for (int i = 0; i < ratingList.size() - 1; i++) {
//                for (int j = i + 1; j < ratingList.size(); j++) {
//
//                    Log.v("@@@ adda value", "" + ratingList.get(i));
//
//                    if (ratingList.get(i) > ratingList.get(j)) {
//                        CommonConstants.perc_tree = ratingList.get(i);
//                        Log.v("@@@ adda value", "" + CommonConstants.perc_tree);
//                    } else {
//                        CommonConstants.perc_tree = ratingList.get(j);
//                        Log.v("@@@ add value", "" + CommonConstants.perc_tree);
//                    }
//                }
//            }
//        }
//
//        Log.v("@@@cheecking the Values", "" + CommonConstants.perc_tree + " size " + mNutrientModelArray.size());
//
//        nutrientDataAdapter.notifyDataSetChanged();
//
//}
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

