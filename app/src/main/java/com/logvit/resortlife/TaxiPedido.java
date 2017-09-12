package com.logvit.resortlife;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class TaxiPedido extends AppCompatActivity {
    public ImageView startHotel;
    public ImageView startMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi_pedido);

        ImageView btnRegresar = (ImageView) this.findViewById(R.id.btn);
        startHotel = (ImageView) this.findViewById(R.id.startHotel);
        startMap = (ImageView) this.findViewById(R.id.startMap);

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaxiPedido.this, TaxiInicio.class);
                startActivity(intent);

            }


        });

        startHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startHotel.setImageResource(R.drawable. gray_square);
                startMap.setImageResource(R.drawable. gray_squaretwo);

            }


        });

        startMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startHotel.setImageResource(R.drawable. gray_squaretwo);
                startMap.setImageResource(R.drawable. gray_square);


            }


        });



    }
}
