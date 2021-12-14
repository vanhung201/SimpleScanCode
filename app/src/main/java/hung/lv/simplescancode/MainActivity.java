package hung.lv.simplescancode;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.support.account.service.AccountAuthService;

public class MainActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {
    public static final int DEFINED_CODE = 222;
    public static final int BITMAP_CODE = 333;
    public static final int MULTIPROCESSOR_SYN_CODE = 444;
    public static final int MULTIPROCESSOR_ASYN_CODE = 555;
    public static final int GENERATE_CODE = 666;
    public static final int DECODE = 1;
    public static final int GENERATE = 2;
    private static final int REQUEST_CODE_DEFINE = 0X0111;
    public static final String DECODE_MODE = "decode_mode";
    public static final String RESULT = "SCAN_RESULT";
    private TextView accountName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            if (window != null) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }

        accountName = (TextView) findViewById(R.id.tv_accountName);
        accountName.setText("Welcome " + getIntent().getStringExtra("name"));
    }

    public void newViewBtnClick(View view) {
        requestPermission(DEFINED_CODE, DECODE);
    }

    public void generateQRCodeBtnClick(View view) {
        requestPermission(GENERATE_CODE, GENERATE);
    }

    private void requestPermission(int requestCode, int mode) {
        if (mode == DECODE) {
            decodePermission(requestCode);
        } else if (mode == GENERATE) {
            generatePermission(requestCode);
        }
    }

    private void decodePermission(int requestCode) {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                requestCode);
    }

    private void generatePermission(int requestCode) {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (permissions == null || grantResults == null) {
            return;
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == GENERATE_CODE) {
            Intent intent = new Intent(this, GenerateCodeActivity.class);
            this.startActivity(intent);
        }

        if (grantResults.length < 2 || grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //Customized View Mode
        if (requestCode == DEFINED_CODE) {
            Intent intent = new Intent(this, DefinedActivity.class);
            this.startActivityForResult(intent, REQUEST_CODE_DEFINE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }

        if (requestCode == REQUEST_CODE_DEFINE) {
            HmsScan obj = data.getParcelableExtra(DefinedActivity.SCAN_RESULT);
            if (obj != null) {
                Intent intent = new Intent(this, DisPlayActivity.class);
                intent.putExtra(RESULT, obj);
                startActivity(intent);
            }
        }
    }
}
