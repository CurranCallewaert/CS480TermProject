package com.course.example.menuinflater;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

public class AddressActivity extends Activity {

    private EditText visitWho;
    private EditText address;
    private EditText city;
    private EditText state;
    private CheckBox check4;
    private Button add4;
    private Button cancel4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        visitWho = (EditText) findViewById(R.id.visitWho);
        address = (EditText) findViewById(R.id.address);
        city = (EditText) findViewById(R.id.city);
        state = (EditText) findViewById(R.id.state);
        check4 = (CheckBox) findViewById(R.id.checkbox4);
        add4 = (Button) findViewById(R.id.button4);
        cancel4 = (Button) findViewById(R.id.cancel4);

        add4.setOnClickListener(new View.OnClickListener() { //controls what to do when a list item is added
            @Override
            public void onClick(View view) {

                //String stat = state.getText().toString().substring(0,2);

                String who = visitWho.getText().toString();
                String num = address.getText().toString() + " "+city.getText().toString()+" "+state.getText().toString();
                boolean check = check4.isChecked();


                Toast.makeText(getApplicationContext(),"Added!",Toast.LENGTH_SHORT).show(); //get rid of this later
                Intent intent2 = new Intent(getApplicationContext(),MenuInflaterActivity.class);
                intent2.putExtra("who", ""+who);
                intent2.putExtra("num", ""+num);
                intent2.putExtra("check", check);
                intent2.putExtra("type","Visit: ");
                startActivity(intent2);

            }
        });

        cancel4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MenuInflaterActivity.class);
                startActivity(intent);

            }
        });
    }
}

