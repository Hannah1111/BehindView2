package nah.hannah.behindview;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by hannah on 2018. 11. 9..
 */
public class AvobeViewService extends Service {
    private View mView;
    private WindowManager mManager;
    private WindowManager.LayoutParams mParams;

    @Override
    public void onCreate() {
        super.onCreate();
        mManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mInflater.inflate(R.layout.floating_view, null);
        mView.setOnTouchListener(mViewTouchListener);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams = new WindowManager.LayoutParams(
                     WindowManager.LayoutParams.WRAP_CONTENT,
                     WindowManager.LayoutParams.WRAP_CONTENT,
                     WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                     WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                     PixelFormat.TRANSLUCENT);
        }else{
            mParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }

        mParams.gravity = Gravity.TOP | Gravity.LEFT;

        mManager.addView(mView, mParams);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        mManager.removeView(mView);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private float mTouchX, mTouchY;
    private int mViewX, mViewY;

    private View.OnTouchListener mViewTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    mTouchX = event.getRawX();
                    mTouchY = event.getRawY();
                    mViewX = mParams.x;
                    mViewY = mParams.y;

                    break;

                case MotionEvent.ACTION_UP:
                    break;

                case MotionEvent.ACTION_MOVE:
                    int x = (int) (event.getRawX() - mTouchX);
                    int y = (int) (event.getRawY() - mTouchY);

                    mParams.x = mViewX + x;
                    mParams.y = mViewY + y;

                    mManager.updateViewLayout(mView, mParams);

                    break;
            }

            return true;
        }
    };

}
