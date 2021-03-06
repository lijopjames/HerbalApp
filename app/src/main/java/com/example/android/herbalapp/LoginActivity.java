package com.example.android.herbalapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button b1;
    TextView tosignup,emaillogin,passlogin;
    AlertDialog.Builder builder;
    final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        b1 = (Button)findViewById(R.id.login_button);
        tosignup = (TextView)findViewById(R.id.tosignup);
        emaillogin = (TextView)findViewById(R.id.email);
        passlogin = (TextView)findViewById(R.id.password);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (emaillogin.getText().toString().equals("")||passlogin.getText().toString().equals("")){


                    builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("Error");
                    builder.setMessage("Please fill all the fields");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();


                }
                else {

                    Ip i=new Ip();
                    String ip=i.getIp();

                    String url = "http://"+ip+"/herbalapp/login.php";

                    final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG,response);
                            try {

                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                                JSONObject JO = jsonArray.getJSONObject(0);
                                String code = JO.getString("code");
                                String message = JO.getString("message");
                                if (code.equals("login_true")){

                                    String[] splited = message.split("\\s+");
                                    String name,email;
                                    name = splited[0];
                                    email = splited[1];
                                    SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
                                    prefs.edit().putString("name", name).apply();
                                    prefs.edit().putString("email",email).apply();
                                    Log.d(TAG,name);
                                    Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                    startActivity(intent);

                                }else if (code.equals("login_false")){

                                    Toast.makeText(LoginActivity.this,"User not found",Toast.LENGTH_SHORT).show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof NoConnectionError) {
                                Toast.makeText(LoginActivity.this,"no connection",Toast.LENGTH_LONG).show();
                            } else if (error instanceof AuthFailureError) {
                                //TODO
                            } else if (error instanceof ServerError) {
                                //TODO
                            } else if (error instanceof NetworkError) {
                                //TODO
                            } else if (error instanceof ParseError) {
                                //TODO
                            }
                            //Toast.makeText(getApplicationContext(),"error while reading",Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<>();
                            params.put("email",emaillogin.getText().toString());
                            params.put("password",passlogin.getText().toString());
                            Log.d(TAG,emaillogin.getText().toString());
                            return params;
                        }


                    };

                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

                }

            }
        });

        tosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
