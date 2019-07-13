package Fragments;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ahmed.bakingapplication.Constants;
import com.ahmed.bakingapplication.R;
import com.google.gson.Gson;

import java.util.List;

import Adapters.IngredientsAdapter;
import Adapters.StepsAdapter;
import Model.Ingredients;
import Model.Recipe;
import Model.Steps;
import Widget.IngredientsWidget;
import butterknife.BindView;
import butterknife.ButterKnife;


public class IngredientsFragment extends Fragment implements StepsAdapter.onStepClick {
    private static final String TAG = IngredientsFragment.class.getSimpleName();
    private static String recipeTitle;
    private static List<Ingredients> mIngredientsList;
    private static List<Steps> mStepsList;
    Gson gson;

    @BindView(R.id.ingredients_recyclerView)
    RecyclerView ingredientsRecyclerView;

    @BindView(R.id.steps_recyclerView)
    RecyclerView stepsRecyclerView;

    private Context context;
    private StepClickListener stepClickListener;
    private Parcelable mStepsRecyclerViewState;
    private Parcelable mIngredientsRecyclerViewState;

    public IngredientsFragment() {
    }

    @VisibleForTesting
    public static IngredientsFragment getFragmentInstance() {
        return new IngredientsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredients, viewGroup, false);
        ButterKnife.bind(this, view);
        context = getContext();
        gson = new Gson();
        setupStepsAndIngredientsViews();
          return view;
      }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mStepsRecyclerViewState = savedInstanceState.getParcelable(Constants.STEPS_STATE_KEY);
            mIngredientsRecyclerViewState = savedInstanceState.getParcelable(Constants.INGREDIENTS_STATE_KEY);
        }

    }

    private void setupStepsAndIngredientsViews() {
        //steps recycler view:
        StepsAdapter mStepsAdapter = new StepsAdapter(context, getmStepsList(), this);
        stepsRecyclerView.setAdapter(mStepsAdapter);
        if (getmStepsList()==null){
            Toast.makeText(context,"there is not steps",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(context,"steps data exist",Toast.LENGTH_LONG).show();
        }
        //ingredients recycler view:
        IngredientsAdapter mIngredientsAdapter = new IngredientsAdapter(context, getmIngredientsList());
        ingredientsRecyclerView.setAdapter(mIngredientsAdapter);

        stepsRecyclerView.setHasFixedSize(true);
        restoreRecyclerViewsState();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mStepsRecyclerViewState = stepsRecyclerView.getLayoutManager().onSaveInstanceState();
        mIngredientsRecyclerViewState = ingredientsRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(Constants.STEPS_STATE_KEY, mStepsRecyclerViewState);
        outState.putParcelable(Constants.INGREDIENTS_STATE_KEY, mIngredientsRecyclerViewState);
    }

    private void restoreRecyclerViewsState() {
        if (mStepsRecyclerViewState != null && mIngredientsRecyclerViewState != null) {
            stepsRecyclerView.getLayoutManager().onRestoreInstanceState(mStepsRecyclerViewState);
            ingredientsRecyclerView.getLayoutManager().onRestoreInstanceState(mIngredientsRecyclerViewState);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            stepClickListener = (StepClickListener) context;
        } catch (ClassCastException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onStepClickListener(Steps step) {
        stepClickListener.onStepClickListener(step);
    }

    private List<Steps> getmStepsList() {
        return mStepsList;
    }

    public void setmStepsList(List<Steps> mStepsList) {
        IngredientsFragment.mStepsList = mStepsList;
    }

    private  List<Ingredients> getmIngredientsList() {
        return mIngredientsList;
    }

    public void setmIngredientsList(List<Ingredients> mIngredientsList) {
        IngredientsFragment.mIngredientsList = mIngredientsList;
    }

    public static String getRecipeTitle() {
        return recipeTitle;
    }

    public static void setRecipe(String title) {
        IngredientsFragment.recipeTitle = title;
    }

    public interface StepClickListener {
        void onStepClickListener(Steps step);
    }
}
