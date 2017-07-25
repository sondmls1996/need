package com.needfood.kh.SupportClass;

import android.app.Application;
import android.content.Context;

/**
 * Created by admin on 25/07/2017.
 */

public class MainApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }
}
