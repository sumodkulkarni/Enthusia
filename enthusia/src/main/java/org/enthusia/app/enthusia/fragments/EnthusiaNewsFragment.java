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

import com.nhaarman.listviewanimations.itemmanipulation.AnimateDismissAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.widget.DynamicListView;
import org.enthusia.app.R;
import org.enthusia.app.Utils;
import org.enthusia.app.enthusia.adapters.EnthusiaNewsAdapter;
import org.enthusia.app.gcm.PushNotificationManager;
import org.enthusia.app.model.PushMessage;

import java.util.ArrayList;

import de.keyboardsurfer.android.widget.crouton.Crouton;

public class EnthusiaNewsFragment extends Fragment {

    private EnthusiaNewsAdapter enthusiaNewsAdapter;
    private AnimateDismissAdapter dismissAdapter;
    private PushNotificationManager manager;
    private ArrayList<PushMessage> messages;
    private int unreadCount = 0;
    private static boolean customAdded = false;

    OnDismissCallback mCallback = new OnDismissCallback() {
        @SuppressWarnings("ResultOfMethodCallIgnored")
        @Override
        public void onDismiss(AbsListView absListView, int[] ints) {
            for (int i : ints) {
                try {
                    manager.deleteMessage(enthusiaNewsAdapter.get(i));
                    enthusiaNewsAdapter.remove(i);
                } catch (IndexOutOfBoundsException ignore) {}
            }
            if (messages.size() == 0) {
                messages.add(new PushMessage(Html.fromHtml(getString(R.string.enthusia_sample_news)), false));
                enthusiaNewsAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.enthusia_fragment_news, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        manager = new PushNotificationManager(getActivity().getApplicationContext());
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
        SwipeDismissAdapter adapter = new SwipeDismissAdapter(enthusiaNewsAdapter, new OnDismissCallback() {
            @Override
            public void onDismiss(AbsListView absListView, int[] ints) {
                for (Integer position : ints) {
                    if (messages.get(position).isRead())
                        unreadCount++;
                    else
                        unreadCount--;
                    messages.get(position).setRead(!messages.get(position).isRead());
                    manager.updateContact(messages.get(position));
                    enthusiaNewsAdapter.notifyDataSetChanged();
                    updateUnread();
                }
            }
        });
        SwingBottomInAnimationAdapter animation = new SwingBottomInAnimationAdapter(enthusiaNewsAdapter);
        animation.setAbsListView((DynamicListView) getActivity().findViewById(R.id.enthusia_framgent_news_listnews));
        adapter.setAbsListView((DynamicListView) getActivity().findViewById(R.id.enthusia_framgent_news_listnews));

        dismissAdapter = new AnimateDismissAdapter(adapter, mCallback);
        dismissAdapter.setAbsListView((DynamicListView) getActivity().findViewById(R.id.enthusia_framgent_news_listnews));
        ((DynamicListView) getActivity().findViewById(R.id.enthusia_framgent_news_listnews)).setAdapter(dismissAdapter);
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
        for (int i=0; i < messages.size(); i++)
            dismissAdapter.animateDismiss(i);

    }
}
