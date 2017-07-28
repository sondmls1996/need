package com.nf.vi.needfoodshipper.Request;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.x;

/**
 * Created by Minh Nhat on 7/5/2017.
 */

public class TrangThaiRequest extends StringRequest {
    private Map<String, String> params;

    public TrangThaiRequest(String accessToken,
                            String note,
                            String status,
                            String idOrder,
                            String timeLeft,
                            String WEBURL, Response.Listener<String> listener) {
        super(Method.POST,WEBURL, listener, null);
        params = new HashMap<>();
        params.put("accessToken", accessToken);
        params.put("note", note);
        params.put("status", status);
        params.put("idOrder", idOrder);
        params.put("timeLeft", timeLeft);
        this.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48,
                x, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}