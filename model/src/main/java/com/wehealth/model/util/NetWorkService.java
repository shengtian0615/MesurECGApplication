package com.wehealth.model.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.wehealth.model.domain.model.AuthToken;
import com.wehealth.model.interfaces.inter_other.WeHealthToken;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

public class NetWorkService {

	public static String client_credentials = "client_credentials";
	public static String bear = "Bearer ";
//    public static <T> T createApi(Class<T> clazz) {
//        return createApi(clazz, CoreApp.getInstance().setBaseUrl());
//    }

//    public static <T> T createApi(Class<T> clazz, String url) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(url)
//                .client(okHttpClient)
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        return retrofit.create(clazz);
//    }

	public static AuthToken getAuthToken(AuthToken oldToken, String url) {
		if(oldToken == null) return null;
		String accessToken = oldToken.getAccess_token();
		if (accessToken == null){
			return null;
		}
		long expireTime = oldToken.getExpires_in();
		if (System.currentTimeMillis() - expireTime > 0){ // need get new token
			try {
				String refreshToken = oldToken.getRefresh_token();
				AuthToken token = createByteApi(WeHealthToken.class, url).refreshToken(bear + accessToken, "refresh_token", Constant.Doctor, refreshToken);
				return token;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}else {
			return oldToken;
		}
	}

	/**
	 * 检测网络是否可用
	 *
	 * @param context
	 * @return true为可以使用
	 */
	public static boolean isNetWorkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
    /**正常接口**/
    public static <T> T createApi(Class<T> clazz, String url){
    	return getRestAdapter(url).create(clazz);
    }
    
    /**超长时间接口**/
    public static <T> T createLongApi(Class<T> clazz, String url){
    	return getLongRestAdapter(url).create(clazz);
    }
    
    /**Byte的超长时间接口**/
    public static <T> T createByteLongApi(Class<T> clazz, String url){
    	return getLongByteRestAdapter(url).create(clazz);
    }
    
    /**Byte的接口**/
    public static <T> T createByteApi(Class<T> clazz, String url){
    	return getByteRestAdapter(url).create(clazz);
    }
    
    /**String的接口**/
    public static <T> T createStringApi(Class<T> clazz, String url){
    	return getStringRestAdapter(url).create(clazz);
    }
    
    /**获取RestAdapter**/
	public static RestAdapter getRestAdapter(String Url){
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint(Url)
				.setConverter(new GsonConverter(gson)).setClient(getSSLClient()).build();
		return restAdapter;
	}
	/**获取RestAdapter 30000毫秒**/
	public static RestAdapter getLongRestAdapter(String Url){
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint(Url)
				.setConverter(new GsonConverter(gson)).setClient(getSSL30000LClient()).build();
		return restAdapter;
	}
	/**获取RestAdapter Byte**/
	public static RestAdapter getByteRestAdapter(String Url){
		Gson gson = GsonHelper.customGson;
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint(Url)
				.setConverter(new GsonConverter(gson)).setClient(getSSLClient()).build();
		return restAdapter;
	}
	/**获取RestAdapter  Byte 30000毫秒**/
	public static RestAdapter getLongByteRestAdapter(String Url){
		Gson gson = GsonHelper.customGson;
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint(Url)
				.setConverter(new GsonConverter(gson)).setClient(getSSL30000LClient()).build();
		return restAdapter;
	}
	
	/**返回StringConverter**/
	public static RestAdapter getStringRestAdapter(String Url){
		RestAdapter restAdapter = new RestAdapter.Builder()
		.setEndpoint(Url)
		.setConverter(new StringConverter()).setClient(getSSLClient()).build();
		return restAdapter;
	}
	
//	public static AuthToken getAuthToken() {
//		AuthToken oldToken = DoctorClinicApplication.getInstance().getToken();
//		if(oldToken == null) return null;
//		String accessToken = oldToken.getAccess_token();
//		if (accessToken == null){
//			return null;
//		}
//		long expireTime = oldToken.getExpires_in();
//		if (System.currentTimeMillis() - expireTime > 0){ // need get new token
//			try {
//				String refreshToken = oldToken.getRefresh_token();
//				AuthToken token = createByteApi(WeHealthToken.class).refreshToken(bear + accessToken, "refresh_token", Constant.Doctor, refreshToken);
//				DoctorClinicApplication.getInstance().setToken(token);
//				return token;
//			} catch (Exception e) {
//				e.printStackTrace();
//				return null;
//			}
//		}else {
//			return oldToken;
//		}
//	}
	
	/**
	 * 验证是否过期
	 * @param token
	 * @return 过期返回true；否则返回false
	 */
	public static boolean isExpireToken(AuthToken token){
		boolean expire = true;
		if (token==null) {
			expire = true;
		}
		long expireTime = token.getExpires_in();
		if (System.currentTimeMillis() - expireTime > 0){//过期了
			expire = true;
		}else {
			expire = false;
		}
		return expire;
	}
    
    /** 超长请求  **/
	@SuppressLint("TrulyRandom")
	public static Client getSSL30000LClient() {
		OkHttpClient okHttp = new OkHttpClient();
		okHttp.setConnectTimeout(30000L, TimeUnit.MILLISECONDS);
		okHttp.setReadTimeout(30000L, TimeUnit.MILLISECONDS);
		OkClient okClient = new OkClient(okHttp);
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs,
					String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs,
				String authType) {
			}
		} };

		SSLContext context;
		try {
			context = SSLContext.getInstance("TLS");
			context.init(null, trustAllCerts, new SecureRandom());
			okHttp.setSslSocketFactory(context.getSocketFactory());
			HostnameVerifier hostVerifier = new HostnameVerifier() {
				public boolean verify(String s, SSLSession sslSession) {
					return true;
				}
			};
			okHttp.setHostnameVerifier(hostVerifier);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return okClient;
	}
	
	public static Client getSSLClient() {
		OkHttpClient okHttp = new OkHttpClient();
		okHttp.setConnectTimeout(5000L, TimeUnit.MILLISECONDS);
		okHttp.setReadTimeout(5000L, TimeUnit.MILLISECONDS);
		OkClient okClient = new OkClient(okHttp);
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs,
					String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs,
				String authType) {
			}
		} };

		SSLContext context;
		try {
			context = SSLContext.getInstance("TLS");
			context.init(null, trustAllCerts, new SecureRandom());
			okHttp.setSslSocketFactory(context.getSocketFactory());
			HostnameVerifier hostVerifier = new HostnameVerifier() {
				public boolean verify(String s, SSLSession sslSession) {
					return true;
				}
			};
			okHttp.setHostnameVerifier(hostVerifier);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return okClient;
	}
}

