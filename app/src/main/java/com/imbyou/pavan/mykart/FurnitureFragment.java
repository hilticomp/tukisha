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
import com.imbyou.pavan.mykart.model.CategoriesModel;
import com.imbyou.pavan.mykart.model.CategoryTypesModel;
import com.imbyou.pavan.mykart.viewholder.CategoriesViewHolder;
import com.imbyou.pavan.mykart.viewholder.CategoryTypesViewHolder;
import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;

/**
 * Created by Lenovo on 13-Apr-17.
 */

@SuppressLint("ValidFragment")
public class FurnitureFragment extends Fragment {

    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;


    //private Preferences mPreferences;

    String imbyou;
    String category;


    private FirebaseDatabase database;
    private FirebaseRecyclerAdapter<CategoryTypesModel, CategoryTypesViewHolder> mAdapter;




    @SuppressLint("ValidFragment")
    public FurnitureFragment(String category, String data) {
        // Required empty public constructor
        this.category = category;
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



        getMyCategories(imbyou);
    }

    private void getMyCategories(String s) {

        // myKey = new Preferences().getUserKey(MyConnectionsActivity.this);
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("category_types");
        Query postsQuery = myRef.child(category).child(s);
        //Query postsQuery = myRef.Child(imbyou);
        mAdapter = new FirebaseRecyclerAdapter<CategoryTypesModel, CategoryTypesViewHolder>
                (CategoryTypesModel.class,
                        R.layout.item_product,
                        CategoryTypesViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(CategoryTypesViewHolder viewHolder,
                                              final CategoryTypesModel model, int position) {
                //final DatabaseReference postRef = getRef(position);
                //inal String friend_key = postRef.getKey();

                //viewHolder.bindToResponse(MyConnectionsActivity.this, model);

                if (model != null) {
                    viewHolder.bindtoCategoriesTypes(getActivity(), model,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                     Toast.makeText(getActivity(), "You Clicked : ",
                                            Toast.LENGTH_SHORT).show();

                                  /*  PAWAN: If you click abs to need to get abs exercise, similiarly if you
                                    click tricep to need to get tricep and so on, in order to achieve this
                                            you need to send the name like "model.getName()" from this activity
                                            and receive it in "WorkoutTypesActivity" and pass that received intent
                                            in query of firebase line number 96*/

                                    /*Intent i = new Intent(MainActivity.this, CategoriesTypesActivity.class);
                                    i.putExtra("from_workouts", model.getName());

                                    startActivity(i);*/

                                    /*Intent i = new Intent(MainActivity.this, FurnitureActivity.class);
                                    i.putExtra("from_main_activity", model.getName());
                                    startActivity(i);*/
                                    Intent i2 = new Intent(getActivity(), DetailsActivity.class);
                                    i2.putExtra("sampleObject", model);
                                    startActivity(i2);



                                }
                            });
                }
            }
        };

        mRecycler.setAdapter(mAdapter);
    }







}





