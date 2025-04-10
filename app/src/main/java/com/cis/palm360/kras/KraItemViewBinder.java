package com.cis.palm360.kras;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cis.palm360.R;
import com.cis.palm360.dbmodels.MonthlyTagetsKRA;

/**
 * Created by siva on 09/09/17.
 */

public class KraItemViewBinder extends ViewBinder<KraAdapterData<MonthlyTagetsKRA>, KraItemViewBinder.ViewHolder> {
    private static final String LOG_TAG = KraItemViewBinder.class.getName();
    private Handler welcomeHandler = null;
    String[] monthArray = {"January","February","March","April","May","June","July","August","September","October","November","December"};
    public KraItemViewBinder() {
    }

    @Override
    public KraItemViewBinder.ViewHolder provideViewHolder(View itemView) {
        return new KraItemViewBinder.ViewHolder(itemView);
    }

    @Override
    public void bindView(StickyHeaderViewAdapter adapter,
                         final KraItemViewBinder.ViewHolder holder, final int position,
                         final KraAdapterData<MonthlyTagetsKRA> kraDataBean, final AppCompatActivity activity) {
        final MonthlyTagetsKRA krasDataToDisplay = kraDataBean.getTarget();

        holder.kraName.setText(krasDataToDisplay.getKraName());
        holder.monthNumber.setText(""+(krasDataToDisplay.getMonthNumber() > 0 ? monthArray[krasDataToDisplay.getMonthNumber()-1] :  " "));
        holder.monthlyAchievedTargetTxt.setText(krasDataToDisplay.getMonthlyTarget() + " " + krasDataToDisplay.getUom());
        holder.monthlyTargetTxt.setText(krasDataToDisplay.getMonthlyTarget() + " " + krasDataToDisplay.getUom());
        holder.annualTargetTxt.setText(""+krasDataToDisplay.getAnnualTarget()+ " " + krasDataToDisplay.getUom());
        holder.annualAchievedTargetTxt.setText(""+krasDataToDisplay.getAchievedTarget()+ " " + krasDataToDisplay.getUom());

//        holder.monthLL.setVisibility((krasDataToDisplay.getMonthlyTarget() > 0) ? View.VISIBLE : View.GONE);
//        holder.yearLL.setVisibility((krasDataToDisplay.getAnnualTarget() > 0) ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemLayoutId(StickyHeaderViewAdapter adapter) {
        return R.layout.single_item_view;
    }

    static class ViewHolder extends ViewBinder.ViewHolder {

        public TextView kraName, monthlyAchievedTargetTxt, monthlyTargetTxt, annualTargetTxt, monthNumber, annualAchievedTargetTxt;
        private LinearLayout monthLL, yearLL;
        public ViewHolder(View rootView) {
            super(rootView);
            this.kraName = (TextView) rootView.findViewById(R.id.craName);
            this.monthlyAchievedTargetTxt = (TextView) rootView.findViewById(R.id.monthlyAchievedTarget);
            this.monthlyTargetTxt = (TextView) rootView.findViewById(R.id.monthlyTarget);
            this.annualTargetTxt = (TextView) rootView.findViewById(R.id.annualTarget);
            this.annualAchievedTargetTxt = (TextView) rootView.findViewById(R.id.annualAchievedTarget);
            this.monthNumber = (TextView) rootView.findViewById(R.id.monthNumber);
        //    this.monthLL = (LinearLayout) rootView.findViewById(R.id.monthLL);
            this.yearLL = (LinearLayout) rootView.findViewById(R.id.yearLL);
        }

    }
}