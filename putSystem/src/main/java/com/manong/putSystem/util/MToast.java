package com.manong.putSystem.util;

import android.content.Context;
import android.widget.Toast;

/**
 * @author: admin
 * @date: 2020/6/19
 * @Explain:
 */
public class MToast {

    /**
     * 解决吐丝时有APP名称问题
     * @param context
     * @param message
     * @param duration
     */
    public static void showToast(Context context, String message, int duration){
        Toast toast = Toast.makeText(context, null, duration);
        toast.setText(message);
        toast.show();
    }

}
