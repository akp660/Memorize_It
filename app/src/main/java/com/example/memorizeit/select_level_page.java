package com.example.memorizeit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class select_level_page extends AppCompatActivity {

    // CardView variables for different difficulty levels
    private CardView easy, medium, hard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_level_page);

        // Handle window insets for padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize CardViews for difficulty levels
        easy = findViewById(R.id.easy);
        medium = findViewById(R.id.medium);
        hard = findViewById(R.id.hard);

        // Set click listeners for each CardView, directly starting the game
        easy.setOnClickListener(v -> startGame(3, 12)); // 3 columns for easy level, 6 pairs of images
        medium.setOnClickListener(v -> startGame(4, 20)); // 4 columns for medium level, 12 pairs of images
        hard.setOnClickListener(v -> startGame(5, 30)); // 5 columns for hard level, 15 pairs of images
    }

    // Function to start game based on selected level, with a default name "Player"
    private void startGame(int cols, int index) {
        Intent intent = new Intent(select_level_page.this, game_page.class);
        intent.putExtra("noOfColumns", cols);
        intent.putExtra("index", index);
        intent.putExtra("playerName", "Player"); // Default name
        startActivity(intent);
        finish();
    }

    // Handle back press to navigate back to the Home_Page
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(select_level_page.this, Home_Page.class));
        finish();
    }
}
