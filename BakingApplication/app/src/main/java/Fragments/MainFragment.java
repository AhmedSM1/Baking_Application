package Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahmed.bakingapplication.Constants;
import com.ahmed.bakingapplication.R;
import com.ahmed.bakingapplication.SimpleIdlingResource;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import Adapters.RecipeAdapter;
import Model.Recipe;
import Retrofit.Client;
import Retrofit.Services;
import UI.IngredientsActivity;
import UI.MainActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainFragment extends Fragment implements RecipeAdapter.RecipeListener {
    private static final String TAG = MainFragment.class.getName();
    private static List<Recipe> recipesReply;
    private RecipeAdapter adapter;
    private Context context;
    SimpleIdlingResource idlingResource;

    @BindView(R.id.mainRecipeList)
    RecyclerView recipesRecyclerView;


    public MainFragment() {
        // Required empty public constructor
    }


    public static MainFragment newInstance(){
         return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this,view);
        initRecipeAdapter();
        if (recipesReply != null){
            adapter.newRecipes(recipesReply);
        }else {
            populateResults();
        }
        idlingResource = (SimpleIdlingResource) ((MainActivity) getActivity()).getmIdlingResource();
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        return view;
    }

    //method for getting the recipes from API using Retrofit
    public void populateResults(){
        Client client = new Client();
        Services services = client.getClient().create(Services.class);
        Call<List<Recipe>> call = services.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                            recipesReply= response.body();
                            adapter.newRecipes(recipesReply);
                            recipesRecyclerView.setAdapter(adapter);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                            recipesRecyclerView.setLayoutManager(layoutManager);
                            idlingResource.setIdleState(true);
                    }
                }else {
                    Log.d(TAG,"Respone code = "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d(TAG,t.getMessage());
            }
        });
    }

    private void initRecipeAdapter() {
        context = getContext();
        adapter = new RecipeAdapter(context,new ArrayList<Recipe>(0),this);
        recipesRecyclerView.setAdapter(adapter);
    }






    @Override
    public void onRecipeClickListener(int recipePosition) {
        Intent intent = new Intent(context, IngredientsActivity.class);
        intent.putExtra(Constants.INTENT_KEY,recipesReply.get(recipePosition));

        startActivity(intent);
    }



}