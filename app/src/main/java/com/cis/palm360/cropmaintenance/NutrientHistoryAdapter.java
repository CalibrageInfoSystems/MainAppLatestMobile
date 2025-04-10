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
import com.cis.palm360.dbmodels.Nutrient;

import java.util.ArrayList;
import java.util.List;


public class NutrientHistoryAdapter extends RecyclerView.Adapter<NutrientHistoryAdapter.NutrientViewHolder> {


    private Context context;
    private List<Nutrient> nutrientList = new ArrayList<>();
    private DataAccessHandler dataAccessHandler; // Assuming you have access to this

    public NutrientHistoryAdapter(Context context, List<Nutrient> nutrientList) {
        this.context = context;
        this.nutrientList = nutrientList;
        this.dataAccessHandler = new DataAccessHandler(context); // Initialize as needed
    }

    @NonNull
    @Override
    public NutrientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_nutrient, parent, false);
        return new NutrientViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull NutrientViewHolder holder, int position) {
        dataAccessHandler = new DataAccessHandler(context);
        Nutrient historynutrient = nutrientList.get(position);
        String nutrient = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getlookupdata(nutrientList.get(position).getNutrientid()));
//        holder.deficiencyName.setText(nutrient.());
//        holder.fertilizerUsed.setText(nutrient.getFertilizerUsed());
//        holder.percentageOfTrees.setText(String.valueOf(nutrient.getPercentageOfTrees()));
        holder.nutrientdeficencyname.setText(nutrient);

        if (historynutrient.getChemicalid() != null){
            holder.nutrientfertilizerusedll.setVisibility(View.VISIBLE);
            String fertilizer = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getlookupdata(historynutrient.getChemicalid()));
            holder.nutrientfertilizerused.setText(fertilizer);
        }else{
            holder.nutrientfertilizerusedll.setVisibility(View.GONE);
        }
        if (historynutrient.getPercTreesId() != 0){
            holder.nutrientpercentageoftressll.setVisibility(View.VISIBLE);
            String percentageoftress = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getlookupdata(historynutrient.getPercTreesId()));
            holder.nutrientpercentageoftress.setText(percentageoftress);
        }else{
            holder.nutrientpercentageoftressll.setVisibility(View.GONE);
        }

        if (historynutrient.getRecommendFertilizerProviderId() != null){
            holder.nutrientfertilizerrecommendedll.setVisibility(View.VISIBLE);
            String fertilizerrec = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getlookupdata(historynutrient.getRecommendFertilizerProviderId()));
            holder.nutrientfertilizerrecommended.setText(fertilizerrec);
        }else{
            holder.nutrientfertilizerrecommendedll.setVisibility(View.GONE);
        }

        if (historynutrient.getRecommendDosage() != 0.0){
            holder.nutrientrecommendeddosagell.setVisibility(View.VISIBLE);
            holder.nutrientrecommendeddosage.setText(historynutrient.getRecommendDosage() + "");
        }else{
            holder.nutrientrecommendeddosagell.setVisibility(View.GONE);
        }

        if (historynutrient.getRecommendUOMId() != null){
            holder.nutrientrecommendeduomll.setVisibility(View.VISIBLE);
            String recommendedUOM = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getUOMdata(historynutrient.getRecommendUOMId()));
            holder.nutrientrecommendeduom.setText(recommendedUOM);
        }else{
            holder.nutrientrecommendeduomll.setVisibility(View.GONE);
        }

        if (historynutrient.getRecommendedUOMId() != null){
            holder.nutrientrecommendeduomperll.setVisibility(View.VISIBLE);
            String recommendedUOMper = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getUOMdata(historynutrient.getRecommendedUOMId()));
            holder.nutrientrecommendeduomper.setText(recommendedUOMper);
        }else{
            holder.nutrientrecommendeduomperll.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(historynutrient.getComments()) || historynutrient.getComments().equalsIgnoreCase("")  || historynutrient.getComments().equalsIgnoreCase(null) ||
                historynutrient.getComments().equalsIgnoreCase("null")){
            holder.nutrientcommentsll.setVisibility(View.GONE);
        }else{
            holder.nutrientcommentsll.setVisibility(View.VISIBLE);
            holder.nutrientcomments.setText(historynutrient.getComments() + "");
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
        return nutrientList.size();
    }

    static class NutrientViewHolder extends RecyclerView.ViewHolder {

        TextView deficiencyName, fertilizerUsed, percentageOfTrees;
        LinearLayout nutrientfertilizerusedll,nutrientpercentageoftressll,nutrientfertilizerrecommendedll,nutrientrecommendeddosagell,nutrientrecommendeduomll,
                nutrientrecommendeduomperll,nutrientcommentsll;
        TextView nutrientdeficencyname,nutrientfertilizerused,nutrientpercentageoftress,nutrientfertilizerrecommended,nutrientrecommendeddosage,
                nutrientrecommendeduom,nutrientrecommendeduomper,nutrientcomments;
        CardView card_view;
        public NutrientViewHolder(@NonNull View itemView) {
            super(itemView);
            deficiencyName = itemView.findViewById(R.id.nutrientdeficencyname);
            fertilizerUsed = itemView.findViewById(R.id.nutrientfertilizerused);
            percentageOfTrees = itemView.findViewById(R.id.nutrientpercentageoftress);
            nutrientfertilizerusedll = (LinearLayout) itemView.findViewById(R.id.nutrientfertilizerusedll);
            nutrientpercentageoftressll = (LinearLayout) itemView.findViewById(R.id.nutrientpercentageoftressll);
            nutrientfertilizerrecommendedll = (LinearLayout) itemView.findViewById(R.id.nutrientfertilizerrecommendedll);
            nutrientrecommendeddosagell = (LinearLayout) itemView.findViewById(R.id.nutrientrecommendeddosagell);
            nutrientrecommendeduomll = (LinearLayout) itemView.findViewById(R.id.nutrientrecommendeduomll);
            nutrientrecommendeduomperll = (LinearLayout) itemView.findViewById(R.id.nutrientrecommendeduomperll);
            nutrientcommentsll = (LinearLayout) itemView.findViewById(R.id.nutrientcommentsll);

            nutrientdeficencyname = (TextView) itemView.findViewById(R.id.nutrientdeficencyname);
            nutrientfertilizerused = (TextView) itemView.findViewById(R.id.nutrientfertilizerused);
            nutrientpercentageoftress = (TextView) itemView.findViewById(R.id.nutrientpercentageoftress);
            nutrientfertilizerrecommended = (TextView) itemView.findViewById(R.id.nutrientfertilizerrecommended);
            nutrientrecommendeddosage = (TextView) itemView.findViewById(R.id.nutrientrecommendeddosage);
            nutrientrecommendeduom = (TextView) itemView.findViewById(R.id.nutrientrecommendeduom);
            nutrientrecommendeduomper = (TextView) itemView.findViewById(R.id.nutrientrecommendeduomper);
            nutrientcomments = (TextView) itemView.findViewById(R.id.nutrientcomments);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
            // Initialize other views here
        }
    }
}
