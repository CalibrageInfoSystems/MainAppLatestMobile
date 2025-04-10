package com.cis.palm360.cropmaintenance;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cis.palm360.R;
import com.cis.palm360.database.DataAccessHandler;
import com.cis.palm360.database.Queries;
import com.cis.palm360.dbmodels.Disease;

import java.util.ArrayList;
import java.util.List;


public class DiseaseHistoryAdapter extends RecyclerView.Adapter<DiseaseHistoryAdapter.DiseaseViewHolder> {


    private Context context;
    private List<Disease> DiseaseList = new ArrayList<>();
    private DataAccessHandler dataAccessHandler; // Assuming you have access to this

    public DiseaseHistoryAdapter(Context context, List<Disease>diseasesList) {
        this.context = context;
        this.DiseaseList = diseasesList;
        this.dataAccessHandler = new DataAccessHandler(context); // Initialize as needed
    }

    @NonNull
    @Override
    public DiseaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_disease_history, parent, false);
        return new DiseaseViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull DiseaseViewHolder holder, int position) {
        dataAccessHandler = new DataAccessHandler(context);
        Disease historydisease= DiseaseList.get(position);


        String disease = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getlookupdata(historydisease.getDiseaseid()));

        holder.diseasesseen.setText(disease);

        if (historydisease.getChemicalid() != null){
            holder.diseasechemicalusedll.setVisibility(View.VISIBLE);
            String chemicalused = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getlookupdata(historydisease.getChemicalid()));
            holder.diseasechemicalused.setText(chemicalused);
        }else{
            holder.diseasechemicalusedll.setVisibility(View.GONE);
        }
        if (historydisease.getPercTreesId() != 0){
            holder.diseasepercentageoftreesll.setVisibility(View.VISIBLE);
            String percentageoftress = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getlookupdata(historydisease.getPercTreesId()));
            holder.diseasepercentageoftrees.setText(percentageoftress);
        }else{
            holder.diseasepercentageoftreesll.setVisibility(View.GONE);
        }


        if (historydisease.getRecommendFertilizerProviderId() != null){
            holder.diseaserecommendedchemicall.setVisibility(View.VISIBLE);
            String fertilizerrec = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getlookupdata(historydisease.getRecommendFertilizerProviderId()));
            holder.diseaserecommendedchemical.setText(fertilizerrec);
        }else{
            holder.diseaserecommendedchemicall.setVisibility(View.GONE);
        }

        if (historydisease.getRecommendDosage() != 0.0){
            holder.diseaserecommendeddosagell.setVisibility(View.VISIBLE);
            holder.diseaserecommendeddosage.setText(historydisease.getRecommendDosage() + "");
        }else{
            holder.diseaserecommendeddosagell.setVisibility(View.GONE);
        }

        if (historydisease.getRecommendUOMId() != null){
            holder.diseaserecommendeduomll.setVisibility(View.VISIBLE);
            String recommendedUOM = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getUOMdata(historydisease.getRecommendUOMId()));
            holder.diseaserecommendeduom.setText(recommendedUOM);
        }else{
            holder.diseaserecommendeduomll.setVisibility(View.GONE);
        }

        if (historydisease.getRecommendedUOMId() != null){
            holder.diseaserecommenduomperll.setVisibility(View.VISIBLE);
            String recommendedUOMper = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getUOMdata(historydisease.getRecommendedUOMId()));
            holder.diseaserecommenduomper.setText(recommendedUOMper);
        }else{
            holder.diseaserecommenduomperll.setVisibility(View.GONE);
        }

//            if (!(TextUtils.isEmpty(diseaselastvisitdatamap.get(0).getComments()))){
//                diseasecommentsll.setVisibility(View.VISIBLE);
//                diseasecomments.setText(diseaselastvisitdatamap.get(0).getComments() + "");
//            }else{
//                diseasecomments.setVisibility(View.GONE);
//            }

        if (TextUtils.isEmpty(historydisease.getComments()) || historydisease.getComments().equalsIgnoreCase("")  || historydisease.getComments().equalsIgnoreCase(null) || historydisease.getComments().equalsIgnoreCase("null")) {
            holder.diseasecomments.setVisibility(View.GONE);
        }else{
            holder.diseasecommentsll.setVisibility(View.VISIBLE);
            holder.diseasecomments.setText(historydisease.getComments() + "");
        }
        if (position % 2 == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.card_view.setCardBackgroundColor(context.getColor(R.color.white));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.card_view.setCardBackgroundColor(context.getColor(R.color.white2));
            }

        }


    }

    @Override
    public int getItemCount() {
        return DiseaseList.size();
    }

    static class DiseaseViewHolder extends RecyclerView.ViewHolder {

        TextView deficiencyName, fertilizerUsed, percentageOfTrees;
        LinearLayout diseasechemicalusedll,diseasepercentageoftreesll,diseasecontrolmeasuresll,diseaserecommendedchemicall,diseaserecommendeddosagell,
                diseaserecommendeduomll,diseaserecommenduomperll,diseasecommentsll;
        TextView diseasesseen,diseasechemicalused,diseasepercentageoftrees,diseasecontrolmeasures,diseaserecommendedchemical,
                diseaserecommendeddosage,diseaserecommendeduom,diseaserecommenduomper,diseasecomments;
        CardView card_view;
        public DiseaseViewHolder(@NonNull View itemView) {
            super(itemView);

             diseasechemicalusedll = (LinearLayout) itemView.findViewById(R.id.diseasechemicalusedll);
             diseasepercentageoftreesll = (LinearLayout) itemView.findViewById(R.id.diseasepercentageoftreesll);
             diseasecontrolmeasuresll = (LinearLayout) itemView.findViewById(R.id.diseasecontrolmeasuresll);
             diseaserecommendedchemicall = (LinearLayout) itemView.findViewById(R.id.diseaserecommendedchemicall);
             diseaserecommendeddosagell = (LinearLayout) itemView.findViewById(R.id.diseaserecommendeddosagell);
             diseaserecommendeduomll = (LinearLayout) itemView.findViewById(R.id.diseaserecommendeduomll);
             diseaserecommenduomperll = (LinearLayout) itemView.findViewById(R.id.diseaserecommenduomperll);
             diseasecommentsll = (LinearLayout) itemView.findViewById(R.id.diseasecommentsll);

             diseasesseen = (TextView) itemView.findViewById(R.id.diseasesseen);
             diseasechemicalused = (TextView) itemView.findViewById(R.id.diseasechemicalused);
             diseasepercentageoftrees = (TextView) itemView.findViewById(R.id.diseasepercentageoftrees);
             diseasecontrolmeasures = (TextView) itemView.findViewById(R.id.diseasecontrolmeasures);
             diseaserecommendedchemical = (TextView) itemView.findViewById(R.id.diseaserecommendedchemical);
             diseaserecommendeddosage = (TextView) itemView.findViewById(R.id.diseaserecommendeddosage);
             diseaserecommendeduom = (TextView) itemView.findViewById(R.id.diseaserecommendeduom);
             diseaserecommenduomper = (TextView) itemView.findViewById(R.id.diseaserecommenduomper);
             diseasecomments = (TextView) itemView.findViewById(R.id.diseasecomments);



            card_view = (CardView) itemView.findViewById(R.id.card_view);
            // Initialize other views here
        }
    }
}
