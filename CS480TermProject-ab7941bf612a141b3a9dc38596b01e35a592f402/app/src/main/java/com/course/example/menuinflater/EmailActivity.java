package com.course.example.menuinflater;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

public class EmailActivity extends Activity {

    private EditText emailWho;
    private EditText email;
    private CheckBox check3;
    private Button add3;
    private Button cancel3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        emailWho = (EditText) findViewById(R.id.emailWho);
        email = (EditText) findViewById(R.id.email);
        check3 = (CheckBox) findViewById(R.id.checkbox3);
        add3 = (Button) findViewById(R.id.button3);
        cancel3 = (Button) findViewById(R.id.cancel3);

        add3.setOnClickListener(new View.OnClickListener() { //controls what to do when a list item is added
            @Override
            public void onClick(View view) {

                String who = emailWho.getText().toString();
                String num = email.getText().toString();
                boolean check = check3.isChecked();


                Toast.makeText(getApplicationContext(),"Added!",Toast.LENGTH_SHORT).show(); //get rid of this later
                Intent intent2 = new Intent(getApplicationContext(),MenuInflaterActivity.class);
                intent2.putExtra("who", ""+who);
                intent2.putExtra("num", ""+num);
                intent2.putExtra("check", check);
                intent2.putExtra("type","Email: ");
                startActivity(intent2);

            }
        });

        cancel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MenuInflaterActivity.class);
                startActivity(intent);

            }
        });
    }
}

