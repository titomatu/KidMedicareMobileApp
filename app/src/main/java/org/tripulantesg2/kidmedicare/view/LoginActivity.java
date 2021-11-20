package org.tripulantesg2.kidmedicare.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.tripulantesg2.kidmedicare.MainActivity;
import org.tripulantesg2.kidmedicare.R;

public class LoginActivity extends AppCompatActivity {

    Button sign_in;
    private FirebaseAuth mAuth;
    private static final String TAG = "tester";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        //Init
        sign_in = findViewById(R.id.sign_in_login_btn);
        TextInputEditText textEmail = findViewById(R.id.textEmail);
        TextInputEditText textPassword = findViewById(R.id.textPassword);

        //Event
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = textEmail.getText().toString();
                String password = textPassword.getText().toString();

                Log.w(TAG, "==================>>>> " + email);
                Log.w(TAG, "==================>>>> " + password);

                signIn(email,password);
                //Intent next = new Intent(LoginActivity.this, MainMenuActivity.class);
                //startActivity(next);
            }
        });
    }

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.w(TAG, "==================>>>> usuario logueado: " + user.getEmail());
                            //updateUI(user);
                            Intent next = new Intent(LoginActivity.this, MainMenuActivity.class);
                            startActivity(next);
                        } else{
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "signInWithEmail:success");
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_LONG).show();
                            //updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        Log.d("tester","test");
        Toast.makeText(LoginActivity.this, ""+user.toString(),
                Toast.LENGTH_SHORT).show();
    }

}