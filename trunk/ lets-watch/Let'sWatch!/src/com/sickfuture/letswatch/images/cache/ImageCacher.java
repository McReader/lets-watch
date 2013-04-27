package com.sickfuture.letswatch.images.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.sickfuture.letswatch.ContextHolder;
import com.sickfuture.letswatch.utils.Md5;

public class ImageCacher {

	protected static final String LOG_TAG = "ImageCacher";

	private static final CompressFormat DEFAULT_COMPRESS_FORMAT = CompressFormat.JPEG;

	private static final int DEFAULT_COMPRESS_QUALITY = 100;

	private static ImageCacher instance;

	private Context mContext = ContextHolder.getInstance().getContext();

	private LruCache<String, Bitmap> mStorage;

	private File mCacheDir;

	private final int memClass = ((ActivityManager) ContextHolder.getInstance()
			.getContext().getSystemService(Context.ACTIVITY_SERVICE))
			.getMemoryClass();

	private final int cacheSize = 1024 * 1024 * memClass / 4;

	public static ImageCacher getInstance() {
		if (instance == null) {
			instance = new ImageCacher();
		}
		return instance;
	}

	private ImageCacher() {
		init();
	}

	private void init() {
		mStorage = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}

			@Override
			protected void entryRemoved(boolean evicted, String key,
					Bitmap oldValue, Bitmap newValue) {
				super.entryRemoved(evicted, key, oldValue, newValue);
				if (evicted) {
					oldValue.recycle();
					Log.d(LOG_TAG, "recycle");
					System.gc();
					oldValue = null;
				}
			}
		};
		mCacheDir = mContext.getCacheDir();
	}

	public Bitmap getBitmapFromFileCache(String url) {
		String key = Md5.convert(url);
		Bitmap bitmap = null;
		FileInputStream fis = null;
		File cacheFile = null;
		try {
			cacheFile = new File(mCacheDir, key);
			if (cacheFile.exists()) {
				fis = new FileInputStream(cacheFile);
				bitmap = BitmapFactory.decodeFileDescriptor(fis.getFD());
				Log.d(LOG_TAG, "Disk cache hit");
			}
		} catch (FileNotFoundException e) {
			// Ignored, because already not cashed
		} catch (IOException e) {
			// TODO do smth
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// ignored if already closed
				}
			}
		}
		return bitmap;
	}

	public void putBitmapToCache(String url, Bitmap value,
			boolean cacheOnDiskMemory) {
		if (url == null || value == null) {
			return;
		}
		putBitmapToMemoryCache(url, value);
		if (!cacheOnDiskMemory) {
			return;
		}
		String key = Md5.convert(url);
		FileOutputStream fos = null;
		File cacheFile = null;
		try {
			cacheFile = new File(mCacheDir, key);
			fos = new FileOutputStream(cacheFile);
			value.compress(DEFAULT_COMPRESS_FORMAT, DEFAULT_COMPRESS_QUALITY,
					fos);
		} catch (IOException e) {
			Log.e(LOG_TAG, "putBitmapToCache - " + e);
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
			}
		}
	}

	public void putBitmapToMemoryCache(String key, Bitmap value) {
		if (mStorage != null) {
			mStorage.put(key, value);
		}
	}

	public Bitmap getBitmapFromMemoryCache(String key) {
		if (mStorage != null) {
			return mStorage.get(key);
		} else {
			throw new NullPointerException("LruCache object is null!!");
		}
	}
}
