package com.cis.palm360.ExpandableListview;

import com.cis.palm360.dbmodels.MonthlyTagetsKRA;

import java.util.List;

/**
 * Created by Lenovo on 11/16/2017.
 */

public class HeaderCategory implements ParentListItem {
    private String name;
    private List<MonthlyTagetsKRA> childList;

    public HeaderCategory(String name, List<MonthlyTagetsKRA> childList) {
        this.name = name;
        this.childList = childList;
    }

    public String getName() {
        return name;
    }

    @Override
    public List<?> getChildItemList() {
        return childList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}

