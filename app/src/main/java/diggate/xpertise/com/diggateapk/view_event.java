package diggate.xpertise.com.diggateapk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class view_event extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnCompletionListener {

    LinearLayout llContenedor;
    Button btnCheckOut;
    String auth;
    String userName;
    String idEvent;
    String url;
    ProgressDialog mProgressDialog;
    RequestQueue mRequestQueue;
    JsonArrayRequest mJsonArrayRequest;
    ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
    ArrayList<TextView> textViews = new ArrayList<TextView>();
    int option;
    Intent ir;
    int generaForm;
    int idForm;
    Toast msj;
    String fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        url = getString(R.string.servidor) + "/api/openEvent/";

        llContenedor = findViewById(R.id.llContenedor);
        btnCheckOut = findViewById(R.id.btnCheckOut);

        Bundle parametros = this.getIntent().getExtras();
        auth = parametros.getString("auth");
        userName = parametros.getString("userName");
        idEvent = parametros.getString("idEvent");
        fullName = parametros.getString("fullName");


        Log.w("idEvent", idEvent);
        url = url + idEvent;
        //mostrar datos

        if (compruebaConexion(this)) {

            cargarFormulario();

        } else {

            msj = Toast.makeText(this, "Sin conexion a Internet", Toast.LENGTH_LONG);
            msj.setGravity(Gravity.CENTER, 0, 0);
            msj.show();
            btnCheckOut.setVisibility(View.GONE);

        }


        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ir = new Intent(view_event.this, checkout.class);
                ir.putExtra("auth", auth);
                ir.putExtra("userName", userName);
                ir.putExtra("idForm", "" + generaForm);
                ir.putExtra("idEvent", "" + idEvent);
                ir.putExtra("fullName", fullName);
                startActivity(ir);
                finish();

                Log.w("generaForm", "" + "" + generaForm);

            }
        });

        btnCheckOut.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {

        option = v.getId();

        String urls = "";

        for (Iterator iterator = imageViews.iterator(); iterator
                .hasNext(); ) {

            ImageView imageView = (ImageView) iterator.next();

            if (imageView.getId() == option) {


                urls = "" + imageView.getContentDescription();
                Uri uri = Uri.parse(urls);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);


            }

            Log.w("Edit", imageView.getId() + " = " + urls);


        }

        for (Iterator iterator = textViews.iterator(); iterator
                .hasNext(); ) {

            TextView textView = (TextView) iterator.next();

            if (textView.getId() == option) {

                Uri uri = Uri.parse(textView.getText().toString().trim());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);


            }

            Log.w("Edit", textView.getId() + " = " + urls);


        }


    }

    private void cargarFormulario() {

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        mRequestQueue = Volley.newRequestQueue(this);

        mJsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                mProgressDialog.dismiss();

                try {

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject form = response.getJSONObject(i);

                        int idEvent = form.getInt("idEvent");
                        int idEventDependency = form.getInt("idEventDependency");
                        String dateEvent = form.getString("dateEvent");
                        String posGeo = form.getString("posGeo");
                        idForm = form.getInt("idForm");
                        generaForm = form.getInt("generaForm");
                        JSONArray opciones = form.getJSONArray("P");

                        if (generaForm > 0) {

                            btnCheckOut.setVisibility(View.VISIBLE);

                        }

                        //ArrayList<obj_params> itemp = new ArrayList<obj_params>();

                        for (int j = 0; j < opciones.length(); j++) {

                            JSONObject op = opciones.getJSONObject(j);
                            int idField = op.getInt("idField");
                            String valueInputField = op.getString("valueInputField");
                            String valueInputDateField = op.getString("valueInputDateField");
                            String valueListField = op.getString("valueListField");
                            String valueFile = op.getString("valueFile");
                            int type = op.getInt("type");
                            String description = op.getString("description");

                            switch (type) {

                                case 1:

                                    createTextView(description);
                                    createEditText(valueInputField);

                                    break;

                                case 2:

                                    createTextView(description);
                                    createEditTextMultiLinea(valueInputField);

                                    break;

                                case 3:

                                    createTextView(description);
                                    createEditText(valueInputField);

                                    break;

                                case 4:

                                    createTextView(description);
                                    createEditText(valueInputDateField);

                                    break;

                                case 5:
                                    createTextView(description);
                                    createEditText(valueInputField);

                                    break;

                                case 6:

                                    createTextView(description);
                                    if (valueInputField.equals("")) {

                                    } else {

                                        createImageView(idField, valueFile, valueInputField);

                                    }

                                    break;

                                case 7:

                                    createTextView(description);
                                    if (valueInputField.equals("")) {

                                    } else {

                                        createTextViewpath(idField, valueInputField);

                                    }

                                    break;

                                case 8:

                                    createTextView(description);
                                    createEditText(valueInputField);

                                    break;

                                case 9:

                                    createTextView(description);
                                    createSwitch("SI    /    NO", valueInputField);

                                    break;

                                case 10:

                                    createtextViewTitle(description);

                                    break;

                                case 11:

                                    createAudio(description, valueInputField);

                                    break;

                            }


                            //itemp.add(new obj_params(idField, des));

                        }

                    }

                } catch (JSONException e) {

                    e.printStackTrace();

                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mProgressDialog.dismiss();

            }
        }) {

            @Override
            public Map getHeaders() throws AuthFailureError {

                HashMap headers = new HashMap();
                headers.put("Authorization", auth); //authentication
                return headers;

            }

        };

        mRequestQueue.add(mJsonArrayRequest);

    }

    public void createTextView(String description) {

        TextView textView = new TextView(this);
        textView.setText(description);
        LinearLayout.LayoutParams lastTxtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lastTxtParams.setMargins(0, 30, 0, 0);
        textView.setLayoutParams(lastTxtParams);
        //textView.setTextColor(getResources().getColor(R.color.colorBlack));
        textView.setTextAppearance(this, R.style.colorText);
        llContenedor.addView(textView);

    }

    public void createtextViewTitle(String texto) {

        TextView tv;
        tv = new TextView(this);
        tv.setText(texto);
        tv.setTextAppearance(this, R.style.colorTitle);
        llContenedor.addView(tv);

    }

    public void createEditText(String description) {

        /*EditText editText = new EditText(this);
        editText.setText(description);
        editText.setEnabled(false);
        llContenedor.addView(editText);*/
        TextView tv;
        tv = new TextView(this);
        tv.setText(description);
        //tv.setTextAppearance(this, R.style.boldreg);
        tv.setTextColor(getResources().getColor(R.color.colorTextVariable));
        llContenedor.addView(tv);

    }

    public void createEditTextMultiLinea(String descripcion) {

        /*EditText et = new EditText(this);
        et.setSingleLine(false);
        et.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        et.setLines(1);
        et.setMaxLines(10);
        et.setVerticalScrollBarEnabled(true);
        et.setMovementMethod(ScrollingMovementMethod.getInstance());
        et.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        et.setText(descripcion);
        InputFilter[] ifet = new InputFilter[1];
        ifet[0] = new InputFilter.LengthFilter(254);
        et.setFilters(ifet);
        et.setEnabled(false);
        llContenedor.addView(et);*/
        TextView tv;
        tv = new TextView(this);
        tv.setText(descripcion);
        tv.setTextAppearance(this, R.style.boldreg);
        llContenedor.addView(tv);

    }

    public void createSwitch(String description, String valor) {

        Log.w("valor", valor);
        boolean variable = Boolean.parseBoolean(valor);
        Log.w("bolean", "" + variable);
        Switch s = new Switch(this);
        s.setText(description);
        s.setTextColor(getResources().getColor(R.color.colorBlack));
        s.setTextOn("Si");
        s.setTextOff("No");
        s.setChecked(variable);
        s.setEnabled(false);
        llContenedor.addView(s);

    }

    public void createImageView(int id, String imagen, String url) {

        ImageView imageView = new ImageView(this);

        imageView.setId(id);
        imageView.setContentDescription(url);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(imagen, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        imageView.setImageBitmap(decodedImage);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(400, 400);
        imageView.setLayoutParams(lp);
        imageView.setOnClickListener(this);
        imageViews.add(imageView);
        llContenedor.addView(imageView);

    }

    public void createTextViewpath(int id, String description) {

        /*TextView textView = new TextView(this);
        textView.setId(id);
        textView.setText(description);
        textView.setTextAppearance(this, R.style.boldreg);
        textView.setOnClickListener(this);
        textViews.add(textView);
        llContenedor.addView(textView);*/

        /***********************/
        LinearLayout llImg = new LinearLayout(this);
        LinearLayout.LayoutParams paramsImg = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llImg.setLayoutParams(paramsImg);
        llImg.setOrientation(LinearLayout.HORIZONTAL);
        llImg.setWeightSum(12);
        llContenedor.addView(llImg);

        ImageView iv = new ImageView(this);
        iv.setId(id);
        iv.setImageResource(R.drawable.files);
        iv.setContentDescription(description);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150, 5f);
        iv.setLayoutParams(lp);
        llImg.addView(iv);
        iv.setOnClickListener(this);
        imageViews.add(iv);

        LinearLayout llImg2 = new LinearLayout(this);
        LinearLayout.LayoutParams paramsImg2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 5f);
        llImg2.setLayoutParams(paramsImg2);
        llImg.addView(llImg2);

        LinearLayout llImg3 = new LinearLayout(this);
        LinearLayout.LayoutParams paramsImg3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 2f);
        llImg3.setLayoutParams(paramsImg3);
        llImg.addView(llImg3);
        /*************************/

        /*ImageView imageView = new ImageView(this);

        imageView.setId(id);
        imageView.setContentDescription(description);
        imageView.setImageResource(R.drawable.files);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(180,180);
        imageView.setLayoutParams(lp);
        imageView.setOnClickListener(this);
        imageViews.add(imageView);
        llContenedor.addView(imageView);*/

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

    /*************************/
    public void createAudio(String descripcion, final String audio) {
        TextView textViewOption = new TextView(this);
        textViewOption.setText(descripcion);
        textViewOption.setTextSize(14);
        textViewOption.setTextColor(getResources().getColor(R.color.colorBlack));
        llContenedor.addView(textViewOption);
        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(lp);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setWeightSum(12);
        linearLayout.setGravity(Gravity.CENTER);
        llContenedor.addView(linearLayout);
        final ImageView imageViewPlay = new ImageView(this);
        imageViewPlay.setImageResource(R.drawable.play);
        LinearLayout.LayoutParams lpaudioo = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 150, 5f);
        imageViewPlay.setLayoutParams(lpaudioo);
        linearLayout.addView(imageViewPlay);
        final ImageView imageViewStop = new ImageView(this);
        imageViewStop.setImageResource(R.drawable.stop);
        imageViewStop.setLayoutParams(lpaudioo);
        linearLayout.addView(imageViewStop);
        imageViewStop.setVisibility(View.INVISIBLE);
        final TextView textViewAudio = new TextView(this);
        textViewAudio.setText("Haga clic para reproducir el audio");
        textViewAudio.setTextSize(14);
        LinearLayout.LayoutParams lpTextView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
        textViewAudio.setLayoutParams(lpTextView);
        linearLayout.addView(textViewAudio);
        textViewAudio.setVisibility(View.INVISIBLE);
        final MediaPlayer[] mp = {new MediaPlayer()};
        imageViewPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp[0] = new MediaPlayer();
                try {
                    mp[0].setDataSource(audio);
                    mp[0].prepare();
                    mp[0].start();
                    textViewAudio.setText("Haga clic para detener el audio");
                    imageViewStop.setVisibility(View.VISIBLE);
                    imageViewPlay.setVisibility(View.INVISIBLE);
                } catch (IOException e) {
                    Log.e("LOG", "" + e);
                }
            }
        });
        imageViewStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp[0].stop();
                mp[0].release();
                textViewAudio.setText("Haga clic para reproducir el audio");
                imageViewPlay.setVisibility(View.VISIBLE);
                imageViewStop.setVisibility(View.INVISIBLE);
            }
        });
    }

    /*************************/

    @Override
    public void onBackPressed() {

        ir = new Intent(view_event.this, events.class);
        ir.putExtra("auth", auth);
        ir.putExtra("userName", userName);
        ir.putExtra("fullName", fullName);
        startActivity(ir);
        finish();

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }
}
