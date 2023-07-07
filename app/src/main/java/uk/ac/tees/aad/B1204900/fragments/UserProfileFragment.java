package uk.ac.tees.aad.B1204900.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.ac.tees.aad.B1204900.MainActivity;
import uk.ac.tees.aad.B1204900.R;
import uk.ac.tees.aad.B1204900.databinding.FragmentCreateCourseBinding;
import uk.ac.tees.aad.B1204900.databinding.FragmentUserProfileBinding;
import uk.ac.tees.aad.B1204900.types.Constants;
import uk.ac.tees.aad.B1204900.utilities.CustomConverters;

public class UserProfileFragment extends Fragment {
    private static final int CAMERA_REQUEST = 1888;
    Context _context;
    View view;
    FragmentUserProfileBinding binding;
    SharedPreferences sharedPreference;

    public UserProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _context = getContext();
        binding = FragmentUserProfileBinding.inflate(getLayoutInflater());
        sharedPreference = _context
                .getSharedPreferences(Constants.Tag, Context.MODE_PRIVATE);

        binding.tvFullName.setText(sharedPreference.getString(Constants.userFullName, ""));
        binding.tvDepartment.setText(sharedPreference.getString(Constants.UserDepartmentTag, ""));
        binding.tvRole.setText(sharedPreference.getString(Constants.UserRoleTag, ""));

        if (!sharedPreference.getString(Constants.UserImageTag, "").equalsIgnoreCase("")){
            Bitmap photo = CustomConverters.StringToBitMap(sharedPreference.getString(Constants.UserImageTag, ""));
            binding.imgViewUser.setImageBitmap(photo);
        }

        binding.imgViewUser.setOnClickListener(view -> {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = binding.getRoot();
        return view;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            Bitmap photoBM = (Bitmap) data.getExtras().get("data");
            String photoStr = CustomConverters.BitMapToString(photoBM);
            sharedPreference.edit().putString(Constants.UserImageTag, photoStr).commit();
            Bitmap photo = CustomConverters.StringToBitMap(photoStr);
            binding.imgViewUser.setImageBitmap(photo);
        }
    }
}