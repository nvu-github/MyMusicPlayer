package com.huawei.mymusicplayer.account;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.AccountManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.huawei.mymusicplayer.LayoutMain;
import com.huawei.mymusicplayer.R;
import com.huawei.mymusicplayer.fragment.layoutfragment.AccountFragment;

public class AccountActivity extends AppCompatActivity {
    public static final String TAG = "HuaweiIdActivity";
    public static final String PROFILE_INFORMATION = "profile";
    private AccountAuthParams mAuthParam;
    private AccountAuthService mAuthService;
    private Button btnHuaweiLogin, btn_toHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        btnHuaweiLogin = findViewById(R.id.btn_huaweiLogin);
        btn_toHome = findViewById(R.id.btn_toHome);
        Boolean logoutStatus = false; // Set default value
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            if (bundle.containsKey("logout")) { // Check if key exists
                logoutStatus = bundle.getBoolean("logout"); // Update variable accordingly
                signOut();
            }else{
                btnHuaweiLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        silentSignInByHwId();
                    }
                });
            }
        }else{
            btnHuaweiLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    silentSignInByHwId();
                }
            });
        }
        btn_toHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLayoutMain = new Intent(AccountActivity.this, LayoutMain.class);
                startActivity(toLayoutMain);
            }
        });
    }
    private  void silentSignInByHwId() {
        // 1. Use AccountAuthParams to specify the user information to be obtained, including the user ID (OpenID and UnionID), email address, and profile (nickname and picture).
        // 2. By default, DEFAULT_AUTH_REQUEST_PARAM specifies two items to be obtained, that is, the user ID and profile.
        // 3. If your app needs to obtain the user's email address, call setEmail().
        mAuthParam = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setEmail()
                .createParams();

        // Use AccountAuthParams to build AccountAuthService.
        mAuthService = AccountAuthManager.getService(this, mAuthParam);

        // Use silent sign-in to sign in with a HUAWEI ID.
        Task<AuthAccount> task = mAuthService.silentSignIn();
        task.addOnSuccessListener(new OnSuccessListener<AuthAccount>() {
            @Override
            public void onSuccess(AuthAccount authAccount) {
                // The silent sign-in is successful. Process the returned account object AuthAccount to obtain the HUAWEI ID information.
                dealWithResultOfSignIn(authAccount);
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // The silent sign-in fails. Use the getSignInIntent() method to show the authorization or sign-in screen.
                if (e instanceof ApiException) {
                    ApiException apiException = (ApiException) e;
                    Intent signInIntent = mAuthService.getSignInIntent();
                    // If your app appears in full-screen mode duing user sign-in, that is, with no satus bar at the top of the phone screen, add the following parameter in the intent:
                    // intent.putExtra(CommonConstant.RequestParams.IS_FULL_SCREEN, true);
                    // Check the details in this FAQ.
                    //signInIntent.putExtra(CommonConstant.RequestParams.IS_FULL_SCREEN, true);
                    startActivityForResult(signInIntent, Constant.REQUEST_CODE_SIGN_IN);
                }
            }
        });
    }

    /**
     * Process the returned AuthAccount object to obtain the HUAWEI ID information.
     *
     * @param authAccount AuthAccount object, which contains the HUAWEI ID information.
     */
    private void dealWithResultOfSignIn(AuthAccount authAccount) {
        // Obtain the HUAWEI ID information.
        Log.i(TAG, "display name:" + authAccount.getDisplayName());
        Log.i(TAG, "photo uri string:" + authAccount.getAvatarUriString());
        Log.i(TAG, "photo uri:" + authAccount.getAvatarUri());
        Log.i(TAG, "email:" + authAccount.getEmail());
        Log.i(TAG, "openid:" + authAccount.getOpenId());
        Log.i(TAG, "unionid:" + authAccount.getUnionId());
        // TODO: Implement service logic after the HUAWEI ID information is obtained.
        SharedPreferences.Editor editor = getSharedPreferences(PROFILE_INFORMATION, MODE_PRIVATE).edit();
        editor.putString("display_name", authAccount.getDisplayName());
        editor.putString("union_id", authAccount.getUnionId());
        editor.putString("email", authAccount.getEmail());
        editor.putString("avatar", authAccount.getAvatarUri().toString());
        editor.apply();
        Intent toLayoutMain = new Intent(this,LayoutMain.class);
        startActivity(toLayoutMain);
    }
    private void signOut() {
        SharedPreferences.Editor editor = getSharedPreferences(PROFILE_INFORMATION, MODE_PRIVATE).edit();
        if(mAuthService != null){
           Task<Void> signOutTask = mAuthService.signOut();
           signOutTask.addOnSuccessListener(new OnSuccessListener<Void>() {
               @Override
               public void onSuccess(Void aVoid) {
                   Toast.makeText(AccountActivity.this, "Logout success", Toast.LENGTH_SHORT).show();
                   editor.clear();
                   editor.apply();
                   Log.i(TAG, "signOut Success");
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(Exception e) {
                   Log.i(TAG, "signOut fail");
                   Toast.makeText(AccountActivity.this, "Logout fail!", Toast.LENGTH_SHORT).show();
                   Intent toLayoutMain = new Intent(AccountActivity.this, LayoutMain.class);
                   startActivity(toLayoutMain);
               }
           });
       }else{
            editor.clear();
            editor.apply();
           Toast.makeText(this, "Logout success", Toast.LENGTH_SHORT).show();
       }
    }

}