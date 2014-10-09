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

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class CreateModeActivity extends Activity
{
    PaletteView mPaletteView;
    private static final String FILE_NAME = "paletteColors.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        mPaletteView = new PaletteView(this);
        mPaletteView.setId(10);

        Button drawingButton = new Button(this);
        drawingButton.setText("Back to drawing");
        drawingButton.setTextSize(30.0f);
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

//    @Override
//    protected void onSaveInstanceState(Bundle state)
//    {
//        super.onSaveInstanceState(state);
//        state.putSerializable("selectedColor", mPaletteView.getActiveColor());
//        Log.i("Putting", "Dat work in");
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState)
//    {
//        super.onRestoreInstanceState(savedInstanceState);
//        mPaletteView.setActiveColor(savedInstanceState.getInt("selectedColor"));
//        Log.i("Inside yo", "azz homie");
//    }

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
    }

    private void openPaintActivity ()
    {
        Intent intent = new Intent(this, PaintActivity.class);
        startActivity(intent);
    }

    private void savePalette()
    {
        Gson gson = new Gson();

        String paletteColors = gson.toJson(mPaletteView.getColors());
        try
        {
            File file = new File(getFilesDir(), FILE_NAME);
            FileWriter fileWriter = new FileWriter(file);

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(paletteColors);
            bufferedWriter.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    private void loadPalette()
    {
        try
        {
            File file = new File(getFilesDir(), FILE_NAME);
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String palette = bufferedReader.readLine();
            bufferedReader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
