package com.example.socialmedia.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.Model.NotificationModel;
import com.example.socialmedia.Model.UserModel;
import com.example.socialmedia.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.viewHolder> {

    ArrayList<NotificationModel> list;
    Context context;

    public NotificationAdapter(ArrayList<NotificationModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.demo_notification, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        NotificationModel model = list.get(position);
        String type = model.getType();
        FirebaseDatabase.getInstance().getReference()
                .child("user")
                .child(model.getNotificationBy())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel user = snapshot.getValue(UserModel.class);
                        Picasso.get().load(user.getCoverPhoto())
                                .placeholder(R.drawable.user)
                                .into(holder.storymodel);

                        if (type.equals("like")) {
                            holder.textnoti.setText(Html.fromHtml("<b>" + user.getName() + "</b>" + " liked your post"));
                        } else {
                            holder.textnoti.setText((Html.fromHtml("<b>" + user.getName() + "</b>" + " start following you")));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView storymodel;
        TextView textnoti, time;


        public viewHolder(@NonNull View itemView) {
            super(itemView);

            storymodel = itemView.findViewById(R.id.storydemo);
            textnoti = itemView.findViewById(R.id.textnoti);
            time = itemView.findViewById(R.id.time);

        }
    }
}

