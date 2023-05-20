package uk.ac.tees.aad.B1204900.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import uk.ac.tees.aad.B1204900.MainActivity;
import uk.ac.tees.aad.B1204900.databinding.FragmentLoginBinding;
import uk.ac.tees.aad.B1204900.models.User;
import uk.ac.tees.aad.B1204900.utilities.ActivityUtil;

public class LoginFragment extends Fragment {
    FragmentLoginBinding binding;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    View view;
    String email = "", password = "";
    ProgressDialog progressDialog;
    Context _context;
    User user;
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _context = getContext();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //initialize firebase
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Signing you in...");
        progressDialog.setCanceledOnTouchOutside(false);

        //initialize view binding
        binding = FragmentLoginBinding.inflate(getLayoutInflater());

        //set register button onclick event
        binding.btnLogin.setOnClickListener(view -> validateData());
    }

    private void validateData() {
        email = binding.etEmail.getText().toString().trim();
        password = binding.etPassword.getText().toString().trim();
        //validate
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.etEmail.setError("Invalid email address format");
        }else if (TextUtils.isEmpty(password)){
            binding.etPassword.setError("Enter password");
        }else if (password.length() < 6){
            binding.etPassword.setError("Password must be at least 6 characters");
        }else{
            SignIn();
        }
    }

    private void SignIn() {
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    //success
                    ActivityUtil.loadUserData(_context);
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Successfully signed in", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getActivity(), MainActivity.class));
                })
                .addOnFailureListener(e -> {
                    //failure
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Error : "+e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = binding.getRoot();
        return view;
    }
}