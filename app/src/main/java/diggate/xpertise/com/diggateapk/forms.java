package diggate.xpertise.com.diggateapk;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

import SecuGen.FDxSDKPro.JSGFPLib;
import SecuGen.FDxSDKPro.SGAutoOnEventNotifier;
import SecuGen.FDxSDKPro.SGFDxDeviceName;

public class forms extends AppCompatActivity implements Response.Listener<JSONArray>, Response.ErrorListener {

    //GridView gvForms;
    ListView listView;
    String id_form;
    ArrayList<obj_form> itemForms = new ArrayList<obj_form>();
    Toast msj;
    String auth;
    String userName;
    ProgressDialog mProgressDialog;
    RequestQueue mRequestQueue;
    JsonArrayRequest mJsonArrayRequest;
    String url;
    Intent ir;

    int idForm;
    String colorForm;
    String descriptionForm;
    String idIconForm;
    int positionForm;
    int typeDependency;
    String fullName;

    // biometrico
    long error;

    private boolean mLed;
    private boolean mAutoOnEnabled;
    private SGAutoOnEventNotifier autoOn;
    HexConversion conversion;

    private static final String TAG = "MIRZA";


    private PendingIntent mPermissionIntent;
    private IntentFilter filter;

    private JSGFPLib sgfplib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.forms);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        url = getString(R.string.servidor) + "/api/parForm";

        //gvForms = findViewById(R.id.gvForms);
        listView = findViewById(R.id.lvForms);

        Bundle parametros = this.getIntent().getExtras();
        auth = parametros.getString("auth");
        userName = parametros.getString("userName");
        fullName = parametros.getString("fullName");

        if (compruebaConexion(this)) {

            cargarFormularios();

        } else {

            cargarFormulariosOffline();

        }

        //funcionalidad a la lista
        /*gvForms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                obj_form elegido = (obj_form) parent.getItemAtPosition(position);

                // recuperamos el id
                id_form = "" + elegido.getId();

                // llamar a la funcion para ver el formulario
                ir = new Intent(forms.this, form_event.class);
                ir.putExtra("auth", auth);
                ir.putExtra("userName", userName);
                ir.putExtra("idForm", id_form);
                ir.putExtra("fullName", fullName);
                startActivity(ir);

            }
        });*/

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                obj_form elegido = (obj_form) parent.getItemAtPosition(position);

                // recuperamos el id
                id_form = "" + elegido.getId();

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

    private void cargarFormularios() {

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.show();

        mRequestQueue = Volley.newRequestQueue(this);

        mJsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, this, this) {

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

        try {

            for (int i = 0; i < response.length(); i++) {

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
            listView.setAdapter(adapter);

        } catch (JSONException e) {

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
        cargarFormulariosOffline();
        Log.w("respuesta", "" + error);

    }

    @Override
    public void onBackPressed() {

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

                try {

                    JSONArray response = new JSONArray(forms);

                    for (int i = 0; i < response.length(); i++) {

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
                    listView.setAdapter(adapter);

                } catch (JSONException e) {

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

        if (isCreada == false) {

            isCreada = fileJson.mkdir();

        }

        if (isCreada == true) {

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

    public String readJsonFile(String path) {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activacion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.activarUSB:

                try {

                    usbPermission();


                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //biometrico
    private void usbPermission() {
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(
                ACTION_USB_PERMISSION), 0);
        filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mUsbReceiver, filter);
        sgfplib = new JSGFPLib(
                (UsbManager) getSystemService(Context.USB_SERVICE));
        mLed = false;
        mAutoOnEnabled = false;
        error = sgfplib.Init(SGFDxDeviceName.SG_DEV_AUTO);
        UsbDevice usbDevice = sgfplib.GetUsbDevice();
        sgfplib.GetUsbManager().requestPermission(usbDevice, mPermissionIntent);
        error = sgfplib.OpenDevice(0);
        SecuGen.FDxSDKPro.SGDeviceInfoParam deviceInfo = new SecuGen.FDxSDKPro.SGDeviceInfoParam();
        //error = sgfplib.GetDeviceInfo(deviceInfo);
        Toast.makeText(forms.this, "USB ACTIVADO", Toast.LENGTH_SHORT).show();
    }

    // USB Device Attach Permission
    private static final String ACTION_USB_PERMISSION = "gesport.xpertise.com.gesportapk.USB_PERMISSION";
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent
                            .getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(
                            UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {

                            Log.d(TAG, "Vender DI" + device.getVendorId());

                            Log.d(TAG, "Producat ID " + device.getProductId());

                        } else
                            Log.e(TAG,
                                    "mUsbReceiver.onReceive() Device is null");
                    } else
                        Log.e(TAG,
                                "mUsbReceiver.onReceive() permission denied for device "
                                        + device);
                }
            }
        }
    };

}
