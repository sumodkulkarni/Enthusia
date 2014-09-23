package org.enthusia.app.enthusia.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;

import org.enthusia.app.R;
import org.enthusia.app.Utils;
import org.enthusia.app.enthusia.adapters.EnthusiaNewsAdapter;
import org.enthusia.app.gcm.PushNotificationManager;
import org.enthusia.app.model.PushMessage;

import java.util.ArrayList;

import de.keyboardsurfer.android.widget.crouton.Crouton;

public class EnthusiaNewsFragment extends Fragment {

    private EnthusiaNewsAdapter enthusiaNewsAdapter;
    private ArrayList<PushMessage> messages;
    private int unreadCount = 0;
    private static boolean customAdded = false;

    OnDismissCallback mCallback = new OnDismissCallback() {
        @SuppressWarnings("ResultOfMethodCallIgnored")
        @Override
        public void onDismiss(@NonNull ViewGroup absListView, @NonNull int[] ints) {
            for (int i : ints) {
                messages.get(i).setRead(!messages.get(i).isRead());
                new PushNotificationManager(getActivity()).updateContact(messages.get(i));
                enthusiaNewsAdapter.notifyDataSetChanged();
                if (messages.get(i).isRead())
                    unreadCount--;
                else
                    unreadCount++;
            }
            updateUnread();
            enthusiaNewsAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.enthusia_fragment_news, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PushNotificationManager manager = new PushNotificationManager(getActivity().getApplicationContext());
        messages = manager.getAllMessages();

        if (messages.size() == 0)
            messages = null;


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
            messages.add(new PushMessage(Html.fromHtml(getString(R.string.enthusia_sample_news)), false));
        }

        enthusiaNewsAdapter = new EnthusiaNewsAdapter(getActivity(), messages);

        AlphaInAnimationAdapter adapter = new AlphaInAnimationAdapter(enthusiaNewsAdapter);
        adapter.setAbsListView((DynamicListView) getActivity().findViewById(R.id.enthusia_framgent_news_listnews));
        ((DynamicListView) getActivity().findViewById(R.id.enthusia_framgent_news_listnews)).setAdapter(adapter);
        ((DynamicListView) getActivity().findViewById(R.id.enthusia_framgent_news_listnews)).disableDragAndDrop();
        ((DynamicListView) getActivity().findViewById(R.id.enthusia_framgent_news_listnews)).enableSwipeToDismiss(mCallback);
        getActivity().findViewById(R.id.enthusia_fragment_news_details_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAll();
            }
        });
    }

    @Override
    public void onDestroy() {
        Crouton.cancelAllCroutons();
        super.onDestroy();
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
        if (customAdded)
            return;

        customAdded = true;
        ((TextView) getActivity().findViewById(R.id.enthusia_fragments_news_details_unread_count)).setText(Html.fromHtml("No Messages Received"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new PushNotificationManager(getActivity()).deleteAll();
                } catch (Exception ignore) {}
            }
        }).start();
        enthusiaNewsAdapter.clear();
        messages.add(new PushMessage(Html.fromHtml(getString(R.string.enthusia_sample_news)), false));
        enthusiaNewsAdapter.notifyDataSetChanged();

    }
}
