package com.example.riderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.os.Bundle;

import com.example.riderapp.Workers.CheckDeliveriesWorker;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    boolean t=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        /*if (t) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, LoginSignUpActivity.class));
            finish();
            t=false;
            //displaySingleSelectionDialog();
            return;
        }*/
        BottomNavigationView navView = findViewById(R.id.bottom_navigatin_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.settingsFragment, R.id.encomendasFragment, R.id.profileFragment)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        PeriodicWorkRequest periodicWorkRequest= new PeriodicWorkRequest
                .Builder(CheckDeliveriesWorker.class, 15, TimeUnit.MINUTES)
                .build();
        WorkManager workManager = WorkManager.getInstance(this);
        workManager.enqueue(periodicWorkRequest);

    }
    public String getAuth() {return "adasdsa";}
}