package com.nf.vi.needfoodshipper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Tung-PC on 20/03/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private String TAG = "Registration";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.F_REF), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.F_CM), token);
        editor.commit();
        Log.d(TAG, token);

    }


}
