package com.nf.vi.needfoodshipper.Request;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.x;

/**
 * Created by Minh Nhat on 7/8/2017.
 */

public class ChangeInformationRequest extends StringRequest {

    private Map<String, String> params;

    public ChangeInformationRequest(String accessToken, String fullName, String email, String address, String birthday, String skype, String facebook, String description,

                                    String WEBURL, Response.Listener<String> listener) {
        super(Method.POST, WEBURL, listener, null);
        params = new HashMap<>();
        params.put("accessToken", accessToken);
        params.put("fullName", fullName);
        params.put("email", email);
        params.put("address", address);
        params.put("birthday", birthday);
        params.put("skype", skype);
        params.put("facebook", facebook);
        params.put("description", description);

        this.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48,
                x, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

