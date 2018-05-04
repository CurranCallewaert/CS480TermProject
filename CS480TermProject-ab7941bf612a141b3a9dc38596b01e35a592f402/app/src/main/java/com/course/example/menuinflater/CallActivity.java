package com.course.example.menuinflater;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.content.Intent;

public class CallActivity extends Activity {

    private EditText callWho;
    private EditText number;
    private CheckBox check1;
    private Button add;
    private Button cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        callWho= (EditText) findViewById(R.id.callWho);
        number= (EditText) findViewById(R.id.number);
        check1 = (CheckBox) findViewById(R.id.checkbox1);
        add = (Button) findViewById(R.id.button1);
        cancel = (Button) findViewById(R.id.cancel1);

        add.setOnClickListener(new View.OnClickListener() { //controls what to do when a list item is added
            @Override
            public void onClick(View view) {

                String who = callWho.getText().toString();
                String num = number.getText().toString();
                boolean check = check1.isChecked();


                Toast.makeText(getApplicationContext(),"Added!",Toast.LENGTH_SHORT).show(); //get rid of this later
                Intent intent = new Intent(getApplicationContext(),MenuInflaterActivity.class);
                intent.putExtra("who", ""+who);
                intent.putExtra("num", ""+num);
                intent.putExtra("check", check);
                intent.putExtra("type","Call: ");
                startActivity(intent);

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MenuInflaterActivity.class);
                startActivity(intent);

            }
        });
    }

}
