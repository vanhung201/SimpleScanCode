package hung.lv.simplescancode;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;
import com.huawei.hms.support.api.entity.common.CommonConstant;
import com.huawei.hms.support.hwid.ui.HuaweiIdAuthButton;

public class LoginActivity extends AppCompatActivity {
    private ImageView loginBtn;
    private EditText mobileNumber;
    private EditText password;
    private HuaweiIdAuthButton huaweiLogin;
    private AccountAuthService mAuthService;
    private AccountAuthParams mAuthParam;
    private static final int REQUEST_CODE_SIGN_IN = 1000;
    private static final String TAG = "Account";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUi();

        huaweiLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                silentSignInByHwId();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sMobile = mobileNumber.getText().toString().trim();
                String sPassword = password.getText().toString().trim();

                if (sMobile.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter the Mobile Number", Toast.LENGTH_SHORT).show();
                } else if (sPassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter the Password", Toast.LENGTH_SHORT).show();
                } else {
                    if (sMobile.equals("0355012525")) {
                        if (sPassword.equals("123456")) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("name", "Admin");
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "The Password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "The Mobile Number is incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void initUi() {
        mobileNumber = (EditText) findViewById(R.id.et_mobileNumber);
        password = (EditText) findViewById(R.id.et_password);
        loginBtn = (ImageView) findViewById(R.id.img_login);
        huaweiLogin = findViewById(R.id.huaweiLogin);
    }

    private void silentSignInByHwId() {
        mAuthParam = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setEmail()
                .createParams();

        mAuthService = AccountAuthManager.getService(this, mAuthParam);

        Task<AuthAccount> task = mAuthService.silentSignIn();
        task.addOnSuccessListener(new OnSuccessListener<AuthAccount>() {
            @Override
            public void onSuccess(AuthAccount authAccount) {
                dealWithResultOfSignIn(authAccount);
                Toast.makeText(LoginActivity.this, "Welcome " + authAccount.getDisplayName(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("name", authAccount.getDisplayName());
                startActivity(intent);
                finish();
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    ApiException apiException = (ApiException) e;
                    Intent signInIntent = mAuthService.getSignInIntent();
                    signInIntent.putExtra(CommonConstant.RequestParams.IS_FULL_SCREEN, true);
                    startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
                }
            }
        });
    }

    private void dealWithResultOfSignIn(AuthAccount authAccount) {
        Log.i(TAG, "display name:" + authAccount.getDisplayName());
        Log.i(TAG, "photo uri string:" + authAccount.getAvatarUriString());
        Log.i(TAG, "photo uri:" + authAccount.getAvatarUri());
        Log.i(TAG, "email:" + authAccount.getEmail());
        Log.i(TAG, "openid:" + authAccount.getOpenId());
        Log.i(TAG, "unionid:" + authAccount.getUnionId());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            Log.i(TAG, "onActivitResult of sigInInIntent, request code: " + REQUEST_CODE_SIGN_IN);
            Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data);
            if (authAccountTask.isSuccessful()) {
                AuthAccount authAccount = authAccountTask.getResult();
                dealWithResultOfSignIn(authAccount);
                Log.i(TAG, "onActivitResult of sigInInIntent, request code: " + REQUEST_CODE_SIGN_IN);
            } else {
                Log.e(TAG, "sign in failed : " + ((ApiException) authAccountTask.getException()).getStatusCode());
            }
        }
    }
}