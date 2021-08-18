package diggate.xpertise.com.diggateapk;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class Info extends AppCompatActivity {


    TextView user;
    TextView version;
    String auth;
    String userName;
    String versionGes;
    String fullName;
    Intent ir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Bundle parametros = this.getIntent().getExtras();
        auth = parametros.getString("auth");
        userName = parametros.getString("userName");
        fullName = parametros.getString("fullName");
        user = findViewById(R.id.user);
        try {
            versionGes = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        user.setText("Usuario: " + userName);
        version = findViewById(R.id.version);
        version.setText("Version " + versionGes);
    }

    public void logo(View v) {
        Uri uri = Uri.parse("https://www.portcolon2000.com/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void gesport(View v) {
        Uri uri = Uri.parse("https://www.xpertise-pa.com/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void privacidad(View v) {
        Uri uri = Uri.parse("https://www.portcolon2000.site/politica/politica_privacidad.pdf");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void onBackPressed() {

        ir = new Intent(this, events.class);
        ir.putExtra("auth", auth);
        ir.putExtra("userName", userName);
        ir.putExtra("fullName", fullName);
        startActivity(ir);
        finish();

    }
}
