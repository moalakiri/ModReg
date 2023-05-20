package uk.ac.tees.aad.B1204900.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import uk.ac.tees.aad.B1204900.MainActivity;
import uk.ac.tees.aad.B1204900.contracts.IAuthService;
import uk.ac.tees.aad.B1204900.databinding.FragmentRegisterBinding;
import uk.ac.tees.aad.B1204900.models.User;
import uk.ac.tees.aad.B1204900.utilities.ActivityUtil;

public class UserAuthService implements IAuthService {
    FragmentRegisterBinding _binding;
    Context _context;
    DatabaseReference _databaseReference;
    Activity _requestingActivity;

    public UserAuthService(Context context, FragmentRegisterBinding binding) {
        _binding = binding;
        _context = context;
        _databaseReference = FirebaseDatabase.getInstance().getReference();
        _requestingActivity = ActivityUtil.getActivity(_context);
    }

    @Override
    public void register() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        User user = new User();
        user.setId(firebaseUser.getUid());
        user.setFirstName(_binding.etFirstName.getText().toString());
        user.setLastName(_binding.etLastName.getText().toString());
        user.setEmail(_binding.etEmail.getText().toString());
        user.setPhoneNumber(_binding.etPhone.getText().toString());
        user.setRole(_binding.spRole.getSelectedItem().toString());
        user.setDepartment(_binding.spDepartment.getSelectedItem().toString());

        _databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                _databaseReference.child("users").child(firebaseUser.getUid())
                        .setValue(user);
                Toast.makeText(_context, "Successfully registered", Toast.LENGTH_SHORT).show();
                ActivityUtil.loadUserData(_context);

                _context.startActivity(new Intent(_context, MainActivity.class));
                if (_requestingActivity != null) _requestingActivity.finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(_context, "Error : "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void login() {

    }
}
