package com.veryoriginalname.run_n_map;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button run;
    Button seek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        run = (Button) findViewById(R.id.run_button);
        seek = (Button) findViewById(R.id.seek_button);


        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this, GenerateCode.class);
                startActivity(i);

            }
        });

        seek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this, SeekerCodeInput.class);
                startActivity(i);

            }
        });
    }
}