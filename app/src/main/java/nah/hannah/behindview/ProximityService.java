package nah.hannah.behindview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Build;
import android.widget.Toast;

public class ProximityService extends ScreenService{
    private PowerController pc;

    static String CHANNEL_P = "PROXIMITY";
    //사용자에게 보여지는 이름
    static String CHANNEL_P_NAME = "위젯용서비스";


    @Override
    public void onCreate() {
        super.onCreate();
        pc = new PowerController(ProximityService.this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        aLog.e("[onStartCommand]"+intent.getAction()+" : "+flags);
        if(intent!=null && intent.getAction() !=null && intent.getAction().equals("ProximityWidget")){
            startForegroundService(this);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public void startForegroundService(Context context) {
        aLog.e("[startForegroundService]"+context);
       NotificationManager notiManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
       //SDK 26 대응 수정
       Notification.Builder mBuilder = null;
       if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
           //minSdkVersion이 26보다 낮으면 버전 체크 경고가 나오니 그대로 해주면 된다.
           if(!checkNotificationChannel(context,CHANNEL_P)){
               @SuppressLint("WrongConstant")
               NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_P,
                       CHANNEL_P_NAME, NotificationManager.IMPORTANCE_HIGH);
               notificationChannel.setDescription("위젯을 사용할수 있습니다");
               notificationChannel.enableVibration(true);
               notificationChannel.enableLights(true);
               notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC); // 락화면에서 알림을 어느정도 보여줄지 설정

               notiManager.createNotificationChannel(notificationChannel);
           }
           mBuilder = new Notification.Builder(context,CHANNEL_P);
       }else{
           mBuilder = new Notification.Builder(context);
       }
       Intent toLaunch = new Intent(context, BehindActivity.class);
       toLaunch.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
       PendingIntent intentBack = PendingIntent.getActivity(context, 0, toLaunch, 0);

       mBuilder.setSmallIcon(R.drawable.ic_launcher_foreground)
               .setContentTitle("비하인드 바로가기")
               .setOnlyAlertOnce(true)
               .setContentIntent(intentBack); //버튼 누르면 동작.

        startForeground(1, mBuilder.build());
    }

    //notification channel 등록 체크
    @TargetApi(Build.VERSION_CODES.O)
    public static boolean checkNotificationChannel(Context ctx, String channelId ) {
        NotificationManager notificationManager = (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        return notificationManager.getNotificationChannel(channelId) != null ? true : false;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        pc.disableProximityWakeLock();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                //near
                Toast.makeText(getApplicationContext(), "near", Toast.LENGTH_SHORT).show();
                pc.enableProximityWakeLock();
            } else {
                //far
                Toast.makeText(getApplicationContext(), "far", Toast.LENGTH_SHORT).show();
                pc.disableProximityWakeLock();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        super.onAccuracyChanged(sensor,accuracy);
    }
}
