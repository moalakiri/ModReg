package uk.ac.tees.aad.B1204900;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager2.widget.ViewPager2;

import uk.ac.tees.aad.B1204900.adapters.AuthenticationPagerAdapter;
import uk.ac.tees.aad.B1204900.databinding.ActivityGuestBinding;
import uk.ac.tees.aad.B1204900.fragments.LoginFragment;
import uk.ac.tees.aad.B1204900.fragments.RegisterFragment;
import uk.ac.tees.aad.B1204900.fragments.SplashFragment;

public class GuestActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityGuestBinding binding;
    View view;
    ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGuestBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser fbUser = auth.getCurrentUser();

        if (fbUser != null) {
            startActivity(new Intent(GuestActivity.this, MainActivity.class));
        } else {
            viewPager = binding.viewPager;
            AuthenticationPagerAdapter pagerAdapter = new AuthenticationPagerAdapter(getSupportFragmentManager(), getLifecycle());
            pagerAdapter.addFragment(new SplashFragment());
            pagerAdapter.addFragment(new LoginFragment());
            pagerAdapter.addFragment(new RegisterFragment());
            viewPager.setAdapter(pagerAdapter);

            //      set handler to run task for specific time interval
            new Handler().postDelayed(() -> {
                pagerAdapter.removeFragment(0);
                viewPager.setAdapter(pagerAdapter);
            }, 2000);
        }

        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                fineLocationGranted = result.getOrDefault(
                                        android.Manifest.permission.ACCESS_FINE_LOCATION, false);
                            }
                            Boolean coarseLocationGranted = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                coarseLocationGranted = result.getOrDefault(
                                        android.Manifest.permission.ACCESS_COARSE_LOCATION,false);
                            }
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                            } else {
                                // No location access granted.
                            }
                        }
                );

// ...

// Before you perform the actual permission request, check whether your app
// already has the permissions, and whether your app needs to show a permission
// rationale dialog. For more details, see Request permissions.
        locationPermissionRequest.launch(new String[] {
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }
}