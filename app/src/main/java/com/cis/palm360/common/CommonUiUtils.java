package com.cis.palm360.common;

import android.content.Context;
import android.text.TextUtils;

import com.cis.palm360.cloudhelper.Log;
import com.cis.palm360.conversion.ConversionDigitalContractFragment;
import com.cis.palm360.database.DataAccessHandler;
import com.cis.palm360.database.DatabaseKeys;
import com.cis.palm360.database.Queries;
import com.cis.palm360.datasync.helpers.DataManager;
import com.cis.palm360.dbmodels.Complaints;
import com.cis.palm360.dbmodels.Address;
import com.cis.palm360.dbmodels.Disease;
import com.cis.palm360.dbmodels.Farmer;
import com.cis.palm360.dbmodels.FarmerBank;
import com.cis.palm360.dbmodels.FileRepository;
import com.cis.palm360.dbmodels.FollowUp;
import com.cis.palm360.dbmodels.Ganoderma;
import com.cis.palm360.dbmodels.GeoBoundaries;
import com.cis.palm360.dbmodels.IdentityProof;
import com.cis.palm360.dbmodels.MainPestModel;
import com.cis.palm360.dbmodels.Nutrient;
import com.cis.palm360.dbmodels.Plot;
import com.cis.palm360.dbmodels.SoilResource;
import com.cis.palm360.dbmodels.WaterResource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by siva on 22/05/17.
 */

// Commonly used UI methods are written here

public class CommonUiUtils {
    private DataAccessHandler dataAccessHandler ;
    //Checks whether all mandatory data is entered or not in Registration Screen
    public static boolean isMandatoryDataEnteredForNRF() {
        return DataManager.getInstance().getDataFromManager(DataManager.FARMER_ADDRESS_DETAILS) != null
                && DataManager.getInstance().getDataFromManager(DataManager.FARMER_PERSONAL_DETAILS) != null
                && DataManager.getInstance().getDataFromManager(DataManager.PLOT_ADDRESS_DETAILS) != null
                && DataManager.getInstance().getDataFromManager(DataManager.PLOT_DETAILS) != null
                && DataManager.getInstance().getDataFromManager(DataManager.PLOT_FOLLOWUP) != null;
    }

    //Checks whether all mandatory data is entered or not in Conversion Screen
    public static boolean isMandatoryDataEnteredForConversion(final Context context) {
        DataAccessHandler dataAccessHandler = new DataAccessHandler(context);
        boolean idProofsRecordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(DatabaseKeys.TABLE_IDENTITYPROOF, "FarmerCode", CommonConstants.FARMER_CODE));
        boolean farmerBankRecordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(DatabaseKeys.TABLE_FARMERBANK, "FarmerCode", CommonConstants.FARMER_CODE));

        return (DataManager.getInstance().getDataFromManager(DataManager.ID_PROOFS_DATA) != null || idProofsRecordExisted)
                && (DataManager.getInstance().getDataFromManager(DataManager.FARMER_BANK_DETAILS) != null || farmerBankRecordExisted)
                && DataManager.getInstance().getDataFromManager(DataManager.PLANTATION_CON_DATA) != null
                //&& DataManager.getInstance().getDataFromManager(DataManager.PLOT_GEO_TAG) != null
                && DataManager.getInstance().getDataFromManager(DataManager.PLOT_GEO_BOUNDARIES) != null
                && ConversionDigitalContractFragment.isContractAgreed;
    }

    //Checks whether all mandatory data is entered or not in Followup Screen
    public static boolean isMandatoryDataEnteredForFollowUp() {
        return DataManager.getInstance().getDataFromManager(DataManager.PLOT_FOLLOWUP) != null;
    }

    //Checks whether all mandatory data is entered or not in Crop Maintenance Screen
    public static boolean isMandatoryDataEnteredForCropMaintenance() {
        return DataManager.getInstance().getDataFromManager(DataManager.CURRENT_PLANTATION) != null
                && (DataManager.getInstance().getDataFromManager(DataManager.WEEDING_HEALTH_OF_PLANTATION_DETAILS) != null ||   CommonConstants.CURRENT_TREE==0)
                && DataManager.getInstance().getDataFromManager(DataManager.PLOT_GEO_TAG) != null;

    }

    //Checks whether market survery added or not

    public static boolean isMarketSurveyAddedForFarmer(final Context context, final String query) {
        DataAccessHandler accessHandler = new DataAccessHandler(context);
        return accessHandler.checkValueExistedInDatabase(query);
    }

    //To set the Address Strings
    public static void setGeoGraphicalData(Farmer selectedFarmer, Context context) {
        DataAccessHandler accessHandler = new DataAccessHandler(context);
        CommonConstants.stateId = String.valueOf(selectedFarmer.getStateid());
        CommonConstants.districtId = String.valueOf(selectedFarmer.getDistictid());
        CommonConstants.mandalId = String.valueOf(selectedFarmer.getMandalid());
        CommonConstants.villageId = String.valueOf(selectedFarmer.getVillageid());
        CommonConstants.stateCode = accessHandler.getOnlyOneValueFromDb(Queries.getInstance().getCodeFromId("State", CommonConstants.stateId));
        CommonConstants.districtCode = accessHandler.getOnlyOneValueFromDb(Queries.getInstance().getCodeFromId("District", CommonConstants.districtId));
        CommonConstants.mandalCode = accessHandler.getOnlyOneValueFromDb(Queries.getInstance().getCodeFromId("Mandal", CommonConstants.mandalId));
        CommonConstants.villageCode = accessHandler.getOnlyOneValueFromDb(Queries.getInstance().getCodeFromId("Village", CommonConstants.villageId));
    }

    //To Reset the Data
    public static void resetPrevRegData() {
        DataManager.getInstance().deleteData(DataManager.FARMER_ADDRESS_DETAILS);
        DataManager.getInstance().deleteData(DataManager.FARMER_PERSONAL_DETAILS);
        DataManager.getInstance().deleteData(DataManager.FILE_REPOSITORY);
        DataManager.getInstance().deleteData(DataManager.PLOT_ADDRESS_DETAILS);
        DataManager.getInstance().deleteData(DataManager.PLOT_DETAILS);
        DataManager.getInstance().deleteData(DataManager.PLOT_CURRENT_CROPS_DATA);
        DataManager.getInstance().deleteData(DataManager.PLOT_NEIGHBOURING_PLOTS_DATA);
        DataManager.getInstance().deleteData(DataManager.SOURCE_OF_WATER);
        DataManager.getInstance().deleteData(DataManager.SoilType);
        DataManager.getInstance().deleteData(DataManager.PLOT_GEO_TAG);
        DataManager.getInstance().deleteData(DataManager.PLOT_GEO_BOUNDARIES);
        DataManager.getInstance().deleteData(DataManager.PLOT_FOLLOWUP);
        DataManager.getInstance().deleteData(DataManager.REFERRALS_DATA);
        DataManager.getInstance().deleteData(DataManager.MARKET_SURVEY_DATA);
        DataManager.getInstance().deleteData(DataManager.OIL_TYPE_MARKET_SURVEY_DATA);
        DataManager.getInstance().deleteData(DataManager.ID_PROOFS_DATA);
        DataManager.getInstance().deleteData(DataManager.FARMER_BANK_DETAILS);
        DataManager.getInstance().deleteData(DataManager.PLANTATION_CON_DATA);
        DataManager.getInstance().deleteData(DataManager.COMPLAINT_DETAILS);
        DataManager.getInstance().deleteData(DataManager.COMPLAINT_REPOSITORY);
        DataManager.getInstance().deleteData(DataManager.COMPLAINT_STATUS_HISTORY);
        DataManager.getInstance().deleteData(DataManager.COMPLAINT_TYPE);
        DataManager.getInstance().deleteData(DataManager.NEW_COMPLAINT_DETAILS);
        DataManager.getInstance().deleteData(DataManager.NEW_COMPLAINT_REPOSITORY);
        DataManager.getInstance().deleteData(DataManager.NEW_COMPLAINT_STATUS_HISTORY);
        DataManager.getInstance().deleteData(DataManager.NEW_COMPLAINT_TYPE);
        DataManager.getInstance().deleteData(DataManager.LANDLORD_BANK_DATA);
        DataManager.getInstance().deleteData(DataManager.LANDLORD_LEASED_DATA);
        DataManager.getInstance().deleteData(DataManager.LANDLORD_IDPROOFS_DATA);
      //  DataManager.getInstance().deleteData(DataManager.PEST_DETAILS);
        DataManager.getInstance().deleteData(DataManager.CHEMICAL_DETAILS);
        ConversionDigitalContractFragment.isContractAgreed = false;
        CommonConstants.isGeoTagTaken = false;
        CommonConstants.isFromPlotDetails = false;
        CommonConstants.leased = false;
        CommonConstants.isplotupdated = false;
        CommonConstants.PLOT_CODE = "";
        CommonConstants.FARMER_CODE = "";
    }

    //Checks whether Geotag/Geo Boundaries are taken or not
    public static boolean checkForGeoTag(final Context context) {
        DataAccessHandler dataAccessHandler = new DataAccessHandler(context);
        boolean existed = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().queryGeoTagCheck(CommonConstants.PLOT_CODE));
        if (existed) {
            return false;
        }
        FollowUp followUp = (FollowUp) DataManager.getInstance().getDataFromManager(DataManager.PLOT_FOLLOWUP);
        if (followUp == null) {
            return false;
        }

        GeoBoundaries geoBoundaries = (GeoBoundaries) DataManager.getInstance().getDataFromManager(DataManager.PLOT_GEO_TAG);
//        if(CommonUtils.isNewRegistration() && CommonConstants.isFromPlotDetails == false){
//        GeoBoundaries geoBoundaries = (GeoBoundaries) DataManager.getInstance().getDataFromManager(DataManager.PLOT_GEO_TAG);
//            return followUp.getIsfarmerreadytoconvert() == 1 && geoBoundaries == null;
//        }else if (CommonUtils.isNewPlotRegistration() && CommonConstants.isFromPlotDetails == false){
//            GeoBoundaries geoBoundaries = (GeoBoundaries) DataManager.getInstance().getDataFromManager(DataManager.PLOT_GEO_TAG);
//            return followUp.getIsfarmerreadytoconvert() == 1 && geoBoundaries == null;
//        }else{
//            return followUp.getIsfarmerreadytoconvert() == 1;
//        }
        return followUp.getIsfarmerreadytoconvert() == 1 && geoBoundaries == null;
    }

    //Checks whether Identity Proof details entered or not
//    public static boolean checkforIdentityDetails(final Context context){
//        DataAccessHandler dataAccessHandler = new DataAccessHandler(context);
//        boolean existed=dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().queryIdentityCheck(CommonConstants.FARMER_CODE));
//        if (existed) {
//            return false;
//        }
//        FollowUp followUp = (FollowUp) DataManager.getInstance().getDataFromManager(DataManager.PLOT_FOLLOWUP);
//        if (followUp == null) {
//            return false;
//        }
//       List<IdentityProof> identityProof=(ArrayList<IdentityProof>) DataManager.getInstance().getDataFromManager(DataManager.ID_PROOFS_DATA);
//    //    return followUp.getIsfarmerreadytoconvert() == 1;
//        return followUp.getIsfarmerreadytoconvert() == 1 &&  identityProof == null && identityProof.isEmpty();
//
//    }
    public static boolean checkforIdentityDetails(final Context context) {
        DataAccessHandler dataAccessHandler = new DataAccessHandler(context);

        // Check if identity details exist in the database
        boolean identityExists = dataAccessHandler.checkValueExistedInDatabase(
                Queries.getInstance().queryIdentityCheck(CommonConstants.FARMER_CODE)
        );

        if (identityExists) {
            Log.i("IdentityCheck", "Identity details already exist for Farmer Code: " + CommonConstants.FARMER_CODE);

            // Fetch follow-up data to confirm farmer's readiness to convert
            FollowUp followUp = (FollowUp) DataManager.getInstance().getDataFromManager(DataManager.PLOT_FOLLOWUP);
            if (followUp != null && followUp.getIsfarmerreadytoconvert() == 1) {
                Log.d("IdentityCheck", "Farmer is ready to convert. Aadhaar verification required.");

                // Retrieve identity proofs from database
                List<IdentityProof> dbProofsList = (List<IdentityProof>) dataAccessHandler.getSelectedIdProofsData(
                        Queries.getInstance().getFarmerIdentityProof(CommonConstants.FARMER_CODE), 1
                );
                List<IdentityProof> identityProofsList = (List<IdentityProof>) DataManager.getInstance().getDataFromManager(DataManager.ID_PROOFS_DATA);
                // Check if Aadhaar is present
                boolean hasAadhaar = false;
                if (dbProofsList != null) {
                    for (IdentityProof proof : dbProofsList) {
                        if (proof.getIdprooftypeid() == 60) { // Aadhaar type ID
                            hasAadhaar = true;
                            break;
                        }
                    }
                }
                if (identityProofsList != null) {
                    for (IdentityProof proof : identityProofsList) {
                        if (proof.getIdprooftypeid() == 60) { // Aadhaar type ID
                            hasAadhaar = true;
                            break;
                        }
                    }
                }
                if (!hasAadhaar) {
                    Log.e("IdentityCheck", "Farmer is ready to convert, but Aadhaar (Typecdid 60) is missing.");
                    return true; // Missing Aadhaar
                }
            }

            Log.i("IdentityCheck", "Identity details and Aadhaar are valid for Farmer Code: " + CommonConstants.FARMER_CODE);
            return false; // All checks passed
        }

        // If identity details do not exist, perform further checks
        FollowUp followUp = (FollowUp) DataManager.getInstance().getDataFromManager(DataManager.PLOT_FOLLOWUP);
        if (followUp == null) {
            Log.w("IdentityCheck", "No follow-up data found in DataManager for Farmer Code: " + CommonConstants.FARMER_CODE);
            return false;
        }

        // Fetch identity proofs data
        List<IdentityProof> identityProofsList = (List<IdentityProof>) DataManager.getInstance().getDataFromManager(DataManager.ID_PROOFS_DATA);
        if (identityProofsList == null) {
            Log.d("IdentityCheck", "No identity proofs found in DataManager. Fetching from database...");
            identityProofsList = (List<IdentityProof>) dataAccessHandler.getSelectedIdProofsData(
                    Queries.getInstance().getFarmerIdentityProof(CommonConstants.FARMER_CODE), 1
            );
        }

        // Check readiness for conversion
        if (followUp.getIsfarmerreadytoconvert() == 1) {
            if (identityProofsList == null || identityProofsList.isEmpty()) {
                Log.e("IdentityCheck", "Farmer is ready to convert, but no identity proof data is available.");
                return true; // Missing identity proofs
            }

            // Check specifically for Aadhaar (Typecdid == 60)
            boolean hasAadhaar = false;
            for (IdentityProof proof : identityProofsList) {
                if (proof.getIdprooftypeid() == 60) {
                    hasAadhaar = true;
                    break;
                }
            }

            if (!hasAadhaar) {
                Log.e("IdentityCheck", "Farmer is ready to convert, but Aadhaar (Typecdid 60) is missing.");
                return true; // Missing Aadhaar
            }
        }

        Log.i("IdentityCheck", "All identity details are valid for Farmer Code: " + CommonConstants.FARMER_CODE);
        return false; // All checks passed
    }



//    public static boolean checkforIdentityDetails(final Context context) {
//        DataAccessHandler dataAccessHandler = new DataAccessHandler(context);
//        boolean identityExists = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().queryIdentityCheck(CommonConstants.FARMER_CODE)
//        );
//
//        if (identityExists) {
//            return false;
//        }
//
//        FollowUp followUp = (FollowUp) DataManager.getInstance().getDataFromManager(DataManager.PLOT_FOLLOWUP);
//        if (followUp == null) {
//            return false;
//        }
//
//        //    List<IdentityProof> identityProofs = (ArrayList<IdentityProof>) DataManager.getInstance().getDataFromManager(DataManager.ID_PROOFS_DATA);
//        List<IdentityProof> identityProofsList = (List<IdentityProof>) DataManager.getInstance().getDataFromManager(DataManager.ID_PROOFS_DATA);
//        if (identityProofsList == null) {
//            identityProofsList = (List<IdentityProof>) dataAccessHandler.getSelectedIdProofsData(
//                    Queries.getInstance().getFarmerIdentityProof(CommonConstants.FARMER_CODE), 1
//            );
//            // Check if follow-up indicates readiness to convert
//            if (followUp.getIsfarmerreadytoconvert() == 1) {
//                if (identityProofsList == null || identityProofsList.isEmpty()) {
//                    // No identity proof data available
//                    return true;
//                }
//            }
//                // Check specifically for Aadhaar (Typecdid == 60)
//                boolean hasAadhaar = false;
//                for (IdentityProof proof : identityProofsList) {
//                    if (proof.getIdprooftypeid() == 60) {
//                        hasAadhaar = true;
//                        break;
//                    }
//                }
//
//                // Return true if Aadhaar is missing
//                return !hasAadhaar;
//            }
//
//            return false;
//        }

    //Checks whether Bank details entered or not
    public static boolean checkBankDetails(final Context context){ 
        DataAccessHandler dataAccessHandler = new DataAccessHandler(context);
        boolean existed = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().queryBankChecking(CommonConstants.FARMER_CODE));
        if(existed){
            return false;
        }
        FollowUp followUp = (FollowUp) DataManager.getInstance().getDataFromManager(DataManager.PLOT_FOLLOWUP);
        if(followUp == null){
            return  false;
        }
        FarmerBank farmerBank = (FarmerBank) DataManager.getInstance().getDataFromManager(DataManager.FARMER_BANK_DETAILS);
        return followUp.getIsfarmerreadytoconvert() == 1 && farmerBank == null;

    }

    //Checks whether Horticulture & Land type details entered or not
    public static boolean checkHorticultureAndLandType(final Context context) {
        DataAccessHandler dataAccessHandler = new DataAccessHandler(context);
        boolean existed = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().queryGeoTagCheck(CommonConstants.PLOT_CODE));
        if (existed) {
            return false;
        }
        FollowUp followUp = (FollowUp) DataManager.getInstance().getDataFromManager(DataManager.PLOT_FOLLOWUP);
        if (followUp == null) {
            return false;
        }
        Plot plot = (Plot) DataManager.getInstance().getDataFromManager(DataManager.PLOT_DETAILS);
        if (plot == null)
        {
            return false;
        }

        Integer landId = plot.getLandTypeId();
        Log.v("@@@landID",""+landId);
        return followUp.getIsfarmerreadytoconvert() == 1  && landId == null && plot.getTotalAreaUnderHorticulture() == 0.0f ;
    }


    //Checks whether Soil,Power & Water details entered or not
    public static boolean checkForWaterSoilPowerDetails(final Context context) {
        DataAccessHandler dataAccessHandler = new DataAccessHandler(context);
        boolean existed = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().queryWaterResourceCheck(CommonConstants.PLOT_CODE));

        if (existed) {
            return false;
        }
        FollowUp followUp = (FollowUp) DataManager.getInstance().getDataFromManager(DataManager.PLOT_FOLLOWUP);
        if (followUp == null) {
            return false;
        }
        List<WaterResource> waterResource = (ArrayList<WaterResource>) DataManager.getInstance().getDataFromManager(DataManager.SOURCE_OF_WATER);
        return followUp.getIsfarmerreadytoconvert() == 1 && waterResource == null;
    }

    //Checks whether Farmer Photo Taken or not
    public static boolean isFarmerPhotoTaken(final Context context) {
        DataAccessHandler dataAccessHandler = new DataAccessHandler(context);
        boolean existed = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().getSelectedFileRepositoryCheckQuery(CommonConstants.FARMER_CODE, 193));
        if (existed) {
            return true;
        }
        FileRepository fileRepository = (FileRepository) DataManager.getInstance().getDataFromManager(DataManager.FILE_REPOSITORY);
        return fileRepository != null && fileRepository.getPicturelocation() != null && !fileRepository.getPicturelocation().equalsIgnoreCase("null");
    }
    public static boolean isFarmerPhotoSavedInDB(final Context context) {
        DataAccessHandler dataAccessHandler = new DataAccessHandler(context);
        boolean existed = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().getSelectedFileRepositoryCheckQuery(CommonConstants.FARMER_CODE, 193));
        return existed;
    }


    //Checks whether Soil,Power & Water details entered or not in Conversion
    public static boolean isWSPowerDataEntered(final Context context) {
        DataAccessHandler dataAccessHandler = new DataAccessHandler(context);
        boolean existed = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().getWaterResourceBinding(CommonConstants.PLOT_CODE));
        if (existed) {
            return true;
        }
        boolean existed2 = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().getSoilResourceBinding(CommonConstants.PLOT_CODE));
        if (existed2) {
            return true;
        }

        List<WaterResource> mWaterTypeModelList = (ArrayList<WaterResource>) DataManager.getInstance().getDataFromManager(DataManager.SOURCE_OF_WATER);
        if (mWaterTypeModelList != null) {
            return true;
        }

        SoilResource msoilTypeModel = (SoilResource) DataManager.getInstance().getDataFromManager(DataManager.SoilType);
        return msoilTypeModel != null;

    }

    //Checks whether required plots data is entered or not while conversion
    public static boolean isConversionPlotDataEntered() {
        Plot plotData = (Plot) DataManager.getInstance().getDataFromManager(DataManager.PLOT_DETAILS);
        if (plotData == null) {
            return false;
        } else {
            Integer plotCareTakerStatus = plotData.getIsplothandledbycaretaker();
            return !(plotCareTakerStatus == null || plotCareTakerStatus == 0);
        }
    }

    //Checks whether required plots data is entered or not while crop maintenance
    public static boolean isConversionPlotAddressDataEntered() {
        Address plotData = (Address) DataManager.getInstance().getDataFromManager(DataManager.VALIDATE_PLOT_ADDRESS_DETAILS);
        if (plotData == null) {
            return false;
        } else {
            String plotCareTakerStatus = plotData.getLandmark();
            return !TextUtils.isEmpty(plotCareTakerStatus);
        }
    }

    //Checks whether required farmer data is entered or not while crop maintenance
    public static boolean isFarmerMandatoryDataEntered() {
        Farmer farmerData = (Farmer) DataManager.getInstance().getDataFromManager(DataManager.FARMER_PERSONAL_DETAILS);
        if (farmerData == null) {
            return false;
        } else {
            Integer anualIncomeTypeId = farmerData.getAnnualincometypeid();
//            String gaurdianName = farmerData.getGuardianname();
            return !(anualIncomeTypeId == null || anualIncomeTypeId == 0) ;
        }
    }

    //Checks whether required plot data is entered or not while crop maintenance
    public static boolean isPlotDataEntered() {
        Plot enteredPlot = (Plot) DataManager.getInstance().getDataFromManager(DataManager.PLOT_DETAILS);
        if (enteredPlot.getPlotownershiptypeid() == null && enteredPlot.getIsplothandledbycaretaker() == null) {
            return false;
        } else {

            return true ;
        }
    }
    public static boolean isGandermaDatacheck() {
        Ganoderma entereddata = (Ganoderma) DataManager.getInstance()
                .getDataFromManager(DataManager.GANODERMA_DETAILS);

        if (entereddata == null) {
            Log.d("GanodermaDataCheck", "Ganoderma data is null or not entered.");
            return false;
        }

        Log.d("GanodermaDataCheck", "Ganoderma data found: " + entereddata.toString());
        return true;
    }

public static boolean isGandermaDataEntered(Context context) {
    // Retrieve the list of Pest objects
    List<Disease> DiseaseList = (List<Disease>) DataManager.getInstance().getDataFromManager(DataManager.DISEASE_DETAILS);
    DataAccessHandler  dataAccessHandler = new DataAccessHandler(context);
    // Check if the list is null or empty
    if (DiseaseList == null || DiseaseList.isEmpty()) {
        Log.d("PestCheck", "Pest list is empty or null.");
        return false;
    }
    else{
        Log.d("PestCheck", DiseaseList.size()+"");
    }

    // Iterate through the Pest list
    for (Disease disease : DiseaseList) {
        Log.d("Disease Check", "Checking Disease ID: " + disease.getDiseaseid());
        String Diseasename = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getlookupdata(disease.getDiseaseid()));
        if (Diseasename.equalsIgnoreCase("Ganoderma")) {
            Log.d("DiseasenameCheck", "Ganoderma (Diseasename == 349) found.");
            return true; // Return true if any Pest has pestId == 349
        }
    }

    // Log and return false if no Pest with pestId == 349 is found
    Log.d("Disease Check", "Ganoderma  not found in the list.");
    return false;
}

    public static boolean ispestdata(Context context) {
        // Retrieve the list of Pest objects
        List<MainPestModel> pestList = (List<MainPestModel>) DataManager.getInstance().getDataFromManager(DataManager.MAIN_PEST_DETAIL);

        // Check if the list is null or empty
        if (pestList == null || pestList.isEmpty()) {
            Log.d("ispestdata", "Pest list is empty or null.");
            return false;
        } else {
            Log.d("ispestdata", "Pest list size: " + pestList.size());
            return true;
        }
    }

    public static boolean isDISEASEdata(Context context) {
        // Retrieve the list of Pest objects
        List<Disease> DiseaseList = (List<Disease>) DataManager.getInstance().getDataFromManager(DataManager.DISEASE_DETAILS);

        // Check if the list is null or empty
        if (DiseaseList == null || DiseaseList.isEmpty()) {
            Log.d("isDiseaseList", "DiseaseList list is empty or null.");
            return false;
        } else {
            Log.d("isDiseaseList", "DiseaseList list size: " + DiseaseList.size());
            return true;
        }
    }
    public static boolean isNDdata(Context context) {
        // Retrieve the list of Pest objects
        List<Nutrient> NutrientList = (List<Nutrient>) DataManager.getInstance().getDataFromManager(DataManager.NUTRIENT_DETAILS);

        // Check if the list is null or empty
        if (NutrientList == null || NutrientList.isEmpty()) {
            Log.d("isNutrientList", "NutrientList list is empty or null.");
            return false;
        } else {
            Log.d("isNutrientList", "NutrientList list size: " + NutrientList.size());
            return true;
        }
    }


//    public static boolean isGandermaDataEntered(Context context) {
//        // Retrieve the list of Pest objects
//        List<MainPestModel> pestList = (List<MainPestModel>) DataManager.getInstance()
//                .getDataFromManager(DataManager.MAIN_PEST_DETAIL);
//        DataAccessHandler dataAccessHandler = new DataAccessHandler(context);
//
//        // Check if the list is null or empty
//        if (pestList == null || pestList.isEmpty()) {
//            Log.d("PestCheck", "Pest list is empty or null.");
//            return false;
//        }
//
//        // Iterate through the Pest list
//        for (MainPestModel pest : pestList) {
//            Log.d("PestCheck", "Checking Pest ID: " + pest.getPest().getPestid());
//            String pestName = dataAccessHandler.getOnlyOneValueFromDb(
//                    Queries.getInstance().getlookupdata(pest.getPest().getPestid()));
//
//            if ("Ganoderma".equalsIgnoreCase(pestName)) {
//                Log.d("PestCheck", "Ganoderma infestation detected.");
//                return true; // Return true if Ganoderma is found
//            }
//        }
//
//        // If no Ganoderma infestation is detected
//        Log.d("PestCheck", "Ganoderma infestation not found in the list.");
//        return false;
//    }

// Unit tests for isGandermaDataEntered()
//@Test
//public void testIsGandermaDataEntered_withNullList() {
//    DataManager.getInstance().setDataForManager(DataManager.PEST_DETAILS, null);
//    assertFalse(CommonUiUtils.isGandermaDataEntered());
//}
//
//@Test
//public void testIsGandermaDataEntered_withEmptyList() {
//    DataManager.getInstance().setDataForManager(DataManager.PEST_DETAILS, new ArrayList<>());
//    assertFalse(CommonUiUtils.isGandermaDataEntered());
//}
//
//@Test
//public void testIsGandermaDataEntered_withGanderma() {
//    List<Pest> pests = new ArrayList<>();
//    pests.add(new Pest(349, "Ganderma"));
//    DataManager.getInstance().setDataForManager(DataManager.PEST_DETAILS, pests);
//    assertFalse(CommonUiUtils.isGandermaDataEntered());
//}
//
//@Test
//public void testIsGandermaDataEntered_withoutGanderma() {
//    List<Pest> pests = new ArrayList<>();
//    pests.add(new Pest(348, "Other"));
//    DataManager.getInstance().setDataForManager(DataManager.PEST_DETAILS, pests);
//    assertTrue(CommonUiUtils.isGandermaDataEntered());
//}
//
//
//











    public static boolean isComplaintsDataEntered() {
        Complaints complaintsData = (Complaints) DataManager.getInstance().getDataFromManager(DataManager.NEW_COMPLAINT_DETAILS);
        return complaintsData != null;
    }
}
