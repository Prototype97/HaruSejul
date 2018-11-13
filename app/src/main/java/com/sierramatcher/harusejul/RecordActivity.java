package com.sierramatcher.harusejul;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class RecordActivity extends AppCompatActivity {

    ListView listView;
    SejulAdapter sejulAdapter;
    Button home;
    TextView homeText;
    SejulDiaryDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        listView = (ListView) findViewById(R.id.listview);
        sejulAdapter = new SejulAdapter(this);
        listView.setAdapter(sejulAdapter);

        //홈 버튼말고 글자 눌러도 버튼 누르는 효과나게 한 것
        home = (Button) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        homeText = (TextView) findViewById(R.id.homeText);
        homeText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                home.performClick();
                return false;
            }
        });
    }

    //데이터베이스 사용위해 만들어져있는 데이터베이스 있다면 열고, 없다면 생성
    public void openDatabase() {
        if(mDatabase != null) {
            mDatabase.close();
            mDatabase = null;
        }

        mDatabase = SejulDiaryDatabase.getInstance(this);
        mDatabase.open();
    }

    //액티비티가 켜질때마다 데이터베이스를 열고, 리스트뷰의 어댑터를 새로 갱신
    @Override
    protected void onStart() {
        super.onStart();
        openDatabase();
        refreshAdapter();
    }

    //리스트뷰의 어댑터를 갱신
    public void refreshAdapter() {
        String SQL = "SELECT * FROM SEJUL ORDER BY _ID DESC";

        if(mDatabase != null) {
            Cursor cursor = mDatabase.rawQuery(SQL);
            sejulAdapter.clear();

            while(cursor.moveToNext()) {
                String year = cursor.getString(1);
                String month = cursor.getString(2);
                String date = cursor.getString(3);
                String day = cursor.getString(4);
                String good = cursor.getString(5);
                String bad = cursor.getString(6);
                String will = cursor.getString(7);

                //만약 월이 달라지면 YearMonthView삽입
                if(cursor.moveToPrevious()) {
                    String prevMonth = cursor.getString(2);
                    if(prevMonth.equals(month)) {
                    } else {
                        sejulAdapter.addItem(year, month);
                    }
                } else {
                    sejulAdapter.addItem(year, month);
                }
                cursor.moveToNext();
                sejulAdapter.addItem(date, day, good, bad, will);
            }
            cursor.close();
            sejulAdapter.notifyDataSetChanged();
        }
    }
}
