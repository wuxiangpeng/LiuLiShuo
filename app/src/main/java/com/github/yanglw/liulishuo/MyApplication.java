package com.github.yanglw.liulishuo;

import android.app.Application;

import com.github.yanglw.liulishuo.util.HttpManager;

/**
 * Created by yanglw on 2015/4/3.
 */
public class MyApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        HttpManager.init(this);
    }
}
