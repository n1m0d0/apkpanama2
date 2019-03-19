package gesport.xpertise.com.gesportapk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class forms extends AppCompatActivity implements Response.Listener<JSONArray>, Response.ErrorListener {

    GridView gvForms;
    String id_form;
    ArrayList<obj_form> itemForms = new ArrayList<obj_form>();
    Toast msj;
    String auth;
    String userName;
    ProgressDialog mProgressDialog;
    RequestQueue mRequestQueue;
    JsonArrayRequest mJsonArrayRequest;
    String url = "https://test.portcolon2000.site/api/parForm";
    Intent ir;

    int idForm;
    String colorForm;
    String descriptionForm;
    String idIconForm;
    int positionForm;
    int typeDependency;
    String fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forms);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        gvForms = findViewById(R.id.gvForms);

        Bundle parametros = this.getIntent().getExtras();
        auth = parametros.getString("auth");
        userName = parametros.getString("userName");
        fullName = parametros.getString("fullName");

        if(compruebaConexion(this)) {

            cargarFormularios();

        } else {

            cargarFormulariosOffline();

        }

        //funcionalidad a la lista
        gvForms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                obj_form elegido = (obj_form) parent.getItemAtPosition(position);

                // recuperamos el id
                id_form = "" + elegido.getId();
                /*msj = Toast.makeText(forms.this, "Formulario elegido elegido: " + id_form, Toast.LENGTH_LONG);
                msj.show();*/
                // llamar a la funcion para ver el formulario
                ir = new Intent(forms.this, form_event.class);
                ir.putExtra("auth", auth);
                ir.putExtra("userName", userName);
                ir.putExtra("idForm", id_form);
                ir.putExtra("fullName", fullName);
                startActivity(ir);

            }
        });

    }

    private void cargarFormularios(){

        mProgressDialog =  new ProgressDialog(this);
        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.show();

        mRequestQueue = Volley.newRequestQueue(this);

        mJsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url,null, this, this){

            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Authorization", auth); //authentication
                return headers;
            }

        };

        mRequestQueue.add(mJsonArrayRequest);

    }

    @Override
    public void onResponse(JSONArray response) {

        /*msj = Toast.makeText(this, "" + response, Toast.LENGTH_LONG);
        msj.show();*/
        mProgressDialog.dismiss();

        try {
            createJson(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{

            for(int i=0;i<response.length();i++) {

                JSONObject form = response.getJSONObject(i);

                idForm = form.getInt("idForm");
                colorForm = form.getString("colorForm");
                descriptionForm = form.getString("descriptionForm");
                idIconForm = form.getString("idIconForm");
                positionForm = form.getInt("positionForm");
                typeDependency = form.getInt("typeDependency");

                itemForms.add(new obj_form(idForm, colorForm, descriptionForm, idIconForm, positionForm, typeDependency));

            }

            adapter_forms adapter = new adapter_forms(forms.this, itemForms);
            gvForms.setAdapter(adapter);

        }catch (JSONException e) {

            e.printStackTrace();
            msj = Toast.makeText(this, "No hay datos para mostrar", Toast.LENGTH_LONG);
            msj.setGravity(Gravity.CENTER, 0, 0);
            msj.show();

            Log.w("respuesta", "" + e);

        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {

        mProgressDialog.dismiss();
        /*msj = Toast.makeText(this, "Ocurrio un Error: " + error, Toast.LENGTH_LONG);
        msj.show();*/

        Log.w("respuesta", "" + error);

    }

    @Override
    public void onBackPressed(){

        ir = new Intent(forms.this, events.class);
        ir.putExtra("auth", auth);
        ir.putExtra("userName", userName);
        ir.putExtra("fullName", fullName);
        startActivity(ir);
        finish();

    }

    public void cargarFormulariosOffline() {

        try {
            String forms = readJsonFile("/storage/emulated/0/geoport/forms.json");
            if (forms == null) {

                msj = Toast.makeText(this, "No hay datos para mostrar", Toast.LENGTH_LONG);
                msj.setGravity(Gravity.CENTER, 0, 0);
                msj.show();

            } else {

                try{

                    JSONArray response = new JSONArray(forms);

                    for(int i=0;i<response.length();i++) {

                        JSONObject form = response.getJSONObject(i);

                        idForm = form.getInt("idForm");
                        colorForm = form.getString("colorForm");
                        descriptionForm = form.getString("descriptionForm");
                        idIconForm = form.getString("idIconForm");
                        positionForm = form.getInt("positionForm");
                        typeDependency = form.getInt("typeDependency");

                        itemForms.add(new obj_form(idForm, colorForm, descriptionForm, idIconForm, positionForm, typeDependency));

                    }

                    adapter_forms adapter = new adapter_forms(forms.this, itemForms);
                    gvForms.setAdapter(adapter);

                }catch (JSONException e) {

                    e.printStackTrace();
                    Log.w("respuesta", "" + e);

                }

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

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

    public void createJson(JSONArray jsonArray) {

        String path = null;
        String carpeta = "geoport";
        File fileJson = new File(Environment.getExternalStorageDirectory(), carpeta);
        boolean isCreada = fileJson.exists();
        String nombreJson = "";

        if(isCreada == false) {

            isCreada = fileJson.mkdir();

        }

        if(isCreada == true) {

            nombreJson = "forms.json";

        }

        path = Environment.getExternalStorageDirectory() + File.separator + carpeta + File.separator + nombreJson;


        try {
            FileWriter writer = new FileWriter(path);
            writer.write(String.valueOf(jsonArray));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String readJsonFile (String path) {

        Log.w("ver", path);

        String jsoneforms = null;

        String json = null;
        try {
            File f = new File(path);
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(f)));

            jsoneforms = fin.readLine();
            fin.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return jsoneforms;
    }

}
