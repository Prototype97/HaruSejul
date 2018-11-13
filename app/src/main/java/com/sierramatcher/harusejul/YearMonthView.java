package com.sierramatcher.harusejul;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class YearMonthView extends LinearLayout{

    TextView yearAndMonth;

    //생성자 2개
    public YearMonthView(Context context) {
        super(context);
        init(context);
    }

    public YearMonthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    //초기화
    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.yearmonth_item,this, true);

        yearAndMonth = (TextView) findViewById(R.id.yearAndMonth);
    }

    public void setYearAndMonth(String year, String month) {
        yearAndMonth.setText(year+". "+month+".");
    }
}
