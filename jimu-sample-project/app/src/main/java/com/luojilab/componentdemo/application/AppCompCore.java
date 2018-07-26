package com.luojilab.componentdemo.application;

import android.util.Log;

import com.luojilab.component.componentlib.applicationlike.IApplicationLike;

public class AppCompCore {

    public static void rigisterComp(IApplicationLike iApplicationLike){
        Log.d("asm---",""+iApplicationLike.getClass().getSimpleName());
        iApplicationLike.onCreate();
    }

    public static void initComp(){

    }

}
