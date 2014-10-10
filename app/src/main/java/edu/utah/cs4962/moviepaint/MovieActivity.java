package edu.utah.cs4962.moviepaint;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

public class MovieActivity extends Activity
{
    MovieView mMovieView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        mMovieView = new MovieView(this);

        SeekBar seekBar = new SeekBar(this);

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
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.89f);

        LinearLayout.LayoutParams seekBarParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.04f);

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.07f);
        buttonParams.setMargins(5, 5, 5, 5);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            movieViewParams =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.87f);

            buttonParams =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.13f);
        }
        rootLayout.addView(mMovieView, movieViewParams);
        rootLayout.addView(seekBar, seekBarParams);
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
