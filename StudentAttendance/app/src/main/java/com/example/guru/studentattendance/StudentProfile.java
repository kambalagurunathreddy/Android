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

public class StudentProfile extends AppCompatActivity {

        private ProgressBar pbar;


        @Override
        protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        setTitle("Profile");

        load_student_data();

        }

        private void load_student_data()
        {

                if (AppStatus.getInstance(this).isOnline()) {

                        Toast.makeText(this,"You are online!!!!",Toast.LENGTH_SHORT).show();
                        FetchStudentDetails getStudentDetailsTask = new FetchStudentDetails(this);
                        pbar=(ProgressBar) findViewById(R.id.progressbar);
                        getStudentDetailsTask.execute("");

                } else {

                        Toast.makeText(this,"You are offline!!!!",Toast.LENGTH_SHORT).show();
                }

        }

        private class FetchStudentDetails extends AsyncTask<String, Void, ArrayList<String>> {
                String RawImage;
                private final String LOG_TAG = FetchStudentDetails.class.getSimpleName();
                WeakReference<Activity> mWeakActivity;

                public FetchStudentDetails (Activity activity) {
                        mWeakActivity = new WeakReference<Activity>(activity);
                }

               /**
                 * Take the String representing the complete forecast in JSON Format and
                 * pull out the data we need to construct the Strings needed for the wireframes.
                 *
                 * Fortunately parsing is easy:  constructor takes the JSON string and converts it
                 * into an Object hierarchy for us.
                 */
                private ArrayList<String> getStudentDetailsFromJson(String studentJsonStr)
                        throws JSONException {

                        JSONObject forecastJson = new JSONObject(studentJsonStr);
                        JSONArray studentDataArray = forecastJson.getJSONArray("data");
                        ArrayList<String> studentAttrbs_List=new ArrayList<String>();

                        JSONObject reader;
//                        String studentObject = "{\"student_fullname\" : \"Kambala Gurunath Reddy\",\"roll_number\" : \"20176012\",\"student_email\" : \"kambalagurunathreddy@gmail.com\",\"rank\" : 19, \"student_mobile1\": \"9441106350\",\"father_name\": \"Kambala Subba Reddy\", \"ug_grade\": \"89.6\", \"father_email\": \"\", \"last_updated\": \"2017-12-20 15:15:38.453560\", \"tenth_grade\": \"94.6\", \"student_firstname\": null, \"mother_name\": \"\", \"hostel\": null, \"intermediate_grade\": \"98.9\", \"qualifier_exam\": \"Walkin\", \"state\": \"\", \"date_of_birth\": \"\", \"student_lastname\": null, \"roll_number\": \"20176012\", \"gat_critical_reading\": \"640\", \"landline\": null, \"quit\": false, \"gat_quantitative_ability\": \"730\", \"address_permanent\": \"\", \"user_type\": \"student\", \"gat_writing\": \"700\", \"father_mobile\": \"9441684408\", \"ug_branch\": \"ECE\", \"total_gat_score\": \"2070\", \"center\": \"\", \"gender\": \"Male\", \"prep_roll_number\": \"\", \"address_temporary\": \"\", \"city\": \"\"}";

                        try {
                                reader = studentDataArray.getJSONObject(0);
                                studentAttrbs_List.add("Name: "+reader.getString("student_fullname"));
                                studentAttrbs_List.add("ID: "+reader.getString("roll_number"));
                                studentAttrbs_List.add("E-Mail: "+reader.getString("student_email"));
                                studentAttrbs_List.add("Rank: "+reader.getString("rank"));
                                studentAttrbs_List.add("Mobile: "+reader.getString("student_mobile1"));
                                studentAttrbs_List.add("Father Name: "+reader.getString("father_name"));
                                studentAttrbs_List.add("UGgrade: "+reader.getString("ug_grade"));
                                studentAttrbs_List.add("Last updated: "+reader.getString("last_updated"));
                                studentAttrbs_List.add("Tenth grade:"+reader.getString("tenth_grade"));
                                studentAttrbs_List.add("Intermediate grade: "+reader.getString("intermediate_grade"));
                                studentAttrbs_List.add("Exam Qualified: "+reader.getString("qualifier_exam"));
                                studentAttrbs_List.add("GAT critical reading: "+reader.getString("gat_critical_reading"));
                                studentAttrbs_List.add("GAT quantitative ability: "+reader.getString("gat_quantitative_ability"));
                                studentAttrbs_List.add("Quit: "+reader.getString("quit"));
                                studentAttrbs_List.add("User Type: "+reader.getString("user_type"));
                                studentAttrbs_List.add("Father Mobile: "+reader.getString("father_mobile"));
                                studentAttrbs_List.add("UG branch: "+reader.getString("ug_branch"));
                                studentAttrbs_List.add("Total GAT score: "+reader.getString("total_gat_score"));
                                studentAttrbs_List.add("GAT quantitative ability: "+reader.getString("gat_quantitative_ability"));
                                studentAttrbs_List.add("GAT critical reading: "+reader.getString("gat_critical_reading"));
                                studentAttrbs_List.add("GAT writing: "+reader.getString("gat_writing"));
                               // studentAttrbs_List.add(reader.getString("image"));
                                RawImage = reader.getString("image");
                                System.out.print(studentAttrbs_List);
                                Activity activity = mWeakActivity.get();




                        }
                        catch (Exception e){
                                e.printStackTrace();
                        }


                        return studentAttrbs_List;

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
                        String studentJsonStr = null;

                        String format = "json";
                        String units = "metric";
                        int numDays = 7;

                        try {
                                // Construct the URL for the OpenWeatherMap query
                                // Possible parameters are avaiable at OWM's forecast API page, at
                                // http://openweathermap.org/API#forecast
                                final String FORECAST_BASE_URL =
                                        "http://msitis-iiith.appspot.com/api/profile/ag5ifm1zaXRpcy1paWl0aHIUCxIHU3R1ZGVudBiAgICA2reXCgw";

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
                                studentJsonStr = buffer.toString();

                                Log.v(LOG_TAG, "StudentDetails string: " + studentJsonStr);
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
                                return getStudentDetailsFromJson(studentJsonStr);
                        } catch (JSONException e) {
                                Log.e(LOG_TAG, e.getMessage(), e);
                                e.printStackTrace();
                        }

                        // This will only happen if there was an error getting or parsing the forecast.
                        return null;
                }


                @Override
                protected void onPostExecute(ArrayList<String> studentAttrbs_List) {

                        pbar.setVisibility(View.INVISIBLE);

                        Activity activity = mWeakActivity.get();
                        if (activity != null) {
                                System.out.println("Guruuuuuuuuuuuu");
                                // Get reference of widgets from XML layout
                                final ListView lv = (ListView) findViewById(R.id.profiledata);
                                lv.setVisibility(View.VISIBLE);
                                // Create an ArrayAdapter from List
                                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                                        (activity, android.R.layout.simple_list_item_1, studentAttrbs_List);

                                // DataBind ListView with items from ArrayAdapter
                                lv.setAdapter(arrayAdapter);
                                System.out.println("Gsdsuruuuusdsdsuuuuuuuu");

                                byte[] latinBytes = new byte[0];
                                Bitmap image = null;
                                //String RawImage=new String(studentAttrbs_List.get(studentAttrbs_List.size()-1).getBytes("ISO-8859-1"), "UTF-8");
//                                                String RawImage = reader.getString("image");
                                // Get the appropriate ImageView (UI Element)
                                ImageView iv = (ImageView) findViewById(R.id.imageView);
                                iv.setVisibility(View.VISIBLE);
                                try {
                                        latinBytes = RawImage.getBytes("ISO-8859-1");
                                } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                }
                                // Make base64 string and its byte array
                                String b64 = Base64.encodeToString(latinBytes, Base64.DEFAULT);
                                byte[] imgBytes = Base64.decode(b64, Base64.DEFAULT);

                                // Generate a bitmap image using the base64 bytes array.
                                Bitmap decodedImage = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
                                iv.setImageBitmap(decodedImage);


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




