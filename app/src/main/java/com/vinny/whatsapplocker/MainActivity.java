package com.vinny.whatsapplocker;


import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


import android.inputmethodservice.Keyboard;
import android.os.SystemClock;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;


import android.view.View;


import android.view.inputmethod.EditorInfo;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.davidmiguel.numberkeyboard.NumberKeyboard;
import com.davidmiguel.numberkeyboard.NumberKeyboardListener;
import com.vinny.whatsapplocker.util.PreferenceContract;

public class MainActivity extends AppCompatActivity {

    private  String TAG = this.getClass().getSimpleName();
    CustomKeyboardView mKeyboardView;
    EditText mTargetView;
    private Keyboard mKeyboard;

    private TextView title;

    private boolean isFirstRun ;

   private boolean confirmPIN = false;

   private NumberKeyboard numberKeyboard;

    int lastDiff = 0;
    volatile boolean flag = false;
    volatile int flag2 = 0;

    private String legitPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // android.support.v7.app.ActionBar actionBar = getSupportActionBar();
      //  actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#388E3C")));



        isFirstRun = getSharedPrefs().getBoolean("isFirstRun",true);



        numberKeyboard = findViewById(R.id.numkeyboard);
        numberKeyboard.showLeftAuxButton();
        numberKeyboard.showRightAuxButton();
        numberKeyboard.setListener(new NumberKeyboardListener() {
            @Override
            public void onNumberClicked(int i) {

            }

            @Override
            public void onLeftAuxButtonClicked() {

                Toast.makeText(MainActivity.this,"Enter pressed",Toast.LENGTH_SHORT).show();

                Log.i("MainActivity", "Enter pressed");

                String pin = mTargetView.getText().toString();


                if (TextUtils.isEmpty(pin))
                    mTargetView.setError("PIN required!");

                if (pin.length() > 0 && pin.length() < 4)
                    mTargetView.setError("Your pin should be 4 digits long");
                else {

                    if (!isFirstRun) {
                        String savedPin = getSharedPrefs().getString("legitPIN", "");


                        if (savedPin.equals(pin)) {

                            getSharedPrefs().edit().putBoolean("isLoggedIn", true).commit();

                            if (!getIntent().hasExtra("isWhatsappRunning"))
                                startActivity(new Intent(getApplicationContext(), Settings.class));
                            finish();

                        } else {
                            mTargetView.setText("");
                            Toast.makeText(getApplicationContext(), "Incorrect PIN ,please try again!", Toast.LENGTH_LONG).show();
                        }

                    }

                       /* if (isFirstRun && !confirmPIN ) {
                            title.setText("Please Confirm your pin");
                            mTargetView.setText("");
                            getSharedPrefs().edit().putString("pin1", pin).commit();
                            confirmPIN = true;
                           // startActivity(new Intent(getApplicationContext(), Settings.class));
                           // finish();
                        }else*/

                    if(confirmPIN) {
                        {
                            String pin1 = getSharedPrefs().getString("pin1", "");

                            if (pin.equals(pin1)) {
                                isFirstRun = getSharedPrefs().edit().putBoolean("isFirstRun", false).commit();
                                getSharedPrefs().edit().putString("legitPIN", pin).commit();
                                getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit().putBoolean("isLoggedIn", true).commit();
                                //unlock
                                if (!getIntent().hasExtra("isWhatsappRunning") && !getIntent().hasExtra("isSettingsActive")) {
                                    startActivity(new Intent(getApplicationContext(), Settings.class));
                                }

                                finish();


                            } else {
                                Toast.makeText(getApplicationContext(), "PIN do not match please try again!", Toast.LENGTH_LONG).show();
                                title.setText("Please Enter PIN");
                            }

                        }
                    }
                }

            }

            @Override
            public void onRightAuxButtonClicked() {

            }

            @Override
            public void onModifierButtonClicked(int i) {

            }
        });






        title = (TextView)findViewById(R.id.txt_display);
        mTargetView = (EditText) findViewById(R.id.targetEdit);


        mTargetView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length() == 4){
                    String savedPin = getSharedPrefs().getString("legitPIN", "-1");
                    int resetCode = getSharedPrefs().getInt("resetCode",-1);
                    if(savedPin.equals(s.toString()) || s.toString().equals(Integer.toString(resetCode))){

                        //unlock
                        if(getIntent().hasExtra("isWhatsappRunning")){
                            getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit().putBoolean("isLoggedIn", true).commit();
                          //whats app is running in background set adflag true
                            getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit().putBoolean("Adflag",true).commit();
                            onBackPressed();
                        }
                        if(!isFirstRun && !getIntent().hasExtra("isWhatsappRunning")){
                           startActivity(new Intent(getApplicationContext(), Settings.class));
                            finish();
                        }


                    }
                }
              //  Toast.makeText(getApplicationContext(),"On text changed "+s.toString(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable s) {
             // Toast.makeText(getApplicationContext(),"After text changed "+s.toString(),Toast.LENGTH_SHORT).show();
            }
        });

        numberKeyboard.setEditText(mTargetView);


        legitPin = getSharedPrefs().getString("legitPIN","");

        if(isFirstRun || TextUtils.isEmpty(legitPin) ) {
            isFirstRun  = true;
            title.setText("PIN not set ,please set a new one.");
           // startService(new Intent(this,MonitorService.class));
        }else {
            title.setText("Enter pin to unlock");

            if(isMyServiceRunning(MonitorService.class)) {
                Log.d(TAG,"Service still running no need to restart it");
               //  Toast.makeText(getApplicationContext(),"Service still running no need to restart it",Toast.LENGTH_LONG).show();
            } else
            {
                Log.d(TAG, "Service not running restarting");
                Toast.makeText(getApplicationContext(),"Service not running restarting",Toast.LENGTH_LONG).show();
              //  startService(new Intent(getApplicationContext(), MonitorService.class));
                Intent i = new Intent(this, MonitorService.class);
                PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
                AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
                am.cancel(pi); //cancel any existing alarms
                am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                        SystemClock.elapsedRealtime()+1000,
                        2000, pi);//start service every 2 seconds
            }


        }


        mTargetView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Log.i("MainActivity", "Enter pressed");

                    String pin = mTargetView.getText().toString();


                    if (TextUtils.isEmpty(pin))
                        mTargetView.setError("PIN required!");

                    if (pin.length() > 0 && pin.length() < 4)
                        mTargetView.setError("Your pin should be 4 digits long");
                    else {

                        if (!isFirstRun) {
                            String savedPin = getSharedPrefs().getString("legitPIN", "");


                            if (savedPin.equals(pin)) {

                                getSharedPrefs().edit().putBoolean("isLoggedIn", true).commit();

                                if (!getIntent().hasExtra("isWhatsappRunning"))
                                    startActivity(new Intent(getApplicationContext(), Settings.class));
                                finish();

                            } else {
                                mTargetView.setText("");
                                Toast.makeText(getApplicationContext(), "Incorrect PIN ,please try again!", Toast.LENGTH_LONG).show();
                            }

                        }

                       /* if (isFirstRun && !confirmPIN ) {
                            title.setText("Please Confirm your pin");
                            mTargetView.setText("");
                            getSharedPrefs().edit().putString("pin1", pin).commit();
                            confirmPIN = true;
                           // startActivity(new Intent(getApplicationContext(), Settings.class));
                           // finish();
                        }else*/

                        if(confirmPIN) {
                            {
                                String pin1 = getSharedPrefs().getString("pin1", "");

                                if (pin.equals(pin1)) {
                                    isFirstRun = getSharedPrefs().edit().putBoolean("isFirstRun", false).commit();
                                    getSharedPrefs().edit().putString("legitPIN", pin).commit();
                                    getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit().putBoolean("isLoggedIn", true).commit();
                                    //unlock
                                    if (!getIntent().hasExtra("isWhatsappRunning") && !getIntent().hasExtra("isSettingsActive")) {
                                        startActivity(new Intent(getApplicationContext(), Settings.class));
                                    }

                                    finish();


                                } else {
                                    Toast.makeText(getApplicationContext(), "PIN do not match please try again!", Toast.LENGTH_LONG).show();
                                    title.setText("Please Enter PIN");
                                }

                            }
                        }
                    }

                    return true;

                }
                return false;
            }
        });



     /*  mKeyboardView =  findViewById(R.id.keyboard);

        mKeyboardView.setPreviewEnabled(false);
        mKeyboardView.setKeyboard(mKeyboard);
        mKeyboardView
                .setOnKeyboardActionListener(new BasicOnKeyboardActionListener(
                        this)); */


        TextView forgotPass = (TextView) findViewById(R.id.txt_forgot);
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ResetPasswordActivity.class).putExtra("type", PreferenceContract.LOCK_PIN));
            }
        });


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



  /*  private void showKeyboardWithAnimation() {
        if (mKeyboardView.getVisibility() == View.GONE) {
            Animation animation = AnimationUtils
                    .loadAnimation(this,
                            R.anim.slide_in_bottom);
            mKeyboardView.showWithAnimation(animation);
        }
    }*/


    public SharedPreferences getSharedPrefs(){
       return  getSharedPreferences("PREFS", MODE_PRIVATE);
    }



    @Override
    public void onBackPressed() {
        if(getIntent().hasExtra("isWhatsappRunning") && !isFirstRun && !getSharedPreferences("PREFS", Context.MODE_PRIVATE).getBoolean("isLoggedIn",false)) {
           //go to homescreen
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);

        }else
            super.onBackPressed();

    }
}
