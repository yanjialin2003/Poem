package com.example.myapplication;

import android.app.Application;

import com.example.myapplication.Fragment.AiPoetry.AiPoetryFragment;
import com.xuexiang.xui.XUI;

public class XUIInit extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        XUI.init(this);
        XUI.debug(true);
    }
}
