package edu.utah.cs4962.moviepaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class PaintArea extends ViewGroup
{
    private int mColor;
    private static final String FILE_NAME = "lines.txt";
    private HashMap<Integer, Line> mLineMap;
    private int mIndex;
    private ImageView mTransformedView;

    public void setColor (int color)
    {
        mColor = color;
    }

    public HashMap<Integer, Line> getLineMap ()
    {
        return mLineMap;
    }

    public void setLineMap (HashMap<Integer, Line> lineMap)
    {
        mLineMap = lineMap;
    }

    public PaintArea (Context context)
    {
        super(context);

        setBackgroundColor(Color.WHITE);
        mColor = Color.BLACK;
        mLineMap = new HashMap<Integer, Line>();
        mIndex = -1;
        mTransformedView = null;
    }

    @Override
    protected void onLayout (boolean b, int i, int i2, int i3, int i4)
    {
        for(int index = 0; i < getChildCount(); index++)
        {
            PaintView child = (PaintView) getChildAt(index);
            if(child.mActive)
            {
                setColor(child.getColor());
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();

        if(event.getActionMasked() == MotionEvent.ACTION_DOWN)
        {
            mIndex++;
            Line line = new Line();
            if(PaletteView.mActiveColor == 0)
                PaletteView.mActiveColor = Color.BLACK;

                line.setColor(PaletteView.mActiveColor);

            mLineMap.put(mIndex, line);
        }
        mLineMap.get(mIndex).mPoints.add(new PointF(x, y));

        invalidate();
        return true;
    }
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        for(int i = 0; i < mLineMap.size(); i++)
        {
            Paint polylinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            polylinePaint.setStyle(Paint.Style.STROKE);
            polylinePaint.setStrokeWidth(4.0f);

            Path polylinePath = new Path();
            polylinePaint.setColor(mLineMap.get(i).getColor());

            if(!mLineMap.isEmpty())
            {
                polylinePath.moveTo(mLineMap.get(i).mPoints.get(0).x,
                        mLineMap.get(i).mPoints.get(0).y);

                for(PointF p : mLineMap.get(i).mPoints)
                    polylinePath.lineTo(p.x, p.y);
            }

            canvas.drawPath(polylinePath, polylinePaint);
        }
    }

    public void saveLines(File directory)
    {
        try
        {
            Gson gson = new Gson();
            String lines = gson.toJson(mLineMap);

            File file = new File(directory, FILE_NAME);
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

    public void loadLines(File directory)
    {
        try
        {
            File file = new File(directory, FILE_NAME);
            FileReader fileReader = new FileReader(file);

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String lineMap = bufferedReader.readLine();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private class Line
    {
        ArrayList<PointF> mPoints = new ArrayList<PointF>();
        int mColor;

        public int getColor ()
        {
            return mColor;
        }
        public void setColor (int color)
        {
            this.mColor = color;
        }
    }
}
