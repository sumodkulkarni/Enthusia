package org.enthusia.app.enthusia.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.enthusia.app.R;
import org.enthusia.app.enthusia.fragments.dialog.EnthusiaPointsTableDialog;

public class EnthusiaIntraFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.enthusia_fragment_intra, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getActivity().findViewById(R.id.enthusia_fragment_intra_button_department_heads).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDepartMentHeads();
            }
        });
        getActivity().findViewById(R.id.enthusia_fragment_intra_button_points_table).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPointsTable();
            }
        });
    }

    private void showPointsTable() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new EnthusiaPointsTableDialog().show(getActivity().getSupportFragmentManager(), "pointstable");
            }
        });
    }

    private void showDepartMentHeads() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit,
                                            R.anim.fragment_enter, R.anim.fragment_exit)
                        .replace(R.id.enthusia_start_fragment_container, new EnthusiaDepartmentHeadsFragment())
                        .addToBackStack("departmentHead")
                        .commit();
            }
        });
    }
}
