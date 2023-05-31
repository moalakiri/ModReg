package uk.ac.tees.aad.B1204900.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import uk.ac.tees.aad.B1204900.MainActivity;
import uk.ac.tees.aad.B1204900.R;
import uk.ac.tees.aad.B1204900.databinding.RvCourseItemBinding;
import uk.ac.tees.aad.B1204900.models.Course;
import uk.ac.tees.aad.B1204900.types.Constants;
import uk.ac.tees.aad.B1204900.utilities.ActivityUtil;
import uk.ac.tees.aad.B1204900.utilities.TinyDB;

public class CourseRecyclerViewAdapter extends RecyclerView.Adapter<CourseRecyclerViewAdapter
        .RvCourseViewHolder> {
    private List<Course> courses;
    SharedPreferences sharedPreference;
    private List<String> courseIds;
    DatabaseReference _databaseReference;
    Context _context;

    public CourseRecyclerViewAdapter(Context context) {
        _context = context;
        sharedPreference = _context
                .getSharedPreferences(Constants.Tag, Context.MODE_PRIVATE);
        _databaseReference = FirebaseDatabase.getInstance().getReference("course_enrolments");
    }

    @NonNull
    @Override
    public RvCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvCourseItemBinding binding = RvCourseItemBinding.inflate(LayoutInflater
                .from(parent.getContext()), parent, false);

        return new RvCourseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RvCourseViewHolder holder, int position) {

        holder.tvCourseCode.setText(courses.get(position).getCode());
        holder.tvCourseTitle.setText(courses.get(position).getTitle());
        holder.tvCourseDepartment.setText(courses.get(position).getDepartment() + " Department");
        holder.tvCourseLecturer.setText("Lectured by "+courses.get(position).getTutorName());
        setDepartmentImage(holder, courses.get(position).getDepartment());
        if (sharedPreference.getString(Constants.UserRoleTag, "role")
                .equalsIgnoreCase(Constants.userRoleStudent) &&
                !courseIds.contains(courses.get(position).getId())){
            holder.btnEnrol.setOnClickListener(view -> {
                addEnrolment(courses.get(position));
                holder.btnEnrol.setVisibility(View.GONE);

            });
        }else{
            holder.btnEnrol.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return courses == null ? 0 : courses.size();
    }

    public static class RvCourseViewHolder extends RecyclerView.ViewHolder{
        final TextView tvCourseCode, tvCourseTitle, tvCourseDepartment, tvCourseLecturer;
        final Button btnEnrol;
        final ImageView imageView;

        public RvCourseViewHolder(@NonNull RvCourseItemBinding binding) {
            super(binding.getRoot());
            tvCourseCode = binding.tvCourseCode;
            tvCourseTitle = binding.tvCourseTitle;
            tvCourseDepartment = binding.tvCourseDepartment;
            tvCourseLecturer = binding.tvCourseLecturer;
            btnEnrol = binding.btnEnrol;
            imageView = binding.imgView;
        }
    }

    public void setCoursesList(List<Course> courses) {
        this.courses = courses;
    }

    public void setEnrolments(List<String> courses) {
        this.courseIds = courses;
    }

    private void setDepartmentImage(RvCourseViewHolder holder, String department){
        switch (department.toLowerCase()){
            case "artificial intelligence":
            case "data science and analytics":
                holder.imageView.setImageResource(R.drawable.ic_data_science_ai);
                break;
            case "computer science":
                holder.imageView.setImageResource(R.drawable.ic_computer_science);
                break;
            case "business administration":
                holder.imageView.setImageResource(R.drawable.ic_business_administration);
                break;
        }
    }

    private void addEnrolment(Course course){
        TinyDB tinyDB = new TinyDB(_context);
        ArrayList<String> enrolments = tinyDB.getListString(Constants.MyEnrolments);
        enrolments.add(course.getId());
        tinyDB.putListString(Constants.MyEnrolments, enrolments);
        Toast.makeText(_context, "Successfully enrolled in "+course.getCode(),
                        Toast.LENGTH_LONG).show();

    }
}
