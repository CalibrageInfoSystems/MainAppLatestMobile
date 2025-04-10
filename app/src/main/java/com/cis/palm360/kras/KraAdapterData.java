package com.cis.palm360.kras;

import android.support.v4.util.SparseArrayCompat;

import com.cis.palm360.R;
import com.cis.palm360.dbmodels.AnnualTagetsKRA;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by siva on 09/09/17.
 */

public class KraAdapterData<T> implements IViewBinderProvider, LayoutItemType {
    private final int itemLayoutId;
    private final boolean shouldSticky;
    private final T target;

    protected KraAdapterData(int itemLayoutId, boolean shouldSticky, T target) {
        this.itemLayoutId = itemLayoutId;
        this.shouldSticky = shouldSticky;
        this.target = target;
    }
    private IViewBinder viewBinder;

    public T getTarget() {
        return (target == null && this instanceof KraAdapterData) ? (T) this : target;
    }

    @Override
    public final IViewBinder provideViewBinder(StickyHeaderViewAdapter adapter, SparseArrayCompat<? extends IViewBinder> viewBinderPool, int position) {
        if (viewBinder == null) {
            viewBinder = viewBinderPool.get(getItemLayoutId(adapter));
        }
        return viewBinder;
    }

    public boolean shouldSticky() {
        return shouldSticky;
    }

    @Override
    public int getItemLayoutId(StickyHeaderViewAdapter adapter) {
        return itemLayoutId;
    }

    public static KraAdapterData<AnnualTagetsKRA> createKraItem(AnnualTagetsKRA conversation) {
        return new KraAdapterData<>(R.layout.single_item_view, false, conversation);
    }

    public static List<KraAdapterData<AnnualTagetsKRA>> createKrasDisplayList(List<AnnualTagetsKRA> krasDataToDisplayList) {
        List<KraAdapterData<AnnualTagetsKRA>> list = new ArrayList<>();
        for (AnnualTagetsKRA krasDataToDisplay : krasDataToDisplayList) {
            list.add(new KraAdapterData<>(R.layout.single_item_view, false, krasDataToDisplay));
        }
        return list;
    }
}
