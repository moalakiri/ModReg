package uk.ac.tees.aad.B1204900.models;

import android.content.Context;
import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
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

import uk.ac.tees.aad.B1204900.types.Constants;

public class CourseViewModel extends AndroidViewModel {
    static SharedPreferences sharedPreference;
    static Context context;
    Application _application;
    private static MutableLiveData<List<Course>> courses = new MutableLiveData<>();
    public static Map<String, Course> courseMap = new HashMap<>();

    public CourseViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        sharedPreference = context
                .getSharedPreferences(Constants.Tag, Context.MODE_PRIVATE);
    }

    public static LiveData<List<Course>> getCourses(){
            FirebaseDatabase.getInstance().getReference("courses")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        List<Course> fbCourses = new ArrayList<>();
                        for (DataSnapshot snp: snapshot.getChildren()
                             ) {
                            Course fbCourse = snp.getValue(Course.class);
                            if (sharedPreference.getString(Constants.UserRoleTag,"")
                                    .equalsIgnoreCase(Constants.userRoleTutor) && !fbCourse
                                    .getTutorName()
                                    .equalsIgnoreCase(sharedPreference
                                            .getString(Constants.userFullName, ""))){

                            }else{
                                fbCourses.add(fbCourse);
                                courseMap.put(fbCourse.getId(), fbCourse);
                            }
                        }
                        if (fbCourses.size() > 0)
                            courses.postValue(fbCourses);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        return courses;
    }
}
