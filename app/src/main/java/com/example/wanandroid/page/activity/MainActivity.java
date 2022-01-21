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

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.wanandroid.page.adapter.HomePageChangeCallback;
import com.example.wanandroid.R;
import com.example.wanandroid.page.adapter.FragmentPaperAdapter;
import com.example.wanandroid.page.fragment.HomeFragment;
import com.example.wanandroid.page.fragment.SearchViewFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.ArrayList;

/**
 * 采用单Activity多Fragment
 * 主界面用 ToolBar+BottomNavigationView + ViewPager2 + Fragment
 *
 * @author WhiteNight123 (Guo Xiaoqiang
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity: ";
    private ViewPager2 mViewPager2;
    private HomePageChangeCallback mHomePageChangeCallback;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void initView() {
        mViewPager2 = findViewById(R.id.activity_main_viewpager2);
        BottomNavigationView bottomNavigationView = findViewById(R.id.activity_main_bnv);
        mToolbar = findViewById(R.id.activity_main_toolbar);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new HomeFragment());
        fragments.add(new HomeFragment());
        fragments.add(new HomeFragment());
        FragmentStateAdapter adapter = new FragmentPaperAdapter(this, fragments);
        mViewPager2.setAdapter(adapter);
        mViewPager2.setCurrentItem(0);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.main_menu_item_home:
                        mViewPager2.setCurrentItem(0);
                        break;
                    case R.id.main_menu_item_question:
                        mViewPager2.setCurrentItem(1);
                        break;
                    case R.id.main_menu_item_topic:
                        mViewPager2.setCurrentItem(2);
                        break;
                    case R.id.main_menu_item_user:
                        mViewPager2.setCurrentItem(3);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        mHomePageChangeCallback = new HomePageChangeCallback(bottomNavigationView, mToolbar);
        mViewPager2.registerOnPageChangeCallback(mHomePageChangeCallback);

        //setSupportActionBar(toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrowleft);
        mToolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_more));
        mToolbar.inflateMenu(R.menu.activity_home_menu);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.main_toolbar_search:
                        SearchViewFragment searchViewFragment = new SearchViewFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.addToBackStack(null);
                        transaction.add(R.id.fragment_main, searchViewFragment);
                        transaction.commit();
                        break;
                    case R.id.main_toolbar_setting:
                        Toast.makeText(MainActivity.this, "设置", Toast.LENGTH_SHORT);
                        Log.e(TAG, "onMenuItemClick: " + "设置显示了吗");
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
        mViewPager2.unregisterOnPageChangeCallback(mHomePageChangeCallback);
    }
}
