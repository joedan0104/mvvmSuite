package com.jindan.views.X5webView;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.jindan.tools.utils.AppUtils;
import com.jindan.tools.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Cookie处理工具类
 * 说明: 客户端和服务器信息通过Cookie传递，服务器端不缓存客户端用户信息，
 * 需要通过客户端传送，因此客户端访问每个网页必须发送Cookie信息。
 * 各种状态下Cookie设置
 * 1)用户未登录，Cookie发送版本号、用户身份(可选)
 * 2)用户登录成功，Cookie发送版本号、Token、用户身份(可选)
 * 3)用户退出登录，Cookie发送版本号、用户身份(可选)
 * fix: 网页间Cookie出现不同步问题。采用每个页面管理自己的Cookie
 *
 */
public class CookieHelper {
	/** 上下文 */
	private Context context;
	/**
     * 用户Token键值
	 */
	private String keyToken = "";
	/**
	 * Domain域
	 */
	private String domain = "";
	/**
     * 主域名地址
	 */
	private List<String> urls;
	/** Cookie是否初始化 */
	private boolean isInit;

	public CookieHelper(Context context, String keyToken, String domain, String mainUrl) {
		this.context = context;
		this.keyToken = StringUtils.stringNoNil(keyToken);
		this.domain = StringUtils.stringNoNil(domain);
		this.urls = new ArrayList<>();
		this.urls.add(mainUrl);
	}

	/** 
	 * 初始化Cookie
	 * 
	 */
	public void initCookie() {
		if(!isInit) {
			//腾讯X5WebView
			initX5WebViewCookie();
			//系统默认Cookie
			initWebkitCookie();
			isInit = true;
		}
	}

	private void initWebkitCookie() {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager2 = CookieManager.getInstance();
		cookieManager2.setAcceptCookie(true);
	}

	private void initX5WebViewCookie() {
		com.tencent.smtt.sdk.CookieSyncManager.createInstance(context);
		com.tencent.smtt.sdk.CookieManager cookieManager = com.tencent.smtt.sdk.CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
	}

	/**
	 * 更新Cookie
	 *
	 */
	public void updateCookie(String token) {
		//更新X5WebView Cookie
		updateX5WebViewCookie(token);
		//更新Webkit Cookie
		updateWebkitCookie(token);
	}

	/**
	 * 更新Webkit Cookie
	 */
	public void updateWebkitCookie(String token) {
		StringBuffer token_cookie = new StringBuffer();
		StringBuffer version_cookie = new StringBuffer();
		StringBuffer from_cookie = new StringBuffer();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			CookieManager.setAcceptFileSchemeCookies(true);
		}
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		//允许设置和接收Cookie
		cookieManager.setAcceptCookie(true);
		//移除SessionCookie
		cookieManager.removeSessionCookie();
		//清理原有Cookie防止出现重名Cookie
		cookieManager.removeAllCookie();
		//token参数
		if (TextUtils.isEmpty(token)) {
			//未登录重置Cookie
			token_cookie.append(keyToken + "=");
			token_cookie.append(";domain=" + domain);
			token_cookie.append(";path=/");
		} else {
			//设置Cookie内容
			token_cookie.append(keyToken + "=" + token);
			token_cookie.append(";domain=" + domain);
			token_cookie.append(";path=/");
		}
		//版本号
		version_cookie.append("app_version=" + AppUtils.getAppVersionName());
		version_cookie.append(";domain=" + domain);
		version_cookie.append(";path=/");
		for(String url : urls) {
			cookieManager.setCookie(url, token_cookie.toString());
			cookieManager.setCookie(url, version_cookie.toString());
			cookieManager.setCookie(url, from_cookie.toString());
		}
		//WebView已经可以在需要的时候自动同步cookie了，
		//所以不再需要创建CookieSyncManager类的对象来进行强制性的同步cookie了
		CookieSyncManager.getInstance().sync();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			//fix: 即时刷新防止100ms延时同步
			cookieManager.flush();
		}
	}

	/**
	 * 更新X5WebView Cookie
	 */
	public void updateX5WebViewCookie(String token) {
		StringBuffer token_cookie = new StringBuffer();
		StringBuffer version_cookie = new StringBuffer();
		StringBuffer from_cookie = new StringBuffer();
		com.tencent.smtt.sdk.CookieSyncManager.createInstance(context);
		com.tencent.smtt.sdk.CookieManager cookieManager = com.tencent.smtt.sdk.CookieManager.getInstance();
		//允许设置和接收Cookie
		cookieManager.setAcceptCookie(true);
		//token参数
		if (TextUtils.isEmpty(token)) {
			//清理原有Cookie防止出现重名Cookie
			cookieManager.removeAllCookie();
			//未登录重置Cookie
			token_cookie.append(keyToken + "=");
			token_cookie.append(";domain=" + domain);
			token_cookie.append(";path=/");
		} else {
			//设置Cookie内容
			token_cookie.append(keyToken + "=" + token);
			token_cookie.append(";domain=" + domain);
			token_cookie.append(";path=/");
		}
		//版本号
		version_cookie.append("app_version=" + AppUtils.getAppVersionName());
		version_cookie.append(";domain=" + domain);
		version_cookie.append(";path=/");
		for(String url : urls) {
			cookieManager.setCookie(url, token_cookie.toString());
			cookieManager.setCookie(url, version_cookie.toString());
			cookieManager.setCookie(url, from_cookie.toString());
		}
		com.tencent.smtt.sdk.CookieSyncManager.createInstance(context);
		com.tencent.smtt.sdk.CookieSyncManager.getInstance().sync();
		//强制刷新
		cookieManager.flush();
	}

	/**
	 * 同步Cookie
	 * 
	 */
	public void syncCookie() {
		syncX5WebViewCookie();
		syncWebkitCookie();
	}

	/**
	 * Webkit Cookie同步
	 */
	private void syncWebkitCookie() {
		try {
			CookieSyncManager.getInstance().startSync();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * X5WebView Cookie同步
	 */
	private void syncX5WebViewCookie() {
		try {
			com.tencent.smtt.sdk.CookieSyncManager.createInstance(context);
			com.tencent.smtt.sdk.CookieSyncManager.getInstance().startSync();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 同步Cookie
	 * 
	 */
	public void stopSyncCookie() {
		stopX5WebViewSyncCookie();
		stopWebkitSyncCookie();
	}

	private void stopWebkitSyncCookie() {
		try {
			CookieSyncManager.getInstance().stopSync();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void stopX5WebViewSyncCookie() {
		try {
			com.tencent.smtt.sdk.CookieSyncManager.createInstance(context);
			com.tencent.smtt.sdk.CookieSyncManager.getInstance().stopSync();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
