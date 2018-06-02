package com.example.guru.studentattendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewAttendance extends AppCompatActivity {
    private StudentDBHelper dbHelper;
    private Integer student_id=20176012;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Daysummary> listitems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_view_attendance);
//
//        //instantiating DB handler
//        dbHelper = new StudentDBHelper(this);
//
//
//        final ListView lv = (ListView) findViewById(R.id.summary_list);
//
//        // Create an ArrayAdapter from List
//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
//                (this, android.R.layout.simple_list_item_1, dbHelper.getAllContacts());
//
//        // DataBind ListView with items from ArrayAdapter
//        lv.setAdapter(arrayAdapter);

        setContentView(R.layout.activity_viewattendance);
        setTitle("Attendance");
        dbHelper = new StudentDBHelper(this);


        recyclerView = (RecyclerView) findViewById(R.id.v_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
        listitems = new ArrayList<>();
//
//        listitems.add(new Daysummary("04/11/2018","Leaves:0.25",new ArrayList<String> (Arrays.asList("Session1-Present", "Session2-Absent", "Session3-Present"))));
//        listitems.add(new Daysummary("05/11/2018","Leaves:0.5",new ArrayList<String> (Arrays.asList("Session1-Absent", "Session2-Absent", "Session3-Present"))));
//        listitems.add(new Daysummary("04/11/2018","Leaves:0.25",new ArrayList<String> (Arrays.asList("Session1-Present", "Session2-Absent", "Session3-Present"))));
//        listitems.add(new Daysummary("05/11/2018","Leaves:0.5",new ArrayList<String> (Arrays.asList("Session1-Absent", "Session2-Absent", "Session3-Present"))));
//        listitems.add(new Daysummary("04/11/2018","Leaves:0.25",new ArrayList<String> (Arrays.asList("Session1-Present", "Session2-Absent", "Session3-Present"))));
//        listitems.add(new Daysummary("05/11/2018","Leaves:0.5",new ArrayList<String> (Arrays.asList("Session1-Absent", "Session2-Absent", "Session3-Present"))));
//        listitems.add(new Daysummary("04/11/2018","Leaves:0.25",new ArrayList<String> (Arrays.asList("Session1-Present", "Session2-Absent", "Session3-Present"))));
//        listitems.add(new Daysummary("05/11/2018","Leaves:0.5",new ArrayList<String> (Arrays.asList("Session1-Absent", "Session2-Absent", "Session3-Present"))));
//
        listitems = dbHelper.getSummary(this.student_id);
        adapter =new DaySummaryCustomAdapter(listitems,this);
        recyclerView.setAdapter(adapter);
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
                this.startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
