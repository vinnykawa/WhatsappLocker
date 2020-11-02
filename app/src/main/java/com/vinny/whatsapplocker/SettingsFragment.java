package com.vinny.whatsapplocker;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.vinny.whatsapplocker.util.HomeWatcher;
import com.vinny.whatsapplocker.util.OnHomePressedListener;
import com.vinny.whatsapplocker.util.PreferenceContract;
import com.vinny.whatsapplocker.util.PreferenceUtils;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by DENNOH on 9/30/2015.
 */
public class SettingsFragment extends PreferenceFragment  implements SharedPreferences.OnSharedPreferenceChangeListener {

    PublisherInterstitialAd mPublisherInterstitialAd;
    HomeWatcher mHomeWatcher;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        mHomeWatcher   = new HomeWatcher(getActivity());
        mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {

                getActivity().finish();
            }

            @Override
            public void onHomeLongPressed() {
            }
        });
        mHomeWatcher.startWatch();

        mPublisherInterstitialAd = new PublisherInterstitialAd(getActivity());
        mPublisherInterstitialAd.setAdUnitId("ca-app-pub-6831791648891300/8630022473");

        mPublisherInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });

        requestNewInterstitial();

        getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit().putBoolean("isFirstRun", false).commit();

        Preference feedback = (Preference) findPreference("pref_feedback");
        feedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                pressedFeedBack();
                return true;
            }
        });





        Preference rateUs = (Preference) findPreference("pref_rate");
        rateUs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                rateOnPlaystore();
                return false;
            }
        });



        Preference pin = (Preference) findPreference("pin");

       final String selectedSecurityOption = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lock_type", "100");


        String title = selectedSecurityOption.equals(PreferenceContract.LOCK_PIN)?"Change Pin":"Change Pattern";
        String summary = selectedSecurityOption.equals(PreferenceContract.LOCK_PIN)?"Allows you to reset your pin":"Allows you to reset your pattern";


        pin.setTitle(title);
        pin.setSummary(summary);
        pin.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //  EditText contentView = new EditText(getActivity());
                if (selectedSecurityOption.equals(PreferenceContract.LOCK_PIN))
                    changePIN();
                else
                    startActivity(new Intent(getActivity(), PatternSetActivity.class));
                return false;
            }
        });


        Preference more = (Preference) findPreference("more");
        more.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //  EditText contentView = new EditText(getActivity());
                if (mPublisherInterstitialAd.isLoaded())
                    mPublisherInterstitialAd.show();
                else
                Toast.makeText(getActivity(),"Loading...",Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        Preference lock_type = (Preference) findPreference("lock_type");
        lock_type.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                String choice = selectedSecurityOption;

                boolean isPatternSet = !TextUtils.isEmpty(getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE).getString("pattern",null));
                boolean isPinSet = !TextUtils.isEmpty(getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE).getString("legitPIN",null));

                //check if pattern is set
               if(choice.equals(PreferenceContract.LOCK_PIN))
               if(!isPatternSet)
                   startActivity(new Intent(getActivity(), PatternSetActivity.class));

                //check if pin is set corredctly
                if(choice.equals(PreferenceContract.LOCK_PATTERN))
                    if(!isPinSet)
                        startActivity(new Intent(getActivity(), MainActivity.class).putExtra("isSettingsActive",1));

                getActivity().recreate();

                return true;
            }
        });

    }


    private void requestNewInterstitial() {
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder()
              //  .addTestDevice("63809C65EB410157659A78F1DE24E292")
                .build();

        mPublisherInterstitialAd.loadAd(adRequest);
    }


    private void changeLockType(){

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mHomeWatcher != null)
        mHomeWatcher.stopWatch();
    }

    private void changePIN() {
        View view = View.inflate(getActivity(),R.layout.dialog_changepin,null);

       final  EditText edP1 = (EditText) view.findViewById(R.id.ed_pin1);
       final EditText edP2 = (EditText) view.findViewById(R.id.ed_pin2);
        final   MaterialDialog mMaterialDialog = new MaterialDialog(getActivity()).setContentView(view);
        //   mMaterialDialog.setBackgroundResource(R.color.primary);
        mMaterialDialog.setTitle("Change Security PIN");
        mMaterialDialog.setPositiveButton("Done", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pin1 = edP1.getText().toString();
                String pin2 = edP2.getText().toString();


                if(TextUtils.isEmpty(pin1))
                    edP1.setError("Please enter new pin");
                 if(TextUtils.isEmpty(pin2))
                     edP2.setError("Please re-enter new pin");

                if(!pin1.equals(pin2) && !TextUtils.isEmpty(pin1)){
                    edP2.setError("Pin codes do not match");
                }else{

                    Toast.makeText(getActivity(),"Security PIN successfully changed!",Toast.LENGTH_SHORT).show();

                    getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit().putString("legitPIN", pin1).commit();
                    mMaterialDialog.dismiss();
                }

            }
        });

        mMaterialDialog.setNegativeButton("Cancel", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        });
        mMaterialDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();

        PreferenceUtils.getPreferences(getActivity())
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        PreferenceUtils.getPreferences(getActivity())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case PreferenceContract.KEY_THEME:
                getActivity().recreate();
        }
    }

    private void pressedFeedBack() {
        Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback on Whatsapp Locker");
        //intent.putExtra(Intent.EXTRA_TEXT, "Body of email");
        intent.setData(Uri.parse("mailto:thejellocompany@gmail.com")); // or just "mailto:" for blank
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        startActivity(intent);
    }

    private void rateOnPlaystore() {
        Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id="
                            + getActivity().getPackageName())));
        }
        //getSharedPreferences("PREFS", MODE_PRIVATE).edit()
        //  .putBoolean("isWilling", false).commit();
    }




}
