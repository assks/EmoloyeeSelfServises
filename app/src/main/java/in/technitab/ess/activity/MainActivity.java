package in.technitab.ess.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.AlarmReceiver;
import in.technitab.ess.NotificationScheduler;
import in.technitab.ess.R;
import in.technitab.ess.fragment.AddVendorFragment;
import in.technitab.ess.fragment.AdminFragment;
import in.technitab.ess.fragment.AttendanceLeaveFragment;
import in.technitab.ess.fragment.ExpenseFragment;
import in.technitab.ess.fragment.PayrollFragment;
import in.technitab.ess.fragment.ProjectTaskFragment;
import in.technitab.ess.fragment.PunchInFragment;
import in.technitab.ess.fragment.ResourceFragment;
import in.technitab.ess.fragment.TimeSheetFragment;
import in.technitab.ess.fragment.TravelBookingFragment;
import in.technitab.ess.util.ReminderData;
import in.technitab.ess.util.UserPref;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private UserPref userPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        userPref = new UserPref(this);
        setSupportActionBar(toolbar);
        showHideNavigationMenu();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(this);

        View view = navView.getHeaderView(0);
        ImageView imageView = view.findViewById(R.id.user_icon);
        TextView userName = view.findViewById(R.id.user_name);
        userName.setText(userPref.getName());
        TextView appVersion = view.findViewById(R.id.app_version);
        appVersion.setText(getResources().getString(R.string.app_version,getVersionInfo()));

        Drawable drawable = getResources().getDrawable(R.mipmap.user);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.circleCrop();
        Glide.with(this).load(drawable).apply(requestOptions).into(imageView);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,UserProfileActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });


        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            displaySelectedView(R.id.nav_attendance_leave);
        else {
            startPunchInFragment();
            navView.setCheckedItem(R.id.nav_punch_in_out);
        }

        setPunchReminder();
    }


    private String getVersionInfo() {
        String versionName = "";
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    private void startPunchInFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new PunchInFragment());
        ft.commit();

        navView.setCheckedItem(R.id.nav_attendance_leave);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(String title) {
        toolbar.setTitle(title);
    }

    public void setToolBarSubtitle(String subtitle){
        if (subtitle != null) {
            toolbar.setSubtitleTextColor(getResources().getColor(R.color.colorSecondary));
            toolbar.setSubtitle(subtitle);
        }
        else {
         toolbar.setSubtitle(null);
        }
    }

    private void showHideNavigationMenu() {
        Menu nav_Menu = navView.getMenu();
        if (userPref.getAccessControlId() == getResources().getInteger(R.integer.user))
            nav_Menu.findItem(R.id.nav_admin).setVisible(false);

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedView(item.getItemId());
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    private void setPunchReminder(){
        ReminderData localData = new ReminderData(this);
        NotificationScheduler.setPunchInReminder(this, AlarmReceiver.class, localData.getPunchInHour(), localData.getPunchInMin());
        NotificationScheduler.setPunchOutReminder(this, AlarmReceiver.class, localData.getPunchOutHour(), localData.getPunchOutMin());
    }


    private void displaySelectedView(int id) {
        navView.setCheckedItem(id);
        Fragment mFragment = null;
        switch (id) {

            case R.id.nav_attendance_leave:
                mFragment = new AttendanceLeaveFragment();
                break;

            case R.id.nav_punch_in_out:
                break;

            case R.id.nav_timesheet:
                mFragment = new TimeSheetFragment();
                break;

            case R.id.nav_admin:
                mFragment = new AdminFragment();
                break;

            case R.id.nav_vendor:
                mFragment = new AddVendorFragment();
                break;

            case R.id.nav_expense:
                mFragment = new ExpenseFragment();
                break;

            case R.id.nav_travel:
                mFragment = new TravelBookingFragment();
                break;

            case R.id.nav_payroll:
                mFragment = new PayrollFragment();
                break;

            case R.id.nav_project_task:
                mFragment = new ProjectTaskFragment();
                break;

            case R.id.nav_resource:
                mFragment = new ResourceFragment();
        }

        if (mFragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, mFragment);
            ft.commit();
        } else {
            startActivity(new Intent(this, FetchLocationActivity.class));
        }

        drawerLayout.closeDrawer(GravityCompat.START);
    }


}
