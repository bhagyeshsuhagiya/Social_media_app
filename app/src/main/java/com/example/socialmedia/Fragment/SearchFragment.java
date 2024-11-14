package com.example.socialmedia.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.Adapter.SearchAdapter;
import com.example.socialmedia.Model.UserModel;
import com.example.socialmedia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class SearchFragment extends Fragment {
    ArrayList<UserModel> list4;
    RecyclerView rcv3;
    View view;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);

        list4 = new ArrayList<>();
        
        rcv3 = view.findViewById(R.id.rcv3);
        SearchAdapter searchAdapter = new SearchAdapter(list4, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcv3.setLayoutManager(linearLayoutManager);
        rcv3.setNestedScrollingEnabled(false);
        rcv3.setAdapter(searchAdapter);

        database.getReference().child("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list4.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    UserModel user = dataSnapshot.getValue(UserModel.class);
                    user.setUserID(dataSnapshot.getKey());
                    if(!dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getUid())){
                        list4.add(user);
                    }
                }
                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}
