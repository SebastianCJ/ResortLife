package com.logvit.resortlife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class TaxiInicio extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi_inicio);

        Button verServicio = (Button) this.findViewById(R.id.taxi);
        ImageView btnHome = (ImageView) this.findViewById(R.id.btn);

        verServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaxiInicio.this, TaxiPedido.class);
                startActivity(intent);

            }


        });


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaxiInicio.this, Menu.class);
                startActivity(intent);
            }


        });
    }
}
