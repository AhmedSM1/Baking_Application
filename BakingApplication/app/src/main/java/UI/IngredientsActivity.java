package UI;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.ahmed.bakingapplication.Constants;
import com.ahmed.bakingapplication.R;
import com.github.clans.fab.FloatingActionMenu;

import java.text.DecimalFormat;
import java.util.List;

import Fragments.IngredientsFragment;
import Fragments.MainFragment;
import Fragments.VideoFragment;
import Model.Ingredients;
import Model.Recipe;
import Model.Steps;
import com.ahmed.bakingapplication.RecipeContract;
import Widget.IngredientsWidgetService;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IngredientsActivity extends AppCompatActivity implements IngredientsFragment.StepClickListener {

    private static final String DUMMY_TEXT = "dummy text for testing";


    private static Recipe recipe;
    @BindView(R.id.menuFab)
    FloatingActionMenu mFab;

     Context mContext;
    private IngredientsFragment mIngredientsFragment;
    private FragmentManager mFragmentManager;
    private List<Ingredients> mIngredientsList;
    private List<Steps> mStepsList;
    private String mRecipeTitle;
    private boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        ButterKnife.bind(this);
        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(Constants.INTENT_KEY)) {
            recipe = intent.getParcelableExtra(Constants.INTENT_KEY);
            mRecipeTitle = recipe.getName();
            mIngredientsList = recipe.getIngredients();
            mStepsList = recipe.getSteps();
        } else {
           Toast.makeText(this, "Error couldnt get Intent", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (mStepsList == null || mIngredientsList == null){
            Toast.makeText(this, "adapter is getting null list", Toast.LENGTH_SHORT).show();
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mRecipeTitle);
        }

        mFragmentManager = getSupportFragmentManager();

       twoPane = findViewById(R.id.tabletLayout) != null;

        if (savedInstanceState == null) {
            startIngredientsFragment();
        }
    }


    @Override
    public void onStepClickListener(Steps step) {
        if (!twoPane) {
            watchStepInstructions(step);
        } else {
            changeView(step);
        }
    }

    private void changeView(Steps steps){
        String videoURL = steps.getVideoURL();
        String stepInstructions = steps.getDescription();
        if (!videoURL.equals("")) {
            VideoFragment videoFragment = new VideoFragment();
            videoFragment.setmVideoURL(videoURL);
            videoFragment.setmStepInstructions(stepInstructions);

            mFragmentManager.beginTransaction()
                    .replace(R.id.videoFragmentContainer, videoFragment)
                    .commit();
        } else {
            Toast.makeText(this, R.string.not_avalible, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.shareFab)
    public void shareIngredientsFab(){
        String ingredients = R.string.ingredients_to_share +
                mRecipeTitle + " :\n " +
                getIngredientsInOnePiece();
        shareIngredients(ingredients);

    }


    private void shareIngredients(String ingredientsToShare) {
        ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(ingredientsToShare) //(TODO)replace this paramater with DUMMY_TEXT
                .setChooserTitle(R.string.share_chooser_label)
                .startChooser();
    }


    @OnClick(R.id.addToWidgetFab)
    public void prepareTheIngredients() {
        long timeAdded = System.currentTimeMillis();
       addIngredientIntoDatabase(mRecipeTitle , getIngredientsInOnePiece(), timeAdded);
    }

    private void addIngredientIntoDatabase(String recipeTitle, String ingredients, long date) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RecipeContract.RecipeEntry.RECIPE_TITLE_COLUMN, recipeTitle);
        contentValues.put(RecipeContract.RecipeEntry.INGREDIENTS_COLUMN, ingredients);
        contentValues.put(RecipeContract.RecipeEntry.TIME_STAMP_COLUMN, date);

        Uri uri = getContentResolver().insert(RecipeContract.RecipeEntry.CONTENT_URI, contentValues);
        onInsertingCompleted(uri);
    }

    private void onInsertingCompleted(Uri uri) {
        if (uri != null) {
            Toast.makeText(this, R.string.added_to_widget, Toast.LENGTH_SHORT).show();
            updateWidget();
            mFab.close(true);


        } else {
            Toast.makeText(this, R.string.falied_to_add, Toast.LENGTH_SHORT).show();
        }
    }
    private void updateWidget() {
        IngredientsWidgetService.startActionUpdateWidget(this);
    }



    private String getIngredientsInOnePiece() {
        StringBuilder ingredientToBeSaved = new StringBuilder();
        DecimalFormat quantityFormatter = new DecimalFormat("0.#");

        for (int i = 0; i < mIngredientsList.size(); i++) {
            double quantity = mIngredientsList.get(i).getQuantity();
            ingredientToBeSaved.append(quantityFormatter.format(quantity))
                    .append(" ")
                    .append(mIngredientsList.get(i).getMeasure())
                    .append("of ")
                    .append(mIngredientsList.get(i).getIngredient())
                    .append("\n");
        }
        return ingredientToBeSaved.toString();
    }

    private void startIngredientsFragment() {
        mIngredientsFragment = new IngredientsFragment();
        mIngredientsFragment.setmIngredientsList(mIngredientsList);
        mIngredientsFragment.setmStepsList(mStepsList);
        mIngredientsFragment.setRecipe(mRecipeTitle);
        mFragmentManager.beginTransaction()
                .replace(R.id.ingredients_fragments_space, mIngredientsFragment)
                .commit();
    }

    private void watchStepInstructions(Steps step) {
        String videoURL = step.getVideoURL();
        String shortDescription = step.getShortDescription();
        String stepInstructions = step.getDescription();
        if (!videoURL.equals("")) {
            Intent videoIntent = new Intent(this, VideoActivity.class);
            videoIntent.putExtra(Constants.INTENT_STEP_INSTRUCTIONS_KEY, stepInstructions);
            videoIntent.putExtra(Constants.INTENT_VIDEO_URL_KEY, videoURL);
            videoIntent.putExtra(Constants.INTENT_DESCRIPTION_KEY, shortDescription);
            startActivity(videoIntent);
        } else {
            Toast.makeText(this, R.string.not_avalible, Toast.LENGTH_SHORT).show();
        }
    }



}
