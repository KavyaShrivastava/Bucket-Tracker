package com.csc396.buckettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;

import com.csc396.buckettracker.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final long DEFAULT_NUM_MINS = 20;
    private static final long MILLIS_PER_MIN = 60000;
    private static final long MILLIS_PER_SEC = 1000;
    private static final long SECS_PER_MIN = 60;

    private CountDownTimer gameTimer;
    private String currTime;
    private ActivityMainBinding binding;
    private View.OnClickListener toggleIsGuest_onClickListener = new View.OnClickListener(){
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            boolean textSelect = binding.toggleIsGuest.isChecked();
//            String toggle_text = binding.toggleIsGuest.getText().toString();
//            if(toggle_text == binding.labelGuest.getText().toString()){
//                binding.labelGuest.setTextColor(getResources().getColor(R.color.red));
//                binding.textviewGuestScore.set
            if(textSelect){
                binding.labelGuest.setTextColor(getResources().getColor(R.color.red, getTheme()));
                binding.textviewGuestScore.setTextColor(getResources().getColor(R.color.red, getTheme()));
                binding.labelHome.setTextColor(getResources().getColor(R.color.black, getTheme()));
                binding.textviewHomeScore.setTextColor(getResources().getColor(R.color.black, getTheme()));
            }
            else{
                binding.labelHome.setTextColor(getResources().getColor(R.color.red, getTheme()));
                binding.textviewHomeScore.setTextColor(getResources().getColor(R.color.red, getTheme()));
                binding.labelGuest.setTextColor(getResources().getColor(R.color.black, getTheme()));
                binding.textviewGuestScore.setTextColor(getResources().getColor(R.color.black, getTheme()));

            }

        }
    };
    private View.OnLongClickListener button_add_score_onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            int score = 0;
            if(binding.checkboxAddOne.isChecked()){
                score+=1;
                binding.checkboxAddOne.toggle();
            }
            if(binding.checkboxAddTwo.isChecked()){
                score+=2;
                binding.checkboxAddTwo.toggle();

            }
            if(binding.checkboxAddThree.isChecked()){
                score+=3;
                binding.checkboxAddThree.toggle();
            }

            if(binding.toggleIsGuest.isChecked()){
                score += Integer.valueOf(binding.textviewGuestScore.getText().toString());
                binding.textviewGuestScore.setText(String.valueOf(score));

            }
            else{
                score += Integer.valueOf(binding.textviewHomeScore.getText().toString());
                binding.textviewHomeScore.setText(String.valueOf(score));
            }
            binding.toggleIsGuest.toggle();
            binding.toggleIsGuest.callOnClick();
            return false;
        }
    };

    private View.OnClickListener switch_game_clock_onClickListener = new View.OnClickListener(){

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {

            if(binding.switchGameClock.isChecked()){
                long min = Long.valueOf(currTime.split(":")[0]) * MILLIS_PER_MIN + Long.valueOf(currTime.split(":")[1]) * MILLIS_PER_SEC;
                gameTimer = getNewTimer(min, MILLIS_PER_SEC);
                gameTimer.start();
            }
            else{
                currTime = binding.textviewTimeRemaining.getText().toString();
                gameTimer.cancel();
            }


        }
    };

    private boolean isEditTextNotEmpty (EditText text){
        return text.getText().toString().length()>0;
    }

    private View.OnClickListener button_set_time_onClickListener = new View.OnClickListener(){
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            if(isEditTextNotEmpty(binding.edittextNumMins) && isEditTextNotEmpty(binding.edittextNumSecs)){
                int mins = Integer.valueOf(binding.edittextNumMins.getText().toString());
                int secs = Integer.valueOf(binding.edittextNumSecs.getText().toString());

                if ((secs >= 0 && secs < SECS_PER_MIN) && (mins>= 0 && mins <  DEFAULT_NUM_MINS)){
                    gameTimer.cancel();
                    currTime = binding.edittextNumMins.getText().toString() + ":" + binding.edittextNumSecs.getText().toString();
                    setTime(currTime);
                    binding.switchGameClock.setChecked(false);

                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currTime = binding.textviewTimeRemaining.getText().toString();

        binding.toggleIsGuest.setOnClickListener(toggleIsGuest_onClickListener);
        binding.buttonAddScore.setOnLongClickListener(button_add_score_onLongClickListener);
        binding.switchGameClock.setOnClickListener(switch_game_clock_onClickListener);
        binding.buttonSetTime.setOnClickListener(button_set_time_onClickListener);
    }

    public CountDownTimer getNewTimer(long totalLengthTimerMili, long timerTickLength){
        return new CountDownTimer(totalLengthTimerMili, timerTickLength) {
            @Override
            public void onTick(long totalLength) {
                String timeRemaining[] = binding.textviewTimeRemaining.getText().toString().split(":");
                long mins = (totalLength / MILLIS_PER_SEC) / SECS_PER_MIN;
                long secs = (totalLength / MILLIS_PER_SEC) % SECS_PER_MIN;
                currTime = mins + ":" + secs;
                setTime(currTime);
                totalLength-=timerTickLength;



            }

            @Override
            public void onFinish() {

            }
        };
    }


    public void setTime(String time){
        binding.textviewTimeRemaining.setText(time);
    }


}