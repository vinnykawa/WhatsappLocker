package com.vinny.whatsapplocker;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.preference.PreferenceManager;

import android.text.TextUtils;
import android.util.Log;


import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.vinny.whatsapplocker.util.PreferenceContract;

import java.util.List;

/**
 * Created by DENNOH on 9/27/2015.
 */
public class MonitorService extends Service {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
//        startForeground(1,null);
          checkWhatsappStatus();

        //    Toast.makeText(getApplicationContext(),"Service started",Toast.LENGTH_SHORT).show();

/*
      final String str = "";
        Timer timer  =  new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int phonelaunched = 0,phoneclosed =0;
            int phonelaunches = 1;
            @Override
            public void run() {

                ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfo = am.getRunningAppProcesses();

                for (ActivityManager.RunningAppProcessInfo appProcess : runningAppProcessInfo) {
                    //   Log.d(appProcess.processName.toString(), "is running");
                    if (appProcess.processName.equals("com.whatsapp")) {

                        String selectedSecurityOption = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("lock_type", "100");
                        boolean isEnabled = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("isEnabled", false);

                        boolean isLoggedIn = getSharedPreferences("PREFS", Context.MODE_PRIVATE).getBoolean("isLoggedIn", false);

                        if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND  isForeground(getApplicationContext(),runningAppProcessInfo.get(i).processName)) {
                            if (phonelaunched == 0) {
                                phonelaunched = 1;


                                if (isEnabled)
                                    if (!isLoggedIn) {

                                        // isInForeGround = true;
                                        if (selectedSecurityOption.equals(PreferenceContract.LOCK_PIN))
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("isWhatsappRunning", 1));
                                        else if (selectedSecurityOption.equals(PreferenceContract.LOCK_PATTERN))
                                            startActivity(new Intent(getApplicationContext(), PatternConfirmActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("isWhatsappRunning", 1));
                                    }
                                Log.e(str, "dude whatsapp has been launched");
                                //  Toast.makeText(getApplicationContext(), "dude whatsapp has been launched", Toast.LENGTH_LONG).show();

                            } else if (phoneclosed == 1) {
                                phonelaunches++;
                                phoneclosed = 0;

                                if (isEnabled)
                                    if (!isLoggedIn)
                                        if (selectedSecurityOption.equals(PreferenceContract.LOCK_PIN))
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("isWhatsappRunning", 1));
                                        else if (selectedSecurityOption.equals(PreferenceContract.LOCK_PATTERN))
                                            startActivity(new Intent(getApplicationContext(), PatternConfirmActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("isWhatsappRunning", 1));


                                Log.e(String.valueOf(phonelaunches), "dude that was counter, whatsapp opened");

                            } else {



                                phoneclosed = 1;
                               if( appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE){

                                   Log.e(str, "dude whatsapp is just sleeping");


                               }

                                if( appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND){

                                   Log.e(str, "dude whatsapp is in background");

                               }
                                Log.e(str, "dude whatsapp is apparently closed! while in foreground? ");

                            }
                        }else{

                            if(appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND || appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE)
                            getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit().putBoolean("isLoggedIn", false).commit();

                            Log.e(str, "dude whatsapp is of importance "+appProcess.importance);

                        }
                    }
                }
            }

        },500,1000);*/

        return START_STICKY;//tells the OS to restart the service incase it got killed during gc
    }


    PublisherInterstitialAd mPublisherInterstitialAd;

    private void checkWhatsappStatus() {
        final String str = "";
        int phonelaunched = 0,phoneclosed =0;
        int phonelaunches = 1;

        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfo = am.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo appProcess : runningAppProcessInfo) {
            //   Log.d(appProcess.processName.toString(), "is running");



            if (appProcess.processName.equals("com.whatsapp")) {

                String selectedSecurityOption = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("lock_type", "100");
                boolean isEnabled = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("isEnabled", true);

                boolean isLoggedIn = getSharedPreferences("PREFS", Context.MODE_PRIVATE).getBoolean("isLoggedIn", false);

                boolean isPatternSet = !TextUtils.isEmpty(getSharedPreferences("PREFS",MODE_PRIVATE).getString("pattern",null));

                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND /* isForeground(getApplicationContext(),runningAppProcessInfo.get(i).processName)*/) {
                    if (phonelaunched == 0) {
                        phonelaunched = 1;


                        if (isEnabled)
                            if (!isLoggedIn) {

                                // isInForeGround = true;
                                if (selectedSecurityOption.equals(PreferenceContract.LOCK_PIN))
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("isWhatsappRunning", 1));
                                else if (selectedSecurityOption.equals(PreferenceContract.LOCK_PATTERN))
                                    if(isPatternSet)
                                       startActivity(new Intent(getApplicationContext(), PatternConfirmActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("isWhatsappRunning", 1));
                                    else
                                        startActivity(new Intent(getApplicationContext(), PatternSetActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("isWhatsappRunning", 1));

                            }
                        Log.e(str, "dude whatsapp has been launched isEnabled="+isEnabled+"  isLoggedIn="+isLoggedIn);
                        //  Toast.makeText(getApplicationContext(), "dude whatsapp has been launched", Toast.LENGTH_LONG).show();

                    } else if (phoneclosed == 1) {
                        phonelaunches++;
                        phoneclosed = 0;

                        if (isEnabled)
                            if (!isLoggedIn)
                                if (selectedSecurityOption.equals(PreferenceContract.LOCK_PIN))
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("isWhatsappRunning", 1));
                                else if (selectedSecurityOption.equals(PreferenceContract.LOCK_PATTERN))
                                    if(isPatternSet)
                                        startActivity(new Intent(getApplicationContext(), PatternConfirmActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("isWhatsappRunning", 1));
                                    else
                                        startActivity(new Intent(getApplicationContext(), PatternSetActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("isWhatsappRunning", 1));


                        Log.e(String.valueOf(phonelaunches), "dude that was counter, whatsapp opened");

                    } else {



                        phoneclosed = 1;
                        if( appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE){

                            Log.e(str, "dude whatsapp is just sleeping");


                        }

                        if( appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND){

                            Log.e(str, "dude whatsapp is in background");

                        }
                        Log.e(str, "dude whatsapp is apparently closed! while in foreground? ");

                    }
                }else{

                    if(appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND || appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE){
                        getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit().putBoolean("isLoggedIn", false).commit();
                    }

                    if( appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE){

                        Log.e(str, "dude whatsapp is just sleeping");


                    }

                    if( appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND){

                        Log.e(str, "dude whatsapp is in background");

                    }


                    //show advert after user exists whatsapp
                    if((appProcess.importance == 130)  && isLoggedIn ){
                        mPublisherInterstitialAd = new PublisherInterstitialAd(getApplicationContext());
                        mPublisherInterstitialAd.setAdUnitId("ca-app-pub-6831791648891300/8630022473");

                        mPublisherInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                // requestNewInterstitial();
                                int views = getSharedPreferences("PREFS", MODE_PRIVATE).getInt("adViews",0);
                                getSharedPreferences("PREFS", MODE_PRIVATE).edit().putInt("adViews", ++views).commit();
                                getSharedPreferences("PREFS", MODE_PRIVATE).edit().putBoolean("Adflag", false).commit();
                            }

                            @Override
                            public void onAdLoaded() {

                                int views = getSharedPreferences("PREFS", MODE_PRIVATE).getInt("adViews",0);
                                boolean adFlag =  getSharedPreferences("PREFS", MODE_PRIVATE).getBoolean("Adflag",false);

                               if (mPublisherInterstitialAd.isLoaded() && adFlag )
                                    mPublisherInterstitialAd.show();
                            }


                        });

                        requestNewInterstitial();


                    }


                    Log.e(str, "dude whatsapp is of importance "+appProcess.importance);

                }
            }
        }
    }


    private void requestNewInterstitial() {
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder()
                //.addTestDevice("63809C65EB410157659A78F1DE24E292")
                .build();

        mPublisherInterstitialAd.loadAd(adRequest);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void setCount(int i){
        getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit().putInt("count",i).commit();
    }

    public int getCount(){
        return  getSharedPreferences("PREFS", Context.MODE_PRIVATE).getInt("count",0);
    }


    //prevent user from killing this service
    //stopService won,t work if this is enabled
    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
