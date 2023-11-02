package com.example.myapplication.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.myapplication.Fragment.AiPoetry.AiPoetryFragment;
import com.example.myapplication.Fragment.My.MyFragment;
import com.example.myapplication.Fragment.Search.SearchFragment;
import com.example.myapplication.Fragment.WebFragment;
import com.example.myapplication.Fragment.association.AssociationFragment;
import com.example.myapplication.Fragment.home.HomeFragment;
import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navView;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //状态栏文字自适应
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initViewPage();
        initBottomNav();

        FragmentManager manager = this.getSupportFragmentManager();
        FragmentTransaction fTransaction = manager.beginTransaction();
        fTransaction.replace(R.id.fragment_search,new SearchFragment());
        fTransaction.commit();

    }

    /**
     * viewPage初始化
     * 获取viewPager，并为其设置适配器
     */
    private void initViewPage() {
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = null;
                /**
                 *页面选中判断，当到达指定position时，返回position对应的页面
                 */
                switch ( position ){
                    case 0:
                        fragment =  new HomeFragment();
                        break;
                    case 1:
                        fragment = new AiPoetryFragment();
                        break;
                    case 2:
//                        requestPermission();
//                        fragment =  new AssociationFragment();
//                        break;
                        fragment =  new WebFragment();
                        break;
                    case 3:
                        fragment =  new MyFragment();
                        break;
                }
                assert fragment != null;
                return fragment;
            }

            /**
             *这里需要填写总的页面数量
             */
            @Override
            public int getCount() {
                return 4;
            }
        });

        //viewPager滑动事件监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                navView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 底部导航栏初始化
     */
    private void initBottomNav() {
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                /**
                 *当点击到菜单中的item时，调用viewPage中的setCurrentItem方法切换到对应界面
                 */
                switch ( item.getItemId() ){
                    case R.id.navigation_home:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.navigation_ai_poetry:
                        viewPager.setCurrentItem(1);
                        break;
//                    case R.id.navigation_association:
//                        viewPager.setCurrentItem(2);
//                        break;
                    case R.id.navigation_web:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.navigation_my:
                        viewPager.setCurrentItem(3);
                        break;
                }
                return false;
            }
        });
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    1234);
        }
    }
}