package com.ttsea.mvp.module.guide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ttsea.jlibrary.common.JLog;
import com.ttsea.jlibrary.common.JToast;
import com.ttsea.jrxbus2.RxBus2;
import com.ttsea.jrxbus2.Subscribe;
import com.ttsea.mvp.R;
import com.ttsea.mvp.base.BaseActivity;
import com.ttsea.mvp.module.login.LoginActivity;

/**
 * // to do <br/>
 * <p>
 * <b>more:</b> 更多请参考<a href="http://www.ttsea.com" title="小周博客">www.ttsea.com</a> <br/>
 * <b>date:</b> 2017/1/11 17:08 <br/>
 * <b>author:</b> Jason <br/>
 * <b>version:</b> 1.0 <br/>
 * <b>last modified date:</b> 2017/1/11 17:08.
 */
public class MainGuideActivity extends BaseActivity {
    private final String TAG = "MainGuideActivity";

    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_guide);
        RxBus2.getInstance().register(this);

        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(mOnSingleClickListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus2.getInstance().unRegister(this);
    }

    @Override
    public void onSingleClick(View view) {
        Intent intent;

        switch (view.getId()) {
            case R.id.btnLogin:
                intent = new Intent(mActivity, LoginActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    @Subscribe(receiveStickyEvent = true)
    public void onRxBusEvent1(String msg) {
        JLog.d(TAG, "guide, " + msg);
        JToast.makeText(mActivity, "guide, " + msg);
    }

    @Subscribe
    public void onRxBusEvent2(String msg) {
        JLog.d(TAG, "guide, " + msg);
        JToast.makeText(mActivity, "guide, " + msg);
    }

    @Subscribe(code = 1)
    public void onRxBusEvent3(String msg) {
        JLog.d(TAG, "guide, " + msg);
        JToast.makeText(mActivity, "guide, " + msg);
    }
}
