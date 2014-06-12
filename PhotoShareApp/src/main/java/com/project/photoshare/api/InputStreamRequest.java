package com.project.photoshare.api;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="mailto:">TakuyaKodama</a> (kodama-t)
 * @version 1.00 13/12/09 kodama-t
 */
public class InputStreamRequest extends Request<InputStream> {

    private final Response.Listener<InputStream> mListener;

    /**
     *
     * @param method
     * @param url
     * @param listener
     * @param errorListener
     */
    public InputStreamRequest(int method, String url, Response.Listener<InputStream> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
    }

    /**
     *
     * @param url
     * @param listener
     * @param errorListener
     */
    public InputStreamRequest(String url, Response.Listener<InputStream> listener, Response.ErrorListener errorListener) {
        this(Method.GET, url, listener, errorListener);
    }

    /**
     *
     * @param response Response from the network
     *
     * @return The parsed response, or null in the case of an error
     */
    @Override
    protected Response<InputStream> parseNetworkResponse(NetworkResponse response) {
        InputStream inputStream = new ByteArrayInputStream(response.data);
        return Response.success(inputStream, HttpHeaderParser.parseCacheHeaders(response));
    }

    /**
     * Subclasses must implement this to perform delivery of the parsed
     * response to their listeners.  The given response is guaranteed to
     * be non-null; responses that fail to parse are not delivered.
     *
     * @param response The parsed response returned by
     *                 {@link #parseNetworkResponse(com.android.volley.NetworkResponse)}
     */
    @Override
    protected void deliverResponse(InputStream response) {
        mListener.onResponse(response);
    }
}
