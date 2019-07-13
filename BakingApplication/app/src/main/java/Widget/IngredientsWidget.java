package Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.ahmed.bakingapplication.Constants;
import com.ahmed.bakingapplication.R;
import com.google.gson.Gson;

import java.util.List;

import Fragments.IngredientsFragment;
import Model.Ingredients;
import Model.Recipe;
import Model.Steps;
import UI.MainActivity;

/**
 * Implementation of App IngredientsWidget functionality.
 */
public class IngredientsWidget extends AppWidgetProvider {
    @SuppressWarnings("WeakerAccess")
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = getIngredientsGridView(context);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static RemoteViews getIngredientsGridView(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.my_widget);
        Intent gridViewServiceIntent = new Intent(context, GridRemoteViewsService.class);
        remoteViews.setRemoteAdapter(R.id.widget_components, gridViewServiceIntent);

        Intent appIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.widget_components, pendingIntent);

        remoteViews.setEmptyView(R.id.widget_components, R.id.empty_view);
        return remoteViews;

    }

    public static void updateAppWidget(Context context, AppWidgetManager manager, int[] widgetsIds) {
        for (int appWidgetId : widgetsIds) {
            updateAppWidget(context, manager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        IngredientsWidgetService.startActionUpdateWidget(context);
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}

