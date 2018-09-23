package com.manmeet.bakeit.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.manmeet.bakeit.R;
import com.manmeet.bakeit.utils.ConstantUtility;
import com.manmeet.bakeit.utils.NetworkUtility;
import com.squareup.picasso.Picasso;

public class VideoFragment extends Fragment implements ExoPlayer.EventListener {
    public static final String TAG = VideoFragment.class.getSimpleName();
    long positionPlayer;
    boolean playWhenReady;
    private TextView videoName;
    private SimpleExoPlayerView simpleExoPlayerView;
    private TextView videoDescription;
    private ImageView videoPlaceholder;
    private String shortDescription;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;
    private MediaSessionCompat mediaSessionCompat;
    private PlaybackStateCompat.Builder playbackBuilder;
    private Uri videoUri;
    private SimpleExoPlayer simpleExoPlayer;
    private ProgressBar mProgressBar;
    private TextView mErrorMessage;
    private LinearLayout videoLinearLayout;

    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            shortDescription = bundle.getString(ConstantUtility.INTENT_SHORT_DESCRIPTION_KEY);
            description = bundle.getString(ConstantUtility.INTENT_DESCRIPTION_KEY);
            videoUrl = bundle.getString(ConstantUtility.INTENT_VIDEO_URL_KEY);
            thumbnailUrl = bundle.getString(ConstantUtility.INTENT_THUMBNAIL_KEY);
            Log.i("VideoFragment", "onCreateView: " + shortDescription);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        simpleExoPlayerView = view.findViewById(R.id.video_exo_player);
        videoDescription = view.findViewById(R.id.video_description);
        videoPlaceholder = view.findViewById(R.id.video_placeholder);
        videoName = view.findViewById(R.id.video_name);
        mErrorMessage = view.findViewById(R.id.video_no_network);
        videoLinearLayout = view.findViewById(R.id.video_linearlayout);
        mProgressBar = view.findViewById(R.id.video_progressbar);
        mProgressBar.setVisibility(View.VISIBLE);

        if (savedInstanceState != null) {
            int placeHolderVisibility = savedInstanceState.getInt(ConstantUtility.KEY_VISIBILITY_VIDEO_PLACEHOLDER);
            videoPlaceholder.setVisibility(placeHolderVisibility);
            int visibilityExo = savedInstanceState.getInt(ConstantUtility.KEY_VISIBILITY_EXO_PLAYER);
            simpleExoPlayerView.setVisibility(visibilityExo);
            //get play when ready boolean
            playWhenReady = savedInstanceState.getBoolean(ConstantUtility.BOOLEAN_PLAY_WHEN_READY);
        }
        if (NetworkUtility.isNetworkConnected(view.getContext())) {
            mErrorMessage.setVisibility(View.GONE);
            videoLinearLayout.setVisibility(View.VISIBLE);

            //Log.d(TAG, "URL : " + url);
            videoName.setText(shortDescription);
            if (videoUrl != null) {
                if (videoUrl.equals("") && !thumbnailUrl.equals("")) {
                    videoUrl = thumbnailUrl;
                }
                if (videoUrl.equals("")) {
                    Log.d(TAG, "EMPTY URL");
                    simpleExoPlayerView.setVisibility(View.GONE);
                    videoPlaceholder.setVisibility(View.VISIBLE);
                    if (!thumbnailUrl.equals("")) {
                        //Load thumbnail if present
                        Picasso.with(getActivity()).load(thumbnailUrl).into(videoPlaceholder);
                    }
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        videoDescription.setText(description);
                    } else {
                        hideUI();
                        simpleExoPlayerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                        simpleExoPlayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                    }
                } else {
                    if (savedInstanceState != null) {
                        //resuming by seeking to the last position
                        positionPlayer = savedInstanceState.getLong(ConstantUtility.MEDIA_POSITION);
                    }
                    videoPlaceholder.setVisibility(View.GONE);
                    initializeMedia();
                    Log.d(TAG, "URL " + videoUrl);
                    initializePlayer(Uri.parse(videoUrl));
                    videoUri = Uri.parse(videoUrl);
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        videoDescription.setText(description);
                    } else {
                        hideUI();
                        simpleExoPlayerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                        simpleExoPlayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                    }
                }
            } else {
                simpleExoPlayerView.setVisibility(View.GONE);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    videoDescription.setText(description);
                } else {
                    hideUI();
                    simpleExoPlayerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                    simpleExoPlayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                }
            }
        } else {
            mErrorMessage.setVisibility(View.VISIBLE);
            videoLinearLayout.setVisibility(View.GONE);
        }
        mProgressBar.setVisibility(View.GONE);
        return view;
    }

    private void initializeMedia() {
        mediaSessionCompat = new MediaSessionCompat(getActivity(), TAG);
        mediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSessionCompat.setMediaButtonReceiver(null);
        playbackBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mediaSessionCompat.setPlaybackState(playbackBuilder.build());
        mediaSessionCompat.setCallback(new SessionCallBacks());
        mediaSessionCompat.setActive(true);
    }

    private void hideUI() {
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            //Use Google's "LeanBack" mode to get fullscreen in landscape
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }

    private void initializePlayer(Uri mediaUri) {
        if (simpleExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance
                    (getActivity(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(simpleExoPlayer);
            simpleExoPlayer.addListener(this);
            String userAgent = Util.getUserAgent(getActivity(),
                    getActivity().getString(R.string.application_name_exo_player));
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                    new DefaultDataSourceFactory(getActivity(), userAgent),
                    new DefaultExtractorsFactory(), null, null);
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ConstantUtility.KEY_VISIBILITY_VIDEO_PLACEHOLDER, videoPlaceholder.getVisibility());
        outState.putInt(ConstantUtility.KEY_VISIBILITY_EXO_PLAYER, simpleExoPlayerView.getVisibility());
        //Saving current Position before rotation
        outState.putLong(ConstantUtility.MEDIA_POSITION, positionPlayer);
        //for preserving state of exoplayer
        outState.putBoolean(ConstantUtility.BOOLEAN_PLAY_WHEN_READY, playWhenReady);
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        if (mediaSessionCompat != null) {
            mediaSessionCompat.setActive(false);
        }
    }

    @Override
    public void onPause() {
        //releasing in Pause and saving current position for resuming
        super.onPause();
        if (simpleExoPlayer != null) {
            positionPlayer = simpleExoPlayer.getCurrentPosition();
            //getting play when ready so that player can be properly store state on rotation
            playWhenReady = simpleExoPlayer.getPlayWhenReady();
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (simpleExoPlayer != null) {
            //resuming properly
            simpleExoPlayer.setPlayWhenReady(playWhenReady);
            simpleExoPlayer.seekTo(positionPlayer);
        } else {
            //Correctly initialize and play properly fromm seekTo function
            initializeMedia();
            initializePlayer(videoUri);
            simpleExoPlayer.setPlayWhenReady(playWhenReady);
            simpleExoPlayer.seekTo(positionPlayer);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == ExoPlayer.STATE_READY && playWhenReady) {
            playbackBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    simpleExoPlayer.getCurrentPosition(), 1f);
        } else if (playbackState == ExoPlayer.STATE_READY) {
            playbackBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    simpleExoPlayer.getCurrentPosition(), 1f);
        }
        mediaSessionCompat.setPlaybackState(playbackBuilder.build());
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

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

    private class SessionCallBacks extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            super.onPlay();
            simpleExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            super.onPause();
            simpleExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            simpleExoPlayer.seekTo(0);
        }
    }
}
