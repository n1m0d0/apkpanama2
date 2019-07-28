package gesport.xpertise.com.gesportapk;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    EditText etUser, etPassword;
    TextView btnLogin, btnForgotPassword;
    Toast msj;
    Intent ir;
    ImageView logo;
    ProgressDialog mProgressDialog;
    RequestQueue mRequestQueue;
    JsonObjectRequest mJsonObjectRequest;
    String url;
    String credentials;
    String auth;
    int code;
    String fullName;
    String direccion;
    String certificado = "";
    String idOffline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        url = getString(R.string.servidor) + "/api/authUser";
        direccion = getString(R.string.servidor) + "/api/saveEvent";

        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);
        logo = findViewById(R.id.img);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        btnForgotPassword.setOnClickListener(this);

        validarPermisos();

        try {
            verificarSesion();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(compruebaConexion(this)) {

            enviarFormularios();

        }

    }

    public void logo(View v) {

                Uri uri = Uri.parse("https://www.portcolon2000.com/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btnLogin:

                if (etUser.getText().toString().trim().equalsIgnoreCase("") || etPassword.getText().toString().trim().equalsIgnoreCase("")) {

                    msj = Toast.makeText(this, "debe coompletar los datos", Toast.LENGTH_LONG);
                    msj.show();
                }
                else {

                    if (!validarEmail(etUser.getText().toString())) {

                        msj = Toast.makeText(this,"la direcion de correo no es valida", Toast.LENGTH_LONG);
                        msj.show();

                    }
                    else {

                        //llamar la funcion de login

                        String password = encryptPassword(etPassword.getText().toString().trim());

                        credentials = etUser.getText().toString().trim()+":"+password;
                        auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

                        if(compruebaConexion(this)) {

                            cargarLogin();

                        } else {

                            msj = Toast.makeText(this, "Sin conexion a Internet", Toast.LENGTH_LONG);
                            msj.setGravity(Gravity.CENTER, 0, 0);
                            msj.show();

                            try {

                                bd conexion = new bd(this);
                                conexion.abrir();
                                Cursor usuario = conexion.login(etUser.getText().toString().trim(), auth);
                                if (usuario.moveToFirst() == false) {

                                    msj = Toast.makeText(this, "Usuario o Clave incorrecto!!", Toast.LENGTH_LONG);
                                    msj.setGravity(Gravity.CENTER, 0, 0);
                                    msj.show();

                                } else {

                                    conexion.createSession(etUser.getText().toString().trim());
                                    ir = new Intent(this, events.class);
                                    ir.putExtra("auth", auth);
                                    ir.putExtra("userName", etUser.getText().toString().trim());
                                    ir.putExtra("fullName", usuario.getString(3));
                                    startActivity(ir);

                                }
                                conexion.cerrar();

                            } catch (Exception e) {

                                e.printStackTrace();

                            }

                        }



                    }

                }

                break;

            case R.id.btnForgotPassword:

                Intent ir = new Intent(this, forgotPassword.class);
                startActivity(ir);

                break;

        }

    }

    private boolean validarEmail(String email) {

        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();

    }

    private void cargarLogin(){

        mProgressDialog =  new ProgressDialog(this);
        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.show();

        mRequestQueue = Volley.newRequestQueue(this);

        mJsonObjectRequest = new JsonObjectRequest(Request.Method.POST,url,null, this, this){

            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Authorization", auth); //authentication
                return headers;
            }

        };

        mRequestQueue.add(mJsonObjectRequest);

    }

    @Override
    public void onErrorResponse(VolleyError error) {

        mProgressDialog.dismiss();
        msj = Toast.makeText(this, "Ocurrio un Error: " + error, Toast.LENGTH_LONG);
        msj.show();

    }

    @Override
    public void onResponse(JSONObject response) {

        mProgressDialog.dismiss();

        try {
            code = response.getInt("code");
            fullName = response.getString("nameComplete");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(code == 0) {

            msj = Toast.makeText(this, "Bienvenido!!", Toast.LENGTH_LONG);
            msj.setGravity(Gravity.CENTER, 0, 0);
            msj.show();

            try {

                bd conexion = new bd(this);
                conexion.abrir();
                Cursor usuario = conexion.searchUser(etUser.getText().toString().trim());
                if (usuario.moveToFirst() == false) {

                    conexion.createUser(etUser.getText().toString().trim(), auth, fullName);
                    conexion.createSession(etUser.getText().toString().trim());

                }
                else {

                    conexion.updateUser(etUser.getText().toString().trim(), auth);
                    conexion.createSession(etUser.getText().toString().trim());

                }

                conexion.cerrar();

            } catch (Exception e) {

                e.printStackTrace();

            }

            ir = new Intent(this, events.class);
            ir.putExtra("auth", auth);
            ir.putExtra("userName", etUser.getText().toString().trim());
            ir.putExtra("fullName", fullName);
            startActivity(ir);

        } else {

            msj = Toast.makeText(this, "Usuario o Clave incorrecto!!", Toast.LENGTH_LONG);
            msj.setGravity(Gravity.CENTER, 0, 0);
            msj.show();

        }

    }

    private static String encryptPassword(String password)
    {
        String sha1 = "";
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return sha1;
    }

    private static String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static boolean compruebaConexion(Context context) {

        boolean connected = false;

        ConnectivityManager connec = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // Recupera todas las redes (tanto móviles como wifi)
        NetworkInfo[] redes = connec.getAllNetworkInfo();

        for (int i = 0; i < redes.length; i++) {
            // Si alguna red tiene conexión, se devuelve true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                connected = true;
            }
        }
        return connected;
    }


    /******************/
    private boolean validarPermisos(){

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

            return true;

        }
        if((checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)){

            return true;

        }

        if ((shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) || (shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION)) || (shouldShowRequestPermissionRationale(CAMERA)) || (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))  || (shouldShowRequestPermissionRationale(RECORD_AUDIO))) {

            cargardialogo();

        }
        else {

            requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, WRITE_EXTERNAL_STORAGE, CAMERA, RECORD_AUDIO}, 100);

        }

        return false;

    }

    private void cargardialogo(){

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Permisos Desactivados");
        builder.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, WRITE_EXTERNAL_STORAGE, CAMERA, RECORD_AUDIO}, 100);
                }

            }
        });
        builder.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100) {

            if(grantResults.length == 5 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED  && grantResults[4] == PackageManager.PERMISSION_GRANTED) {



            } else {

                cargardialogo2();

            }

        }

    }

    private void cargardialogo2(){

        final CharSequence[] op = {"si", "no"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Desea configurar los permisos manualmente?");
        builder.setItems(op, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(op[which].equals("si")){

                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent .setData(uri);
                    startActivity(intent);

                }
                else {

                    msj = Toast.makeText(MainActivity.this, "los permisos no fueron aceptados", Toast.LENGTH_LONG);
                    msj.show();
                    dialog.dismiss();

                }

            }
        });
        builder.show();

    }

    private void enviarFormularios(){

        mProgressDialog =  new ProgressDialog(this);
        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        try {
            final bd conexion = new bd(this);
            conexion.abrir();
            final Cursor offline = conexion.searchActive();
            if (!offline.moveToFirst() == false) {

                for (offline.moveToFirst(); !offline.isAfterLast(); offline.moveToNext()) {

                    mRequestQueue = Volley.newRequestQueue(this);

                    idOffline = offline.getString(0);
                    String jsonAnswers = offline.getString(1);
                    String user = offline.getString(2);
                    certificado = offline.getString(3);

                    Log.w("data", certificado + " " + idOffline);

                    String dataJson = readJsonFileAnswer(jsonAnswers);

                    JSONObject respuesta = new JSONObject(dataJson);

                    Log.w("data", " " + respuesta);

                    mJsonObjectRequest = new JsonObjectRequest(Request.Method.POST, direccion, respuesta, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.w("mio", "" + response);

                            //conexion.updateAnswers(idOffline);
                            bd conexion2 = new bd(MainActivity.this);
                            try {
                                conexion2.abrir();
                                conexion2.updateMyAnswers(idOffline);
                                conexion2.cerrar();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.w("mioerror", "" + error);

                        }
                    }){

                        @Override
                        public Map getHeaders() throws AuthFailureError {
                            HashMap headers = new HashMap();
                            headers.put("Authorization", certificado); //authentication
                            return headers;
                        }

                    };

                    mRequestQueue.add(mJsonObjectRequest);

                }
                conexion.cerrar();

            }
        } catch (Exception e) {
            e.printStackTrace();
            mProgressDialog.dismiss();
        }

        mProgressDialog.dismiss();

    }

    public String readJsonFileAnswer (String path) {

        Log.w("ver", path);

        String jsonAnswer = null;

        String json = null;
        try {
            File f = new File(path);
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(f)));

            jsonAnswer = fin.readLine();
            fin.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return jsonAnswer;
    }

    public void verificarSesion() throws Exception {

        bd conexion = new bd(this);
        conexion.abrir();
        Cursor cursor = conexion.searchSessionActive();
        if (cursor.moveToFirst() != false) {

            Log.w("name", cursor.getString(1));
            Cursor cursor2 = conexion.searchUser(cursor.getString(1));
            cursor2.moveToFirst();
            Log.w("tag", cursor2.getString(1));
            ir = new Intent(this, events.class);
            ir.putExtra("auth", cursor2.getString(2));
            ir.putExtra("userName", cursor2.getString(1));
            ir.putExtra("fullName", cursor2.getString(3));
            startActivity(ir);
            finish();

        }

    }

}
