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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
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
import androidx.core.content.FileProvider;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
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
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import SecuGen.FDxSDKPro.JSGFPLib;
import SecuGen.FDxSDKPro.SGAutoOnEventNotifier;
import SecuGen.FDxSDKPro.SGFDxDeviceName;
import SecuGen.FDxSDKPro.SGFDxErrorCode;
import SecuGen.FDxSDKPro.SGFDxSecurityLevel;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class form_event extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnCompletionListener {

    int idField;
    int opcion;
    String auth;
    String userName;
    String idForm;
    LinearLayout llContenedor, llRecording, llData;
    Button btnSave;
    ImageView ivRecording, ivPlaying;
    TextView tvRecording, tvPathRecording;
    ProgressDialog mProgressDialog;
    RequestQueue mRequestQueue;
    JsonObjectRequest myJsonObjectRequest;

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
    ArrayList<ImageView> signatures = new ArrayList<ImageView>();
    ArrayList<Spinner> spinners2 = new ArrayList<Spinner>();
    ArrayList<TextView> textViewsLocation = new ArrayList<TextView>();
    ArrayList<TextView> spinnersSearch = new ArrayList<TextView>();
    ArrayList<TextView> spinnersSearch2 = new ArrayList<TextView>();

    Bitmap bmp;
    JSONObject jsonenvio = new JSONObject();
    String textDate = "Haga clic para obtener la Fecha";
    String textHour = "Haga clic para obtener la Hora";
    String textFile = "Haga clic para obtener el Archivo";
    String textAudio = "Haga clic para grabar el audio";
    String textImage = "Imagen";
    String textLocation = "Haga clic para obtener la localizacion";
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
    ArrayList<obj_attributes> objAttributesSignatures = new ArrayList<obj_attributes>();

    String currentPhotoPath;

    ArrayList<obj_auth> auths = new ArrayList<obj_auth>();

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

    int idFinger;
    String fingerCapture = "";

    ImageView ivFinger;

    //QR
    String myQR = null;
    int idAuht = 0;

    String part1 = null;
    String part2 = null;

    String branch;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_event);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        url = getString(R.string.servidor) + "/api/parFormFields/";
        url2 = getString(R.string.servidor) + "/api/saveEvent";

        llContenedor = findViewById(R.id.llContenedor);
        llRecording = findViewById(R.id.llRecording);
        llData = findViewById(R.id.llData);
        llData.setVisibility(View.GONE);
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

        Bundle parametros = this.getIntent().getExtras();
        auth = parametros.getString("auth");
        userName = parametros.getString("userName");
        idForm = parametros.getString("idForm");
        fullName = parametros.getString("fullName");
        branch = parametros.getString("branch");
        Log.w("fullname", fullName);
        Log.w("branch", branch);

        conversion = new HexConversion();
        //usbPermission();

        hand.removeCallbacks(actualizar);
        hand.postDelayed(actualizar, 100);

        validarPermisos();

        url = url + idForm;

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
                    String text = editText.getHint().toString().trim();
                    String control = "";
                    if (text.contains("-")) {
                        String[] parts = text.split("-");
                        control = parts[1];
                    } else {
                        control = text;
                    }
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

                /*************/
                //TextView Location
                for (Iterator iterator = textViewsLocation.iterator(); iterator
                        .hasNext(); ) {

                    TextView textView = (TextView) iterator.next();
                    String obs_respuesta = textView.getText().toString().trim();
                    String control = textView.getHint().toString().trim();

                    if (obs_respuesta.equals(textLocation) && control.equals(obligatorio)) {

                        validar++;
                        Log.w("sumaTextViewLocation", "" + validar);

                    }

                    Log.w("controlTextViewLocation", control);

                    Log.w("TextViewLocation", "TextView" + " " + textView.getId() + " " + obs_respuesta);
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
                /*************/

                for (Iterator iterator = spinners.iterator(); iterator
                        .hasNext(); ) {

                    Spinner spinner = (Spinner) iterator.next();

                    int position = spinner.getSelectedItemPosition();

                    String nombre = spinner.getItemAtPosition(position).toString();

                    obj_params elegido = (obj_params) spinner.getItemAtPosition(position);

                    Log.w("Spinner", "spinner: " + spinner.getId() + " posicion: " + position + " " + elegido.getId());

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

                /******** new Spinner ********/
                for (Iterator iterator = spinners2.iterator(); iterator
                        .hasNext(); ) {

                    Spinner spinner = (Spinner) iterator.next();

                    int position = spinner.getSelectedItemPosition();

                    String nombre = spinner.getItemAtPosition(position).toString();

                    obj_params elegido = (obj_params) spinner.getItemAtPosition(position);

                    Log.w("Spinner", "spinner: " + spinner.getId() + " posicion: " + position + " " + elegido.getId());

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
                /*******************************/

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

                //firma
                /******************************************/
                for (Iterator iterator = signatures.iterator(); iterator
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
                    for (Iterator iterator2 = objAttributesSignatures.iterator(); iterator2
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

                    Log.w("Imagen", "signature" + " " + imageView.getId() + " " + encoded);
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

                /****************************************/

                /**************/
                //spinner search
                for (Iterator iterator = spinnersSearch.iterator(); iterator
                        .hasNext(); ) {

                    TextView textView = (TextView) iterator.next();

                    String[] parts = textView.getHint().toString().trim().split("-");

                    if (textView.getHint().equals("")) {

                        validar++;
                        Log.w("countSpinnerSearch", "" + validar);
                        textView.setTextColor(Color.RED);

                    } else {
                        textView.setTextColor(getResources().getColor(R.color.colorTextVariable));

                        int elegido = Integer.parseInt(parts[0]);
                        int control = Integer.parseInt(parts[1]);

                        Log.w("spinnerSearch", "id: " + textView.getId() + " optionId: " + elegido + " control: " + control);

                        if (control == 0) {
                            try {
                                JSONObject parametros = new JSONObject();
                                parametros.put("idField", textView.getId());
                                parametros.put("valueInputField", "");
                                parametros.put("valueInputDateField", "");
                                parametros.put("valueListField", elegido);
                                parametros.put("valueFile", "");
                                respuesta.put(parametros);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                JSONObject parametros = new JSONObject();
                                parametros.put("idField", textView.getId());
                                parametros.put("valueInputField", textView.getText());
                                parametros.put("valueInputDateField", "");
                                parametros.put("valueListField", -1);
                                parametros.put("valueFile", "");
                                respuesta.put(parametros);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                //spinner search2
                for (Iterator iterator = spinnersSearch2.iterator(); iterator
                        .hasNext(); ) {

                    TextView textView = (TextView) iterator.next();

                    if (textView.getHint().equals("")) {

                        validar++;
                        Log.w("countSpinnerSearch2", "" + validar);
                        textView.setTextColor(Color.RED);

                    } else {
                        textView.setTextColor(getResources().getColor(R.color.colorTextVariable));

                        Log.w("spinnerSearch2", "id: " + textView.getId() + " optionId: " + textView.getHint());
                        try {
                            JSONObject parametros = new JSONObject();
                            parametros.put("idField", textView.getId());
                            parametros.put("valueInputField", textView.getHint());
                            parametros.put("valueInputDateField", "");
                            parametros.put("valueListField", -2);
                            parametros.put("valueFile", "");
                            respuesta.put(parametros);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
                /**************/

                localizar();

                //Log.w("json", "" + respuesta);
                try {
                    jsonenvio.put("idEvent", "0");
                    jsonenvio.put("idEventDependency", "0");
                    jsonenvio.put("dateEvent", fecha_2);
                    jsonenvio.put("posGeo", mLocation);
                    jsonenvio.put("idForm", idForm);
                    jsonenvio.put("P", respuesta);
                    jsonenvio.put("idAuth", idAuht);

                    //Log.w("json", "" + jsonenvio);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (validar == 0) {

                    if (compruebaConexion(form_event.this)) {

                        enviarformulario();

                    } else {

                        try {
                            Log.w("conexion", "no hay red");
                            bd conexion = new bd(form_event.this);
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
                        player.setOnCompletionListener(form_event.this);
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

    }

    private void cargarFormulario() {

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        mRequestQueue = Volley.newRequestQueue(this);

        myJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(JSONObject response) {

                Log.w("respuesta", "" + response);

                mProgressDialog.dismiss();

                try {
                    createJson(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {

                    int auth = response.getInt("AUTH");
                    if (auth == 1) {

                        JSONArray fingerContent = response.getJSONArray("ENTITY_AUTH");
                        for (int j = 0; j < fingerContent.length(); j++) {
                            JSONObject fpData = fingerContent.getJSONObject(j);
                            auths.add(new obj_auth(fpData.getInt("IDAUTH"), fpData.getString("BINARYFP"), fpData.getString("DESCFP"), fpData.getString("URLDATA")));
                            Log.w("fpData", fpData.toString());
                        }
                        dialogFinger();
                    }

                    if (auth == 2) {

                        JSONArray fingerContent = response.getJSONArray("ENTITY_AUTH");
                        for (int j = 0; j < fingerContent.length(); j++) {
                            JSONObject fpData = fingerContent.getJSONObject(j);
                            auths.add(new obj_auth(fpData.getInt("IDAUTH"), fpData.getString("BINARYFP"), fpData.getString("DESCFP"), fpData.getString("URLDATA")));
                            Log.w("fpData", fpData.toString());
                        }
                        dialogBarcode();
                    }

                    JSONArray fields = response.getJSONArray("FIELDS");

                    for (int i = 0; i < fields.length(); i++) {

                        JSONObject form = fields.getJSONObject(i);

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
                        int idValue_other = form.getInt("IDVALUE_OTHER");

                        String hint = form.getString("HINT");

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

                        /*****/
                        // llenado de las opciones de lista compuesta
                        JSONArray optionsPSEC = form.getJSONArray("PSEC");
                        ArrayList<obj_params2> itemPSEC = new ArrayList<obj_params2>();
                        for (int k = 0; k < optionsPSEC.length(); k++) {
                            JSONObject op = optionsPSEC.getJSONObject(k);
                            int valor = op.getInt("IDVALUE_PSEC");
                            String descriptionPSEC = op.getString("DESC_PSEC");
                            String descriptionPSEC1 = op.getString("DESC_PSEC1");
                            String descriptionPSEC2 = op.getString("DESC_PSEC2");

                            itemPSEC.add(new obj_params2(valor, descriptionPSEC, descriptionPSEC2, descriptionPSEC1, 0));
                        }
                        /*****/

                        switch (type) {

                            case 1:

                                creartextview(description);
                                crearedittext(idField, is_mandatory, input_max, input_regx, hint);

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
                                createSpinner2(idField, itemp, input_max, idValue_other);

                                break;

                            case 9:

                                creartextview(description);
                                createSwitch(idField, "SI    /    NO");

                                break;

                            case 10:

                                createtextViewTitle(description);

                                break;

                            case 11:

                                createAudio(idField, description, is_mandatory);

                                break;

                            case 14:

                                creartextview(description);
                                createSignature(idField, is_mandatory, photo_resolution_w, photo_resolution_h);

                                break;

                            case 15:

                                creartextview(description);
                                createSpinner3(idField, itemp, input_max, idValue_other);

                                break;

                            case 16:

                                creartextview(description);
                                createTextViewLocation(idField, is_mandatory);

                                break;

                            case 17:

                                creartextview(description);
                                createSpinnerSearch(idField, itemp, input_max);

                                break;

                            case 18:

                                creartextview(description);
                                createSpinnerSearch2(idField, itemp, input_max, idValue_other);

                                break;

                            case 19:

                                creartextview(description);
                                createSpinnerSearch3(idField, itemp);

                                break;

                            case 20:

                                creartextview(description);
                                createSpinnerListSearch(idField, itemPSEC, input_max);

                                break;

                            case 21:

                                creartextview(description);
                                createSpinnerlistSearch2(idField, itemPSEC);

                                break;

                            default:
                                break;

                        }

                    }

                    //dialogFinger();

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

        mRequestQueue.add(myJsonObjectRequest);

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

    public void crearedittext(int id_opcion, String opcion, int descripcion, String regEx, String hint) {


        EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setTextSize(14);
        et.setTextColor(getResources().getColor(R.color.colorTextVariable));
        et.setHint("ej: " + hint + "-" + opcion);
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
        textView.setOnClickListener(form_event.this);

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
        textView.setOnClickListener(form_event.this);

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
        adapter_params adapter = new adapter_params(form_event.this, aux);
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
                try {
                    if (response.getInt("generateReceipt") == 1) {
                        Uri uri = Uri.parse(response.getString("urlReceipt"));
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        finish();
                    } else {
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.w("mio", "" + error);
                try {
                    Log.w("conexion", "no hay red");
                    bd conexion = new bd(form_event.this);
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

        mjsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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
                .withActivity(form_event.this)
                .withRequestCode(codigoFile)
                .start();

    }


    // camara
    private void tomarFotografia() {


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


                    iv.setImageBitmap(bmp);

                    /***********************************/


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

            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() != null) {
                    try {
                        String[] parts = result.getContents().split("[|]");
                        part1 = parts[0];
                        part2 = parts[1];
                        Log.w("parts", part1 + " " + part2);
                        myQR = part1;
                        Log.w("myqr", myQR);
                        Log.w("part1", part1);
                        Log.w("part2", part2);
                    } catch (Exception e) {
                        myQR = result.getContents();
                        Log.w("myqr", result.getContents());
                    }
                    obj_auth respuesta = null;
                    int statusOK = 0;
                    if (myQR != null) {
                        for (Iterator iterator = auths.iterator(); iterator
                                .hasNext(); ) {
                            obj_auth auth = (obj_auth) iterator.next();
                            Log.w("llave", auth.getBinaryfp());
                            if (myQR.equals(auth.getBinaryfp())) {
                                Log.w("estadoQR", "SI");
                                statusOK = 1;
                                respuesta = auth;
                            } else {
                                Log.w("estadoQR", "NO");
                            }
                        }
                        Log.w("statusOK", "" + statusOK);
                        if (statusOK == 0) {
                            Toast.makeText(form_event.this, "No se encontró ninguna coincidencia.", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            idAuht = respuesta.getIdauth();
                            llData.setVisibility(View.VISIBLE);
                            creartextviewLink("" + respuesta.getDescfp(), "" + respuesta.getUrlData());
                        }
                    }
                    if (statusOK == 1) {
                        if (part2 != null) {
                            String codeBinaryFp = respuesta.getBinaryfp();
                            String codeQR = encryptSeed(codeBinaryFp + getDateQR());
                            Log.w("keys", codeQR + " - " + part2);
                            if (part2.equals(codeQR)) {

                            } else {
                                Toast.makeText(form_event.this, "No se encontró ninguna coincidencia.", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    }
                } else {
                    Log.w("fallaqr", "error al scanear");
                    Toast.makeText(form_event.this, "No se encontró ninguna coincidencia.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        } else {
            if (auths.size() > 0) {
                dialogBarcode();
            }
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

        AlertDialog.Builder builder = new AlertDialog.Builder(form_event.this);
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(form_event.this);
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

                    msj = Toast.makeText(form_event.this, "los permisos no fueron aceptados", Toast.LENGTH_LONG);
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

        DatePickerDialog mdatePickerDialog = new DatePickerDialog(form_event.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                TextView textView = new TextView(form_event.this);
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

        TimePickerDialog mTimePickerDialog = new TimePickerDialog(form_event.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                TextView textView = new TextView(form_event.this);
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
            String formulario = readJsonFile("/storage/emulated/0/DigGate/form" + idForm + ".json");
            if (formulario == null) {

                msj = Toast.makeText(this, "No hay datos para mostrar", Toast.LENGTH_LONG);
                msj.setGravity(Gravity.CENTER, 0, 0);
                msj.show();

            } else {

                try {

                    JSONObject response = new JSONObject(formulario);

                    int auth = response.getInt("AUTH");
                    if (auth == 1) {
                        JSONArray fingerContent = response.getJSONArray("ENTITY_AUTH");
                        for (int j = 0; j < fingerContent.length(); j++) {
                            JSONObject fpData = fingerContent.getJSONObject(j);
                            auths.add(new obj_auth(fpData.getInt("IDAUTH"), fpData.getString("BINARYFP"), fpData.getString("DESCFP"), fpData.getString("URLDATA")));
                            Log.w("fpData", fpData.toString());
                        }
                        dialogFinger();
                    }

                    if (auth == 2) {

                        JSONArray fingerContent = response.getJSONArray("ENTITY_AUTH");
                        for (int j = 0; j < fingerContent.length(); j++) {
                            JSONObject fpData = fingerContent.getJSONObject(j);
                            auths.add(new obj_auth(fpData.getInt("IDAUTH"), fpData.getString("BINARYFP"), fpData.getString("DESCFP"), fpData.getString("URLDATA")));
                            Log.w("fpData", fpData.toString());
                        }
                        dialogBarcode();
                    }

                    JSONArray fields = response.getJSONArray("FIELDS");

                    for (int i = 0; i < fields.length(); i++) {

                        JSONObject form = fields.getJSONObject(i);

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
                        int idValue_other = form.getInt("IDVALUE_OTHER");

                        String hint = form.getString("HINT");

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

                        /*****/
                        // llenado de las opciones de lista compuesta
                        JSONArray optionsPSEC = form.getJSONArray("PSEC");
                        ArrayList<obj_params2> itemPSEC = new ArrayList<obj_params2>();
                        for (int k = 0; k < optionsPSEC.length(); k++) {
                            JSONObject op = optionsPSEC.getJSONObject(k);
                            int valor = op.getInt("IDVALUE_PSEC");
                            String descriptionPSEC = op.getString("DESC_PSEC");
                            String descriptionPSEC1 = op.getString("DESC_PSEC1");
                            String descriptionPSEC2 = op.getString("DESC_PSEC2");

                            itemPSEC.add(new obj_params2(valor, descriptionPSEC, descriptionPSEC2, descriptionPSEC1, 0));
                        }
                        /*****/


                        switch (type) {

                            case 1:

                                creartextview(description);
                                crearedittext(idField, is_mandatory, input_max, input_regx, hint);

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
                                createSpinner2(idField, itemp, input_max, idValue_other);

                                break;

                            case 9:

                                creartextview(description);
                                createSwitch(idField, "SI    /    NO");

                                break;

                            case 10:

                                createtextViewTitle(description);

                                break;

                            case 11:

                                createAudio(idField, description, is_mandatory);

                                break;

                            case 14:

                                creartextview(description);
                                createSignature(idField, is_mandatory, photo_resolution_w, photo_resolution_h);

                                break;

                            case 15:

                                creartextview(description);
                                createSpinner3(idField, itemp, input_max, idValue_other);

                                break;

                            case 16:

                                creartextview(description);
                                createTextViewLocation(idField, is_mandatory);

                                break;

                            case 17:

                                creartextview(description);
                                createSpinnerSearch(idField, itemp, input_max);

                                break;

                            case 18:

                                creartextview(description);
                                createSpinnerSearch2(idField, itemp, input_max, idValue_other);

                                break;

                            case 19:

                                creartextview(description);
                                createSpinnerSearch3(idField, itemp);

                                break;

                            case 20:

                                creartextview(description);
                                createSpinnerListSearch(idField, itemPSEC, input_max);

                                break;

                            case 21:

                                creartextview(description);
                                createSpinnerlistSearch2(idField, itemPSEC);

                                break;

                            default:
                                break;

                        }

                    }

                    //dialogFinger();

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

            nombreJson = "form" + idForm + ".json";

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

            nombreJson = "Answer" + fecha_1 + ".json";

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
                        player[0].setOnCompletionListener(form_event.this);
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

    private void dialogFinger() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(form_event.this);

        builder.setCancelable(false);

        LayoutInflater inflater = form_event.this.getLayoutInflater();

        View v = inflater.inflate(R.layout.finger, null);

        builder.setView(v);

        Button btnExit = (Button) v.findViewById(R.id.btnExit);
        ivFinger = (ImageView) v.findViewById(R.id.ivFinger);

        try {
            usbPermission();
        } catch (Exception e) {
            Toast.makeText(form_event.this, "Revise la conexión del USB", Toast.LENGTH_SHORT).show();
            finish();
        }

        final AlertDialog alertDialog = builder.create();

        ivFinger.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

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
                                    //Log.d(TAG, "Template" + Temp);

                                    fingerCapture = Base64.encodeToString(abc, Base64.DEFAULT);
                                    Log.d(TAG, "" + Temp);
                                }
                            });

                            for (Iterator iterator = auths.iterator(); iterator
                                    .hasNext(); ) {
                                obj_auth auth = (obj_auth) iterator.next();
                                //Log.w("Template2", auth.getBinaryfp());
                                String aux = auth.getBinaryfp();
                                byte[] decodedBytes = Base64.decode(aux, 0);
                                String Temp = conversion.getHexString(decodedBytes);
                                Log.w("Template2", "" + Temp);

                                long sl = SGFDxSecurityLevel.SL_NORMAL; // Set security level as NORMAL
                                boolean[] matched1 = new boolean[1];
                                error = sgfplib.MatchTemplate(fingerPrintReader1.getHexTemplate(), decodedBytes, sl, matched1);

                                if (error == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                                    if (matched1[0]) {
                                        idAuht = auth.getIdauth();
                                        Log.d("idAuth", "" + idAuht);
                                        Log.d("descfp", "" + auth.getDescfp());
                                        Log.d("urlData", "" + auth.getUrlData());
                                        creartextviewLink("" + auth.getDescfp(), "" + auth.getUrlData());
                                    }
                                }

                            }
                            if (idAuht == 0) {
                                Toast.makeText(form_event.this, "Persona No Registrado", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                llData.setVisibility(View.VISIBLE);
                                Toast.makeText(form_event.this, "Persona Registrado", Toast.LENGTH_SHORT).show();
                            }
                            alertDialog.dismiss();

                        } catch (Exception e) {
                            Toast.makeText(form_event.this, "Revise la conexión del USB", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                }
        );

        btnExit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Loguear...
                        finish();
                    }
                }

        );

        alertDialog.show();


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

    public void dialogBarcode() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(form_event.this);

        builder.setCancelable(false);

        LayoutInflater inflater = form_event.this.getLayoutInflater();

        View v = inflater.inflate(R.layout.qr, null);

        builder.setView(v);

        Button btnExit = (Button) v.findViewById(R.id.btnExit);
        ImageView ivQR = v.findViewById(R.id.ivQR);

        final AlertDialog alertDialog = builder.create();

        ivQR.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //new IntentIntegrator(form_event.this).initiateScan();
                        new IntentIntegrator(form_event.this).setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES).setPrompt(" Scan QR").initiateScan();
                        alertDialog.dismiss();
                    }
                }
        );

        btnExit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        finish();
                    }
                }

        );

        alertDialog.show();
    }

    // texto link
    public void creartextviewLink(String texto, final String urlData) {
        Log.d("miUrl", texto);
        /**cambio**/
        String[] info = texto.split("\\$");
        TextView tv0;
        tv0 = new TextView(this);
        tv0.setText(info[0]);
        tv0.setTextAppearance(this, R.style.colorTitle);
        llData.addView(tv0);
        for (int i = 1; i < info.length; i++) {
            String[] part = info[i].split("!");
            TextView tv;
            tv = new TextView(this);
            tv.setText(part[0]);
            tv.setTextAppearance(this, R.style.colorTitle);
            llData.addView(tv);
            TextView tv2;
            tv2 = new TextView(this);
            tv2.setText(part[1]);
            tv2.setTextSize(14);
            LinearLayout.LayoutParams lastTxtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lastTxtParams.setMargins(0, 30, 0, 0);
            tv2.setLayoutParams(lastTxtParams);
            //tv.setTextColor(getResources().getColor(R.color.colorBlack));
            tv2.setTextAppearance(this, R.style.boldreg);
            llData.addView(tv2);
        }
        TextView tv;
        tv = new TextView(this);
        tv.setText("ver más...");
        tv.setTextSize(14);
        LinearLayout.LayoutParams lastTxtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lastTxtParams.setMargins(0, 0, 0, 0);
        tv.setLayoutParams(lastTxtParams);
        //tv.setTextColor(getResources().getColor(R.color.colorBlack));
        tv.setTextAppearance(this, R.style.colorText);
        llData.addView(tv);
        /****/

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(urlData);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

    }

    // dialogo de crear opcion spinner
    public void createSpinner2(int idField, final ArrayList<obj_params> aux, int idParametro, final int idValue) {

        final Spinner sp = new Spinner(this);
        sp.setId(idField);
        adapter_params adapter = new adapter_params(form_event.this, aux);
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
                if (idValue > 0 && position == other) {
                    dialogInsertOption(sp, aux, idValue);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void dialogInsertOption(final Spinner spinner, final ArrayList<obj_params> options, final int idValue) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(form_event.this);

        builder.setCancelable(false);

        LayoutInflater inflater = form_event.this.getLayoutInflater();

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
                            Toast.makeText(form_event.this, "Debe ingresar un nombre", Toast.LENGTH_SHORT).show();
                        } else {
                            options.add(new obj_params(idValue, etName.getText().toString().trim(), 1));
                            adapter_params adapter = new adapter_params(form_event.this, options);
                            spinner.setAdapter(adapter);
                            spinner.setSelection(spinner.getCount() - 1);
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

    // firma
    public void createSignature(int idField, String option, int w, int h) {

        LinearLayout llImg = new LinearLayout(this);
        LinearLayout.LayoutParams paramsImg = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llImg.setLayoutParams(paramsImg);
        llImg.setOrientation(LinearLayout.HORIZONTAL);
        llImg.setWeightSum(12);
        llContenedor.addView(llImg);

        ImageView iv = new ImageView(this);
        iv.setId(idField);
        iv.setImageResource(R.drawable.pencil);
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

        if (w > 0 && h > 0) {
            objAttributesSignatures.add(new obj_attributes(idField, w, h, null));
        } else {
            w = 400;
            h = 400;
            objAttributesSignatures.add(new obj_attributes(idField, w, h, null));
        }

        signatures.add(iv);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureSignature(iv, form_event.this);
            }
        });

    }

    public void captureSignature(ImageView iv, Context c) {

        LinearLayout llCon = new LinearLayout(c);
        //***********************************************************************************
        LinearLayout llcrearDialogo = new LinearLayout(c);
        LinearLayout.LayoutParams parametrosTextoEditor = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        parametrosTextoEditor.setMargins(20, 10, 20, 10);
        llcrearDialogo.setLayoutParams(parametrosTextoEditor);
        llcrearDialogo.setOrientation(LinearLayout.VERTICAL);
        llcrearDialogo.setPadding(10, 10, 10, 10);
        llCon.addView(llcrearDialogo);

        LinearLayout linearLayout0 = new LinearLayout(c);
        LinearLayout.LayoutParams lp0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 600);
        linearLayout0.setLayoutParams(lp0);
        linearLayout0.setOrientation(LinearLayout.HORIZONTAL);
        //linearLayout.setWeightSum(12);
        linearLayout0.setBackgroundResource(R.drawable.shape_finger);
        linearLayout0.setGravity(Gravity.CENTER);
        llcrearDialogo.addView(linearLayout0);

        LinearLayout linearLayout = new LinearLayout(c);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(lp);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setPadding(30, 30, 30, 30);
        //linearLayout.setWeightSum(12);
        linearLayout.setGravity(Gravity.CENTER);

        /********* Signature *******/
        /*vista aux = new vista(c, linearLayout, iv);
        aux.setBackgroundColor(Color.GRAY);*/
        CaptureBitmapView mSig;
        mSig = new CaptureBitmapView(this, null);
        linearLayout.addView(mSig);

        /********* Signature *******/

        linearLayout0.addView(linearLayout);

        //***********************BUTTONS********************************
        Button btnLimpiar = new Button(c);
        LinearLayout.LayoutParams parametrosLimpiar = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        parametrosLimpiar.setMargins(0, 30, 0, 0);
        btnLimpiar.setLayoutParams(parametrosLimpiar);
        btnLimpiar.setText("Limpiar");
        btnLimpiar.setTextAppearance(this, R.style.colorTitle);
        btnLimpiar.setBackgroundResource(R.drawable.custonbutton);
        llcrearDialogo.addView(btnLimpiar);

        Button btnAceptar = new Button(c);
        LinearLayout.LayoutParams parametrosAceptar = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        parametrosAceptar.setMargins(0, 30, 0, 0);
        btnAceptar.setLayoutParams(parametrosAceptar);
        btnAceptar.setText("Guardar");
        btnAceptar.setTextAppearance(this, R.style.colorTitle);
        btnAceptar.setBackgroundResource(R.drawable.custonbutton);
        llcrearDialogo.addView(btnAceptar);

        Button btnSalir = new Button(c);
        LinearLayout.LayoutParams parametrosSalir = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        parametrosSalir.setMargins(0, 30, 0, 0);
        btnSalir.setLayoutParams(parametrosSalir);
        btnSalir.setText("Salir");
        btnSalir.setTextAppearance(this, R.style.colorTitle);
        btnSalir.setBackgroundResource(R.drawable.custonbutton);
        llcrearDialogo.addView(btnSalir);

        final AlertDialog optionDialog = new AlertDialog.Builder(c, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen).create();

        optionDialog.setTitle("Firma");

        optionDialog.setView(llCon);//******************************************

        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mSig.ClearCanvas();

            }


        }); //fin del button

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bitmap bitmap = mSig.getBitmap();
                String[] parts = iv.getContentDescription().toString().trim().split("-");
                String description = parts[0] + "captura";
                String control = parts[parts.length - 1];

                iv.setContentDescription(description + "-" + control);
                Log.w("imagenDesc", description + "-" + control);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 600);
                iv.setLayoutParams(lp);
                iv.setImageBitmap(bitmap);
                for (Iterator iterator2 = objAttributesSignatures.iterator(); iterator2
                        .hasNext(); ) {
                    obj_attributes properties = (obj_attributes) iterator2.next();
                    if (iv.getId() == properties.getId()) {
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
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
                        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                                width, height, matrix, true);
                        properties.setImage(resizedBitmap);
                    }
                }
                optionDialog.dismiss();

            }


        }); //fin del button

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                optionDialog.dismiss();

            }


        });

        optionDialog.show();

    }

    // cambio
    // dialogo de crear opcion2 spinner
    public void createSpinner3(int idField, final ArrayList<obj_params> aux, int idParametro, final int idValue) {

        final Spinner sp = new Spinner(this);
        sp.setId(idField);
        adapter_params adapter = new adapter_params(form_event.this, aux);
        sp.setAdapter(adapter);
        spinners2.add(sp);
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
                if (idValue > 0 && position == other) {
                    dialogInsertOption2(sp, aux, idValue);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void dialogInsertOption2(final Spinner spinner, final ArrayList<obj_params> options, final int idValue) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(form_event.this);

        builder.setCancelable(false);

        LayoutInflater inflater = form_event.this.getLayoutInflater();

        View v = inflater.inflate(R.layout.spinner_options2, null);

        builder.setView(v);

        final EditText etName = (EditText) v.findViewById(R.id.etName);
        final EditText etEmail = (EditText) v.findViewById(R.id.etEmail);
        TextView btnSave = (TextView) v.findViewById(R.id.btnSave);
        TextView btnExit = (TextView) v.findViewById(R.id.btnExit);

        final AlertDialog alertDialog = builder.create();


        btnSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etName.getText().toString().trim().equals("") || etEmail.getText().toString().trim().equals("")) {
                            Toast.makeText(form_event.this, "Debe completar los datos", Toast.LENGTH_SHORT).show();
                        } else {
                            if (!validarEmail(etEmail.getText().toString())) {
                                Toast.makeText(form_event.this, "la direcion de correo no es valida", Toast.LENGTH_SHORT).show();
                            } else {
                                options.add(new obj_params(idValue, etName.getText().toString().trim() + " | " + etEmail.getText().toString().trim(), 1));
                                adapter_params adapter = new adapter_params(form_event.this, options);
                                spinner.setAdapter(adapter);
                                spinner.setSelection(spinner.getCount() - 1);
                                alertDialog.cancel();
                            }
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

    /*********/
    // spinner con busuqeda
    public void createSpinnerSearch(int idField, final ArrayList<obj_params> listOption, int idParam) {
        LinearLayout llBody = new LinearLayout(this);
        LinearLayout.LayoutParams paramsBody = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llBody.setLayoutParams(paramsBody);
        llBody.setOrientation(LinearLayout.HORIZONTAL);
        llBody.setWeightSum(10);
        llBody.setPadding(10, 10, 10, 10);
        llContenedor.addView(llBody);

        TextView textView = new TextView(this);
        textView.setId(idField);
        textView.setText("Selecione una opcion");
        textView.setHint("");
        textView.setTextSize(14);
        textView.setTextColor(getResources().getColor(R.color.colorTextVariable));
        textView.setBackgroundResource(R.color.colorSpinner);
        textView.setPadding(30, 20, 30, 20);
        LinearLayout.LayoutParams lastTxtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        lastTxtParams.setMargins(0, 30, 0, 0);
        textView.setLayoutParams(lastTxtParams);
        llBody.addView(textView);

        spinnersSearch.add(textView);

        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 9f);
        lp.gravity = Gravity.CENTER;
        iv.setLayoutParams(lp);
        llBody.addView(iv);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LinearLayout llcrearDialogo = new LinearLayout(form_event.this);
                LinearLayout.LayoutParams parametrosTextoEditor = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                parametrosTextoEditor.setMargins(20, 10, 20, 10);
                llcrearDialogo.setLayoutParams(parametrosTextoEditor);
                llcrearDialogo.setOrientation(LinearLayout.VERTICAL);
                llcrearDialogo.setPadding(10, 10, 10, 10);

                EditText et = new EditText(form_event.this);
                et.setInputType(InputType.TYPE_CLASS_TEXT);
                et.setTextSize(14);
                et.setTextColor(getResources().getColor(R.color.colorTextVariable));
                et.setHint("Buscar");
                et.setBackgroundResource(R.drawable.customedittext);
                et.setPadding(30, 20, 30, 20);

                ListView lv = new ListView(form_event.this);
                LinearLayout.LayoutParams paramsList = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                paramsList.setMargins(0, 10, 0, 0);
                lv.setLayoutParams(paramsList);

                adapter_spinner_search adapter = new adapter_spinner_search(form_event.this, listOption);
                lv.setAdapter(adapter);

                for (int i = 0; i < lv.getCount(); i++) {
                    obj_params elegido = (obj_params) lv.getItemAtPosition(i);
                    if (elegido.getId() == idParam) {
                        textView.setText(elegido.getDescription());
                        textView.setHint(elegido.getId() + "-" + elegido.getControl());
                    }
                }

                llcrearDialogo.addView(et);
                llcrearDialogo.addView(lv);

                final AlertDialog optionDialog = new AlertDialog.Builder(form_event.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert).create();
                optionDialog.setTitle("Seleccione una opcion");
                optionDialog.setView(llcrearDialogo);
                optionDialog.show();

                et.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String text = s.toString();
                        Log.w("textSearch", text);
                        Log.w("countList", "" + listOption.size());
                        ArrayList<obj_params> searchItems = new ArrayList<obj_params>();
                        for (int i = 0; i < listOption.size(); i++) {
                            if (listOption.get(i).getDescription().toLowerCase().contains(text.toLowerCase())) {
                                Log.w("description", listOption.get(i).getDescription());
                                searchItems.add(new obj_params(listOption.get(i).getId(), listOption.get(i).getDescription(), listOption.get(i).getControl()));
                            }
                        }
                        adapter_spinner_search adapter = new adapter_spinner_search(form_event.this, searchItems);
                        lv.setAdapter(adapter);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        obj_params elegido = (obj_params) lv.getItemAtPosition(i);

                        textView.setText(elegido.getDescription());
                        textView.setHint(elegido.getId() + "-" + elegido.getControl());

                        optionDialog.dismiss();
                    }
                });
            }
        });
    }

    // spinner con busqueda y adicion de un nuevo elemento
    public void createSpinnerSearch2(int idField, final ArrayList<obj_params> listOption, int idParam, int idValue) {
        Log.w("idValue", "" + idValue);
        int aux = listOption.size() - 1;
        LinearLayout llBody = new LinearLayout(this);
        LinearLayout.LayoutParams paramsBody = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llBody.setLayoutParams(paramsBody);
        llBody.setOrientation(LinearLayout.HORIZONTAL);
        llBody.setWeightSum(10);
        llBody.setPadding(10, 10, 10, 10);
        llContenedor.addView(llBody);

        TextView textView = new TextView(this);
        textView.setId(idField);
        textView.setText("Selecione una opcion");
        textView.setHint("");
        textView.setTextSize(14);
        textView.setTextColor(getResources().getColor(R.color.colorTextVariable));
        textView.setBackgroundResource(R.color.colorSpinner);
        textView.setPadding(30, 20, 30, 20);
        LinearLayout.LayoutParams lastTxtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        lastTxtParams.setMargins(0, 30, 0, 0);
        textView.setLayoutParams(lastTxtParams);
        llBody.addView(textView);

        spinnersSearch.add(textView);

        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 9f);
        lp.gravity = Gravity.CENTER;
        iv.setLayoutParams(lp);
        llBody.addView(iv);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LinearLayout llcrearDialogo = new LinearLayout(form_event.this);
                LinearLayout.LayoutParams parametrosTextoEditor = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                parametrosTextoEditor.setMargins(20, 10, 20, 10);
                llcrearDialogo.setLayoutParams(parametrosTextoEditor);
                llcrearDialogo.setOrientation(LinearLayout.VERTICAL);
                llcrearDialogo.setPadding(10, 10, 10, 10);

                EditText et = new EditText(form_event.this);
                et.setInputType(InputType.TYPE_CLASS_TEXT);
                et.setTextSize(14);
                et.setTextColor(getResources().getColor(R.color.colorTextVariable));
                et.setHint("Buscar");
                et.setBackgroundResource(R.drawable.customedittext);
                et.setPadding(30, 20, 30, 20);

                ListView lv = new ListView(form_event.this);
                LinearLayout.LayoutParams paramsList = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                paramsList.setMargins(0, 10, 0, 0);
                lv.setLayoutParams(paramsList);

                adapter_spinner_search adapter = new adapter_spinner_search(form_event.this, listOption);
                lv.setAdapter(adapter);

                for (int i = 0; i < lv.getCount(); i++) {
                    obj_params elegido = (obj_params) lv.getItemAtPosition(i);
                    if (elegido.getId() == idParam) {
                        textView.setText(elegido.getDescription());
                        textView.setHint(elegido.getId() + "-" + elegido.getControl());
                    }
                }

                llcrearDialogo.addView(et);
                llcrearDialogo.addView(lv);

                final AlertDialog optionDialog = new AlertDialog.Builder(form_event.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert).create();
                optionDialog.setTitle("Seleccione una opcion");
                optionDialog.setView(llcrearDialogo);
                optionDialog.show();

                et.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String text = s.toString();
                        Log.w("textSearch", text);
                        Log.w("countList", "" + listOption.size());
                        ArrayList<obj_params> searchItems = new ArrayList<obj_params>();
                        for (int i = 0; i < listOption.size(); i++) {
                            if (listOption.get(i).getDescription().toLowerCase().contains(text.toLowerCase())) {
                                Log.w("description", listOption.get(i).getDescription());
                                searchItems.add(new obj_params(listOption.get(i).getId(), listOption.get(i).getDescription(), listOption.get(i).getControl()));
                            }
                        }
                        adapter_spinner_search adapter = new adapter_spinner_search(form_event.this, searchItems);
                        lv.setAdapter(adapter);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        obj_params elegido = (obj_params) lv.getItemAtPosition(i);

                        textView.setText(elegido.getDescription());
                        textView.setHint(elegido.getId() + "-" + elegido.getControl());

                        optionDialog.dismiss();

                        Log.w("position", i + "");
                        if (idValue > 0 && i == aux) {
                            dialogInsertOptionsSearch(textView, lv, listOption, idValue);
                        }
                    }
                });
            }
        });
    }

    private void dialogInsertOptionsSearch(final TextView textView, ListView listView, final ArrayList<obj_params> options, final int idValue) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(form_event.this);

        builder.setCancelable(false);

        LayoutInflater inflater = form_event.this.getLayoutInflater();

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
                            Toast.makeText(form_event.this, "Debe ingresar un nombre", Toast.LENGTH_SHORT).show();
                        } else {
                            options.add(new obj_params(idValue, etName.getText().toString().trim(), 1));
                            adapter_params adapter = new adapter_params(form_event.this, options);
                            listView.setAdapter(adapter);
                            textView.setText(etName.getText().toString());
                            textView.setHint(idValue + "-" + 1);
                            alertDialog.cancel();
                        }
                    }
                }

        );

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("Selecione una opcion");
                textView.setHint("");
                alertDialog.cancel();
            }
        });

        alertDialog.show();
    }

    // spiner con busqueda y seleccion multiple
    public void createSpinnerSearch3(int idField, final ArrayList<obj_params> listOption) {
        LinearLayout llBody = new LinearLayout(this);
        LinearLayout.LayoutParams paramsBody = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llBody.setLayoutParams(paramsBody);
        llBody.setOrientation(LinearLayout.HORIZONTAL);
        llBody.setWeightSum(10);
        llBody.setPadding(10, 10, 10, 10);
        llContenedor.addView(llBody);

        TextView textView = new TextView(this);
        textView.setId(idField);
        textView.setText("Selecione una opcion");
        textView.setHint("");
        textView.setTextSize(14);
        textView.setTextColor(getResources().getColor(R.color.colorTextVariable));
        textView.setBackgroundResource(R.color.colorSpinner);
        textView.setPadding(30, 20, 30, 20);
        LinearLayout.LayoutParams lastTxtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        lastTxtParams.setMargins(0, 30, 0, 0);
        textView.setLayoutParams(lastTxtParams);
        llBody.addView(textView);

        spinnersSearch2.add(textView);

        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 9f);
        lp.gravity = Gravity.CENTER;
        iv.setLayoutParams(lp);
        llBody.addView(iv);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LinearLayout llcrearDialogo = new LinearLayout(form_event.this);
                LinearLayout.LayoutParams parametrosTextoEditor = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                parametrosTextoEditor.setMargins(20, 10, 20, 10);
                llcrearDialogo.setLayoutParams(parametrosTextoEditor);
                llcrearDialogo.setOrientation(LinearLayout.VERTICAL);
                llcrearDialogo.setPadding(10, 10, 10, 10);

                EditText et = new EditText(form_event.this);
                et.setInputType(InputType.TYPE_CLASS_TEXT);
                et.setTextSize(14);
                et.setTextColor(getResources().getColor(R.color.colorTextVariable));
                et.setHint("Buscar");
                et.setBackgroundResource(R.drawable.customedittext);
                et.setPadding(30, 20, 30, 20);

                ListView lv = new ListView(form_event.this);
                LinearLayout.LayoutParams paramsList = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                paramsList.setMargins(0, 10, 0, 0);
                lv.setLayoutParams(paramsList);

                adapter_spinner_search2 adapter = new adapter_spinner_search2(form_event.this, listOption);
                lv.setAdapter(adapter);

                llcrearDialogo.addView(et);
                llcrearDialogo.addView(lv);

                final AlertDialog optionDialog = new AlertDialog.Builder(form_event.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert).create();
                optionDialog.setTitle("Seleccione una opcion");
                optionDialog.setView(llcrearDialogo);
                optionDialog.show();

                et.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String text = s.toString();
                        Log.w("textSearch", text);
                        Log.w("countList", "" + listOption.size());
                        ArrayList<obj_params> searchItems = new ArrayList<obj_params>();
                        for (int i = 0; i < listOption.size(); i++) {
                            if (listOption.get(i).getDescription().toLowerCase().contains(text.toLowerCase())) {
                                Log.w("description", listOption.get(i).getDescription());
                                searchItems.add(new obj_params(listOption.get(i).getId(), listOption.get(i).getDescription(), listOption.get(i).getControl(), listOption.get(i).isActive()));
                            }
                        }
                        adapter_spinner_search2 adapter = new adapter_spinner_search2(form_event.this, searchItems);
                        lv.setAdapter(adapter);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        obj_params elegido = (obj_params) lv.getItemAtPosition(i);
                        for (int j = 0; j < listOption.size(); j++) {
                            if (listOption.get(j).getId() == elegido.getId()) {
                                if (listOption.get(j).isActive()) {
                                    listOption.get(j).setActive(false);
                                } else {
                                    listOption.get(j).setActive(true);
                                }
                            }
                        }

                        String selectedItem = "";
                        String selectedId = "";
                        int count = 0;
                        for (int k = 0; k < listOption.size(); k++) {
                            if (listOption.get(k).isActive()) {
                                if (selectedItem.equals("")) {
                                    selectedItem = listOption.get(k).getDescription();
                                    selectedId = "" + listOption.get(k).getId();
                                } else {
                                    selectedItem = selectedItem + ", " + listOption.get(k).getDescription();
                                    selectedId = selectedId + "|" + listOption.get(k).getId();
                                }
                                count++;
                            }
                        }

                        if (count == 0)
                        {
                            textView.setText("Seleccione una opcion");
                            textView.setHint("");
                        } else {
                            textView.setText(selectedItem);
                            textView.setHint(selectedId);
                        }

                        optionDialog.dismiss();
                    }
                });
            }
        });
    }

    /*********/

    /*********/
    // spinner lista compuesta con busqueda y solo una seleccion
    public void createSpinnerListSearch(int idField, final ArrayList<obj_params2> listOption, int idParam) {
        LinearLayout llBody = new LinearLayout(this);
        LinearLayout.LayoutParams paramsBody = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llBody.setLayoutParams(paramsBody);
        llBody.setOrientation(LinearLayout.HORIZONTAL);
        llBody.setWeightSum(10);
        llBody.setPadding(10, 10, 10, 10);
        llContenedor.addView(llBody);

        TextView textView = new TextView(this);
        textView.setId(idField);
        textView.setText("Selecione una opcion");
        textView.setHint("");
        textView.setTextSize(14);
        textView.setTextColor(getResources().getColor(R.color.colorTextVariable));
        textView.setBackgroundResource(R.color.colorSpinner);
        textView.setPadding(30, 20, 30, 20);
        LinearLayout.LayoutParams lastTxtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        lastTxtParams.setMargins(0, 30, 0, 0);
        textView.setLayoutParams(lastTxtParams);
        llBody.addView(textView);

        spinnersSearch.add(textView);

        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 9f);
        lp.gravity = Gravity.CENTER;
        iv.setLayoutParams(lp);
        llBody.addView(iv);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LinearLayout llcrearDialogo = new LinearLayout(form_event.this);
                LinearLayout.LayoutParams parametrosTextoEditor = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                parametrosTextoEditor.setMargins(20, 10, 20, 10);
                llcrearDialogo.setLayoutParams(parametrosTextoEditor);
                llcrearDialogo.setOrientation(LinearLayout.VERTICAL);
                llcrearDialogo.setPadding(10, 10, 10, 10);

                EditText et = new EditText(form_event.this);
                et.setInputType(InputType.TYPE_CLASS_TEXT);
                et.setTextSize(14);
                et.setTextColor(getResources().getColor(R.color.colorTextVariable));
                et.setHint("Buscar");
                et.setBackgroundResource(R.drawable.customedittext);
                et.setPadding(30, 20, 30, 20);

                ListView lv = new ListView(form_event.this);
                LinearLayout.LayoutParams paramsList = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                paramsList.setMargins(0, 10, 0, 0);
                lv.setLayoutParams(paramsList);

                adapter_list adapter = new adapter_list(form_event.this, listOption);
                lv.setAdapter(adapter);

                for (int i = 0; i < lv.getCount(); i++) {
                    obj_params2 elegido = (obj_params2) lv.getItemAtPosition(i);
                    if (elegido.getId() == idParam) {
                        textView.setText(elegido.getTitle());
                        textView.setHint(elegido.getId() + "-" + elegido.getControl());
                    }
                }

                llcrearDialogo.addView(et);
                llcrearDialogo.addView(lv);

                final AlertDialog optionDialog = new AlertDialog.Builder(form_event.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert).create();
                optionDialog.setTitle("Seleccione una opcion");
                optionDialog.setView(llcrearDialogo);
                optionDialog.show();

                et.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String text = s.toString();
                        Log.w("textSearch", text);
                        Log.w("countList", "" + listOption.size());
                        ArrayList<obj_params2> searchItems = new ArrayList<obj_params2>();
                        for (int i = 0; i < listOption.size(); i++) {
                            if (listOption.get(i).getTitle().toLowerCase().contains(text.toLowerCase())) {
                                Log.w("description", listOption.get(i).getTitle());
                                searchItems.add(new obj_params2(listOption.get(i).getId(), listOption.get(i).getTitle(), listOption.get(i).getText(), listOption.get(i).getDescription() , listOption.get(i).getControl()));
                            }
                        }
                        adapter_list adapter = new adapter_list(form_event.this, searchItems);
                        lv.setAdapter(adapter);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        obj_params2 elegido = (obj_params2) lv.getItemAtPosition(i);

                        textView.setText(elegido.getTitle());
                        textView.setHint(elegido.getId() + "-" + elegido.getControl());

                        optionDialog.dismiss();
                    }
                });
            }
        });
    }

    // spiner lista compuesta con busqueda y seleccion multiple
    public void createSpinnerlistSearch2(int idField, final ArrayList<obj_params2> listOption) {
        LinearLayout llBody = new LinearLayout(this);
        LinearLayout.LayoutParams paramsBody = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llBody.setLayoutParams(paramsBody);
        llBody.setOrientation(LinearLayout.HORIZONTAL);
        llBody.setWeightSum(10);
        llBody.setPadding(10, 10, 10, 10);
        llContenedor.addView(llBody);

        TextView textView = new TextView(this);
        textView.setId(idField);
        textView.setText("Selecione una opcion");
        textView.setHint("");
        textView.setTextSize(14);
        textView.setTextColor(getResources().getColor(R.color.colorTextVariable));
        textView.setBackgroundResource(R.color.colorSpinner);
        textView.setPadding(30, 20, 30, 20);
        LinearLayout.LayoutParams lastTxtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        lastTxtParams.setMargins(0, 30, 0, 0);
        textView.setLayoutParams(lastTxtParams);
        llBody.addView(textView);

        spinnersSearch2.add(textView);

        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 9f);
        lp.gravity = Gravity.CENTER;
        iv.setLayoutParams(lp);
        llBody.addView(iv);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LinearLayout llcrearDialogo = new LinearLayout(form_event.this);
                LinearLayout.LayoutParams parametrosTextoEditor = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                parametrosTextoEditor.setMargins(20, 10, 20, 10);
                llcrearDialogo.setLayoutParams(parametrosTextoEditor);
                llcrearDialogo.setOrientation(LinearLayout.VERTICAL);
                llcrearDialogo.setPadding(10, 10, 10, 10);

                EditText et = new EditText(form_event.this);
                et.setInputType(InputType.TYPE_CLASS_TEXT);
                et.setTextSize(14);
                et.setTextColor(getResources().getColor(R.color.colorTextVariable));
                et.setHint("Buscar");
                et.setBackgroundResource(R.drawable.customedittext);
                et.setPadding(30, 20, 30, 20);

                ListView lv = new ListView(form_event.this);
                LinearLayout.LayoutParams paramsList = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                paramsList.setMargins(0, 10, 0, 0);
                lv.setLayoutParams(paramsList);

                adapter_list2 adapter = new adapter_list2(form_event.this, listOption);
                lv.setAdapter(adapter);

                llcrearDialogo.addView(et);
                llcrearDialogo.addView(lv);

                final AlertDialog optionDialog = new AlertDialog.Builder(form_event.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert).create();
                optionDialog.setTitle("Seleccione una opcion");
                optionDialog.setView(llcrearDialogo);
                optionDialog.show();

                et.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String text = s.toString();
                        Log.w("textSearch", text);
                        Log.w("countList", "" + listOption.size());
                        ArrayList<obj_params2> searchItems = new ArrayList<obj_params2>();
                        for (int i = 0; i < listOption.size(); i++) {
                            if (listOption.get(i).getTitle().toLowerCase().contains(text.toLowerCase())) {
                                Log.w("description", listOption.get(i).getTitle());
                                searchItems.add(new obj_params2(listOption.get(i).getId(), listOption.get(i).getTitle(), listOption.get(i).getText(), listOption.get(i).getDescription(), listOption.get(i).getControl(), listOption.get(i).isActive()));
                            }
                        }
                        adapter_list2 adapter = new adapter_list2(form_event.this, searchItems);
                        lv.setAdapter(adapter);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        obj_params2 elegido = (obj_params2) lv.getItemAtPosition(i);
                        for (int j = 0; j < listOption.size(); j++) {
                            if (listOption.get(j).getId() == elegido.getId()) {
                                if (listOption.get(j).isActive()) {
                                    listOption.get(j).setActive(false);
                                } else {
                                    listOption.get(j).setActive(true);
                                }
                            }
                        }

                        String selectedItem = "";
                        String selectedId = "";
                        int count = 0;
                        for (int k = 0; k < listOption.size(); k++) {
                            if (listOption.get(k).isActive()) {
                                if (selectedItem.equals("")) {
                                    selectedItem = listOption.get(k).getTitle();
                                    selectedId = "" + listOption.get(k).getId();
                                } else {
                                    selectedItem = selectedItem + ", " + listOption.get(k).getTitle();
                                    selectedId = selectedId + "|" + listOption.get(k).getId();
                                }
                                count++;
                            }
                        }

                        if (count == 0)
                        {
                            textView.setText("Seleccione una opcion");
                            textView.setHint("");
                        } else {
                            textView.setText(selectedItem);
                            textView.setHint(selectedId);
                        }

                        optionDialog.dismiss();
                    }
                });
            }
        });
    }
    /*********/

    private boolean validarEmail(String email) {

        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();

    }

    public void createTextViewLocation(int id, String option) {
        ImageView iv = new ImageView(this);
        iv.setId(idField);
        iv.setImageResource(R.drawable.gps);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(150, 150);
        lp.gravity = Gravity.LEFT;
        iv.setLayoutParams(lp);
        llContenedor.addView(iv);

        TextView textView = new TextView(this);
        textView.setId(id);
        textView.setTextSize(14);
        textView.setText(textLocation);
        textView.setHint(option);
        textView.setTextAppearance(this, R.style.colorTitle);
        textView.setVisibility(View.GONE);

        TextView textView2 = new TextView(this);
        textView2.setId(id);
        textView2.setTextSize(14);
        textView2.setText("Ver mapa...");
        textView2.setHint(option);
        textView2.setTextAppearance(this, R.style.colorTitle);
        textView2.setVisibility(View.GONE);

        textViewsLocation.add(textView);
        llContenedor.addView(textView2);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLocation.equals("-1")) {
                    Toast.makeText(form_event.this, "No se encontró la ubicación inténtelo nuevamente", Toast.LENGTH_SHORT).show();
                } else {
                    textView2.setVisibility(View.VISIBLE);
                    textView.setText(mLocation);
                }
            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.google.es/maps?q=" + textView.getText().toString());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    public String getDateQR() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyyMMddHHmm", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private static String encryptSeed(String seed) {
        String sha256 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-256");
            crypt.reset();
            crypt.update(seed.getBytes("UTF-8"));
            sha256 = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sha256;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}