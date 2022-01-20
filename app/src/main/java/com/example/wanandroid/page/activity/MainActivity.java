package com.example.wanandroid.page.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.wanandroid.page.adapter.MyPageChangeCallback;
import com.example.wanandroid.R;
import com.example.wanandroid.page.adapter.FragmentPaperAdapter;
import com.example.wanandroid.page.fragment.HomeFragment;
import com.example.wanandroid.page.fragment.SearchViewFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private MyPageChangeCallback myPageChangeCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager2 = findViewById(R.id.viewpager2);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        Toolbar toolbar = findViewById(R.id.toolbar);

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
        //setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrowleft);
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_more));
        toolbar.inflateMenu(R.menu.activity_home_menu);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.toolbar_search:
                        SearchViewFragment searchViewFragment = new SearchViewFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.addToBackStack(null);
                        transaction.add(R.id.fragment_webview, searchViewFragment);
                        transaction.commit();
                        Toast.makeText(MainActivity.this, fragmentManager.toString(), Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewPager2.unregisterOnPageChangeCallback(myPageChangeCallback);
    }
}
