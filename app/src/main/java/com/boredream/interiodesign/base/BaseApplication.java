package com.boredream.interiodesign.base;


import android.app.Application;

import com.boredream.bdcodehelper.db.AddressData;
import com.boredream.interiodesign.net.HttpRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.facebook.stetho.Stetho;
import com.squareup.okhttp.OkHttpClient;

import java.io.InputStream;

public class BaseApplication extends Application {

    private static BaseApplication instance;

    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        initStetho();
        initGlide();
        initDatabase();
    }

    /**
     * 浏览器调试框架Stetho
     */
    private void initStetho() {
        Stetho.initializeWithDefaults(this);
    }

    /**
     * 图片加载框架Glide,使用OkHttp处理网络请求
     */
    private void initGlide() {
        OkHttpClient okHttpClient = HttpRequest.getHttpClient();
        Glide.get(this).register(GlideUrl.class, InputStream.class,
                new OkHttpUrlLoader.Factory(okHttpClient));
    }

    private void initDatabase() {
        AddressData.init(this);
    }
}
