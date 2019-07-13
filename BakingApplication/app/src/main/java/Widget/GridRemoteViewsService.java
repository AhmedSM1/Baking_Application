package Widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ahmed.bakingapplication.R;

import com.ahmed.bakingapplication.RecipeContract;

public class GridRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridViewsFactory(this.getApplicationContext());
    }

    class GridViewsFactory implements RemoteViewsService.RemoteViewsFactory{
        private Cursor mCursor;
        private Context context;

        public GridViewsFactory(Context context) {
            this.context = context;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

            if (mCursor != null) mCursor.close();

            mCursor = getContentResolver().query(RecipeContract.RecipeEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    RecipeContract.RecipeEntry.TIME_STAMP_COLUMN);
        }

        @Override
        public void onDestroy() {
            if (mCursor != null){
                mCursor.close();
            }
        }

        @Override
        public int getCount() {
            if (mCursor != null){
                return mCursor.getCount();
            }else {
                return 0;
            }
        }
        @Override
        public RemoteViews getViewAt(int i) {
            if (mCursor == null || mCursor.getCount() == 0) {
                return null;
            }

            mCursor.moveToPosition(i);
            int recipePosition = mCursor.getColumnIndex(RecipeContract.RecipeEntry.RECIPE_TITLE_COLUMN);
            int ingredientsPosition = mCursor.getColumnIndex(RecipeContract.RecipeEntry.INGREDIENTS_COLUMN);

            String recipeTitle = mCursor.getString(recipePosition);
            String ingredients = mCursor.getString(ingredientsPosition);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_ingredient_item);

            remoteViews.setTextViewText(R.id.widgetRecipeName, recipeTitle);
            remoteViews.setTextViewText(R.id.widgetIngredienItem, ingredients);
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }



    }




