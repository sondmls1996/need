package com.nf.vi.needfoodshipper.Request;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.x;

/**
 * Created by Minh Nhat on 7/15/2017.
 */

public class SearchRequest extends StringRequest {

    private Map<String, String> params;

    public SearchRequest(String page,
                         String accessToken,
                         String key,
                         String dateStart,
                         String dateEnd,
                         String WEBURL, Response.Listener<String> listener) {
        super(Request.Method.POST, WEBURL, listener, null);
        params = new HashMap<>();
        params.put("page", page);
        params.put("accessToken", accessToken);
        params.put("key", key);
        params.put("dateStart", dateStart);
        params.put("dateEnd", dateEnd);
        this.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48,
                x, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}