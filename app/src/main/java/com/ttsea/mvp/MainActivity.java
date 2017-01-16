package com.ttsea.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ttsea.jlibrary.common.JToast;
import com.ttsea.mvp.base.BaseActivity;
import com.ttsea.mvp.module.guide.MainGuideActivity;
import com.ttsea.mvp.module.login.LoginActivity;
import com.ttsea.mvp.module.login.LoginEventEntity;
import com.ttsea.mvp.rxBus2.RxBus;
import com.ttsea.mvp.rxBus2.Subscribe;

public class MainActivity extends BaseActivity {
    private final String TAG = "MainActivity";

    private Button btnLogin;
    private Button btnGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RxBus.getInstance().register(this);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnGuide = (Button) findViewById(R.id.btnGuide);

        btnLogin.setOnClickListener(mOnSingleClickListener);
        btnGuide.setOnClickListener(mOnSingleClickListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(this);
    }

    @Override
    public void onSingleClick(View view) {
        Intent intent;
        Bundle bundle;

        switch (view.getId()) {
            case R.id.btnLogin:
                intent = new Intent(mActivity, LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.btnGuide:
                intent = new Intent(mActivity, MainGuideActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    @Subscribe
    public void onLoginEvent(LoginEventEntity loginEventEntity) {
        if (loginEventEntity.getStatus() == LoginEventEntity.ACTION_LOGIN_SUCCESS) {
            JToast.makeText(mActivity, "login success");

        } else if (loginEventEntity.getStatus() == LoginEventEntity.ACTION_LOGIN_FAILED) {
            JToast.makeText(mActivity, "login failed, reason:" + loginEventEntity.getMsg());
        }
    }

}
