package com.example.stopdrunktexts;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    boolean activated = false;
    Button btnMain;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnMain = findViewById(R.id.btn_main);
    }

    public void startListening(View view)
    {
        if(!activated)
        {
            startService(new Intent(this, CheckForAppsAndDisplayLock.class));
            activated = true;
            btnMain.setText("Deactivate Lock");
        }
        else{
            activated = false;
            stopService(new Intent(this, CheckForAppsAndDisplayLock.class));
            btnMain.setText("Activate Lock");
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





}