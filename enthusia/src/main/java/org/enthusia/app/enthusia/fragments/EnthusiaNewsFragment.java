package org.enthusia.app.enthusia.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
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
import org.enthusia.app.parse.helper.NotificationDBManager;
import org.enthusia.app.parse.model.Message;

import java.util.ArrayList;

import de.keyboardsurfer.android.widget.crouton.Crouton;

public class EnthusiaNewsFragment extends Fragment {

    private static final String TAG = "EnthusiaNewsAdapter";
    private EnthusiaNewsAdapter enthusiaNewsAdapter;
    private ArrayList<Message> messages;
    private int unreadCount = 0;
    private static boolean customAdded = false;
    NotificationDBManager db;

    OnDismissCallback mCallback = new OnDismissCallback() {
        @SuppressWarnings("ResultOfMethodCallIgnored")
        @Override
        public void onDismiss(@NonNull ViewGroup absListView, @NonNull int[] ints) {
            for (int i : ints) {
                messages.get(i).setIsRead(true);

                Log.i(TAG, messages.get(i).getMessage());
                Log.i(TAG, String.valueOf(messages.get(i).isRead()));
                Log.i(TAG, "db.update: " + String.valueOf(db.updateMessage(messages.get(i))));

                enthusiaNewsAdapter.notifyDataSetChanged();
                if (unreadCount>0)
                    unreadCount--;
            }
            updateUnread();
            populateNews();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        db = new NotificationDBManager(getActivity().getApplicationContext(), null, null, 1);
        return inflater.inflate(R.layout.enthusia_fragment_news, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //PushNotificationManager manager = new PushNotificationManager(getActivity().getApplicationContext());
        messages = db.getUnreadMessages();
        unreadCount = messages.size();

        ArrayList<Message> allMessages = db.getAllMessages();
        Log.i("Total messages: ", String.valueOf(allMessages.size()));
        for (int i = 0; i<allMessages.size(); i++){
            Log.i("All messages: ", allMessages.get(i).getMessage());
            Log.i("isRead? : ", String.valueOf(allMessages.get(i).isRead()));
        }
        Log.i("CountofUnread messages:", String.valueOf(messages.size()));

        updateUnread();
        populateNews();
    }

    @Override
    public void onDestroy() {
        Crouton.clearCroutonsForActivity(getActivity());
        Crouton.cancelAllCroutons();
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Crouton.clearCroutonsForActivity(getActivity());
        Crouton.cancelAllCroutons();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("print", false);
    }

    private void updateUnread() {

        if (unreadCount == 0) {
            try {
                Utils.showInfo(getActivity(), R.string.enthusia_all_read);
            } catch (Exception ignore) {}
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
                    for (Message message : messages){
                        message.setIsRead(true);
                    }
                } catch (Exception ignore) {}
            }
        }).start();
        enthusiaNewsAdapter.clear();
        messages.add(new Message(getString(R.string.enthusia_sample_news_2), false));
        enthusiaNewsAdapter.notifyDataSetChanged();

    }

    public void populateNews(){
        messages = db.getUnreadMessages();

        if (messages.size() == 0) {
            ((TextView) getActivity().findViewById(R.id.enthusia_fragments_news_details_unread_count)).setText(Html.fromHtml("No Messages Received"));
            messages.add(new Message(getString(R.string.enthusia_sample_news_2), false));
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
}
