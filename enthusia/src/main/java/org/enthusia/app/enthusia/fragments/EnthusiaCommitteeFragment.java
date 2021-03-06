package org.enthusia.app.enthusia.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhaarman.listviewanimations.appearance.StickyListHeadersAdapterDecorator;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

import org.enthusia.app.R;
import org.enthusia.app.enthusia.adapters.EnthusiaStickyHeaderAdapter;
import org.enthusia.app.enthusia.model.EnthusiaCommittee;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class EnthusiaCommitteeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.enthusia_fragment_committee, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ArrayList<EnthusiaCommittee> mCommittee = new ArrayList<>();

        mCommittee.add(new EnthusiaCommittee("Milay Haria: +919029410964", 0));
        mCommittee.add(new EnthusiaCommittee("Shubham Gawande: +918698606142", 1));
        mCommittee.add(new EnthusiaCommittee("Mayur Deshmukh: +918408000747", 2));
        mCommittee.add(new EnthusiaCommittee("Swastik Mohapatra: +919029272054", 2));
        mCommittee.add(new EnthusiaCommittee("Dhruv Turakhia: +919664016227", 3));
        mCommittee.add(new EnthusiaCommittee("Manan Doshi: +919167143210", 3));
        mCommittee.add(new EnthusiaCommittee("Yash Beri: +919833128502", 4));
        mCommittee.add(new EnthusiaCommittee("Smitesh Modak: +919769604373", 4));
        mCommittee.add(new EnthusiaCommittee("Deep Gala: +918097157686", 5));
        mCommittee.add(new EnthusiaCommittee("Abraham George: +919619823690", 5));
        mCommittee.add(new EnthusiaCommittee("Anmol Chaturvedi: +919892363138", 5));
        mCommittee.add(new EnthusiaCommittee("Anklesh Shekokar: +917387438280", 6));
        mCommittee.add(new EnthusiaCommittee("Amit Lokare: +917350940343", 6));
        mCommittee.add(new EnthusiaCommittee("Arvind Nair: +919167112743", 6));
        mCommittee.add(new EnthusiaCommittee("Sheetal Kaamthewad: +918451869772", 6));
        mCommittee.add(new EnthusiaCommittee("Ankita Borhade: +918286334264", 6));
        mCommittee.add(new EnthusiaCommittee("Ekansh Mishra: +918652762018", 7));
        mCommittee.add(new EnthusiaCommittee("Rushikesh Mehtre: +917666068036", 7));
        mCommittee.add(new EnthusiaCommittee("Ruturaj Vyawhare: +919757149440", 8));
        mCommittee.add(new EnthusiaCommittee("Ankit Merchant: +919820915740", 9));
        mCommittee.add(new EnthusiaCommittee("Viraj Sanghvi: +919819584725", 9));
        mCommittee.add(new EnthusiaCommittee("Rutuja Karampure: +918097311711", 9));
        mCommittee.add(new EnthusiaCommittee("Shivraj Pawar: +919699902679", 9));
        mCommittee.add(new EnthusiaCommittee("Ankita Bhondare: +918286334264", 9));
        mCommittee.add(new EnthusiaCommittee("Sanket Giri: +918446977134", 10));
        mCommittee.add(new EnthusiaCommittee("Satyam Mali: +919503251469", 11));
        mCommittee.add(new EnthusiaCommittee("Sunil Mali: +919699693231", 11));
        mCommittee.add(new EnthusiaCommittee("Sagar Sable: +919766979737", 11));
        mCommittee.add(new EnthusiaCommittee("Sayyad Sirhan: +919730228606", 12));
        mCommittee.add(new EnthusiaCommittee("Ganesh Chainpure: +917738274310", 12));
        mCommittee.add(new EnthusiaCommittee("Riddhish Shah: +919664541489", 13));
        mCommittee.add(new EnthusiaCommittee("Satyam Mali: +919503251469", 13));
        mCommittee.add(new EnthusiaCommittee("Sahaj Gandhi: +919920305544", 14));
        mCommittee.add(new EnthusiaCommittee(" : ", 14));

        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(new EnthusiaStickyHeaderAdapter(
                getActivity(), mCommittee, getResources().getStringArray(R.array.enthusia_committe_posts)));
        StickyListHeadersAdapterDecorator decorator = new StickyListHeadersAdapterDecorator(animationAdapter);
        decorator.setStickyListHeadersListView(((StickyListHeadersListView) getActivity().findViewById(R.id.enthusia_fragment_committee_list)));
        ((StickyListHeadersListView) getActivity().findViewById(R.id.enthusia_fragment_committee_list)).setAdapter(decorator);

    }
}
