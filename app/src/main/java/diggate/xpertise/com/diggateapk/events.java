package diggate.xpertise.com.diggateapk;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class events extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    TextView tvPending;
    EditText etSearch;
    ListView lvEvents;
    FloatingActionButton fbAdd;
    String id_events;
    ArrayList<obj_events> itemEvents = new ArrayList<obj_events>();
    ArrayList<obj_events> itemEvents2 = new ArrayList<obj_events>();
    Toast msj;
    String auth;
    String userName;
    ProgressDialog mProgressDialog;
    RequestQueue mRequestQueue;
    JsonObjectRequest mJsonArrayRequest;
    String url;
    Intent ir;
    int idForm;
    int idEvent;
    String keyValue;
    String dateEventBegin;
    String dateEventEnd;
    int personNumber;
    int containerNumber;
    int eventState;
    String colorForm;
    String idIconForm;
    adapter_events adapter;
    String direccion;
    String certificado = "";
    String idOffline;
    JsonObjectRequest mJsonObjectRequest;

    String listText = "SUCESOS REGISTRADOS";
    String fullName;

    JSONArray filtros;
    JSONArray formularios;
    ArrayList<Spinner> spinners = new ArrayList<Spinner>();
    ArrayList<Spinner> spinners2 = new ArrayList<Spinner>();
    ArrayList<String> strings = new ArrayList<String>();
    ArrayList<obj_form> obj_forms = new ArrayList<obj_form>();

    String branch;
    String branchOffline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        url = getString(R.string.servidor) + "/api/lastEventsV3";
        direccion = getString(R.string.servidor) + "/api/saveEvent";

        tvPending = findViewById(R.id.tvPending);
        lvEvents = findViewById(R.id.lvEvents);
        fbAdd = findViewById(R.id.fbAdd);
        fbAdd.setVisibility(View.GONE);
        etSearch = findViewById(R.id.etSearch);
        etSearch.setBackgroundResource(R.drawable.customedittext);
        etSearch.setPadding(30, 20, 30, 20);


        Bundle parametros = this.getIntent().getExtras();
        auth = parametros.getString("auth");
        userName = parametros.getString("userName");
        fullName = parametros.getString("fullName");
        branch = parametros.getString("branch");

        //funcionn para llenar el array de itemEvents y mostrarlo en el ListView

        bd verifica = new bd(this);
        try {
            verifica.abrir();
            Cursor cursor = verifica.searchActive();
            if (cursor.moveToFirst() == false) {
                tvPending.setText(listText);
            } else {
                int cantidad = cursor.getCount();
                tvPending.setText(listText + "       " + cantidad + "Pendiente(s)");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (compruebaConexion(this)) {

            cargarEventos();

        } else {

            cargarEventosOffline();

        }

        //funcionalidad a la lista
        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                obj_events elegido = (obj_events) parent.getItemAtPosition(position);

                // recuperamos el id
                id_events = "" + elegido.getId();
                /*msj = Toast.makeText(events.this, "Evento elegido: " + id_events, Toast.LENGTH_LONG);
                msj.show();*/
                // llamar a la funcion para ver el evento
                ir = new Intent(events.this, view_event.class);
                ir.putExtra("auth", auth);
                ir.putExtra("userName", userName);
                ir.putExtra("idEvent", id_events);
                ir.putExtra("fullName", fullName);
                ir.putExtra("branch", branch);
                startActivity(ir);
                finish();


            }
        });

        fbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ir = new Intent(events.this, forms.class);
                ir.putExtra("auth", auth);
                ir.putExtra("userName", userName);
                ir.putExtra("fullName", fullName);
                ir.putExtra("branch", branch);
                startActivity(ir);
                finish();

            }
        });

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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menusesion, menu);
        MenuItem User = menu.findItem(R.id.nameUser);
        User.setTitle(branch);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.search:

                if (compruebaConexion(this)) {

                    filtros(events.this);

                } else {

                    msj = Toast.makeText(this, "Sin conexion a Internet", Toast.LENGTH_LONG);
                    msj.setGravity(Gravity.CENTER, 0, 0);
                    msj.show();

                }

                return true;

            case R.id.closeSession:

                try {

                    cerrarSesion();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;
            case R.id.sincronizar:

                if (compruebaConexion(this)) {

                    enviarFormularios();

                    Handler handler = new Handler();
                    mProgressDialog = new ProgressDialog(this);
                    mProgressDialog.setMessage("Cargando...");
                    mProgressDialog.show();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mProgressDialog.dismiss();
                            itemEvents.clear();
                            cargarEventos();
                        }
                    }, 2000);

                } else {

                    msj = Toast.makeText(this, "Sin conexion a Internet", Toast.LENGTH_LONG);
                    msj.setGravity(Gravity.CENTER, 0, 0);
                    msj.show();

                }

                return true;

            case R.id.info:

                ir = new Intent(events.this, Info.class);
                ir.putExtra("auth", auth);
                ir.putExtra("userName", userName);
                ir.putExtra("fullName", fullName);
                ir.putExtra("branch", branch);
                startActivity(ir);
                finish();

                return true;

            case R.id.preReg:
                //llamar a la actividad de pre-reg
                ir = new Intent(this, preReg.class);
                ir.putExtra("auth", auth);
                ir.putExtra("userName", userName);
                ir.putExtra("fullName", fullName);
                ir.putExtra("branch", branch);
                startActivity(ir);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void cargarEventos() {

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.show();

        mRequestQueue = Volley.newRequestQueue(this);

        mJsonArrayRequest = new JsonObjectRequest(Request.Method.POST, url, null, this, this) {

            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Authorization", auth); //authentication
                headers.put("Branch", branch); //rama
                return headers;
            }

        };

        mJsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(mJsonArrayRequest);

    }


    @Override
    public void onResponse(JSONObject response) {

        /*msj = Toast.makeText(this, "" + response, Toast.LENGTH_LONG);
        msj.show();*/
        Log.w("respuesta", "" + response);

        mProgressDialog.dismiss();

        try {
            createJson(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*try{

            for(int i=0;i<response.length();i++) {

                JSONObject event = response.getJSONObject(i);

                idForm = event.getInt("idForm");
                idEvent = event.getInt("idEvent");
                keyValue = event.getString("keyValue");
                dateEventBegin = event.getString("dateEventBegin");
                dateEventEnd = event.getString("dateEventEnd");
                personNumber = event.getInt("personNumber");
                containerNumber = event.getInt("containerNumber");
                eventState = event.getInt("eventState");
                colorForm = event.getString("colorForm");
                idIconForm = event.getString("idIconForm");

                itemEvents.add(new obj_events(idEvent, keyValue, dateEventBegin, dateEventEnd, idForm, personNumber, containerNumber, eventState, colorForm, idIconForm));
                itemEvents2.add(new obj_events(idEvent, keyValue, dateEventBegin, dateEventEnd, idForm, personNumber, containerNumber, eventState, colorForm, idIconForm));

            }

            adapter = new adapter_events(events.this, itemEvents);
            lvEvents.setAdapter(adapter);

        }catch (JSONException e) {

            e.printStackTrace();
            msj = Toast.makeText(this, "No se tiene datos registrados", Toast.LENGTH_LONG);
            msj.show();

        }*/

        try {
            JSONArray formulario = response.getJSONArray("forms");
            JSONArray eventitos = response.getJSONArray("events");
            filtros = response.getJSONArray("filters");
            formularios = formulario;
            Log.w("filtros", filtros.toString());
            Log.w("formulario", formularios.toString());

            /***/
            JSONArray roles = response.getJSONArray("roles");
            for (int z = 0; z < roles.length(); z++) {
                JSONObject rol = roles.getJSONObject(z);
                if(rol.getInt("canCreate") == 1);
                {
                    fbAdd.setVisibility(View.VISIBLE);
                }
                Log.w("rol", "" + rol.getInt("canCreate"));
            }
            /***/

            bd conexion = new bd(events.this);
            conexion.abrir();
            conexion.deleteForm();

            for (int s = 0; s < formulario.length(); s++) {

                JSONObject formularioObj = formulario.getJSONObject(s);

                idForm = formularioObj.getInt("idForm");
                colorForm = formularioObj.getString("colorForm");
                idIconForm = formularioObj.getString("idIconForm");

                conexion.createForm(idForm, colorForm, idIconForm);

                Log.w("contadorForm", "" + s);

            }
            for (int a = 0; a < eventitos.length(); a++) {

                JSONObject eventitosObj = eventitos.getJSONObject(a);

                int idForm2 = eventitosObj.getInt("idForm");
                idEvent = eventitosObj.getInt("idEvent");
                keyValue = eventitosObj.getString("keyValue");
                dateEventBegin = eventitosObj.getString("dateEventBegin");
                dateEventEnd = eventitosObj.getString("dateEventEnd");
                personNumber = eventitosObj.getInt("personNumber");
                containerNumber = eventitosObj.getInt("containerNumber");
                eventState = eventitosObj.getInt("eventState");

                Cursor atributos = conexion.searchForm(idForm2);
                atributos.moveToNext();
                String colorForm2 = atributos.getString(1);
                String idIconForm2 = atributos.getString(2);

                Log.w("color", colorForm2);
                Log.w("icono", idIconForm2);

                itemEvents.add(new obj_events(idEvent, keyValue, dateEventBegin, dateEventEnd, idForm2, personNumber, containerNumber, eventState, colorForm2, idIconForm2));
                itemEvents2.add(new obj_events(idEvent, keyValue, dateEventBegin, dateEventEnd, idForm2, personNumber, containerNumber, eventState, colorForm2, idIconForm2));


            }

            adapter = new adapter_events(events.this, itemEvents);
            lvEvents.setAdapter(adapter);


        } catch (JSONException e) {
            try {
                if (response.getInt("code") == 1) {
                    try {
                        cerrarSesion();
                    } catch (Exception err) {
                        err.printStackTrace();
                    }
                } else {
                    msj = Toast.makeText(this, "No se tiene datos registrados", Toast.LENGTH_LONG);
                    msj.show();
                }
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onErrorResponse(VolleyError error) {

        mProgressDialog.dismiss();
        /*msj = Toast.makeText(this, "Ocurrio un Error: " + error, Toast.LENGTH_LONG);
        msj.show();*/
        cargarEventosOffline();
        Log.w("respuestaError", "" + error);

    }

    public void searchItem(String texzo) {

        Log.w("buscar", texzo);
        itemEvents.clear();
        for (int i = 0; i < itemEvents2.size(); i++) {

            if (itemEvents2.get(i).getVariable().toLowerCase().contains(texzo.toLowerCase())) {

                itemEvents.add(itemEvents2.get(i));

            }

        }

        adapter.notifyDataSetChanged();

    }

    public void cargarEventosOffline() {

        try {
            String events = readJsonFile("/storage/emulated/0/DigGate/events.json");
            if (events == null) {

                msj = Toast.makeText(this, "No hay datos para mostrar", Toast.LENGTH_LONG);
                msj.setGravity(Gravity.CENTER, 0, 0);
                msj.show();

            } else {

                /*try{

                    JSONArray response = new JSONArray(events);

                    for(int i=0;i<response.length();i++) {

                        JSONObject event = response.getJSONObject(i);

                        idForm = event.getInt("idForm");
                        idEvent = event.getInt("idEvent");
                        keyValue = event.getString("keyValue");
                        dateEventBegin = event.getString("dateEventBegin");
                        dateEventEnd = event.getString("dateEventEnd");
                        personNumber = event.getInt("personNumber");
                        containerNumber = event.getInt("containerNumber");
                        eventState = event.getInt("eventState");
                        colorForm = event.getString("colorForm");
                        idIconForm = event.getString("idIconForm");

                        itemEvents.add(new obj_events(idEvent, keyValue, dateEventBegin, dateEventEnd, idForm, personNumber, containerNumber, eventState, colorForm, idIconForm));
                        itemEvents2.add(new obj_events(idEvent, keyValue, dateEventBegin, dateEventEnd, idForm, personNumber, containerNumber, eventState, colorForm, idIconForm));

                    }

                    adapter = new adapter_events(events.this, itemEvents);
                    lvEvents.setAdapter(adapter);

                }catch (JSONException e) {

                    e.printStackTrace();

                }*/

                try {

                    JSONObject response = new JSONObject(events);

                    JSONArray formulario = response.getJSONArray("forms");
                    JSONArray eventitos = response.optJSONArray("events");

                    bd conexion = new bd(events.this);
                    conexion.abrir();
                    conexion.deleteForm();

                    for (int s = 0; s < formulario.length(); s++) {

                        JSONObject formularioObj = formulario.getJSONObject(s);

                        idForm = formularioObj.getInt("idForm");
                        colorForm = formularioObj.getString("colorForm");
                        idIconForm = formularioObj.getString("idIconForm");

                        conexion.createForm(idForm, colorForm, idIconForm);

                    }
                    for (int a = 0; a < eventitos.length(); a++) {

                        JSONObject eventitosObj = eventitos.getJSONObject(a);

                        int idForm2 = eventitosObj.getInt("idForm");
                        idEvent = eventitosObj.getInt("idEvent");
                        keyValue = eventitosObj.getString("keyValue");
                        dateEventBegin = eventitosObj.getString("dateEventBegin");
                        dateEventEnd = eventitosObj.getString("dateEventEnd");
                        personNumber = eventitosObj.getInt("personNumber");
                        containerNumber = eventitosObj.getInt("containerNumber");
                        eventState = eventitosObj.getInt("eventState");

                        Cursor atributos = conexion.searchForm(idForm2);
                        atributos.moveToNext();
                        String colorForm2 = atributos.getString(1);
                        String idIconForm2 = atributos.getString(2);

                        Log.w("color", colorForm2);
                        Log.w("icono", idIconForm2);

                        itemEvents.add(new obj_events(idEvent, keyValue, dateEventBegin, dateEventEnd, idForm2, personNumber, containerNumber, eventState, colorForm2, idIconForm2));
                        itemEvents2.add(new obj_events(idEvent, keyValue, dateEventBegin, dateEventEnd, idForm2, personNumber, containerNumber, eventState, colorForm2, idIconForm2));


                    }

                    adapter = new adapter_events(events.this, itemEvents);
                    lvEvents.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
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

            nombreJson = "events.json";

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

    public void cerrarSesion() throws Exception {

        bd conexion = new bd(this);
        conexion.abrir();
        conexion.updateSession(userName);
        conexion.cerrar();
        ir = new Intent(this, MainActivity.class);
        startActivity(ir);
        finish();

    }

    /*private void enviarFormularios() {

        mProgressDialog = new ProgressDialog(this);
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
                            bd conexion2 = new bd(events.this);
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
                    }) {

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
                tvPending.setText(listText);

            }
        } catch (Exception e) {
            e.printStackTrace();
            mProgressDialog.dismiss();
        }

        mProgressDialog.dismiss();

    }*/
    private void enviarFormularios() {

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        try {
            final bd conexion = new bd(this);
            conexion.abrir();
            final Cursor offline = conexion.searchActive();
            if (!offline.moveToFirst() == false) {

                offline.moveToFirst();
                while (!offline.isAfterLast()) {

                    mRequestQueue = Volley.newRequestQueue(this);

                    idOffline = offline.getString(0);
                    String jsonAnswers = offline.getString(1);
                    String user = offline.getString(2);
                    certificado = offline.getString(3);
                    branchOffline = offline.getString(5);

                    Log.w("data", certificado + " " + idOffline);

                    String dataJson = readJsonFileAnswer(jsonAnswers);

                    JSONObject respuesta = new JSONObject(dataJson);

                    Log.w("data", " " + respuesta);

                    probar(respuesta, idOffline, branchOffline);

                    offline.moveToNext();
                }
                conexion.cerrar();
            }
        } catch (Exception e) {
            e.printStackTrace();
            mProgressDialog.dismiss();
        }

        mProgressDialog.dismiss();

    }

    /***********/
    public void probar(JSONObject respuesta, final String idBD, String branch) {
        mJsonObjectRequest = new JsonObjectRequest(Request.Method.POST, direccion, respuesta, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.w("mio", "" + response);

                //conexion.updateAnswers(idOffline);
                Log.w("id", idBD);
                bd conexion2 = new bd(events.this);
                try {
                    conexion2.abrir();
                    conexion2.updateMyAnswers(idBD);
                    conexion2.cerrar();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                bd verifica = new bd(events.this);
                try {
                    verifica.abrir();
                    Cursor cursor = verifica.searchActive();
                    if (cursor.moveToFirst() == false) {
                        tvPending.setText(listText);
                    } else {
                        int cantidad = cursor.getCount();
                        tvPending.setText(listText + "       " + cantidad + "Pendiente(s)");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.w("mioerror", "" + error);

            }
        }) {

            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Authorization", certificado); //authentication
                headers.put("Branch", branch); //rama
                return headers;
            }

        };

        mJsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(mJsonObjectRequest);
    }

    /***********/


    public String readJsonFileAnswer(String path) {

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

    public void filtros(Context c) {
        obj_forms.clear();

        LinearLayout llCon = new LinearLayout(c);
        //***********************************************************************************
        LinearLayout llcrearDialogo = new LinearLayout(c);
        LinearLayout.LayoutParams parametrosTextoEditor = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        llcrearDialogo.setLayoutParams(parametrosTextoEditor);
        llcrearDialogo.setOrientation(LinearLayout.VERTICAL);
        llcrearDialogo.setPadding(10, 10, 10, 10);
        llCon.addView(llcrearDialogo);

        ScrollView svBody = new ScrollView(c);
        ScrollView.LayoutParams parametros = new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        svBody.setLayoutParams(parametros);
        svBody.setPadding(10, 10, 10, 10);
        llcrearDialogo.addView(svBody);

        LinearLayout llbody = new LinearLayout(c);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        llbody.setLayoutParams(params);
        llbody.setOrientation(LinearLayout.VERTICAL);
        llbody.setPadding(10, 10, 10, 10);
        svBody.addView(llbody);

        try {

            for (int i = 0; i < filtros.length(); i++) {
                JSONObject filtro = filtros.getJSONObject(i);
                JSONArray options = filtro.getJSONArray("values");
                String nameFilter = filtro.getString("nameFilter");
                String idFilter = filtro.getString("idFilter");
                Log.w("nameFilter", nameFilter);
                ArrayList<obj_params> listoption = new ArrayList<>();
                for (int j = 0; j < options.length(); j++) {
                    JSONObject option = options.getJSONObject(j);
                    int id = option.getInt("idValue");
                    String description = option.getString("description");
                    listoption.add(new obj_params(id, description, i));
                }
                createtextViewTitle(nameFilter, llbody);
                createSpinner(i, listoption, llbody);

                strings.add(idFilter);
            }

            for (int j = 0; j < formularios.length(); j++) {
                JSONObject form = formularios.getJSONObject(j);

                int idForm = form.getInt("idForm");
                String colorForm = form.getString("colorForm");
                String idIconForm = form.getString("idIconForm");
                String descriptionForm = form.getString("descriptionForm");
                obj_forms.add(new obj_form(idForm, colorForm, descriptionForm, idIconForm, 0, 0));
                /*switchForm(idForm, colorForm, idIconForm, llbody);
                Log.w("descriptionForm", descriptionForm + "");*/
            }

            createtextViewTitle("Formularios", llbody);
            createSpinnerForm(obj_forms, llbody);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //***********************BUTTON ACEPTAR********************************
        Button btnAceptar = new Button(c);
        LinearLayout.LayoutParams parametrosAceptar = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        parametrosAceptar.setMargins(0, 30, 0, 0);
        btnAceptar.setLayoutParams(parametrosAceptar);
        btnAceptar.setText("aceptar");
        btnAceptar.setTextAppearance(this, R.style.colorTitle);
        btnAceptar.setBackgroundResource(R.drawable.custonbutton);
        llbody.addView(btnAceptar);


        //AlertDialog.Builder builder = new AlertDialog.Builder(c);
        final AlertDialog optionDialog = new AlertDialog.Builder(c, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen).create();

        optionDialog.setTitle("Selecciona la opcion:");

        optionDialog.setView(llCon);//******************************************

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createSearch();
                optionDialog.dismiss();

            }


        }); //fin del button

        //AlertDialog dialog = builder.create();
        optionDialog.show();
        //dialog.show();

    }

    public void createSpinner(int idField, ArrayList<obj_params> aux, LinearLayout llContainer) {

        Spinner sp = new Spinner(this);
        sp.setId(idField);
        adapter_params adapter = new adapter_params(events.this, aux);
        sp.setAdapter(adapter);
        spinners.add(sp);
        llContainer.addView(sp);
        int posicion = 0;
        sp.setSelection(posicion);

    }

    public void createtextViewTitle(String texto, LinearLayout llContainer) {

        TextView tv;
        tv = new TextView(this);
        tv.setText(texto);
        tv.setTextAppearance(this, R.style.colorTitle);
        llContainer.addView(tv);

    }

    public void createSpinnerForm(ArrayList<obj_form> aux, LinearLayout llContainer) {

        Spinner sp = new Spinner(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(0, 20, 0, 0);
        sp.setLayoutParams(params);
        adapter_paramsForm adapter = new adapter_paramsForm(events.this, aux);
        sp.setAdapter(adapter);
        spinners2.add(sp);
        llContainer.addView(sp);

       /* sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }

    public void createSearch() {

        JSONObject search = new JSONObject();
        JSONArray filterSelected = new JSONArray();

        for (Iterator iterator = spinners.iterator(); iterator
                .hasNext(); ) {

            Spinner spinner = (Spinner) iterator.next();

            int position = spinner.getSelectedItemPosition();

            obj_params elegido = (obj_params) spinner.getItemAtPosition(position);
            Log.w("Spinner", "spinner: " + spinner.getId() + " posicion: " + position + " " + elegido.getId());
            String idFilter = strings.get(elegido.getControl());
            Log.w("idFilter", "" + idFilter);
            try {
                JSONObject parametros = new JSONObject();
                parametros.put("idFilter", idFilter);
                parametros.put("idValue", elegido.getId());
                filterSelected.put(parametros);
                search.put("filterSelected", filterSelected);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        try {
            JSONObject parametros = new JSONObject();
            JSONArray formList = new JSONArray();
            parametros.put("idFilter", "events");
            for (Iterator iterator = spinners2.iterator(); iterator
                    .hasNext(); ) {
                Spinner spinner = (Spinner) iterator.next();
                int position = spinner.getSelectedItemPosition();
                obj_form elegido = (obj_form) spinner.getItemAtPosition(position);
                JSONObject parametros2 = new JSONObject();
                parametros2.put("idFrom", elegido.getId());
                formList.put(parametros2);
            }
            parametros.put("values", formList);
            filterSelected.put(parametros);
            search.put("filterSelected", filterSelected);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.w("search", "" + search);
        spinners.clear();
        spinners2.clear();
        sendSearch(search);

    }

    public void sendSearch(JSONObject jsonSearch) {
        String url = getString(R.string.servidor) + "/api/lastEventsV3";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonSearch, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.w("logres", response.toString());
                try {
                    JSONArray formulario = response.getJSONArray("forms");
                    JSONArray eventitos = response.getJSONArray("events");

                    bd conexion = new bd(events.this);
                    conexion.abrir();
                    conexion.deleteForm();

                    itemEvents.clear();
                    for (int s = 0; s < formulario.length(); s++) {

                        JSONObject formularioObj = formulario.getJSONObject(s);

                        idForm = formularioObj.getInt("idForm");
                        colorForm = formularioObj.getString("colorForm");
                        idIconForm = formularioObj.getString("idIconForm");

                        conexion.createForm(idForm, colorForm, idIconForm);

                        Log.w("contadorForm", "" + s);

                    }
                    for (int a = 0; a < eventitos.length(); a++) {

                        JSONObject eventitosObj = eventitos.getJSONObject(a);

                        int idForm2 = eventitosObj.getInt("idForm");
                        idEvent = eventitosObj.getInt("idEvent");
                        keyValue = eventitosObj.getString("keyValue");
                        dateEventBegin = eventitosObj.getString("dateEventBegin");
                        dateEventEnd = eventitosObj.getString("dateEventEnd");
                        personNumber = eventitosObj.getInt("personNumber");
                        containerNumber = eventitosObj.getInt("containerNumber");
                        eventState = eventitosObj.getInt("eventState");

                        Cursor atributos = conexion.searchForm(idForm2);
                        atributos.moveToNext();
                        String colorForm2 = atributos.getString(1);
                        String idIconForm2 = atributos.getString(2);

                        Log.w("color", colorForm2);
                        Log.w("icono", idIconForm2);

                        itemEvents.add(new obj_events(idEvent, keyValue, dateEventBegin, dateEventEnd, idForm2, personNumber, containerNumber, eventState, colorForm2, idIconForm2));

                    }

                    adapter = new adapter_events(events.this, itemEvents);
                    lvEvents.setAdapter(adapter);


                } catch (JSONException e) {
                    try {
                        if (response.getInt("code") == 1) {
                            try {
                                cerrarSesion();
                            } catch (Exception err) {
                                err.printStackTrace();
                            }
                        } else {
                            msj = Toast.makeText(events.this, "No se tiene datos registrados", Toast.LENGTH_LONG);
                            msj.show();
                        }
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                    e.printStackTrace();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Authorization", auth); //authentication
                headers.put("Branch", branch); //rama
                return headers;
            }

        };

        requestQueue.add(jsonObjectRequest);
    }

}
