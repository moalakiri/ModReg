package uk.ac.tees.aad.B1204900.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;
import java.util.UUID;

import uk.ac.tees.aad.B1204900.MainActivity;
import uk.ac.tees.aad.B1204900.R;
import uk.ac.tees.aad.B1204900.databinding.FragmentCreateCourseBinding;
import uk.ac.tees.aad.B1204900.databinding.FragmentLoginBinding;
import uk.ac.tees.aad.B1204900.models.Course;
import uk.ac.tees.aad.B1204900.models.User;
import uk.ac.tees.aad.B1204900.types.Constants;
import uk.ac.tees.aad.B1204900.utilities.ActivityUtil;

public class CreateCourseFragment extends Fragment {
    FragmentCreateCourseBinding binding;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    View view;
    Context _context;
    User user;

    public CreateCourseFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _context = getContext();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //initialize firebase
        firebaseAuth = FirebaseAuth.getInstance();
        //initialize view binding
        binding = FragmentCreateCourseBinding.inflate(getLayoutInflater());

        //set register button onclick event
        binding.btnSaveCourse.setOnClickListener(view -> saveCourse());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = binding.getRoot();
        return view;
    }

    void saveCourse(){
        if (validatedData()){
            Course course = new Course();

            SharedPreferences sharedPreference = _context
                    .getSharedPreferences(Constants.Tag, Context.MODE_PRIVATE);

            NavController navController = Navigation.findNavController(getActivity(),
                    R.id.nav_host_fragment_content_main);

            course.setTitle(binding.etTitle.getText().toString().trim());
            course.setCode(binding.etCode.getText().toString().trim());
            course.setTutorName(sharedPreference.getString(Constants.userFullName, ""));
            course.setDepartment(sharedPreference.getString(Constants.UserDepartmentTag,""));
            course.setId(UUID.randomUUID().toString());

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    databaseReference.child("courses").child(course.getId())
                            .setValue(course);
                    Toast.makeText(_context, "Successfully registered a new course",
                            Toast.LENGTH_SHORT).show();

                    navController.navigate(R.id.nav_home);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(_context, "Error : "+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean validatedData() {

        //validate
        if (TextUtils.isEmpty(binding.etCode.getText().toString())){
            binding.etCode.setError("Course code is required");
            return false;
        }

        if (TextUtils.isEmpty(binding.etTitle.getText().toString())){
            binding.etTitle.setError("Course title is required");
            return false;
        }

        return true;
    }
}