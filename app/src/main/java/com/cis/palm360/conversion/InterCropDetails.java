package com.cis.palm360.conversion;

public class InterCropDetails {
    private String cropGrown;
    private String recommendedCrop;

    public InterCropDetails(String cropGrown, String recommendedCrop) {
        this.cropGrown = cropGrown;
        this.recommendedCrop = recommendedCrop;
    }

    public String getCropGrown() {
        return cropGrown;
    }

    public String getRecommendedCrop() {
        return recommendedCrop;
    }
}
