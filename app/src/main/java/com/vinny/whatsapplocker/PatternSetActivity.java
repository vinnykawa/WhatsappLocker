package com.vinny.whatsapplocker;

import android.content.Context;
import android.content.Intent;

import java.util.List;

import me.zhanghai.android.patternlock.PatternUtils;
import me.zhanghai.android.patternlock.PatternView;
import me.zhanghai.android.patternlock.SetPatternActivity;


/**
 * Created by DENNOH on 9/30/2015.
 */
public class PatternSetActivity extends SetPatternActivity {

    @Override
    protected void onSetPattern(List<PatternView.Cell> pattern) {
        String patternSha1 = PatternUtils.patternToSha1String(pattern);
        // TODO: Save patternSha1 in SharedPreferences.
        getSharedPreferences("PREFS",MODE_PRIVATE).edit().putString("pattern",patternSha1).commit();
        getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit().putBoolean("isLoggedIn", true).commit();


        if(getSharedPreferences("PREFS",MODE_PRIVATE).getBoolean("isFirstRun",true)){
            //getSharedPreferences("PREFS", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).commit();
          //  startService(new Intent(this, MonitorService.class));//start service
            startActivity(new Intent(this, Settings.class));
            finish();
        }

    }
}
