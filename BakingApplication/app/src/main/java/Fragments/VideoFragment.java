package Fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmed.bakingapplication.Constants;
import com.ahmed.bakingapplication.R;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import Model.Steps;
import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoFragment extends Fragment implements Player.EventListener {

    private static final String TAG = VideoFragment.class.getSimpleName();
    private static long mCurrentPlayerPosition;
    private static boolean mPlayWhenReady = true;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.videoPlayer)
    PlayerView mPlayerView;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.stepDescription)
    TextView mStepInstructionsTextView;
    @SuppressWarnings("WeakerAccess")

    private String mStepInstructions;
    private String mVideoURL;
    private ExoPlayer mPlayer;
    private Context mContext;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mPlayState;

    public VideoFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View recipeView = inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, recipeView);
        mContext = getContext();

        mStepInstructionsTextView.setText(getmStepInstructions());
        if (savedInstanceState != null) {
            mCurrentPlayerPosition = savedInstanceState.getLong(Constants.PLAYER_POSITION_KEY);
            mPlayWhenReady = savedInstanceState.getBoolean(Constants.PLAYER_PLAY_WHEN_READY_KEY);
        }

        return recipeView;
    }


    private void initializePlayer() {
        mPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(),
                new DefaultLoadControl());

        mPlayerView.setPlayer(mPlayer);
        MediaSource mediaSource = loadTheVideo(getmVideoURL());
        mPlayer.prepare(mediaSource, true, false);

        if (mCurrentPlayerPosition != C.POSITION_UNSET) {
            mPlayer.seekTo(mCurrentPlayerPosition);
        }

        mPlayer.setPlayWhenReady(mPlayWhenReady);
        mPlayer.addListener(this);
        initializeMediaSession();
    }

    private MediaSource loadTheVideo(String videoUrl) {
        Uri videoMedia = Uri.parse(videoUrl);
        String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory(userAgent))
                .createMediaSource(videoMedia);
    }

    private void releaseThePlayer() {
        if (getmVideoURL() == null || getmVideoURL().equals("")) return;

        mMediaSession.setActive(false);
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(mContext, TAG);
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(null);
        mPlayState = initializeThePlaybackState();
        mMediaSession.setPlaybackState(mPlayState.build());
        mMediaSession.setCallback(new VideoCallback());
        mMediaSession.setActive(true);
    }

    private PlaybackStateCompat.Builder initializeThePlaybackState() {
        return new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.STATE_PAUSED |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playWhenReady && playbackState == Player.STATE_READY) {
            mPlayState.setState(PlaybackStateCompat.STATE_PLAYING,
                    mPlayer.getCurrentPosition(), 0f);

            mMediaSession.setPlaybackState(mPlayState.build());
        } else if (playbackState == Player.STATE_READY) {
            mPlayState.setState(PlaybackStateCompat.STATE_PAUSED,
                    mPlayer.getCurrentPosition(), 0f);

            mMediaSession.setPlaybackState(mPlayState.build());
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.d(TAG, error.getMessage());
    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }


    @SuppressWarnings("WeakerAccess")
    public String getmVideoURL() {
        return mVideoURL;
    }

    public void setmVideoURL(String mVideoURL) {
        this.mVideoURL = mVideoURL;
    }

    @SuppressWarnings("WeakerAccess")
    public String getmStepInstructions() {
        return mStepInstructions;
    }

    public void setmStepInstructions(String mStepInstructions) {
        this.mStepInstructions = mStepInstructions;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23) {
            initializePlayer();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        mCurrentPlayerPosition = mPlayer.getCurrentPosition();
        mPlayWhenReady = mPlayer.getPlayWhenReady();

        if (Util.SDK_INT <= 23) {
            releaseThePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(Constants.PLAYER_POSITION_KEY, mCurrentPlayerPosition);
        outState.putBoolean(Constants.PLAYER_PLAY_WHEN_READY_KEY, mPlayWhenReady);

    }


    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releaseThePlayer();
        }
    }

    private class VideoCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            mPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mPlayer.seekTo(0);
        }
    }
}