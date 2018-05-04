package com.course.example.menuinflater;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

public class TextActivity extends Activity {

    private EditText textWho;
    private EditText number;
    private CheckBox check2;
    private Button add2;
    private Button cancel2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        textWho = (EditText) findViewById(R.id.textWho);
        number = (EditText) findViewById(R.id.number2);
        check2 = (CheckBox) findViewById(R.id.checkbox2);
        add2 = (Button) findViewById(R.id.button2);
        cancel2 = (Button) findViewById(R.id.cancel2);

        add2.setOnClickListener(new View.OnClickListener() { //controls what to do when a list item is added
            @Override
            public void onClick(View view) {

                String who = textWho.getText().toString();
                String num = number.getText().toString();
                boolean check = check2.isChecked();


                Toast.makeText(getApplicationContext(),"Added!",Toast.LENGTH_SHORT).show(); //get rid of this later
                Intent intent2 = new Intent(getApplicationContext(),MenuInflaterActivity.class);
                intent2.putExtra("who", ""+who);
                intent2.putExtra("num", ""+num);
                intent2.putExtra("check", check);
                intent2.putExtra("type","Text: ");
                startActivity(intent2);

            }
        });

        cancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MenuInflaterActivity.class);
                startActivity(intent);

            }
        });
    }
}
