package com.example.riderapp.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.riderapp.Connections.API_Connection;
import com.example.riderapp.Connections.API_Service;
import com.example.riderapp.MainActivity;
import com.example.riderapp.R;
import com.example.riderapp.Utils.GeoUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EncomendaMapaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EncomendaMapaFragment extends Fragment {


    Button button;
    AlertDialog alertDialog1;
    CharSequence[] values = {"in_distribution", "done"};

    String mstart;
    String mdestination;
    Integer mrider_fee;
    String mtelephone;
    Integer mdeliveryID;
    API_Connection api_connection;
    Integer riderID;
    //GoogleMap mMap;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EncomendaMapaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EncomendaMapaFragment newInstance(String param1, String param2) {
        EncomendaMapaFragment fragment = new EncomendaMapaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mstart = bundle.getString("start");
            mdestination = bundle.getString("destination");
            mrider_fee = bundle.getInt("rider_fee");
            mtelephone = bundle.getString("telephone");
            mdeliveryID = bundle.getInt("deliveryID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
        String firstname = sharedPreferences.getString("FirstName", "User");
        riderID = sharedPreferences.getInt("id", 1);


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_encomenda_mapa, container, false);

        TextView earning = view.findViewById(R.id.textviewEarningMapa);
        TextView start = view.findViewById(R.id.textviewStartMapa);
        TextView destination = view.findViewById(R.id.textviewDestinationMapa);
        TextView distance = view.findViewById(R.id.textviewDistanceMapa);
        TextView deliver = view.findViewById(R.id.textviewEncomendaMapa);
        deliver.setText("Deliver: " + firstname);
        earning.setText("Earnings: " + mrider_fee);
        start.setText("Start: " + mstart);
        destination.setText("Destination:  " + mdestination);
        distance.setText("Remaining Distance:(a calcular)");


        button = view.findViewById(R.id.buttonStateEncomenda);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAlertDialogWithRadioButtonGroup();
            }
        });
        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.home_map))
                .getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            return;
                        }
                        googleMap.setMyLocationEnabled(true);

                        Geocoder geocoder = new Geocoder(getContext());
                        LatLng startcoords = null;
                        LatLng destinationcoords = null;
                        try {
                            Address startaddress= geocoder.getFromLocationName(mstart,1).get(0);
                            startcoords = new LatLng(startaddress.getLatitude(),startaddress.getLongitude());
                            googleMap.addMarker(new MarkerOptions().position(startcoords));
                        } catch (IOException e) {
                            Log.e("EncomendaMapa",e.getMessage());
                        }
                        catch(Exception e){
                            Log.e("EncomendaMapa",e.toString());
                        }
                        try {
                            Address destinationaddress= geocoder.getFromLocationName(mdestination,1).get(0);
                            destinationcoords = new LatLng(destinationaddress.getLatitude(),destinationaddress.getLongitude());
                            googleMap.addMarker(new MarkerOptions().position(destinationcoords).icon(BitmapDescriptorFactory.defaultMarker(138)));
                        } catch (IOException e) {
                            Log.e("EncomendaMapa",e.getMessage());
                        } catch(Exception e){
                            Log.e("EncomendaMapa",e.toString());
                        }
                        if(startcoords != null && destinationcoords != null) {
                            LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                    .include(startcoords)
                                    .include(destinationcoords)
                                    .build();
                            try {
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 200));
                            }
                            catch (Exception e){
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.64, -8.65), 11.95f));
                            }
                            double distanceStartDest = GeoUtils.distanceBetween2Points(startcoords, destinationcoords);
                            distance.setText("Remaining Distance: " + Math.round(distanceStartDest * 10.0) / 10.0 + " km");
                        }
                        else{
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.64, -8.65), 11.95f));
                        }
                    }
                });
        return view;
    }
    public void CreateAlertDialogWithRadioButtonGroup(){

        api_connection= API_Service.getClient();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Update Delivery Status");
        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                switch(item)
                {
                    case 0:
                        Call<String> call = api_connection.api_rider_update_Deliver_State(String.valueOf(mdeliveryID),String.valueOf(riderID),"in_distribution");
                        call.enqueue(new Callback<String>() {
                                         @Override
                                         public void onResponse(Call<String> call, Response<String> response) {
                                             String s = response.body().toString();
                                             Log.w("EncomendaInfo",s);
                                             Toast.makeText(getContext(), "Status Updated to: In Distribution", Toast.LENGTH_LONG).show();

                                         }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            call.cancel();
                                            Toast.makeText(getContext(),"Failure Starting Delivery",Toast.LENGTH_SHORT).show();
                                            Log.w("EncomendaInfo", "Failure Starting Delivery");
                                            Log.d("EncomendaInfo",t.getMessage());
                                        }
                        });
                        break;
                    case 1:
                        Call<String> call2 = api_connection.api_rider_update_Deliver_State(String.valueOf(mdeliveryID),String.valueOf(riderID),"completed");
                        call2.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String s = response.body().toString();
                                Log.w("EncomendaInfo",s);
                                Toast.makeText(getContext(), "Status Updated to: Done", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                getActivity().startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                call.cancel();
                                Toast.makeText(getContext(),"Failure Starting Delivery",Toast.LENGTH_SHORT).show();
                                Log.w("EncomendaInfo", "Failure Starting Delivery");
                                Log.d("EncomendaInfo",t.getMessage());
                            }
                        });
                        break;
                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();
    }
}