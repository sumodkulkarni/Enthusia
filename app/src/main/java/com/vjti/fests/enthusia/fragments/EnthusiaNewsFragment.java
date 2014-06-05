package com.vjti.fests.enthusia.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vjti.fests.R;

public class EnthusiaNewsFragment extends Fragment {

    private final static String ENTHUSIA_FB_URL = "";
    private final static String ENTHUSIA_TWITTER_URL = "";
    private final static String ENTHUSIA_YOUTUBE_URL = "";
    private final static String ENTHUSIA_WEBSITE_URL = "http://www.enthusia.org";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.enthusia_fragment_news, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
