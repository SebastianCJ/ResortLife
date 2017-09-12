package com.logvit.resortlife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;


public class Menu extends AppCompatActivity {
    private ImageView btnTaxi;
    private ImageView btnChat;
    private ImageView btnIdioma;
    private ImageView btnLanding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        btnTaxi = (ImageView) this.findViewById(R.id.btnTaxi);
        btnChat = (ImageView) this.findViewById(R.id.btnChat);
        btnIdioma = (ImageView) this.findViewById(R.id.btnIdioma);
        btnLanding = (ImageView) this.findViewById(R.id.btnLanding);


        btnLanding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, Landing.class);
                startActivity(intent);

            }


        });

        btnTaxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, TaxiInicio.class);
                startActivity(intent);
            }


        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent myIntent = new Intent(Menu.this, ChatSDKLoginActivity.class);
//                startActivity(myIntent);
            }


        });


        btnIdioma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }


        });

    }
}
