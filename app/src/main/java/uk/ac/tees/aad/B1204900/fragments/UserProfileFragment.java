package uk.ac.tees.aad.B1204900.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.ac.tees.aad.B1204900.R;
import uk.ac.tees.aad.B1204900.databinding.FragmentCreateCourseBinding;
import uk.ac.tees.aad.B1204900.databinding.FragmentUserProfileBinding;

public class UserProfileFragment extends Fragment {
    private static final int CAMERA_REQUEST = 1888;
    Context _context;
    View view;
    FragmentUserProfileBinding binding;

    public UserProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _context = getContext();
        binding = FragmentUserProfileBinding.inflate(getLayoutInflater());
        binding.imgViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
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
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            binding.imgViewUser.setImageBitmap(photo);
        }
    }
}