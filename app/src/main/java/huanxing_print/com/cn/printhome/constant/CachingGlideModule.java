package huanxing_print.com.cn.printhome.constant;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;

import android.content.Context;

public class CachingGlideModule implements GlideModule {

	@Override
	public void applyOptions(Context context, GlideBuilder builder) {
		String diskCacheFolder = Config.IMG_CACHE_PATH;
		int diskCacheSize = Config.IMG_CACHE_SIZE;
		DiskLruCacheFactory diskLruCacheFactory = new DiskLruCacheFactory(diskCacheFolder, diskCacheSize);
		builder.setDiskCache(diskLruCacheFactory);

	}

	@Override
	public void registerComponents(Context context, Glide glide) {

	}

}
