package com.muhammadyaseen.classifiedapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class GarageSaleHomeFragment extends Fragment  {
    private NavController navController;
    SearchView SearchView;
    private RecyclerView mRecyclerView;
    TextView hometoPostFragment,hometoMyPostFragment,hometologout,homeFragmentToInfo;

    private ImageAdapter mAdapter;
    private ProgressBar progressBar;
    private DatabaseReference mDatabaseRef;
    private List<Upload> mUploads;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public GarageSaleHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GarageSaleHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GarageSaleHomeFragment newInstance(String param1, String param2) {
        GarageSaleHomeFragment fragment = new GarageSaleHomeFragment();
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
        return inflater.inflate(R.layout.fragment_garage_sale_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);
        SearchView=view.findViewById(R.id.SearchView);
        progressBar=view.findViewById(R.id.progress_circular);
        mRecyclerView=view.findViewById(R.id.recycler_view);
        hometoPostFragment=view.findViewById(R.id.hometoPostFragment);
        hometoMyPostFragment=view.findViewById(R.id.hometoMyPostFragment);
        hometologout=view.findViewById(R.id.hometologout);
        homeFragmentToInfo=view.findViewById(R.id.homeFragmentToInfo);
        mRecyclerView.setHasFixedSize(true);
        setHasOptionsMenu(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mUploads=new ArrayList<>();
        mDatabaseRef=FirebaseDatabase.getInstance().getReference("uploads");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    Upload upload=postSnapshot.getValue(Upload.class);
                    mUploads.add(upload);
                }
                mAdapter=new ImageAdapter(getActivity(), mUploads);
                mRecyclerView.setAdapter(mAdapter);
                progressBar.setVisibility(View.INVISIBLE);
                hometologout.setVisibility(view.VISIBLE);
                hometoMyPostFragment.setVisibility(view.VISIBLE);
                hometoPostFragment.setVisibility(view.VISIBLE);
                homeFragmentToInfo.setVisibility(view.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
                hometologout.setVisibility(view.VISIBLE);
                hometoMyPostFragment.setVisibility(view.VISIBLE);
                hometoPostFragment.setVisibility(view.VISIBLE);
                homeFragmentToInfo.setVisibility(view.VISIBLE);
            }
        });


SearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
    @Override
    public boolean onQueryTextSubmit(String s) {
        hometologout.setVisibility(view.VISIBLE);
        hometoMyPostFragment.setVisibility(view.VISIBLE);
        hometoPostFragment.setVisibility(view.VISIBLE);
        homeFragmentToInfo.setVisibility(view.VISIBLE);





        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        mAdapter.getFilter().filter(s.toString());

        
        return false;
    }
});

hometoMyPostFragment.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        navController.navigate(R.id.action_garageSaleHomeFragment_to_myPostFragment);
    }
});
hometoPostFragment.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        navController.navigate(R.id.action_garageSaleHomeFragment_to_postFragment);
    }
});
homeFragmentToInfo.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        navController.navigate(R.id.action_garageSaleHomeFragment_to_aboutFragment);
    }
});
hometologout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        SharedPreferences preferences=getActivity().getSharedPreferences("checkbox", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("remember","false");
        editor.apply();
        getActivity().finish();

    }
});


    }

    /*
    public void changeKeyBoardstate(View view){
        InputMethodManager inputMethodManager=(InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager!=null){
            if(view.getId()==R.id.SearchView){
                inputMethodManager.showSoftInput(SearchView,InputMethodManager.SHOW_FORCED);
            }else{
                inputMethodManager.hideSoftInputFromWindow(SearchView.getWindowToken(),0);
            }
        }
        */


    }





