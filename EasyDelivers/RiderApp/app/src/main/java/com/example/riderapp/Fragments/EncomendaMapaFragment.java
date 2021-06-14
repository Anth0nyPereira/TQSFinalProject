package com.example.riderapp.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.riderapp.MainActivity;
import com.example.riderapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EncomendaMapaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EncomendaMapaFragment extends Fragment {

    Button button;
    AlertDialog alertDialog1;
    CharSequence[] values = {"in_distribution","done"};
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_encomenda_mapa, container, false);
        button = view.findViewById(R.id.buttonStateEncomenda);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CreateAlertDialogWithRadioButtonGroup() ;

            }
        });
        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.home_map))
                .getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(40.6, -8.6))
                                .title("Hello world"));
                    }
                });
        //mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        return view;
    }
    public void CreateAlertDialogWithRadioButtonGroup(){


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Update Delivery Status");
        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch(item)
                {
                    case 0:
                        Toast.makeText(getContext(), "Status Updated to: In Distribution", Toast.LENGTH_LONG).show();
                        //Fazer Update da encomenda para a loja
                        break;
                    case 1:

                        Toast.makeText(getContext(), "Status Updated to: Done", Toast.LENGTH_LONG).show();
                        //Fazer Update da encomenda para a loja
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), MainActivity.class);
                        getActivity().startActivity(intent);
                        break;
                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();

    }
}