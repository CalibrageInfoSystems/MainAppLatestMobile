package com.cis.palm360.cropmaintenance;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cis.palm360.R;
import com.cis.palm360.dbmodels.RecoveryFarmerModelnew;

import java.util.ArrayList;

public class RecoveryFarmerAdapter extends RecyclerView.Adapter<RecoveryFarmerAdapter.MyViewHolder> {

    private Context context;
    ArrayList<RecoveryFarmerModelnew> recovery_arrayList= new ArrayList<RecoveryFarmerModelnew>();
    int a = 0;
    private recoveryfarmerListListner listener;

    public RecoveryFarmerAdapter(Context context, ArrayList<RecoveryFarmerModelnew> recoveryFarmer_arrayList,recoveryfarmerListListner _listener) {
        this.context = context;

        this.recovery_arrayList = recoveryFarmer_arrayList;

        this.listener =_listener;

    }

    @Override
    public RecoveryFarmerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recoveryfarmerdata, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.recieve_farmercode.setText("  :  " + recovery_arrayList.get(position).getCode() + " ( "+ recovery_arrayList.get(position).getFirstName() + " )");
        holder.recieve_villagename.setText("  :  " + recovery_arrayList.get(position).getVillageName());
        holder.recieve_mandalname.setText("  :  " + recovery_arrayList.get(position).getMandalName());
        holder.recieve_districtname.setText("  :  " + recovery_arrayList.get(position).getDistrictName());
        holder.recieve_statename.setText("  :  " + recovery_arrayList.get(position).getStateName());
        holder.recieve_plamarea.setText("  :  " + recovery_arrayList.get(position).getPalmArea());

        a = holder.getAdapterPosition() + 1;

        holder.staticnum.setText("Recovery Farmer (" + a + ")");

        holder.imgdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Delete Specific Image
                listener.onItemDelete(position);
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return recovery_arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView recieve_villagename, farmercode,recieve_farmercode,recieve_mandalname,recieve_districtname,recieve_statename,staticnum,recieve_plamarea;
        public ImageView thumbnail;
        public LinearLayout palmArea_lyt;
        public ImageView imgdelete;
        public MyViewHolder(@NonNull View view) {
            super(view);

            recieve_farmercode = view.findViewById(R.id.recieve_farmercode);
            recieve_villagename = view.findViewById(R.id.recieve_villagename);
            recieve_mandalname = view.findViewById(R.id.recieve_mandalname);
            recieve_districtname = view.findViewById(R.id.recieve_districtname);
            recieve_statename = view.findViewById(R.id.recieve_statename);
            recieve_plamarea = view.findViewById(R.id.recieve_plamarea);
            palmArea_lyt = view.findViewById(R.id.palmArea_lyt);
            imgdelete=  view.findViewById(R.id.imgdelete);
            staticnum = view.findViewById(R.id.staticnum);
        }
    }

    public interface recoveryfarmerListListner {
        void onItemDelete(int po);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
