package com.ttsea.mvp.base;

import android.content.DialogInterface;
import android.content.Intent;

import com.ttsea.jlibrary.component.dialog.MyAlertDialog;

/**
 * // to do <br/>
 * <p>
 * <b>more:</b> 更多请参考<a href="http://www.ttsea.com" title="小周博客">www.ttsea.com</a> <br/>
 * <b>date:</b> 2016/12/14 15:53 <br/>
 * <b>author:</b> Jason <br/>
 * <b>version:</b> 1.0 <br/>
 * <b>last modified date:</b> 2016/12/14 15:53.
 */
public interface BaseView<T> {

    void setPresenter(T presenter);

    /** dismiss所有的dialog */
    void dismissAllDialog();

    /** 显示加载框 */
    void showProgress(String title, String message, boolean canCancel);

    /** 显示dialog */
    void showDialog(String msg, boolean canceledOnTouchOutside);

    /** 显示dialog */
    void showDialog(String msg, boolean canceledOnTouchOutside, boolean cancelable);

    /** 显示dialog */
    void showDialog(String msg, DialogInterface.OnDismissListener listener, boolean canceledOnTouchOutside, boolean cancelable);


    /** 弹出MyAlertDialog */
    void showAlertDialog(String title, String msg,
                         String positiveTxt, DialogInterface.OnClickListener positiveListener,
                         boolean canceledOnTouchOutside,
                         boolean cancelable);

    /** 弹出MyAlertDialog */
    void showAlertDialog(String title, String msg,
                         String positiveTxt, DialogInterface.OnClickListener positiveListener,
                         String negativeTxt, DialogInterface.OnClickListener negativeListener,
                         boolean canceledOnTouchOutside,
                         boolean cancelable);

    /** 创建MyAlertDialog */
    MyAlertDialog createAlertDialog(String title, String msg,
                                    String positiveTxt, DialogInterface.OnClickListener positiveListener,
                                    String negativeTxt, DialogInterface.OnClickListener negativeListener,
                                    boolean canceledOnTouchOutside,
                                    boolean cancelable);

    /** 创建MyAlertDialog.Builder实例 */
    MyAlertDialog.Builder createAlertDialogBuilder(String title, String msg,
                                                   String positiveTxt, DialogInterface.OnClickListener positiveListener,
                                                   String negativeTxt, DialogInterface.OnClickListener negativeListener,
                                                   boolean canceledOnTouchOutside,
                                                   boolean cancelable);

    /**
     * 通过字串ID获取到字串
     *
     * @param resId string id
     * @return String
     */
    String getStringById(int resId);

    /**
     * 通过id获取颜色值
     *
     * @param resId color id
     * @return int
     */
    int getColorById(int resId);

    /** toast 消息 */
    void toastMessage(String msg);

    /** toast 消息 */
    void toastMessage(int resId);

    /** 单例toast，toast不会重复 */
    void showToast(String text);

    /** 单例toast，toast不会重复 */
    void showToast(int resId);

    /** finish该acitivity，并且设置resultCode */
    void finish(int resultCode);

    /** finish该acitivity，并且设置resultCode和Intent */
    void finish(int resultCode, Intent data);

    /** 显示正常的View */
    void showNormalView();

    /** 显示无数据的View */
    void showNoDataView();

    /** 显示正在加载的View */
    void showLoadingView();

    /** 显示数据异常的View */
    void showErrorView();
}
