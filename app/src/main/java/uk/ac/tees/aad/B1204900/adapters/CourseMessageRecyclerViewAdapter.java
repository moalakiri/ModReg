package uk.ac.tees.aad.B1204900.adapters;

import static uk.ac.tees.aad.B1204900.types.Constants.ARG_COURSE_ID;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.aad.B1204900.R;
import uk.ac.tees.aad.B1204900.databinding.RvCourseItemBinding;
import uk.ac.tees.aad.B1204900.databinding.RvCoursemessageItemBinding;
import uk.ac.tees.aad.B1204900.models.Course;
import uk.ac.tees.aad.B1204900.models.CourseMessage;
import uk.ac.tees.aad.B1204900.types.Constants;
import uk.ac.tees.aad.B1204900.utilities.ActivityUtil;
import uk.ac.tees.aad.B1204900.utilities.TinyDB;

public class CourseMessageRecyclerViewAdapter extends RecyclerView.Adapter<CourseMessageRecyclerViewAdapter
        .RvCourseViewHolder> {
    private List<CourseMessage> messages;
    SharedPreferences sharedPreference;
    Context _context;

    public CourseMessageRecyclerViewAdapter(Context context) {
        _context = context;
        sharedPreference = _context
                .getSharedPreferences(Constants.Tag, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public RvCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvCoursemessageItemBinding binding = RvCoursemessageItemBinding.inflate(LayoutInflater
                .from(parent.getContext()), parent, false);

        return new RvCourseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RvCourseViewHolder holder, int position) {
        holder.tvAuthor.setText(messages.get(position).getAuthor());
        holder.tvMessageBody.setText(messages.get(position).getMessage());
        holder.tvDate.setText(messages.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return messages == null ? 0 : messages.size();
    }

    public static class RvCourseViewHolder extends RecyclerView.ViewHolder{
        final TextView tvAuthor, tvMessageBody, tvDate;

        public RvCourseViewHolder(@NonNull RvCoursemessageItemBinding binding) {
            super(binding.getRoot());
            tvAuthor = binding.tvAuthor;
            tvMessageBody = binding.tvMessageBody;
            tvDate = binding.tvDate;
        }
    }

    public void setMessages(List<CourseMessage> messages) {
        this.messages = messages;
    }
}
