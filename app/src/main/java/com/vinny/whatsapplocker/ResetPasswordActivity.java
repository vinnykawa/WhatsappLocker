package com.vinny.whatsapplocker;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;


import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.vinny.whatsapplocker.util.UserEmailFetcher;

import java.util.Random;

import cz.msebera.android.httpclient.Header;


/**
 * Created by DENNOH on 10/3/2015.
 */
public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

       // android.support.v7.app.ActionBar actionBar = getSupportActionBar();
       // actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#388E3C")));

        EditText securityEmail = (EditText) findViewById(R.id.ed_email);

        securityEmail.setText(UserEmailFetcher.getEmail(this));
        securityEmail.setEnabled(false);


        Button send = (Button) findViewById(R.id.btn_reset);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });



        final EditText code = (EditText) findViewById(R.id.ed_code);

        final Button reset = (Button) findViewById(R.id.btn_code_reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int resetCode =  getSharedPreferences("PREFS",MODE_PRIVATE).getInt("resetCode", -1);


                if(TextUtils.isEmpty(code.getText().toString()))
                    code.setError("This field is required");
                else{
                    int userCode = Integer.parseInt(code.getText().toString());

                    if(resetCode == userCode){

                        if(getIntent().hasExtra("type")) {
                            getSharedPreferences("PREFS",MODE_PRIVATE).edit().putString("legitPIN", null).commit();
                            Toast.makeText(getApplicationContext(), "Pin Reset successfully", Toast.LENGTH_LONG).show();

                        }else{

                            getSharedPreferences("PREFS", MODE_PRIVATE).edit().putString("pattern", null).commit();

                            Toast.makeText(getApplicationContext(), "Pattern Reset successfully", Toast.LENGTH_LONG).show();

                        }

                        finish();
                    }

                }

            }
        });





    }


    public void resetPassword(){
       final  int code = 1000 + new Random().nextInt(8999);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://findchapchap.com/thejellocompany/sendmail.php?to="+UserEmailFetcher.getEmail(this)+"&code="+code, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"

                getSharedPreferences("PREFS",MODE_PRIVATE).edit().putInt("resetCode",code).commit();

                Toast.makeText(getApplicationContext(),"Password reset,please check your email for reset code",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Toast.makeText(getApplicationContext(),"Reset code not delivered to email ,please try again later",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }
}
