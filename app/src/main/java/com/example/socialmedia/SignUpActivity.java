package com.example.socialmedia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.socialmedia.Model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
Button login,signupbutton;
EditText name,email,profession,pass;
private FirebaseAuth Auth;
private FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        login = findViewById(R.id.login);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        profession = findViewById(R.id.profession);
        pass = findViewById(R.id.pass);

        signupbutton = findViewById(R.id.signupbutton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpActivity.this , LoginActivity.class);
                startActivity(i);
            }
        });

      signupbutton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              String email1 = email.getText().toString();
              String pass1= pass.getText().toString();
              Auth.createUserWithEmailAndPassword(email1,pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isSuccessful()){
                          UserModel user = new UserModel(name.getText().toString() , email1 , profession.getText().toString() , pass1);
                          String id = task.getResult().getUser().getUid();
                          database.getReference().child("user").child(Auth.getUid()).setValue(user);
                          Toast.makeText(SignUpActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                          Intent i = new Intent(SignUpActivity.this , SplashActivity.class);
                          startActivity(i);
                      }else{
                          Toast.makeText(SignUpActivity.this, "Error", Toast.LENGTH_SHORT).show();
                      }
                  }
              });
          }
      });
    }
}