package Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahmed.bakingapplication.R;

import java.util.List;

import Model.Ingredients;
import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {
    private Context context;
    private List<Ingredients> ingredientsList;

    public IngredientsAdapter(Context context, List<Ingredients> ingredientsList) {
        this.context = context;
        this.ingredientsList = ingredientsList;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.ingredient,viewGroup,false);
        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsAdapter.IngredientsViewHolder ingredientsViewHolder, int i) {
         String mTitle = ingredientsList.get(i).getIngredient();
         String mMeasure = ingredientsList.get(i).getMeasure();
         String mQuantity = ingredientsList.get(i).getQuantity().toString();

        ingredientsViewHolder.title.setText(mTitle);
        ingredientsViewHolder.measure.setText(mMeasure);
        ingredientsViewHolder.quantity.setText(mQuantity);

    }

    @Override
    public int getItemCount() {
        if (ingredientsList == null) {
            return 0;
        } else {
            return ingredientsList.size();
        }
    }
    public class IngredientsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.quantity)
        TextView quantity;
        @BindView(R.id.measure)
        TextView measure;
        public IngredientsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
