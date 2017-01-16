package com.ttsea.mvp.module.login;


import com.ttsea.mvp.base.BasePresenter;
import com.ttsea.mvp.base.BaseView;

/**
 * // to do <br/>
 * <p>
 * <b>more:</b> 更多请参考<a href="http://www.ttsea.com" title="小周博客">www.ttsea.com</a> <br/>
 * <b>date:</b> 2016/12/15 10:37 <br/>
 * <b>author:</b> Jason <br/>
 * <b>version:</b> 1.0 <br/>
 * <b>last modified date:</b> 2016/12/15 10:37.
 */
public interface Login {

    interface View extends BaseView<BasePresenter> {

        void focusView(int viewId);

    }

    interface Presenter extends BasePresenter {

        void login(String userName, String pwd);
    }

}
