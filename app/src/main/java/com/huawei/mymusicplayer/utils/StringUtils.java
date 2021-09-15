package com.huawei.mymusicplayer.utils;

import android.content.Context;

import com.huawei.mymusicplayer.R;

import java.text.NumberFormat;
import java.util.Formatter;
import java.util.Locale;
import java.util.concurrent.locks.ReentrantLock;

public final class StringUtils {

    private static final ReentrantLock LOCK = new ReentrantLock();

    private StringUtils() {
    }

    public static String makeTimeString(Context context, long secs) {
        StringBuilder sFormatBuilder = new StringBuilder();
        Formatter sFormatter = new Formatter(sFormatBuilder, Locale.getDefault());
        LOCK.lock();
        String sTime;
        try {
            String durationFormat =
                    context.getString(secs >= 216000 ? R.string.durationformatlong : R.string.durationformatshort);
            final Object[] timeArgs = new Object[5];
            timeArgs[0] = secs / 3600;
            timeArgs[1] = secs / 60;
            timeArgs[2] = (secs / 60) % 60;
            timeArgs[3] = secs;
            timeArgs[4] = secs % 60;
            sTime = sFormatter.format(durationFormat, timeArgs).toString();
        } finally {
            LOCK.unlock();
            sFormatter.close();
        }

        return sTime;
    }

    public static String localeString(Context context, long secs) {
        String s = StringUtils.makeTimeString(context, secs);
        return localeString(s);
    }

    public static String localeString(String s) {
        int zero = 0;
        NumberFormat nf = NumberFormat.getIntegerInstance();
        nf.setGroupingUsed(false);
        char localeZero = nf.format(zero).charAt(0);
        if (localeZero != '0') {
            int length = s.length();
            int offsetToLocalizedDigits = localeZero - '0';
            StringBuilder result = new StringBuilder(length);
            for (int i = 0; i < length; ++i) {
                char ch = s.charAt(i);
                if (ch >= '0' && ch <= '9') {
                    ch += offsetToLocalizedDigits;
                }
                result.append(ch);
            }
            return result.toString();
        }
        return s;
    }

    public static boolean isEmpty(CharSequence value) {
        return null == value || 0 == value.length();
    }

    public static String[] split(String src, String split) {
        if (isEmpty(src) || isEmpty(split)) {
            return new String[0];
        }
        if (!src.contains(split)) {
            return new String[] {src};
        }
        return src.split(split);
    }
}
