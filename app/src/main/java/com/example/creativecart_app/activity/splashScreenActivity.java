package com.example.creativecart_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.creativecart_app.R;

import java.util.Objects;

public class splashScreenActivity extends AppCompatActivity {

    private MotionLayout motionLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        try {
//            Objects.requireNonNull(getSupportActionBar()).hide();
//
//        }catch (Exception e){
//          //  Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
//        }


        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        motionLayout=findViewById(R.id.mainLayout);
        motionLayout.startLayoutAnimation();

        motionLayout.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {

            }
        });
    }
}