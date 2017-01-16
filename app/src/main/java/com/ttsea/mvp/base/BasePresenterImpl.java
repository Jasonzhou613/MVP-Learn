package com.ttsea.mvp.base;

import android.content.Context;

import com.ttsea.jlibrary.common.JLog;

/**
 * // to do <br/>
 * <p>
 * <b>more:</b> 更多请参考<a href="http://www.ttsea.com" title="小周博客">www.ttsea.com</a> <br/>
 * <b>date:</b> 2016/12/15 10:45 <br/>
 * <b>author:</b> Jason <br/>
 * <b>version:</b> 1.0 <br/>
 * <b>last modified date:</b> 2016/12/15 10:45.
 */
public abstract class BasePresenterImpl extends BaseRequestWork implements BasePresenter {
    private final String TAG = "BasePresenterImpl";

    public BasePresenterImpl(Context context) {
        super(context);
    }

    @Override
    public void getData() {

    }

    @Override
    public void reloadData() {

    }

    @Override
    public void refreshData() {

    }

    @Override
    public void addData() {

    }

    public synchronized void handleErrorResponse(String errorMsg, int requestCode) {
        JLog.e(TAG, "handleErrorResponse, errorMsg:" + errorMsg);
    }

    @Override
    public boolean handleNetWorkData(String jsonData, int requestCode) {
        JLog.d(TAG, "jsonData:" + jsonData);
        if (mContext == null) {
            return false;
        }
        return false;
    }
}
