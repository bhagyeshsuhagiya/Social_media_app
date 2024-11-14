package com.example.socialmedia.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.socialmedia.LoginActivity;
import com.example.socialmedia.Model.UserModel;
import com.example.socialmedia.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase database;
    Button logout;
    View view;
    TextView vfullname, vwork,vfollowers,following,postCount;
    ImageView vprofile_image, edit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(getContext());

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);
        following = view.findViewById(R.id.following);
        postCount = view.findViewById(R.id.postCount);


        database.getReference()
                .child("user").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            UserModel user = snapshot.getValue(UserModel.class);
                            Picasso.get()
                                    .load(user.getCoverPhoto())
                                    .placeholder(R.drawable.user)
                                    .into(vprofile_image);

                            vfullname.setText(user.getName());
                            vwork.setText(user.getProfession());
                            vfollowers.setText(user.getFollowerCount()+"");
                            following.setText(user.getFollowingCount()+"");
                            postCount.setText(user.getPostCount()+"");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });
        vfullname = view.findViewById(R.id.fullName);
        vprofile_image = view.findViewById(R.id.profile_image);
        vwork = view.findViewById(R.id.work);
        vfollowers = view.findViewById(R.id.followers);

        edit = view.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i, 1);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData() != null) {
            Uri uri = data.getData();
            vprofile_image.setImageURI(uri);


            final StorageReference reference = storage.getReference()
                    .child("cover_photo")
                    .child(FirebaseAuth.getInstance().getUid());
            reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "profile photo updated", Toast.LENGTH_SHORT).show();
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("user")
                                    .child(auth.getUid())
                                    .child("CoverPhoto")
                                    .setValue(uri.toString());
                        }
                    });
                }
            });
        }
    }
}