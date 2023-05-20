package uk.ac.tees.aad.B1204900.models;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseViewModel extends ViewModel {
    private static MutableLiveData<List<Course>> courses = new MutableLiveData<>();
    public static Map<String, Course> courseMap = new HashMap<>();

    public static LiveData<List<Course>> getCourses(){
        if (courses.getValue() == null){
            FirebaseDatabase.getInstance().getReference("courses")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        List<Course> fbCourses = new ArrayList<>();
                        for (DataSnapshot snp: snapshot.getChildren()
                             ) {
                            Course fbCourse = snp.getValue(Course.class);
                            fbCourses.add(fbCourse);
                            courseMap.put(fbCourse.getId(), fbCourse);

                        }
                        if (fbCourses.size() > 0)
                            courses.postValue(fbCourses);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        return courses;
    }
}
