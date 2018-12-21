package nah.hannah.behindview;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.Toast;

public class ScreenService extends Service implements SensorEventListener{
    Sensor mProxiSensor;
    SensorManager mSensorManager;
    static final float SENSOR_SENSITIVITY = 4;
    public ScreenService() {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
           //basic
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        aLog.e("[onAccuracyChanged]"+sensor.getName()+" "+accuracy);
        switch (accuracy) {
            case SensorManager.SENSOR_STATUS_UNRELIABLE:
                Toast.makeText(ScreenService.this, "UNRELIABLE", Toast.LENGTH_SHORT).show();
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
                Toast.makeText(ScreenService.this, "LOW", Toast.LENGTH_SHORT).show();
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
                Toast.makeText(ScreenService.this, "MEDIUM", Toast.LENGTH_SHORT).show();
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
                Toast.makeText(ScreenService.this, "HIGH", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public void onCreate() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProxiSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if(mSensorManager == null){
            if(mProxiSensor == null){
                aLog.e("[mProxiSensor] is null");
            }else{
                aLog.e("[mSensorManager] is null");
            }
        }else{
            aLog.e("[onCreate]");
            aLog.e("[onCreate] "+mProxiSensor.isWakeUpSensor());
            aLog.e("[onCreate] "+mProxiSensor.getStringType());

        }
        aLog.e("[onCreate]"+mSensorManager+" : "+mProxiSensor);
        mSensorManager.registerListener(this, mProxiSensor, SensorManager.SENSOR_DELAY_NORMAL);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        aLog.e("[onStartCommand]"+intent+" : "+flags);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        aLog.e("[onDestroy]"+mSensorManager+" : "+mProxiSensor);
        mSensorManager.unregisterListener(this,mProxiSensor);
        mSensorManager = null;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
