package com.veryoriginalname.run_n_map;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class GenerateCode extends AppCompatActivity {

    TextView code;
    Button start;
    Button end;
    DatabaseReference databaseLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    String latitude, longitude;
    int PERMISSION_ID = 44;
    String generated_code;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generate_code);

        code = (TextView) findViewById(R.id.code);
        start = (Button) findViewById(R.id.button_start);
        end = (Button) findViewById(R.id.button_end);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        getLastLocation();


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Random rand = new Random();

                int f =  rand.nextInt();

                generated_code = Integer.toHexString(f);

                code.setText(generated_code);
                databaseLocation = FirebaseDatabase.getInstance().getReference("location").child(generated_code);

                databaseLocation.setValue(latitude + ", " + longitude);
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference ds = FirebaseDatabase.getInstance().getReference("location").child(generated_code);
                ds.removeValue();

            }
        });
    }



    @SuppressLint("MissingPermission")

    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
               fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                   @Override
                   public void onComplete(@NonNull Task<Location> task) {

                                        Location location = task.getResult();

                                        if (location == null) {

                                            requestNewLocationData();

                                        }

                                        else {

                                            latitude = location.getLatitude() + "";
                                            longitude = location.getLongitude() + "";
                                        }

                   }

               });

            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }

        }

        else {
            requestPermissions();

        }

    }
    @SuppressLint("MissingPermission")

    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mLocationRequest.setInterval(5);

        mLocationRequest.setFastestInterval(0);

        mLocationRequest.setNumUpdates(1);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());

    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

            latitude = mLastLocation.getLatitude() + "";

            longitude = mLastLocation.getLongitude() + "";
        }
    };

    private boolean checkPermissions() {

        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION }, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {

        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getLastLocation();

            }
        }
    }

    @Override

    public void onResume() {
        super.onResume();

        if (checkPermissions()) {

            getLastLocation();

        }

    }

}

