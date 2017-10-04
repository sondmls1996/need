package com.nf.vi.needfoodshipper.Request;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.x;

/**
 * Created by Minh Nhat on 4/10/2017.
 */

public class SaveGpsRequest extends StringRequest {

    private Map<String, String> params;

    public SaveGpsRequest(String token, String la, String lo,

                          String WEBURL, Response.Listener<String> listener) {
        super(Method.POST, WEBURL, listener, null);
        params = new HashMap<>();
        params.put("accessToken", token);
        params.put("lat", la);
        params.put("long", lo);




        this.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48,
                x, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}