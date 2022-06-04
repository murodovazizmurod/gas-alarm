package com.example.gasalarm;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.shinelw.library.ColorArcProgressBar;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ColorArcProgressBar tmpp;
    AnimationDrawable anim;
    Animation animation1, animation2;

    Button button;
    boolean switch1 = true,shart= true;
    int gasValue=0;
    ImageView settingbut,exit;
    Vibrator vib ;
    boolean animCon = true;

    Handler tH = new Handler();
    Runnable runable = new Runnable() {
        @Override
        public void run() {
            if (animCon) {
                animCon = false;
                settingbut.clearAnimation();
                settingbut.startAnimation(animation1);

            } else {
                animCon = true;
                settingbut.clearAnimation();
                settingbut.startAnimation(animation2);
            }
            timerHandler.postDelayed(this, 4000);

        }
    };
    Handler timerHandler = new Handler();
    Runnable flashlight = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject object1 = new JSONObject();
                object1.put("reader", "reader");
                object1.put("mode", 2);
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
                    gasValue = object.getInt("gasValue") ;
                    gasValue -= 60;// -60  moufiqlashtirish uchun


                   // Toast.makeText(MainActivity.this, hour, Toast.LENGTH_SHORT).show();

                }
            } catch (IOException e) {
                // Toast.makeText(MainActivity.this, "Qurilmaga ulanishda xatolik bor!!!", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            tmpp.setCurrentValues(gasValue/10);

            reaksiya();

            timerHandler.postDelayed(this, 30);

        }
    };
    Handler timerHandler1 = new Handler();
    Runnable flashlight1 = new Runnable() {
        @Override
        public void run() {
            if (gasValue > 160 && switch1 ){
                ledonof();
            }else{
                CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                try {
                    String cameraId = cameraManager.getCameraIdList()[0];
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cameraManager.setTorchMode(cameraId, false);
                    }
                } catch (CameraAccessException e) {
                }
            }
            timerHandler1.postDelayed(this, 300);

        }
    };
    private void reaksiya() {

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void ledonof() {
        if (shart) {
            CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            try {
                String cameraId = cameraManager.getCameraIdList()[0];
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    cameraManager.setTorchMode(cameraId, true);
                }
            } catch (final CameraAccessException e) {
            }
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.beep);
            mediaPlayer.start();
            shart = false;
        } else {
            vib.vibrate(100);
            CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            try {
                String cameraId = cameraManager.getCameraIdList()[0];
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    cameraManager.setTorchMode(cameraId, false);
                }
            } catch (CameraAccessException e) {
            }
            shart = true;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }
        wificonnect(this);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        settingbut = findViewById(R.id.setbutton);
        exit = findViewById(R.id.onoff);

        tmpp = findViewById(R.id.gass);
        button = findViewById(R.id.stopbutton);
        ConstraintLayout container = findViewById(R.id.mainact);
        anim = (AnimationDrawable) container.getBackground();
        anim.setEnterFadeDuration(200);
        anim.setExitFadeDuration(1500);
        anim.start();

        animation1 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate1);
        animation2 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate);
        animation1.setDuration(4000);
        animation2.setDuration(4000);
        tH.postDelayed(runable, 0);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!switch1) {
                    proba(switch1);
                    button.getBackground().setColorFilter(Color.rgb(255,0,0), PorterDuff.Mode.SRC_ATOP);
                    switch1 = true;
                    button.setText("Stop");
                } else {
                    proba(switch1);
                    button.getBackground().setColorFilter(Color.rgb(0,255,0), PorterDuff.Mode.SRC_ATOP);
                    button.setText("o`lchash");
                    switch1 = false;
                }
            }
        });

        tmpp.setCurrentValues(10);
        timerHandler.postDelayed(flashlight, 0);
        timerHandler1.postDelayed(flashlight1, 0);

        settingbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
    }

    public void wificonnect(Context context) {
        SharedPreferences sp = getSharedPreferences("Data", MODE_MULTI_PROCESS);
        String networkSSID = sp.getString("ssid", "Wifi");
        String networkPass = sp.getString("pasword", "12345678");
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);
        wifiManager.setWifiEnabled(true);

        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", networkSSID);
        wifiConfig.preSharedKey = String.format("\"%s\"", networkPass);

        int netId = wifiManager.addNetwork(wifiConfig);
        wifiManager.disconnect();

        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();
        callwemos();
        callwemos();
    }

    private void callwemos() {
        try {
            JSONObject object1 = new JSONObject();
            object1.put("mode", 5);
            object1.put("statuscha", 123);

            object1.put("myid", 1234);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://192.168.1.1/connect")
                    .addHeader("Content-Type:", "application/json")
                    .addHeader("body1", object1.toString())
                    .build();
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();
            if (body != null) {

            }
        } catch (IOException e) {
        } catch (JSONException e) {
            e.printStackTrace();
        }
        updateActivity();
    }

    public void updateActivity() {
        SharedPreferences sp = getSharedPreferences("RGBSAVE", MODE_MULTI_PROCESS);
//        red = sp.getInt("red", 0);
//        green = sp.getInt("green", 0);
//        blue = sp.getInt("blue", 0);
//        setbigth = sp.getInt("bright", 0);
//        button2.getBackground().setColorFilter(Color.rgb(red, green, blue), PorterDuff.Mode.SRC_ATOP);
//        bright.setProgress(setbigth);

    }


    private void proba(boolean status) {
        try {
            JSONObject object1 = new JSONObject();
            object1.put("mode", 5);
            object1.put("status", status);

            object1.put("myid", 1234);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://192.168.1.1/connect")
                    .addHeader("Content-Type:", "application/json")
                    .addHeader("body1", object1.toString())
                    .build();
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();
            if (body != null) {
            }
        } catch (IOException e) {
        } catch (JSONException e) {
            e.printStackTrace();
        }
        updateActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
