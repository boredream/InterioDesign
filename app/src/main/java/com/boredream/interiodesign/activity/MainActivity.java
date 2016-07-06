package com.boredream.interiodesign.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.bdcodehelper.fragment.FragmentController;
import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.interiodesign.R;
import com.boredream.interiodesign.base.BaseActivity;
import com.boredream.interiodesign.base.BaseEntity;
import com.boredream.interiodesign.entity.OpenDate;
import com.boredream.interiodesign.fragment.CommunityFragment;
import com.boredream.interiodesign.fragment.HouseTypeFragment;
import com.boredream.interiodesign.net.HttpRequest;
import com.boredream.interiodesign.utils.UpdateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;


public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup rg_bottom_tab;
    private RadioButton rb1;
    private FragmentController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 如果是退出应用flag,则直接关闭当前页面,不加载UI
        boolean exit = getIntent().getBooleanExtra("exit", false);
        if (exit) {
            finish();
            return;
        }

        initView();
        initData();

        ObservableDecorator.decorate(this, HttpRequest.getOpenDate(1, "openDate")).subscribe(
                new Subscriber<ListResponse<OpenDate>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ListResponse<OpenDate> response) {
                        for (final OpenDate od : response.getResults()) {
                            String openDate = od.getOpen_date();
                            if (!openDate.contains("2")) {
                                continue;
                            }

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
                            Map<String, Object> params = new HashMap<>();

                            try {
                                sdf.parse(openDate);
                                params.put("openDate", openDate);
                            } catch (ParseException e) {
                                e.printStackTrace();

                                StringBuilder sb = new StringBuilder();

                                Pattern pattern = Pattern.compile(".*?([\\d]{4})[\\D]+?([\\d]{1,2})?([\\D]+)?([\\d]{1,2})?[\\s\\S]*");
                                Matcher matcher = pattern.matcher(openDate);
                                if (matcher.find()) {
                                    String year = matcher.group(1);
                                    String month = matcher.group(2);
                                    String day = matcher.group(4);

                                    if (year == null) {
                                        continue;
                                    }

                                    sb.append(year);

                                    if (month != null) {
                                        sb.append("-").append(month);
                                        if (day != null) {
                                            sb.append("-").append(day);
                                        }
                                    }
                                }

                                params.put("openDate", sb.toString());
                                showLog(sb.toString());
                            }

                            if (params.size() > 0) {
                                ObservableDecorator.decorate(MainActivity.this,
                                        HttpRequest.getApiService().updateOpenDate(od.getObjectId(), params)).subscribe(
                                        new Subscriber<BaseEntity>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }

                                            @Override
                                            public void onNext(BaseEntity baseEntity) {

                                            }
                                        }
                                );
                            }
                        }
                    }
                }
        );
    }

    private void initView() {
        rg_bottom_tab = (RadioGroup) findViewById(R.id.rg_bottom_tab);
        rb1 = (RadioButton) findViewById(R.id.rb1);

        rg_bottom_tab.setOnCheckedChangeListener(this);
    }

    private void initData() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new CommunityFragment());
        fragments.add(new HouseTypeFragment());
        fragments.add(new CommunityFragment());
        fragments.add(new CommunityFragment());
        controller = new FragmentController(this, R.id.fl_content, fragments);
        // 默认选择fragment
        rb1.setChecked(true);
        controller.showFragment(0);

        UpdateUtils.checkUpdate(this, false);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rb1:
                controller.showFragment(0);
                break;
            case R.id.rb2:
                controller.showFragment(1);
                break;
            case R.id.rb3:
                controller.showFragment(2);
                break;
            case R.id.rb4:
                controller.showFragment(3);
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        // 双击返回键关闭程序
        // 如果两秒重置时间内再次点击返回,则退出程序
        if (doubleBackToExitPressedOnce) {
            exit();
            return;
        }

        doubleBackToExitPressedOnce = true;
        showToast("再按一次返回键关闭程序");
        Observable.just(null)
                .delay(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        // 延迟两秒后重置标志位为false
                        doubleBackToExitPressedOnce = false;
                    }
                });
    }

}
