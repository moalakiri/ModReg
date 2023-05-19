package uk.ac.tees.aad.B1204900.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import uk.ac.tees.aad.B1204900.MainActivity;
import uk.ac.tees.aad.B1204900.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {
    FragmentLoginBinding binding;
    FirebaseAuth firebaseAuth;
    View view;
    String email = "", password = "";
    ProgressDialog progressDialog;
    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize firebase
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Signing you in...");
        progressDialog.setCanceledOnTouchOutside(false);

        //initialize view binding
        binding = FragmentLoginBinding.inflate(getLayoutInflater());

        //set register button onclick event
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
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
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //success
                        progressDialog.dismiss();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = binding.getRoot();
        return view;
    }
}