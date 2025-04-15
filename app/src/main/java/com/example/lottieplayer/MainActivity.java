package com.example.lottieplayer;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.airbnb.lottie.LottieAnimationView;

public class MainActivity extends AppCompatActivity {

    private LottieAnimationView animationView;
    private Button playButton;
    private Button pauseButton;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        animationView = findViewById(R.id.animation_view);


        animationView.setAnimation("animation.json");


        playButton = findViewById(R.id.play_button);
        pauseButton = findViewById(R.id.pause_button);


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationView.playAnimation();
                isPlaying = true;
                updateButtonVisibility();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationView.pauseAnimation();
                isPlaying = false;
                updateButtonVisibility();
            }
        });


        updateButtonVisibility();
    }

    private void updateButtonVisibility() {
        playButton.setVisibility(isPlaying ? View.GONE : View.VISIBLE);
        pauseButton.setVisibility(isPlaying ? View.VISIBLE : View.GONE);
    }
}