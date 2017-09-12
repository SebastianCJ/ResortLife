package com.logvit.resortlife;

/**
 * Created by Gatsu on 9/11/2017.
 */
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class AdapterGuide extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] nombres;
    ArrayList<Bitmap> imagenes;

    static class ViewHolder {
        public TextView nombreTxt;
        public ImageView image;
    }

    public AdapterGuide(Activity context, String[] nombres, ArrayList<Bitmap> imagenes) {
        super(context, R.layout.guide_layout, nombres);
        this.context = context;
        this.nombres = nombres;
        this.imagenes = imagenes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.guide_layout, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.nombreTxt = (TextView) rowView.findViewById(R.id.guideTxt);

            viewHolder.image = (ImageView) rowView
                    .findViewById(R.id.guideimg);
            rowView.setTag(viewHolder);
        }

        // fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();
        String nombre = nombres[position];
        if (imagenes.size()>0) {
            Bitmap imagen = imagenes.get(position);
            holder.image.setImageBitmap(imagen);
        }
        holder.nombreTxt.setText(nombre);


        return rowView;
    }
}
