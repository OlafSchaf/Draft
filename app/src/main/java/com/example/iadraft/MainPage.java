package com.example.iadraft;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.iadraft.Fragments.HomeFragment;
import com.example.iadraft.Fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainPage extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment selectorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.nav_home:
                    startActivity(new Intent(MainPage.this , PostActivity.class));
                    break;

                case R.id.nav_add :
                    selectorFragment = null;
                    startActivity(new Intent(MainPage.this , PostActivity.class));
                    break;

                case R.id.nav_profile :
                    startActivity(new Intent(MainPage.this , ProfileActivity.class));
                    break;
        }
     if (selectorFragment != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , selectorFragment).commit();

     }
            return  true;

        });

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            String profileId = intent.getString("publisherId");

            getSharedPreferences("PROFILE", MODE_PRIVATE).edit().putString("profileId", profileId).apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
            bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , new HomeFragment()).commit();
        }
    }
}