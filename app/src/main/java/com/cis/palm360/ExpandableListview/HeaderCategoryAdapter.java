package com.cis.palm360.ExpandableListview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cis.palm360.R;
import com.cis.palm360.dbmodels.MonthlyTagetsKRA;


import java.util.List;

/**
 * Created by Lenovo on 11/16/2017.
 */

public class HeaderCategoryAdapter extends ExpandableRecyclerAdapter<HeaderViewHolder, SubViewHolder> {

    private LayoutInflater mInflator;

    public HeaderCategoryAdapter(Context context, List<? extends ParentListItem> displayList) {
        super(displayList);
        mInflator = LayoutInflater.from(context);
    }

    @Override
    public HeaderViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View kraAnnualCategoryView = mInflator.inflate(R.layout.kra_header, parentViewGroup, false);
        return new HeaderViewHolder(kraAnnualCategoryView);
    }

    @Override
    public SubViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View kraMonthlyCategoryView = mInflator.inflate(R.layout.single_item_view, childViewGroup, false);
        return new SubViewHolder(kraMonthlyCategoryView);
    }

    @Override
    public void onBindParentViewHolder(HeaderViewHolder kraAnnualCategoryViewHolder, int position, ParentListItem parentListItem) {
        HeaderCategory kraAnnualCategory = (HeaderCategory) parentListItem;
        kraAnnualCategoryViewHolder.bind(kraAnnualCategory.getName());
    }
    @Override
    public void onBindChildViewHolder(SubViewHolder kraMonthlyViewHolder, int position, Object childListItem) {
        if (childListItem instanceof MonthlyTagetsKRA) {
            MonthlyTagetsKRA kraMonthly = (MonthlyTagetsKRA) childListItem;
            kraMonthlyViewHolder.bind(kraMonthly);
        } else {
            Log.e("HeaderCategoryAdapter", "Invalid child item type: " + childListItem.getClass().getSimpleName());
        }
    }


}

