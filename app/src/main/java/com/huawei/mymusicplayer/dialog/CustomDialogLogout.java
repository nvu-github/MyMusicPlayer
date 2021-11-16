package com.huawei.mymusicplayer.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.mymusicplayer.R;
import com.huawei.mymusicplayer.account.AccountActivity;
import com.huawei.mymusicplayer.fragment.layoutfragment.AccountFragment;

public class CustomDialogLogout extends Dialog {
    public static final String TAG = "DialogLogout";
    public Context context;
    private Button btn_dialog_logout, btn_dialog_cancel;
    public CustomDialogLogout(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_logout);

        this.btn_dialog_logout = findViewById(R.id.btn_dialog_logout);
        this.btn_dialog_cancel = findViewById(R.id.btn_dialog_cancel);

        this.btn_dialog_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAccountActivity = new Intent(context, AccountActivity.class);
                toAccountActivity.putExtra("logout", true);
                context.startActivity(toAccountActivity);
            }
        });

        this.btn_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelProcess();
            }
        });
    }

    // User click "Cancel" button.
    private void cancelProcess()  {
        this.dismiss();
    }
}
