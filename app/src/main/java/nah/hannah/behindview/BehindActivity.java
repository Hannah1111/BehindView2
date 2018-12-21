package nah.hannah.behindview;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BehindActivity extends AppCompatActivity {


    private int REQ_CODE_OVERLAY_PERMISSION = 0;

    @BindView(R.id.btnStart)
    Button btnStart;

    @OnClick({R.id.btnStart, R.id.btnStop})
    public void onClickbtn(View view){
        switch(view.getId()){
            case R.id.btnStart:
                if(hasRuntimePermissionToDrawOverlay(BehindActivity.this)){
                    if(!isServiceRunningCheck(BehindActivity.this)){
                        startService(new Intent(BehindActivity.this,WindowService.class));
                        tvStatus.setText("서비스 상태 : 실행중");
                    }
                }else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        createIntentToRequestOverlayPermission(BehindActivity.this);
                    }
                }
                break;

            case R.id.btnStop:
                stopService(new Intent(BehindActivity.this,WindowService.class));
                tvStatus.setText("서비스 상태 : 꺼짐");
                break;

                default:
                    break;
        }

    }
    @BindView(R.id.btnStop)
    Button btnStop;
    @BindView(R.id.tvStatus)
    TextView tvStatus;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behind);
        ButterKnife.bind(this);

        if(isServiceRunningCheck(this)){
            tvStatus.setText("서비스 상태 : 실행중");
        }else{
            tvStatus.setText("서비스 상태 : 꺼짐");
        }
    }



    public static boolean hasRuntimePermissionToDrawOverlay(@NonNull Context context) {
        //noinspection SimplifiableIfStatement
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Runtime permissions are required. Check for the draw overlay permission.
            return Settings.canDrawOverlays(context);
        } else {
            // No runtime permissions required. We're all good.
            return true;
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @NonNull
    public void createIntentToRequestOverlayPermission(@NonNull Context context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, REQ_CODE_OVERLAY_PERMISSION);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        aLog.e("ACTIVITY RESULT "+requestCode);
        startService(new Intent(this,WindowService.class));
        tvStatus.setText("서비스 상태 : 실행중");
    }

    public static boolean isServiceRunningCheck(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            aLog.e("[serviceCheck] "+service.service.getClassName());
            if ("nah.hannah.behindview.ProximityService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
