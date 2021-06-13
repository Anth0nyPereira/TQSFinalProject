package com.example.riderapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.riderapp.Adapters.EncomendasAdapter;
import com.example.riderapp.Classes.ClickListener;
import com.example.riderapp.Classes.Encomenda;
import com.example.riderapp.Connections.API_Connection;
import com.example.riderapp.Connections.API_Service;
import com.example.riderapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EncomendasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EncomendasFragment extends Fragment {


    private RecyclerView recyclerView;
    private EncomendasAdapter encomendasAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<Encomenda> itemsList;

    API_Connection api_connection;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public EncomendasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EncomendasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EncomendasFragment newInstance(String param1, String param2) {
        EncomendasFragment fragment = new EncomendasFragment();
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

        api_connection = new API_Service().getClient();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_encomendas, container, false);
        recyclerView = view.findViewById(R.id.recycleView);
        itemsList= new ArrayList<>();
        encomendasAdapter = new EncomendasAdapter(itemsList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(encomendasAdapter);
        Call<List<Encomenda>> call = api_connection.api_get_deliveries();
        call.enqueue(new Callback<List<Encomenda>>() {
            @Override
            public void onResponse(Call<List<Encomenda>> call, Response<List<Encomenda>> response) {
                itemsList = response.body();
                encomendasAdapter.setItemsList(itemsList);
                encomendasAdapter.notifyDataSetChanged();
                Log.w("EncomendasFragment", "Deliveries Success");

            }

            @Override
            public void onFailure(Call<List<Encomenda>> call, Throwable t) {
                call.cancel();
                Log.w("EncomendasFragment", "Error "+t.toString());
            }
        });
        encomendasAdapter.setOnItemClickListener(new ClickListener<Encomenda>(){
            @Override
            public void onClick(View view, Encomenda data, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("start", data.getStart());
                bundle.putSerializable("destination",data.getDestination());
                bundle.putSerializable("rider_fee",data.getRider_fee());
                bundle.putSerializable("telephone",data.getClient_telephone());
                Navigation.findNavController(getActivity().findViewById(R.id.nav_fragment)).navigate(R.id.action_encomendasFragment_to_encomandaInfoFragment, bundle);
                Toast.makeText(getContext(),"Position = "+position+"\n Item = ",Toast.LENGTH_SHORT).show();
                Log.w("EncomendasFragment", "Position = "+position+"\n Item = ");
            }

        });
        return view;

    }
}