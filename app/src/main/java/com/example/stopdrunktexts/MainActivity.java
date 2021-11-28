package com.example.stopdrunktexts;

import android.annotation.SuppressLint;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    final Handler handler = new Handler(Looper.myLooper());
    final int delay = 1000;
    boolean activated = false;
    Button btnMain;
    String currentlyRunningApp;
    Intent intent;
    boolean test = false;

    public void testActivate(View view){
        test = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnMain = findViewById(R.id.btn_main);
    }


    Runnable runnable = new Runnable() {
        public void run() {
            currentlyRunningApp = getCurrentApp();
            if (currentlyRunningApp.equals("com.whatsapp")) {
                System.out.println("Whatsapp detected. Showing lockscreen...");
                openLockScreen();
                openLockScreen();
                handler.removeCallbacks(runnable);
            }
            else{
                handler.postDelayed(this, delay);
            }

        }
    };


    //Doesn't work if called from runnable -.-
    public void openLockScreen(){
        intent = new Intent(this, LockScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    //TODO: Somehow the text change is not displayed... maybe the activity doesn't update or some shit
    // that's a problem for future me (Should work when I get the fucking handler to work... hopefully)
    public void startListening(View view)
    {
        if(!activated)
        {
            //Sad tries to make this shit work with a service or a handler:
            //startService(new Intent(this, CheckForAppsAndDisplayLock.class));
            handler.postDelayed(runnable,delay);
            activated = true;
            btnMain.setText("Deactivate Lock");
        }
        else{
            activated = false;
            //stopService(new Intent(this, CheckForAppsAndDisplayLock.class));
            btnMain.setText("Activate Lock");
            openLockScreen();
            handler.removeCallbacks(runnable);
        }
    }






 //-------------------------------------------DONT TOUCH BELOW BECAUSE THESE WORK FINE FOR NOW-----------------------------------------------------------------------

//TODO: Ask for permissions on app start if not already granted, remove permission buttons or hide them in sidebar

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



    /*TODO: Works like crap, needs fixing.
       should only return the app that is actually in foreground
       currently returns any application that performs an action, if not actively used by user (i.e. receiving a text via whatsapp makes function return whatsapp)
     */

    //Retrieves a list of apps used in the last 3 seconds and returns the package name of the top most application
    private String getCurrentApp() {
        String topPackageName = "None";
        @SuppressLint("WrongConstant") UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService("usagestats");
        long time = System.currentTimeMillis();
        List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 3000, time);
        //Sorting to find last app used
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
        if(test){
          return "com.whatsapp";
        }
        else{
            return topPackageName;
        }

    }
}