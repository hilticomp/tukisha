package com.imbyou.pavan.mykart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;

/**
 * Created by Lenovo on 13-Apr-17.
 */

@SuppressLint("ValidFragment")
public class FurnitureFragment extends Fragment {

    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    private FirebaseDatabase database;
    //private Preferences mPreferences;

    String imbyou;

    //private FirebaseRecyclerAdapter<WorkoutTypesModel, WorkoutDeleteViewHolder> mAdapter;




    @SuppressLint("ValidFragment")
    public FurnitureFragment(String data) {
        // Required empty public constructor

        this.imbyou = data;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_furniture, container, false);

        mRecycler = (RecyclerView) view.findViewById(R.id.invite_frag_recyclerView);
        database = FirebaseDatabase.getInstance();
        //mPreferences = new Preferences();

        mRecycler.setHasFixedSize(true);
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);



        /* PAWAN: If you want to add animation to the recycler view just add these below three lines
        in which ever activity you want which has a recycler view after importing the library.*/

        JazzyRecyclerViewScrollListener jazzyScrollListener = new JazzyRecyclerViewScrollListener();
        mRecycler.setOnScrollListener(jazzyScrollListener);

        //PAWAN: Channge this numeric "2" to anything to change the animation for recyler view

        jazzyScrollListener.setTransitionEffect(1);
        //jazzyScrollListener.setTransitionEffect(2);
        //jazzyScrollListener.setTransitionEffect(3);
        //jazzyScrollListener.setTransitionEffect(4);
        //jazzyScrollListener.setTransitionEffect(5);
        //jazzyScrollListener.setTransitionEffect(6);
        //jazzyScrollListener.setTransitionEffect(7);
        //jazzyScrollListener.setTransitionEffect(8);
        //jazzyScrollListener.setTransitionEffect(9);
        //jazzyScrollListener.setTransitionEffect(10);
        //jazzyScrollListener.setTransitionEffect(11);
        //jazzyScrollListener.setTransitionEffect(12);
        //jazzyScrollListener.setTransitionEffect(13);
        //jazzyScrollListener.setTransitionEffect(14);
        //jazzyScrollListener.setTransitionEffect(15);




        // Inflate the layout for this fragment
        return view;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //getMyConnections();
    }







}





