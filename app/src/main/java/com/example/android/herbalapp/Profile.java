package com.example.android.herbalapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    TextView profilename,profileemail;
    RelativeLayout profilelogout;
    AlertDialog.Builder builder;
    String namea,emaila;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profilename = (TextView)findViewById(R.id.profilename);
        profileemail = (TextView)findViewById(R.id.profileemail);
        profilelogout = (RelativeLayout)findViewById(R.id.profilelogout);
        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
        namea = prefs.getString("name", "UNKNOWN");
        emaila =prefs.getString("email","UNKNOWN");
        profilename.setText(namea);
        profileemail.setText(emaila);

        profilelogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder = new AlertDialog.Builder(Profile.this);
                builder.setMessage("Are you sure to Logout?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent intent = new Intent(Profile.this,LoginActivity.class);
                        startActivity(intent);

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });





    }
}
