package nah.hannah.behindview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

/**
 * Created by hannah on 2018. 11. 27..
 */
public class PowerController {
    private Context context;
    private PowerManager powerManager;
    private PowerManager.WakeLock proximityWakeLock;
    public PowerController(Context context) {
        this.context = context;
    }

    @SuppressLint("InvalidWakeLockTag")
    public boolean enableProximityWakeLock() {
        if (proximityWakeLock != null) {
            return true;
        }
        Log.e("HANNAH","[enableProximityWakeLock]");
        powerManager =
                (PowerManager) context.getApplicationContext().getSystemService(Context.POWER_SERVICE);
        proximityWakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "");
        proximityWakeLock.acquire();
        return true;
    }

    public void disableProximityWakeLock() {
        if (proximityWakeLock != null) {
            proximityWakeLock.release();
            proximityWakeLock = null;
        }
        Log.e("HANNAH","[disableProximityWakeLock]");
    }
}
