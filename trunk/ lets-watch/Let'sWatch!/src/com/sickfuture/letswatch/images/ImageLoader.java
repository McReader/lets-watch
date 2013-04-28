package com.sickfuture.letswatch.images;

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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.sickfuture.letswatch.ContextHolder;
import com.sickfuture.letswatch.http.HttpManager;
import com.sickfuture.letswatch.images.cache.ImageCacher;
import com.sickfuture.letswatch.task.CustomExecutorAsyncTask;

public class ImageLoader {

	private static final String LOG_TAG = "ImageLoader";

	private static final int FADE_IN_TIME = 600;

	private static ImageLoader instance;

	private Resources mResources;

	private ImageCacher mImageCacher;

	private Context mContext = ContextHolder.getInstance().getContext();

	private final Object mPauseWorkLock = new Object();
	private boolean mPauseWork = false;

	private List<Callback> mQueue;

	protected int mNumberOnExecute;

	private boolean mFadeInBitmap = true;

	private Bitmap mloadingBitmap = null;

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

	private ImageLoader() {
		mQueue = Collections.synchronizedList(new ArrayList<Callback>());
		mImageCacher = ImageCacher.getInstance();
		mResources = mContext.getResources();
	}

	public void bind(final BaseAdapter adapter, final ImageView imageView,
			final String url, boolean cacheOnDiskMemory) {
		Bitmap bitm = null;
		bitm = mImageCacher.getBitmapFromMemoryCache(url);
		if (bitm != null) {
			setImageDrawable(imageView, bitm);
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
		proceed(imageView, cacheOnDiskMemory);
	}

	// public void bind(final ImageView imageView, final String url,
	// final ParamCallback<Void> paramCallback) {
	// Bitmap bitm = null;
	// bitm = mImageCacher.getBitmapFromMemoryCache(url);
	// if (bitm != null) {
	// setImageDrawable(imageView, bitm);
	// paramCallback.onSuccess(null);
	// } else {
	// mQueue.clear();
	// mQueue.add(0, new Callback() {
	//
	// public void onSuccess(Bitmap bm) {
	// setImageDrawable(imageView, bm);
	// paramCallback.onSuccess(null);
	// }
	//
	// public void onError(Exception e) {
	// paramCallback.onError(e);
	// }
	//
	// public String getUrl() {
	// return url;
	// }
	// });
	// }
	// // TODO do param for cache
	// proceed(imageView, false);
	// }

	public void bind(final ImageView imageView, final String url,
			boolean cacheOnDiskMemory) {
		imageView.setImageBitmap(null);
		Bitmap bitm = null;
		bitm = mImageCacher.getBitmapFromMemoryCache(url);
		if (bitm != null) {
			setImageDrawable(imageView, bitm);
		} else {
			mQueue.add(0, new Callback() {

				public void onSuccess(Bitmap bm) {
					Log.d(LOG_TAG, "ONSUCCESS");
					imageView.setImageBitmap(bm);
				}

				public void onError(Exception e) {
					Log.d(LOG_TAG, "ONERRor");
					imageView
							.setImageResource(android.R.drawable.btn_star_big_on);
				}

				public String getUrl() {
					return url;
				}
			});
		}
		proceed(imageView, cacheOnDiskMemory);
	}

	private void proceed(ImageView imageView, boolean cacheOnDiskMemory) {
		if (mNumberOnExecute > 5) {
			if (mQueue.size() > 2)
				mQueue.remove(mQueue.size() - 1);
			return;
		}
		if (mQueue.isEmpty()) {
			return;
		}
		imageView.setImageBitmap(mloadingBitmap);
		final Callback callback = mQueue.remove(0);
		new ImageLoaderTask(callback, imageView, cacheOnDiskMemory).start();
	}

	public void setLoadingBitmap(Bitmap loadingBitmap) {
		mloadingBitmap = loadingBitmap;
		Drawable drawable = null;
	}

	/**
	 * Pause any ongoing background work. This can be used as a temporary
	 * measure to improve performance. For example background work could be
	 * paused when a ListView or GridView is being scrolled using a
	 * {@link android.widget.AbsListView.OnScrollListener} to keep scrolling
	 * smooth.
	 * <p>
	 * If work is paused, be sure setPauseWork(false) is called again before
	 * your fragment or activity is destroyed (for example during
	 * {@link android.app.Activity#onPause()}), or there is a risk the
	 * background thread will never finish.
	 */
	public void setPauseWork(boolean pauseWork) {
		synchronized (mPauseWorkLock) {
			mQueue.clear();
			mPauseWork = pauseWork;
			if (!mPauseWork) {
				mPauseWorkLock.notifyAll();
			}
		}
	}

	private void setImageDrawable(final ImageView imageView, final Bitmap bitmap) {
		BitmapDrawable drawable = new BitmapDrawable(mResources, bitmap);
		if (mFadeInBitmap && imageView.getDrawable() == null) {
			imageView.setImageDrawable(null);
			final TransitionDrawable transitionDrawable = new TransitionDrawable(
					new Drawable[] {
							new ColorDrawable(android.R.color.transparent),
							drawable });
			imageView.setImageDrawable(transitionDrawable);
			transitionDrawable.startTransition(FADE_IN_TIME);
		} else {
			imageView.setImageDrawable(null);
			imageView.setImageDrawable(drawable);
		}
	}

	public class ImageLoaderTask extends
			CustomExecutorAsyncTask<Callback, Void, Object> {
		private static final int CORE_POOL_SIZE = 3;

		private static final int MAXIMUM_POOL_SIZE = 15;

		private static final int KEEP_ALIVE = 5;

		private final BlockingQueue<Runnable> sWorkQueue = new LinkedBlockingQueue<Runnable>(
				MAXIMUM_POOL_SIZE);

		private final ThreadFactory sThreadFactory = new ThreadFactory() {
			private final AtomicInteger mCount = new AtomicInteger(1);

			public Thread newThread(Runnable r) {
				return new Thread(r, "ImageAsyncTask #"
						+ mCount.getAndIncrement());
			}
		};

		private final ThreadPoolExecutor mExecutor = new ThreadPoolExecutor(
				CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
				TimeUnit.SECONDS, sWorkQueue, sThreadFactory);

		private Callback mCallback;

		private final Object mPauseWorkLock = new Object();
		private boolean mPauseWork = false;

		private ImageCacher mImageCacher = ImageCacher.getInstance();

		private ImageView mImageView;

		private boolean mCacheOnDiskMemory;

		public ImageLoaderTask(Callback callback, ImageView imageView,
				boolean doCache) {
			mCallback = callback;
			mImageView = imageView;
			mCacheOnDiskMemory = doCache;
		}

		public void start() {
			execute(mExecutor, mCallback);
		}

		@Override
		protected Object doInBackground(Callback... params) {
			if (TextUtils.isEmpty(params[0].getUrl())) {
				return new IllegalArgumentException();
			}
			mCallback = params[0];
			synchronized (mPauseWorkLock) {
				while (mPauseWork && !isCancelled()) {
					try {
						mPauseWorkLock.wait();
					} catch (InterruptedException e) {
						return e;
					}
				}
			}
			Bitmap bitmap = null;
			try {
				bitmap = mImageCacher
						.getBitmapFromFileCache(params[0].getUrl());
				if (bitmap != null) {
					mImageCacher.putBitmapToMemoryCache(params[0].getUrl(),
							bitmap);
				}
				if (bitmap != null) {
					return bitmap;
				}
				try {
					if (HttpManager.getInstance().isAvalibleInetConnection()) {
						bitmap = HttpManager.getInstance().loadBitmap(
								params[0].getUrl(), 500, 500);
					}
					if (bitmap != null) {
						mImageCacher.putBitmapToCache(params[0].getUrl(),
								bitmap, mCacheOnDiskMemory);
					}
				} catch (MalformedURLException e) {
					return e;
				}
			} catch (IOException e) {
				return e;
			}
			return bitmap;
		}

		@Override
		protected void onPreExecute() {
			mNumberOnExecute++;
			mImageView.setImageDrawable(null);
		}

		@Override
		protected void onPostExecute(Object result) {
			if (result instanceof Bitmap) {
				// setImageDrawable(mImageView, (Bitmap) result);
				mCallback.onSuccess((Bitmap) result);
			}
			if (result instanceof Exception) {
				mCallback.onError((Exception) result);
			}
			mNumberOnExecute--;
		}
	}

}