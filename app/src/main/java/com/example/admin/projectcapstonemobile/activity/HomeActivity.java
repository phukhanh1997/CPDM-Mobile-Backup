package com.example.admin.projectcapstonemobile.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.admin.projectcapstonemobile.R;
import com.example.admin.projectcapstonemobile.fragment.AssignedTaskFragment;
import com.example.admin.projectcapstonemobile.fragment.CreatedTaskFragment;
import com.example.admin.projectcapstonemobile.fragment.TakeLeaveFragment;
import com.example.admin.projectcapstonemobile.fragment.ViewLeaveRequestFragment;
import com.example.admin.projectcapstonemobile.fragment.ViewLeaveStaffFragment;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initialView();
        initialData();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new AssignedTaskFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_assginedTask);
    }

    private void initialView() {
        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initialData() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_open);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_assginedTask:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AssignedTaskFragment()).commit();
                break;
            case R.id.nav_created_task:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CreatedTaskFragment()).commit();
                break;
            case R.id.nav_take_leave:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TakeLeaveFragment()).commit();
                break;
            case R.id.nav_take_leave_list:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ViewLeaveRequestFragment()).commit();
                break;
            case R.id.nav_view_staff_leave:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ViewLeaveStaffFragment()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
