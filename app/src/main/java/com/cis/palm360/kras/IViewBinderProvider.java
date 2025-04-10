package com.cis.palm360.kras;

import android.support.v4.util.SparseArrayCompat;

/**
 * Created by siva on 09/09/17.
 */

public interface IViewBinderProvider {
    IViewBinder provideViewBinder(StickyHeaderViewAdapter adapter, SparseArrayCompat<? extends IViewBinder> viewBinderPool, int position);
}
