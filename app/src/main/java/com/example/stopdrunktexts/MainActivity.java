package com.example.stopdrunktexts;

import android.annotation.SuppressLint;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    boolean activated = false;
    Button btnMain;
    String currentlyRunningApp;
    final Handler handler = new Handler();
    final int delay = 1000;
    Intent intent;
/*
    Runnable runnable = new Runnable() {
        public void run() {
            currentlyRunningApp = getCurrentApp();
            if (currentlyRunningApp.equals("com.whatsapp")) {
                killHandler();
                System.out.println("Whatsapp detected. Showing lockscreen...");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            handler.postDelayed(this, delay);
        }
    };

 */

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnMain = findViewById(R.id.btn_main);
    }


    //Permission for UsageStatsManager to work ( function: "getCurrentApp")
    public void givePermissions1(View view)
    {
        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
    }


    //Permission for starting new intent even when app is in background to work (function: "startListening")
    public void givePermissions2(View view)
    {
        startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION));
    }


    public void startListening(View view)
    {
        if(!activated){

            //Sad tries to make this shit work with a service or a handler:
            //startService(new Intent(this, CheckForAppsAndDisplayLock.class));
            //handler.postDelayed(runnable,delay);
            activated = true;
            btnMain.setText("Deactivate Lock");
            System.out.println("Lock activated.");
            while (activated) {
                currentlyRunningApp = getCurrentApp();
                if (currentlyRunningApp.equals("com.whatsapp")) {
                    System.out.println("Whatsapp detected. Showing Lockscreen...");
                    intent = new Intent(this, LockScreen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    activated = false;
                }

                try {
                    sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            activated = false;

            //stopService(new Intent(this, CheckForAppsAndDisplayLock.class));
            btnMain.setText("Activate Lock");
            System.out.println("Lock deactivated.");
        }
    }
/*
    public void killHandler(){
        handler.removeCallbacks(runnable);
    }

 */
    private String getCurrentApp() {
        String topPackageName = "None";
        @SuppressLint("WrongConstant") UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService("usagestats");
        long time = System.currentTimeMillis();
        List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 3000, time);
        // Sort the stats by the last time used
        if (stats != null) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();
            for (UsageStats usageStats : stats) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (!mySortedMap.isEmpty())
            {
                topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
            }
        }
        System.out.println(topPackageName);
        return topPackageName;
    }
}