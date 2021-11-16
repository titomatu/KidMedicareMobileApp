package org.tripulantesg2.kidmedicare.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.tripulantesg2.kidmedicare.MainActivity;
import org.tripulantesg2.kidmedicare.R;

public class LoginActivity extends AppCompatActivity {

    Button sign_in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Init
        sign_in = findViewById(R.id.sign_in_login_btn);

        //Event
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(LoginActivity.this, MainMenuActivity.class);
                startActivity(next);
            }
        });
        /*sign_in.setOnClickListener(v->{

        });*/
    }
}