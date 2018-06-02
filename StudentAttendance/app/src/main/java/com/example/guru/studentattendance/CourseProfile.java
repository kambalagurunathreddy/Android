package com.example.guru.studentattendance;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class CourseProfile extends AppCompatActivity {

        private ProgressBar pbar;


        @Override
        protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_profile);
        setTitle("Course Profile");
        load_Course_data();

        }

        private void load_Course_data()
        {


                if (AppStatus.getInstance(this).isOnline()) {

                        Toast.makeText(this,"You are online!!!!",Toast.LENGTH_SHORT).show();
                        FetchCourseDetails getCourseDetailsTask = new FetchCourseDetails(this);
                        pbar=(ProgressBar) findViewById(R.id.progressbar);
                        getCourseDetailsTask.execute("");

                } else {

                        Toast.makeText(this,"You are offline!!!!",Toast.LENGTH_SHORT).show();
                }


               ;
        }

        private class FetchCourseDetails extends AsyncTask<String, Void, ArrayList<String>> {
                float cgpa;
                int total_credits = 0;
                float student_credits = 0;
                private final String LOG_TAG = FetchCourseDetails.class.getSimpleName();
                WeakReference<Activity> mWeakActivity;

                public FetchCourseDetails (Activity activity) {
                        mWeakActivity = new WeakReference<Activity>(activity);
                }

               /**
                 * Take the String representing the complete forecast in JSON Format and
                 * pull out the data we need to construct the Strings needed for the wireframes.
                 *
                 * Fortunately parsing is easy:  constructor takes the JSON string and converts it
                 * into an Object hierarchy for us.
                 */
                private ArrayList<String> getCourseDetailsFromJson(String CourseJsonStr)
                        throws JSONException {

                        JSONObject forecastJson = new JSONObject(CourseJsonStr);
                        JSONArray CourseDataArray = forecastJson.getJSONArray("data");

                        HashMap<String, Integer> credits_map = new HashMap<>();
                        HashMap<String, Float> grade_map = new HashMap<>();

                        credits_map.put("Digital Literacy",1);
                        credits_map.put("Mobile Programming",1);
                        credits_map.put("Web programming",3);
                        credits_map.put("Introduction to Computer Systems",4);
                        credits_map.put("Cyber Security",2);
                        credits_map.put("Computer Network Foundation",2);
                        credits_map.put("DBMS",2);
                        credits_map.put("ADS-2",4);
                        credits_map.put("ADS-1",4);
                        credits_map.put("CSPP-2",4);
                        credits_map.put("CSPP-1",4);
                        credits_map.put("Computational Thinking",2);

                        grade_map.put("Ex",Float.valueOf("10"));
                        grade_map.put("A+",Float.valueOf("9.5"));
                        grade_map.put("A",Float.valueOf("9"));
                        grade_map.put("B+",Float.valueOf("8.5"));
                        grade_map.put("B",Float.valueOf("8"));
                        grade_map.put("C",Float.valueOf("7"));


//                        float cgpa = 0;








                        ArrayList<String> CourseAttrbs_List=new ArrayList<String>();

//                        String CourseObject = "{\"Course_fullname\" : \"Kambala Gurunath Reddy\",\"roll_number\" : \"20176012\",\"Course_email\" : \"kambalagurunathreddy@gmail.com\",\"rank\" : 19, \"Course_mobile1\": \"9441106350\",\"father_name\": \"Kambala Subba Reddy\", \"ug_grade\": \"89.6\", \"father_email\": \"\", \"last_updated\": \"2017-12-20 15:15:38.453560\", \"tenth_grade\": \"94.6\", \"Course_firstname\": null, \"mother_name\": \"\", \"hostel\": null, \"intermediate_grade\": \"98.9\", \"qualifier_exam\": \"Walkin\", \"state\": \"\", \"date_of_birth\": \"\", \"Course_lastname\": null, \"roll_number\": \"20176012\", \"gat_critical_reading\": \"640\", \"landline\": null, \"quit\": false, \"gat_quantitative_ability\": \"730\", \"address_permanent\": \"\", \"user_type\": \"Course\", \"gat_writing\": \"700\", \"father_mobile\": \"9441684408\", \"ug_branch\": \"ECE\", \"total_gat_score\": \"2070\", \"center\": \"\", \"gender\": \"Male\", \"prep_roll_number\": \"\", \"address_temporary\": \"\", \"city\": \"\"}";

                        try {
                                CourseAttrbs_List.add("course_id" + " "+
                                        "course_name" + " "+
                                        "grade" + " "+
                                        "mentor_name" + " "
                                );
//                                int total_credits = 0;
//                                float student_credits = 0;
                                System.out.println(" Im in gurunath");
                                for (int i = 0;i<CourseDataArray.length();i++ ) {
                                        JSONObject reader;

                                        reader = CourseDataArray.getJSONObject(i);
                                        CourseAttrbs_List.add(reader.getString("course_id") + " "+
                                                reader.getString("course_name") + " "+
                                                reader.getString("grade") + " "+
                                                reader.getString("mentor_name") + " "
                                        );

                                        System.out.println("Status Gurunath"+(reader.getString("status").equals("Pass")));
                                        if ( reader.getString("status").equals("Pass")) {
                                                int sub_credits = credits_map.get(reader.getString("course_name"));
                                                String sub_grade = reader.getString("grade");
                                                Float sub_grade_points = grade_map.get(sub_grade);
                                                System.out.println("grading gurunath"+ sub_grade);
                                                System.out.println("gradepoints gurunath"+ sub_grade_points);
                                                student_credits += sub_credits * sub_grade_points;
                                                total_credits += sub_credits;
                                                System.out.print("Creds Gurunath"+student_credits+" "+total_credits);

                                        }
                                        System.out.print("Course Details Gurunath"+CourseAttrbs_List.get(i+1));
                                        System.out.println("grade2 gurunath"+ this.cgpa);
                                        System.out.println("###################################");
                                }
                                System.out.print("CourseTotal Gurunath"+CourseAttrbs_List);

                                Log.v("hello","outside gurunath");
                                System.out.println(" Im out gurunath");
                                System.out.println("Total stud_creds gurunath"+ student_credits);
                                System.out.println("Total course_creds gurunath"+ total_credits);

                                cgpa = student_credits/total_credits;
                                System.out.println("cgpa gurunath"+ cgpa);


                        }
                        catch (Exception e){
                                e.printStackTrace();
                        }


                        return CourseAttrbs_List;

                }

                @Override
                protected void onPreExecute() {
                        super.onPreExecute();
                        pbar.setVisibility(View.VISIBLE);
                }

                @Override
                protected ArrayList<String> doInBackground(String... params) {

                        // If there's no zip code, there's nothing to look up.  Verify size of params.
                        if (params.length == 0) {
                                return null;
                        }

                        // These two need to be declared outside the try/catch
                        // so that they can be closed in the finally block.
                        HttpURLConnection urlConnection = null;
                        BufferedReader reader = null;

                        // Will contain the raw JSON response as a string.
                        String CourseJsonStr = null;

                        String format = "json";
                        String units = "metric";
                        int numDays = 7;

                        try {
                                // Construct the URL for the OpenWeatherMap query
                                // Possible parameters are avaiable at OWM's forecast API page, at
                                // http://openweathermap.org/API#forecast
                                final String FORECAST_BASE_URL =
                                        "http://msitis-iiith.appspot.com/api/course/ag5ifm1zaXRpcy1paWl0aHIUCxIHU3R1ZGVudBiAgICA2reXCgw";

                                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                                        .build();
                                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                                // Create the request to OpenWeatherMap, and open the connection
                                try {
                                        URL url = null;
                                        url = new URL(builtUri.toString());
                                        urlConnection = (HttpURLConnection) url.openConnection();
                                        urlConnection.setRequestMethod("GET");
                                        urlConnection.connect();
                                } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                        return null;
                                } catch (IOException e){
                                        e.printStackTrace();

                                }

                                // Read the input stream into a String
                                InputStream inputStream = urlConnection.getInputStream();
                                StringBuffer buffer = new StringBuffer();
                                if (inputStream == null) {
                                        // Nothing to do.
                                        return null;
                                }
                                reader = new BufferedReader(new InputStreamReader(inputStream));

                                String line;
                                while ((line = reader.readLine()) != null) {
                                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                                        // But it does make debugging a *lot* easier if you print out the completed
                                        // buffer for debugging.
                                        buffer.append(line + "\n");
                                }

                                if (buffer.length() == 0) {
                                        // Stream was empty.  No point in parsing.
                                        return null;
                                }
                                CourseJsonStr = buffer.toString();

                                Log.v(LOG_TAG, "CourseDetails string: " + CourseJsonStr);
                        } catch (IOException e) {
                                Log.e(LOG_TAG, "Error ", e);
                                // If the code didn't successfully get the weather data, there's no point in attemping
                                // to parse it.
                                return null;
                        } finally {
                                if (urlConnection != null) {
                                        urlConnection.disconnect();
                                }
                                if (reader != null) {
                                        try {
                                                reader.close();
                                        } catch (final IOException e) {
                                                Log.e(LOG_TAG, "Error closing stream", e);
                                        }
                                }
                        }

                        try {
                                return getCourseDetailsFromJson(CourseJsonStr);
                        } catch (JSONException e) {
                                Log.e(LOG_TAG, e.getMessage(), e);
                                e.printStackTrace();
                        }

                        // This will only happen if there was an error getting or parsing the forecast.
                        return null;
                }


                @Override
                protected void onPostExecute(ArrayList<String> CourseAttrbs_List) {

                        pbar.setVisibility(View.INVISIBLE);

                        Activity activity = mWeakActivity.get();
                        if (activity != null) {
                                System.out.println("Guruuuuuuuuuuuu");
                                // Get reference of widgets from XML layout
                                final ListView lv = (ListView) findViewById(R.id.profiledata);
                                lv.setVisibility(View.VISIBLE);
                                // Create an ArrayAdapter from List
                                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                                        (activity, android.R.layout.simple_list_item_1, CourseAttrbs_List);

                                // DataBind ListView with items from ArrayAdapter
                                lv.setAdapter(arrayAdapter);


                                TextView tv = (TextView) findViewById(R.id.cgpa);
                                tv.setVisibility(View.VISIBLE);
                                cgpa = student_credits/total_credits;
                                tv.setText("CGPA : "+String.valueOf(cgpa));

                        }











//                                System.out.println("^^^^^^^^^^^^image^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
//                                System.out.println(RawImage);
////                                        imageByte = Base64.getDecoder().decode(RawImage);
////                                        imageByte = DatatypeConverter.parseBase64Binary(RawImage);
////                                        byte[] imageByte = Base64.decode(RawImage, Base64.DEFAULT);
////                                        ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
////                                        image = BitmapFactory.decodeStream(bis);
//
////                                        String img = "your_base_64_string";
////                                        byte[] decodedString = Base64.decode(img, Base64.DEFAULT);
////                                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//
////                                byte[] data = Base64.decode(RawImage, Base64.DEFAULT);
//                                String decodedString = null;
//                                //                                        decodedString = new String(data, "ISO-8859-1");
//                                byte[] imageByte = Base64.decode(RawImage, Base64.NO_WRAP | Base64.URL_SAFE);
//                                Bitmap base64Bitmap = BitmapFactory.decodeByteArray(imageByte, 0,
//                                        decodedString.length());
//                                System.out.println("$$$$$$$$$$$$$$$$$$$Myimmage$$$$$$$$$$$$$$$$$$$$$");
//                                Bitmap base64Bitmap = decodeFromBase64ToBitmap(RawImage);
//                                iv.setImageBitmap(base64Bitmap);

                                //                                byte[] decodedString = new byte[0];
//                                decodedString = Base64.decode(RawImage, Base64.DEFAULT);



//                                byte[] decodedString = new byte[0];
//                                decodedString = Base64.decode(RawImage, Base64.DEFAULT);
//                                decodedString = new String(decodedString, "ISO-8859-1");
//                                Bitmap base64Bitmap = BitmapFactory.decodeByteArray(decodedString, 0,
//                                        decodedString.length);



                        }



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




