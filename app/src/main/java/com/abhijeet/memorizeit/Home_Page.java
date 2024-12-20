package com.abhijeet.memorizeit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Home_Page extends AppCompatActivity {

    CardView i_btn, play_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        i_btn = findViewById(R.id.i_btn);
        play_btn = findViewById(R.id.play_btn);




// onClick method for Play Button.
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to open the desired activity
                Intent intent = new Intent(Home_Page.this, select_level_page.class);
                startActivity(intent);
            }
        });



// onClick method for Tutorial Button.
        i_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to open the desired activity
                Intent intent = new Intent(Home_Page.this, tutorial_page.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Close the app when the back button is pressed
        super.onBackPressed();
        finishAffinity(); // Finishes all activities and closes the app
    }

}