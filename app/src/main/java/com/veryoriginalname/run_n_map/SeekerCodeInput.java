package com.veryoriginalname.run_n_map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    DatabaseReference databaseLocation;
    String s, latitude, longitude;
    String [] location;
    SharedPreferences sPref;
    String ID = "Alice";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_input);
        sPref = getSharedPreferences(ID, Context.MODE_PRIVATE);
        code = (EditText) findViewById(R.id.code1);
        start = (Button) findViewById(R.id.start);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseLocation = FirebaseDatabase.getInstance().getReference("Locations");
                System.out.println(databaseLocation.toString());
                databaseLocation.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String gen = code.getText().toString().trim();
                        for(DataSnapshot child:snapshot.getChildren())
                        {
                            if(child.getKey().equals(gen))
                            {
                                String str = child.getValue().toString();
                                SharedPreferences.Editor ed = sPref.edit();
                                ed.putString(ID, str);
                                ed.commit();
                                Intent i = new Intent(SeekerCodeInput.this, MapsActivity.class);
                                startActivity(i);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
    }
}
