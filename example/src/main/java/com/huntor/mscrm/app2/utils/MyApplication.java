package com.huntor.mscrm.app2.utils;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DefaultConfigurationFactory;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.analytics.MobclickAgent;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MobclickAgent.openActivityDurationTrack(false);
        initImageLoader(this);
    }

    private static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .discCache(
                        DefaultConfigurationFactory.createDiscCache1(context,
                                Constant.IMAGE_CACHE_PATH, new Md5FileNameGenerator(),
                                Constant.IMAGE_CACHE_SIZE, 0)).memoryCache(new WeakMemoryCache())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }

}
