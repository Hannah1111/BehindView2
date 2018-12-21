package nah.hannah.behindview;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;

import java.util.Timer;
import java.util.TimerTask;

public class WindowService extends ScreenService {
    public WindowService() {
    }

    int count = 0;

    public class SetTimer{
        TimerTask tt;
        Timer timer = new Timer();

        boolean isRunning = false;

        public void SetTimer(){

        }

        public void run(){
            tt = new TimerTask() {
                @Override
                public void run() { //3초 후 타이머 0
                    count = 0;
                }
            };
            timer.schedule(tt,0,3000);
            isRunning = true;
            aLog.e("[SetTimer run]"+timer);
        }

        public void cancel(){
            aLog.e("[SetTimer cancel]"+timer);
            if(timer!= null) timer.cancel();
            isRunning = false;
        }

        public boolean isActive(){
            aLog.e("[SetTimer isActive]"+isRunning);
            return isRunning;
        }
    }

    private SetTimer setTimer = new SetTimer();



    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            aLog.e("[onSensorChanged]"+event.values[0]);
            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                //near
                if(!setTimer.isActive()){
                    setTimer.run();
                }

                aLog.e("[onSensorChanged near]"+event.values[0]+" : "+count);
                //화면이 켜져있으면 화면을 끈다.

                //화면이 꺼져있는데 count가 2보다 크면 화면을 켜보자.
                count++;
            }else{
                aLog.e("[onSensorChanged far]"+event.values[0]+" : "+count);
            }
        }
    }

    @Override
    public void onCreate() {

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if(setTimer!=null){
            setTimer.cancel();
            setTimer = null;
        }
        super.onDestroy();
    }

}
