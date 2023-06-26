package com.oilpalm3f.mainapp.cropmaintenance;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.oilpalm3f.mainapp.R;
import com.oilpalm3f.mainapp.cloudhelper.ApplicationThread;
import com.oilpalm3f.mainapp.common.CommonConstants;
import com.oilpalm3f.mainapp.common.CommonUtils;
import com.oilpalm3f.mainapp.database.DataAccessHandler;
import com.oilpalm3f.mainapp.database.DatabaseKeys;
import com.oilpalm3f.mainapp.database.Palm3FoilDatabase;
import com.oilpalm3f.mainapp.database.Queries;
import com.oilpalm3f.mainapp.dbmodels.FarmersDataforImageUploading;
import com.oilpalm3f.mainapp.dbmodels.PlantationAuditOptionsModel;
import com.oilpalm3f.mainapp.dbmodels.PlantationAuditQuestionsModel;
import com.oilpalm3f.mainapp.dbmodels.PlotAuditDetails;
import com.oilpalm3f.mainapp.ui.OilPalmBaseActivity;
import com.oilpalm3f.mainapp.utils.UiUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class PlantationAudit extends OilPalmBaseActivity {

    private DataAccessHandler dataAccessHandler;
    private Palm3FoilDatabase palm3FoilDatabase;

    ArrayList<FarmersDataforImageUploading> farmersdata;

    ArrayList<PlotAuditDetails> plotAuditDetailsData;
    String fullname = "", middleName = "";

    TextView nameofthegrower, fcode, pcode, area, variety, dop, clustername, villagename, mandalname, districtname;
    LinearLayout containerLayout;
    Button submit;
    boolean isAllQuestionsAnswered = true;

    List<String> unansweredQuestions = new ArrayList<>();

    @Override
    public void Initialize() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View parentView = inflater.inflate(R.layout.activity_plantation_audit, null);
        baseLayout.addView(parentView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        dataAccessHandler = new DataAccessHandler(PlantationAudit.this);
        palm3FoilDatabase = new Palm3FoilDatabase(this);
        initView();
        setViews();
        setTile("Plantation Audit Screen");
    }

    private void initView() {

        nameofthegrower = findViewById(R.id.nameofthegrower);
        fcode = findViewById(R.id.fcode);
        pcode = findViewById(R.id.pcode);
        area = findViewById(R.id.area);
        variety = findViewById(R.id.variety);
        dop = findViewById(R.id.dop);
        clustername = findViewById(R.id.clustername);
        villagename = findViewById(R.id.villagename);
        mandalname = findViewById(R.id.mandalname);
        districtname = findViewById(R.id.districtname);
        containerLayout = findViewById(R.id.containerlayout);
        submit = findViewById(R.id.submit);
    }

    private void setViews() {

        farmersdata = dataAccessHandler.getFarmerDetailsforImageUploading(Queries.getInstance().getfarmerdetailsforimageuploading(CommonConstants.FARMER_CODE));

        if (!TextUtils.isEmpty(farmersdata.get(0).getMiddleName()) && !
                farmersdata.get(0).getMiddleName().equalsIgnoreCase("null")) {
            middleName = farmersdata.get(0).getMiddleName();
        }

        fullname = farmersdata.get(0).getFirstName().trim() + " " + middleName + " " + farmersdata.get(0).getLastName().trim();

        Log.d("PASelectedFarmerName", fullname + "");
        Log.d("PASelectedFarmerCode", CommonConstants.FARMER_CODE);
        Log.d("PASelectedPlotCode", CommonConstants.PLOT_CODE);

        nameofthegrower.setText(" :  " + fullname);
        fcode.setText(" :  " + CommonConstants.FARMER_CODE);
        pcode.setText(" :  " + CommonConstants.PLOT_CODE);

        plotAuditDetailsData = dataAccessHandler.getPlotDetailsforAudit(Queries.getInstance().getPlotDetailsforAudit(CommonConstants.PLOT_CODE));

        Log.d("TotalPalmArea", plotAuditDetailsData.get(0).getTotalPalmArea() + "");
        Log.d("CropVareity", plotAuditDetailsData.get(0).getCropVareity() + "");
        Log.d("DateofPlanting", plotAuditDetailsData.get(0).getDateofPlanting() + "");
        Log.d("ClusterName", plotAuditDetailsData.get(0).getClusterName() + "");
        Log.d("VillageName", plotAuditDetailsData.get(0).getVillageName() + "");
        Log.d("MandalName", plotAuditDetailsData.get(0).getMandalName() + "");
        Log.d("DistrictName", plotAuditDetailsData.get(0).getDistrictName() + "");

        area.setText(" :  " + plotAuditDetailsData.get(0).getTotalPalmArea() + "");

        if (plotAuditDetailsData.get(0).getCropVareity() == null){
            variety.setText(" :  " + "");
        }else{
            variety.setText(" :  " + plotAuditDetailsData.get(0).getCropVareity() + "");
        }

        if (plotAuditDetailsData.get(0).getDateofPlanting() == null){
            dop.setText(" :  " + "");
        }else{
            dop.setText(" :  " + plotAuditDetailsData.get(0).getDateofPlanting() + "");
        }

        clustername.setText(" :  " + plotAuditDetailsData.get(0).getClusterName() + "");
        villagename.setText(" :  " + plotAuditDetailsData.get(0).getVillageName() + "");
        mandalname.setText(" :  " + plotAuditDetailsData.get(0).getMandalName() + "");
        districtname.setText(" :  " + plotAuditDetailsData.get(0).getDistrictName() + "");


        // Assuming you have the list of questions and their types retrieved from the database
        List<PlantationAuditQuestionsModel> questions = dataAccessHandler.getPlantationAuditQuestions(Queries.getInstance().getPlantationAuditQuestions());
        int topMarginInPixels = getResources().getDimensionPixelSize(R.dimen.margin_top_layout);

// Iterate through the questions
        for (PlantationAuditQuestionsModel question : questions) {
            int questionId = question.getId();
            int questionType = question.getQuestionTypeId();
            String questionName = question.getQuestion();

            // Create a TextView to display the question name
            TextView questionTextView = new TextView(this);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                questionTextView.setTextAppearance(R.style.header_h1_text_title);
            } else {
                questionTextView.setTextAppearance(this, R.style.header_h1_text_title);
            }

            questionTextView.setText(questionName + " * ");

            // Set layout parameters for the question TextView
            LinearLayout.LayoutParams questionParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            if (questions.size() > 0) {
                questionParams.topMargin = topMarginInPixels;
            }
            questionTextView.setLayoutParams(questionParams);

            // Add the question TextView to the container layout
            containerLayout.addView(questionTextView);

            if (questionType == 386) {
                // Dropdown question
                List<PlantationAuditOptionsModel> options = dataAccessHandler.getPlantationAuditOptions(Queries.getInstance().getPlantationAuditOptions(questionId));

                // Create a new list to hold the modified options with the "Select" option
                List<PlantationAuditOptionsModel> modifiedOptions = new ArrayList<>(options);

// Create a new "Select" option
                PlantationAuditOptionsModel selectOption = new PlantationAuditOptionsModel(0, 0, "-- Select " + questionName + " --", 0, null);

// Add the "Select" option to the modified options list
                modifiedOptions.add(0, selectOption);

                Spinner spinner = new Spinner(this);
                spinner.setTag("spinner_" + questionId);
                //spinner = containerLayout.findViewWithTag("spinner_" + questionId);
                spinner.setTop(10);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    spinner.setBackground(ContextCompat.getDrawable(this, R.drawable.spinner_bg));
                } else {
                    spinner.setBackground(ContextCompat.getDrawable(this, R.drawable.spinner_bg));
                }

                // Create a custom ArrayAdapter with the options
                ArrayAdapter<PlantationAuditOptionsModel> adapter = new ArrayAdapter<PlantationAuditOptionsModel>(this, android.R.layout.simple_spinner_item, modifiedOptions);

// Set the dropdown resource for the adapter
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Set the adapter for the spinner
                spinner.setAdapter(adapter);
                spinner.setSelection(0);

                // Set layout parameters for the spinner
                LinearLayout.LayoutParams spinnerParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                spinner.setLayoutParams(spinnerParams);

                // Add the spinner to the container layout
                containerLayout.addView(spinner);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        PlantationAuditOptionsModel selectedItem = (PlantationAuditOptionsModel) parent.getItemAtPosition(position);
                        int optionId = selectedItem.getId();
                        int questionId = question.getId();
                        String optionName = selectedItem.getOption();
                        // Use the optionId, questionId, and optionName as needed

                        // Log the selected optionId, questionId, and optionName
                        Log.d("Selected OptionId", String.valueOf(optionId));
                        Log.d("Selected QuestionId", String.valueOf(questionId));
                        Log.d("Selected OptionName", optionName);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Handle case when no item is selected
                    }
                });

            } else if (questionType == 385) {
                // Textbox question
                // Create an EditText (textbox) dynamically
                EditText editText = new EditText(this);
                editText.setTag("editText_" + questionId);
                editText.setPadding(10,15,10,15);
                editText.setTop(10);

                // Set layout parameters for the EditText
                LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    editText.setBackground(ContextCompat.getDrawable(this, R.drawable.app_edittext));
                } else {
                    editText.setBackground(ContextCompat.getDrawable(this, R.drawable.app_edittext));
                }

                editText.setLayoutParams(editTextParams);

                // Add the EditText to the container layout
                containerLayout.addView(editText);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // Not used in this case
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String enteredText = s.toString();

                        // Use the enteredText and questionId as needed
                        int questionId = question.getId();

                        // Log the entered text and questionId
                        Log.d("Entered Text", enteredText);
                        Log.d("QuestionId", String.valueOf(questionId));
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // Retrieve the entered text from the EditText

                    }
                });
            }
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAllQuestionsAnswered = true;
                unansweredQuestions.clear();
                List<LinkedHashMap> details = new ArrayList<>();

                for (PlantationAuditQuestionsModel question : questions) {
                    LinkedHashMap map = new LinkedHashMap();
                    int questionType = question.getQuestionTypeId();
                    int questionId = question.getId();

                    if (questionType == 386) {
                        // Dropdown question
                        Spinner spinner = containerLayout.findViewWithTag("spinner_" + questionId);
                        int selectedPosition = spinner.getSelectedItemPosition();

                        // Check if the "Select" option is still selected
                        if (selectedPosition == 0) {
                            // Dropdown question not answered
                            isAllQuestionsAnswered = false;
                            unansweredQuestions.add(question.getQuestion());
                            break; // Exit the loop since one question is not answered
                        }
                        else {
                            // Insert the question ID and option ID into the table
                             int optionId = ((PlantationAuditOptionsModel) spinner.getSelectedItem()).getId();
                            map.put("PlotCode",CommonConstants.PLOT_CODE);
                            map.put("QuestionId",questionId);
                            map.put("OptionId",optionId);
                            map.put("Value", "");
                            map.put("IsActive",1);
                            map.put("CreatedByUserId",Integer.parseInt(CommonConstants.USER_ID));
                            map.put("CreatedDate",CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
                            map.put("ServerUpdatedStatus",0);
                            details.add(map);
                        }
                    } else if (questionType == 385) {
                        // Textbox question
                        EditText editText = containerLayout.findViewWithTag("editText_" + questionId);
                        String enteredText = editText.getText().toString().trim();

                        // Check if the textbox is empty
                        if (enteredText.isEmpty()) {
                            // Textbox question not answered
                            isAllQuestionsAnswered = false;
                            unansweredQuestions.add(question.getQuestion());
                            break; // Exit the loop since one question is not answered
                        }
                        else {
                            // Insert the question ID and EditText value into the table
                            map.put("PlotCode",CommonConstants.PLOT_CODE);
                            map.put("QuestionId",questionId);
                            map.put("OptionId",0);
                            map.put("Value", enteredText);
                            map.put("IsActive",1);
                            map.put("CreatedByUserId",Integer.parseInt(CommonConstants.USER_ID));
                            map.put("CreatedDate",CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
                            map.put("ServerUpdatedStatus",0);
                            details.add(map);
                        }
                    }
                    //details.add(map);
                }

                // Check the validation result
                if (isAllQuestionsAnswered) {
                    Log.d("AllQuestionareAnswered", "AllQuestionareAnswered");

                    // All questions are answered, proceed with submission or further processing

                    dataAccessHandler.saveData(DatabaseKeys.TABLE_Plantation_Audit_Answers, details, new ApplicationThread.OnComplete<String>() {
                @Override
                public void execute(boolean success, String result, String msg) {

                    if (success) {
                        Log.d(PlantationAudit.class.getSimpleName(), "==> Analysis ==> TABLE_Plantation_Audit_Answers INSERT COMPLETED");
                        UiUtils.showCustomToastMessage("Submitted Successfully", PlantationAudit.this, 0);
                        finish();

                    } else {
                        Toast.makeText(PlantationAudit.this, "Submit Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
                } else {
                    Log.d("QuestionarenotAnswered", "AllQuestionarenotAnswered");
                    for (String question : unansweredQuestions) {
                        System.out.println("Unanswered Question: " + question);
                        UiUtils.showCustomToastMessage("Please Answer: "+ question, PlantationAudit.this, 1);
                    }
                    // Display an error message or perform appropriate action
                }
            }
        });
        }

    }