package com.vinny.whatsapplocker;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;


import java.util.List;

import me.zhanghai.android.patternlock.ConfirmPatternActivity;
import me.zhanghai.android.patternlock.PatternUtils;
import me.zhanghai.android.patternlock.PatternView;


/**
 * Created by DENNOH on 9/30/2015.
 */
public class PatternConfirmActivity extends ConfirmPatternActivity {

    @Override
    protected boolean isStealthModeEnabled() {
        // TODO: Return the value from SharedPreferences.
        return false;
    }

    @Override
    protected boolean isPatternCorrect(List<PatternView.Cell> pattern) {

        // TODO: Get saved pattern sha1.
        String patternSha1 = getSharedPreferences("PREFS",MODE_PRIVATE).getString("pattern",null);

        boolean isCorrect =  TextUtils.equals(PatternUtils.patternToSha1String(pattern), patternSha1);
        getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit().putBoolean("isLoggedIn",isCorrect).commit();

        String TAG = "PatternConfirmmActivity";
        if(isMyServiceRunning(MonitorService.class)) {
            Log.d(TAG, "Service still running no need to restart it");
            // Toast.makeText(getApplicationContext(),"Service still running no need to restart it",Toast.LENGTH_LONG).show();
        } else
        {
            Log.d(TAG,"Service not running restarting");
            //  Toast.makeText(getApplicationContext(),"Service not running restarting",Toast.LENGTH_LONG).show();
           // startService(new Intent(getApplicationContext(), MonitorService.class));
            Intent i = new Intent(this, MonitorService.class);
            PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
            AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            am.cancel(pi); //cancel any existing alarms
            am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime()+1000,
                    2000, pi);//start service every 2 seconds
        }


        if(isCorrect && !getIntent().hasExtra("isWhatsappRunning")){

            startActivity(new Intent(this,Settings.class));
        }


        return isCorrect;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onForgotPassword() {

        startActivity(new Intent(this, ResetPasswordActivity.class));

        // Finish with RESULT_FORGOT_PASSWORD.
        super.onForgotPassword();
    }

    @Override
    public void onBackPressed() {
        //go to homescreen
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
