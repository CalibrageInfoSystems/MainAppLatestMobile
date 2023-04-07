package com.oilpalm3f.mainapp.cropmaintenance;

import static com.oilpalm3f.mainapp.common.CommonUtils.isFromImagesUploading;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oilpalm3f.mainapp.BuildConfig;
import com.oilpalm3f.mainapp.R;
import com.oilpalm3f.mainapp.cloudhelper.ApplicationThread;
import com.oilpalm3f.mainapp.cloudhelper.Log;
import com.oilpalm3f.mainapp.common.CommonConstants;
import com.oilpalm3f.mainapp.common.CommonUtils;
import com.oilpalm3f.mainapp.database.DataAccessHandler;
import com.oilpalm3f.mainapp.database.DatabaseKeys;
import com.oilpalm3f.mainapp.database.Queries;
import com.oilpalm3f.mainapp.datasync.helpers.DataManager;
import com.oilpalm3f.mainapp.dbmodels.FarmerBank;
import com.oilpalm3f.mainapp.dbmodels.FarmerBankdetailsforImageUploading;
import com.oilpalm3f.mainapp.dbmodels.FarmersDataforImageUploading;
import com.oilpalm3f.mainapp.dbmodels.FileRepository;
import com.oilpalm3f.mainapp.dbmodels.IdentityProof;
import com.oilpalm3f.mainapp.dbmodels.IdentityProofRefresh;
import com.oilpalm3f.mainapp.ui.OilPalmBaseActivity;
import com.oilpalm3f.mainapp.utils.ImageUtility;
import com.oilpalm3f.mainapp.utils.UiUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class UploadImagesNew extends OilPalmBaseActivity {


    private DataAccessHandler dataAccessHandler;
    TextView farmercode,farmername,fathername,mobilenumber,farmermandal,farmerviallge,aadharnumber,norecordsforidproofs,norecordsforbank,accountnumber,accountname,ifsccode,bankname;
    LinearLayout idproofslayout,bankdetailslayout;
    Button submitBtn,addrecoveryfarmers;
    ArrayList<FarmersDataforImageUploading> farmersdata;
    String selectedfarmercode;
    ImageView farmerimage,idproofimage,bankpassbookimage;
    private static final int CAMERA_REQUEST = 1888;

    private static final int PREVIOUS_INTENT = 2000;
    private static final int CAMERA_REQUEST2 = 1889;
    private static final int CAMERA_REQUEST3 = 1890;
    public String mCurrentPhotoPath,IdPhotoPath,passbookimagepath;
    private boolean isImage = false;
    private byte[] bytes = null;


    private String[] PERMISSIONS_STORAGE = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static final int REQUEST_CAM_PERMISSIONS = 1;

    private LinkedHashMap<String, String> idProofsData;
    private List<IdentityProof> identityProofsList;

    private FarmerBankdetailsforImageUploading farmerBank = null;

    private FileRepository savedPictureData = null;
    private FarmerBank farmerBankdetails = null;

    private IdentityProofRefresh idproofdetails = null;
    public static String idproofimageUrl = "";
    public static String bankimageUrl = "";
    public static String imageUrl = "";


    public static Boolean idproofimageexists = false;
    public static Boolean bankimageexists = false;
    public static Boolean farmerimageexists = false;

    String fullname = "", middleName = "";
    int recoveryfarmerexists = 0;
    int LAUNCH_SECOND_ACTIVITY = 1;
    int farmerfilerepoexists = 0;

    @Override
    public void Initialize() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View parentView = inflater.inflate(R.layout.activity_upload_images_new, null);
        baseLayout.addView(parentView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        dataAccessHandler = new DataAccessHandler(UploadImagesNew.this);
        initView();
        setViews();
        setTile("Upload Images Screen");
    }

    public void initView(){

        farmercode = findViewById(R.id.farmercode);
        farmername = findViewById(R.id.farmername);
        fathername = findViewById(R.id.fathername);
        mobilenumber = findViewById(R.id.mobilenumber);
        farmermandal = findViewById(R.id.farmermandal);
        farmerviallge = findViewById(R.id.farmerviallge);
        farmerimage = findViewById(R.id.farmerimage);
        aadharnumber = findViewById(R.id.aadharnumber);
        idproofimage = findViewById(R.id.idproofimage);
        idproofslayout = findViewById(R.id.idproofslayout);
        norecordsforidproofs = findViewById(R.id.norecordsforidproofs);

        bankname = findViewById(R.id.bankname);
        accountnumber = findViewById(R.id.accountnumber);
        accountname = findViewById(R.id.accountname);
        ifsccode = findViewById(R.id.ifsccode);
        norecordsforbank = findViewById(R.id.norecordsforbank);
        bankdetailslayout = findViewById(R.id.bankdetailslayout);
        bankpassbookimage = findViewById(R.id.bankpassbookimage);

        addrecoveryfarmers = findViewById(R.id.addrecoveryfarmers);

        submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setEnabled(false);
        submitBtn.setAlpha(0.5f);
    }

    public void setViews(){

        savedPictureData = new FileRepository();

        selectedfarmercode = CommonConstants.FARMER_CODE;
                Log.d("selectedfarmercode",selectedfarmercode + "");

//        farmerfilerepoexists = dataAccessHandler.getOnlyOneIntValueFromDb(Queries.getInstance().isFarmerFileRepoExists(selectedfarmercode));
//        Log.d("farmerfilerepoexists",farmerfilerepoexists + "");

//       String dateformat = CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS_SSS_NEW);
//       Log.d("dateformat", dateformat + "");

        recoveryfarmerexists = dataAccessHandler.getOnlyOneIntValueFromDb(Queries.getInstance().getIsRecoveryfarmerexists(selectedfarmercode));

        if (recoveryfarmerexists == 0){
            addrecoveryfarmers.setText("Add Recovery Farmer");
        }else{
            addrecoveryfarmers.setText("View Recovery Farmers");
        }

        savedPictureData = (FileRepository) DataManager.getInstance().getDataFromManager(DataManager.FILE_REPOSITORY);

        farmersdata = dataAccessHandler.getFarmerDetailsforImageUploading(Queries.getInstance().getfarmerdetailsforimageuploading(selectedfarmercode));

        if (savedPictureData == null) {
            savedPictureData = dataAccessHandler.getSelectedFileRepository(Queries.getInstance().getSelectedFarmerImageQuery(selectedfarmercode, 193));
        }
        //Log.d("savedPictureData", savedPictureData.getFarmercode() + "");

        if (savedPictureData != null && savedPictureData.getPicturelocation() != null) {
            if (savedPictureData.getPicturelocation().contains("storage")) {

                Log.d("location", "containsstorage");
                imageUrl = savedPictureData.getPicturelocation();
                File f = new File(imageUrl);
                Log.d("farmerimageUrl",imageUrl + "");
                Picasso.with(UploadImagesNew.this)
                        .load(f)
                        .into(farmerimage, new Callback() {
                            @Override
                            public void onSuccess() {
                                // farmerimage.setEnabled(false);
                                farmerimageexists = true;
                                //Toast.makeText(UploadImagesNew.this, "Image Available, Can't upload a new Image", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError() {
                                farmerimage.setImageDrawable(ContextCompat.getDrawable(UploadImagesNew.this, R.mipmap.ic_launcher));
                                //farmerimage.setEnabled(true);
                                farmerimageexists = false;
                            }
                        });

            }else{
                Log.d("location", "notcontainsstorage");
                Log.d("ImageAvailable","ImageAvailable" );
                imageUrl = CommonUtils.getImageUrl(savedPictureData);
                Log.d("farmerstorageimageUrl",imageUrl + "");
                if (imageUrl != null) {
                    Picasso.with(UploadImagesNew.this)
                            .load(imageUrl)
                            .into(farmerimage, new Callback() {
                                @Override
                                public void onSuccess() {
                                    //farmerimage.setEnabled(false);
                                    farmerimageexists = true;
                                    Log.d("Issuccess", "YES");
                                    //Toast.makeText(UploadImagesNew.this, "Image Available, Can't upload a new Image", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError() {
                                    farmerimage.setImageDrawable(ContextCompat.getDrawable(UploadImagesNew.this, R.mipmap.ic_launcher));
                                    //farmerimage.setEnabled(true);
                                    farmerimageexists = false;
                                    Log.d("Issuccess", "No");
                                }
                            });

                }
            }
        }else{
            Log.d("farmerImageNotAvailable","ImageNotAvailable" );
        }

        farmerBankdetails = (FarmerBank) DataManager.getInstance().getDataFromManager(DataManager.FARMER_BANK_DETAILS);
        if (farmerBankdetails == null && (isFromImagesUploading())) {
            farmerBankdetails = (FarmerBank) dataAccessHandler.getSelectedFarmerBankData(Queries.getInstance().getFarmerBankDataforImageUpload(selectedfarmercode), 0);
        }

        if (null != farmerBankdetails) {

            if (farmerBankdetails.getFilelocation().contains("storage")){

                Log.d("location", "containsstorage");
                bankimageUrl = farmerBankdetails.getFilelocation();
                File f = new File(bankimageUrl);
                Log.d("bankimageUrl",bankimageUrl + "");
                Picasso.with(UploadImagesNew.this)
                        .load(f)
                        .into(bankpassbookimage, new Callback() {
                            @Override
                            public void onSuccess() {
                                //bankpassbookimage.setEnabled(false);
                                bankimageexists = true;
                                //Toast.makeText(UploadImagesNew.this, "Image Available, Can't upload a new Image", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError() {
                                bankpassbookimage.setImageDrawable(ContextCompat.getDrawable(UploadImagesNew.this, R.mipmap.ic_launcher));
                                // bankpassbookimage.setEnabled(true);
                                bankimageexists = false;
                            }
                        });

            }else{
                bankimageUrl = CommonUtils.getImageUrl(farmerBankdetails);
                Log.d("bankimageUrl",bankimageUrl + "");
                Picasso.with(UploadImagesNew.this)
                        .load(bankimageUrl)
                        .into(bankpassbookimage, new Callback() {
                            @Override
                            public void onSuccess() {
                                //bankpassbookimage.setEnabled(false);
                                bankimageexists = true;
                                //Toast.makeText(UploadImagesNew.this, "Image Available, Can't upload a new Image", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError() {
                                bankpassbookimage.setImageDrawable(ContextCompat.getDrawable(UploadImagesNew.this, R.mipmap.ic_launcher));
                                //bankpassbookimage.setEnabled(true);
                                bankimageexists = false;
                            }
                        });
            }

        }else{
            Log.d("BankImageNotAvailable","BankImageNotAvailable" );
        }

        idproofdetails = (IdentityProofRefresh) DataManager.getInstance().getDataFromManager(DataManager.ID_PROOFS_DATA);
        if (idproofdetails == null && (isFromImagesUploading())) {
            idproofdetails = (IdentityProofRefresh) dataAccessHandler.getIdProofsData(Queries.getInstance().getFarmerIdProofDataforImageUpload(selectedfarmercode), 0);
        }

        if (null != idproofdetails) {

            if (idproofdetails.getFileLocation().contains("storage")){

                Log.d("location", "containsstorage");
                idproofimageUrl = idproofdetails.getFileLocation();
                File f = new File(idproofimageUrl);
                Log.d("idproofimageUrl",idproofimageUrl + "");
                Picasso.with(UploadImagesNew.this)
                        .load(f)
                        .into(idproofimage, new Callback() {
                            @Override
                            public void onSuccess() {
                                //idproofimage.setEnabled(false);
                                idproofimageexists = true;
                                //Toast.makeText(UploadImagesNew.this, "Image Available, Can't upload a new Image", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError() {
                                idproofimage.setImageDrawable(ContextCompat.getDrawable(UploadImagesNew.this, R.mipmap.ic_launcher));
                                //idproofimage.setEnabled(true);
                                idproofimageexists = false;
                            }
                        });
            }else{

                idproofimageUrl = CommonUtils.getIdproofImageUrl(idproofdetails);
                Log.d("location", "notcontainsstorage");
                Log.d("idproofimageUrl",idproofimageUrl + "");
                Picasso.with(UploadImagesNew.this)
                        .load(idproofimageUrl)
                        .into(idproofimage, new Callback() {
                            @Override
                            public void onSuccess() {
                                //idproofimage.setEnabled(false);
                                idproofimageexists = true;
                                //Toast.makeText(UploadImagesNew.this, "Image Available, Can't upload a new Image", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError() {
                                idproofimage.setImageDrawable(ContextCompat.getDrawable(UploadImagesNew.this, R.mipmap.ic_launcher));
                                //idproofimage.setEnabled(true);
                                idproofimageexists = false;
                            }
                        });
            }


            //Log.d("IdproofImageNotAvailable","IdproofImageNotAvailable" );
        }



        if (!TextUtils.isEmpty(farmersdata.get(0).getMiddleName()) && !
                farmersdata.get(0).getMiddleName().equalsIgnoreCase("null")) {
            middleName = farmersdata.get(0).getMiddleName();
        }

        fullname = farmersdata.get(0).getFirstName().trim() + " " + middleName + " " + farmersdata.get(0).getLastName().trim();

        farmercode.setText("  :  " + farmersdata.get(0).getCode());
        farmername.setText("  :  " + fullname);
        fathername.setText("  :  " + farmersdata.get(0).getGuardianName());
        mobilenumber.setText("  :  " + farmersdata.get(0).getContactNumber());
        farmermandal.setText("  :  " + farmersdata.get(0).getMandalName());
        farmerviallge.setText("  :  " + farmersdata.get(0).getVillageName());


        idProofsData  = dataAccessHandler.getGenericData(Queries.getInstance().getTypeCdDmtData("12"));

        identityProofsList = (List<IdentityProof>) DataManager.getInstance().getDataFromManager(DataManager.ID_PROOFS_DATA);

        if (identityProofsList == null && (isFromImagesUploading())) {
            identityProofsList = (List<IdentityProof>) dataAccessHandler.getSelectedIdProofsData(Queries.getInstance().getSingleFarmerIdentityProof(CommonConstants.FARMER_CODE), 1);
        }

        Log.d("identityProofsList", identityProofsList.size() + "");

        if (identityProofsList.size() == 0) {
            Log.d("NoIDProofs", "IDProofs not available");
            norecordsforidproofs.setVisibility(View.VISIBLE);
            idproofslayout.setVisibility(View.GONE);
        } else {
            Log.d("IDProofs", "IDProofs available");
            norecordsforidproofs.setVisibility(View.GONE);
            idproofslayout.setVisibility(View.VISIBLE);
            aadharnumber.setText("  :  " + identityProofsList.get(0).getIdproofnumber());
        }

        farmerBank = (FarmerBankdetailsforImageUploading) DataManager.getInstance().getDataFromManager(DataManager.FARMER_BANK_DETAILS);
        if (farmerBank == null && (isFromImagesUploading())) {
            farmerBank = (FarmerBankdetailsforImageUploading) dataAccessHandler.getSelectedFarmerBankDataforImageUploading(Queries.getInstance().getFarmerBankDataforImageUploading(CommonConstants.FARMER_CODE), 0);
        }

        if (null == farmerBank) {
            Log.d("NoBankDetails", "BankDetails not available");
            norecordsforbank.setVisibility(View.VISIBLE);
            bankdetailslayout.setVisibility(View.GONE);
        }else{
            Log.d("BankDetails", "BankDetails available");
            norecordsforbank.setVisibility(View.GONE);
            bankdetailslayout.setVisibility(View.VISIBLE);
            bankname.setText("  :  " +farmerBank.getDesc());
            accountnumber.setText("  :  " +farmerBank.getAccountNumber());
            accountname.setText("  :  " +farmerBank.getAccountHolderName());
            ifsccode.setText("  :  " +farmerBank.getIFSCCode());

        }

        farmerimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("farmerimageexists", farmerimageexists + "");

                if (farmerimageexists == false){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (!CommonUtils.isPermissionAllowed(UploadImagesNew.this, Manifest.permission.CAMERA))) {
                        ActivityCompat.requestPermissions(
                                UploadImagesNew.this,
                                PERMISSIONS_STORAGE,
                                REQUEST_CAM_PERMISSIONS
                        );
                    } else {
                        dispatchTakePictureIntent(CAMERA_REQUEST);
                    }
                }else{
                    Toast.makeText(UploadImagesNew.this, "Farmer image already exists,can't upload a new one", Toast.LENGTH_SHORT).show();
                }



            }
        });


        idproofimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (idproofimageexists == false){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (!CommonUtils.isPermissionAllowed(UploadImagesNew.this, Manifest.permission.CAMERA))) {
                        ActivityCompat.requestPermissions(
                                UploadImagesNew.this,
                                PERMISSIONS_STORAGE,
                                REQUEST_CAM_PERMISSIONS
                        );
                    } else {
                        dispatchTakePictureIntent2(CAMERA_REQUEST2);
                    }
                }else {
                    Toast.makeText(UploadImagesNew.this, "Identity Proof image already exists,can't upload a new one", Toast.LENGTH_SHORT).show();
                }



            }
        });
        bankpassbookimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (bankimageexists == false){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (!CommonUtils.isPermissionAllowed(UploadImagesNew.this, Manifest.permission.CAMERA))) {
                        ActivityCompat.requestPermissions(
                                UploadImagesNew.this,
                                PERMISSIONS_STORAGE,
                                REQUEST_CAM_PERMISSIONS
                        );
                    } else {
                        dispatchTakePictureIntent3(CAMERA_REQUEST3);
                    }
                }else{
                    Toast.makeText(UploadImagesNew.this, "Bank image already exists,can't upload a new one", Toast.LENGTH_SHORT).show();
                }

            }
        });

        addrecoveryfarmers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (recoveryfarmerexists == 0){
                    Log.d("fullname", fullname + "");
                    Intent recoveryfarmers = new Intent(UploadImagesNew.this, RecoveryFarmers.class);
                    recoveryfarmers.putExtra("fullname", fullname);
                    startActivityForResult(recoveryfarmers, LAUNCH_SECOND_ACTIVITY);
                    Log.d("RecoveryFarmer", "Not Exists");
                }else{
                    Intent viewrecoveryfarmers = new Intent(UploadImagesNew.this, ViewRecoveryFarmers.class);
                    startActivity(viewrecoveryfarmers);
                    //UiUtils.showCustomToastMessage("Selected Main Farmer Already Having Recovery Farmers", UploadImagesNew.this, 1);
                    Log.d("RecoveryFarmer", "Exists");
                }

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validations()){

                    //dataAccessHandler.executeRawQuery("Update FileRepository set IsActive = 0 where FarmerCode = '" + selectedfarmercode + "' ");

                    if( mCurrentPhotoPath != null ){

                        List<LinkedHashMap> farmerphoto = new ArrayList<>();
                        LinkedHashMap farmermap = new LinkedHashMap();
                        farmermap.put("FarmerCode",CommonConstants.FARMER_CODE.toString());
                        farmermap.put("PlotCode","");
                        farmermap.put("ModuleTypeId",193);
                        farmermap.put("FileName",CommonConstants.FARMER_CODE);
                        farmermap.put("FileLocation",mCurrentPhotoPath.toString());
                        farmermap.put("FileExtension",CommonConstants.JPEG_FILE_SUFFIX);
                        farmermap.put("IsActive",1);
                        farmermap.put("CreatedByUserId",Integer.parseInt(CommonConstants.USER_ID));
                        farmermap.put("CreatedDate",CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
                        farmermap.put("UpdatedByUserId",Integer.parseInt(CommonConstants.USER_ID));
                        farmermap.put("UpdatedDate",CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
                        farmermap.put("ServerUpdatedStatus",0);
                        farmermap.put("CropMaintenanceCode","");
                        farmerphoto.add(farmermap);
                        //String whereCondition = " where  FarmerCode='" + CommonConstants.FARMER_CODE + "'";
                        dataAccessHandler.saveData(DatabaseKeys.TABLE_FILEREPOSITORY, farmerphoto, new ApplicationThread.OnComplete<String>() {
                            @Override
                            public void execute(boolean success, String result, String msg) {

                                if (success) {
                                    Log.d(UploadImagesNew.class.getSimpleName(), "==>  Analysis ==> TABLE_FileRepo Insert Success");
                                    // Toast.makeText(UploadImagesNew.this, "Data Inserted into the table successfully", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(UploadImagesNew.this, "Data Insertion failed", Toast.LENGTH_SHORT).show();
                                    Log.d(UploadImagesNew.class.getSimpleName(), "==>  Analysis ==> TABLE_FileRepo Insert Failed");
                                }
                            }
                        });

//                        dataAccessHandler.updateData(DatabaseKeys.TABLE_FILEREPOSITORY, farmerphoto, true, whereCondition, new ApplicationThread.OnComplete<String>() {
//                            @Override
//                            public void execute(boolean success, String result, String msg) {
//                                if (success) {
//                                    Log.d(UploadImagesNew.class.getSimpleName(), "==>  Analysis ==> TABLE_FileRepo Insert Success");
//                                   Toast.makeText(UploadImagesNew.this, "Data Inserted into the table successfully", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(UploadImagesNew.this, "Data Insertion failed", Toast.LENGTH_SHORT).show();
//                                    Log.d(UploadImagesNew.class.getSimpleName(), "==>  Analysis ==> TABLE_FileRepo Insert Failed");
//                                }
//                            }
//                        });
                    }

                    if (IdPhotoPath !=null){

                        List<LinkedHashMap> idproofphoto = new ArrayList<>();
                        LinkedHashMap idproofmap = new LinkedHashMap();
                        idproofmap.put("FarmerCode",CommonConstants.FARMER_CODE.toString());
                        idproofmap.put("IDProofTypeId",60);
                        idproofmap.put("IdProofNumber",identityProofsList.get(0).getIdproofnumber());
                        idproofmap.put("IsActive",1);
                        idproofmap.put("CreatedByUserId",identityProofsList.get(0).getCreatedbyuserid());
                        idproofmap.put("CreatedDate",identityProofsList.get(0).getCreatedDate());
                        idproofmap.put("UpdatedByUserId",Integer.parseInt(CommonConstants.USER_ID));
                        idproofmap.put("UpdatedDate",CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
                        idproofmap.put("ServerUpdatedStatus",0);
                        idproofmap.put("FileName",CommonConstants.FARMER_CODE);
                        idproofmap.put("FileLocation",IdPhotoPath.toString());
                        idproofmap.put("FileExtension",CommonConstants.JPEG_FILE_SUFFIX);
                        idproofphoto.add(idproofmap);

                        dataAccessHandler.saveData(DatabaseKeys.TABLE_IDENTITYPROOF, idproofphoto, new ApplicationThread.OnComplete<String>() {
                            @Override
                            public void execute(boolean success, String result, String msg) {

                                if (success) {
                                    Log.d(UploadImagesNew.class.getSimpleName(), "==>  Analysis ==> TABLE_IDENTITYPROOF Insert Success");
                                    // Toast.makeText(UploadImagesNew.this, "Data Inserted into the table successfully", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(UploadImagesNew.this, "Data Insertion failed", Toast.LENGTH_SHORT).show();
                                    Log.d(UploadImagesNew.class.getSimpleName(), "==>  Analysis ==> TABLE_IDENTITYPROOF Insert Failed");
                                }
                            }
                        });

                    }

                    if (passbookimagepath !=null){

                        List<LinkedHashMap> passbookphoto = new ArrayList<>();
                        LinkedHashMap passbookmap = new LinkedHashMap();
                        passbookmap.put("FarmerCode",CommonConstants.FARMER_CODE.toString());
                        passbookmap.put("BankId",farmerBank.getBankId());
                        passbookmap.put("AccountHolderName",farmerBank.getAccountHolderName());
                        passbookmap.put("AccountNumber",farmerBank.getAccountNumber());
                        passbookmap.put("FileName",CommonConstants.FARMER_CODE);
                        passbookmap.put("FileLocation",passbookimagepath.toString());
                        passbookmap.put("FileExtension",CommonConstants.JPEG_FILE_SUFFIX);
                        passbookmap.put("IsActive",1);
                        passbookmap.put("CreatedByUserId",farmerBank.getCreatedByUserId());
                        passbookmap.put("CreatedDate",farmerBank.getCreatedDate());
                        passbookmap.put("UpdatedByUserId",Integer.parseInt(CommonConstants.USER_ID));
                        passbookmap.put("UpdatedDate",CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
                        passbookmap.put("ServerUpdatedStatus",0);

                        passbookphoto.add(passbookmap);

                        dataAccessHandler.saveData(DatabaseKeys.TABLE_FARMERBANK, passbookphoto, new ApplicationThread.OnComplete<String>() {
                            @Override
                            public void execute(boolean success, String result, String msg) {

                                if (success) {
                                    Log.d(UploadImagesNew.class.getSimpleName(), "==>  Analysis ==> TABLE_FARMERBANK Insert Success");
                                    //Toast.makeText(UploadImagesNew.this, "Data Inserted into the table successfully", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(UploadImagesNew.this, "Data Insertion failed", Toast.LENGTH_SHORT).show();
                                    Log.d(UploadImagesNew.class.getSimpleName(), "==>  Analysis ==> TABLE_FARMERBANK Insert Failed");
                                }
                            }
                        });

                    }


                    UiUtils.showCustomToastMessage("Data Inserted successfully", UploadImagesNew.this, 0);
                    finish();
                }
            }
        });
    }

    private boolean validations() {
        if ( mCurrentPhotoPath == null  &&  IdPhotoPath == null &&  passbookimagepath == null) {
            UiUtils.showCustomToastMessage("Please Upload at least one Image ", UploadImagesNew.this, 1);
            return false;
        }
        return true;
    }

    private void dispatchTakePictureIntent3(int cameraRequest3) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        switch (cameraRequest3) {
            case CAMERA_REQUEST3:
                File f = null;
                passbookimagepath = null;
                f = setUpPhotoFile3();
                passbookimagepath = f.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile(UploadImagesNew.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        f);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                break;

            default:
                break;
        } // switch
        startActivityForResult(takePictureIntent, cameraRequest3);
    }

    private File setUpPhotoFile3() {
        File f = createImageFile2();
        passbookimagepath = f.getAbsolutePath();

        return f;
    }

    private void dispatchTakePictureIntent2(int cameraRequest) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        switch (cameraRequest) {
            case CAMERA_REQUEST2:
                File f = null;
                IdPhotoPath = null;
                f = setUpPhotoFile2();
                IdPhotoPath = f.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile(UploadImagesNew.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        f);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                break;
        } // switch
        startActivityForResult(takePictureIntent, cameraRequest);
    }

    private File setUpPhotoFile2() {
        File f = createImageFile3();
        IdPhotoPath = f.getAbsolutePath();

        return f;
    }

    private File createImageFile3() {
        File pictureDirectory = new File(CommonUtils.get3FFileRootPath() + "3F_Pictures/" + "UploadedPassbookPhotos");
        if (!pictureDirectory.exists()) {
            pictureDirectory.mkdirs();
        }

        File finalFile = new File(pictureDirectory, CommonConstants.FARMER_CODE + CommonConstants.JPEG_FILE_SUFFIX);
        return finalFile;
    }

    private File createImageFile2() {
        File pictureDirectory = new File(CommonUtils.get3FFileRootPath() + "3F_Pictures/" + "UploadedIDPhotos");
        if (!pictureDirectory.exists()) {
            pictureDirectory.mkdirs();
        }

        File finalFile = new File(pictureDirectory, CommonConstants.FARMER_CODE + CommonConstants.JPEG_FILE_SUFFIX);
        return finalFile;
    }

    private void dispatchTakePictureIntent(int actionCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        switch (actionCode) {
            case CAMERA_REQUEST:
                File f = null;
                mCurrentPhotoPath = null;
                try {
                    f = setUpPhotoFile();
                    mCurrentPhotoPath = f.getAbsolutePath();
                    Uri photoURI = FileProvider.getUriForFile(UploadImagesNew.this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            f);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                } catch (IOException e) {
                    e.printStackTrace();
                    f = null;
                    mCurrentPhotoPath = null;
                }
                break;

            default:
                break;
        } // switch
        startActivityForResult(takePictureIntent, actionCode);
    }

    private File setUpPhotoFile() throws IOException {

        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();

        return f;
    }

    private File createImageFile() {
        File pictureDirectory = new File(CommonUtils.get3FFileRootPath() + "3F_Pictures/" + "UploadedFarmerPhotos");
        if (!pictureDirectory.exists()) {
            pictureDirectory.mkdirs();
        }

        File finalFile = new File(pictureDirectory, CommonConstants.FARMER_CODE + CommonConstants.JPEG_FILE_SUFFIX);
        return finalFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (resultCode == RESULT_OK) {
                    try {
                        handleBigCameraPhoto();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    mCurrentPhotoPath = null;
                    farmerimage.setImageDrawable(getDrawable(R.mipmap.ic_launcher));
                }
                break;
            }
            case CAMERA_REQUEST2: {
                if (resultCode == RESULT_OK) {
                    try {
                        handleBigCameraPhoto2();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    IdPhotoPath = null;
                    idproofimage.setImageDrawable(getDrawable(R.mipmap.ic_launcher));
                }
                break;
            }
            case CAMERA_REQUEST3: {
                if (resultCode == RESULT_OK) {
                    try {
                        handleBigCameraPhoto3();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    passbookimagepath = null;
                    bankpassbookimage.setImageDrawable(getDrawable(R.mipmap.ic_launcher));
                }
                break;
            }

        }

        // switch

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == UploadImagesNew.RESULT_OK){
                String result=data.getStringExtra("result");
                Log.d("result", result);

                recoveryfarmerexists = dataAccessHandler.getOnlyOneIntValueFromDb(Queries.getInstance().getIsRecoveryfarmerexists(selectedfarmercode));

                if (recoveryfarmerexists == 0){
                    addrecoveryfarmers.setText("Add Recovery Farmer");
                }else{
                    addrecoveryfarmers.setText("View Recovery Farmers");
                }
            }
            if (resultCode == UploadImagesNew.RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    }
    private void handleBigCameraPhoto3() throws Exception {
        if (passbookimagepath != null) {
            setPic3();
            galleryAddPic3();
        }
    }

    private void galleryAddPic3() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(passbookimagepath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        UploadImagesNew.this.sendBroadcast(mediaScanIntent);
    }

    private void setPic3() throws Exception {
        /* There isn't enough memory to open up more than a couple camera photos */
        /* So pre-scale the target bitmap into which the file is decoded */

        /* Get the size of the ImageView */
        int targetW = bankpassbookimage.getWidth();
        int targetH = bankpassbookimage.getHeight();

        /* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(passbookimagepath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        /* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        }

        /* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        /* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(passbookimagepath, bmOptions);
        getBytesFromBitmap(bitmap);
        bitmap = ImageUtility.rotatePicture(90, bitmap);
        bankpassbookimage.setImageBitmap(bitmap);

        bankpassbookimage.setVisibility(View.VISIBLE);
        isImage = true;
        bankpassbookimage.invalidate();
        submitBtn.setEnabled(true);
        submitBtn.setAlpha(1.0f);
    }

    private void handleBigCameraPhoto2() throws Exception {
        if (IdPhotoPath != null) {
            setPic2();
            galleryAddPic2();
        }
    }

    private void galleryAddPic2() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(IdPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        UploadImagesNew.this.sendBroadcast(mediaScanIntent);
    }

    private void setPic2() throws Exception {

        /* There isn't enough memory to open up more than a couple camera photos */
        /* So pre-scale the target bitmap into which the file is decoded */

        /* Get the size of the ImageView */
        int targetW = idproofimage.getWidth();
        int targetH = idproofimage.getHeight();

        /* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(IdPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        /* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        }

        /* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        /* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(IdPhotoPath, bmOptions);
        getBytesFromBitmap(bitmap);
        bitmap = ImageUtility.rotatePicture(90, bitmap);
        idproofimage.setImageBitmap(bitmap);

        idproofimage.setVisibility(View.VISIBLE);
        isImage = true;
        idproofimage.invalidate();
        submitBtn.setEnabled(true);
        submitBtn.setAlpha(1.0f);
    }

    private void handleBigCameraPhoto() throws Exception {

        if (mCurrentPhotoPath != null) {
            setPic();
            galleryAddPic();
        }
    }

    private void setPic() throws Exception {

        /* There isn't enough memory to open up more than a couple camera photos */
        /* So pre-scale the target bitmap into which the file is decoded */

        /* Get the size of the ImageView */
        int targetW = farmerimage.getWidth();
        int targetH = farmerimage.getHeight();

        /* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        /* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        }

        /* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        /* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        getBytesFromBitmap(bitmap);
        bitmap = ImageUtility.rotatePicture(90, bitmap);
        farmerimage.setImageBitmap(bitmap);

        farmerimage.setVisibility(View.VISIBLE);
        isImage = true;
        farmerimage.invalidate();
        submitBtn.setEnabled(true);
        submitBtn.setAlpha(1.0f);
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        bytes = stream.toByteArray();
        return stream.toByteArray();
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        UploadImagesNew.this.sendBroadcast(mediaScanIntent);
    }

}