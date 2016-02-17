package in.periculum.ims.handler;

import org.json.JSONObject;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Request.Priority;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonSyntaxException;

import java.util.Map;

import in.periculum.ims.application.AppController;
import in.periculum.ims.listener.HttpRequestCallback;


public class CommonHandler {
    static JsonObjectRequest jsonObjReq;
    public static void loadTheData(String tag_json_obj, String url,
                                   final HttpRequestCallback<JSONObject> callback, JSONObject params) {
        loadTheData(tag_json_obj, url, callback, Priority.IMMEDIATE, params);
    }

    public static void loadTheData(final String tag_json_obj, String url,
                                   final HttpRequestCallback<JSONObject> callback, Priority p,
                                   JSONObject params) {

        Log.e("urlsender", url);
        if (params != null) {
            jsonObjReq = new JsonObjectRequest(Method.POST, url,
                    params, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response != null) {
                            callback.response(tag_json_obj, response);
                        } else
                            callback.onError("");
                    } catch (JsonSyntaxException e) {
                        Log.e("response", "no");
                        callback.onError("error occured");
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callback.onError("Slow Network Error");
                    VolleyLog.d("VOL", "Error: " + error.getMessage());
                }
            });

        } else {
            jsonObjReq = new JsonObjectRequest(Method.POST, url,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response != null) {

                                    callback.response(tag_json_obj, response);
                                } else
                                    callback.onError("");
                            } catch (JsonSyntaxException e) {
                                Log.e("response", "no");
                                callback.onError("error occured");
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callback.onError("Slow Network Error");
                    VolleyLog.d("VOL", "Error: " + error.getMessage());
                }
            });
        }
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }
}
