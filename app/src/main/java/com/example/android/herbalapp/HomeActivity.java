package com.example.android.herbalapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    Button mplants,mcure,addbutton,profilebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mplants = (Button)findViewById(R.id.mplants);
        mcure = (Button)findViewById(R.id.mcure);
        addbutton = (Button)findViewById(R.id.addbutton);
        profilebutton = (Button)findViewById(R.id.profilebutton);


        profilebutton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 Intent intent = new Intent(HomeActivity.this,Profile.class);
                 startActivity(intent);

             }
         });

        mplants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeActivity.this,PlantsList.class);
                startActivity(intent);

            }
        });

        mcure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeActivity.this,ReamediesList.class);
                startActivity(intent);
            }
        });

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,AddPlants.class);
                startActivity(intent);
            }
        });
    }
}
