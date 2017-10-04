package com.nf.vi.needfoodshipper.Request;

/**
 * Created by Minh Nhat on 3/31/2017.
 */
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.x;


public class PostTokenRequest extends StringRequest {
    private Map<String, String> params;

    public PostTokenRequest(String acc,
                            String token,
                            String WEBURL, Response.Listener<String> listener) {
        super(Method.POST,WEBURL, listener, null);
        params = new HashMap<>();
        params.put("accessToken", acc);
        params.put("tokenDevice", token);
        this.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48,
                x, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}


