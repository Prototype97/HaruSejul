package com.sierramatcher.harusejul;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WritingActivity extends Activity {

    FirstFragment firstFragment;
    SecondFragment secondFragment;
    ThirdFragment thirdFragment;
    Button prev;
    TextView prevText;
    Button next;
    TextView nextText;
    TextView dateText;
    String good;
    String bad;
    String will;
    int count;
    SejulDiaryDatabase mDatabase;
    String SQL;
    String year;
    String month;
    String date;
    String day;
    int _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);

        //프래그먼트 3개를 사용, 1줄씩 1프래그먼트 사용
        firstFragment = new FirstFragment();
        prev = (Button) findViewById(R.id.prev);
        prevText = (TextView) findViewById(R.id.prevText);
        getFragmentManager().beginTransaction().replace(R.id.pannel, firstFragment).commit();
        prev.setBackgroundResource(R.drawable.left_arrow_white);
        prevText.setText("Home");

        //뭐라도 작성하고 있었으면 홈으로 나갈때 한번 물어본다.
        next = (Button) findViewById(R.id.next);
        count = 0;
        secondFragment = new SecondFragment();
        thirdFragment = new ThirdFragment();
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
                switch (count) {
                    case -1:
                        if(firstFragment.firstLine.getText().toString().equals("")) {
                            count = 0;
                            finish();
                        } else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WritingActivity.this,R.style.CustomAlertDialogStyle);
                            alertDialogBuilder.setTitle("");
                            alertDialogBuilder
                                    .setMessage("작성중이던 일기가 저장되지 않습니다. 그래도 나가시겠어요?")
                                    .setCancelable(false)
                                    .setPositiveButton("아니요",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog, int id) {
                                                    count = 0;
                                                    dialog.cancel();
                                                }
                                            })
                                    .setNegativeButton("네, 나갈게요",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog, int id) {
                                                    count = 0;
                                                    finish();
                                                }
                                            });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                        break;
                    case 0:
                        getFragmentManager().beginTransaction().replace(R.id.pannel, firstFragment).commit();
                        prev.setBackgroundResource(R.drawable.left_arrow_white);
                        prevText.setText("Home");
                        break;
                    case 1:
                        getFragmentManager().beginTransaction().replace(R.id.pannel, secondFragment).commit();
                        nextText.setText("Next");
                        break;
                    default:
                        break;
                }
            }
        });

        //해당 항목을 먼저 작성하지 않으면 다음단계로 못넘어가게 했다. 순서도 중요한거 같아서
        nextText = (TextView) findViewById(R.id.nextText);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                switch (count) {
                    case 1:
                        if(firstFragment.firstLine.getText().toString().equals("")) {
                            Snackbar.make(dateText,"오늘, 가장 안좋았던 일을 먼저 써보세요", Snackbar.LENGTH_SHORT).show();
                            count--;
                        } else {
                            getFragmentManager().beginTransaction().replace(R.id.pannel, secondFragment).commit();
                            prev.setBackgroundResource(R.drawable.left_white);
                            prevText.setText("Prev");
                        }
                        break;
                    case 2:
                        if(secondFragment.secondLine.getText().toString().equals("")) {
                            Snackbar.make(dateText,"오늘 가장 좋았던 일을 먼저 써보세요", Snackbar.LENGTH_SHORT).show();
                            count--;
                        } else {
                            getFragmentManager().beginTransaction().replace(R.id.pannel, thirdFragment).commit();
                            nextText.setText("Done");
                        }
                        break;
                    case 3:
                        if(thirdFragment.thirdLine.getText().toString().equals("")) {
                            Snackbar.make(dateText,"내일의 다짐도 써보세요", Snackbar.LENGTH_SHORT).show();
                            count--;
                        } else {

                            //오늘 날짜에 2개 이상을 쓰려는 경우, 원래 것을 대체할 건지 물어보고 SAVED ACTIVITY로 INTENT에 부가데이터 담아서 넘기고, 해당 데이터를 데이터베이스에 저장
                            long now = System.currentTimeMillis();
                            Date today = new Date(now);
                            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                            SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
                            SimpleDateFormat dayFormat = new SimpleDateFormat("E");

                            year = yearFormat.format(today);
                            month = monthFormat.format(today);
                            date = dateFormat.format(today);
                            day = dayFormat.format(today);
                            _id = Integer.parseInt(year)*10000+Integer.parseInt(month)*100+Integer.parseInt(date);
                            good = firstFragment.firstLine.getText().toString();
                            bad = secondFragment.secondLine.getText().toString();
                            will = thirdFragment.thirdLine.getText().toString();

                            SQL = "SELECT _ID FROM SEJUL ORDER BY _ID DESC LIMIT 1";

                            if(mDatabase != null) {
                                Cursor cursor = mDatabase.rawQuery(SQL);
                                if(cursor.moveToNext()) {
                                    if(cursor.getInt(0)==_id) {
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WritingActivity.this,R.style.CustomAlertDialogStyle);
                                        alertDialogBuilder.setTitle("");
                                        alertDialogBuilder
                                                .setMessage("세줄일기는 하루에 하나로 충분해요. 새로 쓰신 걸로 수정해드릴까요?")
                                                .setCancelable(false)
                                                .setPositiveButton("네, 수정할게요",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                count = 0;
                                                                dialog.cancel();
                                                                SQL = "DELETE FROM SEJUL WHERE _ID = "+ String.valueOf(_id);
                                                                mDatabase.execSQL(SQL);

                                                                SQL = "INSERT INTO SEJUL(_ID, YEAR, MONTH, DATE, DAY, GOOD, BAD, WILL) VALUES(" + _id + ", '" + year + "', '" + month + "', '" + date + "', '" + day + "', '" + good + "', '" + bad + "', '" + will + "')";
                                                                mDatabase.execSQL(SQL);

                                                                Intent intent = new Intent(getApplicationContext(), SavedActivity.class);
                                                                intent.putExtra("firstLine", good);
                                                                intent.putExtra("secondLine", bad);
                                                                intent.putExtra("thirdLine", will);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        })
                                                .setNegativeButton("아니에요",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(
                                                                    DialogInterface dialog, int id) {
                                                                count--;
                                                                dialog.cancel();
                                                            }
                                                        });

                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();
                                    } else {
                                        count = 0;
                                        SQL = "INSERT INTO SEJUL(_ID, YEAR, MONTH, DATE, DAY, GOOD, BAD, WILL) VALUES(" + _id + ", '" + year + "', '" + month + "', '" + date + "', '" + day + "', '" + good + "', '" + bad + "', '" + will + "')";
                                        mDatabase.execSQL(SQL);

                                        Intent intent = new Intent(getApplicationContext(), SavedActivity.class);
                                        intent.putExtra("firstLine", good);
                                        intent.putExtra("secondLine", bad);
                                        intent.putExtra("thirdLine", will);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    count = 0;
                                    SQL = "INSERT INTO SEJUL(_ID, YEAR, MONTH, DATE, DAY, GOOD, BAD, WILL) VALUES(" + _id + ", '" + year + "', '" + month + "', '" + date + "', '" + day + "', '" + good + "', '" + bad + "', '" + will + "')";
                                    mDatabase.execSQL(SQL);

                                    Intent intent = new Intent(getApplicationContext(), SavedActivity.class);
                                    intent.putExtra("firstLine", good);
                                    intent.putExtra("secondLine", bad);
                                    intent.putExtra("thirdLine", will);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        //글자 눌러도 버튼 누른것 같은 효과 위해서
        prevText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                prev.performClick();
                return false;
            }
        });

        nextText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                next.performClick();
                return false;
            }
        });

        //타이틀 바에 오늘날짜 띄우기
        dateText = (TextView) findViewById(R.id.dateText);
        long now = System.currentTimeMillis();
        Date today = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy. MM. dd. E");
        String timeText = simpleDateFormat.format(today);
        dateText.setText(timeText);
    }

    //데이터베이스 사용 위해 오픈
    public void openDatabase() {
        if(mDatabase != null) {
            mDatabase.close();
            mDatabase = null;
        }

        mDatabase = SejulDiaryDatabase.getInstance(this);
        mDatabase.open();
    }

    //액티비티 열때마다 오픈
    @Override
    protected void onStart() {
        super.onStart();
        openDatabase();
    }
}
