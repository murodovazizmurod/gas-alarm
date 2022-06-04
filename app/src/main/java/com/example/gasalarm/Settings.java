package com.example.gasalarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Settings extends AppCompatActivity {
    AnimationDrawable anim;
    Button button;
    TextView t1,t2;
    EditText raqam, ssid, pasword;
    int signal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SeekBar signalcha;


        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }

        raqam = findViewById(R.id.phone);
        ssid = findViewById(R.id.wifissid);
        pasword = findViewById(R.id.wifipass);
        button = findViewById(R.id.button);
        t1 = findViewById(R.id.simtext);
        t2 = findViewById(R.id.phonetext);

        signalcha = findViewById(R.id.seekBar);

        SharedPreferences sp = getSharedPreferences("Data", MODE_MULTI_PROCESS);
        raqam.setText(sp.getString("phone", "+998991234567"));
        ssid.setText(sp.getString("ssid", "Wifi"));
        pasword.setText(sp.getString("pasword", "12345678"));

        LinearLayout container = findViewById(R.id.settingactivity);
        anim = (AnimationDrawable) container.getBackground();
        anim.setEnterFadeDuration(200);
        anim.setExitFadeDuration(1500);
        anim.start();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proba();
            }
        });
        getSignal();
        signalcha.setProgress(signal);
        if (signal == 100){
            raqam.setVisibility(View.GONE);
            t1.setVisibility(View.GONE);
            t2.setVisibility(View.GONE);
            signalcha.setVisibility(View.GONE);
        }
    }

    private void getSignal() {
        try {
            JSONObject object1 = new JSONObject();
            object1.put("mode", 4);

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://192.168.1.1/connect")
                    .addHeader("Content-Type:", "application/json")
                    .addHeader("body1", object1.toString())
                    .build();
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();
            if (body != null) {
                String sd = body.string();
                JSONObject object = new JSONObject(sd);
                signal = object.getInt("signal");
            }

        } catch (IOException e) {
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void proba() {
        try {
            JSONObject object1 = new JSONObject();
            object1.put("mode", 4);
            object1.put("phone", raqam.getText().toString());
            object1.put("ssid", ssid.getText().toString());
            object1.put("password", pasword.getText().toString());

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://192.168.1.1/connect")
                    .addHeader("Content-Type:", "application/json")
                    .addHeader("body1", object1.toString())
                    .build();
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();
            if (body != null) {
                finish();
            }


            SharedPreferences sp = getSharedPreferences("Data", MODE_MULTI_PROCESS);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("phone", raqam.getText().toString());
            editor.putString("ssid", ssid.getText().toString());
            editor.putString("password", pasword.getText().toString());
            editor.apply();


        } catch (IOException e) {
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}