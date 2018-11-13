package com.sierramatcher.harusejul;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Button penButton;
    Button menuButton;
    RelativeLayout page;
    Animation anim;
    Animation anim_reverse;
    Button menuButton2;
    RelativeLayout page_transparentButton;
    Button seekButton;
    Button pushTimeSelectorButton;
    int hour;
    int minute;
    String ampm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //펜버튼 누르면 세줄일기 작성하는 액티비티로 이동
        penButton = (Button) findViewById(R.id.penButton);
        penButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WritingActivity.class);
                startActivity(intent);
            }
        });

        //메뉴버튼 누르면 페이지 슬라이딩 애니메이션 효과
        menuButton = (Button) findViewById(R.id.menuButton);
        menuButton2 = (Button) findViewById(R.id.menuButton2);
        page_transparentButton = (RelativeLayout) findViewById(R.id.page_transparentButton);
        page = (RelativeLayout) findViewById(R.id.page);
        anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.page_translation);
        anim_reverse = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.page_translation_reverse);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page.setVisibility(View.VISIBLE);
                page.startAnimation(anim);
            }
        });
        menuButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page.startAnimation(anim_reverse);
                page.setVisibility(View.GONE);
            }
        });
        page_transparentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page.startAnimation(anim_reverse);
                page.setVisibility(View.GONE);
            }
        });

        //seek버튼 누르면 기록액티비티로 이동
        seekButton = (Button) findViewById(R.id.seekButton);
        seekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page.startAnimation(anim_reverse);
                page.setVisibility(View.GONE);
                Intent intent = new Intent(getApplicationContext(), RecordActivity.class);
                startActivity(intent);
            }
        });

        //앱꺼져도 알람시간 값 저장을 위한 준비
        SharedPreferences pref = getSharedPreferences( "pref" , MODE_PRIVATE);
        final SharedPreferences.Editor ed = pref.edit();

        //푸쉬알람 대화상자로 시간 설정
        pushTimeSelectorButton = (Button) findViewById(R.id.pushTimeSelectorButton);
        hour = 22;
        minute = 0;
        final AlarmHATT alarmHATT = new AlarmHATT(this);
        pushTimeSelectorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(MainActivity.this,R.style.CustomTimePickerDialogStyle, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int h, int m) {
                        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            hour = timePicker.getHour();
                            minute = timePicker.getMinute();
                            ed.putInt( "pushAlarmHour" , hour );
                            ed.putInt( "pushAlarmMinute" , minute );
                            ed.commit();
                            alarmHATT.Alarm();
                        } else {
                            hour = timePicker.getCurrentHour();
                            minute = timePicker.getCurrentMinute();
                            ed.putInt( "pushAlarmHour" , hour );
                            ed.putInt( "pushAlarmMinute" , minute );
                            ed.commit();
                            alarmHATT.Alarm();
                        }
                        if(hour >= 13) {
                            hour = hour-12;
                            ampm="pm";
                        } else {
                            ampm="am";
                        }
                        if(hour<10) {
                            if(minute<10) {
                                pushTimeSelectorButton.setText(ampm +" 0"+String.valueOf(hour)+":0"+String.valueOf(minute));
                            }
                            else {
                                pushTimeSelectorButton.setText(ampm +" 0"+String.valueOf(hour)+":"+String.valueOf(minute));
                            }
                        } else {
                            if(minute<10) {
                                pushTimeSelectorButton.setText(ampm +" "+String.valueOf(hour)+":0"+String.valueOf(minute));
                            } else {
                                pushTimeSelectorButton.setText(ampm +" "+String.valueOf(hour)+":"+String.valueOf(minute));
                            }
                        }
                    }
                }, hour, minute, false).show();
            }
        });

        //앱다시 켤때 알람시간 값 복원
        int savedHour = pref.getInt("pushAlarmHour", 10);
        int savedMinute = pref.getInt("pushAlarmMinute",0);
        if(savedHour >= 13) {
            savedHour = savedHour-12;
            ampm="pm";
        } else {
            ampm="am";
        }
        if(savedHour<10) {
            if(savedMinute<10) {
                pushTimeSelectorButton.setText(ampm +" 0"+String.valueOf(savedHour)+":0"+String.valueOf(savedMinute));
            }
            else {
                pushTimeSelectorButton.setText(ampm +" 0"+String.valueOf(savedHour)+":"+String.valueOf(savedMinute));
            }
        } else {
            if(savedMinute<10) {
                pushTimeSelectorButton.setText(ampm +" "+String.valueOf(savedHour)+":0"+String.valueOf(savedMinute));
            } else {
                pushTimeSelectorButton.setText(ampm +" "+String.valueOf(savedHour)+":"+String.valueOf(savedMinute));
            }
        }
    }

    //푸쉬알람 만드는 클래스. 특정 시각에 발동하면 PushAlarmBroadcastReceiver가 받아서 알람을 준다.
    public class AlarmHATT {
        private Context context;
        public AlarmHATT(Context context) {
            this.context=context;
        }
        public void Alarm() {
            AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(MainActivity.this, PushAlarmBroadcastReceiver.class);
            PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
            Calendar calendar = Calendar.getInstance();
            if(hour*100+minute<calendar.getTime().getHours()*100+calendar.getTime().getMinutes()) {
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE+1), hour, minute, 0);
            } else {
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), hour, minute, 0);
            }
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        }
    }
}
