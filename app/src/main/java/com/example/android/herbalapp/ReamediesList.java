package com.example.android.herbalapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.amigold.fundapter.interfaces.DynamicImageLoader;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.json.JsonConverter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReamediesList extends AppCompatActivity implements Response.Listener<String> {

    String start = "1";
    final String TAG = this.getClass().getSimpleName();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reamedies_list);

        Ip i=new Ip();
        String ip=i.getIp();

        String url = "http://"+ip+"/herbalapp/curelist.php";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,this, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"error while reading",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("start",start);
                return params;
            }


        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        listView = (ListView)findViewById(R.id.lst2);
    }

    @Override
    public void onResponse(String response) {

        Log.d(TAG,response);
        //Toast.makeText(BusList.this,response,Toast.LENGTH_SHORT).show();
        ArrayList<Cures> jsonObject = new JsonConverter<Cures>().toArrayList(response,Cures.class);
        BindDictionary<Cures> dictionary = new BindDictionary<>();
        dictionary.addStringField(R.id.curename, new StringExtractor<Cures>() {
            @Override
            public String getStringValue(Cures cures, int position) {
                return cures.curename;
            }
        });






        FunDapter<Cures> funDapter = new FunDapter<>(getApplicationContext(),jsonObject,R.layout.custom_layout2,dictionary);
        listView.setAdapter(funDapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String selected =  ((TextView)view.findViewById(R.id.curename)).getText().toString();
                Intent intent = new Intent(ReamediesList.this,ReamediestDetails.class);
                intent.putExtra("curename",selected);
                startActivity(intent);

            }
        });



    }
}
