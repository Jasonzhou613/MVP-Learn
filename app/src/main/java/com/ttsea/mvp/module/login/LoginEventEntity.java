package com.ttsea.mvp.module.login;

import android.content.Intent;

import java.io.Serializable;

/**
 * // to do <br/>
 * <p>
 * <b>more:</b> 更多请参考<a href="http://www.ttsea.com" title="小周博客">www.ttsea.com</a> <br/>
 * <b>date:</b> 2017/1/16 15:25 <br/>
 * <b>author:</b> Jason <br/>
 * <b>version:</b> 1.0 <br/>
 * <b>last modified date:</b> 2017/1/16 15:25.
 */
public class LoginEventEntity implements Serializable {
    /** 登录成功 */
    public static final int ACTION_LOGIN_SUCCESS = 0;
    /** 登录失败 */
    public static final int ACTION_LOGIN_FAILED = -1;

    private int status = ACTION_LOGIN_FAILED;
    private Intent intent;
    private String msg = "";

    public LoginEventEntity(int status) {
        this(status, "");
    }

    public LoginEventEntity(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
