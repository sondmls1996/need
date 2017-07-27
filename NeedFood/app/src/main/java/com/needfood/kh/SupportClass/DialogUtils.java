package com.needfood.kh.SupportClass;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.needfood.kh.R;

/**
 * Created by Vi on 12/20/2016.
 */

public class DialogUtils {
    public static ProgressDialog show(final Context context, String text) {
        final ProgressDialog m_Dialog = new ProgressDialog(context);
        m_Dialog.setMessage(text);
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                m_Dialog.cancel();
                Toast.makeText(context, context.getResources().getString(R.string.checkin), Toast.LENGTH_SHORT).show();
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postAtTime(progressRunnable, 10000);
        m_Dialog.show();
        return m_Dialog;
    }

}
