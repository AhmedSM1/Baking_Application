package com.ahmed.bakingapplication;

import android.net.Uri;
import android.provider.BaseColumns;

public final class RecipeContract {


    public static final String AUTHORITY = "com.ahmed.bakingapplication";
    public static final String RECIPE_PATH = ".RecipeProvider";

    public static final Uri BASE_CONTENT = Uri.parse("content://" + AUTHORITY);

    private RecipeContract() {
        throw new AssertionError("not available!");
    }

    public static class RecipeEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon()
                .appendPath(RECIPE_PATH)
                .build();
        public static final String TABLE_NAME = "Ingredients";
        public static final String RECIPE_TITLE_COLUMN = "_recipeName";
        public static final String INGREDIENTS_COLUMN = "_ingredients";
        public static final String TIME_STAMP_COLUMN = "_time";
    }
}
