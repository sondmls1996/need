package com.nf.vi.needfoodshipper.Request;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.x;

/**
 * Created by Minh Nhat on 4/4/2017.
 */

public class LayMaRepuest extends StringRequest {

    private Map<String, String> params;

    public LayMaRepuest(String sdt,

                           String WEBURL, Response.Listener<String> listener) {
        super(Method.POST, WEBURL, listener, null);
        params = new HashMap<>();
        params.put("fone", sdt);

        this.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48,
                x, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}