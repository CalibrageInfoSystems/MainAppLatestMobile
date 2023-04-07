package com.oilpalm3f.mainapp.cropmaintenance;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oilpalm3f.mainapp.R;
import com.oilpalm3f.mainapp.dbmodels.ViewRecoveryFarmerModel;

import java.util.ArrayList;

public class ViewRecoveryFarmerAdapter extends RecyclerView.Adapter<ViewRecoveryFarmerAdapter.MyViewHolder>{

    private Context context;
    ArrayList<ViewRecoveryFarmerModel> recoveryfarmers_arrayList= new ArrayList<ViewRecoveryFarmerModel>();
    int a = 0;


    public ViewRecoveryFarmerAdapter(Context context, ArrayList<ViewRecoveryFarmerModel> recoveryfarmers_arrayList) {
        this.context = context;
        this.recoveryfarmers_arrayList = recoveryfarmers_arrayList;

    }


    @Override
    public ViewRecoveryFarmerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewrecoveryfarmerdata, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int i) {

        String fullname = "", middleName = "",active="false";

//        Log.d("view_recieve_Active", recoveryfarmers_arrayList.get(i).getIsActive() + "");
//        Log.d("view_recieve_Active", recoveryfarmers_arrayList.get(0).getFirstName() + "");

        if (recoveryfarmers_arrayList.get(i).getFirstName() != null){
            if (!TextUtils.isEmpty(recoveryfarmers_arrayList.get(i).getMiddleName()) && !
                    recoveryfarmers_arrayList.get(i).getMiddleName().equalsIgnoreCase("null")) {
                middleName = recoveryfarmers_arrayList.get(i).getMiddleName();
            }
            if (recoveryfarmers_arrayList.get(i).getIsActive() == 0){
                active = "False";
            }else{
                active = "True";
            }
            fullname = recoveryfarmers_arrayList.get(i).getFirstName().trim() + " " + middleName + " " + recoveryfarmers_arrayList.get(i).getLastname().trim();
        }else{
            holder.view_palmArea_lyt.setVisibility(View.GONE);
            holder.view_statename.setVisibility(View.GONE);
            holder.view_districtname.setVisibility(View.GONE);
            holder.view_mandalname.setVisibility(View.GONE);
            holder.view_villagename.setVisibility(View.GONE);
            holder.view_farmername.setVisibility(View.GONE);
        }

        a = holder.getAdapterPosition() + 1;

        holder.view_staticnum.setText("Recovery Farmer (" + a + ")");
        //holder.view_staticnum.setText("Recovery Farmer (" + holder.getPosition() + ")");

        holder.view_recieve_farmercode.setText(" :  "+ recoveryfarmers_arrayList.get(i).getRecoveryFarmerCode());
        holder.view_recieve_farmername.setText(" :  "+ fullname);
        holder.view_recieve_statename.setText(" :  "+recoveryfarmers_arrayList.get(i).getStateName());
        holder.view_recieve_districtname.setText(" :  "+recoveryfarmers_arrayList.get(i).getDistrictName());
        holder.view_recieve_villagename.setText(" :  "+recoveryfarmers_arrayList.get(i).getVillageName());
        holder.view_recieve_mandalname.setText(" :  "+recoveryfarmers_arrayList.get(i).getMandalName());
        holder.view_recieve_plamarea.setText(" :  "+ recoveryfarmers_arrayList.get(i).getPalmArea());

        //holder.view_recieve_Active.setText(" :  "+active);
    }

    @Override
    public int getItemCount() {
        return recoveryfarmers_arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView viewmain_recieve_farmercode, view_recieve_farmername, view_recieve_villagename,view_recieve_farmercode,view_recieve_mandalname,view_recieve_districtname,view_recieve_statename,view_staticnum,view_recieve_plamarea,view_recieve_Active;
        public LinearLayout view_active_lyt,view_palmArea_lyt, view_statename, view_districtname,view_mandalname,view_villagename,view_farmername;



        public MyViewHolder(@NonNull View view) {
            super(view);

            viewmain_recieve_farmercode = view.findViewById(R.id.viewmain_recieve_farmercode);
            view_recieve_farmername = view.findViewById(R.id.view_recieve_farmername);
            view_recieve_farmercode = view.findViewById(R.id.view_recieve_farmercode);
            view_recieve_villagename = view.findViewById(R.id.view_recieve_villagename);
            view_recieve_mandalname = view.findViewById(R.id.view_recieve_mandalname);
            view_recieve_districtname = view.findViewById(R.id.view_recieve_districtname);
            view_recieve_statename = view.findViewById(R.id.view_recieve_statename);
            view_recieve_plamarea = view.findViewById(R.id.view_recieve_plamarea);
            view_staticnum = view.findViewById(R.id.view_staticnum);
            view_recieve_Active = view.findViewById(R.id.view_recieve_Active);

            view_palmArea_lyt = view.findViewById(R.id.view_palmArea_lyt);
            view_statename = view.findViewById(R.id.view_statename);
            view_districtname = view.findViewById(R.id.view_districtname);
            view_mandalname = view.findViewById(R.id.view_mandalname);
            view_villagename = view.findViewById(R.id.view_villagename);
            view_farmername = view.findViewById(R.id.view_farmername);
            view_active_lyt = view.findViewById(R.id.view_active_lyt);

        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
