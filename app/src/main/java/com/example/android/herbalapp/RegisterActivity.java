package com.example.android.herbalapp;

import android.content.DialogInterface;
import android.content.Intent;
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

public class RegisterActivity extends AppCompatActivity {

    TextView name,email,pass,cpass;
    Button register,gotologin;
    AlertDialog.Builder builder;
    final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = (TextView)findViewById(R.id.name);
        email = (TextView)findViewById(R.id.email);
        pass = (TextView)findViewById(R.id.password);
        cpass = (TextView)findViewById(R.id.conform_password);
        register = (Button)findViewById(R.id.register);
        gotologin = (Button)findViewById(R.id. login);

        gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().equals("")||email.getText().toString().equals("")||pass.getText().toString().equals("")||cpass.getText().toString().equals("")){

                    builder = new AlertDialog.Builder(RegisterActivity.this);
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
                else if (!(pass.getText().toString().equals(cpass.getText().toString()))){

                    builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("Error");
                    builder.setMessage("password not matching");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            pass.setText("");
                            cpass.setText("");

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }

                else {
                    Ip i=new Ip();
                    String ip=i.getIp();
                    String url = "http://"+ip+"/herbalapp/register.php";
                    final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG,response);
                            try {

                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                                JSONObject JO = jsonArray.getJSONObject(0);
                                String code = JO.getString("code");
                                if (code.equals("reg_true")){

                                    builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setTitle("Message");
                                    builder.setMessage("Registeration successful!! ,please login");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                            startActivity(intent);

                                        }
                                    });
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();



                                }else if (code.equals("reg_false")){

                                    Toast.makeText(RegisterActivity.this,"User alredy exist or no connection",Toast.LENGTH_SHORT).show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof NoConnectionError) {
                                Toast.makeText(RegisterActivity.this,"no connection",Toast.LENGTH_LONG).show();
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
                            params.put("name",name.getText().toString());
                            params.put("email",email.getText().toString());
                            params.put("password",pass.getText().toString());
                            Log.d(TAG,name.getText().toString());
                            return params;
                        }


                    };

                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);



                }
            }
        });

    }
}
