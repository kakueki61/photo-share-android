package com.project.photoshare.api;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.HurlStack;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * <p>Customised HurlStack class for POST Multipart contents. For other types of Request, this works as like HurlStack.</p>
 * Created by TakuyaKodama on 14/06/08.
 */
public class MultipartHurlStack extends HurlStack {
    private static final String TAG = MultipartHurlStack.class.getSimpleName();

    public MultipartHurlStack() {
        this(null, null);
    }

    public MultipartHurlStack(UrlRewriter urlRewriter, javax.net.ssl.SSLSocketFactory sslSocketFactory) {
        super(urlRewriter, sslSocketFactory);
    }

    @Override
    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders)
            throws IOException, AuthFailureError {
        if (!(request instanceof MultipartJsonRequest)) {
            return super.performRequest(request, additionalHeaders);
        }

        HttpPost httpPost = new HttpPost(request.getUrl());
        httpPost.addHeader("Content-Type", request.getBodyContentType());
        httpPost.setEntity(((MultipartJsonRequest) request).getEntity());
        addHeaders(httpPost, additionalHeaders);
        addHeaders(httpPost, request.getHeaders());

        logHeaders(httpPost);
        logEntity(httpPost);

        HttpParams httpParams = httpPost.getParams();
        int timeoutMs = request.getTimeoutMs();
        HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
        HttpConnectionParams.setSoTimeout(httpParams, timeoutMs);       // so far timeoutMs is zero. i.e. infinite timeout.

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", new PlainSocketFactory(), 80));
        registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

        ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(httpParams, registry);
        HttpClient httpClient = new DefaultHttpClient(manager, httpParams);

        return httpClient.execute(httpPost);
    }

    private void setMultipartBody(HttpEntityEnclosingRequestBase httpRequest, Request<?> request) {
        if (request instanceof MultipartJsonRequest) {
        }
    }

    private void addHeaders(HttpPost httpPost, Map<String, String> headers) {
        for (String key : headers.keySet()) {
            httpPost.setHeader(key, headers.get(key));
        }
    }

    private void logHeaders(HttpPost httpPost) {
        Header[] headers = httpPost.getAllHeaders();

        Log.i(TAG, "************************");
        for (int i = 0; i < headers.length; i++) {
            Log.i(TAG, headers[i].toString());
        }
        Log.i(TAG, "************************");
    }

    private void logEntity(HttpPost httpPost) {
        HttpEntity entity = httpPost.getEntity();
        Log.i(TAG, "entity.toString(): " + entity.toString());
        Log.i(TAG, "entity.getContenLength(): " + entity.getContentLength());
        Log.i(TAG, "entity.getContentEncoding(): " + entity.getContentEncoding());

//        InputStream content = null;
//        try {
//            content = entity.getContent();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        StringBuilder builder = null;
//        BufferedReader reader = null;
//        try {
//            reader = new BufferedReader(new InputStreamReader(content));
//            builder = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                builder.append(line);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                reader.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                content.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        Log.i(TAG, "content: " + builder.toString());
    }
}
