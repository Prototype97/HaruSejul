package com.sierramatcher.harusejul;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SejulDiaryDatabase {
    public static final String TAG = "SejulDiaryDatabase";

    private static SejulDiaryDatabase database;
    public static String DATABASE_NAME = "DiaryDatabase";
    public static String TABLE_NAME = "SEJUL";
    public static int DATABASE_VERSION = 1;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Context context;

    private SejulDiaryDatabase(Context context) {
        this.context = context;
    }

    public static SejulDiaryDatabase getInstance(Context context) {
        if (database == null) {
            database = new SejulDiaryDatabase(context);
        }
        return database;
    }

    public boolean open() {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();

        return true;
    }

    public void close() {
        db.close();

        database = null;
    }

    //결과값 있는 SQL문 처리메소드
    public Cursor rawQuery(String SQL) {
        println("\nexecuteQuery called.\n");

        Cursor c1 = null;
        try {
            c1 = db.rawQuery(SQL, null);
            println("cursor count : " + c1.getCount());
        } catch(Exception ex) {
            Log.e(TAG, "Exception in executeQuery", ex);
        }

        return c1;
    }

    //결과값 없는 SQL문 처리 메소드
    public boolean execSQL(String SQL) {
        println("\nexecute called.\n");

        try {
            Log.d(TAG, "SQL : " + SQL);
            db.execSQL(SQL);
        } catch(Exception ex) {
            Log.e(TAG, "Exception in executeQuery", ex);
            return false;
        }

        return true;
    }

    //데이터베이스 및 테이블 생성
    public class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            println("creating database [" + DATABASE_NAME + "].");

            println("creating table [" + TABLE_NAME + "].");

            String DROP_SQL = "drop table if exists " + TABLE_NAME;
            try {
                db.execSQL(DROP_SQL);
            } catch (Exception e) {
                Log.e(TAG, "Exception in dropping");
            }

            String CREATE_SQL = "create table " + TABLE_NAME + "("
                    +"_ID INTEGER PRIMARY KEY, "
                    +"YEAR VARCHAR(10), "
                    +"MONTH VARCHAR(10), "
                    +"DATE VARCHAR(10), "
                    +"DAY VARCHAR(10), "
                    +"GOOD TEXT, "
                    +"BAD TEXT, "
                    +"WILL TEXT)";

            try {
                db.execSQL(CREATE_SQL);
            } catch(Exception e) {
                Log.e(TAG, "Exception in creating", e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        @Override
        public void onOpen(SQLiteDatabase db) {

        }
    }
    private void println(String msg) {
        Log.d(TAG, msg);
    }
}
