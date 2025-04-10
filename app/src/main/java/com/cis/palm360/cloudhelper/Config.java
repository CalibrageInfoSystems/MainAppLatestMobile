package com.cis.palm360.cloudhelper;

import com.cis.palm360.BuildConfig;

//Urls can be assigned here

public class Config {
    public static final boolean DEVELOPER_MODE = false;

 public static String live_url = "http://182.18.157.215/3FSmartPalm_Nursery/API/api"; //Local test

    //local URl
    public static void initialize() {
        if (BuildConfig.BUILD_TYPE.equalsIgnoreCase("release")) {

live_url = "http://182.18.157.215/3FSmartPalm_Nursery/API/api"; //local test

        } else {

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

    public static final String getTransCount = "/SyncTransactions/GetCount";//{Date}/{UserId}n
    public static final String getTransData = "/SyncTransactions/%s";//api/TranSync/SyncFarmers/{Date}/{UserId}/{Index}
    public static final String validateTranSync = "/TranSync/ValidateTranSync/%s";
 //public static final String image_url = "http://182.18.139.166/3FOilPalm/FileRepository";//Added on 26th new repo using//Live
public static final String image_url = "http://182.18.157.215/3FSmartPalm_Nursery/3FSmartPalm_Nursery_Repo/FileRepository/";//Test

    public static final String GETMONTHLYTARGETSBYUSERIDANDFINANCIALYEAR = "/KRA/GetMonthlyTargetsByUserIdandFY";
    public static final String GETTARGETSBYUSERIDANDFINANCIALYEAR = "/KRA/GetAnnualTargetsByUserIdandFY";
    public static final String GET_ALERTS = "/SyncTransactions/SyncAlertDetails/";//{UserId}

}
