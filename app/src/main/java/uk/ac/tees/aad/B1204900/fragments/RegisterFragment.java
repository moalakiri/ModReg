package uk.ac.tees.aad.B1204900.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import static com.sucho.placepicker.Constants.*;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.Constants;
import com.sucho.placepicker.PlacePicker;

import uk.ac.tees.aad.B1204900.MainActivity;
import uk.ac.tees.aad.B1204900.R;
import uk.ac.tees.aad.B1204900.databinding.FragmentRegisterBinding;
import uk.ac.tees.aad.B1204900.services.UserAuthService;

public class RegisterFragment extends Fragment {
    FragmentRegisterBinding binding;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    private FusedLocationProviderClient fusedLocationClient;

    private Double myLatitude = Double.valueOf(0);

    private Double myLongitude = Double.valueOf(0);
    View view;
    String email = "", password = "";
    ProgressDialog progressDialog;

    Context context;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this.getContext();
        //initialize firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Creating your account...");
        progressDialog.setCanceledOnTouchOutside(false);

        //initialize view binding
        binding = FragmentRegisterBinding.inflate(getLayoutInflater());

        //set register button onclick event
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

        binding.etAddress.setOnClickListener(view -> {

            if (ContextCompat.checkSelfPermission(
                    getActivity(),
                    Manifest.permission
                            .ACCESS_FINE_LOCATION)
                    == PackageManager
                    .PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(
                    getActivity(),
                    Manifest.permission
                            .ACCESS_COARSE_LOCATION)
                    == PackageManager
                    .PERMISSION_GRANTED) {
                getCurrentLocation();
            }
            else {
                // When permission is not granted
                // Call method
                requestPermissions(
                        new String[] {
                                Manifest.permission
                                        .ACCESS_FINE_LOCATION,
                                Manifest.permission
                                        .ACCESS_COARSE_LOCATION },
                        100);
            }
            String aKey = getString(R.string.mapkey);
            Intent intent = new PlacePicker.IntentBuilder()
                    .setLatLong(myLatitude, myLongitude)
                    .setPlaceSearchBar(true, aKey)
                    .setAddressRequired(true)
                    .build(getActivity());

            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = binding.getRoot();
        return view;
    }

    void validateData(){
        email = binding.etEmail.getText().toString().trim();
        password = binding.etPassword.getText().toString().trim();
        //validate
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.etEmail.setError("Invalid email address format");
        }else if (TextUtils.isEmpty(password)){
            binding.etPassword.setError("Enter password");
        }else if (password.length() < 6){
            binding.etPassword.setError("Password must be at least 6 characters");
        }else if(binding.spRole.getSelectedItemPosition() < 1){
            Toast.makeText(context, "Please select a role", Toast.LENGTH_SHORT).show();
        }
        else{
            SignUp();
        }

    }

    private void SignUp() {
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //success
                        progressDialog.dismiss();
                        //get user info
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        //String email = firebaseUser.getEmail();
                        Toast.makeText(getContext(), "Account created\n"+email, Toast.LENGTH_SHORT).show();
                        firebaseAuth.signInWithEmailAndPassword(email, password);

                        UserAuthService authService = new UserAuthService(context,binding);

                        authService.register();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failure
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Error : "+e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });
        }

    private void SignIn() {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //success
                        Toast.makeText(getContext(), "Successfully signed in", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(getActivity(), MainActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failure
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Error : "+e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                AddressData addressData = data.getParcelableExtra(Constants.ADDRESS_INTENT);
                if (addressData.getAddressList().size() > 0)
                    binding.etAddress.setText(addressData.getAddressList()
                            .get(0).getAddressLine(0));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation()
    {
        // Initialize Location manager
        LocationManager locationManager
                = (LocationManager)getActivity()
                .getSystemService(
                        Context.LOCATION_SERVICE);
        // Check condition
        if (locationManager.isProviderEnabled(
                LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER)) {
            // When location service is enabled
            // Get last location
            fusedLocationClient.getLastLocation().addOnCompleteListener(
                    new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(
                                @NonNull Task<Location> task)
                        {

                            // Initialize location
                            Location location
                                    = task.getResult();
                            // Check condition
                            if (location != null) {
                                // When location result is not
                                // null set latitude
                                myLatitude = location.getLatitude();
                                myLongitude = location.getLongitude();

                            }
                            else {
                                // When location result is null
                                // initialize location request
                                LocationRequest locationRequest
                                        = new LocationRequest()
                                        .setPriority(
                                                LocationRequest
                                                        .PRIORITY_HIGH_ACCURACY)
                                        .setInterval(10000)
                                        .setFastestInterval(
                                                1000)
                                        .setNumUpdates(1);

                                // Initialize location call back
                                LocationCallback
                                        locationCallback
                                        = new LocationCallback() {
                                    @Override
                                    public void
                                    onLocationResult(
                                            LocationResult
                                                    locationResult)
                                    {
                                        // Initialize
                                        // location
                                        Location location1
                                                = locationResult
                                                .getLastLocation();
                                        // Set latitude
                                        myLatitude = location1.getLatitude();
                                        myLongitude = location1.getLongitude();
                                    }
                                };

                                // Request location updates
                                fusedLocationClient.requestLocationUpdates(
                                        locationRequest,
                                        locationCallback,
                                        Looper.myLooper());
                            }
                        }
                    });
        }
        else {
            // When location service is not enabled
            // open location setting
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
        // Check condition
        if (requestCode == 100 && (grantResults.length > 0)
                && (grantResults[0] + grantResults[1]
                == PackageManager.PERMISSION_GRANTED)) {
            // When permission are granted
            // Call  method
            getCurrentLocation();
        }
        else {
            // When permission are denied
            // Display toast
            Toast
                    .makeText(getActivity(),
                            "Permission denied",
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }
}