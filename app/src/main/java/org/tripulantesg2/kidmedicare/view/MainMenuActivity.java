package org.tripulantesg2.kidmedicare.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import org.tripulantesg2.kidmedicare.MainActivity;
import org.tripulantesg2.kidmedicare.databinding.ActivityMainMenuBinding;
import org.tripulantesg2.kidmedicare.R;
import org.tripulantesg2.kidmedicare.view.ui.home.HomeFragment;

import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainMenuBinding binding;
    private NavController navController;
    private static final String TAG = "tester";

    //FirebaseFirestore DataBase
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    String user_image_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMainMenu.toolbar);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_menu);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_menu);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        binding.appBarMainMenu.fab.setOnClickListener(this::onClick);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_menu);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_logout:
                mAuth.signOut();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

                Log.w(TAG, "======== logout");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    private void loadData() {
        try {
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            Log.w(TAG, "=================> info del usuario logueado: " + currentUser.getEmail());

            db = FirebaseFirestore.getInstance();

            final View viewMenuHeader = binding.navView.getHeaderView(0);
            final TextView textViewNombreUsuario = viewMenuHeader.findViewById(R.id.textViewNombreUsuario);
            final TextView textViewCorreoUsuario = viewMenuHeader.findViewById(R.id.textViewCorreoUsuario);
            final CircleImageView profileImageView = viewMenuHeader.findViewById(R.id.profileImageView);

            //Load Data
            //textViewNombreUsuario.setText("Tito Andrés Maturana de la Cruz");
            textViewCorreoUsuario.setText(currentUser.getEmail());

            Log.w(TAG, "=================> uid: " + currentUser.getUid());

            DocumentReference docRef = db.collection("user_info").document(currentUser.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.w(TAG, "=============>>> DocumentSnapshot data: " + document.getData());
                            textViewNombreUsuario.setText(document.getString("name"));
                            user_image_url = document.getString("image_url");

                            //Load User Profile Image
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference gsReference = storage.getReferenceFromUrl(user_image_url);
                            Log.w(TAG, "=============>>> user_image_url: " + user_image_url);
                            getDonwloadUrl(gsReference);
                            //Use Glide to load image
                            Glide.with(viewMenuHeader)
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

    @Override
    public void onClick(View v) {
        navController.navigate(R.id.nav_home);
    }
}