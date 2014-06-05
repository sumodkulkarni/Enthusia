package com.vjti.fests.enthusia.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vjti.fests.R;
import com.vjti.fests.enthusia.model.EnthusiaCommittee;
import com.vjti.fests.enthusia.ui.EnthusiaStickyHeaderAdapter;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class EnthusiaCommitteeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.enthusia_fragment_committee, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ArrayList<EnthusiaCommittee> mCommittee = new ArrayList<EnthusiaCommittee>();

        mCommittee.add(new EnthusiaCommittee("Arundhati Karanth: +919920930194", 0));
        mCommittee.add(new EnthusiaCommittee("Ameya Phansalkar: +918082424061", 1));
        mCommittee.add(new EnthusiaCommittee("Krunal Maskai: +919833897888", 2));
        mCommittee.add(new EnthusiaCommittee("Shubham Thambi: +919860078876", 2));
        mCommittee.add(new EnthusiaCommittee("Devika Bhalerao: +919920731384", 3));
        mCommittee.add(new EnthusiaCommittee("Nandini Mazumdar: +919833780021", 3));
        mCommittee.add(new EnthusiaCommittee("Varad Deolankar: +919773162774", 4));
        mCommittee.add(new EnthusiaCommittee("Sarvesh Dhage: +919819004572", 4));
        mCommittee.add(new EnthusiaCommittee("Akshay Pawar: +917588516680", 5));
        mCommittee.add(new EnthusiaCommittee("Nikhil Shetty: +918087875058", 5));
        mCommittee.add(new EnthusiaCommittee("Tejal Diggikar: +919930522291", 5));
        mCommittee.add(new EnthusiaCommittee("Susmita Samleti: +917588579460", 5));
        mCommittee.add(new EnthusiaCommittee("Aditya Bajaj: +919820728353", 6));
        mCommittee.add(new EnthusiaCommittee("Koustubh Bhandari: +919923641510", 7));
        mCommittee.add(new EnthusiaCommittee("Miloni Gada: +919819996361", 8));
        mCommittee.add(new EnthusiaCommittee("Rahul Iyer: +919967053733", 8));
        mCommittee.add(new EnthusiaCommittee("Pankaj Palwe: +919637081559", 9));
        mCommittee.add(new EnthusiaCommittee("Karan Nair: +919004656479", 10));
        mCommittee.add(new EnthusiaCommittee("Anish Shah: +919821735835", 11));
        mCommittee.add(new EnthusiaCommittee("Eashan Kadam: +919920875281", 11));
        mCommittee.add(new EnthusiaCommittee("Rohit Dawra: +918805212199", 11));
        mCommittee.add(new EnthusiaCommittee("Adithya Swami: ", 12));

        ((StickyListHeadersListView) getActivity().findViewById(R.id.enthusia_fragment_committee_list)).setAdapter(new EnthusiaStickyHeaderAdapter(
                getActivity(), mCommittee, getResources().getStringArray(R.array.enthusia_committe_posts)));

        mCommittee = null;
    }
}
