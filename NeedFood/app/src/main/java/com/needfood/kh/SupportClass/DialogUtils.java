package com.needfood.kh.SupportClass;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Vi on 12/20/2016.
 */

public class DialogUtils {
    public static ProgressDialog show(Context context, String text){
        ProgressDialog m_Dialog = new ProgressDialog(context);
        m_Dialog.setMessage(text);
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        m_Dialog.show();
        return m_Dialog;
    }

}
