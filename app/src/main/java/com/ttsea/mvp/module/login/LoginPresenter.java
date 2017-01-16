package com.ttsea.mvp.module.login;

import android.content.Context;

import com.ttsea.jlibrary.common.JLog;
import com.ttsea.jlibrary.utils.Utils;
import com.ttsea.mvp.R;
import com.ttsea.mvp.base.BasePresenterImpl;

import java.util.HashMap;
import java.util.Map;

import dagger.internal.Preconditions;

/**
 * // to do <br/>
 * <p>
 * <b>more:</b> 更多请参考<a href="http://www.ttsea.com" title="小周博客">www.ttsea.com</a> <br/>
 * <b>date:</b> 2016/12/15 10:39 <br/>
 * <b>author:</b> Jason <br/>
 * <b>version:</b> 1.0 <br/>
 * <b>last modified date:</b> 2016/12/15 10:39.
 */

public class LoginPresenter extends BasePresenterImpl implements Login.Presenter {
    private final String TAG = "LoginPresenter";

    private final int REQUEST_CODE_LOGIN = 0x001;

    private Login.View loginView;

    public LoginPresenter(Context context, Login.View view) {
        super(context);
        loginView = Preconditions.checkNotNull(view, "mainView could not null");
        loginView.setPresenter(this);

        initVariables();
    }

    @Override
    public void initVariables() {

    }

    @Override
    public String getRequestTag() {
        return TAG;
    }

    @Override
    public void login(String userName, String pwd) {
        if (Utils.isEmpty(userName)) {
            loginView.showToast("请输入用户名");
            loginView.focusView(R.id.etUserName);
            return;
        }
        if (Utils.isEmpty(pwd)) {
            loginView.showToast("请输入密码");
            loginView.focusView(R.id.etPwd);
            return;
        }

        JLog.d(TAG, "login, userName:" + userName + ", pwd:" + pwd);

        //loginView.showProgress(null, "正在登入...", true);
        //test login
        String loginUrl = "http://test.api.huiweishang.com/app/ver5.2.0/login.php";
        Map<String, String> loginParams = new HashMap<>();
        loginParams.put("login_name", userName);
        loginParams.put("login_pass", pwd);

        String indexUrl = "http://test.api.huiweishang.com/app/ver5.2.0/index.hws.php";

//        addRequest(indexUrl, Http.Method.POST, null, null, getRequestTag(), true, 10, 3);
        addRequest(loginUrl, loginParams, REQUEST_CODE_LOGIN);
    }

    @Override
    public synchronized void handleErrorResponse(String errorMsg, int requestCode) {
        super.handleErrorResponse(errorMsg, requestCode);
    }

    @Override
    public synchronized boolean handleNetWorkData(String jsonData, int requestCode) {
        if (super.handleNetWorkData(jsonData, requestCode)) {
            return true;
        }

        switch (requestCode) {
            case REQUEST_CODE_LOGIN:
                break;
        }

        return true;
    }
}
