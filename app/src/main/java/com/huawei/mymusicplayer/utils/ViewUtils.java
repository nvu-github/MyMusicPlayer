package com.huawei.mymusicplayer.utils;

import android.app.Activity;
import android.view.View;

public final class ViewUtils {

    private ViewUtils() {
    }

    public static void setVisibility(View view, int visibility) {
        if (null != view) {
            view.setVisibility(visibility);
        }
    }

    public static void setVisibility(View view, boolean isVisibility) {
        if (null != view) {
            view.setVisibility(isVisibility ? View.VISIBLE : View.GONE);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T findViewById(View view, int id) {
        if (null != view) {
            return (T) view.findViewById(id);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T findViewById(Activity activity, int id) {
        if (null != activity) {
            return (T) activity.findViewById(id);
        }
        return null;
    }

}
