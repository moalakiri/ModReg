package uk.ac.tees.aad.B1204900.fragments;

import static uk.ac.tees.aad.B1204900.types.Constants.ARG_COURSE_ID;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import uk.ac.tees.aad.B1204900.MainActivity;
import uk.ac.tees.aad.B1204900.R;
import uk.ac.tees.aad.B1204900.adapters.CourseMessageRecyclerViewAdapter;
import uk.ac.tees.aad.B1204900.adapters.CourseRecyclerViewAdapter;
import uk.ac.tees.aad.B1204900.databinding.FragmentCourseDetailBinding;
import uk.ac.tees.aad.B1204900.models.Course;
import uk.ac.tees.aad.B1204900.models.CourseMessage;
import uk.ac.tees.aad.B1204900.models.CourseMessageViewModel;
import uk.ac.tees.aad.B1204900.models.CourseViewModel;
import uk.ac.tees.aad.B1204900.types.Constants;

public class CourseDetailFragment extends Fragment {
    private String mCourseId;
    private Course course;
    private CollapsingToolbarLayout mToolbarLayout;
    View view;
    CourseMessageRecyclerViewAdapter adapter;
    CourseMessageViewModel courseMessageViewModel;
    Context _context;
    DatabaseReference databaseReference;

    FragmentCourseDetailBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _context = getContext();
        if (getArguments() != null) {
            mCourseId = getArguments().getString(ARG_COURSE_ID);
            course = CourseViewModel.courseMap.get(mCourseId);
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCourseDetailBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        mToolbarLayout = view.findViewById(R.id.toolbar_layout);

        updateContent();

        courseMessageViewModel = new ViewModelProvider(this).get(CourseMessageViewModel.class);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_context);
        binding.rvMessages.setLayoutManager(linearLayoutManager);
        adapter = new CourseMessageRecyclerViewAdapter(_context);
        binding.rvMessages.setAdapter(adapter);
        binding.rvMessages.addItemDecoration(new DividerItemDecoration(_context, linearLayoutManager.getOrientation()));

        courseMessageViewModel.getCourseMessages(mCourseId).observe(getActivity(), courseMessages -> adapter.setMessages(courseMessages));

        binding.fab.setOnClickListener(view -> postMessage());


        return view;
    }

    private void updateContent() {
        if (course != null) {
            binding.courseDetail.setText(course.getCode()+"\n"+course.getTitle()+"\n"+course.getDepartment());
            if (mToolbarLayout != null) {
                mToolbarLayout.setTitle(course.getTitle());
            }
        }
    }

    void postMessage(){
        if (validatedData()){
            CourseMessage message = new CourseMessage();

            SharedPreferences sharedPreference = _context
                    .getSharedPreferences(Constants.Tag, Context.MODE_PRIVATE);

            NavController navController = Navigation.findNavController(getActivity(),
                    R.id.nav_host_fragment_content_main);

            message.setMessage(binding.etPost.getText().toString().trim());
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            message.setDateCreated(dateFormat.format(new Date()));

            message.setAuthor(sharedPreference.getString(Constants.userFullName, ""));

            message.setId(UUID.randomUUID().toString());
            message.setCourseId(mCourseId);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<CourseMessage> adapterMsgs =
                            adapter.getMessages() != null ?
                                    adapter.getMessages() : new ArrayList<>();
                    databaseReference.child("messages").child(message.getId())
                            .setValue(message).addOnSuccessListener(unused -> {
                                Toast.makeText(_context, "Successfully posted message",
                                        Toast.LENGTH_SHORT).show();
                                binding.etPost.setText("");

                                if (adapterMsgs.stream()
                                        .filter(courseMessage -> courseMessage.getId()
                                                .equalsIgnoreCase(message.getId()))
                                        .count() < 1){
                                    adapterMsgs.add(message);
                                    adapter.setMessages(adapterMsgs);
                                }
                            });
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
        if (TextUtils.isEmpty(binding.etPost.getText().toString())){
            binding.etPost.setError("Field is required");
            return false;
        }

        return true;
    }

}