package com.cis.palm360.cropmaintenance;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cis.palm360.R;
import com.cis.palm360.common.CommonConstants;
import com.cis.palm360.database.DataAccessHandler;
import com.cis.palm360.database.Queries;
import com.cis.palm360.dbmodels.Pest;
import com.cis.palm360.dbmodels.PestChemicalXref;

import java.util.ArrayList;
import java.util.List;


public class PestHistoryAdapter extends RecyclerView.Adapter<PestHistoryAdapter.PestViewHolder> {


    private Context context;
    private List<Pest> PestList = new ArrayList<>();
    private List<PestChemicalXref> PestchemicalList = new ArrayList<>();
    private DataAccessHandler dataAccessHandler; // Assuming you have access to this

    public PestHistoryAdapter(Context context, List<Pest>pestList, ArrayList<PestChemicalXref> mPestChemicalXrefModelArray) {
        this.context = context;
        this.PestList = pestList;
        this.PestchemicalList = mPestChemicalXrefModelArray;
        this.dataAccessHandler = new DataAccessHandler(context); // Initialize as needed
    }

    @NonNull
    @Override
    public PestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pest_history, parent, false);
        return new PestViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PestViewHolder holder, int position) {
        dataAccessHandler = new DataAccessHandler(context);
        Pest historypest = PestList.get(position);


        // Fetch pest name
        String pestname = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getlookupdata(historypest.getPestid()));

        holder.pestsseen.setText(pestname);
        Log.d("PestName", "Pest: " + pestname + ", PestID: " + historypest.getPestid());
        Log.d("PestName", "Pest: " + pestname + ", PestID: " + PestchemicalList.size());

        // Fetch last visit code
        String lastVisitCode = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getLatestCropMaintanaceHistoryCode(CommonConstants.PLOT_CODE));
        Log.d("LastVisitCode", "Last Visit Code: " + lastVisitCode);
        if (historypest.getPestid() != 223) {
            if (position < PestchemicalList.size()) {
                PestChemicalXref historypestchem = PestchemicalList.get(position);
                String chemicalused = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getlookupdata(historypestchem.getChemicalId()));
                Log.d("ChemicalName", chemicalused + "");

                holder.pestchemicalusedll.setVisibility(View.VISIBLE);
                holder.pestchemicalused.setText(chemicalused);
            } else {
                Log.e("PestHistoryAdapter", "Invalid position for PestchemicalList. Position: " + position + ", Size: " + PestchemicalList.size());
                holder.pestchemicalusedll.setVisibility(View.GONE);
            }
        } else {
            holder.pestchemicalusedll.setVisibility(View.GONE);
        }

//        if (historypest.getPestid() != 223) {
//            PestChemicalXref historypestchem =   PestchemicalList.get(position);
//            // Fetch pesticide used ID
//            String chemicalused = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getlookupdata(historypestchem.getChemicalId()));
//            Log.d("ChemicalName",  chemicalused+"");
//                // Fetch chemical used name
////                String chemicalused = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getlookupdata(pesticideusedid));
////                Log.d("ChemicalUsed", "Chemical Used: " + chemicalused);
//
//                holder.pestchemicalusedll.setVisibility(View.VISIBLE);
//                holder.pestchemicalused.setText(chemicalused);
////            } else {
////                Log.w("PesticideUsedId", "No pesticide used found for lastVisitCode: " + lastVisitCode);
////                holder.pestchemicalusedll.setVisibility(View.GONE);
////            }
//        } else {
//            holder.pestchemicalusedll.setVisibility(View.GONE);
//        }

        // Percentage of trees
        if (historypest.getPercTreesId() != 0) {
            String percentageoftrees = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getlookupdata(historypest.getPercTreesId()));
            holder.pestpercentageoftrees.setText(percentageoftrees);
            holder.pestpercentageoftreesll.setVisibility(View.VISIBLE);
        } else {
            holder.pestpercentageoftreesll.setVisibility(View.GONE);
        }

        // Recommended fertilizer provider
        if (historypest.getRecommendFertilizerProviderId() != null) {
            String fertilizerrec = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getlookupdata(historypest.getRecommendFertilizerProviderId()));
            holder.pestrecommendedchemical.setText(fertilizerrec);
            holder.pestrecommendedchemicall.setVisibility(View.VISIBLE);
        } else {
            holder.pestrecommendedchemicall.setVisibility(View.GONE);
        }

        // Recommended dosage
        if (historypest.getRecommendDosage() != 0.0) {
            holder.pestrecommendeddosage.setText(String.valueOf(historypest.getRecommendDosage()));
            holder.pestrecommendeddosagell.setVisibility(View.VISIBLE);
        } else {
            holder.pestrecommendeddosagell.setVisibility(View.GONE);
        }

        // Recommended UOM
        if (historypest.getRecommendUOMId() != null) {
            String recommendedUOM = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getUOMdata(historypest.getRecommendUOMId()));
            holder.pestrecommendeduom.setText(recommendedUOM);
            holder.pestrecommendeduomll.setVisibility(View.VISIBLE);
        } else {
            holder.pestrecommendeduomll.setVisibility(View.GONE);
        }

        // Recommended UOM per
        if (historypest.getRecommendedUOMId() != null) {
            String recommendedUOMper = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getUOMdata(historypest.getRecommendedUOMId()));
            holder.pestrecommenduomper.setText(recommendedUOMper);
            holder.pestrecommenduomperll.setVisibility(View.VISIBLE);
        } else {
            holder.pestrecommenduomperll.setVisibility(View.GONE);
        }

        // Comments
        if (TextUtils.isEmpty(historypest.getComments()) || "null".equalsIgnoreCase(historypest.getComments())) {
            holder.pestcomments.setVisibility(View.GONE);
        } else {
            holder.pestcomments.setText(historypest.getComments());
            holder.pestcommentsll.setVisibility(View.VISIBLE);
        }

        // Alternate background color
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
        return PestList.size();
    }

    static class PestViewHolder extends RecyclerView.ViewHolder {

        TextView deficiencyName, fertilizerUsed, percentageOfTrees;
        LinearLayout pestpercentageoftreesll,pestcontrolmeasuresll,pestrecommendedchemicall,pestrecommendeddosagell,pestrecommendeduomll,
                pestrecommenduomperll,pestcommentsll,pestchemicalusedll;
        TextView pestsseen,pestchemicalused,pestpercentageoftrees,pestcontrolmeasures,pestrecommendedchemical,
                pestrecommendeddosage,pestrecommendeduom,pestrecommenduomper,pestcomments;
        CardView card_view;
        public PestViewHolder(@NonNull View itemView) {
            super(itemView);

         pestchemicalusedll = (LinearLayout) itemView.findViewById(R.id.pestchemicalusedll);
         pestpercentageoftreesll = (LinearLayout) itemView.findViewById(R.id.pestpercentageoftreesll);
         pestcontrolmeasuresll = (LinearLayout) itemView.findViewById(R.id.pestcontrolmeasuresll);
         pestrecommendedchemicall = (LinearLayout) itemView.findViewById(R.id.pestrecommendedchemicall);
         pestrecommendeddosagell = (LinearLayout) itemView.findViewById(R.id.pestrecommendeddosagell);
         pestrecommendeduomll = (LinearLayout) itemView.findViewById(R.id.pestrecommendeduomll);
         pestrecommenduomperll = (LinearLayout) itemView.findViewById(R.id.pestrecommenduomperll);
         pestcommentsll = (LinearLayout) itemView.findViewById(R.id.pestcommentsll);

         pestsseen = (TextView) itemView.findViewById(R.id.pestseen);
         pestchemicalused = (TextView) itemView.findViewById(R.id.pestchemicalused);
         pestpercentageoftrees = (TextView) itemView.findViewById(R.id.pestpercentageoftrees);
         pestcontrolmeasures = (TextView) itemView.findViewById(R.id.pestcontrolmeasures);
         pestrecommendedchemical = (TextView) itemView.findViewById(R.id.pestrecommendedchemical);
         pestrecommendeddosage = (TextView) itemView.findViewById(R.id.pestrecommendeddosage);
         pestrecommendeduom = (TextView) itemView.findViewById(R.id.pestrecommendeduom);
         pestrecommenduomper = (TextView) itemView.findViewById(R.id.pestrecommenduomper);
         pestcomments = (TextView) itemView.findViewById(R.id.pestcomments);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
            // Initialize other views here
        }
    }
}
