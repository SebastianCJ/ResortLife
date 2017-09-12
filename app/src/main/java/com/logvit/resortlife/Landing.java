package com.logvit.resortlife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class Landing extends AppCompatActivity {
    private ImageView btnMenuOpen;
    private ImageView btnMenuClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        ImageView btnHome = (ImageView) this.findViewById(R.id.btnHome);
        ImageView btnGuia = (ImageView) this.findViewById(R.id.btnGuide);
        ImageView btnMessages = (ImageView) this.findViewById(R.id.btnMessages);
        ImageView btnSos = (ImageView) this.findViewById(R.id.btnSos);
        ImageView btnTransport = (ImageView) this.findViewById(R.id.btnTransport);
        btnMenuOpen = (ImageView) this.findViewById(R.id.btnMenuOpen);
        btnMenuClose = (ImageView) this.findViewById(R.id.btnMenuClose);

        btnMenuOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Landing.this, Menu.class);
//                startActivity(intent);
                btnMenuOpen.setVisibility(View.GONE);
                btnMenuClose.setVisibility(View.VISIBLE);
            }


        });

        btnMenuClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Landing.this, Menu.class);
//                startActivity(intent);
                btnMenuOpen.setVisibility(View.VISIBLE);
                btnMenuClose.setVisibility(View.GONE);
            }


        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Landing.this, Home.class);
                startActivity(intent);
            }


        });

        btnGuia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Landing.this, CityGuide.class);
                startActivity(intent);
            }


        });

        btnMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }


        });

        btnTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Landing.this, TaxiInicio.class);
                startActivity(intent);
            }


        });

        btnSos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Landing.this, Sos.class);
                startActivity(intent);
            }


        });
    }
}

