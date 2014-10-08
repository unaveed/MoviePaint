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

    PaletteView mPaletteView;
    PaintArea mPaintArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        mPaintArea = new PaintArea(this);
//        mPaletteView = new PaletteView(this);
//
//        mPaletteView.setId(10);
//        mPaletteView.setOnActiveColorChangedListener(new PaletteView.OnActiveColorChangedListener()
//        {
//            @Override
//            public void onActiveColorChanged (PaletteView v)
//            {
//            }
//        });
//
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            rootLayout.addView(mPaintArea, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.92f));
//        rootLayout.addView(mPaletteView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 1));
            rootLayout.addView(addStripMenu(),
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            0, 0.08f));
        }
        else
        {
            rootLayout.addView(mPaintArea, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.87f));
//        rootLayout.addView(mPaletteView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 1));
            rootLayout.addView(addStripMenu(),
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            0, 0.13f));
        }
        setContentView(rootLayout);
    }

    /*
     * Creates the strip menu that will appear at the bottom
     * of the View and adds it to a LinearLayout.
     */
    private LinearLayout addStripMenu()
    {
        LinearLayout stripMenu = new LinearLayout(this);
        stripMenu.setOrientation(LinearLayout.HORIZONTAL);
        stripMenu.setBackgroundColor(Color.rgb(191,204,12));

        Button activeColorStripButton = new Button(this);
        activeColorStripButton.setBackgroundColor(PaletteView.mActiveColor);

        Button createModeButton = new Button(this);
        createModeButton.setText("Create");
        createModeButton.setTextSize(35.0f);
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
        watchModeButton.setTextSize(35.0f);
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
        createParams.setMargins(0,5,5,5);
        stripMenu.addView(createModeButton, createParams);

        LinearLayout.LayoutParams activeStripParams =
                new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.05f);
        activeStripParams.setMargins(0, 5, 5, 5);
        stripMenu.addView(activeColorStripButton, activeStripParams);

        LinearLayout.LayoutParams watchParams =
                new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.45f);
        watchParams.setMargins(0,5,0,5);
        stripMenu.addView(watchModeButton, watchParams);

        return stripMenu;
    }

    private void openWatchModeActivity ()
    {
        Intent intent = new Intent(this, WatchModeActivity.class);
        startActivity(intent);
    }

    private void openCreateModeActivity ()
    {
        Intent intent = new Intent(this, CreateModeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // TODO: Implement
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        // TODO: Implement
    }
}
