package com.example.piyushkashyap.picscholar;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Activity3 extends AppCompatActivity {

    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

//        Intent intent = getIntent();
//        String s = intent.getStringExtra("message");
//        //String message = bundle.getString("message");
//        Log.e("eddd",s);
//        TextView txtView = (TextView) findViewById(R.id.textView2);
//        txtView.setText(s);

        b.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                            Intent i = new Intent();
                            i.setComponent(new ComponentName("com.android.contacts", "com.android.contacts.DialtactsContactsEntryActivity"));
                            i.setAction("android.intent.action.MAIN");
                            i.addCategory("android.intent.category.LAUNCHER");
                            i.addCategory("android.intent.category.DEFAULT");
                            startActivity(i);


                    }
                }
        );
    }
}
