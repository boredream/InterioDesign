package com.boredream.interiodesign.activity;

import android.os.Bundle;
import android.text.TextUtils;

import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.interiodesign.R;
import com.boredream.interiodesign.base.BaseActivity;
import com.boredream.interiodesign.base.BaseEntity;
import com.boredream.interiodesign.entity.HouseType;
import com.boredream.interiodesign.net.HttpRequest;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }
}
