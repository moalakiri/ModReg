package uk.ac.tees.aad.B1204900;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.aad.B1204900.databinding.ActivityMainBinding;
import uk.ac.tees.aad.B1204900.fragments.CourseDetailFragment;
import uk.ac.tees.aad.B1204900.types.Constants;
import uk.ac.tees.aad.B1204900.ui.home.HomeFragment;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private Context _context;
    TextView txtEmail, txtFullName, txtRole;

    SharedPreferences sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        _context = this;

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreference = _context
                .getSharedPreferences(Constants.Tag, Context.MODE_PRIVATE);

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_create_course,
                R.id.nav_user_profile)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        txtEmail = binding.navView.getHeaderView(0).findViewById(R.id.txtViewEmail);
        txtFullName = binding.navView.getHeaderView(0).findViewById(R.id.txtFullName);
        txtRole = txtEmail = binding.navView.getHeaderView(0).findViewById(R.id.txtRole);

        writeMenuUserProfile();

        if (sharedPreference.getString(Constants.UserRoleTag, "role")
                .equalsIgnoreCase(Constants.userRoleTutor))
        {
            binding.appBarMain.fab.setOnClickListener(view -> Navigation
                    .findNavController(this, R.id.nav_host_fragment_content_main)
                    .navigate(R.id.nav_create_course));
        }else{
            binding.appBarMain.fab.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void signOut() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        sharedPreference.edit().clear().apply();
        Intent intent = new Intent(this, GuestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_logout){
            signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    private void writeMenuUserProfile(){
        txtEmail.setText(sharedPreference.getString(Constants.userEmail, ""));
        txtFullName.setText(sharedPreference.getString(Constants.userFullName, ""));
        txtRole.setText(sharedPreference.getString(Constants.UserRoleTag, ""));
    }

    public void reloadActivity(){
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onResume() {
        writeMenuUserProfile();
        super.onResume();
    }
}