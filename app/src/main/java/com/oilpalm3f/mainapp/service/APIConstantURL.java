package com.oilpalm3f.mainapp.service;

public interface APIConstantURL {

public static  final  String LOCAL_URL="http://182.18.157.215/3FSmartPalm_Nursery/API/";//local
//public static  final  String LOCAL_URL="http://182.18.157.215/3FSmartPalmNursery_UAT/API/";//UAT
//public static final String LOCAL_URL = "http://182.18.139.166/3FOilPalm/API/";//Live

//public static  final  String LOCAL_URL="http://182.18.157.215/3FOilPalmNurseryTest/API/";//UAT
    String SendOTPForCropMaintenance ="api/SyncTransactions/SendOTPForCropMaintenance";
    String VerifyCropMaintenanceOTP ="api/SyncTransactions/VerifyCropMaintenanceOTP/";
    String SendOTPForHarvestorVisit ="api/SyncTransactions/SendOTPForHarvestorVisit";
    String VerifyForHarvestorOTP ="api/SyncTransactions/VerifyForHarvestorOTP/";

}
