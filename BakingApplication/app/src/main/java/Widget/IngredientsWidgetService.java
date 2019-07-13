package Widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.ahmed.bakingapplication.R;

public class IngredientsWidgetService extends IntentService {
    public static final String UPDATE_WIDGET = "android.appwidget.action.APPWIDGET_UPDATE";
    public static final String SERVICE_NAME = "IngredientsWidgetService";
    public IngredientsWidgetService() {
        super(SERVICE_NAME);
    }

    public static void startActionUpdateWidget(Context context) {
        Intent intent = new Intent(context, IngredientsWidgetService.class);
        intent.setAction(UPDATE_WIDGET);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (UPDATE_WIDGET.equals(action)) {
                handleWidgetUpdate();
            }
    }
    }

   private void handleWidgetUpdate(){

       AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
       int widgetsIds[] = widgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidget.class));
       widgetManager.notifyAppWidgetViewDataChanged(widgetsIds, R.id.widget_components);
       IngredientsWidget.updateAppWidget(this, widgetManager, widgetsIds);
   }



}

