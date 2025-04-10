package com.cis.palm360.kras;

import com.cis.palm360.R;
import com.cis.palm360.dbmodels.AnnualTagetsKRA;

import java.util.List;

/**
 * Created by siva on 09/09/17.
 */

public class KraItemHeader  extends KraAdapterData {
    List<AnnualTagetsKRA> krasDataToDisplayList;

    public List<AnnualTagetsKRA> getKraData() {
        return krasDataToDisplayList;
    }

    public KraItemHeader(List<AnnualTagetsKRA> krasDataToDisplayList) {
        super(R.layout.kra_header, true, null);
        this.krasDataToDisplayList = krasDataToDisplayList;
    }
}
