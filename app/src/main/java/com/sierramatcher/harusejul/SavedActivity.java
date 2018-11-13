package com.sierramatcher.harusejul;

import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SavedActivity extends AppCompatActivity {

    CalendarView calendar;
    String SQL;
    SejulDiaryDatabase mDatabase;
    TextView good;
    TextView bad;
    TextView will;
    TextView initialMessage;
    LinearLayout sejulDiaryPannel;
    TextView dateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        initialMessage = (TextView) findViewById(R.id.initialMessage);
        sejulDiaryPannel =(LinearLayout) findViewById(R.id.sejulDiaryPannel);
        good = (TextView) findViewById(R.id.good);
        bad = (TextView) findViewById(R.id.bad);
        will = (TextView) findViewById(R.id.will);
        calendar = (CalendarView) findViewById(R.id.calendar);

        //처음 띄워놨을 때 오늘 날짜 보여주는 역할
        dateText = (TextView) findViewById(R.id.dateText);
        long now = System.currentTimeMillis();
        Date today = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy. MM. dd.");
        String timeText = simpleDateFormat.format(today);
        dateText.setText(timeText);

        //세줄일기 딱 다 쓰자마자 넘어왔을 때 세줄일기가 나타나지 않기 때문에(날짜 선택이 자동으로 되서) intent로 부가데이터 받아서 따로 보여주는 메소드
        Intent intent = getIntent();
        processIntent(intent);

        //다른 날짜 고르면, 타이틀 바의 날짜 텍스트 바뀌고, 세줄일기가 있으면 있는거 보여주고, 없으면 없다고 나타내기
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                dateText.setText(String.valueOf(year)+". "+String.valueOf(month+1)+". "+String.valueOf(dayOfMonth)+".");
                SQL = "SELECT GOOD, BAD, WILL FROM SEJUL WHERE _ID="+String.valueOf(year*10000+(month+1)*100+dayOfMonth);
                if(mDatabase != null) {
                    Cursor cursor = mDatabase.rawQuery(SQL);
                    if(cursor.moveToNext()) {
                        good.setText(cursor.getString(0));
                        bad.setText(cursor.getString(1));
                        will.setText(cursor.getString(2));
                        initialMessage.setVisibility(View.GONE);
                        sejulDiaryPannel.setVisibility(View.VISIBLE);
                    } else {
                        initialMessage.setVisibility(View.VISIBLE);
                        sejulDiaryPannel.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    //액티비티가 이미 켜져있을 경우에도 인텐트 받기 위함
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processIntent(intent);
    }

    //인텐트 받아서 첫 화면에만 잘 보이게끔 처리
    public void processIntent(Intent intent) {
        good.setText(intent.getStringExtra("firstLine"));
        bad.setText(intent.getStringExtra("secondLine"));
        will.setText(intent.getStringExtra("thirdLine"));
    }

    //데이터베이스 사용위해 오픈
    public void openDatabase() {
        if(mDatabase != null) {
            mDatabase.close();
            mDatabase = null;
        }

        mDatabase = SejulDiaryDatabase.getInstance(this);
        mDatabase.open();
    }

    //액티비티 열때마다 데이터베이스 오픈
    @Override
    protected void onStart() {
        super.onStart();
        openDatabase();
    }
}
