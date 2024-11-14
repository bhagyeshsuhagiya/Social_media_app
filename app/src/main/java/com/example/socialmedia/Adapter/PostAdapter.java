package com.example.socialmedia.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.Model.NotificationModel;
import com.example.socialmedia.Model.PostModel;
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

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.viewHolder> {

    ArrayList<PostModel> list2;
    Context context;

    public PostAdapter(ArrayList<PostModel> list2, Context context) {
        this.list2 = list2;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.demo_post, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        PostModel model = list2.get(position);
        Picasso.get().load(model.getPostImage())
                .placeholder(R.drawable.pc)
                .into(holder.post);
        holder.desc.setText(model.getPostDescription());
        holder.likecount.setText(String.valueOf(model.getPostLike()));

        FirebaseDatabase.getInstance().getReference().child("user")
                .child(model.getPostedBy()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel user = snapshot.getValue(UserModel.class);
                        if (user != null) {
                            Picasso.get().load(user.getCoverPhoto())
                                    .placeholder(R.drawable.user)
                                    .into(holder.profile_image);
                            holder.fullName.setText(user.getName());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        FirebaseDatabase.getInstance().getReference().child("posts")
                .child(model.getPostId())
                .child("likes")
                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            holder.like.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.feellike));
                        } else {
                            holder.like.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    FirebaseDatabase.getInstance().getReference().child("posts")
                                            .child(model.getPostId())
                                            .child("likes")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .setValue(true)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("posts")
                                                            .child(model.getPostId())
                                                            .child("postLike")
                                                            .setValue(model.getPostLike() + 1) // Fixed missing parenthesis here
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    holder.like.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.feellike));

                                                                    NotificationModel notificationModel = new NotificationModel();
                                                                    notificationModel.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                    notificationModel.setNotificationAt(new Date().getTime());
                                                                    notificationModel.setType("like");

                                                                    FirebaseDatabase.getInstance().getReference()
                                                                            .child("notification")
                                                                            .child(model.getPostedBy())
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
        return list2.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        ImageView like, profile_image, post;
        TextView fullName, desc, likecount;

        @SuppressLint("WrongViewCast")
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            like = itemView.findViewById(R.id.like);
            post = itemView.findViewById(R.id.post);
            profile_image = itemView.findViewById(R.id.profile_image);
            fullName = itemView.findViewById(R.id.fullName);
            desc = itemView.findViewById(R.id.desc);
            likecount = itemView.findViewById(R.id.likecount);
        }
    }
}
