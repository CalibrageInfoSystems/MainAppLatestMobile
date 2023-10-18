package com.oilpalm3f.mainapp.palmcare;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.oilpalm3f.mainapp.R;
import com.oilpalm3f.mainapp.alerts.AlertPlotInfoAdapter;
import com.oilpalm3f.mainapp.alerts.AlertsVisitsInfo;
import com.oilpalm3f.mainapp.common.CommonUtils;
import com.oilpalm3f.mainapp.ui.RecyclerItemClickListener;
import com.oilpalm3f.mainapp.utils.UiUtils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClosedcropDetailsAdapter extends RecyclerView.Adapter<ClosedcropDetailsAdapter.ViewHolder>{

    private static final String LOG_TAG = AlertPlotInfoAdapter.class.getName();
    private List<ClosedDataDetails> mList;
    private Context context;
    private ClosedDataDetails item;
    private ButtonClickListener recyclerItemClickListener;

    private DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private  DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public ClosedcropDetailsAdapter(Context context, List<ClosedDataDetails> mList,ButtonClickListener listener) {
        this.context = context;
        this.mList = mList;
        this.recyclerItemClickListener = listener;
    }

    public void addItems(List<ClosedDataDetails> list) {
        this.mList = new ArrayList<>(list);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.close_crop_item,  viewGroup, false);
        ViewHolder myHolder = new ViewHolder(view);
        return myHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        item = mList.get(position);

        holder.farmername.setText(item.getFarmerName());
//        if (!TextUtils.isEmpty(item.getContactNumber()) && !  item.getContactNumber().equalsIgnoreCase("null")) {
//            holder.tvcontactNumber.setText(item.getContactNumber());
//        } else {
//            holder.tvcontactNumber.setText("");
//        }
        holder.villagename.setText(item.getVillageName() != null ? item.getVillageName().trim() : "");
        holder.contactNumber.setText(item.getContactNumber() != null ? item.getContactNumber().trim() : "");
        holder.clusterName.setText(item.getClusterName() != null ? item.getClusterName().trim() : "");
        holder.plotid.setText(item.getPlotCode() != null ? item.getPlotCode().trim() : "");
        holder.Farmerid.setText(item.getFarmerCode() != null ? item.getFarmerCode().trim() : "");
        holder.palmarea.setText(item.getTotalPalmArea() != null ? item.getTotalPalmArea().trim() : "");
        holder.visitedby.setText(item.getUserName() != null ? item.getUserName().trim() : "");
        holder.Cropcode.setText(item.getCropCode() != null ? item.getCropCode().trim() : "");
        if(item.getCreatedDate() != null && !(item.getCreatedDate().trim().isEmpty())){
            String dop = item.getCreatedDate().replace("T"," ");

            Date date = null;
            try {
                date = inputFormat.parse(dop);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            String DateOP = outputFormat.format(date);
            holder.lastvisiteddate.setText(DateOP);
        }
        else{
            holder.lastvisiteddate.setText("");
        }



            holder.generateotp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (CommonUtils.isNetworkAvailable(context)) {
                    recyclerItemClickListener.onItemSelected(mList.get(position));
                    holder.generateotp.setVisibility(View.GONE);
                    holder.resendotp.setVisibility(View.VISIBLE);
                    holder.closebtn.setClickable(true);
                    holder.closebtn.setBackground(context.getResources().getDrawable(R.drawable.btn_stateful));
                    }
                    else
                    {

                        UiUtils.showCustomToastMessage("Please check network connection", context, 1);

                    }
                }
            });


        holder.resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerItemClickListener.onItemSelected(mList.get(position));
            }
        });

        holder.closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonUtils.isNetworkAvailable(context)) {
                recyclerItemClickListener.onItemclosed(mList.get(position));
                }
                else
                {

                    UiUtils.showCustomToastMessage("Please check network connection", context, 1);

                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void updateAdapter(List<ClosedDataDetails> list) {
        this.mList = list;
        notifyDataSetChanged();
    }
    public interface ButtonClickListener {
        void onItemSelected(ClosedDataDetails AlertsVisitsInfo);
        void onItemclosed(ClosedDataDetails AlertsVisitsInfo);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView plotid;

        private TextView Farmerid;
        private TextView farmername;
        private TextView contactNumber;
        private TextView villagename;
        private TextView clusterName;
        private TextView palmarea;
        private TextView visitedby,Cropcode;
        private TextView lastvisiteddate;
Button generateotp,resendotp,closebtn;
        private View convertView;

        public ViewHolder(View view) {
            super(view);
            convertView = view;

            plotid = (TextView) view.findViewById(R.id.plotid);
            Farmerid = (TextView) view.findViewById(R.id.Farmerid);
            farmername = (TextView) view.findViewById(R.id.farmername);
            contactNumber = (TextView) view.findViewById(R.id.contactNumber);
            villagename = (TextView) view.findViewById(R.id.villagename);
            clusterName = (TextView) view.findViewById(R.id.clusterName);
            palmarea = (TextView) view.findViewById(R.id.palmarea);
            Cropcode = (TextView) view.findViewById(R.id.Cropcode);
            visitedby = (TextView) view.findViewById(R.id.visitedby);
            lastvisiteddate = (TextView) view.findViewById(R.id.lastvisiteddate);
            generateotp = (Button) view.findViewById(R.id.generateotp);
            resendotp = (Button) view.findViewById(R.id.resendotp);
            closebtn = (Button) view.findViewById(R.id.closebtn);
        }
    }


}
