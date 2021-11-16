package org.tripulantesg2.kidmedicare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import org.tripulantesg2.kidmedicare.view.LoginActivity;

public class MainActivity extends AppCompatActivity {
    ImageButton botonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botonLogin = (ImageButton) findViewById(R.id.next_step_btn);


    }

    public void pantallaLogin(View view){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}