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

public class PaintActivity extends Activity {

    private PaintArea mPaintArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        mPaintArea = new PaintArea(this);

        rootLayout.addView(mPaintArea, new LinearLayout.LayoutParams
                          (ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.93f));
        rootLayout.addView(addStripMenu(), new LinearLayout.LayoutParams
                          (ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.07f));

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            rootLayout.addView(mPaintArea, new LinearLayout.LayoutParams
                              (ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.87f));
            rootLayout.addView(addStripMenu(), new LinearLayout.LayoutParams
                              (ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.13f));
        }
        setContentView(rootLayout);
    }

    @Override
    protected void onResume()
    {
        super.onResume();


    }

    @Override
    protected void onPause()
    {
        super.onPause();

        // TODO: Implement
    }

   /*
    * Creates the strip menu that will appear at the bottom
    * of the View and adds it to a LinearLayout.
    */
    private LinearLayout addStripMenu()
    {
        LinearLayout stripMenu = new LinearLayout(this);
        stripMenu.setOrientation(LinearLayout.HORIZONTAL);

        Button activeColorStripButton = new Button(this);

        if(PaletteView.mActiveColor == 0)
            activeColorStripButton.setBackgroundColor(Color.BLACK);
        else
            activeColorStripButton.setBackgroundColor(PaletteView.mActiveColor);

        Button createModeButton = new Button(this);
        createModeButton.setText("Colors");
        createModeButton.setTextSize(30.0f);
        createModeButton.setBackgroundColor(Color.rgb(10, 99, 178));
        createModeButton.setTextColor(Color.rgb(255, 255, 255));
        createModeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                openCreateModeActivity();
            }
        });

        Button watchModeButton = new Button(this);
        watchModeButton.setText("Watch");
        watchModeButton.setTextSize(30.0f);
        watchModeButton.setBackgroundColor(Color.rgb(255, 75, 66));
        watchModeButton.setTextColor(Color.rgb(255, 255, 255));
        watchModeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                openWatchModeActivity();
            }
        });

        LinearLayout.LayoutParams createParams =
                new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.45f);
        createParams.setMargins(5,5,5,5);
        stripMenu.addView(createModeButton, createParams);

        LinearLayout.LayoutParams activeStripParams =
                new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.05f);
        activeStripParams.setMargins(0, 5, 5, 5);
        stripMenu.addView(activeColorStripButton, activeStripParams);

        LinearLayout.LayoutParams watchParams =
                new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.45f);
        watchParams.setMargins(0,5,5,5);
        stripMenu.addView(watchModeButton, watchParams);

        return stripMenu;
    }

    private void openWatchModeActivity ()
    {
        Intent intent = new Intent(this, MovieActivity.class);
        startActivity(intent);
    }

    private void openCreateModeActivity ()
    {
        Intent intent = new Intent(this, PaletteActivity.class);
        startActivity(intent);
    }
}
