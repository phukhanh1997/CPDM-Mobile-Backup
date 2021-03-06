package com.example.admin.projectcapstonemobile.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.admin.projectcapstonemobile.fragment.AssignedTaskFragment;
import com.example.admin.projectcapstonemobile.fragment.CreatedTaskFragment;
import com.example.admin.projectcapstonemobile.R;
import com.example.admin.projectcapstonemobile.fragment.UserInformationFragment;
import com.example.admin.projectcapstonemobile.remote.ApiUtils;
import com.example.admin.projectcapstonemobile.remote.UserService;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class FragmentActivity extends AppCompatActivity {
    private final String userInformationSharedPreferences = "informationSharedPreferences";
    TabLayout tabLayout;
    ViewPager viewPager;
    private UserService userService;
    private Integer userId;
    private String userToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        userService = ApiUtils.getUserService();
        SharedPreferences sharedPreferences = getSharedPreferences(userInformationSharedPreferences, Context.MODE_PRIVATE);
        userToken = sharedPreferences.getString("userToken", "");

        String checkCode = "";
        checkCode = (String) getIntent().getSerializableExtra("CheckCode");
        System.out.println("Check code de hien thi la" + checkCode);
        setupViewPager(viewPager);
        if (checkCode == null) {
            viewPager.setCurrentItem(1);
        }
        if (checkCode != null) {
            viewPager.setCurrentItem(2);
            checkCode = null;
        }

        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CreatedTaskFragment(), "Tác vụ                  đã giao");
        adapter.addFragment(new AssignedTaskFragment(), "Tác vụ        tham gia");
        adapter.addFragment(new UserInformationFragment(), "Quản lý    người dùng");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

}
