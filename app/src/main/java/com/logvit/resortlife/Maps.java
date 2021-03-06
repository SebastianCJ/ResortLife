package com.logvit.resortlife;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Maps extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerClickListener,
        LocationListener {

    private GoogleMap mMap;
    private ImageView btnLanding;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    boolean CRDS_CALL = false;
    public SharedPreferences datosPersistentes;
    private static ArrayList<Point> props = new ArrayList<>();
    Map <String, Bitmap> imagenes = new HashMap<>();
    private ArrayList<Marker> arrayMarcadores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnLanding = (ImageView) findViewById(R.id.btnLanding);


        btnLanding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Maps.this, Landing.class);
                startActivity(intent);
            }


        });

    }

    public void desconectarWifi(){
        int wifiId = datosPersistentes.getInt("netIDS1t10w1f1",0);
        System.out.println("WIFIID MAPS:" + wifiId);
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifi.removeNetwork(wifiId);
        wifi.disableNetwork(wifiId);
        wifi.disconnect();
        wifi.saveConfiguration();
    }

    public static ArrayList<Point> returnList()
    {
        return(props);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override @SuppressWarnings("deprecation")
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setPadding(50,150,0,0);
        //mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnMarkerClickListener(this);

        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startRepeatingTask();
//
//            }
//        }, 30000);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

//    private final static int INTERVAL = 1000 * 60 * 3; //30 minutes
//    Handler mHandler = new Handler();
//
//    Runnable mHandlerTask = new Runnable()
//    {
//        @Override
//        public void run() {
//            Random rand = new Random();
//            Intent myIntent;
//            int n = rand.nextInt(30);
//            if (n < 10) {
//                myIntent = new Intent(Maps.this, Videos.class);
//                startActivity(myIntent);
//            }
//            if(n >= 10 && n <= 20) {
//                myIntent = new Intent(Maps.this, Encuestas.class);
//                startActivity(myIntent);
//            }
//            if(n > 20){
//                myIntent = new Intent(Maps.this, Banner.class);
//                startActivity(myIntent);
//            }
//            mHandler.postDelayed(mHandlerTask, INTERVAL);
//        }
//    };
//
//    public void startRepeatingTask()
//    {
//        mHandlerTask.run();
//    }

    @Override
    public void onConnected(Bundle bundle) {

        // Get last known recent location.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        // Note that this can be NULL if last location isn't already known.
        if (mLastLocation != null) {
            mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()) ,4) );
//            SharedPreferences.Editor editarDatosPersistentes = datosPersistentes.edit();
//            putDouble(editarDatosPersistentes,"latitudS1t10w1f1",mLastLocation.getLatitude());
//            putDouble(editarDatosPersistentes,"longitudS1t10w1f1",mLastLocation.getLongitude());
//            editarDatosPersistentes.apply();

        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }

    public double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

//    public Bitmap resizeMapIcons(int width, int height){
//        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.marker);
//        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
//    }

    private ArrayList<Point> crearMarcadores(){

        JSONData conexion = new JSONData();
        JSONObject respuesta;
        try {
            String serverUrl = "http://distro.mx/SitioWifi/s1t10w1f1.php";
            Log.d("MARCADORES: ","action=marcadores&latitud="+mLastLocation.getLatitude()+"&longitud="+mLastLocation.getLongitude());
            respuesta = conexion.conexionServidor(serverUrl, "action=marcadores&latitud="+mLastLocation.getLatitude()+"&longitud="+mLastLocation.getLongitude());
            if (respuesta.getString("success").equals("OK")) {

                JSONArray marcadores = respuesta.getJSONArray("marcadores");

                int i = 0;
                props = new ArrayList<>();
                imagenes.clear();
                while (i < marcadores.length()) {
                    JSONObject marcador = marcadores.getJSONObject(i);

                    LatLng latLng = new LatLng(marcador.getDouble("latitud"), marcador.getDouble("longitud"));
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(marcador.getString("nombre"));
                    markerOptions.snippet(marcador.getString("detalle"));

                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.iconmarker));
                    Marker marker = mMap.addMarker(markerOptions);
                    String remotePath = "http://distro.mx/SitioWifi/imagenes/" + marcador.getString("imagen") + ".png";
                    Bitmap myBitMap = getBitmapFromURL(remotePath);

                    arrayMarcadores.add(marker);
                    imagenes.put(marcador.getString("nombre"), myBitMap);

                    props.add(new Point(marcador.getDouble("latitud"), marcador.getDouble("longitud"), marcador.getString("nombre")));
                    i++;

                }
                return props;

            } else {
                Toast.makeText(Maps.this, respuesta.getString("success"), Toast.LENGTH_LONG).show();
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void borrarMarcadores(){
        for (int i=0;i < arrayMarcadores.size();i++){
            arrayMarcadores.get(i).remove();
        }
        arrayMarcadores.clear();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        if (!marker.getTitle().equals("404")) {
            LatLng posicion = marker.getPosition();
            marker.hideInfoWindow();
            final Dialog dialog = new Dialog(Maps.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.activity_ubicacion);

            double distancia = SphericalUtil.computeDistanceBetween(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), posicion);

            TextView dist = (TextView) dialog.findViewById(R.id.distanciaMts);
            TextView detalle = (TextView) dialog.findViewById(R.id.detalle);
            ImageView imagenUbicacion = (ImageView) dialog.findViewById(R.id.imagenUbicacion);

            int distanciaInt = (int) Math.round(distancia);
            String distTxt = "Distancia: " + distanciaInt + " Mts";
            dist.setText(distTxt);
            detalle.setText(marker.getSnippet());
            Bitmap imagen = imagenes.get(marker.getTitle());
            imagenUbicacion.setImageBitmap(imagen);

            Button dialogButton = (Button) dialog.findViewById(R.id.btn_regresar);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
        return true;
    }




    public Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            InputStream input = connection.getInputStream();
            options.inSampleSize = calculateInSampleSize(options, 50, 50);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(input,null,options);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

//    public LatLng getPosicion(){
//        double latitud = getDouble(datosPersistentes,"latitudS1t10w1f1",0);
//        double longitud = getDouble(datosPersistentes,"longitudS1t10w1f1",0);
//
//        return new LatLng(latitud,longitud);
//    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
//
//        LatLng posicion = getPosicion();
//        distancia = SphericalUtil.computeDistanceBetween(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), posicion);
//
//        if (distancia >= 1000){
//            SharedPreferences.Editor editarDatosPersistentes = datosPersistentes.edit();
//            borrarMarcadores();
//            crearMarcadores();
//            putDouble(editarDatosPersistentes,"latitudS1t10w1f1",mLastLocation.getLatitude());
//            putDouble(editarDatosPersistentes,"longitudS1t10w1f1",mLastLocation.getLongitude());
//            editarDatosPersistentes.apply();
//
//        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if(!CRDS_CALL){
            mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            borrarMarcadores();
            crearMarcadores();
            CRDS_CALL = true;
        }


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }


//    @Override
//    public void onDestroy(){
//        super.onDestroy();
//        desconectarWifi();
//
//    }
}

