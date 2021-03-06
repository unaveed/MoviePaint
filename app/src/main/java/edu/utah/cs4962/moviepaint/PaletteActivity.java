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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class PaletteActivity extends Activity
{
    PaletteView mPaletteView;
    private ArrayList<Integer> mColors = new ArrayList<Integer>();
    private final String mPaletteFile = "paletteColors.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        if(mColors.size() > 0)
            mPaletteView = new PaletteView(this, mColors);
        else
            mPaletteView = new PaletteView(this);

        mPaletteView.setId(10);

        Button drawingButton = new Button(this);
        drawingButton.setText("Back to drawing");
        drawingButton.setTextSize(14.0f);
        drawingButton.setBackgroundColor(Color.rgb(10, 99, 178));
        drawingButton.setTextColor(Color.rgb(255,255,255));
        drawingButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                openPaintActivity();
            }
        });

        LinearLayout.LayoutParams paletteViewParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.93f);

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.07f);
        buttonParams.setMargins(5, 5, 5, 5);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            paletteViewParams =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.87f);

            buttonParams =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.13f);
        }
        rootLayout.addView(mPaletteView, paletteViewParams);
        rootLayout.addView(drawingButton, buttonParams);

        setContentView(rootLayout);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        savePalette();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loadPalette();
        if(mColors.size() > 0)
        {
            mPaletteView.clear();
            for(int color : mColors)
                mPaletteView.addColor(color);
        }
    }

    private void loadPalette()
    {
        try
        {
            File file = new File(getFilesDir(), mPaletteFile);
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String paletteColor = null;
            do
            {
                paletteColor = bufferedReader.readLine();
                mColors.add(Integer.parseInt(paletteColor));
            }
            while(paletteColor != null);
            bufferedReader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void savePalette()
    {
        try
        {
            File file = new File(getFilesDir(), mPaletteFile);
            FileWriter fileWriter = new FileWriter(file);

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for(int color : mPaletteView.getColors())
                bufferedWriter.write(color + "\n");
            bufferedWriter.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void openPaintActivity ()
    {
        Intent intent = new Intent(this, PaintActivity.class);
        startActivity(intent);
    }
}
