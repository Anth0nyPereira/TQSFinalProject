package com.example.riderapp.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.riderapp.Activities.EncomendaMapaActivity;
import com.example.riderapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EncomandaInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EncomandaInfoFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_START = "start";
    private static final String ARG_DESTINATION = "destination";
    private static final String ARG_RIDER_FEE= "rider_fee";
    private static final String ARG_TELEPHONE= "telephone";

    private LocationManager mLocationManager;

    // TODO: Rename and change types of parameters
    private String mStart;
    private String mDestination;
    private int mRider_Fee;
    private String mTelephone;

    public EncomandaInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EncomandaInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EncomandaInfoFragment newInstance(String start, String destination,int rider_fee, String telephone) {
        EncomandaInfoFragment fragment = new EncomandaInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_START, start);
        args.putString(ARG_DESTINATION, destination);
        args.putInt(ARG_RIDER_FEE,rider_fee);
        args.putString(ARG_TELEPHONE,telephone);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStart = getArguments().getString(ARG_START);
            mDestination = getArguments().getString(ARG_DESTINATION);
            mTelephone = getArguments().getString(ARG_TELEPHONE);
            mDestination = getArguments().getString(ARG_RIDER_FEE);
            mRider_Fee =getArguments().getInt(ARG_RIDER_FEE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_encomanda_info, container, false);
        mLocationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
        }
        Button buttonStartEncomenda = view.findViewById(R.id.buttonStartEncomenda);

        TextView earning = view.findViewById(R.id.textviewEarning);
        TextView start = view.findViewById(R.id.textviewStart);
        TextView destination = view.findViewById(R.id.textviewDestination);
        TextView distance = view.findViewById(R.id.textviewDistance);
        earning.setText("Earnings: "+mRider_Fee);
        start.setText("Start: "+mStart);
        destination.setText("Destination:  "+mDestination);
        distance.setText("Distance (a calcular)" );

        buttonStartEncomenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeToEncomendaMapaActivity();
            }
        });
        return view;
    }

    //
    public void changeToEncomendaMapaActivity(){
        Intent intent = new Intent(getActivity(), EncomendaMapaActivity.class);
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap=googleMap;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
}
