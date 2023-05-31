package uk.ac.tees.aad.B1204900.fragments;

import static uk.ac.tees.aad.B1204900.types.Constants.ARG_COURSE_ID;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import uk.ac.tees.aad.B1204900.R;
import uk.ac.tees.aad.B1204900.databinding.FragmentCourseDetailBinding;
import uk.ac.tees.aad.B1204900.models.Course;
import uk.ac.tees.aad.B1204900.models.CourseViewModel;

public class CourseDetailFragment extends Fragment {
    private String mCourseId;
    private Course course;
    private CollapsingToolbarLayout mToolbarLayout;
    View view;

    FragmentCourseDetailBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCourseId = getArguments().getString(ARG_COURSE_ID);
            course = CourseViewModel.courseMap.get(mCourseId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCourseDetailBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        mToolbarLayout = view.findViewById(R.id.toolbar_layout);

        updateContent();

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
}