package com.vjti.fests.enthusia.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.vjti.fests.R;

public class EnthusiaSponsorsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.enthusia_fragment_sponsors, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ((ImageView) getActivity().findViewById(R.id.enthusia_fragment_sponsors_image)).setImageResource(R.drawable.enthusia_sponsors);
    }
}
