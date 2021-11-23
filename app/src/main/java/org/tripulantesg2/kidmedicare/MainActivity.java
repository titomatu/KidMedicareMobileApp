package org.tripulantesg2.kidmedicare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.tripulantesg2.kidmedicare.view.LoginActivity;
import org.tripulantesg2.kidmedicare.view.MainMenuActivity;

public class MainActivity extends AppCompatActivity {
    ImageButton botonLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        botonLogin = (ImageButton) findViewById(R.id.next_step_btn);


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void pantallaLogin(View view){
        FirebaseUser currentuser = mAuth.getCurrentUser();

        if(currentuser==null){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else{
            Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
            startActivity(intent);
        }

    }

}