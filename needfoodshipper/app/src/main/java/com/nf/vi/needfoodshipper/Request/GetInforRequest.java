package com.nf.vi.needfoodshipper.Request;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.x;

/**
 * Created by admin on 31/07/2017.
 */

public class GetInforRequest extends StringRequest {

    private Map<String, String> params;

    public GetInforRequest(String token,
                            String idshiper,
                           String WEBURL, Response.Listener<String> listener) {
        super(Method.POST, WEBURL, listener, null);
        params = new HashMap<>();
        params.put("accessToken", token);
        params.put("idShiper", token);

        this.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48,
                x, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
