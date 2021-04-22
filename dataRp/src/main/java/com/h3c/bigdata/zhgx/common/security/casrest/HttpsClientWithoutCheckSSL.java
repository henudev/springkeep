package com.h3c.bigdata.zhgx.common.security.casrest;


import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/*
 * @Description: https 不进行证书验证来发送模拟客户端请求
 * @author: y14079
 * @date: 2017-11-28
 *
 * @modified by:
 * @modified date:
 * @problem no:
 */
public class HttpsClientWithoutCheckSSL implements X509TrustManager {

	public static HttpURLConnection getHttpsURLConnection(String url, String reportContent, String method) {
		URL r_url = null;
        HttpURLConnection urlConn = null;
		try {
			r_url = new URL(url);
			urlConn = (HttpURLConnection) r_url.openConnection();
			urlConn.setDoOutput(true);
			urlConn.setDoInput(true);
			urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			urlConn.setRequestProperty("Charset", "utf-8");
			urlConn.setRequestMethod(method);
			OutputStream out = urlConn.getOutputStream();
			out.write(reportContent.getBytes("utf-8"));
			out.flush();

			return urlConn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String sendHttps(String url, String reportContent, String method) {
		URL r_url = null;
		HttpURLConnection urlConn = null;
		int status = 0;
		try {
			r_url = new URL(url);
			urlConn = (HttpURLConnection) r_url.openConnection();
			urlConn.setDoOutput(true);
			urlConn.setDoInput(true);
			urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			urlConn.setRequestProperty("Charset", "utf-8");
			urlConn.setRequestMethod(method);
			OutputStream out = urlConn.getOutputStream();
			out.write(reportContent.getBytes("utf-8"));
			out.flush();

			status = urlConn.getResponseCode();
			if (200 == status) {
				StringBuffer buffer = new StringBuffer();
				InputStream input = null;
				try {
					if (urlConn != null) {
						input = urlConn.getInputStream();
						byte[] bt = new byte[1024];
						int length = -1;
						while ((length = input.read(bt)) != -1) {
							buffer.append(new String(bt, 0, length, "utf-8"));
						}
					}
					String data = buffer.toString();
					return data;
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (input != null) {
						try {
							input.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
	}

	public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
	}

	public X509Certificate[] getAcceptedIssuers() {
		return null; // To change body of implemented methods use File | Settings | File Templates.
	}

}
