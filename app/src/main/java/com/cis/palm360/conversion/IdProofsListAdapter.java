package com.cis.palm360.conversion;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.cis.palm360.R;
import com.cis.palm360.database.DataAccessHandler;
import com.cis.palm360.dbmodels.IdentityProof;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Calibrage10 on 9/26/2016.
 */

//Displaying the Id Proofs entered
public class IdProofsListAdapter extends RecyclerView.Adapter<IdProofsListAdapter.MyHolder> {
    private Context mContext;
    private List<IdentityProof> idProofsPair;

    private idProofsClickListener idProofsClickListener;
    private LinkedHashMap<String, String> idProofsData;
    private List<IdentityProof> identityProofsList;
    private DataAccessHandler dataAccessHandler;
    public IdProofsListAdapter(Context mContext, List<IdentityProof> idProofsPair, LinkedHashMap<String, String> idProofsData) {
        this.mContext = mContext;
        this.idProofsPair = idProofsPair;
        this.idProofsData = idProofsData;
        dataAccessHandler = new DataAccessHandler(mContext);
    }

    @Override
    public IdProofsListAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View bookingView = inflater.inflate(R.layout.adapter_idproof, null);
        MyHolder myHolder = new MyHolder(bookingView);
        return myHolder;
    }


    @Override
    public void onBindViewHolder(IdProofsListAdapter.MyHolder holder, final int position) {
        holder.idProofName.setText(idProofsData.get(String.valueOf(idProofsPair.get(position).getIdprooftypeid())));
        holder.idProofNo.setText(idProofsPair.get(position).getIdproofnumber());

        holder.editView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idProofsClickListener.onEditClicked(position);
            }
        });

        holder.deleteView.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (idProofsClickListener != null && currentPosition != RecyclerView.NO_POSITION) {
                idProofsClickListener.onDeleteClicked(currentPosition);
            }
        });
//        holder.deleteView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                idProofsClickListener.onDeleteClicked(position);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return idProofsPair.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        TextView idProofName, idProofNo;
        ImageView editView, deleteView;

        public MyHolder(View itemView) {
            super(itemView);
            idProofName = (TextView) itemView.findViewById(R.id.idproofName);
            idProofNo = (TextView) itemView.findViewById(R.id.idproofNo);
            editView = (ImageView) itemView.findViewById(R.id.editIcon);
            deleteView = (ImageView) itemView.findViewById(R.id.trashIcon);
        }
    }

    public void setIdProofsClickListener(idProofsClickListener idProofsClickListener) {
        this.idProofsClickListener = idProofsClickListener;
    }
    public interface idProofsClickListener {
        void onEditClicked(int position);
        void onDeleteClicked(int position);
    }

    public void updateData(List<IdentityProof> idProofsPair, LinkedHashMap<String, String> idProofsData) {
        this.idProofsPair = idProofsPair;
        this.idProofsData = idProofsData;
        notifyDataSetChanged();
    }

}
