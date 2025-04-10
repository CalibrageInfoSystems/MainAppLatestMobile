package com.cis.palm360.palmcare;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cis.palm360.R;
import com.cis.palm360.alerts.AlertPlotInfoAdapter;
import com.cis.palm360.ui.RecyclerItemClickListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NoVisitsInfoAdapter extends RecyclerView.Adapter<NoVisitsInfoAdapter.ViewHolder>{

    private static final String LOG_TAG = AlertPlotInfoAdapter.class.getName();
    private List<NotVisitedPlotsInfo> mList;
    private Context context;
    private NotVisitedPlotsInfo item;
    private RecyclerItemClickListener recyclerItemClickListener;

    private DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private  DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public NoVisitsInfoAdapter(Context context, List<NotVisitedPlotsInfo> mList) {
        this.context = context;
        this.mList = mList;
    }

    public void addItems(List<NotVisitedPlotsInfo> list) {
        this.mList = new ArrayList<>(list);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.no_visits_item, viewGroup, false);
        ViewHolder myHolder = new ViewHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        item = mList.get(position);

        holder.tvfirstName.setText(item.getFarmerName());
//
        holder.tvvillageName.setText(item.getVillageName() != null ? item.getVillageName().trim() : "");
     holder.tvPlotcluster.setText(item.getClusterName() != null ? item.getClusterName().trim() : "");
        holder.tvplotCode.setText(item.getPlotCode() != null ? item.getPlotCode().trim() : "");
       holder.tvContactNumber.setText(item.getContactNumber() != null ? item.getContactNumber().trim() : "");
        holder.tvfarmerCode.setText(item.getFarmerCode() != null ? item.getFarmerCode().trim() : "");
        holder.tvtotalPlotArea.setText(item.getTotalPalmArea() != null ? item.getTotalPalmArea().trim() : "");
        holder.tvvisitedby.setText(item.getVisitedBy() != null ? item.getVisitedBy().trim() : "");
        if(item.getLastvisiteddate() != null && !(item.getLastvisiteddate().trim().isEmpty())){
            String dop = item.getLastvisiteddate().replace("T"," ");

            Date date = null;
            try {
                date = inputFormat.parse(dop);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            String DateOP = outputFormat.format(date);
            holder.tvplotvisiteddate.setText(DateOP);
        }
        else{
            holder.tvplotvisiteddate.setText("");
        }



    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void updateAdapter(List<NotVisitedPlotsInfo> list) {
        this.mList = list;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvplotCode;
        private TextView tvfarmerCode;
        private TextView tvfirstName;
        private TextView tvPlotcluster;
        private TextView tvvisitedby;
        private TextView tvContactNumber;
        private TextView tvvillageName;
        private TextView tvtotalPlotArea;
        private TextView tvdateofplanting;
        private TextView tvplotvisiteddate;

        private View convertView;

        public ViewHolder(View view) {
            super(view);
            convertView = view;

            tvplotCode = (TextView) view.findViewById(R.id.tvPlotID);
            tvfarmerCode = (TextView) view.findViewById(R.id.tv_farmercode);
            tvfirstName = (TextView) view.findViewById(R.id.tv_farmername);
         // tvPlotcluster = (TextView) view.findViewById(R.id.tvContactNumber);
            tvPlotcluster = (TextView) view.findViewById(R.id.tvPlotcluster);
            tvvillageName = (TextView) view.findViewById(R.id.tvPlotVillage);
            tvtotalPlotArea = (TextView) view.findViewById(R.id.tvSize);
            tvvisitedby = (TextView) view.findViewById(R.id.tvvisitedby);
            tvplotvisiteddate = (TextView) view.findViewById(R.id.tvplotvisiteddate);
            tvContactNumber = (TextView) view.findViewById(R.id.tv_contactNumber);
        }
    }
}
