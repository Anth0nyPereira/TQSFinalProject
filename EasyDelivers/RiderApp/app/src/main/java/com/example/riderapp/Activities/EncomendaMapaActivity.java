package com.example.riderapp.Activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;

import com.example.riderapp.Fragments.EncomendaMapaFragment;
import com.example.riderapp.R;

import androidx.appcompat.app.AppCompatActivity;

public class EncomendaMapaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encomenda_mapa);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.encomendasMapFragment, EncomendaMapaFragment.class, null)
                .commit();
    }

}