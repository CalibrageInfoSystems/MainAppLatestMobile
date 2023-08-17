package com.oilpalm3f.mainapp.cropmaintenance;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.oilpalm3f.mainapp.R;
import com.oilpalm3f.mainapp.areaextension.UpdateUiListener;
import com.oilpalm3f.mainapp.cloudhelper.Log;
import com.oilpalm3f.mainapp.common.CommonConstants;
import com.oilpalm3f.mainapp.common.CommonUtils;
import com.oilpalm3f.mainapp.database.DataAccessHandler;
import com.oilpalm3f.mainapp.database.DatabaseKeys;
import com.oilpalm3f.mainapp.database.Queries;
import com.oilpalm3f.mainapp.datasync.helpers.DataManager;
import com.oilpalm3f.mainapp.dbmodels.Nutrient;
import com.oilpalm3f.mainapp.dbmodels.Uprootment;
import com.oilpalm3f.mainapp.ui.BaseFragment;
import com.oilpalm3f.mainapp.utils.UiUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static com.oilpalm3f.mainapp.cropmaintenance.CommonUtilsNavigation.getKey;

/**
 * Created by CHENCHAIAH on 5/27/2017.
 */

//Current Plantation Screen shows saplings planted
public class CurrentPlantationFragment extends Fragment {
    private TextView Noofsaplingsplanted_text, countoftreespreviousvisit_text, missingtrees_text, noofmissingtrees_text, comments_text, expectedTreecount_visit;
    private TextView comments_tv;
    private EditText counttresscurrentvisitEdt, comment_edit, gapfillingsaplingcount;
    private Spinner reasonformissing;
    private View rootView;
    private Context mContext;
    private Button savebtn;
    private DataAccessHandler dataAccessHandler;
    private UpdateUiListener updateUiListener;
    private LinkedHashMap<String, String> reasonDataMap;
    private Uprootment mUprootmentModel;
    private String treesCount, preCount;
    private int saplingsCount = 0;
    private ActionBar actionBar;
    private String gapFillingTreeCount, expecetedTreesCount;
    private String[] expecetTreeCount;
    private int missingTrees = 0;
    private LinearLayout reasonformissingtreesLL;

    private int gapfillingtresscount = 0;
    Spinner requiredspinner;
    private LinearLayout gapfillinglinear,gapfillingcountlinear;
    private LinkedHashMap<String, String> requiredgapfilling;
//    private int expecetedTreesCount = 0;

    private Button historyBtn;
    private ArrayList<Uprootment> currentplantationlastvisitdatamap;


    public CurrentPlantationFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.currentplantation_layout, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        actionBar = activity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle(getString(R.string.current_plantation));

        mContext = getActivity();
        setHasOptionsMenu(true);

        dataAccessHandler = new DataAccessHandler(getActivity());
        reasonDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getLookUpData("4"));
        treesCount = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().querySumOfSaplings(CommonConstants.PLOT_CODE));
        try {
            expecetTreeCount = dataAccessHandler.getOnlyTwoValueFromDb(Queries.getInstance().getExpectedTreeCount(CommonConstants.PLOT_CODE)).split("@");

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (expecetTreeCount != null) {
            gapFillingTreeCount = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getGapFillingTreeCount(CommonConstants.PLOT_CODE, expecetTreeCount[1]));
            if (!TextUtils.isEmpty(gapFillingTreeCount)) {
                expecetedTreesCount = String.valueOf(Integer.parseInt(expecetTreeCount[0]) + Integer.parseInt(gapFillingTreeCount));

            } else {
                expecetedTreesCount = expecetTreeCount[0];
            }
        } else {
            expecetedTreesCount = treesCount;
        }


        initViews();
        bindData();
        setViews();
        if (expecetedTreesCount != null)
//            saplingsCount = CommonUtils.convertToBigNumber(treesCount);
            saplingsCount = CommonUtils.convertToBigNumber(expecetedTreesCount);
        return rootView;
    }


//    private void bindData() {
//        mUprootmentModel = (Uprootment) DataManager.getInstance().getDataFromManager(DataManager.CURRENT_PLANTATION);
//        if (mUprootmentModel != null) {
//            counttresscurrentvisitEdt.setText("" + mUprootmentModel.getPlamscount());
//            noofmissingtrees_text.setText("" + mUprootmentModel.getMissingtreescount());
//            missingtrees_text.setText(mUprootmentModel.getIstreesmissing() == 1 ? "Yes" : "No");
//            reasonformissing.setSelection(mUprootmentModel.getReasontypeid() == null ? 0 : CommonUtilsNavigation.getvalueFromHashMap(reasonDataMap, mUprootmentModel.getReasontypeid()));
//            comment_edit.setText("" + mUprootmentModel.getComments());
//            preCount = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().queryGetCountOfPreviousTrees(CommonConstants.PLOT_CODE));
//            if (!TextUtils.isEmpty(preCount)) {
//                countoftreespreviousvisit_text.setText(preCount);
//            } else {
//                countoftreespreviousvisit_text.setText(treesCount);
//            }
//        } else {
//            preCount = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().queryGetCountOfPreviousTrees(CommonConstants.PLOT_CODE));
//            if (!TextUtils.isEmpty(preCount)) {
//                countoftreespreviousvisit_text.setText(preCount);
//            } else {
//                countoftreespreviousvisit_text.setText(treesCount);
//            }
//        }
//    }

    private void bindData() {
        mUprootmentModel = (Uprootment) DataManager.getInstance().getDataFromManager(DataManager.CURRENT_PLANTATION);
        if (mUprootmentModel != null) {
            counttresscurrentvisitEdt.setText("" + mUprootmentModel.getPlamscount());
            noofmissingtrees_text.setText("" + mUprootmentModel.getMissingtreescount());

            missingTrees = mUprootmentModel.getMissingtreescount();
            missingtrees_text.setText(mUprootmentModel.getIstreesmissing() == 1 ? "Yes" : "No");
            if( mUprootmentModel.getMissingtreescount()!=0) {
                reasonformissingtreesLL.setVisibility(View.VISIBLE);
                gapfillinglinear.setVisibility(View.VISIBLE);

                reasonformissing.setSelection(mUprootmentModel.getReasontypeid() == null ? 0 : CommonUtilsNavigation.getvalueFromHashMap(reasonDataMap, mUprootmentModel.getReasontypeid()));

                requiredspinner.setSelection(mUprootmentModel.getIsGapFillingRequired() == null ? 0 : CommonUtilsNavigation.getvalueFromHashMap(requiredgapfilling, mUprootmentModel.getIsGapFillingRequired()));
                gapfillingsaplingcount.setText(mUprootmentModel.getGapFillingSaplingsCount() + "");
            }else{
                reasonformissingtreesLL.setVisibility(View.GONE);
                gapfillinglinear.setVisibility(View.GONE);
            }
            comment_edit.setText("" + mUprootmentModel.getComments());
            preCount = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().queryGetCountOfPreviousTrees(CommonConstants.PLOT_CODE));
            if (!TextUtils.isEmpty(preCount)) {
                countoftreespreviousvisit_text.setText(preCount);
            } else {
                countoftreespreviousvisit_text.setText(treesCount);
            }
        } else {
            preCount = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().queryGetCountOfPreviousTrees(CommonConstants.PLOT_CODE));
            if (!TextUtils.isEmpty(preCount)) {
                countoftreespreviousvisit_text.setText(preCount);
            } else {
                countoftreespreviousvisit_text.setText(treesCount);
            }
        }

    }

    private void initViews() {

        Noofsaplingsplanted_text = (TextView) rootView.findViewById(R.id.saplingplanted_text);
        Noofsaplingsplanted_text.setText(treesCount);
        expectedTreecount_visit = rootView.findViewById(R.id.expectedTreecountvisit);
        expectedTreecount_visit.setText(expecetedTreesCount);
        countoftreespreviousvisit_text = (TextView) rootView.findViewById(R.id.countoftreesvisit_text);
        counttresscurrentvisitEdt = (EditText) rootView.findViewById(R.id.counttresscurrentvisitEdt);
        missingtrees_text = (TextView) rootView.findViewById(R.id.missingtrees_text);
        noofmissingtrees_text = (TextView) rootView.findViewById(R.id.no_of_missing_treesTV);
        reasonformissing = (Spinner) rootView.findViewById(R.id.reason_for_missing_treesSpin);
        reasonformissingtreesLL = rootView.findViewById(R.id.reasonformissingtreesLL);
        comment_edit = (EditText) rootView.findViewById(R.id.commentsEdit);
        savebtn = (Button) rootView.findViewById(R.id.SaveBtn);
        comments_tv = (TextView) rootView.findViewById(R.id.comments_tv);

        historyBtn = (Button) rootView.findViewById(R.id.currentplantationlastvisitdataBtn);

        reasonformissing.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(mContext, "Reason", reasonDataMap));

        gapfillingsaplingcount = (EditText) rootView.findViewById(R.id.gapfillingcount);
        gapfillinglinear =(LinearLayout)rootView.findViewById(R.id.gapfillinglinear);
        gapfillingcountlinear =(LinearLayout)rootView.findViewById(R.id.gapfillingcountlinear);
        requiredspinner = (Spinner)rootView.findViewById(R.id.requiredspinner);
        requiredgapfilling  = dataAccessHandler.getGenericData(Queries.getInstance().getYesNo());
        requiredspinner.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Is Gap Filling Required? ", requiredgapfilling));

    }

//    private void setViews() {
//
//
//
//        counttresscurrentvisitEdt.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String count = s.toString();
//                int previusTrees = 0;
//                if (!s.toString().equalsIgnoreCase("")) {
//                    int currentTrees = CommonUtils.convertToBigNumber(s.toString());
//
//                    if (Integer.parseInt(expecetedTreesCount) > currentTrees) {
//                        missingtrees_text.setText("Yes");
//                        noofmissingtrees_text.setText("" + (Integer.parseInt(expecetedTreesCount) - currentTrees));
//                        reasonformissingtreesLL.setVisibility(View.VISIBLE);
//                        comments_tv.setText("Comments*");
//                        missingTrees = (Integer.parseInt(expecetedTreesCount) - currentTrees);
//                        Log.v("@@@missing", "" + missingTrees);
//                    } else {
//                        missingtrees_text.setText("No");
//                        noofmissingtrees_text.setText("0");
//                        comments_tv.setText("Comments");
//                        reasonformissingtreesLL.setVisibility(View.GONE);
//                        missingTrees = 0;
//                        Log.v("@@@missing", "" + missingTrees);
//                    }
//
//                } else {
//                    missingtrees_text.setText("No");
//                    noofmissingtrees_text.setText("0");
//                    missingTrees = 0;
//                    Log.v("@@@missing", "" + missingTrees);
//                }
//
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//        });
//
//        savebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                if (TextUtils.isEmpty(counttresscurrentvisitEdt.getText().toString())) {
//                    UiUtils.showCustomToastMessage("Please Enter Count Of Trees", mContext, 0);
//                    return;
//                }
//
//                if (missingTrees > 0) {
//                    if (TextUtils.isEmpty(comment_edit.getText().toString())) {
//                        UiUtils.showCustomToastMessage("Please Enter Comments", mContext, 0);
//                        return;
//                    }
//
//                }
//                CommonConstants.CURRENT_TREE = CommonUtils.convertToBigNumber(counttresscurrentvisitEdt.getText().toString());
//                mUprootmentModel = new Uprootment();
//                mUprootmentModel.setPlamscount(CommonUtils.convertToBigNumber(counttresscurrentvisitEdt.getText().toString()));
//                mUprootmentModel.setIstreesmissing(missingtrees_text.getText().toString().contains("Yes") ? 1 : 0);
//
//                if (TextUtils.isEmpty(noofmissingtrees_text.getText().toString())) {
//                    mUprootmentModel.setMissingtreescount(0);
//
//                } else {
//                    mUprootmentModel.setMissingtreescount(CommonUtils.convertToBigNumber(noofmissingtrees_text.getText().toString()));
//
//                }
//                mUprootmentModel.setReasontypeid(reasonformissing.getSelectedItemPosition() == 0 ? null :
//                        Integer.parseInt(getKey(reasonDataMap, reasonformissing.getSelectedItem().toString())));
//                mUprootmentModel.setComments(comment_edit.getText().toString());
//
//
//                if (treesCount != null) {
//                    mUprootmentModel.setSeedsplanted(Integer.parseInt(treesCount));
//                } else {
//                    mUprootmentModel.setSeedsplanted(0);
//                }
//                mUprootmentModel.setExpectedPlamsCount(CommonUtils.convertToBigNumber(expecetedTreesCount));
//                DataManager.getInstance().addData(DataManager.CURRENT_PLANTATION, mUprootmentModel);
//                CommonUtilsNavigation.hideKeyBoard(getActivity());
//                getFragmentManager().popBackStack();
//                updateUiListener.updateUserInterface(0);
//            }
//        });
//
//        historyBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                showDialog(getContext());
//            }
//        });
//
//    }

    private void setViews() {


        counttresscurrentvisitEdt.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                String count = s.toString();
                int previusTrees = 0;
                int currentTrees = 0;
                if (!s.toString().equalsIgnoreCase("")) {
                     currentTrees = CommonUtils.convertToBigNumber(s.toString());

                    currentTrees = CommonUtils.convertToBigNumber(s.toString());
                    if(currentTrees >Integer.parseInt(expecetedTreesCount)  ){
                        UiUtils.showCustomToastMessage("Expected Tree Count should be less than or equal to the Previous Tree Count", mContext, 0);
                        counttresscurrentvisitEdt.setText("");
                        return;
                    }

                    if (Integer.parseInt(expecetedTreesCount) > currentTrees) {
                        missingtrees_text.setText("Yes");
                        noofmissingtrees_text.setText("" + (Integer.parseInt(expecetedTreesCount) - currentTrees));
                        reasonformissingtreesLL.setVisibility(View.VISIBLE);
                        gapfillinglinear.setVisibility(View.VISIBLE);
                        comments_tv.setText("Comments*");
                        missingTrees = (Integer.parseInt(expecetedTreesCount) - currentTrees);
                        Log.v("@@@missing", "" + missingTrees);
                    } else {
                        missingtrees_text.setText("No");
                        noofmissingtrees_text.setText("0");
                        comments_tv.setText("Comments");
                        reasonformissingtreesLL.setVisibility(View.GONE);
                        gapfillinglinear.setVisibility(View.GONE);
                        missingTrees = 0;
                        Log.v("@@@missing", "" + missingTrees);
                    }

                } else {
                    missingtrees_text.setText("No");
                    noofmissingtrees_text.setText("0");
                    missingTrees = 0;
                    Log.v("@@@missing", "" + missingTrees);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        requiredspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (requiredspinner.getSelectedItemPosition() == 1){

                    gapfillingcountlinear.setVisibility(View.VISIBLE);
                }
                else{
                    gapfillingsaplingcount.setText("");
                    gapfillingcountlinear.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
//


        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("========missingTrees",missingTrees+"");
                if (TextUtils.isEmpty(counttresscurrentvisitEdt.getText().toString())) {
                    UiUtils.showCustomToastMessage("Please Enter Count Of Trees", mContext, 0);
                    return;
                }

                if (missingTrees > 0) {
                    if (requiredspinner.getSelectedItemPosition() == 0){
                        UiUtils.showCustomToastMessage("Please Select Is Gap Filling Required? ", mContext, 0);
                        return;
                    }
                    if (requiredspinner.getSelectedItemPosition() == 1) {
                        if (TextUtils.isEmpty(gapfillingsaplingcount.getText().toString())) {
                            UiUtils.showCustomToastMessage("Please Enter Gap Filling Saplings Count", mContext, 0);
                            return;
                        }
                        gapfillingtresscount   =  Integer.parseInt(gapfillingsaplingcount.getText().toString());
                        Log.e("========gapfillingtresscount",gapfillingtresscount+"");

                        if(missingTrees < gapfillingtresscount){
                            UiUtils.showCustomToastMessage("Gap filling sapling should be less than or equal to the Missing trees", mContext, 0);
                            return;

                        }

                        if(gapfillingtresscount == 0){
                            UiUtils.showCustomToastMessage("Gap filling sapling count should be greater than 0", mContext, 0);
                            return;
                        }
                    }


                    if (TextUtils.isEmpty(comment_edit.getText().toString())) {
                        UiUtils.showCustomToastMessage("Please Enter Comments", mContext, 0);
                        return;
                    }

                }

                CommonConstants.CURRENT_TREE = CommonUtils.convertToBigNumber(counttresscurrentvisitEdt.getText().toString());
                mUprootmentModel = new Uprootment();
                mUprootmentModel.setPlamscount(CommonUtils.convertToBigNumber(counttresscurrentvisitEdt.getText().toString()));
                mUprootmentModel.setIstreesmissing(missingtrees_text.getText().toString().contains("Yes") ? 1 : 0);

                if (TextUtils.isEmpty(noofmissingtrees_text.getText().toString())) {
                    mUprootmentModel.setMissingtreescount(0);

                } else {
                    mUprootmentModel.setMissingtreescount(CommonUtils.convertToBigNumber(noofmissingtrees_text.getText().toString()));

                }
                mUprootmentModel.setReasontypeid(reasonformissing.getSelectedItemPosition() == 0 ? null :
                        Integer.parseInt(getKey(reasonDataMap, reasonformissing.getSelectedItem().toString())));
                mUprootmentModel.setComments(comment_edit.getText().toString());


                if (treesCount != null) {
                    mUprootmentModel.setSeedsplanted(Integer.parseInt(treesCount));
                } else {
                    mUprootmentModel.setSeedsplanted(0);
                }
                mUprootmentModel.setExpectedPlamsCount(CommonUtils.convertToBigNumber(expecetedTreesCount));
                mUprootmentModel.setIsGapFillingRequired(requiredspinner.getSelectedItemPosition() == 0 ? null :
                        Integer.parseInt(getKey(requiredgapfilling, requiredspinner.getSelectedItem().toString())));

                //      mUprootmentModel.setGapFillingSaplingsCount(Integer.parseInt(gapfillingsaplingcount.getText().toString()));

//                if (TextUtils.isEmpty(noofmissingtrees_text.getText().toString())) {
//                    mUprootmentModel.setIsGapFillingRequired(requiredspinner.getSelectedItemPosition() == 0 ? null :
//                            Integer.parseInt(getKey(requiredgapfilling, requiredspinner.getSelectedItem().toString())));
//
//                } else {
//                    mUprootmentModel.setIsGapFillingRequired(requiredspinner.getSelectedItemPosition() == 0 ? null :
//                            Integer.parseInt(getKey(requiredgapfilling, requiredspinner.getSelectedItem().toString())));
//
//                }
                if (requiredspinner.getSelectedItemPosition() == 1){

                    mUprootmentModel.setGapFillingSaplingsCount(Integer.parseInt(gapfillingsaplingcount.getText().toString()));
                }
                else{
                    mUprootmentModel.setGapFillingSaplingsCount(null);
                }



                DataManager.getInstance().addData(DataManager.CURRENT_PLANTATION, mUprootmentModel);
                CommonUtilsNavigation.hideKeyBoard(getActivity());
                getFragmentManager().popBackStack();
                updateUiListener.updateUserInterface(0);
            }
        });

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
        dialog.setContentView(R.layout.cplastvisteddata);

        Toolbar titleToolbar;
        titleToolbar = (Toolbar) dialog.findViewById(R.id.titleToolbar);
        titleToolbar.setTitle("Current Plantation History");
        titleToolbar.setTitleTextColor(getResources().getColor(R.color.white));

        LinearLayout commentsll = (LinearLayout) dialog.findViewById(R.id.cpdatacommentsLL);
        LinearLayout reasonformissingtreesLL = (LinearLayout) dialog.findViewById(R.id.cpdatareasonformissingtreesLL);
        LinearLayout mainLL = (LinearLayout) dialog.findViewById(R.id.mainlyt);
        LinearLayout cpdatasaplingforgapfillingLL= (LinearLayout)dialog .findViewById(R.id.cpdatasaplingforgapfillingLL);
        LinearLayout cpdatagapfillrequiredlinear= (LinearLayout)dialog .findViewById(R.id.cpdatagapfillrequiredlinear);
        TextView cpdatagapfillrequired=(TextView)dialog.findViewById(R.id.cpdatagapfillrequired);
        TextView cpdatasaplingforgapfilling_tv=(TextView)dialog.findViewById(R.id.cpdatasaplingforgapfilling_tv);

        TextView saplingsplanted = (TextView) dialog.findViewById(R.id.cpdatasaplingplanted_text);
        TextView countoftressinpreviousvisit = (TextView) dialog.findViewById(R.id.cpdatacountoftreesvisit_text);
        TextView expectedtreecount = (TextView) dialog.findViewById(R.id.cpdataexpectedTreecountvisit);
        TextView tresscurrentlypresent = (TextView) dialog.findViewById(R.id.cpdatacounttresscurrentvisitEdt);
        TextView missingtress = (TextView) dialog.findViewById(R.id.cpdatamissingtrees_text);
        TextView nomissingtress = (TextView) dialog.findViewById(R.id.cpdatano_of_missing_treesTV);
        TextView reasonformissingtrees = (TextView) dialog.findViewById(R.id.cpdatareasonformissingtress);
        TextView comments = (TextView) dialog.findViewById(R.id.cpdatacomments_tv);
        TextView norecords = (TextView) dialog.findViewById(R.id.norecord_tv);

        String lastVisitCode = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getLatestCropMaintanaceHistoryCode(CommonConstants.PLOT_CODE));
        currentplantationlastvisitdatamap = (ArrayList<Uprootment>) dataAccessHandler.getUprootmentData(Queries.getInstance().getRecommndCropMaintenanceHistoryData(lastVisitCode, DatabaseKeys.TABLE_UPROOTMENT), 1);

        if (currentplantationlastvisitdatamap.size() > 0){
            norecords.setVisibility(View.GONE);
            mainLL.setVisibility(View.VISIBLE);



            String reason = null;
            String ismissing;

            if (currentplantationlastvisitdatamap.get(0).getIstreesmissing() == 1){
                ismissing = "Yes";
            }else{

                ismissing = "No";
            }

            if (currentplantationlastvisitdatamap.get(0).getReasontypeid() != null){


                reason = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getlookupdata(currentplantationlastvisitdatamap.get(0).getReasontypeid()));
            }
            if (currentplantationlastvisitdatamap.get(0).getReasontypeid() != null){


                reason = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getlookupdata(currentplantationlastvisitdatamap.get(0).getReasontypeid()));
            }

            saplingsplanted.setText(currentplantationlastvisitdatamap.get(0).getSeedsplanted() + "");
            countoftressinpreviousvisit.setText(currentplantationlastvisitdatamap.get(0).getExpectedPlamsCount() + "");
            expectedtreecount.setText(currentplantationlastvisitdatamap.get(0).getExpectedPlamsCount() + "");
            tresscurrentlypresent.setText(currentplantationlastvisitdatamap.get(0).getPlamscount() + "");
            missingtress.setText(ismissing);
            nomissingtress.setText(currentplantationlastvisitdatamap.get(0).getMissingtreescount() + "");

            if (currentplantationlastvisitdatamap.get(0).getReasontypeid() != null){
                reasonformissingtreesLL.setVisibility(View.VISIBLE);
                reasonformissingtrees.setText(reason);
            }else{
                reasonformissingtreesLL.setVisibility(View.GONE);
            }

            if (currentplantationlastvisitdatamap.get(0).getIsGapFillingRequired() != null){
                cpdatagapfillrequiredlinear.setVisibility(View.VISIBLE);
                String Required;
                if(currentplantationlastvisitdatamap.get(0).getIsGapFillingRequired() == 1){
                    Required = "Yes";
                    cpdatasaplingforgapfillingLL.setVisibility(View.VISIBLE);
                    cpdatasaplingforgapfilling_tv.setText(currentplantationlastvisitdatamap.get(0).getGapFillingSaplingsCount()+"");
                }
                else{

                    Required = "No";
                    cpdatasaplingforgapfillingLL.setVisibility(View.GONE);
                }
                cpdatagapfillrequired.setText(Required);

            }else{
                cpdatagapfillrequiredlinear.setVisibility(View.GONE);
            }

            Log.d("GETCOMMENTS",currentplantationlastvisitdatamap.get(0).getComments());

            //if (!TextUtils.isEmpty(currentplantationlastvisitdatamap.get(0).getComments()) || !currentplantationlastvisitdatamap.get(0).getComments().equalsIgnoreCase("")  || !currentplantationlastvisitdatamap.get(0).getComments().equalsIgnoreCase(null) || !currentplantationlastvisitdatamap.get(0).getComments().equalsIgnoreCase("null")){
            if (TextUtils.isEmpty(currentplantationlastvisitdatamap.get(0).getComments()) || currentplantationlastvisitdatamap.get(0).getComments().equalsIgnoreCase("")  || currentplantationlastvisitdatamap.get(0).getComments().equalsIgnoreCase(null) || currentplantationlastvisitdatamap.get(0).getComments().equalsIgnoreCase("null")){
                commentsll.setVisibility(View.GONE);
            }else{
                commentsll.setVisibility(View.VISIBLE);
                comments.setText(currentplantationlastvisitdatamap.get(0).getComments() + "");
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

//    public void showDialog(Context activity) {
//        final Dialog dialog = new Dialog(activity);
//        dialog.setCancelable(true);
//        dialog.setContentView(R.layout.cplastvisteddata);
//
//        Toolbar titleToolbar;
//        titleToolbar = (Toolbar) dialog.findViewById(R.id.titleToolbar);
//        titleToolbar.setTitle("Current Plantation History");
//        titleToolbar.setTitleTextColor(getResources().getColor(R.color.white));
//
//        LinearLayout commentsll = (LinearLayout) dialog.findViewById(R.id.cpdatacommentsLL);
//        LinearLayout reasonformissingtreesLL = (LinearLayout) dialog.findViewById(R.id.cpdatareasonformissingtreesLL);
//        LinearLayout mainLL = (LinearLayout) dialog.findViewById(R.id.mainlyt);
//
//        TextView saplingsplanted = (TextView) dialog.findViewById(R.id.cpdatasaplingplanted_text);
//        TextView countoftressinpreviousvisit = (TextView) dialog.findViewById(R.id.cpdatacountoftreesvisit_text);
//        TextView expectedtreecount = (TextView) dialog.findViewById(R.id.cpdataexpectedTreecountvisit);
//        TextView tresscurrentlypresent = (TextView) dialog.findViewById(R.id.cpdatacounttresscurrentvisitEdt);
//        TextView missingtress = (TextView) dialog.findViewById(R.id.cpdatamissingtrees_text);
//        TextView nomissingtress = (TextView) dialog.findViewById(R.id.cpdatano_of_missing_treesTV);
//        TextView reasonformissingtrees = (TextView) dialog.findViewById(R.id.cpdatareasonformissingtress);
//        TextView comments = (TextView) dialog.findViewById(R.id.cpdatacomments_tv);
//        TextView norecords = (TextView) dialog.findViewById(R.id.norecord_tv);
//
//        String lastVisitCode = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getLatestCropMaintanaceHistoryCode(CommonConstants.PLOT_CODE));
//        currentplantationlastvisitdatamap = (ArrayList<Uprootment>) dataAccessHandler.getUprootmentData(Queries.getInstance().getRecommndCropMaintenanceHistoryData(lastVisitCode, DatabaseKeys.TABLE_UPROOTMENT), 1);
//
//        if (currentplantationlastvisitdatamap.size() > 0){
//            norecords.setVisibility(View.GONE);
//            mainLL.setVisibility(View.VISIBLE);
//
//
//
//            String reason = null;
//            String ismissing;
//
//            if (currentplantationlastvisitdatamap.get(0).getIstreesmissing() == 1){
//                ismissing = "Yes";
//            }else{
//
//                ismissing = "No";
//            }
//
//            if (currentplantationlastvisitdatamap.get(0).getReasontypeid() != null){
//
//
//                reason = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getlookupdata(currentplantationlastvisitdatamap.get(0).getReasontypeid()));
//            }
//
//            saplingsplanted.setText(currentplantationlastvisitdatamap.get(0).getSeedsplanted() + "");
//            countoftressinpreviousvisit.setText(currentplantationlastvisitdatamap.get(0).getExpectedPlamsCount() + "");
//            expectedtreecount.setText(currentplantationlastvisitdatamap.get(0).getExpectedPlamsCount() + "");
//            tresscurrentlypresent.setText(currentplantationlastvisitdatamap.get(0).getPlamscount() + "");
//            missingtress.setText(ismissing);
//            nomissingtress.setText(currentplantationlastvisitdatamap.get(0).getMissingtreescount() + "");
//
//            if (currentplantationlastvisitdatamap.get(0).getReasontypeid() != null){
//                reasonformissingtreesLL.setVisibility(View.VISIBLE);
//                reasonformissingtrees.setText(reason);
//            }else{
//                reasonformissingtreesLL.setVisibility(View.GONE);
//            }
//
//            Log.d("GETCOMMENTS",currentplantationlastvisitdatamap.get(0).getComments());
//
//            //if (!TextUtils.isEmpty(currentplantationlastvisitdatamap.get(0).getComments()) || !currentplantationlastvisitdatamap.get(0).getComments().equalsIgnoreCase("")  || !currentplantationlastvisitdatamap.get(0).getComments().equalsIgnoreCase(null) || !currentplantationlastvisitdatamap.get(0).getComments().equalsIgnoreCase("null")){
//            if (TextUtils.isEmpty(currentplantationlastvisitdatamap.get(0).getComments()) || currentplantationlastvisitdatamap.get(0).getComments().equalsIgnoreCase("")  || currentplantationlastvisitdatamap.get(0).getComments().equalsIgnoreCase(null) || currentplantationlastvisitdatamap.get(0).getComments().equalsIgnoreCase("null")){
//                commentsll.setVisibility(View.GONE);
//            }else{
//                commentsll.setVisibility(View.VISIBLE);
//                comments.setText(currentplantationlastvisitdatamap.get(0).getComments() + "");
//            }
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

    public void setUpdateUiListener(UpdateUiListener updateUiListener) {
        this.updateUiListener = updateUiListener;
    }
}
