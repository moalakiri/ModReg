package uk.ac.tees.aad.B1204900.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import uk.ac.tees.aad.B1204900.models.User;
import uk.ac.tees.aad.B1204900.types.Constants;

public class ActivityUtil {
    public static Activity getActivity(Context context)
    {
        if (context == null)
        {
            return null;
        }
        else if (context instanceof ContextWrapper)
        {
            if (context instanceof Activity)
            {
                return (Activity) context;
            }
            else
            {
                return getActivity(((ContextWrapper) context).getBaseContext());
            }
        }

        return null;
    }

    public static void loadUserData(Context context) {
        DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.child("users").child(FirebaseAuth.getInstance()
                                                        .getCurrentUser().getUid())
                                                        .getValue(User.class);

                SharedPreferences sharedPreference = context
                        .getSharedPreferences(Constants.Tag, Context.MODE_PRIVATE);

                sharedPreference.edit()
                        .putString(Constants.UserRoleTag, user.getRole())
                        .putString(Constants.userEmail, user.getEmail())
                        .putString(Constants.UserDepartmentTag, user.getDepartment())
                        .putString(Constants.userFullName, user.getFirstName() + " " + user.getLastName())
                        .commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG);

            }
        });

    }
}
