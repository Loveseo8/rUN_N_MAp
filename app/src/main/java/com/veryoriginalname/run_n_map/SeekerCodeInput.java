package com.veryoriginalname.run_n_map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SeekerCodeInput extends AppCompatActivity {

    Button start;
    EditText code;
    String [] location;
    DatabaseReference databaseLocation;
    String latitude, longitude, s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_input);

        code = (EditText) findViewById(R.id.code1);
        start = (Button) findViewById(R.id.start);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseLocation = FirebaseDatabase.getInstance().getReference("Locations");
                databaseLocation.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String gen = code.getText().toString();
                        s = dataSnapshot.child(gen).getValue(String.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                location = s.split(", ");
                latitude = location[0];

                Log.d("LOG", latitude);
                longitude = location[1];

                Intent i = new Intent(SeekerCodeInput.this, MapsActivity.class);
                i.putExtra("latitude", latitude);
                i.putExtra("longitude", longitude);
                startActivity(i);
            }
        });


    }
}
