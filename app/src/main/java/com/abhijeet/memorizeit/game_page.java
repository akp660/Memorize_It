package com.abhijeet.memorizeit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abhijeet.memorizeit.adapter.LevelAdapter;

import java.util.ArrayList;
import java.util.Collections;

public class game_page extends AppCompatActivity {

    // Constants
    private static final long COUNTDOWN_DURATION = 4000;
    private static final int COUNTDOWN_INTERVAL = 1000;

    // Views
    private RecyclerView recyclerView;
    private ImageView winGameImageView, pauseButton;
    private TextView countdownTextView, timerTextView;
    private View blackOverlay, touchInterceptor;

    // Game State Variables
    private final ArrayList<Integer> imageList = new ArrayList<>();
    private Handler handler;
    private long startTime, elapsedTime;
    private boolean isCountdownFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_page);

        // Initialize views
        initializeViews();

        // Get Intent data
        Intent intent = getIntent();
        int numberOfColumns = intent.getIntExtra("noOfColumns", 0);
        int numberOfItems = intent.getIntExtra("index", 0);
        String playerName = intent.getStringExtra("playerName");

        // Initialize image resources and setup RecyclerView
        initializeImageList();
        setupRecyclerView(numberOfColumns, numberOfItems, playerName);

        // Start countdown before game starts
        startCountdown();

        // Set up pause button click listener
        pauseButton.setOnClickListener(v -> pauseGame());
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerView);
        winGameImageView = findViewById(R.id.winGame);
        countdownTextView = findViewById(R.id.countdown);
        blackOverlay = findViewById(R.id.blackOverlay);
        touchInterceptor = findViewById(R.id.touchInterceptor);
        timerTextView = findViewById(R.id.timer);
        pauseButton = findViewById(R.id.pauseTimer);
        handler = new Handler();

        // Setup window insets for padding
        setupWindowInsets();
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeImageList() {
        Collections.addAll(imageList,
                R.drawable.i1, R.drawable.i1,
                R.drawable.i2, R.drawable.i2,
                R.drawable.i3, R.drawable.i3,
                R.drawable.i4, R.drawable.i4,
                R.drawable.i5, R.drawable.i5,
                R.drawable.i6, R.drawable.i6,
                R.drawable.i7, R.drawable.i7,
                R.drawable.i8, R.drawable.i8,
                R.drawable.i9, R.drawable.i9,
                R.drawable.i10, R.drawable.i10,
                R.drawable.i11, R.drawable.i11,
                R.drawable.i12, R.drawable.i12,
                R.drawable.i13, R.drawable.i13,
                R.drawable.i14, R.drawable.i14,
                R.drawable.i15, R.drawable.i15);
    }

    private void setupRecyclerView(int numberOfColumns, int numberOfItems, String playerName) {
        // Ensure sublist does not exceed imageList size
        int validNumberOfItems = Math.min(numberOfItems, imageList.size());

        LevelAdapter adapter = new LevelAdapter(this, imageList.subList(0, validNumberOfItems), recyclerView, winGameImageView, numberOfColumns, timerTextView, pauseButton, playerName);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        recyclerView.setAdapter(adapter);
    }

    private void startCountdown() {
        countdownTextView.setVisibility(View.VISIBLE);
        blackOverlay.setVisibility(View.VISIBLE);
        touchInterceptor.setVisibility(View.VISIBLE);

        new CountDownTimer(COUNTDOWN_DURATION, COUNTDOWN_INTERVAL) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                countdownTextView.setText(secondsRemaining > 0 ? String.valueOf(secondsRemaining) : "Start!");
            }

            public void onFinish() {
                countdownTextView.setVisibility(View.GONE);
                blackOverlay.setVisibility(View.GONE);
                touchInterceptor.setVisibility(View.GONE);
                isCountdownFinished = true;
                startTimer();
            }
        }.start();
    }

    private void startTimer() {
        startTime = System.currentTimeMillis();
        handler.post(timerRunnable);
    }

    private void stopTimer() {
        handler.removeCallbacks(timerRunnable);
    }

    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long currentElapsedMillis = System.currentTimeMillis() - startTime + elapsedTime;
            int minutes = (int) (currentElapsedMillis / 60000);
            int seconds = (int) (currentElapsedMillis / 1000) % 60;
            int milliseconds = (int) (currentElapsedMillis % 1000);

            String timeString = String.format("%02d:%02d:%03d", minutes, seconds, milliseconds);
            timerTextView.setText(timeString);
            handler.postDelayed(this, 1);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isCountdownFinished) {
            elapsedTime += System.currentTimeMillis() - startTime;
            stopTimer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isCountdownFinished) {
            startTimer(); // Start the timer without resetting elapsedTime
        }
    }

    private void pauseGame() {
        elapsedTime += System.currentTimeMillis() - startTime;
        stopTimer();

        new AlertDialog.Builder(this)
                .setTitle("Go Back?")
                .setMessage("All your progress will be lost")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, i) -> {
                    startActivity(new Intent(this, select_level_page.class));
                    finish();
                })
                .setNegativeButton("No", (dialog, i) -> {
                    dialog.dismiss();
                    startTimer(); // Continue the timer from where it left off
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (timerTextView.getVisibility() == View.GONE) {
            startActivity(new Intent(this, select_level_page.class));
            finish();
        } else {
            pauseGame();
        }
    }

    public void startAnimation(View v) {
        v.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(50)
                .setInterpolator(new DecelerateInterpolator())
                .withEndAction(() -> v.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(50)
                        .setInterpolator(new DecelerateInterpolator())
                        .start()).start();
    }
}
