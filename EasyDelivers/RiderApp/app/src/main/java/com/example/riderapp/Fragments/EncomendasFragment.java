package com.example.riderapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import com.example.riderapp.R;

import java.util.ArrayList;
import java.util.List;

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

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_encomendas, container, false);
        itemsList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycleView);
        encomendasAdapter = new EncomendasAdapter(itemsList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(encomendasAdapter);

        encomendasAdapter.setOnItemClickListener(new ClickListener<Encomenda>(){
            @Override
            public void onClick(View view, Encomenda data, int position) {
                Toast.makeText(getContext(),"Position = "+position+"\n Item = "+data.getName(),Toast.LENGTH_SHORT).show();
                Log.w("EncomendasFragment", "Position = "+position+"\n Item = "+data.getName());
            }

        });

        prepareItems();
        return view;

    }
    private void prepareItems(){
        for(int i = 0; i < 50; i++) {
            Encomenda items = new Encomenda("Item"+i,20+i);
            itemsList.add(items);
        }
        encomendasAdapter.notifyDataSetChanged();
    }

}