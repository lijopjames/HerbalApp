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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Billing extends AppCompatActivity {

    TextView commonname,ptypeout,orderdate,amountdue;
    String commonnamea,ptypeouta,amountduea,formattedDate,namea,emaila;
    Button button;
    AlertDialog.Builder builder;
    final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        commonname = (TextView)findViewById(R.id.commonname);
        ptypeout = (TextView)findViewById(R.id.ptypeout);
        orderdate = (TextView)findViewById(R.id.orderdate);
        amountdue = (TextView)findViewById(R.id.amountdue);
        button = (Button)findViewById(R.id.button);
        commonnamea = getIntent().getStringExtra("pname");
        ptypeouta = getIntent().getStringExtra("ptype");
        amountduea = getIntent().getStringExtra("finalamount");
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MMMM dd, yyyy HH:mm");
        formattedDate = df.format(c);
        commonname.setText(commonnamea);
        ptypeout.setText(ptypeouta);
        orderdate.setText(formattedDate);
        amountdue.setText(amountduea);
        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
        namea = prefs.getString("name", "UNKNOWN");
        emaila =prefs.getString("email","UNKNOWN");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Ip i=new Ip();
                String ip=i.getIp();

                String url = "http://"+ip+"/herbalapp/order.php";
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
                            if (code.equals("update_true")){

                                builder = new AlertDialog.Builder(Billing.this);
                                builder.setTitle("Alert");
                                builder.setMessage(message);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        Intent intent = new Intent(Billing.this,HomeActivity.class);
                                        startActivity(intent);

                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();






                            }else if (code.equals("update_false")){

                                Toast.makeText(Billing.this,"Server error and purchase failed!!",Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError) {
                            Toast.makeText(Billing.this,"no connection",Toast.LENGTH_LONG).show();
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
                        params.put("name",namea);
                        params.put("email",emaila);
                        params.put("productname",commonnamea);
                        params.put("producttype",ptypeouta);
                        params.put("purchasedate",formattedDate);
                        params.put("amountdue",amountduea);
                        Log.d(TAG,emaila);
                        return params;
                    }
                };

                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

            }
        });



    }
}
