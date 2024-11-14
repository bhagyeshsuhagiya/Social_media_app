package com.example.socialmedia.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.Model.FollowerModel;
import com.example.socialmedia.Model.NotificationModel;
import com.example.socialmedia.Model.UserModel;
import com.example.socialmedia.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    ArrayList<UserModel> list4;
    Context context;

    public SearchAdapter(ArrayList<UserModel> list4, Context context) {
        this.list4 = list4;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.demo_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        UserModel user = list4.get(position);

        Picasso.get().load(user.getCoverPhoto())
                .placeholder(R.drawable.user)
                .into(holder.vprofile_image);
        holder.vfullName.setText(user.getName());
        holder.vwork.setText(user.getProfession());

        FirebaseDatabase.getInstance().getReference().child("user")
                .child(user.getUserID())
                .child("follower")
                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            holder.vbutton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.following));
                            holder.vbutton.setText("Following");
                            holder.vbutton.setTextColor(context.getResources().getColor(R.color.black));
                            holder.vbutton.setEnabled(false);
                        } else {
                            holder.vbutton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    FollowerModel follow = new FollowerModel();
                                    follow.setFollowedBy(FirebaseAuth.getInstance().getUid());
                                    follow.setFollowedAt(String.valueOf(new Date().getTime()));

                                    FirebaseDatabase.getInstance().getReference().child("user")
                                            .child(user.getUserID())
                                            .child("follower")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .setValue(follow)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    FirebaseDatabase.getInstance().getReference().child("user")
                                                            .child(user.getUserID())
                                                            .child("followerCount")
                                                            .setValue(user.getFollowerCount() + 1)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    holder.vbutton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.following));
                                                                    holder.vbutton.setText("Following");
                                                                    holder.vbutton.setTextColor(context.getResources().getColor(R.color.black));
                                                                    holder.vbutton.setEnabled(false);

                                                                    String currentUserID = FirebaseAuth.getInstance().getUid();
                                                                    FirebaseDatabase.getInstance().getReference().child("user")
                                                                            .child(currentUserID)
                                                                            .child("followingCount")
                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                    long followingCount = 0;
                                                                                    if (snapshot.exists()) {
                                                                                        followingCount = snapshot.getValue(Long.class);
                                                                                    }
                                                                                    FirebaseDatabase.getInstance().getReference().child("user")
                                                                                            .child(currentUserID)
                                                                                            .child("followingCount")
                                                                                            .setValue(followingCount + 1);
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                }
                                                                            });

                                                                    NotificationModel notificationModel = new NotificationModel();
                                                                    notificationModel.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                    notificationModel.setNotificationAt(new Date().getTime());
                                                                    notificationModel.setType("follow");

                                                                    FirebaseDatabase.getInstance().getReference()
                                                                            .child("notification")
                                                                            .child(user.getUserID())
                                                                            .push()
                                                                            .setValue(notificationModel);
                                                                }
                                                            });
                                                }
                                            });
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return list4.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView vprofile_image;
        TextView vfullName, vwork;
        Button vbutton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vprofile_image = itemView.findViewById(R.id.profile_image);
            vfullName = itemView.findViewById(R.id.fullName);
            vwork = itemView.findViewById(R.id.work);
            vbutton = itemView.findViewById(R.id.button);
        }
    }
}
