package com.ttsea.mvp.base;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.squareup.leakcanary.LeakCanary;
import com.ttsea.jlibrary.base.JBaseApplication;
import com.ttsea.jlibrary.common.JLog;
import com.ttsea.jlibrary.utils.CacheDirUtils;
import com.ttsea.mvp.R;
import com.ttsea.mvp.common.MVPSingleton;
import com.ttsea.mvp.config.Config;

import java.io.File;

/**
 * // to do <br/>
 * <p>
 * <b>more:</b> 更多请参考<a href="http://www.ttsea.com" title="小周博客">www.ttsea.com</a> <br/>
 * <b>date:</b> 2016/12/5 11:34 <br/>
 * <b>author:</b> Jason <br/>
 * <b>version:</b> 1.0 <br/>
 * <b>last modified date:</b> 2016/12/5 11:35.
 */
public class MVPApplication extends JBaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        // 解决AsyncTask.onPostExecute不执行问题, start
        try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // 解决AsyncTask.onPostExecute不执行问题, end

        initGlobalConfig();
        initImageLoader(this);
    }

    private void initGlobalConfig() {
        if (Config.DEBUG) {
            JLog.enableLogging();
            MVPSingleton.setRefWatcher(LeakCanary.install(this));
        } else {
            JLog.disableLogging();
        }
    }

    private void initImageLoader(Context appContext) {
        File cacheDir = new File(CacheDirUtils.getImageCacheDir(appContext));

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，图片太多就这这个。还有其他设置
                // 如Bitmap.Config.ARGB_8888
                .showImageOnLoading(R.color.gainsboro) // 默认图片
                .showImageForEmptyUri(R.color.gainsboro) // url爲空會显示该图片，自己放在drawable里面的
                .showImageOnFail(R.color.gainsboro)// 加载失败显示的图片
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(480, 800)
                // 缓存在内存的图片的宽和高度
                //.discCacheExtraOptions(480, 800, Bitmap.CompressFormat.PNG, 70, null)
                // CompressFormat.PNG类型，70质量（0-100）
                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize(2 * 1024 * 1024) // 缓存到内存的最大数据
                .discCacheSize(50 * 1024 * 1024)// 缓存到文件的最大数据
                .discCacheFileCount(1000) // 文件数量
                .discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
                .defaultDisplayImageOptions(options)// 上面的options对象，一些属性配置
                .build();

        ImageLoader.getInstance().init(config); // 初始化
    }
}
