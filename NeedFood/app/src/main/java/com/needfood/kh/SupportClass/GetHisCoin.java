package com.needfood.kh.SupportClass;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.x;

/**
 * Created by admin on 15/08/2017.
 */

public class GetHisCoin extends StringRequest {

    private Map<String, String> params;

    public GetHisCoin(
            String WEBURL,
            String accessToken,
            String page,
            Response.Listener<String> listener) {
        super(Request.Method.POST, WEBURL, listener, null);
        params = new HashMap<>();
//        params = params2;
        params.put("accessToken", accessToken);
        params.put("page", page);
        this.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48,
                x, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
