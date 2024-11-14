package com.example.socialmedia.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.socialmedia.Model.PostModel;
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

import java.util.Date;

public class AddFragment extends Fragment {
    FirebaseAuth auth;
    FirebaseDatabase database;

    TextView vfullname, vwork;
    ImageView vprofile_image, vpost, vgallery;
    EditText vdesc;
    Button vbutton;
    FirebaseStorage storage;
    Uri uri;
    View view;
    ProgressDialog dialog;

    public AddFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(getContext());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        dialog = new ProgressDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add, container, false);

        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Post Uploading");
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        vbutton = view.findViewById(R.id.button);
        vdesc = view.findViewById(R.id.desc);
        vdesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String description = vdesc.getText().toString();
                if (!description.isEmpty()) {
                    vbutton.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.postbutton));
                    vbutton.setTextColor(getContext().getResources().getColor(R.color.white));
                    vbutton.setEnabled(true);
                } else {
                    vbutton.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.postbutton));
                    vbutton.setTextColor(getContext().getResources().getColor(R.color.black));
                    vbutton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

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
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        vfullname = view.findViewById(R.id.fullName);
        vprofile_image = view.findViewById(R.id.profile_image);
        vwork = view.findViewById(R.id.work);
        vpost = view.findViewById(R.id.post);
        vgallery = view.findViewById(R.id.gallery);
        vbutton = view.findViewById(R.id.button);

        vgallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i, 2);
            }
        });

        vbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                final StorageReference reference = storage.getReference().child("posts")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child((String.valueOf(new Date().getTime())));
                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                PostModel post = new PostModel();
                                post.setPostImage(uri.toString());
                                post.setPostedBy(FirebaseAuth.getInstance().getUid());
                                post.setPostDescription(vdesc.getText().toString());
                                post.setPostedAt(new Date().getTime());

                                database.getReference().child("posts")
                                        .push()
                                        .setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                String currentUserID = FirebaseAuth.getInstance().getUid();
                                                database.getReference().child("user")
                                                        .child(currentUserID)
                                                        .child("postCount")
                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                long postCount = 0;
                                                                if (snapshot.exists()) {
                                                                    postCount = snapshot.getValue(Long.class);
                                                                }
                                                                database.getReference().child("user")
                                                                        .child(currentUserID)
                                                                        .child("postCount")
                                                                        .setValue(postCount + 1);
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {
                                                            }
                                                        });

                                                dialog.dismiss();
                                                Toast.makeText(getContext(), "posted successfully", Toast.LENGTH_SHORT).show();


                                            }
                                        });
                            }
                        });
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getData() != null) {
            uri = data.getData();
            vpost.setImageURI(uri);
            vbutton.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.postbutton));
            vbutton.setTextColor(getContext().getResources().getColor(R.color.white));
            vbutton.setEnabled(true);
        }
    }
}
