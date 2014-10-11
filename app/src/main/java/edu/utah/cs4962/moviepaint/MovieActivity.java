package edu.utah.cs4962.moviepaint;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;

/**
 * Allows the user to watch the painting be redrawn
 * on the screen.
 */
public class MovieActivity extends Activity
{
    private MovieView mMovieView;
    private boolean mIsPlay;
    private ImageButton mControl;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout movieControls = new LinearLayout(this);
        movieControls.setOrientation(LinearLayout.HORIZONTAL);

        mMovieView = new MovieView(this);

        mIsPlay = true;

        mControl = new ImageButton(this);
        mControl.setImageResource(R.drawable.play);
        mControl.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                if(mIsPlay)
                {
                    mControl.setImageResource(R.drawable.play);
                }
                else
                {
                    mControl.setImageResource(R.drawable.stop);
                }
                mIsPlay = !mIsPlay;
             }
        });

        SeekBar seekBar = new SeekBar(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged (SeekBar seekBar, int i, boolean b)
            {
                mMovieView.setSeekPosition(i);
                mMovieView.play();
            }

            @Override
            public void onStartTrackingTouch (SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch (SeekBar seekBar)
            {

            }
        });

        LinearLayout.LayoutParams controlParams = new LinearLayout.
                     LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.10f);
        LinearLayout.LayoutParams seekBarParams = new
                     LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.90f);

        movieControls.addView(mControl, controlParams);
        movieControls.addView(seekBar, seekBarParams);

        Button button = new Button(this);
        button.setText("Back to drawing");
        button.setTextSize(30.0f);
        button.setBackgroundColor(Color.rgb(255, 75, 66));
        button.setTextColor(Color.rgb(255,255,255));
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                openPaintActivity();
            }
        });

        LinearLayout.LayoutParams movieViewParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.87f);

        LinearLayout.LayoutParams movieControlParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.06f);

        LinearLayout.LayoutParams buttonParams =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.07f);
        buttonParams.setMargins(5, 5, 5, 5);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            movieViewParams =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.79f);

            movieControlParams =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.09f);

            buttonParams =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.12f);
        }
        rootLayout.addView(mMovieView, movieViewParams);
        rootLayout.addView(movieControls, movieControlParams);
        rootLayout.addView(button, buttonParams);

        setContentView(rootLayout);
    }

    private void openPaintActivity ()
    {
        Intent intent = new Intent(this, PaintActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }
}
