package com.recifit.application;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.bumptech.glide.Glide;
import com.recifit.application.callback.SessionCallback;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.recifit.application.common.ProfileData;
import com.recifit.application.databinding.ActivityMainBinding;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.recifit.application.LoginActivity;
import com.recifit.application.R;
import com.recifit.application.ui.recipeTest.ResultFragment;
import com.recifit.application.ui.recipeTest.TestFragment;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private static final String LOG_TAG = "MainActivity";
    private static int flag = 0;
    // menu item initialization
    private SessionCallback sessionCallback = new SessionCallback();
    public String strNickname, strProfile, strEmail, strUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_recipe, R.id.nav_myRecipe)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        /**
         * Drawer 및 Navigation Setting 후 호출
         */
        View headerView = navigationView.getHeaderView(0);
        TextView nickName = headerView.findViewById(R.id.nickName);
        nickName.setText(ProfileData.getNickName());
        ImageView profileImageUrl = headerView.findViewById(R.id.profileImageUrl);
        Glide.with(this)
                .load(ProfileData.getThumbnail())
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
                .fallback(R.mipmap.ic_launcher_round)
                .into(profileImageUrl);

        String[] array = getResources().getStringArray(R.array.nohow);
        if(flag == 0) {
            LayoutInflater inflater = this.getLayoutInflater();
            View titleView = inflater.inflate(R.layout.custom_title, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this).setCustomTitle(titleView);

            Random rand = new Random();
            int i = rand.nextInt(20);
            builder.setMessage(array[i]);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            flag++;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Menu item selection
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                UserManagement.getInstance()
                        .requestLogout(new LogoutResponseCallback() {
                            @Override
                            public void onSessionClosed(ErrorResult errorResult) {
                                super.onSessionClosed(errorResult);
                                Log.d(LOG_TAG, "onSessionClosed: " + errorResult.getErrorMessage());
                            }
                            @Override
                            public void onCompleteLogout() {
                                if (sessionCallback != null) {
                                    Session.getCurrentSession().removeCallback(sessionCallback);
                                }
                                Log.d(LOG_TAG, "onCompleteLogout:logout ");
                            }
                        });
                Intent i = new Intent(this, LoginActivity.class);
                this.startActivity(i);
                finish();
                return true;
            case R.id.recipeAnimation:
                this.startActivity(new Intent(this,SplashActivity.class));
                Toast.makeText(getApplicationContext(), "오늘의 자글자글 레시피", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //프래그먼트 이동용
    public void onFragmentChange(int index, int tag){
        TestFragment testFragment = new TestFragment();
        ResultFragment resultFragment = new ResultFragment();

        Bundle bundle = new Bundle(1);
        bundle.putInt("key", tag);
        testFragment.setArguments(bundle);
        resultFragment.setArguments(bundle);

        if(index == 3){
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.test_fragment , resultFragment).commit();
        }


        //여기서 다른 프래그먼트로 이동하는 기능 구현가능
    }

    @Override
    protected void onDestroy() {
        flag = 0;
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy");
    }

}