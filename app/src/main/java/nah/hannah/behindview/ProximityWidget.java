package nah.hannah.behindview;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class ProximityWidget extends AppWidgetProvider {

    private static String ACTION_BTN_CLICK = "WIDGET_BTN_CLICK";
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.proximity_widget);
        if(BehindActivity.isServiceRunningCheck(context)){
            views.setTextViewText(R.id.tvWidget, "끄기");
        }else{
            views.setTextViewText(R.id.tvWidget, "시작");
        }
        Intent clickIntent=new Intent(context,ProximityWidget.class);
        clickIntent.setAction(ACTION_BTN_CLICK);

        PendingIntent pendingIntent=PendingIntent.getBroadcast(context,0,clickIntent,0);
        views.setOnClickPendingIntent(R.id.tvWidget,pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Log.e("HANNAH","[onUpdate]"+appWidgetIds);

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        Log.e("HANNAH","[onReceive]"+action);
        if(action.equals(ACTION_BTN_CLICK)){
            Log.e("HANNAH","[onReceive2]"+action);
            if(!BehindActivity.isServiceRunningCheck(context)){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //잡 스케줄러를 돌립시다!
                    Intent intent1 = new Intent(context,ProximityService.class);
                    intent1.setAction("ProximityWidget");
                    context.startService(intent1);
                }else{
                    context.startService(new Intent(context,ProximityService.class));
                }
                Toast.makeText(context,"서비스시작",Toast.LENGTH_LONG).show();
            }else{
                context.stopService(new Intent(context,ProximityService.class));
                Toast.makeText(context,"서비스꺼짐",Toast.LENGTH_LONG).show();
            }
        }

        updateAppWidget(context, AppWidgetManager.getInstance(context), 0);
        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
        //잡스케줄러 레지
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        //잡스케줄러 언레지
        // Enter relevant functionality for when the last widget is disabled
    }
}

