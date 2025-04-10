package com.cis.palm360.cropmaintenance;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.cis.palm360.R;
import com.cis.palm360.dbmodels.RecoveryFarmerModel;

import java.util.ArrayList;
import java.util.List;

public class CustomFarmerAdapter extends ArrayAdapter {

    private List<RecoveryFarmerModel> dataList;
    private Context mContext;
    private int itemLayout;

    private ListFilter listFilter = new ListFilter();
    private List<RecoveryFarmerModel> dataListAllItems;



    public CustomFarmerAdapter(Context context, int resource, List<RecoveryFarmerModel> storeDataLst) {
        super(context, resource, storeDataLst);
        dataList = storeDataLst;
        mContext = context;
        itemLayout = resource;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public RecoveryFarmerModel getItem(int position) {
//        Log.d("CustomListAdapter",
//                dataList.get(position).getCode());
        return dataList.get(position);
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(itemLayout, parent, false);
        }
        TextView strCode = (TextView) view.findViewById(R.id.fcode);
        TextView strName = (TextView) view.findViewById(R.id.ffirstname);
        TextView strMobile = (TextView) view.findViewById(R.id.fcontactnumber);

        String fullname = "", middleName = "";


        if (!TextUtils.isEmpty(getItem(position).getMiddleName()) && !
                getItem(position).getMiddleName().equalsIgnoreCase("null")) {
            middleName = getItem(position).getMiddleName();
        }

        fullname  = getItem(position).getFirstName().trim() + " " + middleName + " " + getItem(position).getLastName().trim();

        strCode.setText(" : " +getItem(position).getCode());
        strName.setText(" : " + fullname + "");
        strMobile.setText(" : " +getItem(position).getContactNumber());
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    public class ListFilter extends Filter {
        private Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (dataListAllItems == null) {
                synchronized (lock) {
                    dataListAllItems = new ArrayList<RecoveryFarmerModel>(dataList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = dataListAllItems;
                    results.count = dataListAllItems.size();
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                ArrayList<RecoveryFarmerModel> matchValues = new ArrayList<RecoveryFarmerModel>();

                for (RecoveryFarmerModel dataItem : dataListAllItems) {
                    if (dataItem.getCode().toLowerCase().contains(searchStrLowerCase)   ) {
                        matchValues.add(dataItem);

                    } else if(dataItem.getFirstName().toLowerCase().contains(searchStrLowerCase)){
                        matchValues.add(dataItem);
                    }
                    else if(dataItem.getLastName().toLowerCase().contains(searchStrLowerCase)){
                        matchValues.add(dataItem);
                    }
                    else if(dataItem.getContactNumber().toLowerCase().contains(searchStrLowerCase)){
                        matchValues.add(dataItem);
                    }
                }

                results.values = matchValues;
                results.count = matchValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                dataList = (ArrayList<RecoveryFarmerModel>)results.values;
            } else {
                dataList = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}
