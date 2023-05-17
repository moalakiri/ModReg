package uk.ac.tees.aad.B1204900;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager2.widget.ViewPager2;

import uk.ac.tees.aad.B1204900.adapaters.AuthenticationPagerAdapter;
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
    }
}