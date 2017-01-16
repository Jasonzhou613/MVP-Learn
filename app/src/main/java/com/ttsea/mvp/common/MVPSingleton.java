package com.ttsea.mvp.common;

import com.squareup.leakcanary.RefWatcher;

/**
 * 所有的单例都放在该文件中 <br/>
 * <p>
 * <b>more:</b> 更多请参考<a href="http://www.ttsea.com" title="小周博客">www.ttsea.com</a> <br/>
 * <b>date:</b> 2016/12/6 9:52 <br/>
 * <b>author:</b> Jason <br/>
 * <b>version:</b> 1.0 <br/>
 * <b>last modified date:</b> 2016/12/6 9:52.
 */
public class MVPSingleton {
    private static final String TAG = "MVPSingleton";

    /** 内存检测工具 */
    private static RefWatcher mRefWatcher;


    /** 获取内存检测工具，有可能返回null */
    public static RefWatcher getRefWatcher() {
        return mRefWatcher;
    }

    /** 需要在Application onCreate中赋值 */
    public static void setRefWatcher(RefWatcher refWatcher) {
        MVPSingleton.mRefWatcher = refWatcher;
    }

}
