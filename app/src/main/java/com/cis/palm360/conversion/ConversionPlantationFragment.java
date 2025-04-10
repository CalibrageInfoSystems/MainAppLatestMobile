
package com.cis.palm360.conversion;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cis.palm360.BuildConfig;
import com.cis.palm360.R;
import com.cis.palm360.areacalculator.LocationProvider;
import com.cis.palm360.areaextension.UpdateUiListener;
import com.cis.palm360.cloudhelper.Log;
import com.cis.palm360.common.CommonConstants;
import com.cis.palm360.common.CommonUtils;
import com.cis.palm360.common.OilPalmException;
import com.cis.palm360.cropmaintenance.CommonUtilsNavigation;
import com.cis.palm360.database.DataAccessHandler;
import com.cis.palm360.database.Queries;
import com.cis.palm360.datasync.helpers.DataManager;
import com.cis.palm360.dbmodels.Farmer;
import com.cis.palm360.dbmodels.FileRepository;
import com.cis.palm360.dbmodels.NurserySaplingDetails;
import com.cis.palm360.dbmodels.Plantation;
import com.cis.palm360.dbmodels.Plot;
import com.cis.palm360.dbmodels.PlotCurrentCrop;
import com.cis.palm360.ui.BaseFragment;
import com.cis.palm360.uihelper.InteractiveScrollView;
import com.cis.palm360.utils.UiUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static com.cis.palm360.common.CommonUtils.isFromConversion;
import static com.cis.palm360.common.CommonUtils.isFromCropMaintenance;
import static com.cis.palm360.common.CommonUtils.isFromFollowUp;
import static com.cis.palm360.farmersearch.DisplayPlotsFragment.getLocationProvider;


//Used to enter number of saplings planted
public class ConversionPlantationFragment extends BaseFragment implements View.OnClickListener, PalmDetailsEditListener, SaplingsPlantedListner {

    public static final int REQUEST_CAM_PERMISSIONS = 1;

    private static final String LOG_TAG = ConversionPlantationFragment.class.getName();
   // public ImageView scrollBottomIndicator, farmer_image;
    private EditText areaallocatedEdt, saplingcounttreesEdt, plotArea,
            dateofplantingEdt, reasonformissingsaplings;
    private FileRepository savedPictureData = null;

    private Spinner sourceofsaplingsSpin, cropSpin, varietySpin, reasonSpin, saplingNurserySpin, saplingVendorSpin;
    private Button farmerSaveBtn, addbtn;
    private LinearLayout recyclerItemsLayout;
    private InteractiveScrollView interactiveScrollView;
    private LinkedHashMap<String, String> reasonDataMap, varietyDataMap, sourceofsaplingsMap, cropDataMap, saplingVendorMap, saplingNurseryMap;
    private DataAccessHandler dataAccessHandler;
    private Context mContext;
    private int croppos;
    public double TotalPlotArea,LeftOutArea,palmOilArea, allocatedTreeCount;
    public boolean isComentRequired = false;
    private Calendar myCalendar = Calendar.getInstance();
    private String[] PERMISSIONS_STORAGE = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private Bitmap userImageData;
    private File fileToUpload;
    private byte[] imageData = null;
    private LinearLayout parentLayout, saplingLayout, reasonformissingsaplinglyt;
    private List<Plantation> plantationList,plantationObject;
    private RecyclerView palmDetailsList;
    private PalmDetailsAdaptor itemListDataAdapter;
    private UpdateUiListener updateUiListener;
    private boolean isUpdate;
    private boolean fromDataBase = false;
    private static final int CAMERA_REQUEST = 1888;
    private String mCurrentPhotoPath ;
    private Integer planted, totalplantedsaplings = 0, totalTreecount = 0;
    private TextView dopTxt,missingData;
    private ImageView iv_plantaion;
    private List<NurserySaplingDetails> nurserySaplingDetails;

    private DecimalFormat df = new DecimalFormat("#.##");
    double  area;
    @SuppressLint("SimpleDateFormat")
    private DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    private DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
    private String cropId;
    private double totalPlotArea;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private Uri photoUri;
    private LocationManager locationManager;
    private static LocationProvider mLocationProvider;

    private double latitude, longitude;
    private String currentPhotoPath;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    public ConversionPlantationFragment() {

    }

//Initializing the Class & set data
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void Initialize() {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View parentView = inflater.inflate(R.layout.frag_conversion_plantationdetails, null);
        baseLayout.addView(parentView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setTile(getActivity().getResources().getString(R.string.plantation_details));

        mContext = getActivity();
        dataAccessHandler = new DataAccessHandler(getActivity());
        savedPictureData = new FileRepository();

        totalPlotArea = Double.parseDouble(dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getTotalPlotArea(CommonConstants.PLOT_CODE)));

        parentLayout = parentView.findViewById(R.id.parent_layout);
        interactiveScrollView = parentView.findViewById(R.id.scrollView);
        farmerSaveBtn = parentView.findViewById(R.id.plantationSaveBtn);
        addbtn = parentView.findViewById(R.id.addBtn);
        reasonSpin = parentView.findViewById(R.id.reasonSpin);
        dateofplantingEdt = parentView.findViewById(R.id.date_of_plantingEdt);
        dopTxt = parentView.findViewById(R.id.dopTxt);
        dopTxt.setText(CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMM));
        plotArea = parentView.findViewById(R.id.plot_area_edt);

        reasonformissingsaplings = parentView.findViewById(R.id.reasonformissingsaplings);
        reasonformissingsaplinglyt = parentView.findViewById(R.id.reason_lyt);
        saplingcounttreesEdt = parentView.findViewById(R.id.saplingcounttreesEdt);
        areaallocatedEdt = parentView.findViewById(R.id.area_allocatedEdt);
        varietySpin = parentView.findViewById(R.id.varietySpin);
        cropSpin = parentView.findViewById(R.id.cropSpin);
        sourceofsaplingsSpin = parentView.findViewById(R.id.source_of_saplingsSpin);
        saplingVendorSpin = parentView.findViewById(R.id.saplingVendorSpin);
        saplingNurserySpin = parentView.findViewById(R.id.saplingNurserySpin);
        palmDetailsList = parentView.findViewById(R.id.varietyCropRecyclerView);
        iv_plantaion = parentView.findViewById(R.id.iv_plantaion);
        missingData = parentView.findViewById(R.id.missingData);


        parentLayout.setOnTouchListener((view, motionEvent) -> {
            CommonUtilsNavigation.hideKeyBoard(getActivity());
            return false;
        });
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, locationListener);
        }
        if (ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            getLocation();

        }
        iv_plantaion.setOnClickListener(view -> {
            if (android.os.Build.VERSION.SDK_INT >= 29) {
                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    getLocationDetails();


                }
            } else {
                if (CommonUtils.isLocationPermissionGranted(getActivity())) {

                    getLocationDetails();

                }
            }
            if (latitude == 0.0 || longitude == 0.0) {
                Toast.makeText(getActivity(), "Fetching location... Please wait!", Toast.LENGTH_SHORT).show();
//                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (!CommonUtils.isPermissionAllowed(getActivity(), Manifest.permission.CAMERA))) {
                android.util.Log.v(LOG_TAG, "Location Permissions Not Granted");
                ActivityCompat.requestPermissions(
                        getActivity(),
                        PERMISSIONS_STORAGE,
                        REQUEST_CAM_PERMISSIONS
                );
            } else {
                dispatchTakePictureIntent(CAMERA_REQUEST);
                Log.e("Location", "Location: " + latitude + "  " + longitude);

            }


        });
//        iv_plantaion.setOnClickListener(view -> {
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (!CommonUtils.isPermissionAllowed(getActivity(), Manifest.permission.CAMERA))) {
//                android.util.Log.v(LOG_TAG, "Location Permissions Not Granted");
//                ActivityCompat.requestPermissions(
//                        getActivity(),
//                        PERMISSIONS_STORAGE,
//                        REQUEST_CAM_PERMISSIONS
//                );
//            } else {
//                dispatchTakePictureIntent(CAMERA_REQUEST);
//            }
//
//
//        });

//        saplingsPlanted.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//                if (!editable.toString().equalsIgnoreCase("")) {
//
//                    planted = Integer.parseInt(editable.toString());
//                    Log.d("countofsaplings", planted + "");
//
//                    Integer totaldispatchedsaplings = 0;
//
//                    for (int i = 0; i < plantationList.size(); i++){
//
//                        totaldispatchedsaplings = totaldispatchedsaplings+plantationList.get(i).getTreescount();
//                    }
//
//                    if (planted < totaldispatchedsaplings){
//
//                        missingsaplings.setText(totaldispatchedsaplings - planted + "");
//                        missingsaplinglyt.setVisibility(View.VISIBLE);
//                        reasonformissingsaplinglyt.setVisibility(View.VISIBLE);
//                    }else{
//                        missingsaplinglyt.setVisibility(View.GONE);
//                        reasonformissingsaplinglyt.setVisibility(View.GONE);
//
//                    }
//
//                }else {
//                    missingsaplinglyt.setVisibility(View.GONE);
//                    reasonformissingsaplinglyt.setVisibility(View.GONE);
//
//                }
//
//
//
//
//
//            }
//        });

        recyclerItemsLayout = parentView.findViewById(R.id.saplingLayout);

        plantationList = new ArrayList<>();
        farmerSaveBtn.setOnClickListener(this);

        cropId = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getCropId());

        cropDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getCropsMasterInfo());
        cropSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(mContext, "CropName", cropDataMap));

        List<String> cropIndexes = new ArrayList<>(cropDataMap.keySet());
        croppos = cropIndexes.indexOf("1") + 1;
        cropSpin.setSelection(croppos);

        reasonDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getLookUpData("3"));
        reasonSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(mContext, "SwapReasonName", reasonDataMap));

        varietyDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getLookUpData("18"));
       // ArrayAdapter<String> adapterSet = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, CommonUtils.removeDuplicates(varietyDataMap, "varietyType"));
       // adapterSet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       // varietySpin.setAdapter(adapterSet);

        sourceofsaplingsMap = dataAccessHandler.getGenericData(Queries.getInstance().getLookUpData("17"));
       // sourceofsaplingsSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(mContext, "" + getString(R.string.source_of_saplings), sourceofsaplingsMap));

        saplingNurseryMap = dataAccessHandler.getGenericData(Queries.getInstance().getSaplingsNursery());
       // saplingNurserySpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(mContext, "Saplings nursery", saplingNurseryMap));

        saplingVendorMap = dataAccessHandler.getGenericData(Queries.getInstance().getLookUpData("19"));
      //  saplingVendorSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(mContext, "Saplings vendor", saplingVendorMap));

        nurserySaplingDetails = dataAccessHandler.getNurserySaplingDetails(Queries.getInstance().getNurserySaplings(CommonConstants.PLOT_CODE));
        

      //  Toast.makeText(getActivity(),""+nurserySaplingDetails.size()+"vvvvv"+nurserySaplingDetails.get(0).getNurseryId(),Toast.LENGTH_LONG).show();

        if (!(nurserySaplingDetails.isEmpty()))
        {

            if(nurserySaplingDetails.get(0).getNurseryId() !=0)
            {
                itemListDataAdapter = new PalmDetailsAdaptor(getActivity(), plantationList, saplingVendorMap, sourceofsaplingsMap,saplingNurseryMap,varietyDataMap, this);

                itemListDataAdapter.setEditClickListener(this);
                palmDetailsList.setHasFixedSize(true);
                palmDetailsList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                palmDetailsList.setAdapter(itemListDataAdapter);

                plantationList = (List<Plantation>) DataManager.getInstance().getDataFromManager(DataManager.PLANTATION_CON_DATA);

            }
            else {
                missingData.setVisibility(View.VISIBLE);
                farmerSaveBtn.setVisibility(View.INVISIBLE);
            }

        }



        final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        if (plantationList == null) {
            fromDataBase = true;
        }

        if ((plantationList == null || plantationList.isEmpty()) && (isFromFollowUp() || isFromCropMaintenance() || isFromConversion())) {
            plantationList = (ArrayList<Plantation>) dataAccessHandler.getPlantationData(Queries.getInstance().getPlantatiobData(CommonConstants.PLOT_CODE), 1);
        }

        if (null != plantationList && !plantationList.isEmpty()) {

            int lifted = 0, planted = 0;

            for (int i= 0; i< plantationList.size(); i++){

                lifted = plantationList.get(i).getTreescount();
                planted = plantationList.get(i).getSaplingsplanted();

                Log.d("lifted", lifted + "");
                Log.d("planted", planted + "");
            }

            int difference = lifted - planted;

            if (difference > 0){
                reasonformissingsaplinglyt.setVisibility(View.VISIBLE);
                reasonformissingsaplings.setText(plantationList.get(0).getMissingPlantsComments());

//                List<Plantation> finalpalntationdata = itemListDataAdapter.getSendlist();
//                for (Plantation item:finalpalntationdata
//                ) {
//                    item.setMissingPlantsComments(reasonformissingsaplings.getText().toString());
//                }
//
//                DataManager.getInstance().addData(DataManager.PLANTATION_CON_DATA, finalpalntationdata);

            }else{
                reasonformissingsaplinglyt.setVisibility(View.GONE);
            }

            Log.d("difference", difference + "");
            Log.d("lifted1", lifted + "");
            Log.d("planted1", planted + "");

        }


        if (null != plantationList && !plantationList.isEmpty()) {
            isUpdate = true;
            palmDetailsList.setVisibility(View.VISIBLE);
            recyclerItemsLayout.setVisibility(View.VISIBLE);
            bindData();
        } else {
            plantationList = new ArrayList<>();
            viewPlantationList();
        }
    }
    private void getLocationDetails() {
        String latlong[] = getLatLong(getActivity(), false).split("@");

        if (!TextUtils.isEmpty(latlong[0]) && !TextUtils.isEmpty(latlong[1])) {
            latitude = Double.parseDouble(latlong[0]);
            longitude = Double.parseDouble(latlong[1]);
        }

    }
    private static String latLong = "";

    public String getLatLong(Context context, boolean showDialog) {

        mLocationProvider = getLocationProvider(context, showDialog);

        if (mLocationProvider != null) {
            latLong = mLocationProvider.getLatitudeLongitude();


        }
        return latLong;
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            android.util.Log.d("Location Update", "Latitude: " + latitude + ", Longitude: " + longitude);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Deprecated in API level 30, but still required for lower versions
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            android.util.Log.d("Location", "Provider enabled: " + provider);
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            android.util.Log.d("Location", "Provider disabled: " + provider);
        }
    };

    private void getLocation() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    android.util.Log.e("latitude========$", latitude + " longitude " + longitude);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    // Required for compatibility with older API versions
                }

                @Override
                public void onProviderEnabled(@NonNull String provider) {
                    android.util.Log.d("Location", "Provider enabled: " + provider);
                }

                @Override
                public void onProviderDisabled(@NonNull String provider) {
                    android.util.Log.d("Location", "Provider disabled: " + provider);
                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    //Binding Data to plantation model
    private void viewPlantationList() {
        List<Plantation> imported = new ArrayList<>(),indegenous = new ArrayList<>();
        allocatedTreeCount = Double.valueOf((dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getNurserySaplingsArea(CommonConstants.PLOT_CODE))));
        palmOilArea = Double.valueOf((dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getAdvanceReceivedArea(CommonConstants.PLOT_CODE))));

//        area = Double.parseDouble(df.format(allocatedTreeCount/143));
//        Log.d("area is", area + "");
//        Log.d("allocatedTreeCount is", allocatedTreeCount + "");

        double totaltresscount = 0.0;

        if (!nurserySaplingDetails.isEmpty()){

            if(nurserySaplingDetails.get(0).getNurseryId()!=0)
            {
                for(int i=0;i<nurserySaplingDetails.size();i++)
                { double tresscount = Double.parseDouble(df.format(nurserySaplingDetails.get(i).getNoOfSaplingsDispatched()));
                    totaltresscount = tresscount + totaltresscount;
                  double  area = tresscount/143;
                  area  = Double.parseDouble(String.format("%.2f", area));
                  Log.d("CIS", "TRESS COUNT VALIDATE ======> tressCount :"+tresscount +"      area :"+area);

                    Plantation plantation = new Plantation();
                    plantation.setNurserycode(nurserySaplingDetails.get(i).getNurseryId());
                    plantation.setSaplingvendorid(nurserySaplingDetails.get(i).getSaplingVendorId());
                    plantation.setSaplingsourceid(nurserySaplingDetails.get(i).getSaplingSourceId());
                    plantation.setCropVarietyId(nurserySaplingDetails.get(i).getCropVarietyId());
                    plantation.setAllotedarea(area);
                    plantation.setTreescount(nurserySaplingDetails.get(i).getNoOfSaplingsDispatched());
                    plantation.setReasonTypeId(null);

//                    if(nurserySaplingDetails.get(i).getSaplingSourceId() == 55)
//                    {
//
//                        if(imported.size() > 0)
//                        {
//                            int currentcount =  imported.get(0).getTreescount();
//                            imported.get(0).setTreescount(currentcount +plantation.getTreescount());
//                        }else{
//                            imported.add(plantation);
//                        }
//
//                    }else if(nurserySaplingDetails.get(i).getSaplingSourceId() == 56){
//                        if(indegenous.size() > 0)
//                        {
//                            int currentcount =  indegenous.get(0).getTreescount();
//                            indegenous.get(0).setTreescount(currentcount +plantation.getTreescount());
//                        }else{
//                            indegenous.add(plantation);
//                        }
//                    }
                    plantationList.add(plantation);
                    itemListDataAdapter.updateData(plantationList);
                }

                Log.d("totaltresscount", totaltresscount  +"");

                Log.d("TotalPalmAreaCalculation", totaltresscount/143 + "");
                Double value = Double.valueOf(String.format("%.2f", totaltresscount/143));
                Log.d("TotalPalmAreaCalculationValue", value + "");
                plotArea.setText(""+value);



//                plantationList.addAll(imported);
//                plantationList.addAll(indegenous);

                itemListDataAdapter.updateData(plantationList);
            }

        }
             //plotArea.setText(""+palmOilArea);


    }


    //Binding data to fields
    private void bindData() {
        Plot selectedPlot = null;
        if (fromDataBase) {
            selectedPlot = (Plot) dataAccessHandler.getSelectedPlotData(Queries.getInstance().getSelectedPlot(CommonConstants.PLOT_CODE), 0);
        } else {
            selectedPlot = (Plot) DataManager.getInstance().getDataFromManager(DataManager.PLOT_DETAILS);
        }


        itemListDataAdapter.updateData(plantationList);
       plotArea.setText("" + selectedPlot.getTotalpalmarea());

         String input = selectedPlot.getDateofPlanting();

        Date date = null;
        try {
            date = outputFormat.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String output = inputFormat.format(date);

        dopTxt.setText(output);
        int cropPos = CommonUtils.getIndex(cropDataMap.keySet(), String.valueOf(selectedPlot.getLeftoutareacropid())) + 1;
        cropSpin.setSelection(cropPos);

        int reasonPos = CommonUtils.getIndex(reasonDataMap.keySet(), String.valueOf(selectedPlot.getSwapingReasonId())) + 1;
        reasonSpin.setSelection(reasonPos);

        savedPictureData = (FileRepository) DataManager.getInstance().getDataFromManager(DataManager.CP_FILE_REPOSITORY_PLANTATION);
        if (savedPictureData != null && !TextUtils.isEmpty(savedPictureData.getPicturelocation())) {
            mCurrentPhotoPath = savedPictureData.getPicturelocation();
            loadImageFromStorage(savedPictureData.getPicturelocation());
            iv_plantaion.invalidate();
        }

    }

    //Save Image data
    private void savePictureData() throws Exception {
      if (savedPictureData == null){
          savedPictureData = new FileRepository();
      }
        savedPictureData.setFarmercode(CommonConstants.FARMER_CODE);
        savedPictureData.setPlotcode(CommonConstants.PLOT_CODE);
        savedPictureData.setModuletypeid(303);
        savedPictureData.setFilename(CommonConstants.PLOT_CODE);
        savedPictureData.setPicturelocation(mCurrentPhotoPath);
        savedPictureData.setFileextension(CommonConstants.JPEG_FILE_SUFFIX);
        savedPictureData.setCreatedbyuserid(Integer.parseInt(CommonConstants.USER_ID));
        savedPictureData.setCreatedDate(CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
        savedPictureData.setUpdatedbyuserid(Integer.parseInt(CommonConstants.USER_ID));
        savedPictureData.setServerUpdatedStatus(0);
        savedPictureData.setIsActive(1);
        savedPictureData.setUpdatedDate(CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
//        plotCodes.add(CommonConstants.PLOT_CODE);

        DataManager.getInstance().addData(DataManager.CP_FILE_REPOSITORY_PLANTATION, savedPictureData);
        addCurrentCropData();

    }

    //Adding current crop data to the model
    private void addCurrentCropData() {
        PlotCurrentCrop plotCurrentCrop = new PlotCurrentCrop();
        plotCurrentCrop.setCropid(Integer.valueOf(cropId));
        plotCurrentCrop.setCurrentcroparea(Double.parseDouble(plotArea.getText().toString()));
        plotCurrentCrop.setPlotcode(CommonConstants.PLOT_CODE);
        plotCurrentCrop.setIsactive(1);
        plotCurrentCrop.setCreatedbyuserid(Integer.parseInt(CommonConstants.USER_ID));
        plotCurrentCrop.setUpdatedbyuserid(Integer.parseInt(CommonConstants.USER_ID));
        plotCurrentCrop.setCreateddate(CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
        plotCurrentCrop.setUpdateddate(CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
        plotCurrentCrop.setServerupdatedstatus(0);

        DataManager.getInstance().addData(DataManager.PlotCurrent_Crop_Conversion, plotCurrentCrop);
        updateUiListener.updateUserInterface(0);
        getFragmentManager().popBackStack();

    }

    //Handling Image methods
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

        return f;
    }

    private File createImageFile() {
        File pictureDirectory = new File(CommonUtils.get3FFileRootPath() + "3F_Pictures/" + "ConversionPlantaionPhotos");
        if (!pictureDirectory.exists()) {
            pictureDirectory.mkdirs();
        }
        File finalFile = new File(pictureDirectory, CommonConstants.PLOT_CODE + CommonConstants.JPEG_FILE_SUFFIX);
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
                }
                break;
            }


        } // switch
    }

    private void handleBigCameraPhoto() {

        if (mCurrentPhotoPath != null) {
            setPic();
            addTimestampToImage();
            galleryAddPic();
        }

    }

    private void addTimestampToImage() {
        try {
            File imageFile = new File(mCurrentPhotoPath);
            Uri photoUri = Uri.fromFile(imageFile);

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);

            // **Correct the rotation**
            bitmap = rotateImageIfRequired(bitmap, mCurrentPhotoPath);

            // Convert to mutable bitmap
            Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(mutableBitmap);
            Paint paint = new Paint();
            Paint backgroundPaint = new Paint();

            // **Text Settings**
            paint.setColor(Color.WHITE);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            paint.setTextSize(60);
            paint.setAntiAlias(true);
            paint.setShadowLayer(5.0f, 2.0f, 2.0f, Color.BLACK);
            paint.setTextAlign(Paint.Align.LEFT);

            // **Background Settings**
            backgroundPaint.setColor(Color.BLACK);
            backgroundPaint.setAlpha(180);

            // **Prepare Text**
            String timeStamp = "Date: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault()).format(new Date());
            String locationText = "Lat: " + latitude + "  Long: " + longitude;
            String addressText = "Address:  " + fetchAddress();

            // **Wrap Long Address into Multiple Lines**
            List<String> wrappedAddress = wrapText(addressText, paint, canvas.getWidth() * 0.8f);

            // **Calculate Text Positions**
            float xPos = canvas.getWidth() * 0.01f;
            float yPos = canvas.getHeight() - 350;
            float padding = 20;
            float rectHeight = (paint.getTextSize() * (3 + wrappedAddress.size())) + (padding * 2);

            // **Draw Background Rectangle**
            RectF backgroundRect = new RectF(
                    xPos - padding,
                    yPos - (paint.getTextSize() + padding),
                    canvas.getWidth() - 40,
                    yPos + rectHeight
            );
            canvas.drawRoundRect(backgroundRect, 15, 15, backgroundPaint);

            // **Draw Text on Canvas**
            canvas.drawText(timeStamp, xPos, yPos, paint);
            canvas.drawText(locationText, xPos, yPos + paint.getTextSize() * 1.5f, paint);

            // **Ensure Address is Split into Two Lines Only**
            float addressYPos = yPos + paint.getTextSize() * 3f;
            int maxLines = 2;
            for (int i = 0; i < Math.min(wrappedAddress.size(), maxLines); i++) {
                canvas.drawText(wrappedAddress.get(i), xPos, addressYPos, paint);
                addressYPos += paint.getTextSize() * 1.3f;
            }

            // **Set Updated Image in ImageView**
            iv_plantaion.setImageBitmap(mutableBitmap);
            saveImage(mutableBitmap);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Failed to load image", Toast.LENGTH_SHORT).show();
        }
    }

    // Function to wrap text into multiple lines based on canvas width
    private List<String> wrapText(String text, Paint paint, float maxWidth) {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            if (paint.measureText(currentLine + " " + word) > maxWidth) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            } else {
                if (currentLine.length() > 0) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            }
        }

        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }

        return lines;
    }

    private void saveImage(Bitmap finalBitmap) {
        File file = new File(mCurrentPhotoPath);
        try (FileOutputStream out = new FileOutputStream(file)) {
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            Toast.makeText(getActivity(), "Image Saved with Timestamp!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            android.util.Log.e("Save Image", "Error saving image", e);
        }
    }

    private String fetchAddress() {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getAddressLine(0);
            } else {
                return "Address not found";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Unable to fetch address";
        }
    }

    private void loadImageFromStorage(final String path) {
        if (null != savedPictureData) {
            final String imageUrl = CommonUtils.getImageUrl(savedPictureData);
            Picasso.with(getActivity())
                    .load(imageUrl)
                    .into(iv_plantaion, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            if (null != getActivity() && !getActivity().isFinishing()) {
                                Glide.with(getActivity())
                                        .load(path)
                                        .into(iv_plantaion);
                            }
                        }
                    });
        } else {
            if (!getActivity().isFinishing()) {
                Glide.with(getActivity())
                        .load(path)
                        .into(iv_plantaion);
            }
        }
    }
    private void setPic() {
        try {
            /* Get the size of the ImageView */
            int targetW = iv_plantaion.getWidth();
            int targetH = iv_plantaion.getHeight();

            /* Get the size of the image */
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            /* Scale the image to fit the ImageView */
            int scaleFactor = 1;
            if (targetW > 0 && targetH > 0) {
                scaleFactor = Math.min(photoW / targetW, photoH / targetH);
            }

            /* Decode the image file into a Bitmap */
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

            // **Rotate the image based on EXIF data**
            bitmap = rotateImageIfRequired(bitmap, mCurrentPhotoPath);

            /* Set the Bitmap to ImageView */
            iv_plantaion.setImageBitmap(bitmap);
            iv_plantaion.setVisibility(View.VISIBLE);
            iv_plantaion.invalidate();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Failed to load image", Toast.LENGTH_SHORT).show();
        }
    }

    /* Function to rotate image based on EXIF metadata */
    private Bitmap rotateImageIfRequired(Bitmap img, String photoPath) throws IOException {
        ExifInterface ei = new ExifInterface(photoPath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
                break;
            default:
                return img; // No rotation needed
        }

        return Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
    }

//    private void setPic() {
//
//		/* There isn't enough memory to open up more than a couple camera photos */
//        /* So pre-scale the target bitmap into which the file is decoded */
//
//		/* Get the size of the ImageView */
//        int targetW = iv_plantaion.getWidth();
//        int targetH = iv_plantaion.getHeight();
//
//		/* Get the size of the image */
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        bmOptions.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//        int photoW = bmOptions.outWidth;
//        int photoH = bmOptions.outHeight;
//
//		/* Figure out which way needs to be reduced less */
//        int scaleFactor = 1;
//        if ((targetW > 0) || (targetH > 0)) {
//            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
//        }
//
//		/* Set bitmap options to scale the image decode target */
//        bmOptions.inJustDecodeBounds = false;
//        bmOptions.inSampleSize = scaleFactor;
//        bmOptions.inPurgeable = true;
//
//		/* Decode the JPEG file into a Bitmap */
//        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//        bitmap = ImageUtility.rotatePicture(0, bitmap);
//        iv_plantaion.setImageBitmap(bitmap);
//
//        iv_plantaion.setVisibility(View.VISIBLE);
//        //farmerIcon.setVisibility(View.GONE);
//        iv_plantaion.invalidate();
//    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }
/*

    public void addPlantationData() {
        palmDetailsList.setVisibility(View.VISIBLE);
        recyclerItemsLayout.setVisibility(View.VISIBLE);
        Plantation plantation = new Plantation();
        plantation.setNurserycode(Integer.parseInt(CommonUtils.getKeyFromValue(saplingNurseryMap, saplingNurserySpin.getSelectedItem().toString())));
        plantation.setSaplingvendorid(Integer.parseInt(CommonUtils.getKeyFromValue(saplingVendorMap, saplingVendorSpin.getSelectedItem().toString())));
        plantation.setSaplingsourceid(Integer.parseInt(CommonUtils.getKeyFromValue(sourceofsaplingsMap, sourceofsaplingsSpin.getSelectedItem().toString())));
        if (!CommonUtils.isEmptySpinner(varietySpin)) {
            plantation.setCropVarietyId(Integer.parseInt(CommonUtils.getKeyFromValue(varietyDataMap, varietySpin.getSelectedItem().toString())));
        } else {
            plantation.setCropVarietyId(null);
        }
        plantation.setAllotedarea(Double.parseDouble(areaallocatedEdt.getText().toString()));
//        palmOilArea = Double.parseDouble(areaallocatedEdt.getText().toString());
        plantation.setTreescount(CommonUtils.convertToBigNumber(saplingcounttreesEdt.getText().toString()));
        plantation.setReasonTypeId(null);
        plantationList.add(plantation);
        itemListDataAdapter.updateData(plantationList);
        clearFields();
    }
*/

    //Update Date of Planting label
    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateofplantingEdt.setText(sdf.format(myCalendar.getTime()));

    }

    //Validations
    public boolean validateFields() {
        if (CommonUtils.isEmptySpinner(saplingNurserySpin)) {
            UiUtils.showCustomToastMessage("Please select sapling nursery", getActivity(), 1);
            return false;
        }

        if (CommonUtils.isEmptySpinner(saplingVendorSpin)) {
            UiUtils.showCustomToastMessage("Please select " + getResources().getString(R.string.sapling_vendor), getActivity(), 1);
            return false;
        }

        if (CommonUtils.isEmptySpinner(sourceofsaplingsSpin)) {
            UiUtils.showCustomToastMessage("Please select sapling source", getActivity(), 1);
            return false;
        }

        if (TextUtils.isEmpty(areaallocatedEdt.getText().toString())) {
            UiUtils.showCustomToastMessage("Please enter area allocated", getActivity(), 1);
            return false;
        }

        int treeCount ;
        if (TextUtils.isEmpty(saplingcounttreesEdt.getText().toString())){
          treeCount = 0;
        }else {
        treeCount = Integer.parseInt((saplingcounttreesEdt.getText().toString()));
        }

        if (TextUtils.isEmpty(saplingcounttreesEdt.getText().toString()) || (treeCount == 0)) {
            UiUtils.showCustomToastMessage("Please enter count of trees & must not be Zero", getActivity(), 1);
            return false;
        }
        return true;
    }



    // Save Btn OnClick Listener
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.plantationSaveBtn:
                //isComentRequired = false;
                if (plantationList != null && !plantationList.isEmpty()) {
                    if (isPlotDataValid()) {
                        try {
                            savePictureData();
                        } catch (Exception e) {
                            Log.pushExceptionToCrashlytics(LOG_TAG ,new OilPalmException("Method :saveConv_PictureData"+e),"Method :savePictureData");

                            e.printStackTrace();
                        }
                             Log.d(LOG_TAG,"Plantation Data adding to manager  ITEM COUNT :" +itemListDataAdapter.getSendlist().size());


                          // final array create and send

                          List<Plantation> finalpalntationdata = itemListDataAdapter.getSendlist();
                        for (Plantation item:finalpalntationdata
                             ) {
                            item.setMissingPlantsComments(reasonformissingsaplings.getText().toString());
                        }

                            DataManager.getInstance().addData(DataManager.PLANTATION_CON_DATA, finalpalntationdata);



                        Plot selectedPlot = (Plot) DataManager.getInstance().getDataFromManager(DataManager.PLOT_DETAILS);

                        if (null == selectedPlot) {
                            Farmer selectedFarmer = (Farmer) DataManager.getInstance().getDataFromManager(DataManager.FARMER_PERSONAL_DETAILS);
                            selectedPlot = (Plot) dataAccessHandler.getSelectedPlotData(Queries.getInstance().getSelectedPlot(dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getPlotCodeFromFarmerCode(selectedFarmer.getCode()))), 0);
                        }
                        selectedPlot.setTotalpalmarea(Double.parseDouble(plotArea.getText().toString()));
                        String ddMm = dopTxt.getText().toString();
                        Date date = null;
                        try {
                            date = inputFormat.parse(ddMm);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        String yyMm = outputFormat.format(date);

                        selectedPlot.setDateofPlanting(yyMm);
                        LeftOutArea = totalPlotArea - (palmOilArea);
                        selectedPlot.setLeftoutarea(LeftOutArea);
                        if (!CommonUtils.isEmptySpinner(cropSpin)) {
                            selectedPlot.setLeftoutareacropid(Integer.parseInt(CommonUtils.getKeyFromValue(cropDataMap, cropSpin.getSelectedItem().toString())));
                        } else {
                            selectedPlot.setLeftoutareacropid(null);
                        }
                        selectedPlot.setSwapingReasonId(Integer.parseInt(CommonUtils.getKeyFromValue(reasonDataMap, reasonSpin.getSelectedItem().toString())));
                        DataManager.getInstance().addData(DataManager.PLOT_DETAILS, selectedPlot);
                       // Log.d("missingsaplingscount", missingsaplings.getText().toString()  + "");
                        Log.d("missingsaplingsreason", reasonformissingsaplings.getText().toString()  + "");
                        updateUiListener.updateUserInterface(0);
                        getFragmentManager().popBackStack();
                    }
                } else {
                    UiUtils.showCustomToastMessage("Please enter sapling data"+plantationList.size(), getActivity(), 1);
                }
                break;

        }
    }

    //Validations in Adapter
    public boolean isPlotDataValid() {

        if (itemListDataAdapter.getTreesCount() == false){
            UiUtils.showCustomToastMessage("Please Enter Planted Saplings", getActivity(), 1);
            return false;
        }

        if(isComentRequired == true && TextUtils.isEmpty(reasonformissingsaplings.getText().toString())){
            UiUtils.showCustomToastMessage("Please Enter Reason for Missing Saplings", getActivity(), 1);
            //isComentRequired = false;
               return false;
        }

//        if (TextUtils.isEmpty(saplingsPlanted.getText().toString())) {
//            UiUtils.showCustomToastMessage("Please enter number of Saplings Planted", getActivity(), 1);
//            return false;
//        }
//
//        Integer totalsaplings = 0;
//
//        for (int i = 0; i < plantationList.size(); i++){
//
//            totalsaplings = totalsaplings+plantationList.get(i).getTreescount();
//        }

//        if (TextUtils.isEmpty(saplingsPlanted.getText().toString()) || Integer.parseInt(saplingsPlanted.getText().toString()) > totalsaplings ) {
//            UiUtils.showCustomToastMessage("Number of Saplings Planted should not be greater than Total tree count", getActivity(), 1);
//            return false;
//        }
//
//        if (TextUtils.isEmpty(saplingsPlanted.getText().toString()) || Integer.parseInt(saplingsPlanted.getText().toString()) < totalsaplings ) {
//
//            if (TextUtils.isEmpty(reasonformissingsaplings.getText().toString())) {
//                UiUtils.showCustomToastMessage("Please enter reason for missing saplings", getActivity(), 1);
//                return false;
//            }
//
//        }


        if (TextUtils.isEmpty(plotArea.getText().toString())) {
            UiUtils.showCustomToastMessage("Please Enter Plot Area", getActivity(), 1);
            return false;
        }
        if (CommonUtils.isEmptySpinner(reasonSpin)) {
            UiUtils.showCustomToastMessage("Please Select Swap Reason", getActivity(), 1);
            return false;
        }
        if (mCurrentPhotoPath == null){
            UiUtils.showCustomToastMessage("Please Take Plantation Picture", getActivity(), 1);
            return false;
        }
      /*  Double allotedArea = 0.0;
        for (Plantation plantation : plantationList) {
            allotedArea = allotedArea + Double.parseDouble(String.valueOf(plantation.getAllotedarea()));
        }

         if (!(allotedArea == Double.parseDouble(plotArea.getText().toString()))) {
            UiUtils.showCustomToastMessage("Sapling area should not exceed the total area under palm", getActivity(), 1);
            return false;
        }*/
        return true;
    }

    //Clear fields
    private void clearFields() {
        cropSpin.setSelection(0);
        varietySpin.setSelection(0);
        saplingNurserySpin.setSelection(0);
        saplingVendorSpin.setSelection(0);
        sourceofsaplingsSpin.setSelection(0);
        areaallocatedEdt.setText("");
        saplingcounttreesEdt.setText("");
        reasonSpin.setSelection(0);
    }

    @Override
    public void onEditClicked(int position) {
        plantationList.remove(position);
        itemListDataAdapter.updateData(plantationList);
        if (plantationList.isEmpty()) {
            palmDetailsList.setVisibility(View.GONE);
            recyclerItemsLayout.setVisibility(View.GONE);
        }
    }

    public void setUpdateUiListener(UpdateUiListener updateUiListener) {
        this.updateUiListener = updateUiListener;
    }

    //Get data from the Adapter using Listener
    @Override
    public void getPlantedSaplingsListner(Integer count,Integer totaltreecount,Integer missingtreecount) {

        Log.d("missingtreecount ","missingtreecount :"+missingtreecount);

        if (missingtreecount >= 1){
            reasonformissingsaplinglyt.setVisibility(View.VISIBLE);
            isComentRequired = true;
        }else{
            reasonformissingsaplinglyt.setVisibility(View.GONE);
          isComentRequired = false;
        }

//        Log.d("Conversion ","TotalSaplingsCount :"+count);
//        totalplantedsaplings = count;
//        totalTreecount = totaltreecount;
//        Log.d("Conversion ","TotalSaplingsCountttt :"+totalplantedsaplings);
//
//        if(totaltreecount > count) {
//            reasonformissingsaplinglyt.setVisibility(View.VISIBLE);
//            isComentRequired = true;
//        }
//        else{
//            reasonformissingsaplinglyt.setVisibility(View.GONE);
//            isComentRequired = false;
//        }

    }
}
