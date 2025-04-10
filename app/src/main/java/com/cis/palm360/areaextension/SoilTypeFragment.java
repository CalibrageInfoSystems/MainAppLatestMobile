package com.cis.palm360.areaextension;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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
import com.cis.palm360.cloudhelper.Log;
import com.cis.palm360.common.CommonConstants;
import com.cis.palm360.common.CommonUtils;
import com.cis.palm360.cropmaintenance.CommonUtilsNavigation;
import com.cis.palm360.database.DataAccessHandler;
import com.cis.palm360.database.DatabaseKeys;
import com.cis.palm360.database.Queries;
import com.cis.palm360.datasync.helpers.DataManager;
import com.cis.palm360.dbmodels.PlotIrrigationTypeXref;
import com.cis.palm360.dbmodels.SoilResource;
import com.cis.palm360.utils.UiUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;


//Soil/Power Type Entry Fragment
public class SoilTypeFragment extends Fragment implements SoilTypeAdapter.OnCartChangedListener, EditEntryDialogFragment.OnDataEditChangeListener {

    private View rootView;
    private ActionBar actionBar;
    private Spinner soiltype, plotprioritizationSpin, typeofirrigationSpin,soilNatureType,irrigationRecSpn;
    private EditText noofhourspowerEdit, water_commentsEdit,irrigatedArea;
    private Button  irigationSaveBtn, saveBtn,soilhistoryBtn;
    private RecyclerView mRecyclerView;
    private SoilTypeAdapter mSoilTypeAdapter;
    private SoilIrrigationAdapter SoilTypeAdapter;
    private int selectedPosition = 0;
    private Spinner powerAvailSpin;
    private LinkedHashMap<String, String> soilTypeMap, typeofirrigationMap, plotPrioritizationMap,soilNatureTypeMap;
    private DataAccessHandler dataAccessHandler;
    private SoilResource msoilTypeModel;
    private ArrayList<PlotIrrigationTypeXref> msoilTypeIrrigationModelList = new ArrayList<>();
    private boolean updateFromDb;
    private UpdateUiListener updateUiListener;
    private ArrayList<SoilResource> SoilResourcelastvisitdatamap;
    public SoilTypeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_soil_type, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        actionBar = activity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(activity.getResources().getString(R.string.water_soil_power_details));

        initViews();
        setViews();

        return rootView;
    }

    private void initViews() {
        dataAccessHandler = new DataAccessHandler(getActivity());

        soiltype = (Spinner) rootView.findViewById(R.id.soiltype);
        plotprioritizationSpin = (Spinner) rootView.findViewById(R.id.plotprioritizationSpin);
        typeofirrigationSpin = (Spinner) rootView.findViewById(R.id.typeofirrigationSpin);
        powerAvailSpin = (Spinner) rootView.findViewById(R.id.poweravailSpin);
        noofhourspowerEdit = (EditText) rootView.findViewById(R.id.noofhourspowerEdit);
        water_commentsEdit = (EditText) rootView.findViewById(R.id.water_commentsEdit);
        irigationSaveBtn = (Button) rootView.findViewById(R.id.irigationSaveBtn);
        saveBtn = (Button) rootView.findViewById(R.id.saveBtn);
        soilhistoryBtn = (Button) rootView.findViewById(R.id.soilhistoryBtn);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        irrigatedArea = rootView.findViewById(R.id.irrigatedArea);
        soilNatureType = rootView.findViewById(R.id.soilNatureType);
        irrigationRecSpn = rootView.findViewById(R.id.irrigationRecSpn);
        soilhistoryBtn.setVisibility(CommonUtils.isFromCropMaintenance() ? View.VISIBLE : View.GONE);
        //soilhistoryBtn.setVisibility(CommonUtils.isFromConversion() ? View.GONE : View.VISIBLE);
    }

    private void setViews() {
        soilTypeMap = dataAccessHandler.getGenericData(Queries.getInstance().getTypeCdDmtData("35"));
        soilNatureTypeMap = dataAccessHandler.getGenericData(Queries.getInstance().getTypeCdDmtData("54"));
        typeofirrigationMap = dataAccessHandler.getGenericData(Queries.getInstance().getTypeCdDmtData("36"));
        plotPrioritizationMap = dataAccessHandler.getGenericData(Queries.getInstance().getTypeCdDmtData("37"));

        soiltype.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "SoilType", soilTypeMap));
        soilNatureType.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "SoilNatureType", soilNatureTypeMap));
        plotprioritizationSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Prioritization", plotPrioritizationMap));
        selectedPosition = 0;

        msoilTypeIrrigationModelList = (ArrayList<PlotIrrigationTypeXref>) DataManager.getInstance().getDataFromManager(DataManager.TypeOfIrrigation);
//        if (msoilTypeIrrigationModelList == null && (isFromFollowUp() || isFromCropMaintenance() || isFromConversion())) {
//            msoilTypeIrrigationModelList = (ArrayList<PlotIrrigationTypeXref>) dataAccessHandler.getPlotIrrigationXRefData(Queries.getInstance().getPlotIrrigationTypeXrefBinding(CommonConstants.PLOT_CODE), 1);
//        }

        if (msoilTypeIrrigationModelList == null || msoilTypeIrrigationModelList.isEmpty())
            msoilTypeIrrigationModelList = new ArrayList<>();
        else{
            Log.e("=====msoilTypeIrrigationModelList",msoilTypeIrrigationModelList.size()+"");
            for (PlotIrrigationTypeXref mPlotIrrigationTypeXref : msoilTypeIrrigationModelList) {
                typeofirrigationMap.remove(String.valueOf(mPlotIrrigationTypeXref.getIrrigationtypeid()));
            }
            saveBtn.setVisibility(View.VISIBLE);
        }
        typeofirrigationSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Type", typeofirrigationMap));
        irrigationRecSpn.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Type", typeofirrigationMap));



        mRecyclerView.setHasFixedSize(true);
        mSoilTypeAdapter = new SoilTypeAdapter(getActivity(), msoilTypeIrrigationModelList,typeofirrigationMap);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mSoilTypeAdapter);
        mSoilTypeAdapter.setOnCartChangedListener(this);

        msoilTypeModel = (SoilResource) DataManager.getInstance().getDataFromManager(DataManager.SoilType);
//        if (msoilTypeModel == null && (isFromFollowUp() || isFromCropMaintenance() || isFromConversion())) {
//            updateFromDb = true;
//            msoilTypeModel = (SoilResource) dataAccessHandler.getSoilResourceData(Queries.getInstance().getSoilResourceBinding(CommonConstants.PLOT_CODE), 0);
//        }

        if (msoilTypeModel != null) {
            soiltype.setSelection(CommonUtilsNavigation.getvalueFromHashMap(soilTypeMap, msoilTypeModel.getSoiltypeid()));
            powerAvailSpin.setSelection((null != msoilTypeModel.getIspoweravailable() && msoilTypeModel.getIspoweravailable() == 1) ? 1 : 2);
            noofhourspowerEdit.setText("" + msoilTypeModel.getAvailablepowerhours());
            plotprioritizationSpin.setSelection(msoilTypeModel.getPrioritizationtypeid() == null ? 0 : CommonUtilsNavigation.getvalueFromHashMap(plotPrioritizationMap, msoilTypeModel.getPrioritizationtypeid()));
            water_commentsEdit.setText(msoilTypeModel.getComments());
            irrigatedArea.setText("" + msoilTypeModel.getIrrigatedArea());
            soilNatureType.setSelection(CommonUtilsNavigation.getvalueFromHashMap(soilNatureTypeMap, msoilTypeModel.getSoilNatureId()));
        }

        irigationSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeofirrigationSpin.getSelectedItemPosition() != 0 && irrigationRecSpn.getSelectedItemPosition()!=0) {
                    PlotIrrigationTypeXref msoilTypeIrrigationModel = new PlotIrrigationTypeXref();
                    msoilTypeIrrigationModel.setIrrigationtypeid(Integer.parseInt(CommonUtilsNavigation.getKey(typeofirrigationMap, typeofirrigationSpin.getSelectedItem().toString())));
                    msoilTypeIrrigationModel.setName(typeofirrigationSpin.getSelectedItem().toString());
                    msoilTypeIrrigationModel.setRecmIrrgId(Integer.parseInt(CommonUtilsNavigation.getKey(typeofirrigationMap, irrigationRecSpn.getSelectedItem().toString())));
                    Log.v("@@@id",""+msoilTypeIrrigationModel.getRecmIrrgId());

                    msoilTypeIrrigationModelList.add(msoilTypeIrrigationModel);
                    DataManager.getInstance().addData(DataManager.TypeOfIrrigation, msoilTypeIrrigationModelList);
                    mSoilTypeAdapter.notifyDataSetChanged();
                    typeofirrigationSpin.setSelection(0);
                    irrigationRecSpn.setSelection(0);

                } else
                    UiUtils.showCustomToastMessage("Please Select Irrigation Type and Recommended Irrigation", getContext(), 0);

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                try {
                    if (!TextUtils.isEmpty(noofhourspowerEdit.getText().toString()) && Double.parseDouble(noofhourspowerEdit.getText().toString()) > 24){
                        CommonUtils.showToast(getString(R.string.error_exceed24), getActivity());
                        return;
                    }
                }catch (Exception e){

                }


                if (CommonUtilsNavigation.spinnerSelect("Soil Type", soiltype.getSelectedItemPosition(), getActivity())
                        &&CommonUtilsNavigation.spinnerSelect("Soil Nature Type", soilNatureType.getSelectedItemPosition(), getActivity())
                        && CommonUtilsNavigation.spinnerSelect("Power Availability", powerAvailSpin.getSelectedItemPosition(), getActivity())
                        && CommonUtilsNavigation.edittextSelect(getActivity(),irrigatedArea,"Irrigated area")
                        && CommonUtilsNavigation.listEmpty(msoilTypeIrrigationModelList, "Irrigation Details", getActivity())) {

                        msoilTypeModel = new SoilResource();
                        msoilTypeModel.setSoiltypeid(Integer.parseInt(CommonUtilsNavigation.getKey(soilTypeMap, soiltype.getSelectedItem().toString())));
                        msoilTypeModel.setIspoweravailable(powerAvailSpin.getSelectedItemPosition() == 1 ? 1 : 0);
                        msoilTypeModel.setAvailablepowerhours(noofhourspowerEdit.getText().toString().length() > 0 ? Double.parseDouble(noofhourspowerEdit.getText().toString()) : 0.0);
                        msoilTypeModel.setPrioritizationtypeid(plotprioritizationSpin.getSelectedItemPosition() == 0 ? null : Integer.parseInt(CommonUtilsNavigation.getKey(plotPrioritizationMap, plotprioritizationSpin.getSelectedItem().toString())));
                        msoilTypeModel.setComments(water_commentsEdit.getText().toString());
                        msoilTypeModel.setSoilNatureId(Integer.parseInt(CommonUtilsNavigation.getKey(soilNatureTypeMap, soilNatureType.getSelectedItem().toString())));
                        msoilTypeModel.setIrrigatedArea(Float.parseFloat(irrigatedArea.getText().toString()));
                        DataManager.getInstance().addData(DataManager.SoilType, msoilTypeModel);
                        soiltype.setEnabled(false);
                        powerAvailSpin.setEnabled(false);
                        noofhourspowerEdit.setEnabled(false);
                        plotprioritizationSpin.setEnabled(false);
                        water_commentsEdit.setEnabled(false);

//                        if (updateFromDb) {
//                            DataManager.getInstance().addData(DataManager.IS_WOP_DATA_UPDATED, true);
//                        }
//                        CommonConstants.Flags.isWOPDataUpdated = true;
                      updateUiListener.updateUserInterface(0);
                        getFragmentManager().popBackStack();

                }


            }
        });

        soilhistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(getContext());
            }
        });
    }
    public void showDialog(Context activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.soillastvisteddata);

        Toolbar titleToolbar = dialog.findViewById(R.id.titleToolbar);
        if (titleToolbar != null) {
            titleToolbar.setTitle("Soil & Power Details History");
            titleToolbar.setTitleTextColor(activity.getResources().getColor(R.color.white));
        }

        RecyclerView irrigationRecyclerView = dialog.findViewById(R.id.irrigationRecyclerView);
        TextView soilnaturetype = dialog.findViewById(R.id.soilnaturetype);
        TextView soiltype = dialog.findViewById(R.id.soiltype);
        TextView availablehours = dialog.findViewById(R.id.availablehours);
        TextView poweravilable = dialog.findViewById(R.id.poweravilable);
        TextView plotpriortization = dialog.findViewById(R.id.plotpriortization);

        TextView irrigatedArea = dialog.findViewById(R.id.irrigatedArea);
        TextView Comments = dialog.findViewById(R.id.Comments);

        TextView norecords = dialog.findViewById(R.id.intercropnorecord_tv);
        LinearLayout mainLL = dialog.findViewById(R.id.intercropmainlyt);

        String lastVisitCode = dataAccessHandler.getOnlyOneValueFromDb(
                Queries.getInstance().getLatestCropMaintanaceHistoryCode(CommonConstants.PLOT_CODE)
        );
        android.util.Log.e("lastVisitCode", lastVisitCode + "");
        SoilResourcelastvisitdatamap = (ArrayList<SoilResource>) dataAccessHandler.getSoilResourceData(Queries.getInstance().getRecommndCropMaintenanceHistoryData(lastVisitCode, DatabaseKeys.TABLE_SOILRESOURCE), 1);
        android.util.Log.e("lastVisitCode SoilResource query", Queries.getInstance().getRecommndCropMaintenanceHistoryData(lastVisitCode, DatabaseKeys.TABLE_SOILRESOURCE)+ "");

//        if (lastVisitCode != null) {
            msoilTypeIrrigationModelList = (ArrayList<PlotIrrigationTypeXref>) dataAccessHandler.getPlotIrrigationXRefData(Queries.getInstance().getRecommndCropMaintenanceHistoryData(lastVisitCode, DatabaseKeys.TABLE_PLOTIRRIGATIONTYPEXREF), 1);
        android.util.Log.e("lastVisitCode Irrigation query", Queries.getInstance().getRecommndCropMaintenanceHistoryData(lastVisitCode, DatabaseKeys.TABLE_PLOTIRRIGATIONTYPEXREF)+ "");
            msoilTypeModel = (SoilResource) dataAccessHandler.getSoilResourceData(
                    Queries.getInstance().getSoilResourceBinding(CommonConstants.PLOT_CODE), 0
            );

            if (SoilResourcelastvisitdatamap != null && !SoilResourcelastvisitdatamap.isEmpty()) {
                SoilResource soilResource = SoilResourcelastvisitdatamap.get(0);

                String soil_type = soilResource != null
                        ? dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().gettypcdmtdata(soilResource.getSoiltypeid()))
                        : null;
                if (soiltype != null && soil_type != null) soiltype.setText(soil_type);

                String soilnature_type = soilResource != null
                        ? dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().gettypcdmtdata(soilResource.getSoilNatureId()))
                        : null;
                if (soilnaturetype != null && soilnature_type != null) soilnaturetype.setText(soilnature_type);

                String priortization = soilResource.getPrioritizationtypeid() != null ? dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().gettypcdmtdata(soilResource.getPrioritizationtypeid())) : null;
                if (plotpriortization != null && priortization != null) plotpriortization.setText(priortization);

                if (availablehours != null && soilResource != null) {
                    availablehours.setText(String.valueOf(soilResource.getAvailablepowerhours()));
                }
                Log.e("Ispoweravailable=========>", soilResource.getIspoweravailable() + "");
                if (soilResource.getIspoweravailable() != null && soilResource != null) {
                    Log.e("Ispoweravailable=========>", soilResource.getIspoweravailable() + "");
                    poweravilable.setText(soilResource.getIspoweravailable() == 1 ? "Yes" : "No");
                }
                else{
                    poweravilable.setText("No");
                }

                if (irrigatedArea != null && soilResource != null) {
                    irrigatedArea.setText(String.valueOf(soilResource.getIrrigatedArea()));
                }

                if (Comments != null && soilResource != null && soilResource.getComments() != null) {
                    Comments.setText(soilResource.getComments());
                }
            }

            if (msoilTypeIrrigationModelList != null && !msoilTypeIrrigationModelList.isEmpty()) {
                if (norecords != null) norecords.setVisibility(View.GONE);
                if (mainLL != null) mainLL.setVisibility(View.VISIBLE);

                SoilTypeAdapter = new SoilIrrigationAdapter(activity, msoilTypeIrrigationModelList, typeofirrigationMap);
                if (irrigationRecyclerView != null) {
                    irrigationRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
                    irrigationRecyclerView.setAdapter(SoilTypeAdapter);
                }
            } else {
                if (mainLL != null) mainLL.setVisibility(View.GONE);
                if (norecords != null) norecords.setVisibility(View.VISIBLE);
            }
     //   }

        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Any delayed operations
            }
        }, 500);
    }




    @Override
    public void setCartClickListener(String clickItem, final int selectPos) {
        if (clickItem.equalsIgnoreCase("edit")) {
            EditEntryDialogFragment editEntryDialogFragment = new EditEntryDialogFragment();
            editEntryDialogFragment.setOnDataEditChangeListener(this);
            Bundle inputBundle = new Bundle();
            selectedPosition = selectPos;
            inputBundle.putString("title", "Irrigation Type");

            inputBundle.putInt("typeDialog", EditEntryDialogFragment.TYPE_SPINNER_IRIGATION_TYPE);
            inputBundle.putString("prevData", msoilTypeIrrigationModelList.get(selectedPosition).getName() + "-" + getString(R.string.typeofirrigation) + (selectedPosition + 1));

            editEntryDialogFragment.setArguments(inputBundle);
            FragmentManager mFragmentManager = getChildFragmentManager();
            editEntryDialogFragment.show(mFragmentManager, "fragment_edit_name");
        } else if (clickItem.equalsIgnoreCase("delete")) {
            msoilTypeIrrigationModelList.remove(selectPos);
            mSoilTypeAdapter.notifyDataSetChanged();
            DataManager.getInstance().addData(DataManager.TypeOfIrrigation, msoilTypeIrrigationModelList);
        }
    }

    @Override
    public void onDataEdited(Bundle dataBundle) {
        PlotIrrigationTypeXref msoilTypeIrrigationModel = new PlotIrrigationTypeXref();
        msoilTypeIrrigationModel.setName("" + dataBundle.getString("inputValue"));
        msoilTypeIrrigationModelList.set(selectedPosition, msoilTypeIrrigationModel);

        mSoilTypeAdapter.notifyDataSetChanged();

    }



    public void setUpdateUiListener(UpdateUiListener updateUiListener) {
        this.updateUiListener = updateUiListener;
    }
}
