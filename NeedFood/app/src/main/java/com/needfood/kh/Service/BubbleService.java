package com.needfood.kh.Service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.needfood.kh.R;


/**
 * Created by Vi on 6/14/2017.
 */

public class BubbleService extends Service implements View.OnClickListener {
    private WindowManager mWindowManager;
    private View mChatHeadView;
    LinearLayout lnb;
    TextView txtgia;
    public BubbleService() {
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public int onStartCommand (Intent intent, int flags, int startId) {
        if(intent.hasExtra("MN")){
            int pr = Integer.parseInt(intent.getStringExtra("MN"));

            txtgia.setText(Integer.parseInt(txtgia.getText().toString())+pr +"");
        }

        return START_STICKY;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //Inflate the chat head layout we created

        mChatHeadView = LayoutInflater.from(this).inflate(R.layout.buynow, null);


        //Add the view to the window.
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mChatHeadView, params);

        txtgia = (TextView)mChatHeadView.findViewById(R.id.txthang);


            txtgia.setText("0");

        lnb = (LinearLayout)mChatHeadView.findViewById(R.id.lnbn);
        lnb.setOnTouchListener(new View.OnTouchListener() {
            private int lastAction;
            private int initialX;
            private int initialY;
            private float initialTouchX;

            private static final int MAX_CLICK_DURATION = 1000;

            /**
             * Max allowed distance to move during a "click", in DP.
             */
            private static final int MAX_CLICK_DISTANCE = 15;

            private long pressStartTime;
            private float initialTouchY;

            @Override
                                   public boolean onTouch(View v, MotionEvent event) {
                                       switch (event.getAction()) {
                                           case MotionEvent.ACTION_DOWN:
                                               pressStartTime = System.currentTimeMillis();
                                               //remember the initial position.
                                               initialX = params.x;
                                               initialY = params.y;
                                               initialTouchX = event.getRawX();
                                               initialTouchY = event.getRawY();

                                               lastAction = event.getAction();
                                               return true;


                                           case MotionEvent.ACTION_UP:
                                               //As we implemented on touch listener with ACTION_MOVE,
                                               //we have to check if the previous action was ACTION_DOWN
                                               //to identify if the user clicked the view or not.
                                               long totime =System.currentTimeMillis();
                                               long endtime = totime-pressStartTime;
                                               int Xdiff = (int) (event.getRawX() - initialTouchX);
                                               int Ydiff = (int) (event.getRawY() - initialTouchY);
                                               if (endtime<200) {
                                                   //Open the chat conversation click.
                                                   //  startService(new Intent(QBService.this, BubbleService2.class));
                                               }
                                               lastAction = event.getAction();
                                               return true;
                                           case MotionEvent.ACTION_MOVE:
                                               //Calculate the X and Y coordinates of the view.
                                               params.x = initialX + (int) (event.getRawX() - initialTouchX);
                                               params.y = initialY + (int) (event.getRawY() - initialTouchY);

                                               //Update the layout with new X & Y coordinate
                                               mWindowManager.updateViewLayout(mChatHeadView, params);
                                               lastAction = event.getAction();
                                               return true;

                                       }
                                       return false;
                                   }
                               }
        );

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatHeadView != null) mWindowManager.removeView(mChatHeadView);
    }

    @Override
    public void onClick(View view) {



    }





}