package com.huawei.mymusicplayer.fragment.layoutfragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
import com.huawei.mymusicplayer.Constant;
import com.huawei.mymusicplayer.MainActivity;
import com.huawei.mymusicplayer.Playlist;
import com.huawei.mymusicplayer.R;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AccountFragment extends Fragment {
    public static final String TAG = "HuaweiIdActivity";
    private AccountAuthParams mAuthParam;
    private AccountAuthService mAuthService;
    TextView add_playlist;
    ImageView mySong;
    RecyclerView listPlaylist;
    ArrayList<Playlist> arrPlaylist;
    CustomAdapter myAdapter;
    DatabaseReference database;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        add_playlist = view.findViewById(R.id.add_playlist);
        add_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(Gravity.CENTER);
            }

        });
        mySong = view.findViewById(R.id.mySong);
        mySong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("playlist", 1);
                startActivity(intent);
            }
        });
        silentSignInByHwId();

        database = FirebaseDatabase.getInstance("https://mymusicplayer-5e719-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Playlist");
        listPlaylist = view.findViewById(R.id.listPlaylist);
        listPlaylist.setLayoutManager(new LinearLayoutManager(getActivity()));
        arrPlaylist = new ArrayList<>();
        myAdapter = new CustomAdapter(getActivity(), arrPlaylist, new CustomAdapter.IClickListener() {
            @Override
            public void onClickDeleteItem(Playlist playlist) {
                onClickDeleteData(playlist);
            }
        });

        listPlaylist.setAdapter(myAdapter);
        return view;

    }

    public void setText(String text){
        TextView textView = (TextView) getView().findViewById(R.id.profile);
        textView.setText(text);
    }

    public void showdata(String account_id)
    {
        database.orderByChild("account_id").equalTo(account_id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Playlist pl = snapshot.getValue(Playlist.class);
                if(pl != null)
                {
                    arrPlaylist.add(pl);
                    myAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Playlist pl = snapshot.getValue(Playlist.class);
                if(pl == null || arrPlaylist == null || arrPlaylist.isEmpty()){
                    return;
                }
                for(int i = 0; i<arrPlaylist.size(); i++)
                {
                    if(pl.getKey() == arrPlaylist.get(i).getKey())
                    {
                        arrPlaylist.remove(arrPlaylist.get(i));
                        break;
                    }
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void onClickDeleteData(Playlist playlist){
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.app_name))
                .setMessage("Bạn có chắc muốn xoá playlist này không?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.child(String.valueOf(playlist.getKey())).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(getActivity(), "Delete playlist success", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    private void openDialog(int gravity)
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowattributes = window.getAttributes();
        windowattributes.gravity = gravity;
        window.setAttributes(windowattributes);

        if(Gravity.CENTER == gravity){
            dialog.setCancelable(true);
        }else{
            dialog.setCancelable(false);
        }

        EditText editText = dialog.findViewById(R.id.playlist_name);
        Button button = dialog.findViewById(R.id.btn_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_add:
                        String name = editText.getText().toString().trim();
                        if(!TextUtils.isEmpty(name)){
                            String key = database.push().getKey();
                            Playlist playlist = new Playlist(key,name);
                            database.child(key).setValue(playlist);
                            Toast.makeText(getActivity(), "Thêm playlist thành công", Toast.LENGTH_LONG).show();

                        }else{
                            Toast.makeText(getActivity(), "Thêm playlist thất bại", Toast.LENGTH_LONG).show();
                        }
                }
            }
        });
        dialog.show();
    }


private  void silentSignInByHwId() {
    // 1. Use AccountAuthParams to specify the user information to be obtained, including the user ID (OpenID and UnionID), email address, and profile (nickname and picture).
    // 2. By default, DEFAULT_AUTH_REQUEST_PARAM specifies two items to be obtained, that is, the user ID and profile.
    // 3. If your app needs to obtain the user's email address, call setEmail().
    mAuthParam = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            .setEmail()
            .createParams();

    // Use AccountAuthParams to build AccountAuthService.
    mAuthService = AccountAuthManager.getService(getActivity(), mAuthParam);

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
        // Obtain the HUAWEI DI information.
        Log.i(TAG, "display name:" + authAccount.getDisplayName());
        Log.i(TAG, "photo uri string:" + authAccount.getAvatarUriString());
        Log.i(TAG, "photo uri:" + authAccount.getAvatarUri());
        Log.i(TAG, "email:" + authAccount.getEmail());
        Log.i(TAG, "openid:" + authAccount.getOpenId());
        Log.i(TAG, "unionid:" + authAccount.getUnionId());
        // TODO: Implement service logic after the HUAWEI ID information is obtained.
        showdata(authAccount.getUnionId());
        setText(authAccount.getEmail());
        // show The Image in a ImageView
        new DownloadImageTask((ImageView) getView().findViewById(R.id.profile_avatar))
                .execute(authAccount.getAvatarUri().toString());
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> { // class lấy link avatar từ profile huawei convert to bitmap và hiển thị trên imageview
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
