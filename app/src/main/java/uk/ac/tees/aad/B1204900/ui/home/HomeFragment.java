package uk.ac.tees.aad.B1204900.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.ac.tees.aad.B1204900.MainActivity;
import uk.ac.tees.aad.B1204900.adapters.CourseRecyclerViewAdapter;
import uk.ac.tees.aad.B1204900.databinding.FragmentHomeBinding;
import uk.ac.tees.aad.B1204900.models.Course;
import uk.ac.tees.aad.B1204900.models.CourseViewModel;
import uk.ac.tees.aad.B1204900.types.Constants;
import uk.ac.tees.aad.B1204900.utilities.TinyDB;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    CourseRecyclerViewAdapter adapter;
    CourseViewModel courseViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.rvCourses.setLayoutManager(linearLayoutManager);
        adapter = new CourseRecyclerViewAdapter(getContext());
        binding.rvCourses.setAdapter(adapter);
        binding.rvCourses.addItemDecoration(new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation()));
        TinyDB tinyDB = new TinyDB(getContext());

        courseViewModel.getCourses().observe(getActivity(), courses -> {

            adapter.setCoursesList(courses);
            adapter.setEnrolments(tinyDB.getListString(Constants.MyEnrolments));
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume(){
        super.onResume();
        courseViewModel.getCourses().observe(getActivity(), courses ->
                adapter.setCoursesList(courses));
    }

}