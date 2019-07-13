package Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahmed.bakingapplication.Constants;
import com.ahmed.bakingapplication.R;
import com.google.gson.Gson;

import java.util.List;

import Model.Ingredients;
import Model.Recipe;
import Model.Steps;
import UI.IngredientsActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private List<Recipe> recipes;
    private RecipeListener mRecipeListener;
    private Context mContext;
    private List<Ingredients> ingredientList;
    private List<Steps> stepList;

    public RecipeAdapter(Context context,List<Recipe> recipes, RecipeListener mRecipeListener) {
        this.mContext = context;
        this.recipes = recipes;
        this.mRecipeListener = mRecipeListener;
    }


    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        final View recipesListView = LayoutInflater.from(mContext).inflate(R.layout.recipe, viewGroup, false);
        final RecipeViewHolder recipeViewHolder = new RecipeViewHolder(recipesListView);
        recipesListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecipeListener.onRecipeClickListener(recipeViewHolder.getAdapterPosition());
            }
        });
        return recipeViewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder viewHolder, int i) {
       String title = recipes.get(i).getName();
       viewHolder.mTitle.setText(title);

    }
    public void newRecipes(List<Recipe> newRecipes){
        recipes.clear();
        recipes.addAll(newRecipes);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
    public interface RecipeListener{
        void onRecipeClickListener(int recipePosition);
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        private Recipe mItem;
        @BindView(R.id.recipe_title)
        TextView mTitle;

        public RecipeViewHolder(View view) {
            super(view);
            mView = view;

            ButterKnife.bind(this, view);
        }




    }
}






