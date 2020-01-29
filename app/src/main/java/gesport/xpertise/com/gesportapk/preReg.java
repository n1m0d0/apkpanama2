package gesport.xpertise.com.gesportapk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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

public class preReg extends AppCompatActivity {
    String auth, userName, fullName;
    ProgressDialog mProgressDialog;
    ListView lvReg;
    ArrayList<obj_preReg> preRegArrayList = new ArrayList<>();
    adapter_preReg adapter;
    Intent ir;
    Toast msj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_reg);

        lvReg = findViewById(R.id.lvReg);

        Bundle parametros = this.getIntent().getExtras();
        auth = parametros.getString("auth");
        userName = parametros.getString("userName");
        fullName = parametros.getString("fullName");

        if (compruebaConexion(this)) {
            registered();
        } else {
            registeredOffLine();
        }

        lvReg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                obj_preReg elegido = (obj_preReg) adapterView.getItemAtPosition(i);

                // recuperamos el id
                Toast msj = Toast.makeText(preReg.this, "Evento elegido: " + elegido.getId(), Toast.LENGTH_LONG);
                msj.show();
                // llamar a la funcion para ver el evento
                /*ir = new Intent(preReg.this, view_event.class);
                ir.putExtra("auth", auth);
                ir.putExtra("userName", userName);
                ir.putExtra("idEvent", elegido.getId());
                ir.putExtra("fullName", fullName);
                startActivity(ir);
                finish();*/
            }
        });
    }

    public void registered() {

        String url = getString(R.string.servidor) + "/api/parFormPre";

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.show();

        RequestQueue mRequestQueue;
        JsonArrayRequest jsonArrayRequest;

        mRequestQueue = Volley.newRequestQueue(this);

        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.w("MyResponse", response.toString());

                try {
                    createJson(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject form = response.getJSONObject(i);
                        int idFormPre = form.getInt("idFormPre");
                        String color = form.getString("color");
                        String description = form.getString("description");
                        String icon = form.getString("icon");
                        preRegArrayList.add(new obj_preReg(idFormPre, color, description, icon));
                    }
                    adapter = new adapter_preReg(preReg.this, preRegArrayList);
                    lvReg.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mProgressDialog.cancel();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("MyError", error.toString());
                mProgressDialog.cancel();
            }
        }) {

            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Authorization", auth); //authentication
                return headers;
            }

        };

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(jsonArrayRequest);
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

        if (isCreada == false) {

            isCreada = fileJson.mkdir();

        }

        if (isCreada == true) {

            nombreJson = "formPreReg.json";

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

    public String readJsonFile(String path) {

        Log.w("ver", path);

        String jsonevents = null;

        String json = null;
        try {
            File f = new File(path);
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(f)));

            jsonevents = fin.readLine();
            fin.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return jsonevents;
    }

    public void registeredOffLine() {
        try {
            String formPreReg = readJsonFile("/storage/emulated/0/geoport/formPreReg.json");
            if (formPreReg == null) {

                msj = Toast.makeText(this, "No hay datos para mostrar", Toast.LENGTH_LONG);
                msj.setGravity(Gravity.CENTER, 0, 0);
                msj.show();

            } else {

                try {

                    JSONArray response = new JSONArray(formPreReg);

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject form = response.getJSONObject(i);
                        int idFormPre = form.getInt("idFormPre");
                        String color = form.getString("color");
                        String description = form.getString("description");
                        String icon = form.getString("icon");
                        preRegArrayList.add(new obj_preReg(idFormPre, color, description, icon));
                    }
                    adapter = new adapter_preReg(preReg.this, preRegArrayList);
                    lvReg.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}
