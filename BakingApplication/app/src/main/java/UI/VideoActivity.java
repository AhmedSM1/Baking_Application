package UI;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ahmed.bakingapplication.Constants;
import com.ahmed.bakingapplication.R;

import Fragments.VideoFragment;

public class VideoActivity extends AppCompatActivity {
    final private static String TAG = VideoActivity.class.getName();
    private String videoURL;
    private String stepInstruction;
    private String sStepDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Intent iRecipe = getIntent();

        if (iRecipe != null) {
            videoURL = iRecipe.getStringExtra(Constants.INTENT_VIDEO_URL_KEY);
            stepInstruction = iRecipe.getStringExtra(Constants.INTENT_STEP_INSTRUCTIONS_KEY);
           sStepDescription = iRecipe.getStringExtra(Constants.INTENT_DESCRIPTION_KEY);
        } else {
            Log.d(TAG, "Couldnt get intent");
            finish();
        }

        if (!isNetworkAvailable()) {
            Toast.makeText(this,
                    R.string.no_internet_label, Toast.LENGTH_LONG).show();
            finish();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(sStepDescription);
        }

        initRecipeFragment();

    }

    private void initRecipeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        VideoFragment fragment = new VideoFragment();
        fragment.setmVideoURL(videoURL);
        fragment.setmStepInstructions(stepInstruction);

        fragmentManager.beginTransaction()
                .replace(R.id.videoFragmentContainer, fragment)
                .commit();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}