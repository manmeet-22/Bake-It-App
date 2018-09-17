package com.manmeet.bakeit.utils;

import android.content.Context;

public interface OnStepClickListener {
    void onStepSelected(Context context, String shortDescription, String description, String videoUrl, String thumbnailUrl);
}
