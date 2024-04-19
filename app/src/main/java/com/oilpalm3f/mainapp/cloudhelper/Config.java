package com.oilpalm3f.mainapp.cloudhelper;

import com.oilpalm3f.mainapp.BuildConfig;

//Urls can be assigned here

public class Config {
    public static final boolean DEVELOPER_MODE = false;
   //public static String live_url = "http://120.138.8.8:9020/API/api";//Commented on 12-04-2021 to change the URL to local
    //public static String live_url = "http://183.82.111.111/3FOilPalm_Live/API/api"; //locall ive
    //public static  String live_url = "http://182.18.139.166/3FOilPalm/API/api";//live
   //public static String live_url = "http://182.18.157.215/3FSmartPalm/API/api"; //newtest
 public static String live_url = "http://182.18.157.215/3FSmartPalm_Nursery/API/api"; //Local test
    //local URl
    public static void initialize() {
        if (BuildConfig.BUILD_TYPE.equalsIgnoreCase("release")) {
           // live_url = "http://120.138.8.8:9020/API/api";//Commented on 12-04-2021 to change the URL to local
           //live_url = "http://183.82.111.111/3FOilPalm_Live/API/api";//locallive
         //live_url = "http://182.18.139.166/3FOilPalm/API/api";//live
            //live_url = "http://182.18.157.215/3FSmartPalm/API/api"; //newtest
     live_url = "http://182.18.157.215/3FSmartPalm_Nursery/API/api"; //local test
        } else {
           // live_url = "http://120.138.8.8:9020/API/api";//Commented on 12-04-2021 to change the URL to local
           // live_url = "http://183.82.111.111/3FOilPalm_Live/API/api";//locallive
             //live_url = "http://182.18.139.166/3FOilPalm/API/api";//live
            //live_url = "http://182.18.157.215/3FSmartPalm/API/api"; //newtest
        live_url = "http://182.18.157.215/3FSmartPalm_Nursery/API/api"; //localtest
        }
    }

    public static final String masterSyncUrl = "/SyncMasters/Sync";

    public static final String transactionSyncURL = "/SyncTransactions/SyncTransactions";
    //public static final String transactionSyncURL = "/SyncTransactions/SyncTransactionss";
    public static final String locationTrackingURL = "/LocationTracker/SaveOfflineLocations";
    public static final String imageUploadURL = "/SyncTransactions/UploadImage";

    public static final String findcollectioncode = "/SyncTransactions/FindCollectionCode/%s";
    public static final String findconsignmentcode = "/SyncTransactions/FindConsignmentCode/%s";
    public static final String findcollectionplotcode = "/SyncTransactions/FindCollectionPlotXref/%s/%s";
 
    public static final String updatedbFile = "/TabDatabase/UploadDatabase";

    public static final String getTransCount = "/SyncTransactions/GetCount";//{Date}/{UserId}
    public static final String getTransData = "/SyncTransactions/%s";//api/TranSync/SyncFarmers/{Date}/{UserId}/{Index}
    public static final String validateTranSync = "/TranSync/ValidateTranSync/%s";
    //public static final String image_url = "http://182.18.139.166/3FOilPalm/FileRepository";//Added on 26th new repo using
  public static final String image_url = "http://182.18.157.215/3FSmartPalm_Nursery/3FSmartPalm_Nursery_Repo/FileRepository/";

//    public static final String GETMONTHLYTARGETSBYUSERIDANDFINANCIALYEAR = "/KRA/GetMonthlyTargetsByUserIdandFinancialYear";
//    public static final String GETTARGETSBYUSERIDANDFINANCIALYEAR = "/KRA/GetTargetsByUserIdandFinancialYear";
    public static final String GETMONTHLYTARGETSBYUSERIDANDFINANCIALYEAR = "/KRA/GetMonthlyTargetsByUserIdandFY";
    public static final String GETTARGETSBYUSERIDANDFINANCIALYEAR = "/KRA/GetAnnualTargetsByUserIdandFY";
    public static final String GET_ALERTS = "/SyncTransactions/SyncAlertDetails/";//{UserId}

}
