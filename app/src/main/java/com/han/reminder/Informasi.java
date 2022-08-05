package com.han.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Informasi extends AppCompatActivity {
    TextView btnback;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi);


        btnback = findViewById(R.id.textViewBack);

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent_Home = new Intent(Informasi.this, HomeActivity.class);
                startActivity(Intent_Home);


            }
        });

    }
}