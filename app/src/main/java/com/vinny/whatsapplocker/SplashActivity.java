package com.vinny.whatsapplocker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;


import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.viewpagerindicator.LinePageIndicator;
import com.vinny.whatsapplocker.adapters.WelcomePagerAdapter;


/**
 * Created by DENNOH on 10/2/2015.
 */
public class SplashActivity extends FragmentActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.viewpager);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);

        WelcomePagerAdapter adapter = new WelcomePagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);
        LinePageIndicator   mIndicator = (LinePageIndicator)findViewById(R.id.indicator);

        mIndicator.setViewPager(pager);

    }


    @Override
    protected void onResume() {
        super.onResume();

        Intent i = new Intent(this, MonitorService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi); //cancel any existing alarms
        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime()+1000,
                2000, pi);//start service every 2 seconds

    }
}
