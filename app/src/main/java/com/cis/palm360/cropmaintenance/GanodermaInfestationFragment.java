package com.cis.palm360.cropmaintenance;

import static android.app.Activity.RESULT_OK;
import static com.cis.palm360.database.Palm3FoilDatabase.LOG_TAG;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cis.palm360.BuildConfig;
import com.cis.palm360.R;
import com.cis.palm360.areaextension.UpdateUiListener;
import com.cis.palm360.cloudhelper.Log;
import com.cis.palm360.common.CommonConstants;
import com.cis.palm360.common.CommonUtils;
import com.cis.palm360.database.DataAccessHandler;
import com.cis.palm360.database.Queries;
import com.cis.palm360.datasync.helpers.DataManager;
import com.cis.palm360.dbmodels.Ganoderma;
import com.cis.palm360.dbmodels.GanodermaRefresh;
import com.cis.palm360.utils.ImageUtility;
import com.cis.palm360.utils.UiUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class GanodermaInfestationFragment extends Fragment implements View.OnClickListener, UpdateUiListener  {

    private UpdateUiListener updateUiListener;
    private View rootView;
    Toolbar toolbar;
    private ActionBar actionBar;
    public static RelativeLayout sec_rel;
    private LinearLayout parentLayout;
    private ImageView ganodermaImageView;
    private String[] PERMISSIONS_STORAGE = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private Context mContext;
    public static final int REQUEST_CAM_PERMISSIONS = 1;
    private static final int CAMERA_REQUEST = 1889;
    private String mCurrentPhotoPath,plantationImageId;
    private Ganoderma ganoderma;
    EditText yellowingOfLeavesTxt,leafDroopingAndDryingTxt,bracketsIdentifiedTxt,holeOnTheStemTxt,fallenPlantsTxt,
            QuantityinltrTxt,nooftimesappliedTxt, otherBioProductsTxt;
    Spinner trichodermaAppliedSpin;
    Button Ganodermasave,lastvisitdataBtn;
    LinearLayout QuantityinltrLayout,nooftimesappliedLayout;
    private DataAccessHandler dataAccessHandler;
    private ArrayList<Ganoderma> ganodermalastvisitdatamap;

    public GanodermaInfestationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_ganoderma_infestation, container, false);
        //        return inflater.inflate(R.layout.fragment_ganoderma_infestation, container, false);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        actionBar = activity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Ganoderma infestation");
        mContext = getActivity();
        initViews();
        setViews();
        bindViews();
        return rootView;
    }

    private void bindViews() {

        ganoderma = (Ganoderma) DataManager.getInstance().getDataFromManager(DataManager.GANODERMA_DETAILS);
        if (ganoderma != null) {
            yellowingOfLeavesTxt.setText(ganoderma.getYellowingOfLeaves()+"");
            leafDroopingAndDryingTxt.setText(ganoderma.getLeafDroopingAndDrying()+"");
            bracketsIdentifiedTxt.setText(ganoderma.getBracketsIdentified()+"");
            holeOnTheStemTxt.setText(ganoderma.getHoleOnTheStem()+"");
            fallenPlantsTxt.setText(ganoderma.getFallenPlants()+"");
            leafDroopingAndDryingTxt.setText(ganoderma.getLeafDroopingAndDrying()+"");
            otherBioProductsTxt.setText(ganoderma.getBioProductsUsed()+"");
            trichodermaAppliedSpin.setSelection((ganoderma.isTrichodermaApplied() ==    1) ? 1 : 2);
          //  holeOnTheStemTxt.setText(ganoderma.getYellowingOfLeaves()+"");
            if (!TextUtils.isEmpty(ganoderma.getFileLocation())) {
                 mCurrentPhotoPath = ganoderma.getFileLocation();
                Log.e("=========CurrentPhotoPath",mCurrentPhotoPath+"");
                loadImageFromStorage(ganoderma.getFileLocation());
                ganodermaImageView.invalidate();
            }


//                ganoderma.setYellowingOfLeaves(Integer.parseInt(yellowingOfLeavesTxt.getText().length()>0?yellowingOfLeavesTxt.getText().toString():"0"));

            if (trichodermaAppliedSpin.getSelectedItemPosition() == 1) { // "Yes"
                QuantityinltrTxt.setText(ganoderma.getQuantityInLt()+"");
                nooftimesappliedTxt.setText(ganoderma.getAppliedInAYear()+"");

            }

        }
    }
    private void loadImageFromStorage(String path) {
        File photoFiles = new File(path);
        if (photoFiles != null) {
            Uri uri = Uri.fromFile(photoFiles);
            if (uri != null) {
                Picasso.with(getActivity()).load(uri).into(ganodermaImageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
//                        renderImage();
                    }
                });
            }
        }
    }


    private void initViews() {
        dataAccessHandler = new DataAccessHandler(mContext);
        ganodermaImageView = (ImageView) rootView.findViewById(R.id.ganodermaImageView );
        sec_rel = (RelativeLayout) rootView.findViewById(R.id.sec_rel);
        parentLayout = (LinearLayout) rootView.findViewById(R.id.ganodermaInfestationLayout);
        yellowingOfLeavesTxt = (EditText) rootView.findViewById(R.id.yellowingOfLeavesTxt);
        leafDroopingAndDryingTxt = (EditText) rootView.findViewById(R.id.leafDroopingAndDryingTxt);
        bracketsIdentifiedTxt = (EditText) rootView.findViewById(R.id.bracketsIdentifiedTxt);
        holeOnTheStemTxt =(EditText) rootView.findViewById(R.id.holeOnTheStemTxt);
        fallenPlantsTxt =(EditText) rootView.findViewById(R.id.fallenPlantsTxt);
        trichodermaAppliedSpin = (Spinner) rootView.findViewById(R.id.trichodermaAppliedSpin);
        QuantityinltrTxt = (EditText) rootView.findViewById(R.id.QuantityinltrTxt);
        nooftimesappliedTxt =(EditText)rootView.findViewById(R.id.nooftimesappliedTxt);
        otherBioProductsTxt =(EditText) rootView.findViewById(R.id.otherBioProductsTxt);
        Ganodermasave =  (Button) rootView.findViewById(R.id.Ganodermasave);
        QuantityinltrLayout =(LinearLayout) rootView.findViewById(R.id.QuantityinltrLayout);
        nooftimesappliedLayout= (LinearLayout) rootView.findViewById(R.id.nooftimesappliedLayout);
        lastvisitdataBtn = (Button) rootView.findViewById(R.id.lastvisitdataBtn);


    }

    private void setViews() {

        parentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CommonUtilsNavigation.hideKeyBoard(getActivity());
                return false;
            }
        });
        sec_rel.setOnClickListener(this);
        Ganodermasave.setOnClickListener(this);
        // Spinner listener to toggle visibility
        trichodermaAppliedSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) { // Assuming 1 = "Yes"
                    QuantityinltrLayout.setVisibility(View.VISIBLE);
                    nooftimesappliedLayout.setVisibility(View.VISIBLE);
                   // otherBioProductsTxt.setVisibility(View.VISIBLE);
                } else {
                    QuantityinltrLayout.setVisibility(View.GONE);
                    nooftimesappliedLayout.setVisibility(View.GONE);
                    QuantityinltrTxt.setText("");
                    nooftimesappliedTxt.setText("");
                   // otherBioProductsTxt.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        lastvisitdataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(getContext());
            }
        });
    }

    public void showDialog(Context activity) {
        // Initialize the dialog
        final Dialog dialog = new Dialog(activity);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.ganodermalastvisteddata);

        // Toolbar setup
        Toolbar titleToolbar = dialog.findViewById(R.id.titleToolbar);
        titleToolbar.setTitle("Ganoderma History");
        titleToolbar.setTitleTextColor(activity.getResources().getColor(R.color.white));

        // Layouts
        LinearLayout mainLL = dialog.findViewById(R.id.ganomainlyt);
        LinearLayout quantityInLtrRel = dialog.findViewById(R.id.QuantityinltrrRel);
        LinearLayout noOfTimeLL = dialog.findViewById(R.id.nooftimell);

        // TextView elements
        TextView yellowingOfLeaves = dialog.findViewById(R.id.yellowingOfLeaves);
        TextView leafDroopingAndDrying = dialog.findViewById(R.id.leafDroopingAndDrying);
        TextView bracketsIdentified = dialog.findViewById(R.id.bracketsIdentified);
        TextView holeOnTheStem = dialog.findViewById(R.id.holeOnTheStem);
        TextView fallenPlants = dialog.findViewById(R.id.fallenPlants);
        TextView trichodermaApplied = dialog.findViewById(R.id.trichodermaApplied);
        TextView quantityInLtr = dialog.findViewById(R.id.Quantityinltr);
        TextView noOfTimesApplied = dialog.findViewById(R.id.nooftimesapplied);
        TextView otherBioProducts = dialog.findViewById(R.id.otherBioProducts);
        TextView noRecords = dialog.findViewById(R.id.hopnorecord_tv);

        // Fetch last visit data
        String lastVisitCode = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getLatestCropMaintanaceHistoryCode(CommonConstants.PLOT_CODE)
        );
        ArrayList<GanodermaRefresh> ganodermaLastVisitDataMap = (ArrayList<GanodermaRefresh>) dataAccessHandler.getGanodermaData(
                Queries.getInstance().getganodermahistory(lastVisitCode), 1
        );

        // Populate the dialog with data or show "no records"
        if (ganodermaLastVisitDataMap != null && !ganodermaLastVisitDataMap.isEmpty()) {
            GanodermaRefresh lastVisitData = ganodermaLastVisitDataMap.get(0);

            // Show data layout and hide "no records"
            noRecords.setVisibility(View.GONE);
            mainLL.setVisibility(View.VISIBLE);

            // Populate TextViews with data
            yellowingOfLeaves.setText(lastVisitData.getYellowingOfLeaves()+"");
            leafDroopingAndDrying.setText(lastVisitData.getLeafDroopingAndDrying()+"");
            bracketsIdentified.setText(lastVisitData.getBracketsIdentified()+"");
            holeOnTheStem.setText(lastVisitData.getHoleOnTheStem()+"");
            fallenPlants.setText(lastVisitData.getFallenPlants()+"");
            otherBioProducts.setText(lastVisitData.getBioProductsUsed()+"");

            boolean isTrichodermaApplied = lastVisitData.isTrichodermaApplied() == 1;
            trichodermaApplied.setText(isTrichodermaApplied ? "Yes" : "No");

            // Show or hide additional data based on Trichoderma application
            if (isTrichodermaApplied) {
                quantityInLtrRel.setVisibility(View.VISIBLE);
                noOfTimeLL.setVisibility(View.VISIBLE);
                quantityInLtr.setText(String.valueOf(lastVisitData.getQuantityInLt()));
                noOfTimesApplied.setText(String.valueOf(lastVisitData.getAppliedInAYear()));
            } else {
                quantityInLtrRel.setVisibility(View.GONE);
                noOfTimeLL.setVisibility(View.GONE);
            }

        } else {
            // Show "no records" and hide data layout
            mainLL.setVisibility(View.GONE);
            noRecords.setVisibility(View.VISIBLE);
        }

        // Show the dialog
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Ganodermasave:

             if (validateFields()) {
                  //  savePictureData();
                ganoderma = new Ganoderma();
                ganoderma.setYellowingOfLeaves(Integer.parseInt(yellowingOfLeavesTxt.getText().length() > 0 ? yellowingOfLeavesTxt.getText().toString():"0"));
                ganoderma.setLeafDroopingAndDrying(Integer.parseInt(leafDroopingAndDryingTxt.getText().length() > 0 ? leafDroopingAndDryingTxt.getText().toString():"0"));
                ganoderma.setBracketsIdentified(Integer.parseInt(bracketsIdentifiedTxt.getText().length() > 0 ? bracketsIdentifiedTxt.getText().toString():"0"));
                ganoderma.setHoleOnTheStem(Integer.parseInt(holeOnTheStemTxt.getText().length() > 0 ? holeOnTheStemTxt.getText().toString():"0"));
                ganoderma.setFallenPlants(Integer.parseInt(fallenPlantsTxt.getText().length() > 0 ? fallenPlantsTxt.getText().toString():"0"));
                ganoderma.setTrichodermaApplied(trichodermaAppliedSpin.getSelectedItemPosition() == 1 ? 1 : 0);
                ganoderma.setBioProductsUsed(otherBioProductsTxt.getText()+"");
                ganoderma.setFileName("GanodermaImage");
                 ganoderma.setFileLocation(mCurrentPhotoPath);
                 ganoderma.setFileExtension(CommonConstants.JPEG_FILE_SUFFIX);

//                ganoderma.setYellowingOfLeaves(Integer.parseInt(yellowingOfLeavesTxt.getText().length()>0?yellowingOfLeavesTxt.getText().toString():"0"));

                 if (trichodermaAppliedSpin.getSelectedItemPosition() == 1) { // "Yes"
                     ganoderma.setQuantityInLt(TextUtils.isEmpty(QuantityinltrTxt.getText().toString()) == true ? 0.0 : Double.parseDouble(QuantityinltrTxt.getText().toString()));
                     ganoderma.setAppliedInAYear(Integer.parseInt(nooftimesappliedTxt.getText().length() > 0 ? nooftimesappliedTxt.getText().toString():"0"));

                 }
                 DataManager.getInstance().addData(DataManager.GANODERMA_DETAILS, ganoderma);
                 getFragmentManager().popBackStack();
             //    updateUiListener.updateUserInterface(0);
                 if(updateUiListener!=null){
                     updateUiListener.updateUserInterface(0);}
             }
                CommonUtilsNavigation.hideKeyBoard(getActivity());
                break;
            case R.id.sec_rel:
//                CommonUtils.profilePic(getActivity());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (!CommonUtils.isPermissionAllowed(getActivity(), Manifest.permission.CAMERA))) {
                    android.util.Log.v(LOG_TAG, "Camera Permissions Not Granted");
                    ActivityCompat.requestPermissions(
                            getActivity(),
                            PERMISSIONS_STORAGE,
                            REQUEST_CAM_PERMISSIONS
                    );
                } else {
                    // CommonUtils.profilePic(getActivity());
                    dispatchTakePictureIntent(CAMERA_REQUEST);
                }
                break;
        }
    }

    private boolean validateFields() {
        // Check if the photo is captured
        if (mCurrentPhotoPath == null || TextUtils.isEmpty(mCurrentPhotoPath)) {
            UiUtils.showCustomToastMessage("Please Capture Ganoderma Affected Tree Photo", getActivity(), 0);
            return false;
        }

        // Check "Yellowing Of Leaves"
        if (yellowingOfLeavesTxt.getText().toString().trim().isEmpty()) {
            UiUtils.showCustomToastMessage("Please Enter Yellowing Of Leaves", getActivity(), 0);
            return false;
        }

        // Check "Leaf Drooping And Drying"
        if (leafDroopingAndDryingTxt.getText().toString().trim().isEmpty()) {
            UiUtils.showCustomToastMessage("Please Enter Leaf Drooping And Drying", getActivity(), 0);
            return false;
        }

        // Check "Brackets Identified"
        if (bracketsIdentifiedTxt.getText().toString().trim().isEmpty()) {
            UiUtils.showCustomToastMessage("Please Enter Brackets Identified", getActivity(), 0);
            return false;
        }

        // Check "Hole On The Stem"
        if (holeOnTheStemTxt.getText().toString().trim().isEmpty()) {
            UiUtils.showCustomToastMessage("Please Enter Hole On The Stem/Putrefaction Of Stem", getActivity(), 0);
            return false;
        }

        // Check "Fallen Plants"
        if (fallenPlantsTxt.getText().toString().trim().isEmpty()) {
            UiUtils.showCustomToastMessage("Please Enter Fallen Plants", getActivity(), 0);
            return false;
        }

        // Check "Trichoderma Applied" condition
        if (trichodermaAppliedSpin.getSelectedItemPosition() == 0 || trichodermaAppliedSpin.getSelectedItem() == null) {
            UiUtils.showCustomToastMessage("Please Select Trichoderma Applied", getActivity(), 0);
            return false;
        }
        // Check "Trichoderma Applied" condition
        if (CommonUtilsNavigation.spinnerSelect("Trichoderma Applied", trichodermaAppliedSpin.getSelectedItemPosition(), getActivity())) {
            if (trichodermaAppliedSpin.getSelectedItemPosition() == 1) { // Assuming "Yes" is at position 1
                // Validate the dependent fields
                if (QuantityinltrTxt.getText().toString().trim().isEmpty()) {
                    UiUtils.showCustomToastMessage("Please Enter Quantity In Liters", getActivity(), 0);
                    return false;
                }
                if (nooftimesappliedTxt.getText().toString().trim().isEmpty()) {
                    UiUtils.showCustomToastMessage("Please Enter No Of Times Applied In A Year", getActivity(), 0);
                    return false;
                }
            }

        }

        // Check "Other Bio Products Used" only after resolving "Trichoderma Applied"
        if (otherBioProductsTxt.getText().toString().trim().isEmpty()) {
            UiUtils.showCustomToastMessage("Please Enter Other Bio Products Used If Any Please Mention", getActivity(), 0);
            return false;
        }

        // If all fields are valid
        return true;
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
                    Uri photoURI = FileProvider.getUriForFile(getActivity(),
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
Log.e("=========mCurrentPhotoPath",mCurrentPhotoPath+"");
        return f;
    }

    private File createImageFile() throws IOException {
        File pictureDirectory = new File(CommonUtils.get3FFileRootPath() + "/3F_Pictures/" + "ganodermaPhotos");
        if (!pictureDirectory.exists()) {
            pictureDirectory.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        // Create the file with the timestamp
        File finalFile = new File(pictureDirectory, timeStamp + "_" + CommonConstants.PLOT_CODE  + CommonConstants.JPEG_FILE_SUFFIX);

      //  File finalFile = new File(pictureDirectory, CommonConstants.PLOT_CODE + CommonConstants.JPEG_FILE_SUFFIX);
        return finalFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (resultCode == RESULT_OK) {
                    handleBigCameraPhoto();
                }
                break;
            } // ACTION_TAKE_PHOTO_B

        } // switch
    }

    private void handleBigCameraPhoto() {

        if (mCurrentPhotoPath != null) {
            setPic();
            galleryAddPic();
//            mCurrentPhotoPath = null;
        }

    }

    private void setPic() {

        /* There isn't enough memory to open up more than a couple camera photos */
        /* So pre-scale the target bitmap into which the file is decoded */

        /* Get the size of the ImageView */
        int targetW = ganodermaImageView.getWidth();
        int targetH = ganodermaImageView.getHeight();

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

        bitmap = ImageUtility.rotatePicture(90, bitmap);
        ganodermaImageView.setImageBitmap(bitmap);
        ganodermaImageView.setVisibility(View.VISIBLE);
//        farmerIcon.setVisibility(View.GONE);
        ganodermaImageView.invalidate();
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    @Override
    public void updateUserInterface(int refreshPosition) {

    }


    public void setUpdateUiListener(UpdateUiListener updateUiListener) {
        this.updateUiListener = updateUiListener;
    }
}
