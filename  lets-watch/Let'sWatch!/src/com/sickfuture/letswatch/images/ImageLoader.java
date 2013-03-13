package com.sickfuture.letswatch.images;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.sickfuture.letswatch.ContextHolder;
import com.sickfuture.letswatch.http.HttpManager;
import com.sickfuture.letswatch.task.CustomExecutorAsyncTask;
import com.sickfuture.letswatch.task.ParamCallback;
import com.sickfuture.letswatch.utils.Md5;

public class ImageLoader {

	private int mNumberOnExecute;

	private static final int CORE_POOL_SIZE = 1;

	private static final int MAXIMUM_POOL_SIZE = 50;

	private static final int KEEP_ALIVE = 10;

	private static final BlockingQueue<Runnable> sWorkQueue = new LinkedBlockingQueue<Runnable>(
			MAXIMUM_POOL_SIZE);

	private static final ThreadFactory sThreadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		public Thread newThread(Runnable r) {
			return new Thread(r, "ImageAsyncTask #" + mCount.getAndIncrement());
		}
	};

	private static final ThreadPoolExecutor mExecutor = new ThreadPoolExecutor(
			CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,
			sWorkQueue, sThreadFactory);

	private static final String LOG_TAG = "ImageLoader";

	private static volatile ImageLoader instance;

	public static ImageLoader getInstance() {
		ImageLoader localInstance = instance;
		if (instance == null) {
			synchronized (ImageLoader.class) {
				localInstance = instance;
				if (localInstance == null) {
					instance = localInstance = new ImageLoader();
				}
			}
		}
		return localInstance;
	}

	private List<Callback> mQueue;

	private LruCache<String, Bitmap> mStorage;

	private File mCacheDir;

	private final int memClass = ((ActivityManager) ContextHolder.getInstance()
			.getContext().getSystemService(Context.ACTIVITY_SERVICE))
			.getMemoryClass();

	private final int cacheSize = 1024 * 1024 * memClass / 4;

	private ImageLoader() {
		mQueue = Collections.synchronizedList(new ArrayList<Callback>());
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
		mCacheDir = ContextHolder.getInstance().getContext().getCacheDir();
		mNumberOnExecute = 0;
	}

	public void bind(final BaseAdapter adapter, final ImageView imageView,
			final String url) {
		imageView.setImageBitmap(null);
		Bitmap bitm = null;
		bitm = mStorage.get(url);
		if (bitm != null) {
			imageView.setImageBitmap(bitm);
			return;
		} else {
			if (mQueue.size() > 4) {
				mQueue.clear();
			}
			mQueue.add(0, new Callback() {

				public void onSuccess(Bitmap bm) {
					adapter.notifyDataSetChanged();
				}

				public void onError(Exception e) {
				}

				public String getUrl() {
					return url;
				}
			});
		}
		proceed();
	}

	public void bind(final ImageView imageView, final String url,
			final ParamCallback<Void> paramCallback) {
		// imageView.setImageBitmap(null);
		Bitmap bitm = null;
		bitm = mStorage.get(url);
		if (bitm != null) {
			imageView.setImageBitmap(bitm);
			paramCallback.onSuccess(null);
		} else {
			/*
			 * if (mQueue.size() > 10) { trimQueue(); }
			 */
			mQueue.clear();
			mQueue.add(0, new Callback() {

				public void onSuccess(Bitmap bm) {
					imageView.setImageBitmap(bm);
					paramCallback.onSuccess(null);
				}

				public void onError(Exception e) {
					paramCallback.onError(e);
				}

				public String getUrl() {
					return url;
				}
			});
		}
		proceed();
	}

	private void trimQueue() {
		while (mQueue.size() > 10) {
			mQueue.remove(mQueue.size() - 1);
		}
	}

	public void bind(final ImageView imageView, final String url) {
		imageView.setImageBitmap(null);
		Bitmap bitm = null;
		if (mStorage.get(url) != null) {
			bitm = mStorage.get(url);
		}
		if (bitm != null) {
			imageView.setImageBitmap(bitm);
		} else {
			if (mQueue.size() > 10) {
				trimQueue();
			}
			mQueue.add(0, new Callback() {

				public void onSuccess(Bitmap bm) {
					imageView.setImageBitmap(bm);
				}

				public void onError(Exception e) {
					imageView
							.setImageResource(android.R.drawable.alert_dark_frame);
				}

				public String getUrl() {
					return url;
				}
			});
		}
		proceed();
	}

	private void proceed() {
		Log.w("ImageLoader Queue size", "Queue Size = " + mQueue.size());
		if (mNumberOnExecute > 30) {
			if (mQueue.size() > 2)
				mQueue.remove(mQueue.size() - 1);
			return;
		}
		if (mQueue.isEmpty()) {
			return;
		}
		final Callback callback = mQueue.remove(0);
		new ImageTask().execute(mExecutor, callback);
	}

	private void putBitmapToCache(Bitmap b, String url) {
		File cacheFile = new File(mCacheDir, Md5.convert(url));
		try {
			mStorage.put(url, b);
			FileOutputStream fos = new FileOutputStream(cacheFile);
			b.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			Log.e(LOG_TAG, "Error when saving image to cache. ", e);
		}
	}

	private Bitmap getBitmapFromFileCache(String url) {
		if (TextUtils.isEmpty(url)) {
			return null;
		}
		File cacheFile = new File(mCacheDir, Md5.convert(url));
		FileInputStream fis = null;
		try {
			if (cacheFile.exists()) {
				fis = new FileInputStream(cacheFile);
				return BitmapFactory.decodeStream(fis);
			}
		} catch (FileNotFoundException e) {
			Log.e(LOG_TAG, "Error when saving image to cache. ", e);
			// ignored because not cached yet
			return null;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// ignored if already closed
				}
			}
		}
		return null;
	}

	private class ImageTask extends
			CustomExecutorAsyncTask<Callback, Void, Object> {

		private Callback mCallback;

		public ImageTask() {
		}

		@Override
		protected Object doInBackground(Callback... params) {
			if (TextUtils.isEmpty(params[0].getUrl())) {
				return new IllegalArgumentException();
			}
			mCallback = params[0];
			try {
				Bitmap bitmap = getBitmapFromFileCache(params[0].getUrl());
				if (bitmap != null) {
					if (mStorage.get(params[0].getUrl()) == null) {
						mStorage.put(params[0].getUrl(), bitmap);
					}
					return bitmap;
				}
				try {
					if (HttpManager.getInstance().isAvalibleInetConnection()) {
						bitmap = HttpManager.getInstance().loadBitmap(
								params[0].getUrl());
					}
					if (bitmap != null) {
						putBitmapToCache(bitmap, params[0].getUrl());
					}
					return bitmap;
				} catch (MalformedURLException e) {
					return e;
				}
			} catch (IOException e) {
				return e;
			}
		}

		@Override
		protected void onPreExecute() {
			mNumberOnExecute++;
		}

		@Override
		protected void onPostExecute(Object result) {
			if (result instanceof Bitmap) {
				mCallback.onSuccess((Bitmap) result);
			}
			if (result instanceof Exception) {
				mCallback.onError((Exception) result);
			}
			mNumberOnExecute--;
		}

	}
}