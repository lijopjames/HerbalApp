package com.example.android.herbalapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddPlants extends AppCompatActivity implements View.OnClickListener {

    EditText commonname,sname,mname,desc,usages,imagename,amount;
    Button uploadimage,updatebutton;
    ImageView uploadedimage;
    private final int IMG_REQUEST = 1;
    private Bitmap bitmap;
    Uri path;
    AlertDialog.Builder builder;
    final String TAG = this.getClass().getSimpleName();
    Ip i=new Ip();
    String ip=i.getIp();
    private String UploadUrl = "http://"+ip+"/herbalapp/updateplants.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plants);

        commonname = (EditText)findViewById(R.id.commonname);
        sname = (EditText)findViewById(R.id.sname);
        mname = (EditText)findViewById(R.id.mname);
        desc = (EditText)findViewById(R.id.desc);
        usages = (EditText)findViewById(R.id.usages);
        imagename = (EditText)findViewById(R.id.imagename);
        amount = (EditText)findViewById(R.id.amount);

        uploadimage = (Button)findViewById(R.id.uploadimage);
        updatebutton = (Button)findViewById(R.id.updatebutton);

        uploadedimage = (ImageView)findViewById(R.id.uploadedimage);

        uploadimage.setOnClickListener(this);
        updatebutton.setOnClickListener(this);








    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.uploadimage:
                selectImage();
                break;

            case R.id.updatebutton:
                uploadImage();
                break;
        }

    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMG_REQUEST && resultCode == RESULT_OK && data!=null){
            path = data.getData();
            //getRealPathFromURI(path);
            //Toast.makeText(HomeActivity.this, getRealPathFromURI(path),Toast.LENGTH_LONG).show();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                uploadedimage.setImageBitmap(bitmap);
                uploadedimage.setVisibility(View.VISIBLE);
                imagename.setVisibility(View.VISIBLE);



            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



    private void uploadImage(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UploadUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.d(TAG,response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        final String stringresponse = jsonObject.getString("response");

                        builder = new AlertDialog.Builder(AddPlants.this);
                        builder.setTitle("Message");
                        builder.setMessage(stringresponse);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(AddPlants.this,HomeActivity.class);
                                startActivity(intent);


                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("image",imageToString(bitmap));
                    params.put("name",imagename.getText().toString().trim());
                    params.put("commonname",commonname.getText().toString().trim());
                    params.put("sname",sname.getText().toString().trim());
                    params.put("mname",mname.getText().toString().trim());
                    params.put("desc",desc.getText().toString().trim());
                    params.put("usages",usages.getText().toString().trim());
                    params.put("amount",amount.getText().toString().trim());
                    return params;
                }
            };

            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);









    }


    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgbyte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgbyte,Base64.DEFAULT);

    }









}
