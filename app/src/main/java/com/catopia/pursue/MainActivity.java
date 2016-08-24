package com.catopia.pursue;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.IdentityManager;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        JobFilterFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener
{
    /**
     * Logging tag for this class.
     */
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    /**
     * The identity manager used to keep track of the current user account.
     */
    private IdentityManager identityManager;

    /**
     * This will be all the user's details that is attached to their facebook or google account.
     */
    private TextView userNameTextView;
    private ImageView userPicImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Obtain a reference to the mobile client. It is created in the Application class,
        // but in case a custom Application class is not used, we initialize it here if necessary.
        AWSMobileClient.initializeMobileClientIfNecessary(this);
        // Obtain a reference to the identity manager.
        identityManager = AWSMobileClient.defaultMobileClient().getIdentityManager();

        setContentView(R.layout.activity_main);
        // Attach toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Attach Nav drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        // Set the nav view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Initialize header
        View header = navigationView.getHeaderView(0);
        // Reference views to the nav header
        userNameTextView = (TextView) header.findViewById(R.id.userName);
        userPicImageView = (ImageView) header.findViewById(R.id.userProfilePic);
        // method call to retrieve user name and picture
        fetchUserIdentity();
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home)
        {

        }
        else if (id == R.id.nav_filter)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.containerFragment, new JobFilterFragment()).commit();
        }
        else if (id == R.id.nav_saved_job)
        {

        }
        else if (id == R.id.nav_share)
        {

        }
        else if (id == R.id.nav_setting)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.containerFragment, new SettingsFragment()).commit();
        }
        else if (id == R.id.nav_sign_out)
        {
            // The user is currently signed in with a provider. Sign out of that provider.
            identityManager.signOut();
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Fetches the user identity safely on the background thread.  It may make a network call.
     */
    public void fetchUserIdentity()
    {
        Log.d(LOG_TAG, "fetchUserIdentity");

        AWSMobileClient.defaultMobileClient().getIdentityManager().getUserID(new IdentityManager.IdentityHandler()
        {
            @Override
            public void handleIdentityID(String identityId)
            {
                clearUserInfo();
                // We have successfully retrieved the user's identity. You can use the
                // user identity value to uniquely identify the user. For demonstration
                // purposes here, we will display the value in a text view.
                if (identityManager.isUserSignedIn())
                {

                    userNameTextView.setText(identityManager.getUserName());

                    if (identityManager.getUserImage() != null)
                    {
                        userPicImageView.setImageBitmap(identityManager.getUserImage());
                    }
                }
            }

            @Override
            public void handleError(Exception exception)
            {
                clearUserInfo();

                new AlertDialog.Builder(getApplicationContext())
                        .setTitle(R.string.alert_error_title)
                        .setMessage(getString(R.string.alert_error_message)
                                + exception.getMessage())
                        .setNegativeButton(R.string.alert_error_dismiss, null)
                        .create()
                        .show();
            }
        });
    }

    private void clearUserInfo()
    {
        clearUserImage();

        try
        {
            userNameTextView.setText(getString(R.string.unknown_user));
        }
        catch (final IllegalStateException e)
        {
            // This can happen when app shuts down and activity is gone
            Log.w(LOG_TAG, "Unable to reset user name back to default.");
        }
    }

    private void clearUserImage()
    {
        try
        {
            userPicImageView.setImageResource(R.mipmap.user);
        }
        catch (final IllegalStateException e)
        {
            // This can happen when app shuts down and activity is gone
            Log.w(LOG_TAG, "Unable to reset user image back to default image.");
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }
}
