package com.manmeet.bakeit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.manmeet.bakeit.fragments.VideoFragment;
import com.manmeet.bakeit.utils.ConstantUtility;

public class VideoActvity extends AppCompatActivity {
    private String shortDescription;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Intent intent = getIntent();
        shortDescription = intent.getStringExtra(ConstantUtility.INTENT_SHORT_DESCRIPTION_KEY);
        description = intent.getStringExtra(ConstantUtility.INTENT_DESCRIPTION_KEY);
        videoUrl = intent.getStringExtra(ConstantUtility.INTENT_VIDEO_URL_KEY);
        thumbnailUrl = intent.getStringExtra(ConstantUtility.INTENT_THUMBNAIL_KEY);

        FragmentManager fragmentManager = getSupportFragmentManager();
        VideoFragment videoFragment = new VideoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ConstantUtility.INTENT_SHORT_DESCRIPTION_KEY,shortDescription);
        bundle.putString(ConstantUtility.INTENT_DESCRIPTION_KEY,description);
        bundle.putString(ConstantUtility.INTENT_VIDEO_URL_KEY,videoUrl);
        bundle.putString(ConstantUtility.INTENT_THUMBNAIL_KEY,thumbnailUrl);
        videoFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .add(R.id.video_single_container,videoFragment)
                .commit();
    }
}
