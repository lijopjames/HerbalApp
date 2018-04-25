package com.example.android.herbalapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HerbDetails extends AppCompatActivity implements Response.Listener<String> {

    ImageView herbimage;
    TextView cname,sname,malname,description,uses,price;
    Button buy;
    String herbname;
    final String TAG = this.getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_herb_details);
        herbimage = (ImageView)findViewById(R.id.himage);
        cname = (TextView)findViewById(R.id.datefrom);
        sname = (TextView)findViewById(R.id.dateuntil);
        malname = (TextView)findViewById(R.id.malname);
        description = (TextView)findViewById(R.id.desc);
        uses = (TextView)findViewById(R.id.usedfor);
        price = (TextView)findViewById(R.id.price);
        buy = (Button)findViewById(R.id.button);

        herbname = getIntent().getStringExtra("herbname");
        Ip i=new Ip();
        String ip=i.getIp();


        String url = "http://"+ip+"/herbalapp/plantdetails.php";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,this, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"error while reading",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("herbname",herbname);
                return params;
            }


        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);



    }

    @Override
    public void onResponse(String response) {
        Log.d(TAG,response);

        JSONArray array = null;
        try {
            array = new JSONArray(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < array.length(); i++) {
            JSONObject row = null;
            try {
                row = array.getJSONObject(i);
                final String commname = row.getString("pname");
                String sciname = row.getString("sname");
                String malayname = row.getString("mname");
                String plantdescription = row.getString("description");
                String plantuses = row.getString("usedfor");
                final String plantprice = row.getString("rates");
                String imageurl = row.getString("imageurl");
                cname.setText(commname);
                sname.setText(sciname);
                malname.setText(malayname);
                description.setText(plantdescription);
                uses.setText(plantuses);
                price.setText(plantprice);
                Ip ii=new Ip();
                String ip=ii.getIp();
                Picasso.with(getApplicationContext()).load("http://"+ip+"/herbalapp/"+imageurl).into(herbimage);

                buy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(HerbDetails.this,Sales.class);
                        intent.putExtra("productname",commname);
                        intent.putExtra("productprice",plantprice);
                        startActivity(intent);



                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }





    }
}
