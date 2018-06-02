package com.example.guru.studentattendance;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Created by Guru on 4/13/2018.
 */

public class LocationTrack extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener, LocationListener {

        double user_latitude;
        double user_longitude;

        Location mLocation;
        TextView latLng;
        GoogleApiClient mGoogleApiClient;
        private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

        private LocationRequest mLocationRequest;
        private long UPDATE_INTERVAL = 15000;  /* 15 secs */
        private long FASTEST_INTERVAL = 5000; /* 5 secs */

        private ArrayList permissionsToRequest;
        private ArrayList permissionsRejected = new ArrayList();
        private ArrayList permissions = new ArrayList();

        private final static int ALL_PERMISSIONS_RESULT = 101;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
//            setContentView(R.layout.loc_test);

            latLng = (TextView) findViewById(R.id.latLng);

            permissions.add(ACCESS_FINE_LOCATION);
            permissions.add(ACCESS_COARSE_LOCATION);

            permissionsToRequest = findUnAskedPermissions(permissions);
            //get the permissions we have asked for before but are not granted..
            //we will store this in a global list to access later.


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


                if (permissionsToRequest.size() > 0)
                    requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }



            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();


        }


        private ArrayList findUnAskedPermissions(ArrayList wanted) {
            ArrayList result = new ArrayList();

            for (Object perm : wanted) {
                if (!hasPermission((String) perm)) {
                    result.add(perm);
                }
            }

            return result;
        }

        @Override
        protected void onStart() {
            super.onStart();
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }
        }

        @Override
        protected void onResume() {
            super.onResume();

            if (!checkPlayServices()) {
//                latLng.setText("Please install Google Play services.");

                Toast.makeText(getApplicationContext(), "Please install Google Play services.", Toast.LENGTH_LONG).show();

            }
        }

        @Override
        public void onConnected(@Nullable Bundle bundle) {


            if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


            if(mLocation!=null)
            {
//                latLng.setText("Latitude : "+mLocation.getLatitude()+" , Longitude : "+mLocation.getLongitude());

                this.user_latitude = mLocation.getLatitude();
                this.user_longitude = mLocation.getLongitude();
                System.out.println("latitude "+this.user_latitude+"Longitude"+this.user_longitude);
                Intent returnIntent = new Intent();
//            System.out.println("Location result"+this.user_latitude);
                returnIntent.putExtra("Latitude",this.user_latitude);
                returnIntent.putExtra("Longitude",this.user_longitude);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();

            }

            startLocationUpdates();

        }


    @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onLocationChanged(Location location) {

            if(location!=null)
                //latLng.setText("Latitude : "+location.getLatitude()+" , Longitude : "+location.getLongitude());
            this.user_latitude = mLocation.getLatitude();
            this.user_longitude = mLocation.getLongitude();
            System.out.println("latitude "+this.user_latitude+"Longitude"+this.user_longitude);
            Intent returnIntent = new Intent();
//            System.out.println("Location result"+this.user_latitude);
            returnIntent.putExtra("Latitude",this.user_latitude);
            returnIntent.putExtra("Longitude",this.user_longitude);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();



        }

        private boolean checkPlayServices() {
            GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
            int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
            if (resultCode != ConnectionResult.SUCCESS) {
                if (apiAvailability.isUserResolvableError(resultCode)) {
                    apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                            .show();
                } else
                    finish();

                return false;
            }
            return true;
        }

        protected void startLocationUpdates() {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(UPDATE_INTERVAL);
            mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
            if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Enable Permissions", Toast.LENGTH_LONG).show();
            }

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);


        }

        private boolean hasPermission(String permission) {
            if (canMakeSmores()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
                }
            }
            return true;
        }

        private boolean canMakeSmores() {
            return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
        }


        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

            switch (requestCode) {

                case ALL_PERMISSIONS_RESULT:
                    for (Object perms : permissionsToRequest) {
                        if (!hasPermission((String) perms)) {
                            permissionsRejected.add(perms);
                        }
                    }

                    if (permissionsRejected.size() > 0) {


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                                showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }

                    break;
            }

        }

        private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
            new AlertDialog.Builder(LocationTrack.this)
                    .setMessage(message)
                    .setPositiveButton("OK", okListener)
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show();
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            stopLocationUpdates();
        }


        public void stopLocationUpdates()
        {
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi
                        .removeLocationUpdates(mGoogleApiClient, this);
                mGoogleApiClient.disconnect();
            }
        }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
    }

//    public double getUser_latitude(){
//            return this.user_latitude;
//    }
//
//    public double getUser_longitude() {
//        return this.user_longitude;
//    }
}
