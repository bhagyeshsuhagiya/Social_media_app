package com.example.socialmedia;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.socialmedia.Fragment.AddFragment;
import com.example.socialmedia.Fragment.HomeFragment;
import com.example.socialmedia.Fragment.NotiFragment;
import com.example.socialmedia.Fragment.ProfileFragment;
import com.example.socialmedia.Fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

private BottomNavigationView bottomNavigationView;
private FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        frameLayout = findViewById(R.id.frameLayout);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int itemid = menuItem.getItemId();
                if(itemid == R.id.home){
                    loadFragment(new HomeFragment() , false);
                } else if (itemid == R.id.noti) {
                    loadFragment(new NotiFragment() , false);
                } else if (itemid == R.id.add) {
                    loadFragment(new AddFragment() , false);
                } else if (itemid == R.id.search) {
                    loadFragment(new SearchFragment() , false);
                }else{
                    loadFragment(new ProfileFragment(), false);
                }
                return true;
            }
        });
        loadFragment(new HomeFragment() , true);
    }
    private void loadFragment(Fragment fragment ,Boolean isInitialized ){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(isInitialized){
            fragmentTransaction.add(R.id.frameLayout , fragment);
        }else{
            fragmentTransaction.replace(R.id.frameLayout , fragment);
        }
        fragmentTransaction.commit();
        }
    }

