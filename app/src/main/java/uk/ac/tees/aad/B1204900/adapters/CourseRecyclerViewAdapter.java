package uk.ac.tees.aad.B1204900.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uk.ac.tees.aad.B1204900.databinding.RvCourseItemBinding;
import uk.ac.tees.aad.B1204900.models.Course;

public class CourseRecyclerViewAdapter extends RecyclerView.Adapter<CourseRecyclerViewAdapter.RvCourseViewHolder> {
    private List<Course> courses;

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
        holder.btnEnrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Hello Button", Toast.LENGTH_LONG).show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Hello ItemView", Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return courses == null ? 0 : courses.size();
    }

    public class RvCourseViewHolder extends RecyclerView.ViewHolder{
        final TextView tvCourseCode, tvCourseTitle, tvCourseDepartment, tvCourseLecturer;
        final Button btnEnrol;

        public RvCourseViewHolder(@NonNull RvCourseItemBinding binding) {
            super(binding.getRoot());
            tvCourseCode = binding.tvCourseCode;
            tvCourseTitle = binding.tvCourseTitle;
            tvCourseDepartment = binding.tvCourseDepartment;
            tvCourseLecturer = binding.tvCourseLecturer;
            btnEnrol = binding.btnEnrol;
        }
    }

    public void setCoursesList(List<Course> courses) {
        this.courses = courses;
    }
}
