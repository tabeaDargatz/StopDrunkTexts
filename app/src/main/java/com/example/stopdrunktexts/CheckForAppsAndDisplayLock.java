package com.example.stopdrunktexts;

import android.annotation.SuppressLint;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class CheckForAppsAndDisplayLock extends Service {
    String currentlyRunningApp;
    Runnable runnable;
    Handler handler = new Handler();
    Intent i;
    final int delay = 1000;
    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        handler.postDelayed( runnable = new Runnable() {
            public void run() {
                currentlyRunningApp = getCurrentApp();
                if (currentlyRunningApp.equals("com.whatsapp"))
                {
                    System.out.println("Whatsapp detected. Showing lockscreen...");
                    i = new Intent(getApplicationContext(), LockScreen.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    handler.removeCallbacks(this);
                    stopSelf();
                }
                handler.postDelayed(this, delay);
            }
        }, delay);
        return START_STICKY;
    }

    /*TODO: Works like crap, needs fixing.
       should only return the app that is actually in foreground
       currently returns any application that performs an action, if not actively used by user (i.e. receiving a text via whatsapp makes function return whatsapp)
     */
    private String getCurrentApp() {
        String topPackageName = "None";
        @SuppressLint("WrongConstant") UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService("usagestats");
        long time = System.currentTimeMillis();
        List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 3000, time);

        //Sorting by last app used
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





    //Not used but needs to be here
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}