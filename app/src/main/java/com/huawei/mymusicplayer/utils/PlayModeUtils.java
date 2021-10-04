package com.huawei.mymusicplayer.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.huawei.mymusicplayer.R;
import com.huawei.mymusicplayer.fragment.PlayHelper;

import static android.content.ContentValues.TAG;

//import static com.huawei.mymusicplayer.AccountActivity.TAG;

/**
 * Play mode utils
 *
 * @since 2020-06-01
 */
public final class PlayModeUtils {

    private static final int MODE_SIZE = 4;

    private static final int MODE_SHUFFLE_OFF = 0;

    private static final int MODE_SHUFFLE_ON = 1;

    private static final int MODE_REPEAT_ALL = 2;

    private static final int MODE_REPEAT_SELF = 3;

    private static final PlayModeUtils INSTANCE = new PlayModeUtils();

    private PlayModeUtils() {

    }

    public static PlayModeUtils getInstance() {
        return INSTANCE;
    }

    public void changePlayMode(Context context, ImageView view) {
        Log.i(TAG, "changePlayMode");
        if (null == view || context == null) {
            return;
        }
        int playMode = PlayHelper.getInstance().getPlayMode();
        playMode = (playMode + 1) % PlayModeUtils.MODE_SIZE;
        switch (playMode) {
            case MODE_SHUFFLE_OFF:
                view.setContentDescription(context.getString(R.string.mode_shuffle_off));
                toastShortMsg(context, R.string.mode_shuffle_off);
                view.setImageResource(R.drawable.menu_order_normal);
                PlayHelper.getInstance().setPlayMode(MODE_SHUFFLE_OFF);
                break;
            case MODE_SHUFFLE_ON:
                view.setContentDescription(context.getString(R.string.mode_shuffle_on));
                toastShortMsg(context, R.string.mode_shuffle_on);
                view.setImageResource(R.drawable.menu_shuffle_normal);
                PlayHelper.getInstance().setPlayMode(MODE_SHUFFLE_ON);
                break;
            case MODE_REPEAT_ALL:
                view.setContentDescription(context.getString(R.string.mode_repeat_all));
                toastShortMsg(context, R.string.mode_repeat_all);
                view.setImageResource(R.drawable.menu_loop_normal);
                PlayHelper.getInstance().setPlayMode(MODE_REPEAT_ALL);
                break;
            case MODE_REPEAT_SELF:
                view.setContentDescription(context.getString(R.string.mode_repeat_current));
                toastShortMsg(context, R.string.mode_repeat_current);
                view.setImageResource(R.drawable.menu_loop_one_normal);
                PlayHelper.getInstance().setPlayMode(MODE_REPEAT_SELF);
                break;
            default:
                break;
        }
    }

    private void toastShortMsg(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    public void updatePlayMode(Context context, ImageView view) {
        Log.i(TAG, "updatePlayMode");
        if (null == view || context == null) {
            return;
        }
        int mode = PlayHelper.getInstance().getPlayMode();
        if (mode < 0) {
            return;
        }
        switch (mode) {
            case MODE_SHUFFLE_OFF:
                view.setContentDescription(context.getString(R.string.mode_shuffle_off));
                view.setImageResource(R.drawable.menu_order_normal);
                break;
            case MODE_SHUFFLE_ON:
                view.setContentDescription(context.getString(R.string.mode_shuffle_on));
                view.setImageResource(R.drawable.menu_shuffle_normal);
                break;
            case MODE_REPEAT_ALL:
                view.setContentDescription(context.getString(R.string.mode_repeat_all));
                view.setImageResource(R.drawable.menu_loop_normal);
                break;
            case MODE_REPEAT_SELF:
                view.setContentDescription(context.getString(R.string.mode_repeat_current));
                view.setImageResource(R.drawable.menu_loop_one_normal);
                break;
            default:
                Log.w(TAG, "Unknown mode:" + mode);
                break;
        }
    }
}
