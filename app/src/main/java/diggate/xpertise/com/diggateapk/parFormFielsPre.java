package diggate.xpertise.com.diggateapk;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import SecuGen.FDxSDKPro.JSGFPLib;
import SecuGen.FDxSDKPro.SGAutoOnEventNotifier;
import SecuGen.FDxSDKPro.SGFDxDeviceName;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class parFormFielsPre extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnCompletionListener {

    int idField;
    int opcion;
    String auth;
    String userName;
    int idFromPreReg;
    LinearLayout llContenedor, llRecording;
    Button btnSave;
    ImageView ivRecording, ivPlaying;
    TextView tvRecording, tvPathRecording;
    ProgressDialog mProgressDialog;
    RequestQueue mRequestQueue;
    JsonArrayRequest mJsonArrayRequest;

    final int codigoCamera = 20;
    final int codigoFile = 10;

    private final String camara_raiz = "misImagenesSistema/";
    private final String ruta_imagen = camara_raiz + "misFotos";


    String url;
    String url2;

    Intent ir;
    Toast msj;
    ArrayList<EditText> editTexts = new ArrayList<EditText>();
    ArrayList<CheckBox> checkBoxs = new ArrayList<CheckBox>();
    ArrayList<RadioGroup> radioGroups = new ArrayList<RadioGroup>();
    ArrayList<Spinner> spinners = new ArrayList<Spinner>();
    ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
    ArrayList<ToggleButton> toggleButtons = new ArrayList<ToggleButton>();
    ArrayList<Switch> switches = new ArrayList<Switch>();
    ArrayList<TextView> textViewsDate = new ArrayList<TextView>();
    ArrayList<TextView> textViewsHour = new ArrayList<TextView>();
    ArrayList<TextView> textViewsFiles = new ArrayList<TextView>();
    ArrayList<String> stringsRegEx = new ArrayList<String>();
    ArrayList<TextView> textViewsAudio = new ArrayList<TextView>();

    Bitmap bmp;
    JSONObject jsonenvio = new JSONObject();
    String textDate = "Haga clic para obtener la Fecha";
    String textHour = "Haga clic para obtener la Hora";
    String textFile = "Haga clic para obtener el Archivo";
    String textAudio = "Haga clic para grabar el audio";
    String textImage = "Imagen";
    String obligatorio = "obligatorio";

    Handler hand = new Handler();
    String fecha_2;
    String fecha_1;
    String mLocation = "-1";

    MediaRecorder recorder;
    MediaPlayer player;
    String pathAudio = null;
    int idAudio = 0;
    int option = 0;
    String fullName;

    ArrayList<obj_attributes> objAttributes = new ArrayList<obj_attributes>();

    String currentPhotoPath;

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
    FingerPrintReader fingerPrintReader1;

    TextView tvFinger;
    ImageView ivFinger;
    int idFinger;
    String fingerCapture = "";

    String branch;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_par_form_fiels_pre);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        url = getString(R.string.servidor) + "/api/parFormFieldsPre/";
        url2 = getString(R.string.servidor) + "/api/savePre";

        llContenedor = findViewById(R.id.llContenedor);
        llRecording = findViewById(R.id.llRecording);
        btnSave = findViewById(R.id.btnSave);
        ivRecording = findViewById(R.id.ivRecording);
        ivPlaying = findViewById(R.id.ivPlaying);
        tvRecording = findViewById(R.id.tvRecording);
        tvRecording.setTextSize(14);
        tvRecording.setTextColor(getResources().getColor(R.color.colorBlack));
        tvPathRecording = findViewById(R.id.tvPathRecording);
        tvPathRecording.setTextSize(14);
        tvPathRecording.setText(textAudio);
        tvPathRecording.setHint("");

        tvFinger = findViewById(R.id.tvFinger);
        ivFinger = findViewById(R.id.ivFinger);

        Bundle parametros = this.getIntent().getExtras();
        auth = parametros.getString("auth");
        userName = parametros.getString("userName");
        fullName = parametros.getString("fullName");
        idFromPreReg = parametros.getInt("idFromPreReg");
        branch = parametros.getString("branch");

        //biometrico
        conversion = new HexConversion();

        try {
            usbPermission();
        } catch (Exception e) {
            Toast.makeText(parFormFielsPre.this, "Revise la conexión del USB", Toast.LENGTH_SHORT).show();
            ir = new Intent(parFormFielsPre.this, preReg.class);
            ir.putExtra("auth", auth);
            ir.putExtra("userName", userName);
            ir.putExtra("fullName", fullName);
            ir.putExtra("branch", branch);
            startActivity(ir);
            finish();
        }
        //biometrico

        hand.removeCallbacks(actualizar);
        hand.postDelayed(actualizar, 100);

        validarPermisos();

        url = url + idFromPreReg;

        if (compruebaConexion(this)) {

            cargarFormulario();

        } else {

            cargarFormularioOffline();

        }

        localizar();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnSave.setEnabled(false);
                int validar = 0;

                JSONArray respuesta = new JSONArray();

                int counterEditText = 0;
                for (Iterator iterator = editTexts.iterator(); iterator
                        .hasNext(); ) {

                    EditText editText = (EditText) iterator.next();
                    String obs_respuesta = editText.getText().toString().trim();
                    String control = editText.getHint().toString().trim();
                    editText.setTextColor(Color.BLACK);
                    String regEx = stringsRegEx.get(counterEditText);

                    Log.w("RegEx", regEx + " posicion " + counterEditText);

                    Log.w("controlEditText", control);

                    if (obs_respuesta.equals("") && control.equals(obligatorio)) {

                        validar++;
                        Log.w("sumaEditText", "" + validar);
                        editText.setHintTextColor(Color.RED);

                    } else {
                        if (!obs_respuesta.equals("")) {
                            if (!regEx.equals("")) {
                                if (!validarRegEx(obs_respuesta, regEx)) {
                                    validar++;
                                    Log.w("sumaEditText", "" + validar);
                                    editText.setTextColor(Color.RED);
                                }
                            }
                        }
                    }

                    Log.w("control", "" + validar);

                    Log.w("Edit", "editText" + " " + editText.getId() + " " + obs_respuesta);
                    try {
                        JSONObject parametros = new JSONObject();
                        parametros.put("idField", editText.getId());
                        parametros.put("valueInputField", obs_respuesta);
                        parametros.put("valueInputDateField", "");
                        parametros.put("valueListField", "");
                        parametros.put("valueFile", "");
                        respuesta.put(parametros);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    counterEditText++;

                }

                for (Iterator iterator = textViewsDate.iterator(); iterator
                        .hasNext(); ) {

                    TextView textView = (TextView) iterator.next();
                    String obs_respuesta = textView.getText().toString().trim();
                    String control = textView.getHint().toString().trim();

                    Log.w("controlTextViewDate", control);

                    if (obs_respuesta.equals(textDate) && control.equals(obligatorio)) {

                        validar++;
                        Log.w("sumaTextViewDate", "" + validar);

                    }

                    Log.w("TextViewDate", "TextView" + " " + textView.getId() + " " + obs_respuesta);
                    try {
                        JSONObject parametros = new JSONObject();
                        parametros.put("idField", textView.getId());
                        parametros.put("valueInputField", "");
                        parametros.put("valueInputDateField", obs_respuesta);
                        parametros.put("valueListField", "");
                        parametros.put("valueFile", "");
                        respuesta.put(parametros);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                for (Iterator iterator = textViewsHour.iterator(); iterator
                        .hasNext(); ) {

                    TextView textView = (TextView) iterator.next();
                    String obs_respuesta = textView.getText().toString().trim();
                    String control = textView.getHint().toString().trim();

                    if (obs_respuesta.equals(textHour) && control.equals(obligatorio)) {

                        validar++;
                        Log.w("sumaTextViewHour", "" + validar);

                    }

                    Log.w("controlTextViewHour", control);

                    Log.w("TextViewHour", "TextView" + " " + textView.getId() + " " + obs_respuesta);
                    try {
                        JSONObject parametros = new JSONObject();
                        parametros.put("idField", textView.getId());
                        parametros.put("valueInputField", obs_respuesta);
                        parametros.put("valueInputDateField", "");
                        parametros.put("valueListField", "");
                        parametros.put("valueFile", "");
                        respuesta.put(parametros);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                for (Iterator iterator = spinners.iterator(); iterator
                        .hasNext(); ) {

                    Spinner spinner = (Spinner) iterator.next();

                    int position = spinner.getSelectedItemPosition();

                    String nombre = spinner.getItemAtPosition(position).toString();

                    obj_params elegido = (obj_params) spinner.getItemAtPosition(position);

                    Log.w("Spinner", "spinner: " + spinner.getId() + " posicion: " + position + " " + elegido.getId());

                    /*try {
                        JSONObject parametros = new JSONObject();
                        parametros.put("idField", spinner.getId());
                        parametros.put("valueInputField", "");
                        parametros.put("valueInputDateField", "");
                        parametros.put("valueListField", elegido.getId());
                        parametros.put("valueFile", "");
                        respuesta.put(parametros);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                    if (elegido.control == 0) {
                        try {
                            JSONObject parametros = new JSONObject();
                            parametros.put("idField", spinner.getId());
                            parametros.put("valueInputField", "");
                            parametros.put("valueInputDateField", "");
                            parametros.put("valueListField", elegido.getId());
                            parametros.put("valueFile", "");
                            respuesta.put(parametros);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            JSONObject parametros = new JSONObject();
                            parametros.put("idField", spinner.getId());
                            parametros.put("valueInputField", elegido.getDescription());
                            parametros.put("valueInputDateField", "");
                            parametros.put("valueListField", -1);
                            parametros.put("valueFile", "");
                            respuesta.put(parametros);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }

                for (Iterator iterator = switches.iterator(); iterator
                        .hasNext(); ) {

                    Switch s = (Switch) iterator.next();

                    Log.w("Switch", "switches" + " " + s.getId() + " " + s.isChecked());
                    try {
                        JSONObject parametros = new JSONObject();
                        parametros.put("idField", s.getId());
                        parametros.put("valueInputField", s.isChecked());
                        parametros.put("valueInputDateField", "");
                        parametros.put("valueListField", "");
                        parametros.put("valueFile", "");
                        respuesta.put(parametros);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                for (Iterator iterator = imageViews.iterator(); iterator
                        .hasNext(); ) {

                    ImageView imageView = (ImageView) iterator.next();


                    String[] parts = imageView.getContentDescription().toString().trim().split("-");
                    String description = parts[0];
                    String control = parts[parts.length - 1];

                    if (description.equals(textImage) && control.equals(obligatorio)) {

                        validar++;
                        Log.w("sumaImageView", "" + validar);

                    }

                    Log.w("controlImageView", control);
                    /**********/
                    Bitmap bitmap = null;
                    for (Iterator iterator2 = objAttributes.iterator(); iterator2
                            .hasNext(); ) {
                        obj_attributes properties = (obj_attributes) iterator2.next();
                        if (imageView.getId() == properties.getId()) {
                            bitmap = properties.getImage();
                        }
                    }
                    /**********/
                    /*imageView.buildDrawingCache();
                    Bitmap bitmap = imageView.getDrawingCache();*/
                    String encoded = "";
                    if (bitmap != null) {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();

                        encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    }
                    if (description.equals(textImage)) {

                        encoded = "";

                    }

                    Log.w("Imagen", "imageView" + " " + imageView.getId() + " " + encoded);
                    try {
                        JSONObject parametros = new JSONObject();
                        parametros.put("idField", imageView.getId());
                        parametros.put("valueInputField", "");
                        parametros.put("valueInputDateField", "");
                        parametros.put("valueListField", "");
                        parametros.put("valueFile", encoded);
                        respuesta.put(parametros);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                for (Iterator iterator = textViewsFiles.iterator(); iterator
                        .hasNext(); ) {

                    TextView textView = (TextView) iterator.next();
                    textView.setTextColor(Color.BLACK);
                    String obs_respuesta = textView.getText().toString().trim();
                    String control = textView.getHint().toString().trim();

                    Log.w("controlTextViewFiles", control);

                    if (obs_respuesta.equals(textFile) && control.equals(obligatorio)) {

                        validar++;
                        Log.w("sumaTextViewFiles", "" + validar);

                    }

                    File file = new File(textView.getText().toString().trim());

                    int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));

                    Log.w("min", "" + file_size);

                    if (file_size > 2000) {

                        validar++;
                        textView.setTextColor(Color.RED);

                    }

                    String[] parts = textView.getText().toString().trim().split("/");
                    String nombre = parts[parts.length - 1];

                    byte[] fileArray = new byte[(int) file.length()];
                    InputStream inputStream;

                    String encodedFile = "";
                    try {
                        inputStream = new FileInputStream(file);
                        inputStream.read(fileArray);
                        encodedFile = Base64.encodeToString(fileArray, Base64.DEFAULT);
                    } catch (Exception e) {
                        // Manejar Error
                    }

                    if (obs_respuesta.equals(textFile)) {

                        encodedFile = "";

                    }

                    Log.w("File", "files" + " " + textView.getId() + "nombre" + nombre);
                    try {
                        JSONObject parametros = new JSONObject();
                        parametros.put("idField", textView.getId());
                        parametros.put("valueInputField", nombre);
                        parametros.put("valueInputDateField", "");
                        parametros.put("valueListField", "");
                        parametros.put("valueFile", encodedFile);
                        respuesta.put(parametros);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                /****************************************/

                for (Iterator iterator = textViewsAudio.iterator(); iterator
                        .hasNext(); ) {

                    TextView textView = (TextView) iterator.next();
                    textView.setTextColor(Color.BLACK);
                    String obs_respuesta = textView.getText().toString().trim();
                    String control = textView.getHint().toString().trim();

                    Log.w("controlTextViewFiles", control);

                    if (obs_respuesta.equals(textAudio) && control.equals(obligatorio)) {

                        validar++;
                        Log.w("sumaTextViewFiles", "" + validar);

                    }

                    File file = new File(textView.getText().toString().trim());

                    int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));

                    Log.w("min", "" + file_size);

                    if (file_size > 2000) {

                        validar++;
                        textView.setTextColor(Color.RED);

                    }

                    String[] parts = textView.getText().toString().trim().split("/");
                    String nombre = parts[parts.length - 1];

                    byte[] fileArray = new byte[(int) file.length()];
                    InputStream inputStream;

                    String encodedFile = "";
                    try {
                        inputStream = new FileInputStream(file);
                        inputStream.read(fileArray);
                        encodedFile = Base64.encodeToString(fileArray, Base64.DEFAULT);
                    } catch (Exception e) {
                        // Manejar Error
                    }

                    if (obs_respuesta.equals(textAudio)) {

                        encodedFile = "";

                    }

                    Log.w("File", "files" + " " + textView.getId() + "nombre" + nombre);
                    try {
                        JSONObject parametros = new JSONObject();
                        parametros.put("idField", textView.getId());
                        parametros.put("valueInputField", nombre);
                        parametros.put("valueInputDateField", "");
                        parametros.put("valueListField", "");
                        parametros.put("valueFile", encodedFile);
                        respuesta.put(parametros);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                /****************************************/

                /**********************/

                if (fingerCapture.equals("")) {
                    //validar++;
                } else {
                    try {
                        JSONObject parametros = new JSONObject();
                        parametros.put("idField", idFinger);
                        parametros.put("valueInputField", "huella" + idFinger);
                        parametros.put("valueInputDateField", "");
                        parametros.put("valueListField", "");
                        parametros.put("valueFile", fingerCapture);
                        respuesta.put(parametros);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                /*********************/

                localizar();

                //Log.w("json", "" + respuesta);
                try {
                    jsonenvio.put("dateRegPre", fecha_2);
                    jsonenvio.put("idForm", idFromPreReg);
                    jsonenvio.put("P", respuesta);

                    //Log.w("json", "" + jsonenvio);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (validar == 0) {

                    if (compruebaConexion(parFormFielsPre.this)) {

                        enviarformulario();

                    } else {

                        try {
                            Log.w("conexion", "no hay red");
                            bd conexion = new bd(parFormFielsPre.this);
                            conexion.abrir();
                            String answer = createAnswerJson(jsonenvio);
                            conexion.createAnswers(userName, auth, answer, branch);
                            conexion.cerrar();
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                } else {

                    completarDatos();
                    btnSave.setEnabled(true);

                }

            }

        });

        ivRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("option", "" + option);
                switch (option) {
                    case 0:
                        Log.w("seleccion", "record");
                        recorder = new MediaRecorder();
                        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        recorder.setOutputFile(pathAudio);
                        try {
                            recorder.prepare();
                        } catch (IOException e) {
                        }
                        recorder.start();
                        ivRecording.setImageDrawable(getResources().getDrawable(R.drawable.stop));
                        ivPlaying.setVisibility(View.INVISIBLE);
                        option++;
                        break;
                    case 1:
                        Log.w("seleccion", "stop");
                        recorder.stop();
                        recorder.release();
                        player = new MediaPlayer();
                        player.setOnCompletionListener(parFormFielsPre.this);
                        try {
                            player.setDataSource(pathAudio);
                        } catch (IOException e) {
                        }
                        try {
                            player.prepare();
                        } catch (IOException e) {
                        }
                        ivRecording.setImageDrawable(getResources().getDrawable(R.drawable.recording));
                        ivPlaying.setVisibility(View.VISIBLE);
                        tvPathRecording.setText(pathAudio);
                        option = 0;
                        break;
                }
            }
        });

        ivPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.start();
            }
        });

        ivFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            fingerPrintReader1.readFingerPrint();
                            ivFinger.setImageBitmap(fingerPrintReader1
                                    .toGrayscale(fingerPrintReader1.getFPBitMap()));

                            byte[] abc = fingerPrintReader1.getHexTemplate();
                            String Temp = conversion.getHexString(abc);
                            Log.d(TAG, "Template" + Temp);

                            fingerCapture = Base64.encodeToString(abc, Base64.DEFAULT);

                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(parFormFielsPre.this, "Revise la conexión del USB", Toast.LENGTH_SHORT).show();
                    ir = new Intent(parFormFielsPre.this, preReg.class);
                    ir.putExtra("auth", auth);
                    ir.putExtra("userName", userName);
                    ir.putExtra("fullName", fullName);
                    ir.putExtra("branch", branch);
                    startActivity(ir);
                    finish();
                }
            }
        });

    }

    private void cargarFormulario() {

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        mRequestQueue = Volley.newRequestQueue(this);

        mJsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(JSONArray response) {

                Log.w("respuesta", "" + response);

                mProgressDialog.dismiss();

                try {
                    createJson(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject form = response.getJSONObject(i);

                        idField = form.getInt("idField");
                        //String name = form.getString("name");
                        String description = form.getString("description");
                        int position = form.getInt("position");
                        int type = form.getInt("type");
                        int idparameter = form.getInt("idParameter");
                        int input_max = form.getInt("inputMax");
                        String input_regx = form.getString("inputRegex");
                        int photo_resolution_w = form.getInt("photoResolutionW");
                        int photo_resolution_h = form.getInt("photoResolutionH");
                        //String photo_resolution = form.getString("PHOTO_RESOLUTION");
                        int file_size = form.getInt("fileSize");
                        //int reg_begin = form.getInt("REG_BEGIN");
                        //int reg_end = form.getInt("REG_END");
                        String is_mandatory = form.getString("isMandatory");
                        //int is_keybaule = form.getInt("IS_KEYVALUE");
                        int idValue_other =form.getInt("IDVALUE_OTHER");

                        JSONArray opciones = form.getJSONArray("P");

                        String[] listopcion = new String[opciones.length()];

                        ArrayList<obj_params> itemp = new ArrayList<obj_params>();

                        for (int j = 0; j < opciones.length(); j++) {

                            JSONObject op = opciones.getJSONObject(j);
                            int valor = op.getInt("idValue");
                            String des = op.getString("description");

                            listopcion[j] = des;

                            itemp.add(new obj_params(valor, des, 0));

                            Log.w("Description opcion", listopcion[j]);

                        }


                        switch (type) {

                            case 1:

                                creartextview(description);
                                crearedittext(idField, is_mandatory, input_max, input_regx);

                                break;

                            case 2:

                                creartextview(description);
                                crearedittextmultilinea(idField, is_mandatory, input_max);

                                break;

                            case 3:

                                creartextview(description);
                                createSpinner(idField, itemp, input_max);


                                break;

                            case 4:

                                creartextview(description);
                                createTextViewDate(idField, is_mandatory);

                                break;

                            case 5:

                                creartextview(description);
                                createTextViewHour(idField, is_mandatory);

                                break;

                            case 6:

                                creartextview(description);
                                createImageView(idField, is_mandatory, photo_resolution_w, photo_resolution_h);

                                break;

                            case 7:

                                creartextview(description);
                                createTextviewFile(idField, is_mandatory);

                                break;

                            case 8:

                                creartextview(description);
                                //createSpinner(idField, itemp, input_max);
                                createSpinner2(idField, itemp, input_max,idValue_other);

                                break;

                            case 9:

                                creartextview(description);
                                createSwitch(idField, "SI    /    NO");

                                break;

                            case 10:

                                createtextViewTitle(description);

                                break;

                            case 11:

                                /*tvRecording.setVisibility(View.VISIBLE);
                                tvRecording.setText(description);
                                llRecording.setVisibility(View.VISIBLE);
                                idAudio = idField;
                                tvPathRecording.setHint(is_mandatory);
                                /***********************/
                                /*String carpeta = "geoport";
                                File fileAudio = new File(Environment.getExternalStorageDirectory(), carpeta);
                                boolean isCreada = fileAudio.exists();
                                String nameAudio = "";

                                if(isCreada == false) {

                                    isCreada = fileAudio.mkdir();

                                }

                                if(isCreada == true) {

                                    nameAudio = "AudioGesport" + fecha_1 +".3gp";

                                }

                                pathAudio = Environment.getExternalStorageDirectory() + File.separator + carpeta + File.separator + nameAudio;*/
                                /**********************/
                                /*creartextview(description);
                                createTextviewAudio(idField,is_mandatory);*/

                                createAudio(idField, description, is_mandatory);

                                break;

                            case 12:
                                idFinger = idField;
                                tvFinger.setText(description);
                                break;

                            default:
                                break;

                        }

                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                    Log.w("jsonException", "" + e);

                }

            }


        }, new Response.ErrorListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onErrorResponse(VolleyError error) {

                mProgressDialog.dismiss();
                cargarFormularioOffline();

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

        mRequestQueue.add(mJsonArrayRequest);

    }

    // crear textview en el contenedor

    public void creartextview(String texto) {

        TextView tv;
        tv = new TextView(this);
        tv.setText(texto);
        tv.setTextSize(14);
        LinearLayout.LayoutParams lastTxtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lastTxtParams.setMargins(0, 30, 0, 0);
        tv.setLayoutParams(lastTxtParams);
        //tv.setTextColor(getResources().getColor(R.color.colorBlack));
        tv.setTextAppearance(this, R.style.colorText);
        llContenedor.addView(tv);

    }

    // crear textview en el contenedor

    public void createtextViewTitle(String texto) {

        TextView tv;
        tv = new TextView(this);
        tv.setText(texto);
        tv.setTextAppearance(this, R.style.colorTitle);
        llContenedor.addView(tv);

    }

    // crear edittext en el contenedor

    public void crearedittext(int id_opcion, String opcion, int descripcion, String regEx) {


        EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setTextSize(14);
        et.setTextColor(getResources().getColor(R.color.colorTextVariable));
        et.setHint(opcion);
        et.setId(id_opcion);
        /***************/
        et.setBackgroundResource(R.drawable.customedittext);
        et.setPadding(30, 20, 30, 20);
        /***************/
        InputFilter[] ifet = new InputFilter[1];
        ifet[0] = new InputFilter.LengthFilter(descripcion);
        et.setFilters(ifet);

        String[][] reemplazos = {{"(", "{"}, {")", "}"}, {"<", "["}, {">", "]"}, {"¿", "("}, {"?", ")"}};
        String cadena = regEx;
        for (String[] reemplazar : reemplazos) {
            cadena = cadena.replace(reemplazar[0], reemplazar[1]);
        }

        llContenedor.addView(et);
        editTexts.add(et);
        stringsRegEx.add(cadena);

    }

    // crear edittext multininea en el contenedor

    public void crearedittextmultilinea(int id_opcion, String opcion, int descripcion) {

        EditText et = new EditText(this);
        et.setSingleLine(false);
        et.setTextSize(14);
        et.setTextColor(getResources().getColor(R.color.colorTextVariable));
        et.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        /***************/
        et.setBackgroundResource(R.drawable.customedittext);
        et.setPadding(30, 20, 30, 20);
        /***************/
        et.setLines(1);
        et.setMaxLines(10);
        et.setVerticalScrollBarEnabled(true);
        et.setMovementMethod(ScrollingMovementMethod.getInstance());
        et.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        et.setHint(opcion);
        et.setId(id_opcion);
        InputFilter[] ifet = new InputFilter[1];
        ifet[0] = new InputFilter.LengthFilter(descripcion);
        et.setFilters(ifet);
        llContenedor.addView(et);
        editTexts.add(et);
        stringsRegEx.add("");

    }

    // crear TextViewDate en el contenedor

    public void createTextViewDate(int id, String option) {

        TextView textView = new TextView(this);
        textView.setId(id);
        textView.setTextSize(14);
        textView.setText(textDate);
        textView.setHint(option);
        textView.setOnClickListener(parFormFielsPre.this);

        textViewsDate.add(textView);
        llContenedor.addView(textView);

    }

    // crear TextViewHour en el contenedor
    public void createTextViewHour(int id, String option) {

        TextView textView = new TextView(this);
        textView.setId(id);
        textView.setTextSize(14);
        textView.setText(textHour);
        textView.setHint(option);
        textView.setOnClickListener(parFormFielsPre.this);

        textViewsHour.add(textView);
        llContenedor.addView(textView);

    }

    // crear radiobutton en el contenedor

    public void crearradiobutton(int idField, ArrayList<obj_params> items) {

        RadioGroup rg = new RadioGroup(this);
        rg.setId(idField);

        Log.w("RadioButton", "llegue aqui");

        Log.w("RadioButton", "" + items);

        for (Iterator iterator = items.iterator(); iterator
                .hasNext(); ) {

            Log.w("RadioButton", "llegue aqui2");

            obj_params obj = (obj_params) iterator.next();

            RadioButton rb = new RadioButton(this);

            rb.setId(obj.getId());
            rb.setText(obj.getDescription());

            rg.addView(rb);

            Log.w("RadioButton", "editText" + " " + obj.getId() + " " + obj.getDescription());

        }

        llContenedor.addView(rg);
        radioGroups.add(rg);

    }

    // crear ToggleButton
    public void createToggleButton(int idField) {

        ToggleButton tb = new ToggleButton(this);
        tb.setId(idField);
        tb.setChecked(true);

        llContenedor.addView(tb);
        toggleButtons.add(tb);

    }

    // crear Switch
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void createSwitch(int idField, String description) {


        Switch s = new Switch(this);
        s.setId(idField);
        s.setText(description);
        s.setTextSize(14);
        s.setTextColor(getResources().getColor(R.color.colorBlack));
        s.getThumbDrawable().setColorFilter(Color.parseColor("#2F3887"), PorterDuff.Mode.MULTIPLY);
        s.getTrackDrawable().setColorFilter(Color.parseColor("#2F3887"), PorterDuff.Mode.MULTIPLY);
        s.setTextOn("Si");
        s.setTextOff("No");
        s.setChecked(true);
        s.setOnClickListener(this);
        Log.w("Switch", "su id es  " + s.getId());

        llContenedor.addView(s);
        switches.add(s);


    }

    // crear checkbox en el contenedor

    public void crearcheckbox(int id_opcion, String opcion) {

        CheckBox cb = new CheckBox(this);
        cb.setText(opcion);
        cb.setTextColor(getResources().getColor(R.color.colorBlack));
        cb.setId(id_opcion);
        llContenedor.addView(cb);
        checkBoxs.add(cb);

    }

    // crear un spinner en el contenedor

    public void createSpinner(int idField, ArrayList<obj_params> aux, int idParametro) {

        Spinner sp = new Spinner(this);
        sp.setId(idField);
        adapter_params adapter = new adapter_params(parFormFielsPre.this, aux);
        sp.setAdapter(adapter);
        spinners.add(sp);
        llContenedor.addView(sp);
        int posicion = 0;
        for (int i = 0; i < sp.getCount(); i++) {

            obj_params elegido = (obj_params) sp.getItemAtPosition(i);
            if (elegido.getId() == idParametro) {
                posicion = i;
            }
        }
        sp.setSelection(posicion);

    }

    // Crear imageview en el contenedor

    public void createImageView(int idField, String option, int w, int h) {

        LinearLayout llImg = new LinearLayout(this);
        LinearLayout.LayoutParams paramsImg = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llImg.setLayoutParams(paramsImg);
        llImg.setOrientation(LinearLayout.HORIZONTAL);
        llImg.setWeightSum(12);
        llContenedor.addView(llImg);

        ImageView iv = new ImageView(this);
        iv.setId(idField);
        iv.setImageResource(R.drawable.camera);
        iv.setContentDescription(textImage + "-" + option);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150, 5f);
        iv.setLayoutParams(lp);
        llImg.addView(iv);

        LinearLayout llImg2 = new LinearLayout(this);
        LinearLayout.LayoutParams paramsImg2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 5f);
        llImg2.setLayoutParams(paramsImg2);
        llImg.addView(llImg2);

        LinearLayout llImg3 = new LinearLayout(this);
        LinearLayout.LayoutParams paramsImg3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 2f);
        llImg3.setLayoutParams(paramsImg3);
        llImg.addView(llImg3);

        imageViews.add(iv);

        if (w > 0 && h > 0) {
            objAttributes.add(new obj_attributes(idField, w, h, null));
        } else {
            w = 400;
            h = 400;
            objAttributes.add(new obj_attributes(idField, w, h, null));
        }

        iv.setOnClickListener(this);

    }

    public void createTextviewFile(int idField, String option) {

        TextView textView = new TextView(this);
        textView.setId(idField);
        textView.setTextSize(14);
        textView.setText(textFile);
        textView.setHint(option);
        textView.setOnClickListener(this);

        llContenedor.addView(textView);
        textViewsFiles.add(textView);

    }

    private void enviarformulario() {

        Log.w("url", url2);

        JsonObjectRequest mjsonObjectRequest;

        RequestQueue mRequestQueue2;

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        mRequestQueue2 = Volley.newRequestQueue(this);

        mjsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url2, jsonenvio, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.w("mio", "" + response);
                /*msj = Toast.makeText(form_event.this, "" + response, Toast.LENGTH_LONG);
                msj.show();*/
                mProgressDialog.dismiss();
                ir = new Intent(parFormFielsPre.this, preReg.class);
                ir.putExtra("auth", auth);
                ir.putExtra("userName", userName);
                ir.putExtra("fullName", fullName);
                ir.putExtra("branch", branch);
                startActivity(ir);
                finish();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.w("mio", "" + error);
                try {
                    Log.w("conexion", "no hay red");
                    bd conexion = new bd(parFormFielsPre.this);
                    conexion.abrir();
                    String answer = createAnswerJson(jsonenvio);
                    conexion.createAnswers(userName, auth, answer, branch);
                    conexion.cerrar();
                    mProgressDialog.dismiss();
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }

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

        mRequestQueue2.add(mjsonObjectRequest);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {

        /*msj = Toast.makeText(this, "" + v.getId(), Toast.LENGTH_LONG);
        msj.show();*/

        opcion = v.getId();

        boolean usarCamara = false;
        boolean date = false;
        boolean uploadFile = false;
        boolean recordingAudio = false;

        for (Iterator iterator = imageViews.iterator(); iterator
                .hasNext(); ) {

            ImageView imageView = (ImageView) iterator.next();

            if (imageView.getId() == opcion) {

                usarCamara = true;

            }

            Log.w("Edit", imageView.getId() + " = " + opcion);


        }

        for (Iterator iterator = textViewsDate.iterator(); iterator
                .hasNext(); ) {

            TextView textView = (TextView) iterator.next();

            if (textView.getId() == opcion) {

                date = true;

            }

        }

        for (Iterator iterator = textViewsHour.iterator(); iterator
                .hasNext(); ) {

            TextView textView = (TextView) iterator.next();

            if (textView.getId() == opcion) {

                getHour();

            }

        }

        for (Iterator iterator = textViewsFiles.iterator(); iterator
                .hasNext(); ) {

            TextView textView = (TextView) iterator.next();

            if (textView.getId() == opcion) {

                uploadFile = true;

            }

        }

        /*****************************/

        /*****************************/

        for (Iterator iterator = switches.iterator(); iterator
                .hasNext(); ) {

            Switch s = (Switch) iterator.next();

            if (s.getId() == opcion) {

                if (s.isChecked()) {

                    s.getThumbDrawable().setColorFilter(Color.parseColor("#2F3887"), PorterDuff.Mode.MULTIPLY);
                    s.getTrackDrawable().setColorFilter(Color.parseColor("#2F3887"), PorterDuff.Mode.MULTIPLY);

                } else {

                    s.getThumbDrawable().setColorFilter(Color.parseColor("#3f8155"), PorterDuff.Mode.MULTIPLY);
                    s.getTrackDrawable().setColorFilter(Color.parseColor("#3f8155"), PorterDuff.Mode.MULTIPLY);

                }

            }

        }

        if (date) {

            getDate();

        }

        if (usarCamara) {

            tomarFotografia();

        }

        if (uploadFile) {

            recoverDataFile();

        }

    }

    //recuperar archivo
    public void recoverDataFile() {

        new MaterialFilePicker()
                .withActivity(parFormFielsPre.this)
                .withRequestCode(codigoFile)
                .start();

    }


    // camara
    private void tomarFotografia() {

        /*File fileImagen = new File(Environment.getExternalStorageDirectory(), ruta_imagen);
        boolean isCreada = fileImagen.exists();
        String nombreImagen = "";


        if(isCreada == false) {

            isCreada = fileImagen.mkdir();

        }

        if(isCreada == true) {

            nombreImagen = (System.currentTimeMillis()/1000) + ".jpg";

        }

        path = Environment.getExternalStorageDirectory() + File.separator + ruta_imagen + File.separator + nombreImagen;

        File imagen =  new File(path);

        output = Uri.fromFile(imagen);
        if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.N) {
            output = Uri.parse(path);
        } else{
            output = Uri.fromFile(new File(path));
        }*/

        /*String carpeta = "geoport";
        File fileJson = new File(Environment.getExternalStorageDirectory(), carpeta);
        boolean isCreada = fileJson.exists();
        String nombreImg = "";

        if(isCreada == false) {

            isCreada = fileJson.mkdir();

        }

        if(isCreada == true) {

            nombreImg = "img" + (System.currentTimeMillis()/1000) + ".json";

        }

        path = Environment.getExternalStorageDirectory() + File.separator + carpeta + File.separator + nombreImg;

        File imagen =  new File(path);

        output = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", imagen);


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
        startActivityForResult(intent, codigoCamera);*/

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "diggate.xpertise.com.diggateapk.fileprovider", photoFile);
                //Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, codigoCamera);
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {


            switch (requestCode) {

                case codigoCamera:

                    /*Bundle ext = data.getExtras();
                    bmp = (Bitmap) ext.get("data");*/
                    ImageView iv = findViewById(opcion);

                    String[] parts = iv.getContentDescription().toString().trim().split("-");
                    String description = parts[0] + "captura";
                    String control = parts[parts.length - 1];

                    iv.setContentDescription(description + "-" + control);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(400, 400);
                    iv.setLayoutParams(lp);

                    Log.w("Img_src", currentPhotoPath);
                    //iv.setImageURI(Uri.parse(currentPhotoPath));

                    Uri imageUri = Uri.parse(currentPhotoPath);
                    try {
                        bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    /*ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);*/

                    iv.setImageBitmap(bmp);

                    /***********************************/
                    /*
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream .toByteArray();

                    String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                    ImageView iv = findViewById(opcion);

                    String[] parts = iv.getContentDescription().toString().trim().split("-");
                    String description = parts[0] + "captura";
                    String control = parts[parts.length - 1];

                    iv.setContentDescription(description + "-" + control);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(400,400);
                    iv.setLayoutParams(lp);

                    iv.setImageBitmap(bmp);*/

                    for (Iterator iterator2 = objAttributes.iterator(); iterator2
                            .hasNext(); ) {
                        obj_attributes properties = (obj_attributes) iterator2.next();
                        if (opcion == properties.getId()) {
                            int width = bmp.getWidth();
                            int height = bmp.getHeight();
                            int newWidth = properties.w;
                            int newHeight = properties.h;

                            // calculamos el escalado de la imagen destino
                            float scaleWidth = ((float) newWidth) / width;
                            float scaleHeight = ((float) newHeight) / height;

                            // para poder manipular la imagen
                            // debemos crear una matriz

                            Matrix matrix = new Matrix();
                            // resize the Bitmap
                            matrix.postScale(scaleWidth, scaleHeight);

                            // volvemos a crear la imagen con los nuevos valores
                            Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0,
                                    width, height, matrix, true);
                            properties.setImage(resizedBitmap);
                        }
                    }
                    break;

                case codigoFile:

                    String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);

                    TextView textView = findViewById(opcion);
                    textView.setText(filePath);

                    Log.w("path", filePath);

                    break;

            }

            /*MediaScannerConnection.scanFile(this, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String path, Uri uri) {

                    Log.i("Ruta de almacenamiento", "path: " + path);

                }
            });

            Bitmap bitmap = BitmapFactory.decodeFile(path);
            ImageView iv = findViewById(opcion);
            iv.setImageBitmap(bitmap);*/


        }
    }

    private boolean validarPermisos() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

            return true;

        }
        if ((checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {

            return true;

        }

        if ((shouldShowRequestPermissionRationale(CAMERA)) || (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))) {

            cargardialogo();

        } else {

            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, 100);

        }

        return false;

    }

    private void cargardialogo() {

        AlertDialog.Builder builder = new AlertDialog.Builder(parFormFielsPre.this);
        builder.setTitle("Permisos Desactivados");
        builder.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, 100);
                }

            }
        });
        builder.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {

            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {


            } else {

                cargardialogo2();

            }

        }

    }

    private void cargardialogo2() {

        final CharSequence[] op = {"si", "no"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(parFormFielsPre.this);
        builder.setTitle("Desea configurar los permisos manualmente?");
        builder.setItems(op, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (op[which].equals("si")) {

                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);

                } else {

                    msj = Toast.makeText(parFormFielsPre.this, "los permisos no fueron aceptados", Toast.LENGTH_LONG);
                    msj.show();
                    dialog.dismiss();

                }

            }
        });
        builder.show();

    }

    // actualiza la fecha

    private Runnable actualizar = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub

            SimpleDateFormat fmt1 = new SimpleDateFormat("yyyyMMddHHmmss");
            SimpleDateFormat fmt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            fecha_1 = fmt1.format(date);
            fecha_2 = fmt2.format(date);
            hand.postDelayed(this, 1000);

        }

    };

    //alert dialog para obtener fecha
    public void getDate() {

        int mYear, mMonth, mDay;
        Calendar mcurrentDate = Calendar.getInstance();
        mYear = mcurrentDate.get(Calendar.YEAR);
        mMonth = mcurrentDate.get(Calendar.MONTH);
        mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mdatePickerDialog = new DatePickerDialog(parFormFielsPre.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                TextView textView = new TextView(parFormFielsPre.this);
                textView = findViewById(opcion);
                int aux = month + 1;
                String fecha = "" + year + "-" + aux + "-" + dayOfMonth;
                Log.w("fecha", fecha);
                textView.setText(fecha);

            }
        }, mYear, mMonth, mDay);
        mdatePickerDialog.setTitle("Selecione la fecha");
        mdatePickerDialog.show();

    }

    //alert dialog para obtener hora
    public void getHour() {

        int mHour, mMinute;
        Calendar mcurrentDate = Calendar.getInstance();
        mHour = mcurrentDate.get(Calendar.HOUR_OF_DAY);
        mMinute = mcurrentDate.get(Calendar.MINUTE);

        TimePickerDialog mTimePickerDialog = new TimePickerDialog(parFormFielsPre.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                TextView textView = new TextView(parFormFielsPre.this);
                textView = findViewById(opcion);
                textView.setText(hourOfDay + ":" + minute);
                Log.w("Hora", hourOfDay + ":" + minute);

            }
        }, mHour, mMinute, false);

        mTimePickerDialog.setTitle("Selecione la Hora");
        mTimePickerDialog.show();

    }

    public void completarDatos() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Importante");
        builder.setMessage("Debe completar todos los datos Requeridos o llenarlos correctamente");
        builder.setPositiveButton("Aceptar", null);
        builder.create();
        builder.show();

    }

    private boolean validarRegEx(String datos, String exreg) {

        Pattern pattern = Pattern.compile(exreg);
        return pattern.matcher(datos).matches();

    }

    public void localizar() {

        LocationManager lm;
        LocationListener datos;
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        datos = new LocationListener() {

            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO Auto-generated method stub

            }

            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub

                Toast.makeText(getApplicationContext(), "Gps Activo", Toast.LENGTH_SHORT).show();

            }

            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub

                Toast.makeText(getApplicationContext(), "Gps Desactivado", Toast.LENGTH_SHORT).show();

            }

            public void onLocationChanged(Location loc) {
                // TODO Auto-generated method stub

                loc.getLongitude();
                loc.getLatitude();
                mLocation = "" + loc.getLatitude() + " , " + loc.getLongitude();

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, datos);

    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void cargarFormularioOffline() {

        try {
            String formulario = readJsonFile("/storage/emulated/0/DigGate/formPre" + idFromPreReg + ".json");
            if (formulario == null) {

                msj = Toast.makeText(this, "No hay datos para mostrar", Toast.LENGTH_LONG);
                msj.setGravity(Gravity.CENTER, 0, 0);
                msj.show();

            } else {

                try {

                    JSONArray response = new JSONArray(formulario);

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject form = response.getJSONObject(i);

                        idField = form.getInt("IDFIELD");
                        String name = form.getString("NAME");
                        String description = form.getString("DESCRIPTION");
                        int position = form.getInt("POSITION");
                        int type = form.getInt("TYPE");
                        int idparameter = form.getInt("IDPARAMETER");
                        int input_max = form.getInt("INPUT_MAX");
                        String input_regx = form.getString("INPUT_REGEX");
                        int photo_resolution_w = form.getInt("PHOTO_RESOLUTION_W");
                        int photo_resolution_h = form.getInt("PHOTO_RESOLUTION_H");
                        //String photo_resolution = form.getString("PHOTO_RESOLUTION");
                        int file_size = form.getInt("FILE_SIZE");
                        int reg_begin = form.getInt("REG_BEGIN");
                        int reg_end = form.getInt("REG_END");
                        String is_mandatory = form.getString("IS_MANDATORY");
                        int is_keybaule = form.getInt("IS_KEYVALUE");
                        int idValue_other =form.getInt("IDVALUE_OTHER");

                        JSONArray opciones = form.getJSONArray("P");

                        String[] listopcion = new String[opciones.length()];

                        ArrayList<obj_params> itemp = new ArrayList<obj_params>();

                        for (int j = 0; j < opciones.length(); j++) {

                            JSONObject op = opciones.getJSONObject(j);
                            int valor = op.getInt("IDVALUE");
                            String des = op.getString("DESCRIPTION");

                            listopcion[j] = des;

                            itemp.add(new obj_params(valor, des, 0));

                            Log.w("Description opcion", listopcion[j]);

                        }


                        switch (type) {

                            case 1:

                                creartextview(description);
                                crearedittext(idField, is_mandatory, input_max, input_regx);

                                break;

                            case 2:

                                creartextview(description);
                                crearedittextmultilinea(idField, is_mandatory, input_max);

                                break;

                            case 3:

                                creartextview(description);
                                createSpinner(idField, itemp, input_max);


                                break;

                            case 4:

                                creartextview(description);
                                createTextViewDate(idField, is_mandatory);

                                break;

                            case 5:

                                creartextview(description);
                                createTextViewHour(idField, is_mandatory);

                                break;

                            case 6:

                                creartextview(description);
                                createImageView(idField, is_mandatory, photo_resolution_w, photo_resolution_h);

                                break;

                            case 7:

                                creartextview(description);
                                createTextviewFile(idField, is_mandatory);

                                break;

                            case 8:

                                creartextview(description);
                                //createSpinner(idField, itemp, input_max);
                                createSpinner2(idField, itemp, input_max,idValue_other);

                                break;

                            case 9:

                                creartextview(description);
                                createSwitch(idField, "SI    /    NO");

                                break;

                            case 10:

                                createtextViewTitle(description);

                                break;

                            case 11:

                                /*creartextview(description);
                                createTextviewAudio(idField,is_mandatory);*/
                                /*tvRecording.setVisibility(View.VISIBLE);
                                tvRecording.setText(description);
                                llRecording.setVisibility(View.VISIBLE);
                                idAudio = idField;
                                tvPathRecording.setHint(is_mandatory);
                                /***********************/
                                /*String carpeta = "geoport";
                                File fileAudio = new File(Environment.getExternalStorageDirectory(), carpeta);
                                boolean isCreada = fileAudio.exists();
                                String nameAudio = "";

                                if(isCreada == false) {

                                    isCreada = fileAudio.mkdir();

                                }

                                if(isCreada == true) {

                                    nameAudio = "AudioGesport" + fecha_1 +".3gp";

                                }

                                pathAudio = Environment.getExternalStorageDirectory() + File.separator + carpeta + File.separator + nameAudio;
                                /**********************/
                                createAudio(idField, description, is_mandatory);

                                break;

                            case 12:
                                idFinger = idField;
                                tvFinger.setText(description);
                                break;

                            default:
                                break;

                        }

                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                    Log.w("jsonException", "" + e);

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
        String carpeta = "DigGate";
        File fileJson = new File(Environment.getExternalStorageDirectory(), carpeta);
        boolean isCreada = fileJson.exists();
        String nombreJson = "";

        if (isCreada == false) {

            isCreada = fileJson.mkdir();

        }

        if (isCreada == true) {

            nombreJson = "formPre" + idFromPreReg + ".json";

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

    public String createAnswerJson(JSONObject jsonArray) {

        String path = null;
        String carpeta = "DigGate";
        File fileJson = new File(Environment.getExternalStorageDirectory(), carpeta);
        boolean isCreada = fileJson.exists();
        String nombreJson = "";

        if (isCreada == false) {

            isCreada = fileJson.mkdir();

        }

        if (isCreada == true) {

            nombreJson = "AnswerPre" + fecha_1 + ".json";

        }

        path = Environment.getExternalStorageDirectory() + File.separator + carpeta + File.separator + nombreJson;


        try {
            FileWriter writer = new FileWriter(path);
            writer.write(String.valueOf(jsonArray));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return path;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void createAudio(int id, String descripcion, String requerido) {
        final int[] option = {0};
        final String[] pathAudio = {null};
        final MediaRecorder[] recorder = new MediaRecorder[1];
        final MediaPlayer[] player = new MediaPlayer[1];
        TextView textViewAudio = new TextView(this);
        textViewAudio.setText(descripcion);
        textViewAudio.setTextSize(14);
        //textViewAudio.setTextColor(getResources().getColor(R.color.colorBlack));
        textViewAudio.setTextAppearance(R.style.colorText);
        llContenedor.addView(textViewAudio);
        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(lp);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setWeightSum(12);
        linearLayout.setGravity(Gravity.CENTER);
        llContenedor.addView(linearLayout);
        final TextView textView = new TextView(this);
        textView.setId(id);
        textView.setText(textAudio);
        textView.setHint(requerido);
        textView.setTextSize(14);
        LinearLayout.LayoutParams lpTextView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
        textView.setLayoutParams(lpTextView);
        linearLayout.addView(textView);
        textViewsAudio.add(textView);
        //viaibilidad
        textView.setVisibility(View.GONE);
        final ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.recording);
        LinearLayout.LayoutParams lpImageView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 150, 5f);
        imageView.setLayoutParams(lpImageView);
        linearLayout.addView(imageView);
        final ImageView imageView2 = new ImageView(this);
        imageView2.setImageResource(R.drawable.play);
        LinearLayout.LayoutParams lpImageView2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 150, 5f);
        imageView2.setLayoutParams(lpImageView2);
        imageView2.setVisibility(View.INVISIBLE);
        linearLayout.addView(imageView2);
        /**********************/
        final TextView textView1 = new TextView(this);
        textView1.setId(id);
        textView1.setText(textAudio);
        textView1.setHint(requerido);
        textView1.setTextSize(14);
        LinearLayout.LayoutParams lpTextView1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
        textView1.setLayoutParams(lpTextView1);
        linearLayout.addView(textView1);
        textView1.setVisibility(View.INVISIBLE);
        /**********************/

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("option", "" + option[0]);
                switch (option[0]) {
                    case 0:
                        String carpeta = "DigGate";
                        File fileAudio = new File(Environment.getExternalStorageDirectory(), carpeta);
                        boolean isCreada = fileAudio.exists();
                        String nameAudio = "";

                        if (isCreada == false) {

                            isCreada = fileAudio.mkdir();

                        }

                        if (isCreada == true) {

                            nameAudio = "AudioGesport" + fecha_1 + ".3gp";

                        }

                        pathAudio[0] = Environment.getExternalStorageDirectory() + File.separator + carpeta + File.separator + nameAudio;
                        Log.w("seleccion", "record");
                        recorder[0] = new MediaRecorder();
                        recorder[0].setAudioSource(MediaRecorder.AudioSource.MIC);
                        recorder[0].setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        recorder[0].setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        recorder[0].setOutputFile(pathAudio[0]);
                        try {
                            recorder[0].prepare();
                        } catch (IOException e) {
                        }
                        recorder[0].start();
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.stop));
                        imageView2.setVisibility(View.INVISIBLE);
                        option[0]++;
                        break;
                    case 1:
                        Log.w("seleccion", "stop");
                        recorder[0].stop();
                        recorder[0].release();
                        player[0] = new MediaPlayer();
                        player[0].setOnCompletionListener(parFormFielsPre.this);
                        try {
                            player[0].setDataSource(pathAudio[0]);
                        } catch (IOException e) {
                        }
                        try {
                            player[0].prepare();
                        } catch (IOException e) {
                        }
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.recording));
                        imageView2.setVisibility(View.VISIBLE);
                        textView.setText(pathAudio[0]);
                        option[0] = 0;
                        break;
                }
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player[0].start();
            }
        });

    }

    /*****************/
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = "file://" + image.getAbsolutePath();
        return image;
    }

    /*****************/

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    public void onBackPressed() {
        //super.onBackPressed();
        ir = new Intent(parFormFielsPre.this, preReg.class);
        ir.putExtra("auth", auth);
        ir.putExtra("userName", userName);
        ir.putExtra("fullName", fullName);
        ir.putExtra("branch", branch);
        startActivity(ir);
        finish();
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

        fingerPrintReader1 = new FingerPrintReader(ivFinger,
                sgfplib);
    }

    // USB Device Attach Permission
    private static final String ACTION_USB_PERMISSION = "diggate.xpertise.com.diggateapk.USB_PERMISSION";
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

    // dialogo de crear opcion spinner
    public void createSpinner2(int idField, final ArrayList<obj_params> aux, int idParametro, final int idValue) {

        final Spinner sp = new Spinner(this);
        sp.setId(idField);
        adapter_params adapter = new adapter_params(parFormFielsPre.this, aux);
        sp.setAdapter(adapter);
        spinners.add(sp);
        llContenedor.addView(sp);
        int posicion = 0;
        for (int i = 0; i < sp.getCount(); i++) {

            obj_params elegido = (obj_params) sp.getItemAtPosition(i);
            if (elegido.getId() == idParametro) {
                posicion = i;
            }
        }
        sp.setSelection(posicion);
        final int other = aux.size() - 1;
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.w("posicion", position + "");
                if(idValue > 0 && position == other) {
                    dialogInsertOption(sp, aux, idValue);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void dialogInsertOption(final Spinner spinner, final ArrayList<obj_params> options, final int idValue) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(parFormFielsPre.this);

        builder.setCancelable(false);

        LayoutInflater inflater = parFormFielsPre.this.getLayoutInflater();

        View v = inflater.inflate(R.layout.spinner_options, null);

        builder.setView(v);

        final EditText etName = (EditText) v.findViewById(R.id.etName);
        TextView btnSave = (TextView) v.findViewById(R.id.btnSave);
        TextView btnExit = (TextView) v.findViewById(R.id.btnExit);

        final AlertDialog alertDialog = builder.create();


        btnSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etName.getText().toString().trim().equals("")) {
                            Toast.makeText(parFormFielsPre.this, "Debe ingresar un nombre", Toast.LENGTH_SHORT).show();
                        } else {
                            options.add(new obj_params(idValue, etName.getText().toString().trim(), 1));
                            adapter_params adapter = new adapter_params(parFormFielsPre.this, options);
                            spinner.setAdapter(adapter);
                            spinner.setSelection(spinner.getCount()-1);
                            alertDialog.cancel();
                        }
                    }
                }

        );

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setSelection(0);
                alertDialog.cancel();
            }
        });

        alertDialog.show();
    }

}