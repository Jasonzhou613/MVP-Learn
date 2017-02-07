package com.ttsea.mvp.module.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ttsea.jrxbus2.RxBus2;
import com.ttsea.mvp.R;
import com.ttsea.mvp.base.BaseActivity;
import com.ttsea.mvp.module.guide.MainGuideActivity;

/**
 * // to do <br/>
 * <p>
 * <b>more:</b> 更多请参考<a href="http://www.ttsea.com" title="小周博客">www.ttsea.com</a> <br/>
 * <b>date:</b> 2016/12/15 10:37 <br/>
 * <b>author:</b> Jason <br/>
 * <b>version:</b> 1.0 <br/>
 * <b>last modified date:</b> 2016/12/15 10:37.
 */
public class LoginActivity extends BaseActivity implements Login.View {
    private final String TAG = "LoginActivity";

    private EditText etUserName;
    private EditText etPwd;
    private Button btnLogin;
    private Button btnGuide;
    private Button btnSendEvent;
    private Button btnSendStickEvent;

    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        loginPresenter = new LoginPresenter(mActivity, this);
        if (getIntent() != null && getIntent().getExtras() != null
                && getIntent().getExtras().getParcelable("intent") != null
                && getIntent().getExtras().getParcelable("intent") instanceof Intent) {
            Intent intent = (Intent) getIntent().getExtras().getParcelable("intent");
            loginPresenter.setLoginSuccessIntent(intent);
        }

        etUserName = (EditText) findViewById(R.id.etUserName);
        etPwd = (EditText) findViewById(R.id.etPwd);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnGuide = (Button) findViewById(R.id.btnGuide);
        btnSendEvent = (Button) findViewById(R.id.btnSendEvent);
        btnSendStickEvent = (Button) findViewById(R.id.btnSendStickEvent);

        btnLogin.setOnClickListener(mOnSingleClickListener);
        btnGuide.setOnClickListener(mOnSingleClickListener);
        btnSendEvent.setOnClickListener(mOnSingleClickListener);
        btnSendStickEvent.setOnClickListener(mOnSingleClickListener);

        etUserName.setText("aaa108");
        etPwd.setText("123456");
    }

    @Override
    public void onSingleClick(View view) {
        Intent intent;

        switch (view.getId()) {
            case R.id.btnLogin:
                loginPresenter.login(etUserName.getText().toString(), etPwd.getText().toString());
                break;

            case R.id.btnGuide:
                intent = new Intent(mActivity, MainGuideActivity.class);
                startActivity(intent);
                break;

            case R.id.btnSendEvent:
                RxBus2.getInstance().post("normal event");
                break;

            case R.id.btnSendStickEvent:
                RxBus2.getInstance().postStickyEvent(2, "sticky event");
                break;

            default:
                break;
        }
    }

    @Override
    public void focusView(int viewId) {
        if (findViewById(viewId) != null) {
            findViewById(viewId).requestFocus();
        }
    }
}
