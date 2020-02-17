package com.deepmodi.shareden;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.deepmodi.shareden.common.Common;
import com.deepmodi.shareden.ui.Home.HomeFragment;
import com.deepmodi.shareden.ui.MyAccount.MyAccountFragment;
import com.deepmodi.shareden.ui.MyPost.MyPostFragment;
import com.deepmodi.shareden.ui.Search.SearchFragment;
import com.esotericsoftware.kryo.serializers.DefaultArraySerializers;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import android.provider.Settings;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    //FrameLayout frameLayout;
    //private int STORAGE_PERMISSION_CODE = 123;
    //final Fragment fragment1 = new HomeFragment();
    //final Fragment fragment2 = new SearchFragment();
    //final Fragment fragment3 = new MyAccountFragment();
    //final Fragment fragment4 = new MyPostFragment();
    //final FragmentManager fm = getSupportFragmentManager();
    //Fragment active;
    public static final int PERMISSION_REQUEST_CODE = 101;
    private ViewPager viewPager;
    private BottomNavigationView navigation;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Paper init
        Paper.init(this);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white));
        }

        requestStoragePermissionUsingDexter();
        viewPager = findViewById(R.id.viewPager);

        //frameLayout = findViewById(R.id.frame_layout);
        navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.setSelectedItemId(R.id.action_home);


        //fm.beginTransaction().add(R.id.frame_layout,fragment4,"4").hide(fragment4).commit();
        //fm.beginTransaction().add(R.id.frame_layout, fragment3, "3").hide(fragment3).commit();
        //fm.beginTransaction().add(R.id.frame_layout, fragment2, "2").hide(fragment2).commit();
        //fm.beginTransaction().add(R.id.frame_layout,fragment1, "1").commit();

        SetupViewPager();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.action_home:
                //fm.beginTransaction().hide(active).show(fragment1).commit();
                //active = fragment1;
                viewPager.setCurrentItem(0);
                return false;
            case R.id.action_search:
                //fm.beginTransaction().hide(active).show(fragment2).commit();
                //active = fragment2;
                viewPager.setCurrentItem(1);
                return false;
            case R.id.action_account:
                //fm.beginTransaction().hide(active).show(fragment3).commit();
                //active = fragment3;
                viewPager.setCurrentItem(2);
                return false;
            case R.id.action_my_post:
                //fm.beginTransaction().hide(active).show(fragment4).commit();
                //active = fragment4;
                viewPager.setCurrentItem(3);
                return false;
        }
        return true;

    }
    /*
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed for load the images from your phone.")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }
     */


    private void initFragments(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        MyPostFragment postFragment = MyPostFragment.newInstance();
        MyAccountFragment accountFragment = MyAccountFragment.newInstance();
        HomeFragment homeFragment = HomeFragment.newInstance();
        SearchFragment searchFragment = SearchFragment.newInstance();
        adapter.addFragment(homeFragment);
        adapter.addFragment(postFragment);
        adapter.addFragment(searchFragment);
        adapter.addFragment(accountFragment);
        viewPager.setAdapter(adapter);
       // active = postFragment;
    }

    private void SetupViewPager()
    {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(menuItem!=null)
                {
                    menuItem.setCheckable(false);
                }
                else
                {
                    navigation.getMenu().getItem(0).setChecked(false);
                }

                navigation.getMenu().getItem(position).setChecked(false);
                menuItem = navigation.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initFragments(viewPager);
    }


    private void requestStoragePermissionUsingDexter()
    {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE
                ,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA
                ,Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted())
                        {

                        }

                        if(report.isAnyPermissionPermanentlyDenied())
                        {
                            ShowDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                    }
                })
                .withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(HomeActivity.this,"Problem in Grating permissions.",Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void ShowDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("All permissions are needed");
        builder.setMessage("If you want to use some of the best features of the app you need to these all of the permissions." +
                "Otherwise you are not be able to use these features.");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openSettings();
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
                .show();
    }

    private void openSettings()
    {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",getPackageName(),null);
        intent.setData(uri);
        startActivityForResult(intent,PERMISSION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PERMISSION_REQUEST_CODE && resultCode == RESULT_OK && data!=null)
        {
            Toast.makeText(this, "Permission Granted.", Toast.LENGTH_SHORT).show();
        }
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter
    {
        private final List<Fragment> fragments = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fragmentManager)
        {
            super(fragmentManager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment)
        {
            fragments.add(fragment);
        }
    }
}

