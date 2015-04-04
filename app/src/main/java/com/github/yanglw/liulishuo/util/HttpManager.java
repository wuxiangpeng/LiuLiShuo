package com.github.yanglw.liulishuo.util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by yanglw on 2015/4/3.
 */
public class HttpManager
{
    private static RequestQueue sRequestQueue;

    public static void init(Context context)
    {
        if (sRequestQueue == null)
        {
            sRequestQueue = Volley.newRequestQueue(context);
        }
    }

    public static <T> Request<T> add(Request<T> request)
    {
        return sRequestQueue.add(request);
    }
}
