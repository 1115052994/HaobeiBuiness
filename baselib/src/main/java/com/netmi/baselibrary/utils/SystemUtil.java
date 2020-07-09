package com.netmi.baselibrary.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.SystemClock;

/**
 * 类描述：
 * 创建人：Jacky
 * 创建时间：2019/1/14
 * 修改备注：
 */
public class SystemUtil {
    //复制字符串到粘贴板
    public static void CopyToClipboard(Context context, String text) {
        ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clip.setText(text); // 复制
    }
    //粘贴字符串
    public static String pasteClipboard(Context context) {
        ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return clip.getText().toString(); // 粘贴
    }


    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = SystemClock.uptimeMillis(); // 此方法仅用于Android
        if (time - lastClickTime < 400) {
            return true;
        }
        lastClickTime = time;
        return false;
    }


}
