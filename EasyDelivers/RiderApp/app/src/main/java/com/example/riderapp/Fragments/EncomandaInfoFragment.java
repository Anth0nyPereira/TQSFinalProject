package com.example.riderapp.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.riderapp.Activities.EncomendaMapaActivity;
import com.example.riderapp.Connections.API_Connection;
import com.example.riderapp.Connections.API_Service;
import com.example.riderapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EncomandaInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EncomandaInfoFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    API_Connection api_connection;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_START = "start";
    private static final String ARG_DESTINATION = "destination";
    private static final String ARG_RIDER_FEE= "rider_fee";
    private static final String ARG_TELEPHONE= "telephone";
    private static final String ARG_ID= "idDelivery";

    private LocationManager mLocationManager;

    // TODO: Rename and change types of parameters
    private String mStart;
    private String mDestination;
    private int mRider_Fee;
    private String mTelephone;
    private int mID;

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
    public static EncomandaInfoFragment newInstance(String start, String destination,int rider_fee, String telephone, int ID) {
        EncomandaInfoFragment fragment = new EncomandaInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_START, start);
        args.putString(ARG_DESTINATION, destination);
        args.putInt(ARG_RIDER_FEE,rider_fee);
        args.putString(ARG_TELEPHONE,telephone);
        args.putInt(ARG_ID,ID);
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
            mRider_Fee =getArguments().getInt(ARG_RIDER_FEE);
            mID = getArguments().getInt(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        api_connection= API_Service.getClient();
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("UserData",MODE_PRIVATE);
        int id = sharedPreferences.getInt("id",0);

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

                Call<String> call = api_connection.api_accept_delivery(String.valueOf(mID),String.valueOf(id));
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String s = response.body().toString();
                        Log.w("EncomendaInfo",s);
                        if (s.equals("Delivery Assigned")){
                            Toast.makeText(getContext(),"Starting Delivery",Toast.LENGTH_SHORT).show();
                            Log.w("EncomendaInfo", "Starting Delivery");
                            changeToEncomendaMapaActivity();
                        }
                        else{
                            Toast.makeText(getContext(),"Error Starting Delivery",Toast.LENGTH_SHORT).show();
                            Log.w("EncomendaInfo", "Error Starting Delivery");
                        }

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        call.cancel();
                        Toast.makeText(getContext(),"Failure Starting Delivery",Toast.LENGTH_SHORT).show();
                        Log.w("EncomendaInfo", "Failure Starting Delivery");
                        Log.d("EncomendaInfo",t.getMessage());
                    }
                });
            }
        });
        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map))
                .getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.64, -8.65), 11.95f));
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        googleMap.setMyLocationEnabled(true);
                    }
                });
        return view;
    }

    //
    public void changeToEncomendaMapaActivity(){
        Intent intent = new Intent(getActivity(), EncomendaMapaActivity.class);
        intent.putExtra("start",mStart);
        intent.putExtra("destination",mDestination);
        intent.putExtra("deliveryID",mID);
        intent.putExtra("telephone",mTelephone);
        intent.putExtra("rider_fee",mRider_Fee);
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
