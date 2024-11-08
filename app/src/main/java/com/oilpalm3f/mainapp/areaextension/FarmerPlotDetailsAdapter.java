package com.oilpalm3f.mainapp.areaextension;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.oilpalm3f.mainapp.R;
import com.oilpalm3f.mainapp.cloudhelper.Log;
import com.oilpalm3f.mainapp.common.CommonConstants;
import com.oilpalm3f.mainapp.common.CommonUtils;
import com.oilpalm3f.mainapp.database.DataAccessHandler;
import com.oilpalm3f.mainapp.database.Queries;
import com.oilpalm3f.mainapp.farmersearch.DisplayPlotsFragment;
import com.oilpalm3f.mainapp.dbmodels.PlotDetailsObj;
import com.oilpalm3f.mainapp.uihelper.SelectableAdapter;
import com.oilpalm3f.mainapp.utils.UiUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by skasam on 9/28/2016.
 */

//To Bind the farmer plot details
public class FarmerPlotDetailsAdapter extends SelectableAdapter<FarmerPlotDetailsAdapter.PlotDetailsViewHolder> {


    private Context context;
    private List<PlotDetailsObj> plotlist;
    private PlotDetailsObj plotdetailsObj;
    private ClickListener clickListener;
    private int layoutResourceId;
    private boolean showArrow;
    DataAccessHandler dataAccessHandler;
    private double currentLatitude, currentLongitude;


    public FarmerPlotDetailsAdapter(Context context, List<PlotDetailsObj> plotlist, int layoutResourceId, boolean showArrow) {
        this.context = context;
        this.plotlist = plotlist;
        this.layoutResourceId = layoutResourceId;
        this.showArrow = showArrow;
    }

    @Override
    public PlotDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layoutResourceId, null);
        PlotDetailsViewHolder myHolder = new PlotDetailsViewHolder(view);
        return myHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final PlotDetailsViewHolder holder, final int position) {

        plotdetailsObj = plotlist.get(position);

        holder.tvplotId.setText("Plot Code : " + plotdetailsObj.getPlotID());

        if (!TextUtils.isEmpty(plotdetailsObj.getPlotLandMark())) {
            holder.tvlandmark.setText("Plot LandMark : " + plotdetailsObj.getPlotLandMark());
            holder.tvlandmark.setVisibility(View.VISIBLE);
        } else {
            holder.tvlandmark.setVisibility(View.GONE);
        }
        if (CommonUtils.isFromCropMaintenance() || CommonUtils.isComplaint() || CommonUtils.isFromHarvesting()) {
            holder.tvPlotDop.setVisibility(View.VISIBLE);
            holder.tvPlotDop.setText("Date Of Planting : " + plotdetailsObj.getDateofPlanting());
        } else {
            holder.tvPlotDop.setVisibility(View.GONE);
        }

        if (CommonUtils.isFromCropMaintenance() || CommonUtils.isComplaint() || CommonUtils.isFromHarvesting()) {

            holder.tvplotarea.setText("Total Palm Area : " + plotdetailsObj.getTotalPalm() + " Ha");
        } else {
            holder.tvplotarea.setText("Total Plot Area : " + plotdetailsObj.getPlotArea() + " Ha");
        }

        holder.tvplotvillage.setText("Plot Village : " + plotdetailsObj.getVillageName());
        if (!TextUtils.isEmpty(plotdetailsObj.getSurveyNumber()) && !plotdetailsObj.getSurveyNumber().equalsIgnoreCase("null")) {
            holder.tvplotsurveynumber.setText("Plot SurveyNumber : " + plotdetailsObj.getSurveyNumber());
        } else {
            holder.tvplotsurveynumber.setVisibility(View.GONE);

        }

        dataAccessHandler = new DataAccessHandler(context);

//        String visitCount = dataAccessHandler.getOnlyTwoValueFromDb(Queries.getInstance().getVisitCount(plotdetailsObj.getPlotID()));
//
//        String count = visitCount.split("@")[0];
//
//        String date = visitCount.split("@")[1];
//
//
//        if (CommonUtils.isFromCropMaintenance() || CommonUtils.isFromHarvesting()) {
//
////            if (!count.equals("0")) {
////                holder.vist_count.setText("Plot Visit Count : " + count);
////            } else {
////                holder.vist_count.setVisibility(View.GONE);
////                // holder.lastest_vistDate.setVisibility(View.GONE);
////            }
//
//            if (!TextUtils.isEmpty(date))
//                holder.lastest_vistDate.setText("Last Visit Date : " + date.split("T")[0]);
//
//        } else {
//            //holder.vist_count.setVisibility(View.GONE);
//            holder.lastest_vistDate.setVisibility(View.GONE);
//        }


        String visitCount = dataAccessHandler.getOnlyTwoValueFromDb(Queries.getInstance().getVisitCount(plotdetailsObj.getPlotID()));
        String harvestvisitCount = dataAccessHandler.getOnlyTwoValueFromDb(Queries.getInstance().getHarvestVisitCount(plotdetailsObj.getPlotID()));


        String count = visitCount.split("@")[0];
        String cmlastvisitdate = visitCount.split("@")[1];
        Log.d("CMdate", cmlastvisitdate + "");
        String harvestcount = harvestvisitCount.split("@")[0];
        String lastharvestdate = harvestvisitCount.split("@")[1];
        Log.d("Harvestdate", lastharvestdate + "");

        SimpleDateFormat inputFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss" );
        SimpleDateFormat inputFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss");

        if (CommonUtils.isFromCropMaintenance() || CommonUtils.isFromHarvesting()) {
            // Handling last visit date
            if (TextUtils.isEmpty(cmlastvisitdate) || cmlastvisitdate.equalsIgnoreCase("null")) {
                holder.lastest_vistDate.setText("Last CM Visit Date : Not Visited");
            } else {
                String formattedDate = formatDate(cmlastvisitdate, inputFormat1, inputFormat2, outputFormat);
                Log.d("formattedCMDate", formattedDate + "");
                if (!TextUtils.isEmpty(formattedDate)) {
                    String[] dateTimeParts = formattedDate.split("T");
                    if (dateTimeParts.length == 2) {
                        holder.lastest_vistDate.setText("Last CM Visit Date : " + dateTimeParts[0] + " " + dateTimeParts[1]);
                    } else {
                        holder.lastest_vistDate.setText("Last CM Visit Date : " + formattedDate);
                    }
                } else {
                    holder.lastest_vistDate.setText("Last CM Visit Date : Not Visited");
                }
            }

            // Handling last harvest date
            if (TextUtils.isEmpty(lastharvestdate) || lastharvestdate.equalsIgnoreCase("null")) {
                holder.lastest_harvestDate.setText("Last Harvesting Date : Not Visited");
            } else {
                String formattedHarvestDate = formatDate(lastharvestdate, inputFormat1, inputFormat2, outputFormat);
                Log.d("formattedHarvestingDate", formattedHarvestDate + "");
                if (!TextUtils.isEmpty(formattedHarvestDate)) {
                    String[] dateTimeParts = formattedHarvestDate.split("T");
                    if (dateTimeParts.length == 2) {
                        holder.lastest_harvestDate.setText("Last Harvesting Date : " + dateTimeParts[0] + " " + dateTimeParts[1]);
                    } else {
                        holder.lastest_harvestDate.setText("Last Harvesting Date : " + formattedHarvestDate);
                    }
                } else {
                    holder.lastest_harvestDate.setText("Last Harvesting Date : Not Visited");
                }
            }
        } else {
            holder.lastest_vistDate.setVisibility(View.GONE);
            holder.lastest_harvestDate.setVisibility(View.GONE);
        }

//
//        String count = visitCount.split("@")[0];
//        String cmlastvisitdate = visitCount.split("@")[1];
//        Log.d("CMdate", cmlastvisitdate + "");
//        String harvestcount = harvestvisitCount.split("@")[0];
//        String lastharvestdate = harvestvisitCount.split("@")[1];
//        Log.d("Harvestdate", lastharvestdate + "" );
//
//        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss");
//
//        if (CommonUtils.isFromCropMaintenance() || CommonUtils.isFromHarvesting()) {
//            // Handling last visit date
//            if (TextUtils.isEmpty(cmlastvisitdate) ||  cmlastvisitdate.equalsIgnoreCase("null")) {
//
//                holder.lastest_vistDate.setText("Last CM Visit Date : " + "Not Visited");
//
//            } else {
//                Date parsedDate = null;
//                try {
//                    parsedDate = inputFormat.parse(cmlastvisitdate);
//                } catch (ParseException e) {
//                    throw new RuntimeException(e);
//                }
//                String formattedDate = outputFormat.format(parsedDate);
//
//                Log.d("formattedCMDate", formattedDate + "");
//
//                String[] dateTimeParts = formattedDate.split("T");
//                if (dateTimeParts.length == 2) {
//                    holder.lastest_vistDate.setText("Last CM Visit Date : " + dateTimeParts[0] + " " + dateTimeParts[1]);
//                } else {
//                    holder.lastest_vistDate.setText("Last CM Visit Date : " + formattedDate);
//                }
//            }
//
//            // Handling last harvest date
//            if (TextUtils.isEmpty(lastharvestdate) || lastharvestdate.equalsIgnoreCase("null")) {
//                holder.lastest_harvestDate.setText("Last Harvesting Date : " + "Not Visited");
//
//            } else {
//                Date parsedDate = null;
//                try {
//                    parsedDate = inputFormat.parse(lastharvestdate);
//                } catch (ParseException e) {
//                    throw new RuntimeException(e);
//                }
//                String formattedHarvestDate = outputFormat.format(parsedDate);
//
//                Log.d("formattedHarvestingDate", formattedHarvestDate + "");
//
//
//                String[] dateTimeParts = formattedHarvestDate.split("T");
//                if (dateTimeParts.length == 2) {
//                    holder.lastest_harvestDate.setText("Last Harvesting Date : " + dateTimeParts[0] + " " + dateTimeParts[1]);
//                } else {
//                    holder.lastest_harvestDate.setText("Last Harvesting Date : " + formattedHarvestDate);
//                }
//            }
//        }
//        else {
//            //holder.vist_count.setVisibility(View.GONE);
//            holder.lastest_vistDate.setVisibility(View.GONE);
//            holder.lastest_harvestDate.setVisibility(View.GONE);
//        }



        holder.convertView.setOnClickListener(v -> {
            Log.v(FarmerPlotDetailsAdapter.class.getSimpleName(), "#### clicked position " + position);
            clickListener.onItemClicked(position, holder.convertView);
        });

        holder.arrowImage.setVisibility((showArrow) ? View.VISIBLE : View.INVISIBLE);
        holder.ivb_plot_location_cropcollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonConstants.PLOT_CODE = plotdetailsObj.getPlotID();
                final String selectedLatLong = new DataAccessHandler(context).getLatLongs(Queries.getInstance().queryVerifyGeoTag());

                if (!selectedLatLong.isEmpty()) {
                    final String[] yieldDataArr = selectedLatLong.split("-");
                    final String latlong[] = new DisplayPlotsFragment().getLatLong(context, false).split("@");
                    String land_lattitude = yieldDataArr[0].replaceAll("[\\s\\-()]", "");
                    String land_longitude = yieldDataArr[1].replaceAll("[\\s\\-()]", "");
                    if (land_longitude.isEmpty()) {
                        try {
                            land_longitude = yieldDataArr[2].replaceAll("[\\s\\-()]", "");
                        } catch (Exception e) {
                            land_longitude = "00.000";
                        }
                    }

                    currentLatitude = Double.parseDouble(latlong[0]);
                    currentLongitude = Double.parseDouble(latlong[1]);
                    String uri = "http://maps.google.com/maps?saddr=" + currentLatitude + "," + currentLongitude + "(" + "Village Name = " + plotdetailsObj.getVillageName() + "/" + "LandMark = " + plotdetailsObj.getPlotLandMark() + ")&daddr=" + land_lattitude + "," + land_longitude;
//                    String uri = String.format(Locale.ROOT, "geo:%f,%f?z=%d&q=%f,%f (%s)",
//                            ""+latlong[0], ""+latlong[1], 100, ""+yieldDataArr[0], ""+yieldDataArr[1], "Village Name = " + prospectivePlotsModels.get(position).getPlotVillageName() +  "MandalName = " + prospectivePlotsModels.get(position).getMandalName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    context.startActivity(intent);

                } else {

                    UiUtils.showCustomToastMessage("This Plot has not Lat_Long to show plot on Google Map", context, 1);
                }


            }
        });

    }

    private String formatDate(String dateStr, SimpleDateFormat inputFormat1, SimpleDateFormat inputFormat2, SimpleDateFormat outputFormat) {
        Date date = null;
        try {
            if (dateStr.contains("T")) {
                date = inputFormat1.parse(dateStr);
            } else {
                date = inputFormat2.parse(dateStr);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null) {
            return outputFormat.format(date);
        } else {
            return "";
        }
    }

    @Override
    public int getItemCount() {
        return plotlist.size();
    }

    public static class PlotDetailsViewHolder extends RecyclerView.ViewHolder {
        private TextView tvplotId;
        private TextView tvlandmark;
        private TextView tvplotarea;
        private TextView tvplotvillage;
        private TextView tvplotsurveynumber, tvPlotDop;
        private TextView vist_count, lastest_vistDate, lastest_harvestDate;
        private ImageView arrowImage;
        private View convertView;
        private ImageButton ivb_plot_location_cropcollection;

        public PlotDetailsViewHolder(View view) {
            super(view);
            this.convertView = view;
            tvplotId = view.findViewById(R.id.tvplotidvalue);
            tvlandmark = view.findViewById(R.id.tvplotlandmarkvalue);
            tvplotarea = view.findViewById(R.id.tvplotareavalue);
            tvplotvillage = view.findViewById(R.id.tvplotvillagevalue);
            tvPlotDop = view.findViewById(R.id.tvplotDOP);
            tvplotsurveynumber = view.findViewById(R.id.tvplotsurveyvalue);
            arrowImage = view.findViewById(R.id.arrow_right);
            vist_count = view.findViewById(R.id.vistCount);
            lastest_vistDate = view.findViewById(R.id.lastest_vistDate);
            lastest_harvestDate = view.findViewById(R.id.lastest_harvestDate);
            ivb_plot_location_cropcollection = view.findViewById(R.id.ivb_plot_location_cropcollection);
        }
    }

    public void setOnClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClicked(int position, View view);
    }
}
