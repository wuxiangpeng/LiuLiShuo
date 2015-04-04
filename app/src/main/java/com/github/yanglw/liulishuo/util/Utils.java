package com.github.yanglw.liulishuo.util;

import android.content.Context;

/**
 * Created by yanglw on 2015/4/3.
 */
public class Utils
{
    public static int dp2px(Context context, float dpValue)
    {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
