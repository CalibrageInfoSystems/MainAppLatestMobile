package com.oilpalm3f.mainapp.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.oilpalm3f.mainapp.R;
import com.oilpalm3f.mainapp.cloudhelper.ApplicationThread;
import com.oilpalm3f.mainapp.cloudhelper.Config;
import com.oilpalm3f.mainapp.cloudhelper.Log;
import com.oilpalm3f.mainapp.common.CommonConstants;
import com.oilpalm3f.mainapp.common.CommonUtils;
import com.oilpalm3f.mainapp.common.FiscalDate;
import com.oilpalm3f.mainapp.conversion.ConversionDigitalContractFragment;
import com.oilpalm3f.mainapp.database.DataAccessHandler;
import com.oilpalm3f.mainapp.database.Palm3FoilDatabase;
import com.oilpalm3f.mainapp.database.Queries;
import com.oilpalm3f.mainapp.datasync.helpers.DataSyncHelper;
import com.oilpalm3f.mainapp.dbmodels.BasicFarmerDetails;
import com.oilpalm3f.mainapp.dbmodels.CropMaintenanceDocs;
import com.oilpalm3f.mainapp.dbmodels.DigitalContract;
import com.oilpalm3f.mainapp.dbmodels.UserDetails;
import com.oilpalm3f.mainapp.helper.PrefUtil;
import com.oilpalm3f.mainapp.uihelper.ProgressBar;
import com.oilpalm3f.mainapp.utils.UiUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SplashScreen extends AppCompatActivity {

    public static final String LOG_TAG = SplashScreen.class.getName();

    private Palm3FoilDatabase palm3FoilDatabase;


    private ArrayList<DigitalContract> digitalList = new ArrayList<>();
    private ArrayList<CropMaintenanceDocs> cmdocsList = new ArrayList<>(); ;
    private DataAccessHandler dataAccessHandler;
    private String[] PERMISSIONS_REQUIRED = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.FOREGROUND_SERVICE
    };
    private SharedPreferences sharedPreferences;
    private DigitalContract digitalContract;
    private CropMaintenanceDocs cropMaintenanceDocs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //sharedPreferences = getSharedPreferences("appprefs", MODE_PRIVATE);
        sharedPreferences = getSharedPreferences("", MODE_PRIVATE);

        //  rootDirectory = new File(CommonUtils.get3FFileRootPath() + "3F_DigitalContract/");
        if (!CommonUtils.isNetworkAvailable(this)) {
            UiUtils.showCustomToastMessage("Please check your network connection", SplashScreen.this, 1);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !CommonUtils.areAllPermissionsAllowedNew(this, PERMISSIONS_REQUIRED)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_REQUIRED, CommonUtils.PERMISSION_CODE);
        } else {
            try {
                palm3FoilDatabase = Palm3FoilDatabase.getPalm3FoilDatabase(this);
                palm3FoilDatabase.createDataBase();
                dbUpgradeCall();
            } catch (Exception e) {
                e.getMessage();
            }
            dataAccessHandler = new DataAccessHandler(SplashScreen.this);
            startMasterSync();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CommonUtils.PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(LOG_TAG, "permission granted");
                    try {
                        palm3FoilDatabase = Palm3FoilDatabase.getPalm3FoilDatabase(this);
                        palm3FoilDatabase.createDataBase();
                        dbUpgradeCall();
                    } catch (Exception e) {
                        Log.e(LOG_TAG, "@@@ Error while getting master data "+e.getMessage());
                    }
                    startMasterSync();
                }
                break;
        }
    }

//    public void startMasterSync() {
//
//        if (CommonUtils.isNetworkAvailable(this) && !sharedPreferences.getBoolean("",false)) {
//        //if (CommonUtils.isNetworkAvailable(this)) {
//         //if (CommonUtils.isNetworkAvailable(this) && !PrefUtil.getBool(this, CommonConstants.IS_MASTER_SYNC_SUCCESS)) {
//
//        DataSyncHelper.performMasterSync(this, PrefUtil.getBool(this, CommonConstants.IS_MASTER_SYNC_SUCCESS), new ApplicationThread.OnComplete() {
//                @Override
//                public void execute(boolean success, Object result, String msg) {
//                    ProgressBar.hideProgressBar();
//                    if (success) {
//                        UiUtils.showCustomToastMessage("Master Sync Success", SplashScreen.this, 0);
//                        sharedPreferences.edit().putBoolean(CommonConstants.IS_MASTER_SYNC_SUCCESS, true).apply();
//                        startActivity(new Intent(SplashScreen.this, MainLoginScreen.class));
//                        finish();
//                    } else {
//                        Log.v(LOG_TAG, "@@@ Master sync failed " + msg);
//                        ApplicationThread.uiPost(LOG_TAG, "master sync message", new Runnable() {
//                            @Override
//                            public void run() {
//                                UiUtils.showCustomToastMessage("Data syncing failed", SplashScreen.this, 1);
//                                startActivity(new Intent(SplashScreen.this, MainLoginScreen.class));
//                                finish();
//                            }
//                        });
//                    }
//                }
//            });
//        } else {
//            startActivity(new Intent(SplashScreen.this, MainLoginScreen.class));
//            finish();
//        }
//    }

    public void startMasterSync() {

       if (CommonUtils.isNetworkAvailable(this) && !sharedPreferences.getBoolean(CommonConstants.IS_MASTER_SYNC_SUCCESS,false)) {
   //   if (CommonUtils.isNetworkAvailable(this) && !PrefUtil.getBool(this, CommonConstants.IS_MASTER_SYNC_SUCCESS)){
            DataSyncHelper.performMasterSync(this, PrefUtil.getBool(this, CommonConstants.IS_MASTER_SYNC_SUCCESS), new ApplicationThread.OnComplete() {
                @Override
                public void execute(boolean success, Object result, String msg) {
                    ProgressBar.hideProgressBar();
                    if (success) {

                        // sharedPreferences.edit().putBoolean(CommonConstants.IS_MASTER_SYNC_SUCCESS, true).apply();

                        sharedPreferences.edit().putBoolean(CommonConstants.IS_MASTER_SYNC_SUCCESS, true).apply();
                        // digitalContract = (DigitalContract) dataAccessHandler.getDigitalContractData(Queries.getInstance().getDigitalContract(stateid), 0);
                        digitalpdfsave();

//                        startActivity(new Intent(SplashScreen.this, MainLoginScreen.class));
//                        finish();
                    } else {
                        Log.v(LOG_TAG, "@@@ Master sync failed " + msg);
                        ApplicationThread.uiPost(LOG_TAG, "master sync message", new Runnable() {
                            @Override
                            public void run() {
                                UiUtils.showCustomToastMessage("Data syncing failed", SplashScreen.this, 1);
                                startActivity(new Intent(SplashScreen.this, MainLoginScreen.class));
                                finish();
                            }
                        });
                    }
                }
            });
        } else {
            startActivity(new Intent(SplashScreen.this, MainLoginScreen.class));
            finish();
        }
    }

    private void digitalpdfsave() {
        dataAccessHandler = new DataAccessHandler(SplashScreen.this);
        digitalList = (ArrayList<DigitalContract>) dataAccessHandler.getDigitalContractData(Queries.getInstance().getDigitalContract(),1);
        Log.e("========>datasize", digitalList.size() + "");
        for (int i = 0; i < digitalList.size(); i++) {
            int stateid = digitalList.get(i).getStateId();
            Log.e("========>156", stateid + "");
            // fileExtention =digitalContract.getFileExtension();
            // rootDirectory = new File(CommonUtils.get3FFileRootPath() + "3F_DigitalContract/");
            digitalContract = (DigitalContract) dataAccessHandler.getDigitalContractData(Queries.getInstance().getDigitalContractbystatecode(stateid), 0);
            String url = Config.image_url + "/" + digitalContract.getFileLocation() + "/" + digitalContract.getFILENAME() + digitalContract.getFileExtension();
            Log.e("========>171url", url + "");

            new DownloadpdfFileFromURL(digitalContract.getFILENAME(), digitalContract.getFileExtension()).execute(url);}
        cmdocssave();
//                                new DownloadpdfFileFromURL().execute(url);

    }
    private void cmdocssave() {
        dataAccessHandler = new DataAccessHandler(SplashScreen.this);
        cmdocsList = (ArrayList<CropMaintenanceDocs>) dataAccessHandler.getCMDocsData(Queries.getInstance().getAllCMDocs(),1);
        Log.e("cmdocsListsize", cmdocsList.size() + "");
        for (int i = 0; i < cmdocsList.size(); i++) {
            int sectionId = cmdocsList.get(i).getCMSectionId();
            Log.e("sectionId", sectionId + "");
            // fileExtention =digitalContract.getFileExtension();
            // rootDirectory = new File(CommonUtils.get3FFileRootPath() + "3F_DigitalContract/");
            cropMaintenanceDocs = (CropMaintenanceDocs) dataAccessHandler.getCMDocsData(Queries.getInstance().getCMDocsbysectionId(sectionId), 0);
            String cmdocurl = Config.image_url + "/" + cropMaintenanceDocs.getFileLocation() + "/" + cropMaintenanceDocs.getFileName() + cropMaintenanceDocs.getFileExtension();
            Log.e("cmdocurlurl", cmdocurl + "");

            new DownloadCMDocsFromURL(cropMaintenanceDocs.getFileName(), cropMaintenanceDocs.getFileExtension()).execute(cmdocurl);}
        startActivity(new Intent(SplashScreen.this, MainLoginScreen.class));
        finish();
    }


    public void dbUpgradeCall() {
        DataAccessHandler dataAccessHandler = new DataAccessHandler(SplashScreen.this, false);
        String count = dataAccessHandler.getCountValue(Queries.getInstance().UpgradeCount());
        if (TextUtils.isEmpty(count) || Integer.parseInt(count) == 0) {
            SharedPreferences sharedPreferences = getSharedPreferences("appprefs", MODE_PRIVATE);
            sharedPreferences.edit().putBoolean(CommonConstants.IS_FRESH_INSTALL, true).apply();
        }
    }
    public class DownloadpdfFileFromURL extends AsyncTask<String, Void, String> {

        private boolean downloadSuccess = false;
        private String filename;
        private String fileExtension;

        public DownloadpdfFileFromURL(String filename, String fileExtension) {
            this.filename = filename;
            this.fileExtension = fileExtension;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String rootDirectory = CommonUtils.get3FFileRootPath() + "3F_DigitalContract/";
                File directory = new File(rootDirectory);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                OutputStream output = new FileOutputStream(rootDirectory + filename + fileExtension);

                byte data[] = new byte[1024];

                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
                downloadSuccess = true;
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                downloadSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            if (downloadSuccess) {
                File fileToDownload = new File(CommonUtils.get3FFileRootPath() + "3F_DigitalContract/" + filename + fileExtension);
                Log.d("File Path:", fileToDownload.getAbsolutePath());
                if (fileToDownload.exists()) {
                    Log.d("File Path:", fileToDownload.getAbsolutePath());
                } else {
                    UiUtils.showCustomToastMessage("File does not exist", SplashScreen.this, 1);
                }
            }
        }
    }


    public class DownloadCMDocsFromURL extends AsyncTask<String, Void, String> {

        private boolean downloadSuccess = false;
        private String filename;
        private String fileExtension;

        public DownloadCMDocsFromURL(String filename, String fileExtension) {
            this.filename = filename;
            this.fileExtension = fileExtension;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String rootDirectory = CommonUtils.get3FFileRootPath() + "3F_CMDocs/";
                File directory = new File(rootDirectory);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                OutputStream output = new FileOutputStream(rootDirectory + filename + fileExtension);

                byte data[] = new byte[1024];

                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
                downloadSuccess = true;
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                downloadSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            if (downloadSuccess) {
                File fileToDownload = new File(CommonUtils.get3FFileRootPath() + "3F_CMDocs/" + filename + fileExtension);
                Log.d("File Path:", fileToDownload.getAbsolutePath());
                if (fileToDownload.exists()) {
                    Log.d("File Path:", fileToDownload.getAbsolutePath());
                } else {
                    UiUtils.showCustomToastMessage("File does not exist", SplashScreen.this, 1);
                }
            }
        }
    }

//    class DownloadpdfFileFromURL extends AsyncTask<String, String, String> {
//
//        public boolean downloadSuccess = false;
//
//        public DownloadpdfFileFromURL(String filenamee, String fileExtension) {
//            filename =filenamee;
//            fileExtention =fileExtension;
//        }
//
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(String... f_url) {
//            Log.d("===========>filename",filename+"");
//            Log.d("===========>fileExtension",fileExtention+"");
//            int count;
//            try {
//                URL url = new URL(f_url[0]);
//                URLConnection conection = url.openConnection();
//                conection.connect();
//
//                InputStream input = new BufferedInputStream(url.openStream(),
//                        8192);
//
//                OutputStream output = new FileOutputStream(rootDirectory + filename + fileExtention);
//
//                byte data[] = new byte[1024];
//
//                while ((count = input.read(data)) != -1) {
//                    output.write(data, 0, count);
//                }
//                output.flush();
//                output.close();
//                input.close();
//                downloadSuccess = true;
//            } catch (Exception e) {
//                android.util.Log.e("Error: ", e.getMessage());
//                downloadSuccess = false;
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String file_url) {
//            Log.d("===========>filename260",filename+"");
//            Log.d("===========>fileExtension261",fileExtention+"");
//            if (downloadSuccess ) {
//                fileToDownLoad = new File(rootDirectory + filename + fileExtention);
//                if (null != fileToDownLoad && fileToDownLoad.exists()) {
//                  Log.d("===========>filepath",fileToDownLoad+"");
//                } else {
//                    UiUtils.showCustomToastMessage("File not exist", SplashScreen.this, 1);
//                }
//            }
//
//        }
//    }
}

