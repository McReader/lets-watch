package com.sickfuture.letswatch.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.util.Log;

import com.sickfuture.letswatch.app.ContextHolder;

public class HttpManager {

	private static final String LOG_TAG = "HttpManager";

	private static final String UTF_8 = "UTF_8";

	private HttpClient mClient;

	private static volatile HttpManager instance;

	private static final int SO_TIMEOUT = 20000;

	private ConnectivityManager mConnectivityManager;

	private HttpManager() {
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, UTF_8);
		params.setBooleanParameter("http.protocol.expect-continue", false);
		HttpConnectionParams.setConnectionTimeout(params, SO_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, SO_TIMEOUT);

		// REGISTERS SCHEMES FOR BOTH HTTP AND HTTPS
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		final SSLSocketFactory sslSocketFactory = SSLSocketFactory
				.getSocketFactory();
		sslSocketFactory
				.setHostnameVerifier(SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		registry.register(new Scheme("https", sslSocketFactory, 443));
		ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(
				params, registry);
		mClient = new DefaultHttpClient(manager, params);
		mConnectivityManager = (ConnectivityManager) ContextHolder
				.getInstance().getContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	public static HttpManager getInstance() {
		HttpManager localInstance = instance;
		if (instance == null) {
			synchronized (HttpManager.class) {
				localInstance = instance;
				if (localInstance == null) {
					instance = localInstance = new HttpManager();
				}
			}
		}
		return localInstance;
	}

	public Bitmap loadBitmap(String url) throws MalformedURLException,
			IOException {
		InputStream openStream = null;
		try {
			openStream = new URL(url).openStream();
			return BitmapFactory.decodeStream(openStream);
		} finally {
			openStream.close();
		}
	}

	public String postRequest(String url, ArrayList<BasicNameValuePair> params)
			throws ClientProtocolException, IOException, JSONException {
		HttpPost post = new HttpPost(url);
		UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
		post.setEntity(ent);
		HttpResponse response = mClient.execute(post);
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			String entityValue = EntityUtils.toString(response.getEntity());
			throw new IOException(response.getStatusLine().getReasonPhrase()
					+ " " + entityValue + " "
					+ response.getStatusLine().getStatusCode());
		}
		return response.toString();
	}

	public String loadAsString(String url) throws ClientProtocolException,
			IOException, JSONException {
		return loadAsString(new HttpGet(url));
	}

	public JSONArray loadAsJsonArray(String url)
			throws ClientProtocolException, JSONException, IOException {
		return new JSONArray(loadAsString(url));
	}

	public JSONObject loadAsJSONObject(String url)
			throws ClientProtocolException, JSONException, IOException {
		return new JSONObject(loadAsString(url));
	}

	private String loadAsString(HttpRequestBase request)
			throws ClientProtocolException, IOException {
		/*
		 * HttpResponse response = mClient.execute(request); if
		 * (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
		 * String entityValue = null; entityValue =
		 * EntityUtils.toString(response.getEntity()); throw new
		 * IOException(response.getStatusLine().getReasonPhrase() + " " +
		 * entityValue + " " + response.getStatusLine().getStatusCode()); }
		 */
		final InputStream is = loadInputStream(request);
		BufferedReader rd = null;
		try {
			rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			final String jsonText = readAll(rd);
			Log.d(LOG_TAG, "source = " + jsonText);
			return jsonText;
		} finally {
			rd.close();
			is.close();
		}
	}

	private InputStream loadInputStream(HttpRequestBase request)
			throws ParseException, IOException {
		HttpResponse response = mClient.execute(request);
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			String entityValue = null;
			entityValue = EntityUtils.toString(response.getEntity());
			throw new IOException(response.getStatusLine().getReasonPhrase()
					+ " " + entityValue + " "
					+ response.getStatusLine().getStatusCode());
		}
		final InputStream is = new URL(request.getURI().toString())
				.openStream();
		return is;
	}

	private static String readAll(final Reader rd) throws IOException {
		final StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	// if won't work, maybe because connectivity manager is static
	public boolean isAvalibleInetConnection() {
		return mConnectivityManager.getActiveNetworkInfo() != null;
	}

}
