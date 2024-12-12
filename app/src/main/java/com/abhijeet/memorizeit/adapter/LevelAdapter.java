package com.abhijeet.memorizeit.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.abhijeet.memorizeit.R;
import com.abhijeet.memorizeit.Shared_Preference_Helper;
import com.abhijeet.memorizeit.game_page;
import com.abhijeet.memorizeit.select_level_page;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.ViewHolder> {
    private final Context context;
    private final List<Integer> animalList;
    private final RecyclerView recyclerView;
    private final ImageView winGame, pauseGame;
    private final TextView timerTextView;
    private final Stack<Integer> imgStack = new Stack<>();
    private final Stack<ImageView> imgOfBlock = new Stack<>();
    private final Shared_Preference_Helper sharedPreferencesHelper;
    private final int noOfCols;
    private int top = 0, count = 0;
    private final String playerName;

    public LevelAdapter(Context context, List<Integer> animalList, RecyclerView recyclerView,
                        ImageView winGame, int noOfCols, TextView timerTextView,
                        ImageView pauseGame, String playerName) {
        this.context = context;
        this.animalList = animalList;
        this.recyclerView = recyclerView;
        this.winGame = winGame;
        this.pauseGame = pauseGame;
        this.noOfCols = noOfCols;
        this.timerTextView = timerTextView;
        this.playerName = playerName;
        Collections.shuffle(animalList);
        sharedPreferencesHelper = new Shared_Preference_Helper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.box_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setCardViewDimensions(holder.cardView);

        holder.imgBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAnimation(view);
                handleImageSelection(holder);
            }
        });
    }

    private void setCardViewDimensions(CardView cardView) {
        ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
        if (noOfCols == 5) {
            layoutParams.height = 164;
            layoutParams.width = 164;
        } else if (noOfCols == 4) {
            layoutParams.height = 200;
            layoutParams.width = 200;
        } else {
            layoutParams.height = 250;
            layoutParams.width = 250;
        }
        cardView.setLayoutParams(layoutParams);
    }

    private void handleImageSelection(ViewHolder holder) {
        top++;
        if (top < 3) {
            holder.imgBlock.setImageResource(animalList.get(holder.getAdapterPosition()));
            holder.imgBlock.setEnabled(false);
            imgStack.push(animalList.get(holder.getAdapterPosition()));
            imgOfBlock.push(holder.imgBlock);
        }
        if (top == 2) {
            evaluateSelection();
        }
    }

    private void evaluateSelection() {
        int img1 = imgStack.pop();
        int img2 = imgStack.pop();
        ImageView imageView2 = imgOfBlock.pop();
        ImageView imageView1 = imgOfBlock.pop();

        imageView1.setEnabled(true);
        imageView2.setEnabled(true);

        if (img1 == img2) {
            count++;
            new Handler().postDelayed(() -> {
                imageView2.setVisibility(View.INVISIBLE);
                imageView1.setVisibility(View.INVISIBLE);
                top = 0;
                checkWinCondition();
            }, 1000);
        } else {
            new Handler().postDelayed(() -> {
                imageView2.setImageResource(R.drawable.box);
                imageView1.setImageResource(R.drawable.box);
                top = 0;
            }, 1000);
        }
    }

    private void checkWinCondition() {
        if (count == getItemCount() / 2) {
            Log.d("Game Status", "You won!");
            winGame();
        }
    }

    @Override
    public int getItemCount() {
        return animalList.size();
    }

    private void startAnimation(View view) {
        view.animate()
                .scaleX(0.85f)
                .scaleY(0.85f)
                .setDuration(50)
                .setInterpolator(new DecelerateInterpolator())
                .withEndAction(() -> view.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(50)
                        .setInterpolator(new DecelerateInterpolator())
                        .start())
                .start();
    }

    public void winGame() {
        recyclerView.setVisibility(View.GONE);
        winGame.setVisibility(View.VISIBLE);
        pauseGame.setVisibility(View.GONE);

        Glide.with(context)
                .asGif()
                .load(R.raw.game_over)
                .into(winGame);

        String score = timerTextView.getText().toString();
        timerTextView.setVisibility(View.GONE);

        new Handler().postDelayed(() -> {
            winGame.setVisibility(View.GONE);
            showScoreDialog(score);
        }, 2000);
    }

    public void showScoreDialog(String score) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_score);
        dialog.show();

        CardView btnDone = dialog.findViewById(R.id.btnDone);
        //TextView congratsMessage = dialog.findViewById(R.id.congratsMessage);
        TextView yourScore = dialog.findViewById(R.id.yourScore);
        TextView highestScore = dialog.findViewById(R.id.highestScore);
        LottieAnimationView newHighScore = dialog.findViewById(R.id.newHighScore);

       // congratsMessage.setText("Congratulations " + playerName + "!");
        yourScore.setText(score);

        String getHighscore = getHighScoreAccToLevel(noOfCols);
        if (getHighscore == null) {
            highestScore.setText(score);
            newHighScore.setVisibility(View.VISIBLE);
            saveHighestScore(noOfCols, score);
        } else {
            if (convertToTimestamp(score) < convertToTimestamp(getHighscore)) {
                newHighScore.setVisibility(View.VISIBLE);
                highestScore.setText(score);
                saveHighestScore(noOfCols, score);
            } else {
                highestScore.setText(getHighscore);
                newHighScore.setVisibility(View.GONE);
            }
        }

        btnDone.setOnClickListener(view -> {
            startAnimation(view);
            new Handler().postDelayed(() -> {
                context.startActivity(new Intent(context, select_level_page.class));
                if (context instanceof game_page) {
                    ((game_page) context).finish();
                }
                dialog.dismiss();
            }, 100);
        });
    }

    public long convertToTimestamp(String timeString) {
        String[] timeComponents = timeString.split(":");
        int minutes = Integer.parseInt(timeComponents[0]);
        int seconds = Integer.parseInt(timeComponents[1]);
        int milliseconds = Integer.parseInt(timeComponents[2]);
        return (minutes * 60 * 1000) + (seconds * 1000) + milliseconds;
    }

    public String getHighScoreAccToLevel(int noOfCols) {
        switch (noOfCols) {
            case 3:
                return sharedPreferencesHelper.getHighestScoreLevel1();
            case 4:
                return sharedPreferencesHelper.getHighestScoreLevel2();
            case 5:
                return sharedPreferencesHelper.getHighestScoreLevel3();
            default:
                return null;
        }
    }

    public void saveHighestScore(int noOfCols, String score) {
        switch (noOfCols) {
            case 3:
                sharedPreferencesHelper.saveHighestScoreLevel1(score);
                break;
            case 4:
                sharedPreferencesHelper.saveHighestScoreLevel2(score);
                break;
            case 5:
                sharedPreferencesHelper.saveHighestScoreLevel3(score);
                break;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBlock;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBlock = itemView.findViewById(R.id.imgBlock);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
