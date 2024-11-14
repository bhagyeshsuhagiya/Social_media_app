package com.example.socialmedia.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.socialmedia.Adapter.NotificationAdapter;
import com.example.socialmedia.Model.NotificationModel;
import com.example.socialmedia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotiFragment extends Fragment {

    RecyclerView rcvnoti;
    ArrayList<NotificationModel> list;
    FirebaseDatabase database;

    public NotiFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_noti, container, false);



        rcvnoti = view.findViewById(R.id.rcvnoti);
        list = new ArrayList<>();


        NotificationAdapter adapter = new NotificationAdapter(list, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvnoti.setLayoutManager(linearLayoutManager);
        rcvnoti.setNestedScrollingEnabled(false);
        rcvnoti.setAdapter(adapter);


        database.getReference().child("notification")
                .child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            NotificationModel notificationModel = dataSnapshot.getValue(NotificationModel.class);
                            notificationModel.setNotificationID(dataSnapshot.getKey());
                            list.add(notificationModel);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        return view;
    }
}
