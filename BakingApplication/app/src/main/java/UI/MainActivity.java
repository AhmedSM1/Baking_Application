package UI;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.ahmed.bakingapplication.R;
import com.ahmed.bakingapplication.SimpleIdlingResource;

import Fragments.MainFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Nullable
    private SimpleIdlingResource mIdlingResource;


    @Nullable
    public SimpleIdlingResource getmIdlingResource() {
        if (mIdlingResource == null){
        mIdlingResource = new SimpleIdlingResource();
    }
           return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!isNetworkAvailable()) {
            Toast.makeText(this,"Please make sure you have a network connection",Toast.LENGTH_LONG).show();
        } else if (savedInstanceState == null) {
            initRecipeFragment();
          }
        getmIdlingResource();
    }


    private void initRecipeFragment() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        MainFragment fragment = (MainFragment) fragmentManager.findFragmentById(R.id.main_fragment_space);
        fragment.populateResults();
    }




    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
