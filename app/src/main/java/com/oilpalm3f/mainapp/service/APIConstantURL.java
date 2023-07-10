package com.oilpalm3f.mainapp.service;

public interface APIConstantURL {
//public static  final  String LOCAL_URL="http://103.241.144.240:9096/api/";
  //test
public static  final  String LOCAL_URL="http://182.18.157.215/3FSmartPalm_Nursery/API/";


//public static  final  String LOCAL_URL="http://103.241.144.240:9098/api/";//live



    String SendOTPForCropMaintenance ="api/SyncTransactions/SendOTPForCropMaintenance";
    String VerifyCropMaintenanceOTP ="api/SyncTransactions/VerifyCropMaintenanceOTP/";
    String SendOTPForHarvestorVisit ="api/SyncTransactions/SendOTPForHarvestorVisit";
    String VerifyForHarvestorOTP ="api/SyncTransactions/VerifyForHarvestorOTP/";

}
