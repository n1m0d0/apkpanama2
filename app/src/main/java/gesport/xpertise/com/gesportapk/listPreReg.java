package gesport.xpertise.com.gesportapk;

import android.app.ProgressDialog;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class listPreReg extends AppCompatActivity {
    String auth, userName, fullName;
    int idFromPreReg;
    EditText etSearch;
    ListView lvPreReg;
    FloatingActionButton fabAdd;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pre_reg);

        etSearch = findViewById(R.id.etSearch);
        lvPreReg = findViewById(R.id.lvPreReg);
        fabAdd = findViewById(R.id.fabAdd);

        Bundle parametros = this.getIntent().getExtras();
        auth = parametros.getString("auth");
        userName = parametros.getString("userName");
        fullName = parametros.getString("fullName");
        idFromPreReg = parametros.getInt("idFromPreReg");

        list();
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

    public void createJson(JSONObject jsonArray) {

        String path = null;
        String carpeta = "geoport";
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
}
