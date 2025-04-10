package com.cis.palm360.service;

import com.cis.palm360.palmcare.OtpResponceModel;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

public interface ApiService {


//    @GET
//    Observable<LerningsModel> getlernings(@Url String url);
//
//
//    @GET
//    Observable<RecomPlotcodes> getplots(@Url String url);
//
//
    @GET
    Observable<OtpResponceModel> getFormerOTP(@Url String url);

//    @GET
//    Observable<FarmerOtpResponceModel> getFormerdetails(@Url String url);


}
