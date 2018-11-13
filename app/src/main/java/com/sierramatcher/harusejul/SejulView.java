package com.sierramatcher.harusejul;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SejulView extends LinearLayout {

    TextView date;
    TextView day;
    TextView good;
    TextView bad;
    TextView will;

    //생성자 2개
    public SejulView(Context context) {
        super(context);
        init(context);
    }

    public SejulView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    //초기화
    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sejul_item,this, true);

        date = (TextView) findViewById(R.id.date);
        day = (TextView) findViewById(R.id.day);
        good = (TextView) findViewById(R.id.good);
        bad = (TextView) findViewById(R.id.bad);
        will = (TextView) findViewById(R.id.will);
    }

    public void setDate(String dateString) {
        date.setText(dateString);
    }

    public void setDay(String dayString) {
        day.setText(dayString);
    }

    public void setGood(String goodString) {
        good.setText("- "+goodString);
    }

    public void setBad(String badString) {
        bad.setText("- "+badString);
    }

    public void setWill(String willString) {
        will.setText("- "+willString);
    }
}
