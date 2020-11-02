package com.vinny.whatsapplocker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vinny.whatsapplocker.MainActivity;
import com.vinny.whatsapplocker.PatternSetActivity;
import com.vinny.whatsapplocker.R;
import com.vinny.whatsapplocker.util.PreferenceContract;


/**
 * Created by DENNOH on 10/2/2015.
 */
public class ChooserFragment extends Fragment {


    public static Fragment getInstance(){
        return new ChooserFragment();
    }

    final int LOCK_PIN = 100;
    final int LOCK_PATTERN  = 101;
     String  userChoice = "100";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.chooserfragment,container,false);

        RadioGroup radios = (RadioGroup) root.findViewById(R.id.radioGroup);



        radios.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_pin:
                        userChoice = PreferenceContract.LOCK_PIN;
                        break;
                    case R.id.radio_pattern:
                        userChoice = PreferenceContract.LOCK_PATTERN;
                        break;
                }
            }
        });

        Button next = (Button) root.findViewById(R.id.btn_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lock ;
                  if(userChoice.equals(PreferenceContract.LOCK_PIN)) {
                      startActivity(new Intent(getActivity(), MainActivity.class));
                      lock = PreferenceContract.LOCK_PIN;
                  } else {
                      lock = PreferenceContract.LOCK_PATTERN;
                      startActivity(new Intent(getActivity(), PatternSetActivity.class));
                  }

                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("lock_type",lock).commit();

                getActivity().finish();

            }
        });




        return root;
        
    }
}
