package com.example.wanandroid.page.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;


import com.example.wanandroid.page.adapter.MyPageChangeCallback;
import com.example.wanandroid.R;
import com.example.wanandroid.page.adapter.FragmentPaperAdapter;
import com.example.wanandroid.page.fragment.HomeFragment;
import com.example.wanandroid.page.fragment.WebViewFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ViewPager2 viewPager2;
    private MyPageChangeCallback myPageChangeCallback;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager2 = findViewById(R.id.viewpager2);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        toolbar = findViewById(R.id.toolbar);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new HomeFragment());
        fragments.add(new HomeFragment());
        fragments.add(new HomeFragment());
        FragmentStateAdapter adapter = new FragmentPaperAdapter(this, fragments);
        viewPager2.setAdapter(adapter);
        viewPager2.setCurrentItem(0);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_home:
                        viewPager2.setCurrentItem(0);
                        break;
                    case R.id.menu_item_question:
                        viewPager2.setCurrentItem(1);
                        break;
                    case R.id.menu_item_topic:
                        viewPager2.setCurrentItem(2);
                        break;
                    case R.id.menu_item_user:
                        viewPager2.setCurrentItem(3);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        myPageChangeCallback = new MyPageChangeCallback(bottomNavigationView, toolbar);
        viewPager2.registerOnPageChangeCallback(myPageChangeCallback);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back, null));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewPager2.unregisterOnPageChangeCallback(myPageChangeCallback);
    }

}
