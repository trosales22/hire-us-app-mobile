package com.trosales.hireusapp.fragments;


import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trosales.hireusapp.R;

import org.jetbrains.annotations.NotNull;

public class UnavailableDatesFragment extends BottomSheetDialogFragment {
    public static UnavailableDatesFragment getInstance() {
        return new UnavailableDatesFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_unavailable_dates, container, false);
    }
}
