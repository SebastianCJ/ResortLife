package com.logvit.resortlife;

/**
 * Created by Gatsu on 6/28/2017.
 */
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


public class AdapterActividades extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] nombres;
    private final String[] descripciones;
    ArrayList<Bitmap> imagenes;

    static class ViewHolder {
        public TextView nombreTxt;
        public TextView descripcionTxt;
        public ImageView image;
    }

    public AdapterActividades(Activity context, String[] nombres,String[] descripciones , ArrayList<Bitmap> imagenes) {
        super(context, R.layout.actividades_layout, nombres);
        this.context = context;
        this.nombres = nombres;
        this.descripciones = descripciones;
        this.imagenes = imagenes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.actividades_layout, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.nombreTxt = (TextView) rowView.findViewById(R.id.nombreActividad);
            viewHolder.descripcionTxt = (TextView) rowView.findViewById(R.id.descripcionTxt);
            viewHolder.image = (ImageView) rowView
                    .findViewById(R.id.actividadImg);
            rowView.setTag(viewHolder);
        }

        // fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();
        String nombre = nombres[position];
        String descripcion = descripciones[position];
        if (imagenes.size()>0) {
            Bitmap imagen = imagenes.get(position);
            holder.image.setImageBitmap(imagen);
        }
        holder.nombreTxt.setText(nombre);
        holder.descripcionTxt.setText(descripcion);


        return rowView;
    }
}
