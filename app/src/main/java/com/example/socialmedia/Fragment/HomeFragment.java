package com.example.socialmedia.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.Adapter.PostAdapter;
import com.example.socialmedia.Model.PostModel;
import com.example.socialmedia.Model.UserModel;
import com.example.socialmedia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    FirebaseAuth auth;
    FirebaseDatabase database;

    TextView vfullname;
    ImageView vprofile_image, vprofile_image1;
    RecyclerView rcv;
    RecyclerView rcv2;

    ArrayList<PostModel> list2;


    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        vprofile_image1 = view.findViewById(R.id.profile_image1);


        database.getReference()
                .child("user").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            UserModel user = snapshot.getValue(UserModel.class);

                            Picasso.get()
                                    .load(user.getCoverPhoto())
                                    .placeholder(R.drawable.user)
                                    .into(vprofile_image1);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        rcv2 = view.findViewById(R.id.rcv2);

        list2 = new ArrayList<>();


        PostAdapter postAdapter = new PostAdapter(list2, getContext());
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcv2.setLayoutManager(linearLayoutManager1);
        rcv2.setNestedScrollingEnabled(false);
        rcv2.setAdapter(postAdapter);

        database.getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list2.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    PostModel post = dataSnapshot.getValue(PostModel.class);
                    post.setPostId(dataSnapshot.getKey());
                    list2.add(post);
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;

    }
}
