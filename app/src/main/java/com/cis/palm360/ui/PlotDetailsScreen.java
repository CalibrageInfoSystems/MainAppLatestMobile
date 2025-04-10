package com.cis.palm360.ui;

import com.cis.palm360.R;
import com.cis.palm360.areaextension.PlotDetailsFragment;

//Initializing the Plot details screen
public class PlotDetailsScreen extends OilPalmBaseActivity {

    @Override
    public void Initialize() {
        setTile("Plot Details");
        replaceFragment(new PlotDetailsFragment());
    }

}
