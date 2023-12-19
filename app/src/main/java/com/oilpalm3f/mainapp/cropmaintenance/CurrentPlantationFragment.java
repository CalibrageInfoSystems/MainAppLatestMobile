package com.oilpalm3f.mainapp.cropmaintenance;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oilpalm3f.mainapp.R;
import com.oilpalm3f.mainapp.areaextension.UpdateUiListener;
import com.oilpalm3f.mainapp.cloudhelper.ApplicationThread;
import com.oilpalm3f.mainapp.cloudhelper.Log;
import com.oilpalm3f.mainapp.common.CommonConstants;
import com.oilpalm3f.mainapp.common.CommonUtils;
import com.oilpalm3f.mainapp.database.DataAccessHandler;
import com.oilpalm3f.mainapp.database.DataSavingHelper;
import com.oilpalm3f.mainapp.database.DatabaseKeys;
import com.oilpalm3f.mainapp.database.Queries;
import com.oilpalm3f.mainapp.datasync.helpers.DataManager;
import com.oilpalm3f.mainapp.dbmodels.PlotGapFillingDetails;
import com.oilpalm3f.mainapp.dbmodels.Uprootment;
import com.oilpalm3f.mainapp.utils.UiUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import static com.oilpalm3f.mainapp.cropmaintenance.CommonUtilsNavigation.getKey;
import static com.oilpalm3f.mainapp.database.DataSavingHelper.saveRecordIntoActivityLog;

import static java.sql.Types.NULL;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by CHENCHAIAH on 5/27/2017.
 */

//Current Plantation Screen shows saplings planted
public class CurrentPlantationFragment extends Fragment {

    private static final String LOG_TAG = CurrentPlantationFragment.class.getName();
    private TextView Noofsaplingsplanted_text, countoftreespreviousvisit_text, missingtrees_text, noofmissingtrees_text, comments_text, expectedTreecount_visit;
    private TextView comments_tv;
    private EditText counttresscurrentvisitEdt, comment_edit, gapfillingsaplingcount,EdtExpdate,Importedsaplingcount,IndigenousSaplingcount,Edtgapfillingcomments;
    private Spinner reasonformissing;
    private View rootView;
    private Context mContext;
    private Button savebtn;
    private DataAccessHandler dataAccessHandler;
    private UpdateUiListener updateUiListener;
    private LinkedHashMap<String, String> reasonDataMap,gapfillingreasonDataMap;
    private Uprootment mUprootmentModel;
    private String treesCount, preCount;
    private int saplingsCount = 0;
    private ActionBar actionBar;
    private String gapFillingTreeCount, expecetedTreesCount;
    private String[] expecetTreeCount;
    private int missingTrees = 0;
    private int gapfillingtresscount = 0;
    private int IndigenousSaplingscount = 0;
    private int ImportedSaplingscount = 0;
    private int totalimportedandIndigenousSaplingscount = 0;
    Spinner requiredspinner,gapfillingreasonspinner;
    private LinearLayout reasonformissingtreesLL, gapfillinglinear, gapfillingcountlinear,sublinear;
//    private int expecetedTreesCount = 0;

    private Button historyBtn;
    private ArrayList<Uprootment> currentplantationlastvisitdatamap;
    private ArrayList<PlotGapFillingDetails> lastgapfillingdetails;
    private TextView total_no_of_missing_treesTV;
    private LinkedHashMap<String, String> requiredgapfilling;
    int currentTrees;
    private int TotalMissingTrees = 0;
    private int TotalMissingTreesbind = 0;

    private Calendar myCalendar = Calendar.getInstance();
    String Exp_date;
    int Isverified = 0;


    private PlotGapFillingDetails plotgapfillingdetails;
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
        gapfillingreasonDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getLookUpData("328"));
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
// //  //  //      saplingsCount = CommonUtils.convertToBigNumber(treesCount);
            saplingsCount = CommonUtils.convertToBigNumber(expecetedTreesCount);
        return rootView;
    }


    private void bindData() {
        mUprootmentModel = (Uprootment) DataManager.getInstance().getDataFromManager(DataManager.CURRENT_PLANTATION);
        plotgapfillingdetails = (PlotGapFillingDetails)DataManager.getInstance().getDataFromManager(DataManager.PlotGapFilling_Details);

        if (mUprootmentModel != null) {
            counttresscurrentvisitEdt.setText("" + mUprootmentModel.getPlamscount());
            noofmissingtrees_text.setText("" + mUprootmentModel.getMissingtreescount());

            missingTrees = mUprootmentModel.getMissingtreescount();
            missingtrees_text.setText(mUprootmentModel.getIstreesmissing() == 1 ? "Yes" : "No");
            if (mUprootmentModel.getMissingtreescount() != 0) {
                reasonformissingtreesLL.setVisibility(View.VISIBLE);
                gapfillinglinear.setVisibility(View.VISIBLE);

                reasonformissing.setSelection(mUprootmentModel.getReasontypeid() == null ? 0 : CommonUtilsNavigation.getvalueFromHashMap(reasonDataMap, mUprootmentModel.getReasontypeid()));

                requiredspinner.setSelection(mUprootmentModel.getIsGapFillingRequired() == null ? 0 : CommonUtilsNavigation.getvalueFromHashMap(requiredgapfilling, mUprootmentModel.getIsGapFillingRequired()));
                gapfillingsaplingcount.setText(mUprootmentModel.getGapFillingSaplingsCount() + "");

                if(plotgapfillingdetails!=null){
                    preCount = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().queryGetCountOfPreviousTrees(CommonConstants.PLOT_CODE));

                    String outputFormat = "yyyy-MM-dd";
                    String myFormat = "MM/dd/yyyy";

                    // Create a SimpleDateFormat object to parse the input string
                    SimpleDateFormat inputDateFormat = new SimpleDateFormat(outputFormat);
                    TotalMissingTrees= Integer.parseInt(preCount) - Integer.parseInt(mUprootmentModel.getPlamscount()+"");
                    Log.e("=========180",Integer.parseInt(mUprootmentModel.getPlamscount()+"")+"");
                    Log.e("=========TotalMissingTreesbind",TotalMissingTrees+"");
                    total_no_of_missing_treesTV.setText(TotalMissingTrees+"");
                    Exp_date = plotgapfillingdetails.getExpectedDateOfPickup();

                    try {
                        // Parse the input string into a Date object
                        Date parsedDate = inputDateFormat.parse(plotgapfillingdetails.getExpectedDateOfPickup());

                        // Print the parsed Date object
                        System.out.println("Parsed Date: " + parsedDate);

                        // Optionally, you can convert the parsed Date to another format
                        // Format for output date string
                        SimpleDateFormat outputDateFormat = new SimpleDateFormat(myFormat);

                        EdtExpdate.setText(outputDateFormat.format(parsedDate)+"");
                        // Print the formatted output date string
                        System.out.println("Formatted Date: " + Exp_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    ImportedSaplingscount = plotgapfillingdetails.getImportedSaplingsToBeIssued();
                    IndigenousSaplingscount =plotgapfillingdetails.getIndigenousSaplingsToBeIssued();
                    Importedsaplingcount.setText(plotgapfillingdetails.getImportedSaplingsToBeIssued()+"");
                    IndigenousSaplingcount.setText(plotgapfillingdetails.getIndigenousSaplingsToBeIssued()+"");
                    Edtgapfillingcomments.setText(plotgapfillingdetails.getComments()+"");

                    gapfillingreasonspinner.setSelection(plotgapfillingdetails.getGapFillingReasonTypeId() == null ? 0 : CommonUtilsNavigation.getvalueFromHashMap(gapfillingreasonDataMap,plotgapfillingdetails.getGapFillingReasonTypeId()  ));

                }

            } else {
                reasonformissingtreesLL.setVisibility(View.GONE);
                gapfillinglinear.setVisibility(View.GONE);
             //   sublinear.setVisibility(View.VISIBLE);
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
        gapfillingsaplingcount = (EditText) rootView.findViewById(R.id.gapfillingcount);
        gapfillinglinear = (LinearLayout) rootView.findViewById(R.id.gapfillinglinear);
        gapfillingcountlinear = (LinearLayout) rootView.findViewById(R.id.gapfillingcountlinear);
        requiredspinner = (Spinner) rootView.findViewById(R.id.requiredspinner);
        reasonformissing.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(mContext, "Reason", reasonDataMap));
        requiredgapfilling = dataAccessHandler.getGenericData(Queries.getInstance().getYesNo());
        requiredspinner.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Is Gap Filling Required? ", requiredgapfilling));
        sublinear = (LinearLayout) rootView.findViewById(R.id.sublinear);
        total_no_of_missing_treesTV = (TextView) rootView.findViewById(R.id.total_no_of_missing_treesTV);
        EdtExpdate = (EditText)rootView.findViewById(R.id.EdtExpdate);
        gapfillingreasonspinner =(Spinner) rootView.findViewById(R.id.gapfillingreasonspinner);
        gapfillingreasonspinner.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Gap Filling Reason", gapfillingreasonDataMap));
        Importedsaplingcount = (EditText) rootView.findViewById(R.id.Importedsaplingcount);
        IndigenousSaplingcount =(EditText)rootView.findViewById(R.id.IndigenousSaplingcount);
        Edtgapfillingcomments= (EditText) rootView.findViewById(R.id.Edtgapfillingcomments);
    }

    private void setViews() {


        counttresscurrentvisitEdt.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                String count = s.toString();
                int previusTrees = 0;
                if (!s.toString().equalsIgnoreCase("") && s != null) {
                    currentTrees = CommonUtils.convertToBigNumber(s.toString());

//                    if (currentTrees > Integer.parseInt(expecetedTreesCount)) {
//                        UiUtils.showCustomToastMessage(" Count Of Trees should be less than or equal to the Count of trees ", mContext, 0);
//                        counttresscurrentvisitEdt.setText("");
//                        return;
//                    }

                    if (Integer.parseInt(expecetedTreesCount) > currentTrees) {
                        missingtrees_text.setText("Yes");
                        noofmissingtrees_text.setText("" + (Integer.parseInt(expecetedTreesCount) - currentTrees));
                        reasonformissingtreesLL.setVisibility(View.VISIBLE);;
                        gapfillinglinear.setVisibility(View.VISIBLE);
                        comments_tv.setText("Comments*");
                        missingTrees = (Integer.parseInt(expecetedTreesCount) - currentTrees);
                        Log.v("@@@missing", "" + missingTrees);
                        Log.v("@@@preCount", "" + preCount);
                        TotalMissingTrees = Integer.parseInt(preCount) - currentTrees;
                        total_no_of_missing_treesTV.setText(TotalMissingTrees+"");
                       sublinear.setVisibility(View.GONE);
                        requiredspinner.setSelection(0);
                        reasonformissing.setSelection(0);
                    } else {
                        missingtrees_text.setText("No");
                        noofmissingtrees_text.setText("0");
                        comments_tv.setText("Comments");
                        reasonformissingtreesLL.setVisibility(View.GONE);
                        gapfillinglinear.setVisibility(View.GONE);
                        missingTrees = 0;
                        gapfillingsaplingcount.setText("");
                        EdtExpdate.setText("");
                        Importedsaplingcount.setText("");
                        IndigenousSaplingcount.setText("");
                        Edtgapfillingcomments.setText("");
                        gapfillingreasonspinner.setSelection(0);
                        requiredspinner.setSelection(0);
                        reasonformissing.setSelection(0);
                        gapfillingcountlinear.setVisibility(View.GONE);
                      sublinear.setVisibility(View.GONE);
                        Log.v("@@@missing", "" + missingTrees);
                    }


                } else {
                    missingtrees_text.setText("No");
                    noofmissingtrees_text.setText("0");
                    missingTrees = 0;
                    gapfillingsaplingcount.setText("");
                    EdtExpdate.setText("");
                    Importedsaplingcount.setText("");
                    IndigenousSaplingcount.setText("");
                    Edtgapfillingcomments.setText("");
                    gapfillingreasonspinner.setSelection(0);
                    reasonformissingtreesLL.setVisibility(View.GONE);
                    gapfillinglinear.setVisibility(View.GONE);
                    gapfillingcountlinear.setVisibility(View.GONE);
                 sublinear.setVisibility(View.GONE);
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


                if (requiredspinner.getSelectedItemPosition() == 1) {

                    sublinear.setVisibility(View.VISIBLE);
                    gapfillingcountlinear.setVisibility(View.VISIBLE);
//                    TotalMissingTrees = Integer.parseInt(treesCount) - currentTrees;
//                    total_no_of_missing_treesTV.setText(TotalMissingTrees+"");
                    gapfillingmethod();
//

                } else {
                    gapfillingsaplingcount.setText("");
                    EdtExpdate.setText("");
                    Importedsaplingcount.setText("");
                    IndigenousSaplingcount.setText("");
                    Edtgapfillingcomments.setText("");
                    gapfillingreasonspinner.setSelection(0);
                    gapfillingcountlinear.setVisibility(View.GONE);
                    sublinear.setVisibility(View.GONE);
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
                Log.e("========missingTrees", missingTrees + "");

                if (TextUtils.isEmpty(counttresscurrentvisitEdt.getText().toString())) {
                    UiUtils.showCustomToastMessage("Please Enter Count Of Trees", mContext, 0);
                    return;
                }


                if (missingTrees > 0) {
                    if (requiredspinner.getSelectedItemPosition() == 0) {
                        UiUtils.showCustomToastMessage("Please Select Is Gap Filling Required? ", mContext, 0);
                        return;
                    }
                    if (requiredspinner.getSelectedItemPosition() == 1) {


                        if (TextUtils.isEmpty(Importedsaplingcount.getText().toString()) && TextUtils.isEmpty(IndigenousSaplingcount.getText().toString()) ){
                            UiUtils.showCustomToastMessage("Please Enter Imported or  Indigenous Sapling count or Both", mContext, 0);
                            return;
                        }
                        if (Importedsaplingcount.getText().toString().equalsIgnoreCase("0") && IndigenousSaplingcount.getText().toString().equalsIgnoreCase("0") ) {
                            UiUtils.showCustomToastMessage("Please Enter Imported or  Indigenous Sapling count or Both", mContext, 0);
                            return;
                        }
                        if (TextUtils.isEmpty(Importedsaplingcount.getText().toString()) && IndigenousSaplingcount.getText().toString().equalsIgnoreCase("0") ){
                            UiUtils.showCustomToastMessage("Please Enter Imported or  Indigenous Sapling count or Both", mContext, 0);
                            return;
                        }
                        if (Importedsaplingcount.getText().toString().equalsIgnoreCase("0") &&TextUtils.isEmpty(IndigenousSaplingcount.getText().toString()) ) {
                            UiUtils.showCustomToastMessage("Please Enter Imported or  Indigenous Sapling count or Both", mContext, 0);
                            return;
                        }

                        if (TotalMissingTrees < totalimportedandIndigenousSaplingscount) {
                            UiUtils.showCustomToastMessage("Total No Of Saplings to be Issued should be less than or equal to the Total Missing trees", mContext, 0);
                            return;
                        }
                        if (TotalMissingTrees < totalimportedandIndigenousSaplingscount) {
                            UiUtils.showCustomToastMessage("Saplings to be Issued should be less than or equal To the total Missing trees", mContext, 0);
                            return;
                        }
                        if (TextUtils.isEmpty(EdtExpdate.getText().toString())) {
                            UiUtils.showCustomToastMessage("Please Enter Pickup Date ", mContext, 0);
                            return;
                        }
                        if (gapfillingreasonspinner.getSelectedItemPosition() == 0) {
                            UiUtils.showCustomToastMessage("Please Select  Gap Filling Reason? ", mContext, 0);
                            return;
                        }

//                        lastgapfillingdetails = (ArrayList<PlotGapFillingDetails>) dataAccessHandler.getPlotgapFillingdetails(Queries.getInstance().getGapFillingDetailsHistoryData(), 1);
//                        if(lastgapfillingdetails.size()!=0) {
//                            Isverified =lastgapfillingdetails.get(0).getIsVerified();
//                            Log.e("===Isverified",Isverified+"");
//                            if (Isverified == 0) {
//                                UiUtils.showCustomToastMessage("Gap Filling is Already Initiated For this Plot. ", mContext, 0);
//                                return;
//                            }
//                        }

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
                if (requiredspinner.getSelectedItemPosition() == 1) {

                    mUprootmentModel.setGapFillingSaplingsCount(Integer.parseInt(gapfillingsaplingcount.getText().toString()));
                    plotgapfillingdetails();
                } else {
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

    private void gapfillingmethod() {
        Importedsaplingcount.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {



            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().isEmpty()) {

                    ImportedSaplingscount = CommonUtils.convertToBigNumber(s.toString());
                    Log.e("====>ImportedSaplingscount", IndigenousSaplingscount + "");
                    totalimportedandIndigenousSaplingscount = ImportedSaplingscount + IndigenousSaplingscount;
                    gapfillingsaplingcount.setText(totalimportedandIndigenousSaplingscount + "");
                } else {
                    ImportedSaplingscount = 0;
                    totalimportedandIndigenousSaplingscount = ImportedSaplingscount + IndigenousSaplingscount;
                    gapfillingsaplingcount.setText(totalimportedandIndigenousSaplingscount + "");
                    //  gapfillingsaplingcount.setText(""); // Clear the text if input is empty
                }
            }


        });

        IndigenousSaplingcount.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
//
//             if (!s.toString().isEmpty()) {
//            char firstDigit = s.charAt(0); // Get the first digit
//            IndigenousSaplingscount = CommonUtils.convertToBigNumber(String.valueOf(firstDigit));
//            Log.e("====>IndigenousSaplingscount", IndigenousSaplingscount + "");
//            totalimportedandIndigenousSaplingscount = ImportedSaplingscount + IndigenousSaplingscount;
//            gapfillingsaplingcount.setText(totalimportedandIndigenousSaplingscount + "");
//        } else {
//            gapfillingsaplingcount.setText(""); // Clear the text if input is empty
//        }
//    }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {

                    IndigenousSaplingscount = CommonUtils.convertToBigNumber(s.toString());
                    Log.e("====>IndigenousSaplingscount", IndigenousSaplingscount + "");
                    totalimportedandIndigenousSaplingscount = ImportedSaplingscount + IndigenousSaplingscount;
                    gapfillingsaplingcount.setText(totalimportedandIndigenousSaplingscount + "");
                } else {
                    IndigenousSaplingscount = 0;
                    totalimportedandIndigenousSaplingscount = ImportedSaplingscount + IndigenousSaplingscount;
                    gapfillingsaplingcount.setText(totalimportedandIndigenousSaplingscount + "");
                }

            }
        });

        final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();


        };

        EdtExpdate.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));  //date is dateSetListener as per your code in question
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();


        });
    }


    private void plotgapfillingdetails() {
        Log.e("=========>",CommonConstants.PLOT_CODE);
        plotgapfillingdetails = new PlotGapFillingDetails();
        // plotgapfillingdetails.setId();
        plotgapfillingdetails.setPlotCode(CommonConstants.PLOT_CODE);
        plotgapfillingdetails.setSaplingsToBeIssued(totalimportedandIndigenousSaplingscount);
        plotgapfillingdetails.setImportedSaplingsToBeIssued(ImportedSaplingscount);
        plotgapfillingdetails.setIndigenousSaplingsToBeIssued(IndigenousSaplingscount);
        plotgapfillingdetails.setExpectedDateOfPickup(Exp_date);
        plotgapfillingdetails.setGapFillingReasonTypeId(gapfillingreasonspinner.getSelectedItemPosition() == 0 ? null :
                Integer.parseInt(getKey(gapfillingreasonDataMap, gapfillingreasonspinner.getSelectedItem().toString())));
        plotgapfillingdetails.setIsApproved(0);
        plotgapfillingdetails.setIsDeclined(0);
        plotgapfillingdetails.setComments(Edtgapfillingcomments.getText().toString());
        plotgapfillingdetails.setIsActive(1);
        plotgapfillingdetails.setFileName("");
        plotgapfillingdetails.setFileLocation("");
        plotgapfillingdetails.setFileExtension("");
        plotgapfillingdetails.setCreatedByUserId(Integer.parseInt(CommonConstants.USER_ID));
        plotgapfillingdetails.setCreatedDate(CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
        plotgapfillingdetails.setUpdatedByUserId(Integer.parseInt(CommonConstants.USER_ID));
        plotgapfillingdetails.setUpdatedDate(CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
        plotgapfillingdetails.setApprovedByUserId(null);
        plotgapfillingdetails.setApprovedDate("");
        plotgapfillingdetails.setDeclinedByUserId(null);
        plotgapfillingdetails.setDeclinedDate("");
        plotgapfillingdetails.setAshApprovedComments("");
        plotgapfillingdetails.setDeclinedComments("");
        plotgapfillingdetails.setIsVerified(0);

        plotgapfillingdetails.setGapFillingStatusTypeId(null); // Use null instead of 'null'
        plotgapfillingdetails.setShApprovedComments(null); // Use null instead of 'null'

        plotgapfillingdetails.setServerUpdatedStatus(0);
        plotgapfillingdetails.setCmApprovedComments("");


        DataManager.getInstance().addData(DataManager.PlotGapFilling_Details, plotgapfillingdetails);
        CommonUtilsNavigation.hideKeyBoard(getActivity());
        getFragmentManager().popBackStack();
        updateUiListener.updateUserInterface(0);

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        EdtExpdate.setText(sdf.format(myCalendar.getTime()));
        // Create a SimpleDateFormat object to parse the input string
        SimpleDateFormat inputDateFormat = new SimpleDateFormat(myFormat);

        try {
            // Parse the input string into a Date object
            Date parsedDate = inputDateFormat.parse(EdtExpdate.getText().toString());

            // Print the parsed Date object
            System.out.println("Parsed Date: " + parsedDate);

            // Optionally, you can convert the parsed Date to another format
            String outputFormat = "yyyy-MM-dd"; // Format for output date string
            SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputFormat);
            Exp_date = outputDateFormat.format(parsedDate);

            // Print the formatted output date string
            System.out.println("Formatted Date: " + Exp_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        LinearLayout cpdatasaplingforgapfillingLL = (LinearLayout) dialog.findViewById(R.id.cpdatasaplingforgapfillingLL);
        LinearLayout cpdatagapfillrequiredlinear = (LinearLayout) dialog.findViewById(R.id.cpdatagapfillrequiredlinear);
        TextView cpdatagapfillrequired = (TextView) dialog.findViewById(R.id.cpdatagapfillrequired);
        TextView cpdatasaplingforgapfilling_tv = (TextView) dialog.findViewById(R.id.cpdatasaplingforgapfilling_tv);

        TextView saplingsplanted = (TextView) dialog.findViewById(R.id.cpdatasaplingplanted_text);
        TextView countoftressinpreviousvisit = (TextView) dialog.findViewById(R.id.cpdatacountoftreesvisit_text);
        TextView expectedtreecount = (TextView) dialog.findViewById(R.id.cpdataexpectedTreecountvisit);
        TextView tresscurrentlypresent = (TextView) dialog.findViewById(R.id.cpdatacounttresscurrentvisitEdt);
        TextView missingtress = (TextView) dialog.findViewById(R.id.cpdatamissingtrees_text);
        TextView nomissingtress = (TextView) dialog.findViewById(R.id.cpdatano_of_missing_treesTV);
        TextView reasonformissingtrees = (TextView) dialog.findViewById(R.id.cpdatareasonformissingtress);
        TextView comments = (TextView) dialog.findViewById(R.id.cpdatacomments_tv);
        TextView norecords = (TextView) dialog.findViewById(R.id.norecord_tv);
        TextView cpdatatotalno_of_missing_treesTV = (TextView) dialog.findViewById(R.id.cpdatatotalno_of_missing_treesTV);
        TextView NoofImportedSaplings = (TextView) dialog.findViewById(R.id.NoofImportedSaplings);
        TextView NoofIndigenousSaplings = (TextView) dialog.findViewById(R.id.NoofIndigenousSaplings);
        TextView ExpDateofPickup_tv = (TextView) dialog.findViewById(R.id.ExpDateofPickup_tv);
        TextView GapFillingReason_tv = (TextView) dialog.findViewById(R.id.GapFillingReason_tv);
        TextView cpdatagapfillingcomments_tv = (TextView) dialog.findViewById(R.id.cpdatagapfillingcomments_tv);
        LinearLayout gapfillinglinear = (LinearLayout)dialog.findViewById(R.id.gapfillinglinear);

        String lastVisitCode = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getLatestCropMaintanaceHistoryCode(CommonConstants.PLOT_CODE));
        currentplantationlastvisitdatamap = (ArrayList<Uprootment>) dataAccessHandler.getUprootmentData(Queries.getInstance().getRecommndCropMaintenanceHistoryData(lastVisitCode, DatabaseKeys.TABLE_UPROOTMENT), 1);
        lastgapfillingdetails = (ArrayList<PlotGapFillingDetails>) dataAccessHandler.getPlotGapFillingDetails(Queries.getInstance().getGapFillingDetailsHistoryData(), 1);

        if (currentplantationlastvisitdatamap.size() > 0) {
            norecords.setVisibility(View.GONE);
            mainLL.setVisibility(View.VISIBLE);


            String reason = null;
            String ismissing;

            if (currentplantationlastvisitdatamap.get(0).getIstreesmissing() == 1) {
                ismissing = "Yes";
            } else {

                ismissing = "No";
            }


            if (currentplantationlastvisitdatamap.get(0).getReasontypeid() != null) {


                reason = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getlookupdata(currentplantationlastvisitdatamap.get(0).getReasontypeid()));
            }

            saplingsplanted.setText(currentplantationlastvisitdatamap.get(0).getSeedsplanted() + "");
            countoftressinpreviousvisit.setText(currentplantationlastvisitdatamap.get(0).getExpectedPlamsCount() + "");
            expectedtreecount.setText(currentplantationlastvisitdatamap.get(0).getExpectedPlamsCount() + "");
            tresscurrentlypresent.setText(currentplantationlastvisitdatamap.get(0).getPlamscount() + "");
            missingtress.setText(ismissing);
            nomissingtress.setText(currentplantationlastvisitdatamap.get(0).getMissingtreescount() + "");

            if (currentplantationlastvisitdatamap.get(0).getReasontypeid() != null) {
                reasonformissingtreesLL.setVisibility(View.VISIBLE);
                reasonformissingtrees.setText(reason);
            } else {
                reasonformissingtreesLL.setVisibility(View.GONE);
            }

            if (currentplantationlastvisitdatamap.get(0).getIsGapFillingRequired() != null) {
                cpdatagapfillrequiredlinear.setVisibility(View.VISIBLE);
                String Required;
                if (currentplantationlastvisitdatamap.get(0).getIsGapFillingRequired() == 1) {
                    Required = "Yes";
                    cpdatasaplingforgapfillingLL.setVisibility(View.VISIBLE);
                    cpdatasaplingforgapfilling_tv.setText(currentplantationlastvisitdatamap.get(0).getGapFillingSaplingsCount() + "");
                } else {

                    Required = "No";
                    cpdatasaplingforgapfillingLL.setVisibility(View.GONE);
                }
                cpdatagapfillrequired.setText(Required);
                if(Required.equalsIgnoreCase( "No")){
                    gapfillinglinear.setVisibility(View.GONE);
                }else{
                    gapfillinglinear.setVisibility(View.VISIBLE);
                }

            } else {
                cpdatagapfillrequiredlinear.setVisibility(View.GONE);
            }

            Log.d("GETCOMMENTS", currentplantationlastvisitdatamap.get(0).getComments());

            //if (!TextUtils.isEmpty(currentplantationlastvisitdatamap.get(0).getComments()) || !currentplantationlastvisitdatamap.get(0).getComments().equalsIgnoreCase("")  || !currentplantationlastvisitdatamap.get(0).getComments().equalsIgnoreCase(null) || !currentplantationlastvisitdatamap.get(0).getComments().equalsIgnoreCase("null")){
            if (TextUtils.isEmpty(currentplantationlastvisitdatamap.get(0).getComments()) || currentplantationlastvisitdatamap.get(0).getComments().equalsIgnoreCase("") || currentplantationlastvisitdatamap.get(0).getComments().equalsIgnoreCase(null) || currentplantationlastvisitdatamap.get(0).getComments().equalsIgnoreCase("null")) {
                commentsll.setVisibility(View.GONE);
            } else {
                commentsll.setVisibility(View.VISIBLE);
                comments.setText(currentplantationlastvisitdatamap.get(0).getComments() + "");
            }
        } else {
            mainLL.setVisibility(View.GONE);
            norecords.setVisibility(View.VISIBLE);
        }

        if (lastgapfillingdetails.size() > 0){
            String gapfillingreason= null;
            cpdatatotalno_of_missing_treesTV.setText((currentplantationlastvisitdatamap.get(0).getSeedsplanted() - currentplantationlastvisitdatamap.get(0).getPlamscount() )  + "");
            NoofImportedSaplings.setText(lastgapfillingdetails.get(0).getImportedSaplingsToBeIssued()+"");
            NoofIndigenousSaplings.setText(lastgapfillingdetails.get(0).getIndigenousSaplingsToBeIssued()+"");

            String outputFormat = "yyyy-MM-dd";
            String myFormat = "MM/dd/yyyy";

            // Create a SimpleDateFormat object to parse the input string
            SimpleDateFormat inputDateFormat = new SimpleDateFormat(outputFormat);


            try {
                // Parse the input string into a Date object
                Date parsedDate = inputDateFormat.parse(lastgapfillingdetails.get(0).getExpectedDateOfPickup());

                // Print the parsed Date object
                System.out.println("Parsed Date: " + parsedDate);

                SimpleDateFormat outputDateFormat = new SimpleDateFormat(myFormat);
                ExpDateofPickup_tv.setText(outputDateFormat.format(parsedDate)+"");


            } catch (ParseException e) {
                e.printStackTrace();
            }


            if (lastgapfillingdetails.get(0).getGapFillingReasonTypeId() != null) {


                gapfillingreason = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getlookupdata(lastgapfillingdetails.get(0).getGapFillingReasonTypeId()));
            }
            GapFillingReason_tv.setText(gapfillingreason+"");
            cpdatagapfillingcomments_tv.setText(lastgapfillingdetails.get(0).getComments());

        }



        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 500);
    }

    public void setUpdateUiListener(UpdateUiListener updateUiListener) {
        this.updateUiListener = updateUiListener;
    }
}
