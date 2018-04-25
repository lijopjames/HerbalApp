package com.example.android.herbalapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Sales extends AppCompatActivity {

    TextView name,price,addedamount;
    Spinner ptype,quanitity;
    Button button;
    String[] producttype = new String[] {"Farm Fresh", "Dried", "Dried & Powdered"};
    Integer[] amountproduct = new Integer[]{ 100,500,1000 };
    String productname,productprice,finalresult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        name = (TextView)findViewById(R.id.datefrom);
        price = (TextView)findViewById(R.id.price);
        addedamount = (TextView)findViewById(R.id.addedamount);
        ptype = (Spinner)findViewById(R.id.ptype);
        quanitity = (Spinner)findViewById(R.id.quanitity);
        button = (Button)findViewById(R.id.button);
        productname = getIntent().getStringExtra("productname");
        name.setText(productname);
        productprice = getIntent().getStringExtra("productprice");
        price.setText(productprice);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Sales.this,
                android.R.layout.simple_spinner_item, producttype);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ptype.setAdapter(adapter);

        ArrayAdapter<Integer> adapter2 = new ArrayAdapter<Integer>(Sales.this,
                android.R.layout.simple_spinner_item, amountproduct);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quanitity.setAdapter(adapter2);

        quanitity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected= quanitity.getSelectedItem().toString();
                int result = Integer.parseInt(selected);
                int productpint = Integer.parseInt(productprice);
                int interresult = (result/100)*productpint;
                finalresult = Integer.toString(interresult);
                addedamount.setText(finalresult);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Sales.this,Billing.class);
                intent.putExtra("pname",productname);
                intent.putExtra("finalamount",finalresult);
                intent.putExtra("ptype",ptype.getSelectedItem().toString());
                startActivity(intent);

            }
        });
    }
}
