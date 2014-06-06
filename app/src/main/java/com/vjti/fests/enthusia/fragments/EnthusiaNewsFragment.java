package com.vjti.fests.enthusia.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.nhaarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;
import com.nhaarman.listviewanimations.widget.DynamicListView;
import com.vjti.fests.R;
import com.vjti.fests.Utils;
import com.vjti.fests.enthusia.ui.EnthusiaNewsAdapter;
import com.vjti.fests.model.PushMessage;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import de.keyboardsurfer.android.widget.crouton.Crouton;

public class EnthusiaNewsFragment extends Fragment {

    private EnthusiaNewsAdapter enthusiaNewsAdapter;
    private ArrayList<PushMessage> messages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.enthusia_fragment_news, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            messages = Utils.getPushMessages(getActivity(), Utils.ENTHUSIA);
        } catch (IOException ex) {
            messages = null;
        }

        if (messages == null) {
            Utils.showConfirm(getActivity(), "No News");
        }
        enthusiaNewsAdapter = new EnthusiaNewsAdapter(getActivity(), messages);

        SwipeDismissAdapter adapter = new SwipeDismissAdapter(enthusiaNewsAdapter, new OnDismissCallback() {
            @Override
            public void onDismiss(AbsListView absListView, int[] ints) {
                for (Integer position : ints) {
                    messages.get(position).setRead(!messages.get(position).isRead());
                    enthusiaNewsAdapter.getItem(position).setRead(!enthusiaNewsAdapter.getItem(position).isRead());
                }
            }
        });
        adapter.setAbsListView((DynamicListView) getActivity().findViewById(R.id.enthusia_framgent_news_listnews));
        ((DynamicListView) getActivity().findViewById(R.id.enthusia_framgent_news_listnews)).setAdapter(enthusiaNewsAdapter);
    }

    @Override
    public void onStop() {
        Crouton.cancelAllCroutons();
        try {
            Utils.editPushMessages(getActivity(), Utils.ENTHUSIA, messages);
        } catch (IOException ex) {}
        super.onStop();
    }
}
