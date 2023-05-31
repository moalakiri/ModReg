package uk.ac.tees.aad.B1204900.models;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.tees.aad.B1204900.types.Constants;

public class CourseMessageViewModel extends AndroidViewModel {
    static SharedPreferences sharedPreference;
    static Context context;
    Application _application;
    private static MutableLiveData<List<CourseMessage>> messages = new MutableLiveData<>();

    public CourseMessageViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        sharedPreference = context
                .getSharedPreferences(Constants.Tag, Context.MODE_PRIVATE);
    }

    public static LiveData<List<CourseMessage>> getCourseMessages(String courseId){
            FirebaseDatabase.getInstance().getReference("messages")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        List<CourseMessage> fbMessages = new ArrayList<>();
                        for (DataSnapshot snp: snapshot.getChildren()
                             ) {
                            CourseMessage fbMessage = snp.getValue(CourseMessage.class);
                            if (fbMessage.getCourseId().equalsIgnoreCase(courseId))
                                fbMessages.add(fbMessage);
                        }
                        if (fbMessages.size() > 0)
                            messages.postValue(fbMessages);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        return messages;
    }
}
