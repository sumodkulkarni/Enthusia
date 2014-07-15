package org.enthusia.app.enthusia.fragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import org.enthusia.app.R;
import org.enthusia.app.ui.ExpandableTextView;

public class EnthusiaAboutFragment extends Fragment implements View.OnClickListener {

    private boolean isExpanded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.enthusia_fragment_about, container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((ExpandableTextView) getActivity().findViewById(R.id.enthusia_fragment_about_content)).setText(Html.fromHtml(getString(R.string.about_us)));

        getActivity().findViewById(R.id.enthusia_fragment_about_showmore_image).setOnClickListener(this);

        getActivity().findViewById(R.id.r_fb).setOnClickListener(developerURLS);
        getActivity().findViewById(R.id.r_twit).setOnClickListener(developerURLS);
        getActivity().findViewById(R.id.r_plus).setOnClickListener(developerURLS);
    }

    @Override
    public void onClick(View v) {
        AnimatorSet set = new AnimatorSet();
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.play(getTextViewAnimation()).with(getImageViewAnimation());
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                getActivity().findViewById(R.id.enthusia_fragment_about_showmore_image).setOnClickListener(null);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isExpanded = !isExpanded;
                getActivity().findViewById(R.id.enthusia_fragment_about_showmore_image).setOnClickListener(EnthusiaAboutFragment.this);
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        set.start();
    }

    private ObjectAnimator getTextViewAnimation() {
        ObjectAnimator animator =  ObjectAnimator.ofFloat(getActivity().findViewById(R.id.enthusia_fragment_about_content),
                "maxLines",
                3, 30);

        if (isExpanded) {
            animator.setFloatValues(30, 3);
        }

        return animator;
    }

    private ObjectAnimator getImageViewAnimation() {
        return ObjectAnimator.ofFloat(getActivity().findViewById(R.id.enthusia_fragment_about_showmore_image),
                View.ROTATION,
                getActivity().findViewById(R.id.enthusia_fragment_about_showmore_image).getRotation(),
                getActivity().findViewById(R.id.enthusia_fragment_about_showmore_image).getRotation() + 180);
    }

    private View.OnClickListener developerURLS = new View.OnClickListener() {

        private String FB_ID = "100001708505410";
        private String FB_URL = "https://www.facebook.com/TheAdirah47";
        private String TWIT_ID = "2258055728";
        private String TWIT_URL = "https://twitter.com/RahulSIyer";
        private String PLUS_URL = "https://plus.google.com/u/0/+RahulIyer95";

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.r_fb:
                    try {
                        getActivity().getPackageManager().getPackageInfo("com.facebook.katana", 0);
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/" + FB_ID));
                    } catch (Exception ex) {
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(FB_URL));
                    }
                    break;

                case R.id.r_twit:
                    try {
                        getActivity().getPackageManager().getPackageInfo("com.twitter.android", 0);
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=" + TWIT_ID));
                    } catch (Exception ex) {
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(TWIT_URL));
                    }
                    break;

                case R.id.r_plus:
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(PLUS_URL));
                    break;
            }

            if (intent != null) {
                startActivity(intent);
            }
        }

    };

}
