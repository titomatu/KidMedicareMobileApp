package org.tripulantesg2.kidmedicare.view.ui.slideshow;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.tripulantesg2.kidmedicare.R;
import org.tripulantesg2.kidmedicare.databinding.FragmentSlideshowBinding;

import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;
    private static final String TAG = "tester";

    //FirebaseFirestore DataBase
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    String user_image_url;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        loadData();

//        final TextView textView = binding.textSlideshow;
//        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadData() {
        try {
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();

            db = FirebaseFirestore.getInstance();

            final View viewGallery = binding.getRoot();
            final TextView textViewNombreUsuario = viewGallery.findViewById(R.id.textViewNombreUsuarioData);
            final TextView textViewCorreoUsuario = viewGallery.findViewById(R.id.textViewCorreoUsuarioData);
            final TextView textCreationDateData = viewGallery.findViewById(R.id.textCreationDateData);
            final CircleImageView profileImageView = viewGallery.findViewById(R.id.profileImageViewData);

            //Load Data
            textViewCorreoUsuario.setText(currentUser.getEmail());

            DocumentReference docRef = db.collection("user_info").document(currentUser.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        if (document.exists()) {
                            String formatDate = new SimpleDateFormat("dd/MM/yyyy")
                                    .format(document.getTimestamp("creation_date").toDate());
                            textViewNombreUsuario.setText(document.getString("name"));
                            textCreationDateData.setText("Fecha de Afiliación: " + formatDate);
                            user_image_url = document.getString("image_url");

                            //Load User Profile Image
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference gsReference = storage.getReferenceFromUrl(user_image_url);
                            getDonwloadUrl(gsReference);
                            //Use Glide to load image
                            Glide.with(viewGallery)
                                    .load(currentUser.getPhotoUrl())
                                    .into(profileImageView);
                        } else {
                            Log.w(TAG, "=============>>> No such document");
                        }
                    } else {
                        Log.w(TAG, "=============>>> get failed with ", task.getException());
                    }
                }
            });

        } catch(Exception exc){
            Log.w(TAG, "=================> " + exc.getMessage());
        }
    }

    private void getDonwloadUrl(StorageReference storage){
        storage.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.w(TAG, "=================> " + uri);
                        setUserProfileUrl(uri);
                    }
                });
    }

    private void setUserProfileUrl(Uri uri){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        user.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.w(TAG, "=============>>> user profile image loaded");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "=============>>> user profile image not loaded");
                    }
                });
    }
}