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

        String mstart = getIntent().getStringExtra("start");
        String mdestination = getIntent().getStringExtra("destination");
        Integer mrider_fee = getIntent().getIntExtra("rider_fee",0);
        String mtelephone = getIntent().getStringExtra("telephone");
        Integer mdeliveryID = getIntent().getIntExtra("deliveryID",0);
        Bundle arguments = new Bundle();
        arguments.putInt("deliveryID", mdeliveryID);
        arguments.putInt("rider_fee", mrider_fee);
        arguments.putString("telephone",mtelephone);
        arguments.putString("start",mstart);
        arguments.putString("destination",mdestination);
        setContentView(R.layout.activity_encomenda_mapa);
        EncomendaMapaFragment myFragment = new EncomendaMapaFragment();
        myFragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.encomendasMapFragment, myFragment, null)
                .commit();
    }

}