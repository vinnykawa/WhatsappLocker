package com.vinny.whatsapplocker.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vinny.whatsapplocker.MainActivity;
import com.vinny.whatsapplocker.PatternConfirmActivity;
import com.vinny.whatsapplocker.R;
import com.vinny.whatsapplocker.util.PreferenceContract;


/**
 * Created by DENNOH on 10/2/2015.
 */
public class WelcomeFragment extends Fragment {


    public static Fragment getInstance(){
        return new WelcomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE).getBoolean("isFirstRun",true)) {


            View root = inflater.inflate(R.layout.landing_page, container, false);

            return root;
        }else{

         String lock =   PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lock_type", PreferenceContract.LOCK_PIN);

            if(lock.equals(PreferenceContract.LOCK_PIN))
            startActivity(new Intent(getActivity(), MainActivity.class));
            else
                startActivity(new Intent(getActivity(), PatternConfirmActivity.class));

            getActivity().finish();
            return null;
        }

    }
}
