package org.enthusia.app.enthusia.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.nhaarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;
import com.nhaarman.listviewanimations.widget.DynamicListView;
import org.enthusia.app.R;
import org.enthusia.app.Utils;
import org.enthusia.app.enthusia.adapters.EnthusiaNewsAdapter;
import org.enthusia.app.model.PushMessage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.keyboardsurfer.android.widget.crouton.Crouton;

public class EnthusiaNewsFragment extends Fragment {

    private EnthusiaNewsAdapter enthusiaNewsAdapter;
    private ArrayList<PushMessage> messages;
    private int unreadCount = 0;
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

            for (PushMessage message: messages) {
                if (!message.isRead()) {
                    unreadCount++;
                }
            }

            updateUnread();
            customAdded = false;
        } else {
            customAdded = true;
            ((TextView) getActivity().findViewById(R.id.enthusia_fragments_news_details_unread_count)).setText(Html.fromHtml("No Messages Received"));
            messages = new ArrayList<PushMessage>();
            messages.add(new PushMessage(Html.fromHtml(getString(R.string.enthusia_sample_news))));
        }

        enthusiaNewsAdapter = new EnthusiaNewsAdapter(getActivity(), messages);
        SwipeDismissAdapter adapter = new SwipeDismissAdapter(enthusiaNewsAdapter, new OnDismissCallback() {
            @Override
            public void onDismiss(AbsListView absListView, int[] ints) {
                for (Integer position : ints) {
                    if (messages.get(position).isRead())
                        unreadCount++;
                    else
                        unreadCount--;
                    messages.get(position).setRead(!messages.get(position).isRead());
                    enthusiaNewsAdapter.notifyDataSetChanged();
                    updateUnread();
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
        getActivity().findViewById(R.id.enthusia_fragment_news_details_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAll();
            }
        });
    }

    @Override
    public void onStop() {
        Crouton.cancelAllCroutons();
        try {
            if (!customAdded)
                Utils.editPushMessages(getActivity(), Utils.ENTHUSIA, messages);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        super.onStop();
    }

    private void updateUnread() {

        if (customAdded)
            return;

        if (unreadCount == 0) {
            Utils.showConfirm(getActivity(), R.string.enthusia_all_read);
            ((TextView) getActivity().findViewById(R.id.enthusia_fragments_news_details_unread_count)).setText(Html.fromHtml(getString(R.string.enthusia_all_messages_read)).toString());
        } else {
            ((TextView) getActivity().findViewById(R.id.enthusia_fragments_news_details_unread_count)).setText(Html.fromHtml(String.format(getString(R.string.enthusia_unread_count), unreadCount, (unreadCount > 1 ? "s" : "") )).toString());
        }
    }

    private void clearAll() {
        customAdded = true;
        ((TextView) getActivity().findViewById(R.id.enthusia_fragments_news_details_unread_count)).setText(Html.fromHtml("No Messages Received"));
        messages.clear();
        messages.add(new PushMessage(Html.fromHtml(getString(R.string.enthusia_sample_news)).toString()));
        enthusiaNewsAdapter.notifyDataSetChanged();
        new File(getActivity().getCacheDir(), Utils.ENTHUSIA).delete();

    }
}
