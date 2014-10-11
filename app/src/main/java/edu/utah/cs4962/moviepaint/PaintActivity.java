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
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;

public class PaintActivity extends Activity {

    private PaintAreaView mPaintAreaView;
    private final String mLinesFileName = "lines.txt";
    private final String mKeyFileName = "pointer.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        mPaintAreaView = new PaintAreaView(this);

        LinearLayout.LayoutParams paintAreaParams = new LinearLayout.
                     LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.93f);
        LinearLayout.LayoutParams stripMenuParams = new LinearLayout.
                     LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.07f);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            paintAreaParams = new LinearLayout.
                                  LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.87f);
            stripMenuParams = new LinearLayout.
                                  LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.13f);
        }

        rootLayout.addView(mPaintAreaView, paintAreaParams);
        rootLayout.addView(addStripMenu(), stripMenuParams);
        setContentView(rootLayout);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        HashMap<Integer, Line> lines = loadLines();
        if(lines != null)
            mPaintAreaView.setLineMap(lines);
        Integer index = loadPointer();
        if(index != null)
            mPaintAreaView.setId(index.intValue());
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        saveLines();
        savePointer();
    }

   /*
    * Creates the strip menu that will appear at the bottom
    * of the View and adds it to a LinearLayout.
    */
    private LinearLayout addStripMenu()
    {
        LinearLayout stripMenu = new LinearLayout(this);
        stripMenu.setOrientation(LinearLayout.HORIZONTAL);

        Button createModeButton = new Button(this);
        createModeButton.setText("Colors");
        createModeButton.setTextSize(30.0f);
        createModeButton.setTextColor(Color.WHITE);
        if(PaletteView.mActiveColor == Color.WHITE)
            createModeButton.setTextColor(Color.BLACK);
        if(PaletteView.mActiveColor == 0)
            createModeButton.setBackgroundColor(Color.BLACK);
        else
            createModeButton.setBackgroundColor(PaletteView.mActiveColor);

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

        Button resetCanvas = new Button(this);
        resetCanvas.setText("Reset");
        resetCanvas.setTextSize(30.0f);
        resetCanvas.setTextColor(Color.WHITE);
        resetCanvas.setBackgroundColor(Color.rgb(10, 99, 178));
        resetCanvas.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                mPaintAreaView.clear();
            }
        });

        LinearLayout.LayoutParams createParams =
                new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.33f);
        createParams.setMargins(5, 5, 5, 5);

        LinearLayout.LayoutParams clearParams =
                new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.33f);
        clearParams.setMargins(0,5,0,5);

        LinearLayout.LayoutParams watchParams =
                new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.33f);
        watchParams.setMargins(5, 5, 5, 5);

        stripMenu.addView(createModeButton, createParams);
        stripMenu.addView(resetCanvas, clearParams);
        stripMenu.addView(watchModeButton, watchParams);

        return stripMenu;
    }

    private void saveLines()
    {
        try
        {
            Gson gson = new Gson();
            String lines = gson.toJson(mPaintAreaView.getLineMap());

            File file = new File(getFilesDir(), mLinesFileName);
            FileWriter fileWriter = new FileWriter(file);

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(lines);
            bufferedWriter.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void savePointer()
    {
        try
        {
            File file = new File(getFilesDir(), mKeyFileName);
            FileWriter fileWriter = new FileWriter(file);

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(mPaintAreaView.getKey() + "\n");
            bufferedWriter.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private HashMap<Integer, Line> loadLines()
    {
        try
        {
            File file = new File(getFilesDir(), mLinesFileName);
            FileReader fileReader = new FileReader(file);

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String map = bufferedReader.readLine();
            Type lineMap = new TypeToken<HashMap<Integer, Line>>(){}.getType();

            Gson gson = new Gson();
            return gson.fromJson(map, lineMap);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private Integer loadPointer()
    {
        try
        {
            File file = new File(getFilesDir(), mKeyFileName);
            FileReader fileReader = new FileReader(file);

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String pointer = bufferedReader.readLine();
            return Integer.parseInt(pointer);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
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
