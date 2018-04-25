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

public class PlantsList extends AppCompatActivity implements Response.Listener<String> {


    String start = "1";
    final String TAG = this.getClass().getSimpleName();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plants_list);
        Ip i=new Ip();
        String ip=i.getIp();
        String url = "http://"+ip+"/herbalapp/plantlist.php";
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
        listView = (ListView)findViewById(R.id.lst1);


    }


    @Override
    public void onResponse(String response) {

        Log.d(TAG,response);
        //Toast.makeText(BusList.this,response,Toast.LENGTH_SHORT).show();
        ArrayList<Herbs> jsonObject = new JsonConverter<Herbs>().toArrayList(response,Herbs.class);
        BindDictionary<Herbs> dictionary = new BindDictionary<>();
        dictionary.addStringField(R.id.herbname, new StringExtractor<Herbs>() {
            @Override
            public String getStringValue(Herbs herbs, int position) {
                return herbs.pname;
            }
        });

        dictionary.addStringField(R.id.herbscientific, new StringExtractor<Herbs>() {
            @Override
            public String getStringValue(Herbs herbs, int position) {
                return herbs.sname;
            }
        });

        dictionary.addDynamicImageField(R.id.herbimage, new StringExtractor<Herbs>() {
            @Override
            public String getStringValue(Herbs herbs, int position) {
                return herbs.imageurl;
            }
        }, new DynamicImageLoader() {
            @Override
            public void loadImage(String url, ImageView view) {

                Ip i=new Ip();
                String ip=i.getIp();
                Picasso.with(getApplicationContext()).load("http://"+ip+"/herbalapp/"+ url).into(view);

            }
        });



        FunDapter<Herbs> funDapter = new FunDapter<>(getApplicationContext(),jsonObject,R.layout.custom_layout1,dictionary);
        listView.setAdapter(funDapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String selected =  ((TextView)view.findViewById(R.id.herbname)).getText().toString();
                Intent intent = new Intent(PlantsList.this,HerbDetails.class);
                intent.putExtra("herbname",selected);
                startActivity(intent);

            }
        });



    }
}
