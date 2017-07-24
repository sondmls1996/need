package com.needfood.kh.SupportClass;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.TextView;

import com.needfood.kh.R;

import java.text.NumberFormat;
import java.util.Locale;

import static com.needfood.kh.R.id.prename;
import static com.needfood.kh.R.id.prequan;

/**
 * Created by Vi on 7/25/2017.
 */

public class DisableScroll extends ListView {



    private int mPosition;

    public DisableScroll(Context context) {
        super(context);
    }

    public DisableScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DisableScroll(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int actionMasked = ev.getActionMasked() & MotionEvent.ACTION_MASK;

        if (actionMasked == MotionEvent.ACTION_DOWN) {
            // Record the position the list the touch landed on
            mPosition = pointToPosition((int) ev.getX(), (int) ev.getY());
            return super.dispatchTouchEvent(ev);
        }

        if (actionMasked == MotionEvent.ACTION_MOVE) {
            // Ignore move events
            return true;
        }

        if (actionMasked == MotionEvent.ACTION_UP) {
            // Check if we are still within the same view
            if (pointToPosition((int) ev.getX(), (int) ev.getY()) == mPosition) {
                super.dispatchTouchEvent(ev);
            } else {
                // Clear pressed state, cancel the action
                setPressed(false);
                invalidate();
                return true;
            }
        }

        return super.dispatchTouchEvent(ev);
    }
}