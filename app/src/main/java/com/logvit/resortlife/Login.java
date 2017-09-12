package com.logvit.resortlife;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class Login extends AppCompatActivity {
    private String serverUrl = "http://resortlife.com.mx/webservices/r3s0rtl1f3.php";
    private Button btnIniciarSesion;
    private EditText usuario;
    private EditText contrasena;
    private Button signIn;
    public SharedPreferences datosPersistentes;
    public JSONObject respuesta;
    private String id;
    private String correo;
    private String username;
    private Long userID;
    private String UserName;
    JSONObject res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuario = (EditText) this.findViewById(R.id.emailTxt);
        contrasena = (EditText) this.findViewById(R.id.passwordTxt);
        signIn = (Button) this.findViewById(R.id.signin);
        contrasena.setOnKeyListener(new EditText.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
                        keyCode == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    if (!event.isShiftPressed()) {
                        Log.v("AndroidEnterKeyActivity","Enter Key Pressed!");
                        String enteredUsername = usuario.getText().toString();
                        String enteredPassword = contrasena.getText().toString();

                        if (enteredUsername.equals("")  ){
                            Toast.makeText(Login.this, "No pueden existir campos vacios", Toast.LENGTH_LONG).show();
                            return true;
                        }

                        //autentificacion con el servidor remoto
                        new AsyncLogin().execute("login");

                        return true;
                    }

                }
                return false; // pass on to other listeners.
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredUsername = usuario.getText().toString();
                String enteredPassword = contrasena.getText().toString();

                if (enteredUsername.equals("")) {
                    Toast.makeText(Login.this, "No pueden existir campos vacios", Toast.LENGTH_LONG).show();
                }

                //autentificacion con el servidor remoto
                new AsyncLogin().execute("login");

            }


        });

        //vemos si tenemos guardado nombre de usuario y/o contraseña, en caso de que esten guardados, los mostramos
        datosPersistentes = getSharedPreferences("H0t3l1f3", Context.MODE_PRIVATE);
        String usuarioguardado = datosPersistentes.getString("usrH0t3l1f3", "");
        String passguardado = datosPersistentes.getString("passH0t3l1f3", "");

        //si ya hay datos guardados, se muestran en pantalla
        if (usuarioguardado.length() > 0 && passguardado.length() > 0) {
            usuario.setText(usuarioguardado);
            contrasena.setText(passguardado);
        }
    }

    private JSONObject conectar(){
        String enteredUsername = usuario.getText().toString();
        String enteredPassword = contrasena.getText().toString();
        JSONData conexion = new JSONData();
        JSONObject respuesta = null;

        try {
            respuesta = conexion.conexionServidor(serverUrl, "action=login&usuario=" + enteredUsername);
            Log.d("url: ","action=login&usuario=" + enteredUsername);
            if (respuesta.getString("success").equals("OK")) {

                SharedPreferences.Editor editarDatosPersistentes = datosPersistentes.edit();
                editarDatosPersistentes.putString("usrH0t3l1f3", enteredUsername);
//                editarDatosPersistentes.putString("idhotelH0t3l1f3", respuesta.getString("idhotel"));
                editarDatosPersistentes.putString("idusrH0t3l1f3", respuesta.getString("idusuario"));
                editarDatosPersistentes.putString("nombreH0t3l1f3",respuesta.getString("nombre"));

                editarDatosPersistentes.apply();

            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return respuesta;
    }





    public class AsyncLogin extends AsyncTask<String, String, String> {

        public AsyncLogin() {
            //set context variables if required
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... params) {

            //String urlString = params[0]; // URL to call

            String resultToDisplay = "";

            InputStream in = null;
            try {
                switch (params[0]) {
                    case "login":

                        res = conectar();
                        return res.getString("success");
                }

            } catch (Exception e) {

                System.out.println(e.getMessage());

                return e.getMessage();

            }

//            try {
//                resultToDisplay = IOUtils.toString(in, "UTF-8");
//                //to [convert][1] byte stream to a string
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            try {
                return res.getString("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "Error";
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("OK")) {
                Intent intent = new Intent(Login.this, Landing.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(Login.this, result, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
