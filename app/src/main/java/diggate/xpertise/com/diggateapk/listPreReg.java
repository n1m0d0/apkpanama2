package diggate.xpertise.com.diggateapk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class listPreReg extends AppCompatActivity {
    String auth, userName, fullName;
    int idFromPreReg;
    EditText etSearch;
    ListView lvPreReg;
    String description;
    String colorPreReg;
    String iconPreReg;
    FloatingActionButton fabAdd;
    ProgressDialog mProgressDialog;
    ArrayList<obj_listPreReg> arrayListPreReg = new ArrayList<>();
    ArrayList<obj_listPreReg> arrayListPreReg2 = new ArrayList<>();
    adapter_listPreReg adapter;
    Toast msj;
    Intent ir;

    String branch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pre_reg);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        etSearch = findViewById(R.id.etSearch);
        lvPreReg = findViewById(R.id.lvPreReg);
        fabAdd = findViewById(R.id.fabAdd);

        Bundle parametros = this.getIntent().getExtras();
        auth = parametros.getString("auth");
        userName = parametros.getString("userName");
        fullName = parametros.getString("fullName");
        idFromPreReg = parametros.getInt("idFromPreReg");
        branch = parametros.getString("branch");

        if (compruebaConexion(this)) {
            list();
        } else {
            listOffline();
        }

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                searchItem("" + s);
                Log.w("buscar", "" + s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lvPreReg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                obj_listPreReg elegido = (obj_listPreReg) adapterView.getItemAtPosition(i);

                // recuperamos el id
                //Toast msj = Toast.makeText(listPreReg.this, "Evento elegido: " + elegido.getId(), Toast.LENGTH_LONG);
                //msj.show();
                // llamar a la funcion para ver el evento
                ir = new Intent(listPreReg.this, detailPre.class);
                ir.putExtra("auth", auth);
                ir.putExtra("userName", userName);
                ir.putExtra("idRegPre", elegido.getId());
                ir.putExtra("fullName", fullName);
                ir.putExtra("branch", branch);
                startActivity(ir);
                finish();
            }
        });


        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ir = new Intent(listPreReg.this, parFormFielsPre.class);
                ir.putExtra("auth", auth);
                ir.putExtra("userName", userName);
                ir.putExtra("fullName", fullName);
                ir.putExtra("idFromPreReg", idFromPreReg);
                ir.putExtra("branch", branch);
                startActivity(ir);
                finish();
            }
        });
    }

    public void list() {

        String url = getString(R.string.servidor) + "/api/listPre/" + idFromPreReg;

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.show();

        RequestQueue mRequestQueue;
        JsonObjectRequest jsonArrayRequest;

        mRequestQueue = Volley.newRequestQueue(this);

        jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.w("MyResponse", response.toString());

                try {
                    createJson(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    description = response.getString("description");
                    colorPreReg = response.getString("color");
                    iconPreReg = response.getString("icon");
                    JSONArray registros = response.getJSONArray("R");
                    for (int i = 0; i < registros.length(); i++) {
                        JSONObject item = registros.getJSONObject(i);
                        int idRegPre = item.getInt("idRegPre");
                        String keyValue = item.getString("keyValue");
                        String stringValue = item.getString("stringValue");
                        arrayListPreReg.add(new obj_listPreReg(idRegPre, description, colorPreReg, iconPreReg, keyValue, stringValue));
                        arrayListPreReg2.add(new obj_listPreReg(idRegPre, description, colorPreReg, iconPreReg, keyValue, stringValue));
                    }
                    adapter_listPreReg adapter = new adapter_listPreReg(listPreReg.this, arrayListPreReg);
                    lvPreReg.setAdapter(adapter);

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
                headers.put("Branch", branch); //rama
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

    public void createJson(JSONObject jsonArray) {

        String path = null;
        String carpeta = "DigGate";
        File fileJson = new File(Environment.getExternalStorageDirectory(), carpeta);
        boolean isCreada = fileJson.exists();
        String nombreJson = "";

        if (isCreada == false) {

            isCreada = fileJson.mkdir();

        }

        if (isCreada == true) {

            nombreJson = "listPreReg" + idFromPreReg + ".json";

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

    public void listOffline() {

        try {
            String PreReg = readJsonFile("/storage/emulated/0/DigGate/listPreReg" + idFromPreReg + ".json");
            if (PreReg == null) {

                msj = Toast.makeText(this, "No hay datos para mostrar", Toast.LENGTH_LONG);
                msj.setGravity(Gravity.CENTER, 0, 0);
                msj.show();

            } else {

                JSONObject response = new JSONObject(PreReg);

                try {
                    description = response.getString("description");
                    colorPreReg = response.getString("color");
                    iconPreReg = response.getString("icon");
                    JSONArray registros = response.getJSONArray("R");
                    for (int i = 0; i < registros.length(); i++) {
                        JSONObject item = registros.getJSONObject(i);
                        int idRegPre = item.getInt("idRegPre");
                        String keyValue = item.getString("keyValue");
                        String stringValue = item.getString("stringValue");
                        arrayListPreReg.add(new obj_listPreReg(idRegPre, description, colorPreReg, iconPreReg, keyValue, stringValue));
                        arrayListPreReg2.add(new obj_listPreReg(idRegPre, description, colorPreReg, iconPreReg, keyValue, stringValue));
                    }
                    adapter = new adapter_listPreReg(listPreReg.this, arrayListPreReg);
                    lvPreReg.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public void searchItem(String texzo) {

        Log.w("buscar", texzo);
        arrayListPreReg.clear();
        for (int i = 0; i < arrayListPreReg2.size(); i++) {

            if (arrayListPreReg2.get(i).getKeyValue().toLowerCase().contains(texzo.toLowerCase())) {

                arrayListPreReg.add(arrayListPreReg2.get(i));

            }

        }

        adapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        ir = new Intent(listPreReg.this, preReg.class);
        ir.putExtra("auth", auth);
        ir.putExtra("userName", userName);
        ir.putExtra("fullName", fullName);
        ir.putExtra("branch", branch);
        startActivity(ir);
        finish();
    }
}
