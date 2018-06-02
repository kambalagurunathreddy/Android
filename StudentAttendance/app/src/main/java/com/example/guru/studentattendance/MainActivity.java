package com.example.guru.studentattendance;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.util.GregorianCalendar;
import android.icu.util.TimeZone;
import android.icu.util.ULocale;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.evernote.android.job.JobManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


//interface  bReceiver{
//    void onReceive(Context context,Intent intent);
//
//
//
//}
public class MainActivity extends AppCompatActivity implements View.OnClickListener
//        , LocationListener

{
    private BroadcastReceiver bReceiver;



    private Chronometer chronometer;
    private long timeWhenStopped = 0;
    private boolean stopClicked;

    private PendingIntent pendingIntent1,pendingIntent2,pendingIntent3;
    private AlarmManager manager;

    private Button b_Mark_Attendance;
    private Button b_View_Attendance;

    private StudentDBHelper dbHelper;
    private Integer student_id = 20176012;

    private boolean didCheckIn;


    double user_latitude;
    double user_longitude;




    @SuppressLint("ResourceType")
    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Home");

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent1 = PendingIntent.getBroadcast(this, 1, alarmIntent, 0);
        pendingIntent2 = PendingIntent.getBroadcast(this, 2, alarmIntent, 0);
        pendingIntent3 = PendingIntent.getBroadcast(this, 3, alarmIntent, 0);

        //instantiating DB handler
        dbHelper = new StudentDBHelper(this);
        dbHelper.insertCheckInStatus(student_id, 0);
        this.didCheckIn = dbHelper.getCheckInStatus(student_id);
//        Cursor c=dbHelper.getData(student_id);

//        c.moveToFirst();
//        try {
//            if (c != null) {
//                c.moveToFirst();
////                while (c.moveToNext()) {
////                    System.out.println("looping gurunath");
////
////                }
//                System.out.println("printing gurunath student_id"+c.getString(0));
//                System.out.println("printing gurunath"+c.getString(1));
//            }
//
//        } finally {
//            c.close();
//        }

        for (String item : dbHelper.getAllContacts()) {
            System.out.println("Printing gurunath in loop" + item);
        }

        Chronometer timeElapsed = (Chronometer) findViewById(R.id.chronometer);
        TextView test = (TextView) findViewById(R.id.TimeRemaining);
//        test.setBackgroundResource(this.getResources().getColor(0x7b);// .getColor(android.R.color.holo_green_light));
        timeElapsed.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();
//                long time = SystemClock.;
//                System.out.println("cArg.getBase()"+cArg.getBase());
//                System.out.println("SystemClock.elapsedRealtime()"+SystemClock.elapsedRealtime());
                int h = (int) (time / 3600000);
                int m = (int) (time - h * 3600000) / 60000;
                int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                String hh = h < 10 ? "0" + h : h + "";
                String mm = m < 10 ? "0" + m : m + "";
                String ss = s < 10 ? "0" + s : s + "";
                try {
                    cArg.setText(/*hh + ":" + mm + ":" + ss + "\n" +*/ getTimeRemaining(setButtons()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                System.out.println(setButtons());
            }
        });
        System.out.print("$$$$$hey$$$$$$$$" + timeElapsed.getText());
        timeElapsed.getText();
        //timeElapsed.setBase(SystemClock.elapsedRealtime());
//        timeElapsed.setCountDown(false);
        timeElapsed.start();




        Intent intent = new Intent(this, LocationTrack.class);
        startActivityForResult(intent,1);


        System.out.println("my latitude"+this.user_longitude + " "+ this.user_latitude);

        b_Mark_Attendance = (Button) findViewById(R.id.UI_button_MarkAttendance);
        b_Mark_Attendance.setOnClickListener(this);

        b_View_Attendance = (Button) findViewById(R.id.UI_button_ViewAttendance);
        b_View_Attendance.setOnClickListener(this);


//        int sessionid= setButtons();
//        if(sessionid >= 3){
//            Toast.makeText(this,"You cannot sign in during this time",Toast.LENGTH_SHORT).show();
////            b_Mark_Attendance.isEnabled(false);
//        }

//        JobManager.create(this).addJobCreator(new DemoJobCreator());
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY,00);
        calendar.set(Calendar.MINUTE,42);
        calendar.set(Calendar.SECOND,30);

//        System.out.println("Im starting Notification in main gurunath"+SystemClock.elapsedRealtime() + 5000);
//
//        Intent intent1 = new Intent(getApplicationContext(),Notification_receiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
////        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 5000,
//                pendingIntent);
//        System.out.println("Im setting Notification in main gurunath");

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater Inflater = getMenuInflater();
        Inflater.inflate(R.menu.student_menu,menu);
        return true;
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ViewAttendance:
                Intent intent = new Intent(this, ViewAttendance.class);
                this.startActivity(intent);
                break;

            case R.id.profile:
                intent = new Intent(this, StudentProfile.class);
                this.startActivity(intent);
                break;

            case R.id.ViewCourses:
                intent = new Intent(this, CourseProfile.class);
                this.startActivity(intent);
                break;
            default:
                intent = new Intent(this, MainActivity.class);
//                this.startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.UI_button_MarkAttendance:
                markAttendance();

                break;
            case R.id.UI_button_ViewAttendance:
                dbHelper.getSummary(student_id);

                Intent intent = new Intent(this, ViewAttendance.class);
//                intent.putExtra("DBHelper", getDBHelper());
                this.startActivity(intent);

            default:
                break;
        }

    }
    public void markAttendance(){
        int sessionid= setButtons();
        if(sessionid > 3){
            Toast.makeText(getApplicationContext(),"You cannot sign in during this time",Toast.LENGTH_SHORT).show();
            return;
        }
//        this.didCheckIn = true;
        System.out.println("Marked Attendance"+sessionid);
        System.out.println("mark  attendance Gurunath 1"+this.didCheckIn);

        boolean insertStatus = dbHelper.insertContact(student_id,sessionid,this.user_latitude,this.user_longitude);

        if(!insertStatus){
            Toast.makeText(getApplicationContext(),"You have already checked in for this session",Toast.LENGTH_SHORT).show();
        }
        else{
            dbHelper.updateCheckInStatus(student_id,1);
            this.didCheckIn = dbHelper.getCheckInStatus(student_id);
            Toast.makeText(getApplicationContext(),"Your Attendance is Registered at "+getCurrentTime()+"\n Location : "+this.user_latitude + " "+this.user_longitude,Toast.LENGTH_SHORT).show();
        }


    }


    public String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        String strDate = mdformat.format(calendar.getTime());
       return strDate;
    }

    public String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("YYYY-MM-DD");
        String strDate = mdformat.format(calendar.getTime());
        return strDate;
    }

    public int setButtons(){
        String currTime=getCurrentTime();
        if(currTime.compareTo("08:50:00")>0 && currTime.compareTo("09:09:59")<0 ){
            b_Mark_Attendance.setEnabled(true);
            return 1;
        }
        else if (currTime.compareTo("10:50:00")>0 && currTime.compareTo("11:09:59")<0 ){
            b_Mark_Attendance.setEnabled(true);
            return 2;
        }
        else if (currTime.compareTo("13:50:00")>0 && currTime.compareTo("14:09:59")<0 ){
//            !didCheckIn &&
            b_Mark_Attendance.setEnabled(true);
            return 3;
        }
//        else{
//            b_Mark_Attendance.setEnabled(false);
////            dbHelper.updateCheckInStatus(student_id,0);
////            this.didCheckIn = dbHelper.getCheckInStatus(student_id);
////            if (currTime.compareTo("08:50:00")==0){
////                dbHelper.updateCheckInStatus(student_id,0);
////                this.didCheckIn = dbHelper.getCheckInStatus(student_id);
////            }
////            else if (currTime.compareTo("10:50:00")==0){
////                dbHelper.updateCheckInStatus(student_id,0);
////                this.didCheckIn = dbHelper.getCheckInStatus(student_id);
////            }
////            else if(currTime.compareTo("13:50:00")==0){
////                dbHelper.updateCheckInStatus(student_id,0);
////                this.didCheckIn = dbHelper.getCheckInStatus(student_id);
////            }
//            return 4;
//        }



//        b_Mark_Attendance.setEnabled(false);
        return 4;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
//                System.out.println( "Latitude Result"+data.getStringExtra("result"));
//                this.user_latitude = Double.valueOf(data.getStringExtra("Latitude"));
//                this.user_longitude = Double.valueOf(data.getStringExtra("Longitude"));
                this.user_latitude = data.getDoubleExtra("Latitude",0);
                this.user_longitude = data.getDoubleExtra("Longitude",0);
                System.out.println( "Latitude Result"+this.user_latitude);
                System.out.println( "Longitude Result"+this.user_longitude);
                Toast.makeText(getApplicationContext(),"Location : "+this.user_latitude + " "+this.user_longitude,Toast.LENGTH_SHORT).show();


            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }



    public  String getTimeRemaining(int session_id) throws ParseException {
        String temp=getCurrentTime();
        String time1 = getCurrentTime();
        String time2;
        int Flag = 0;
        if (session_id == 4){
            if (time1.compareTo("08:50:00")<0 ){//&& time1.compareTo("09:09:59")<0
                time2 = "08:50:00";
            }
            else if (time1.compareTo("10:50:00")<0 ){
                time2 = "10:50:00";
            }
            else if (time1.compareTo("13:50:00")<0 ){
                time2 = "13:50:00";
            }
            //else if (time1.compareTo("13:50:00")>0 ){
            else{
                time2 = "19:00:00";
            }
        }
        else if (session_id ==1){
            time2 = "09:10:00";
        }
        else if (session_id ==2){
            time2 = "11:10:00";
        }
        else{
            time2 = "02:10:00";
        }

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date1 = format.parse(time1);
        Date date2 = format.parse(time2);
        long millis = date2.getTime() - date1.getTime();

        String timeLeft = String.format("%02d:%02d:%02d",
                Math.abs(TimeUnit.MILLISECONDS.toHours(millis)),
                Math.abs(TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))), // The change is in this line
                Math.abs(TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));
        return timeLeft;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }


    public void startAlarm(View view) {
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        long interval =10000;

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        // set to mid-night
        cal.set(Calendar.HOUR_OF_DAY,8);
        cal.set(Calendar.MINUTE, 50);

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent1);

        cal.set(Calendar.HOUR_OF_DAY,10);
        cal.set(Calendar.MINUTE, 50);

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent2);

        cal.set(Calendar.HOUR_OF_DAY,13);
        cal.set(Calendar.MINUTE, 50);

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent3);
////        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarm_time1, AlarmManager.INTERVAL_DAY, pendingIntent);
//
//        Calendar cal2 = Calendar.getInstance();
//        // set to mid-night
//        cal2.set(Calendar.HOUR_OF_DAY, 07);
//        cal2.set(Calendar.MINUTE, 00);
//        cal2.set(Calendar.SECOND, 00);
//        cal2.set(Calendar.MILLISECOND, 00);
//        long alarm_time2 = cal2.getTimeInMillis();
//        manager.setExact(AlarmManager.RTC_WAKEUP, alarm_time2, pendingIntent);
//
//        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarm_time2, AlarmManager.INTERVAL_DAY, pendingIntent);
//
//        Calendar cal3 = Calendar.getInstance();
//        // set to mid-night
//        cal3.set(Calendar.HOUR_OF_DAY, 13);
//        cal3.set(Calendar.MINUTE, 48);
//        cal3.set(Calendar.SECOND, 00);
//        cal3.set(Calendar.MILLISECOND, 00);
//        long alarm_time3 = cal3.getTimeInMillis();
//
//        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,alarm_time3, AlarmManager.INTERVAL_DAY, pendingIntent);
////
//        manager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+interval, pendingIntent);
////        manager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+5000, pendingIntent);
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm(View view) {
//        if (manager != null) {
//            manager.cancel(pendingIntent);
//            Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
//        }
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent1 = PendingIntent.getBroadcast(this, 1, alarmIntent, 0);
        pendingIntent2 = PendingIntent.getBroadcast(this, 2, alarmIntent, 0);
        pendingIntent3 = PendingIntent.getBroadcast(this, 3, alarmIntent, 0);
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent1);
        manager.cancel(pendingIntent2);
        manager.cancel(pendingIntent3);
        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
    }

}
