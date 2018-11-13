package com.sierramatcher.harusejul;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class SejulAdapter extends BaseAdapter {
    public static final int ITEM_VIEW_TYPE_SEJUL = 0;
    public static final int ITEM_VIEW_TYPE_YEARMONTH = 1;
    public static final int ITEM_VIEW_TYPE_MAX = 2;

    ArrayList<SejulItem> items = new ArrayList<SejulItem>();
    Context mContext;

    public SejulAdapter(Context context) {
        super();
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return ITEM_VIEW_TYPE_MAX;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    public void removeItem(SejulItem item) {
        items.remove(item);
    }

    public void clear() {
        items.clear();
    }

    public void addItem(String date, String day, String good, String bad, String will) {
        SejulItem item = new SejulItem(date, day, good, bad, will);
        item.setType(ITEM_VIEW_TYPE_SEJUL);
        item.setDate(date);
        item.setDay(day);
        item.setGood(good);
        item.setBad(bad);
        item.setWill(will);
        items.add(item);
    }

    public void addItem(String year, String month) {
        SejulItem item = new SejulItem(year, month);
        item.setType(ITEM_VIEW_TYPE_YEARMONTH);
        item.setYear(year);
        item.setMonth(month);
        items.add(item);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //해당 item을 리스트뷰로 보여줄때
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);

        SejulView view_0 = null;
        YearMonthView view_1 = null;

        switch (viewType) {
            case ITEM_VIEW_TYPE_SEJUL:
                if(convertView == null) {
                    view_0 = new SejulView(mContext);
                } else {
                    view_0 = (SejulView) convertView;
                }
                SejulItem item = items.get(position);

                view_0.setDate(item.getDate());
                view_0.setDay(item.getDay());
                view_0.setGood(item.getGood());
                view_0.setBad(item.getBad());
                view_0.setWill(item.getWill());

                return view_0;

            case ITEM_VIEW_TYPE_YEARMONTH:
                if(convertView == null) {
                    view_1 = new YearMonthView(mContext);
                } else {
                    view_1 = (YearMonthView) convertView;
                }
                item = items.get(position);

                view_1.setYearAndMonth(item.getYear(),item.getMonth());

                return view_1;
            default:
                return convertView;
        }
    }
}
