package com.example.guru.studentattendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Guru on 4/10/2018.
 */

public class StudentDBHelper extends SQLiteOpenHelper{

    public StudentDBHelper(Context context) {

        super(context, "studentAttendance.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("drop table if exists attendance");
//        db.execSQL("drop table if exists checkin");
        db.execSQL("CREATE TABLE attendance ( s_id INTEGER,checkin_time DATETIME DEFAULT (time('now','localtime')),checkin_date DATE DEFAULT (date(datetime('now','localtime'))), session_id INTEGER DEFAULT -99, penalty FLOAT DEFAULT 0.0, latitude FLOAT DEFAULT 0.0, longitude FLOAT DEFAULT 0.0)");
        db.execSQL("CREATE TABLE checkin (s_id INTEGER,session_id INTEGER,status INTEGER)");
        System.out.println("Created Gurunath");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("drop table if exists attendance");
        db.execSQL("drop table if exists checkin");
        System.out.println("Deleted Gurunath");
        onCreate(db);

    }

    public void insertCheckInStatus(Integer id,Integer status){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select status from checkin where s_id="+id+"", null );
        if (res.getCount() !=0 ){
            System.out.println("Returning gurunath without inserting checkin status");
            return ;
        }
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("s_id", id);
        contentValues.put("status", status);
        db.insert("checkin", null, contentValues);
        System.out.println("Inserted checkin status gurunath");
        return ;
    }

    public void updateCheckInStatus(Integer id,Integer status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("status", status);
        int stat= db.update("checkin", contentValues, "s_id="+id, null);

        System.out.println("Updated checkin status gurunath with status"+stat);
        return ;
    }


    public boolean getCheckInStatus(Integer id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select status from checkin where s_id="+id+"", null );
//        System.out.println("get gurunath checkin c"+res);
        System.out.println("get gurunath checkin count"+res.getCount());
        res.moveToFirst();
        return res.getInt(0)==1;
    }

    public String getCurrentDate() {
        Date date = new Date();
        String Date= new SimpleDateFormat("yyyy-MM-dd").format(date);
        return Date;
    }

    public boolean insertContact (Integer s_id,Integer session_id,double latitude,double longitude) {
        SQLiteDatabase db = this.getReadableDatabase();
        String s ="select * from attendance where s_id = ? AND checkin_date = ? AND session_id = ?";
        Cursor res =  db.rawQuery( s, new String[] {s_id.toString(),getCurrentDate(),session_id.toString()} );
        if(res.getCount()>0){
            System.out.println("Returning gurunath without inserting checkin status");
            return false;
        }
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("s_id", s_id);
        contentValues.put("session_id", session_id);
        contentValues.put("latitude", latitude);
        contentValues.put("longitude", longitude);
        db.insert("attendance", null, contentValues);
        System.out.println("Inserted gurunath");
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from attendance where s_id="+id+"", null );
//        System.out.println("gurunath row"+res.getString(0));
        System.out.println("Returned gurunath");

        return res;
    }


    public ArrayList<String> getAllContacts() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from attendance", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex("s_id"))+"  "+res.getString(res.getColumnIndex("checkin_date"))+" "+res.getString(res.getColumnIndex("checkin_time"))+" session-"+res.getString(res.getColumnIndex("session_id"))+" "+res.getString(res.getColumnIndex("penalty"))+" "+res.getString(res.getColumnIndex("penalty"))+" "+res.getString(res.getColumnIndex("latitude"))+" " +res.getString(res.getColumnIndex("longitude")));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<Daysummary> getSummary(Integer id){
        ArrayList<Daysummary> listitems = new ArrayList<Daysummary>();
        ArrayList<String> days = new ArrayList<String>();

        //listitems.add(new Daysummary("04/11/2018","Leaves:0.25",new ArrayList<String> (Arrays.asList("Session1-Present", "Session2-Absent", "Session3-Present"))));

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select DISTINCT checkin_date from attendance", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            days.add(res.getString(res.getColumnIndex("checkin_date")));
            res.moveToNext();
        }
        System.out.println("Days ArrayList Gurunath"+days);


        for (String i:days ){
            ArrayList<String> session_rows = new ArrayList<String>(Arrays.asList("Session1 - Absent","Session2 - Absent","Session3 - Absent"));
            Cursor res2;
            String s ="select * from attendance where s_id = ? AND checkin_date = ?";
            res2 =  db.rawQuery( s, new String[] {id.toString(),i} );
            res2.moveToFirst();
            float Leaves = 1;
            System.out.println("########################Day Gurunath########");
            while(res2.isAfterLast() == false){

                int sess_id = Integer.valueOf(res2.getString(res2.getColumnIndex("session_id")));
                System.out.println("session id "+sess_id);
                if (sess_id == 1 || sess_id == 2){
                    System.out.println("Leaves1 gurunath"+ Leaves);
                    Leaves -= 0.25;
                }
                else if (sess_id == 3){

                    Leaves -= 0.5;
                    System.out.println("Leaves2 gurunath"+ Leaves);
                }
                session_rows.set(sess_id-1,("Session"+sess_id+" - "+"Present"+"       Loc - "+res2.getString(res2.getColumnIndex("latitude"))+" "+res2.getString(res2.getColumnIndex("longitude"))));
                System.out.println("im in Session rows ArrayList Gurunath"+session_rows);

                res2.moveToNext();
            }
            System.out.println("Session rows gurunath"+ session_rows);
            System.out.println("Leaves gurunath"+ Leaves);
            listitems.add(new Daysummary(i,"Leaves : " + Float.toString(Leaves),session_rows));
            System.out.println("################################");


        }


//        System.out.println("Session rows ArrayList Gurunath"+session_rows);
        return listitems;
    }



}
