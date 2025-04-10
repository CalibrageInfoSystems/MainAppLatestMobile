package com.cis.palm360.ExpandableListview;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cis.palm360.R;
import com.cis.palm360.dbmodels.MonthlyTagetsKRA;

/**
 * Created by Lenovo on 11/16/2017.
 */

public class SubViewHolder extends ChildViewHolder {

    private TextView mMoviesTextView;
    public TextView kraName, monthlyAchievedTargetTxt, monthlyTargetTxt, annualTargetTxt, monthNumber, annualAchievedTargetTxt;
    private LinearLayout monthLL, yearLL;
    String[] monthArray = {"January","February","March","April","May","June","July","August","September","October","November","December"};


    public SubViewHolder(View rootView) {
        super(rootView);
        this.kraName = (TextView) rootView.findViewById(R.id.craName);
        this.monthlyAchievedTargetTxt = (TextView) rootView.findViewById(R.id.monthlyAchievedTarget);
        this.monthlyTargetTxt = (TextView) rootView.findViewById(R.id.monthlyTarget);
        this.annualTargetTxt = (TextView) rootView.findViewById(R.id.annualTarget);
        this.annualAchievedTargetTxt = (TextView) rootView.findViewById(R.id.annualAchievedTarget);
        this.monthNumber = (TextView) rootView.findViewById(R.id.monthNumber);
       // this.monthLL = (LinearLayout) rootView.findViewById(R.id.monthLL);
        this.yearLL = (LinearLayout) rootView.findViewById(R.id.yearLL);
    }

    public void bind(MonthlyTagetsKRA kraDataBean  ) {
        final MonthlyTagetsKRA krasDataToDisplay = kraDataBean;
        kraName.setText(krasDataToDisplay.getKraName());
        monthNumber.setText(""+(krasDataToDisplay.getMonthNumber() > 0 ? monthArray[krasDataToDisplay.getMonthNumber()-1] :  " "));

        monthlyAchievedTargetTxt.setText(krasDataToDisplay.getAchievedTarget() + " " + krasDataToDisplay.getUom());
        monthlyTargetTxt.setText(krasDataToDisplay.getMonthlyTarget() + " " + krasDataToDisplay.getUom());
        annualTargetTxt.setText(""+krasDataToDisplay.getAnnualTarget()+ " " + krasDataToDisplay.getUom());
        annualAchievedTargetTxt.setText(""+krasDataToDisplay.getAnnualTarget()+ " " + krasDataToDisplay.getUom());

    }
}
