package com.huawei.mymusicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;

public class AccountActivity extends AppCompatActivity{
    ImageView logoHeartBeat, logoHuawei, logoGuest;
    TextView txt_SignIn, txt_SignIn_Guest;
    Animation topAnimation, leftAnimation, rightAnimation;

    public static final String TAG = "HuaweiIdActivity";
    private AccountAuthService mAuthManager;
    private AccountAuthParams mAuthParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation_sign);
        leftAnimation = AnimationUtils.loadAnimation(this, R.anim.left_animation_sign);
        rightAnimation = AnimationUtils.loadAnimation(this, R.anim.right_animaiton_sign);

        logoHeartBeat = findViewById(R.id.logoHeartBeat);
        logoHuawei = findViewById(R.id.logoHuawei);
        logoGuest = findViewById(R.id.logoGuest);

        txt_SignIn = findViewById(R.id.txt_SignIn);
        txt_SignIn_Guest = findViewById(R.id.txt_SignIn_Guest);

        logoHeartBeat.setAnimation(topAnimation);
        logoHuawei.setAnimation(leftAnimation);
        logoGuest.setAnimation(rightAnimation);

        txt_SignIn.setAnimation(leftAnimation);
        txt_SignIn_Guest.setAnimation(rightAnimation);

    }

    public void guestAction(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void huaweiAction(View v) {
        Toast.makeText(this, "login in progress, wait a moment",
                Toast.LENGTH_LONG).show();
        signInCode();
    }

    private void signInCode() {
        mAuthParam = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setProfile()
                .setAuthorizationCode()
                .createParams();
        mAuthManager = AccountAuthManager.getService(AccountActivity.this, mAuthParam);
        startActivityForResult(mAuthManager.getSignInIntent(), 1003);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1002) {
            //login success
            //get user message by parseAuthResultFromIntent
            Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data);
            if (authAccountTask.isSuccessful()) {
                AuthAccount authAccount = authAccountTask.getResult();
                Log.i(TAG, authAccount.getDisplayName() + " signIn success ");
                Log.i(TAG, "AccessToken: " + authAccount.getAccessToken());
            } else {
                Log.i(TAG, "signIn failed: " + ((ApiException) authAccountTask.getException()).getStatusCode());
            }
        }
        if (requestCode == 1003) {
            //login success
            Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data);
            if (authAccountTask.isSuccessful()) {
                AuthAccount authAccount = authAccountTask.getResult();
                Log.i(TAG, "signIn get code success.");
                Log.i(TAG, "ServerAuthCode: " + authAccount.getAuthorizationCode());
                Toast.makeText(this, "Login success. Enjoy",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Sorry. Login fail, try again or using like a guest. ",
                        Toast.LENGTH_LONG).show();
                Log.i(TAG, "signIn get code failed: " + ((ApiException) authAccountTask.getException()).getStatusCode());
            }
        }
    }
}

