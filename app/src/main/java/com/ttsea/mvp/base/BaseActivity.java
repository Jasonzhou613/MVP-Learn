package com.ttsea.mvp.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ttsea.jlibrary.base.JBaseApplication;
import com.ttsea.jlibrary.common.JLog;
import com.ttsea.jlibrary.component.dialog.MyAlertDialog;
import com.ttsea.jlibrary.component.dialog.MyDialog;
import com.ttsea.jlibrary.component.dialog.MyProgressDialog;
import com.ttsea.jlibrary.debug.ViewServer;
import com.ttsea.jlibrary.interfaces.OnActivityLifeChangedListener;
import com.ttsea.jlibrary.interfaces.OnSingleClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * // to do <br/>
 * <p>
 * <b>more:</b> 更多请参考<a href="http://www.ttsea.com" title="小周博客">www.ttsea.com</a> <br/>
 * <b>date:</b> 2016/12/6 17:27 <br/>
 * <b>author:</b> Jason <br/>
 * <b>version:</b> 1.0 <br/>
 * <b>last modified date:</b> 2016/12/6 17:27.
 */

/**
 * Activity基类，这里只对UI进行处理 <br/>
 * <p>
 * <b>more:</b> 更多请参考<a href="http://www.ttsea.com" title="小周博客">www.ttsea.com</a> <br/>
 * <b>date:</b> 2016/4/11 20:13 <br/>
 * <b>author:</b> Jason <br/>
 * <b>version:</b> 1.0 <br/>
 * <b>last modified date:</b> 2016/4/11 20:13
 */
public class BaseActivity extends Activity implements BaseView<BasePresenter> {
    public Activity mActivity;
    public OnSingleClickListener mOnSingleClickListener;

    private BasePresenter mBasePresenter;
    private MyProgressDialog progressDialog;
    private MyDialog myDialog;
    private Toast mToast;
    private List<OnActivityLifeChangedListener> mOnActivityLifeChangedListenerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;

        JBaseApplication.addActivity(mActivity);
        //调试模式下，使其能够使用hierarchyview
        if (JLog.isDebugMode()) {
            ViewServer.get(mActivity).addWindow(this);
        }

        init();
    }

    private void init() {
        mOnActivityLifeChangedListenerList = new ArrayList<OnActivityLifeChangedListener>();
        myDialog = new MyDialog(mActivity, com.ttsea.jlibrary.R.style.my_dialog_theme, null, 120, 120);
        mOnSingleClickListener = new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                BaseActivity.this.onSingleClick(v);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        for (int i = 0; i < mOnActivityLifeChangedListenerList.size(); i++) {
            OnActivityLifeChangedListener l = mOnActivityLifeChangedListenerList.get(i);
            if (l != null) {
                l.onStart();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //调试模式下，使其能够使用hierarchyview
        if (JLog.isDebugMode()) {
            ViewServer.get(mActivity).setFocusedWindow(this);
        }

        for (int i = 0; i < mOnActivityLifeChangedListenerList.size(); i++) {
            OnActivityLifeChangedListener l = mOnActivityLifeChangedListenerList.get(i);
            if (l != null) {
                l.onResume();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        for (int i = 0; i < mOnActivityLifeChangedListenerList.size(); i++) {
            OnActivityLifeChangedListener l = mOnActivityLifeChangedListenerList.get(i);
            if (l != null) {
                l.onPause();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        for (int i = 0; i < mOnActivityLifeChangedListenerList.size(); i++) {
            OnActivityLifeChangedListener l = mOnActivityLifeChangedListenerList.get(i);
            if (l != null) {
                l.onStop();
            }
        }
    }

    @Override
    protected void onDestroy() {
        //调试模式下，使其能够使用hierarchyview
        if (JLog.isDebugMode()) {
            ViewServer.get(mActivity).removeWindow(this);
        }
        while (!mOnActivityLifeChangedListenerList.isEmpty()) {
            OnActivityLifeChangedListener l = mOnActivityLifeChangedListenerList.remove(0);
            if (l != null) {
                l.onDestroy();
            }
        }
        JBaseApplication.removeActivity(mActivity);
        if (mBasePresenter != null && mBasePresenter instanceof BaseRequestWork) {
            BaseRequestWork baseRequestWork = (BaseRequestWork) mBasePresenter;
            baseRequestWork.cancelRequest(baseRequestWork.getRequestTag());
        }
        super.onDestroy();
    }

    @Override
    public void setPresenter(BasePresenter presenter) {
        mBasePresenter = presenter;
    }

    @Override
    public void dismissAllDialog() {
        dismissDialog();
        dismissProgress();
    }

    /** 显示加载框 */
    @Override
    public void showProgress(String title, String message, boolean canCancel) {
        if (mActivity != null && !mActivity.isFinishing() && !isProgressShowing()) {
            progressDialog = MyProgressDialog.show(mActivity, title, message,
                    canCancel);
            progressDialog.setCanceledOnTouchOutside(canCancel);
        }
    }

    /** 关闭加载框 */
    public void dismissProgress() {
        if (mActivity != null && !mActivity.isFinishing()
                && progressDialog != null && isProgressShowing()) {
            progressDialog.dismiss();
        }
    }

    /** 加载框进行中 */
    public boolean isProgressShowing() {
        if (progressDialog != null) {
            return progressDialog.isShowing();
        }
        return false;
    }

    /** 显示dialog */
    @Override
    public void showDialog(String msg, boolean canceledOnTouchOutside) {
        showDialog(msg, canceledOnTouchOutside, true);
    }

    /** 显示dialog */
    @Override
    public void showDialog(String msg, boolean canceledOnTouchOutside, boolean cancelable) {
        showDialog(msg, null, canceledOnTouchOutside, cancelable);
    }

    /** 显示dialog */
    @Override
    public void showDialog(String msg, DialogInterface.OnDismissListener listener) {
        showDialog(msg, listener, false, true);
    }

    /** 显示dialog */
    @Override
    public void showDialog(String msg, DialogInterface.OnDismissListener listener, boolean canceledOnTouchOutside, boolean cancelable) {
        if (myDialog == null || myDialog.isShowing()) {
            return;
        }
        myDialog.setOnDismissListener(listener);
        myDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        myDialog.setCancelable(cancelable);
        myDialog.show(msg);
    }

    /** 关闭dialog */
    public void dismissDialog() {
        if (myDialog != null && myDialog.isShowing()
                && !mActivity.isFinishing()) {
            myDialog.dismiss();
        }
    }

    /** 弹出MyAlertDialog */
    @Override
    public void showAlertDialog(String title, String msg,
                                String positiveTxt, DialogInterface.OnClickListener positiveListener,
                                boolean canceledOnTouchOutside,
                                boolean cancelable) {
        MyAlertDialog dialog = createAlertDialog(title, msg,
                positiveTxt, positiveListener,
                null, null,
                canceledOnTouchOutside,
                cancelable);
        dialog.show();
    }

    /** 弹出MyAlertDialog */
    @Override
    public void showAlertDialog(String title, String msg,
                                String positiveTxt, DialogInterface.OnClickListener positiveListener,
                                String negativeTxt, DialogInterface.OnClickListener negativeListener,
                                boolean canceledOnTouchOutside,
                                boolean cancelable) {
        MyAlertDialog dialog = createAlertDialog(title, msg,
                positiveTxt, positiveListener,
                negativeTxt, negativeListener,
                canceledOnTouchOutside,
                cancelable);
        dialog.show();
    }

    /** 创建MyAlertDialog */
    @Override
    public MyAlertDialog createAlertDialog(String title, String msg,
                                           String positiveTxt, DialogInterface.OnClickListener positiveListener,
                                           String negativeTxt, DialogInterface.OnClickListener negativeListener,
                                           boolean canceledOnTouchOutside,
                                           boolean cancelable) {
        MyAlertDialog.Builder builder = createAlertDialogBuilder(title, msg,
                positiveTxt, positiveListener,
                negativeTxt, negativeListener,
                canceledOnTouchOutside,
                cancelable);
        return builder.create();
    }

    /** 创建MyAlertDialog.Builder实例 */
    @Override
    public MyAlertDialog.Builder createAlertDialogBuilder(String title, String msg,
                                                          String positiveTxt, DialogInterface.OnClickListener positiveListener,
                                                          String negativeTxt, DialogInterface.OnClickListener negativeListener,
                                                          boolean canceledOnTouchOutside,
                                                          boolean cancelable) {
        MyAlertDialog.Builder builder = new MyAlertDialog.Builder(mActivity);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveTxt, positiveListener);
        builder.setNegativeButton(negativeTxt, negativeListener);
        builder.setCanceledOnTouchOutside(canceledOnTouchOutside);
        builder.setCancelable(cancelable);

        return builder;
    }

    /**
     * 通过字串ID获取到字串
     *
     * @param resId string id
     * @return String
     * @author Jason
     */
    @Override
    public String getStringById(int resId) {
        return mActivity.getResources().getString(resId);
    }

    /**
     * 通过id获取颜色值
     *
     * @param resId color id
     * @return int
     */
    @Override
    public int getColorById(int resId) {
        return mActivity.getResources().getColor(resId);
    }

    /** toast 消息 */
    @Override
    public void toastMessage(String msg) {
        if (msg != null) {
            Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
        }
    }

    /** toast 消息 */
    @Override
    public void toastMessage(int resId) {
        toastMessage(getStringById(resId));
    }

    /** 单例toast，toast不会重复 */
    @Override
    public void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(mActivity, text, Toast.LENGTH_SHORT);
        }
        mToast.setText(text);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    /** 单例toast，toast不会重复 */
    @Override
    public void showToast(int resId) {
        showToast(getStringById(resId));
    }

    /** finish该acitivity，并且设置resultCode */
    @Override
    public void finish(int resultCode) {
        mActivity.setResult(resultCode);
        mActivity.finish();
    }

    /** finish该acitivity，并且设置resultCode和Intent */
    @Override
    public void finish(int resultCode, Intent data) {
        mActivity.setResult(resultCode, data);
        mActivity.finish();
    }

    protected void onSingleClick(View v) {

    }

    /** 添加Activity生命周期监听器 */
    public void addActivityLifeCycleListener(OnActivityLifeChangedListener l) {
        if (!mOnActivityLifeChangedListenerList.contains(l)) {
            mOnActivityLifeChangedListenerList.add(l);
        }
    }

    /** 移除指定的Activity生命周期监听器 */
    public void removeActivityLifeCycleListener(OnActivityLifeChangedListener l) {
        mOnActivityLifeChangedListenerList.remove(l);
    }

    /** 显示正常的View */
    @Override
    public void showNormalView() {
    }

    /** 显示无数据的View */
    @Override
    public void showNoDataView() {
    }

    /** 显示正在加载的View */
    @Override
    public void showLoadingView() {
    }

    /** 显示数据异常的View */
    @Override
    public void showErrorView() {
    }
}
