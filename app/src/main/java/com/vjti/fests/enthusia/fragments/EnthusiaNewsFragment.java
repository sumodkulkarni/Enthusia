package com.vjti.fests.enthusia.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

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
    private static boolean customAdded = false;

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

        if (messages != null) {
            boolean allRead = true;
            for (PushMessage message: messages) {
                if (!message.isRead()) {
                    allRead = false;
                    break;
                }
            }
            if (allRead) {
                Utils.showConfirm(getActivity(), R.string.enthusia_all_read);
            }
            customAdded = false;
        } else {
            customAdded = true;
            messages = new ArrayList<PushMessage>();
            messages.add(new PushMessage(Html.fromHtml(getString(R.string.enthusia_sample_news)).toString()));
        }

        enthusiaNewsAdapter = new EnthusiaNewsAdapter(getActivity(), messages);
        SwipeDismissAdapter adapter = new SwipeDismissAdapter(enthusiaNewsAdapter, new OnDismissCallback() {
            @Override
            public void onDismiss(AbsListView absListView, int[] ints) {
                for (Integer position : ints) {
                    messages.get(position).setRead(!messages.get(position).isRead());
                    enthusiaNewsAdapter.notifyDataSetChanged();
                }
            }
        });
        adapter.setAbsListView((DynamicListView) getActivity().findViewById(R.id.enthusia_framgent_news_listnews));
        ((DynamicListView) getActivity().findViewById(R.id.enthusia_framgent_news_listnews)).setAdapter(enthusiaNewsAdapter);
        ((DynamicListView) getActivity().findViewById(R.id.enthusia_framgent_news_listnews)).setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return true;
            }
        });
    }

    @Override
    public void onStop() {
        Crouton.cancelAllCroutons();
        try {
            if (!customAdded)
                Utils.editPushMessages(getActivity(), Utils.ENTHUSIA, messages);
        } catch (IOException ex) {}
        super.onStop();
    }
}
