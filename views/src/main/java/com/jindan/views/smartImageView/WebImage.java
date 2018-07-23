package com.jindan.views.smartImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class WebImage implements SmartImage {
	private static final int CONNECT_TIMEOUT = 20000;
	private static final int READ_TIMEOUT = 20000;

	private static WebImageCache webImageCache;

	private String url;

	private Context context;

	public WebImage(Context context, String url) {
		this.url = url;
		this.context = context;
	}

	public Bitmap getBitmap(Context context) {
		// Don't leak context
		if (webImageCache == null) {
			webImageCache = new WebImageCache(context);
		}

		// Try getting bitmap from cache first
		Bitmap bitmap = null;
		if (url != null) {
			bitmap = webImageCache.get(url);
			// if(bitmap==null){
			// Log.i("WebImageCache","从手机里取NULL--"+url);
			// }else{
			// Log.i("WebImageCache","从手机里取出来了--"+url);
			// }
			if (bitmap == null) {
				// Log.i("WebImageCache","开始从网络下载--"+url);
				bitmap = getBitmapFromUrl(url);
				if (bitmap != null) {
					webImageCache.put(url, bitmap);
				}
			}
		}

		return bitmap;
	}

	private Bitmap getBitmapFromUrl(String url) {
		Bitmap bitmap = null;
		URLConnection conn = null;
		try {

			conn = new URL(url).openConnection();

			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);

			bitmap = BitmapFactory
					.decodeStream((InputStream) conn.getContent());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	public static void removeFromCache(String url) {
		if (webImageCache != null) {
			webImageCache.remove(url);
		}
	}
}