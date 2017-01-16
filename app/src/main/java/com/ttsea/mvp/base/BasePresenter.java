package com.ttsea.mvp.base;

/**
 * // to do <br/>
 * <p>
 * <b>more:</b> 更多请参考<a href="http://www.ttsea.com" title="小周博客">www.ttsea.com</a> <br/>
 * <b>date:</b> 2016/12/6 17:25 <br/>
 * <b>author:</b> Jason <br/>
 * <b>version:</b> 1.0 <br/>
 * <b>last modified date:</b> 2016/12/6 17:25.
 */
public interface BasePresenter {

    /** 初始化变量，该方法不在{@link BasePresenterImpl}中实现 */
    void initVariables();

    /** 获取数据 */
    void getData();

    /** 重载数据 */
    void reloadData();

    /** 刷新数据 */
    void refreshData();

    /** 添加数据 */
    void addData();
}
