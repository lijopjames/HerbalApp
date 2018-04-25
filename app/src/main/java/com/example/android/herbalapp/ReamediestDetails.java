package com.example.android.herbalapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class ReamediestDetails extends AppCompatActivity implements Response.Listener<String> {

    TextView curename,curedesc,cureremedy;
    String dname;
    final String TAG = this.getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reamediest_details);
        curename =(TextView)findViewById(R.id.curename);
        curedesc =(TextView)findViewById(R.id.curedesc);
        cureremedy = (TextView)findViewById(R.id.remediesdesc);
        dname = getIntent().getStringExtra("curename");

        Ip i=new Ip();
        String ip=i.getIp();

        String url = "http://"+ip+"/herbalapp/curedetails.php";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,this, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"error while reading",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("dname",dname);
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
                String dname = row.getString("curename");
                String ddesc = row.getString("ddetails");
                String dcure = row.getString("dcuredesc");

                curename.setText(dname);
                curedesc.setText(ddesc);
                cureremedy.setText(dcure);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
