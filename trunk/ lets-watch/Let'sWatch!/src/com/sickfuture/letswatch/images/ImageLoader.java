package com.sickfuture.letswatch.images;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.sickfuture.letswatch.ContextHolder;
import com.sickfuture.letswatch.http.HttpManager;

public class ImageLoader {

	private static ImageLoader instance;

	public static ImageLoader getInstance() {
		if (instance == null) {
			instance = new ImageLoader();
		}
		return instance;
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
					Log.d("ray", "recycle");
					System.gc();
					oldValue = null;
				}
			}
		};
		mCacheDir = ContextHolder.getInstance().getContext().getCacheDir();
	}

	public String md5(String s) {
		try {
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}

	}

	public void bind(final BaseAdapter adapter, final ImageView imageView,
			final String url) {
		imageView.setImageBitmap(null);
		Bitmap bitm = null;
		bitm = mStorage.get(url);
		if (bitm != null) {
			imageView.setImageBitmap(bitm);
		} else {
			if (mQueue.size() > 10) {
				trimQueue();
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
		if (mQueue.isEmpty()) {
			return;
		}
		final Callback callback = mQueue.remove(0);
		new ImageTask().start(callback);
	}

	private void putBitmapToCache(Bitmap b, String url) {
		File cacheFile = new File(mCacheDir, md5(url));
		try {
			mStorage.put(url, b);
			FileOutputStream fos = new FileOutputStream(cacheFile);
			b.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			Log.e("ray", "Error when saving image to cache. ", e);
		}
	}

	private Bitmap getBitmapFromFileCache(String url) {
		if (TextUtils.isEmpty(url)) {
			return null;
		}
		File cacheFile = new File(mCacheDir, md5(url));
		FileInputStream fis = null;
		try {
			if (cacheFile.exists()) {
				fis = new FileInputStream(cacheFile);
				return BitmapFactory.decodeStream(fis);
			}
		} catch (FileNotFoundException e) {
			Log.e("ray", "Error when saving image to cache. ", e);
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

	private class ImageTask extends AsyncTask<Callback, Void, Object> {

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
		protected void onPostExecute(Object result) {
			if (result instanceof Bitmap) {
				mCallback.onSuccess((Bitmap) result);
			}
			if (result instanceof Exception) {
				mCallback.onError((Exception) result);
			}

		}

		public void start(Callback... params) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				executeOnExecutor(Executors.newCachedThreadPool(), params);
			} else {
				execute(params);
			}
		}
	}
}