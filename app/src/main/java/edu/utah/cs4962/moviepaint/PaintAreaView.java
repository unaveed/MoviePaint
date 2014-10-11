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

import java.util.HashMap;

public class PaintAreaView extends ViewGroup
{
    private int mColor;
    public static HashMap<Integer, Line> mLineMap;
    private int mKey;
    private ImageView mTransformedView;

    public void setColor (int color)
    {
        mColor = color;
    }

    public int getKey ()
    {
        return mKey;
    }

    public void setKey (int key)
    {
        mKey = key;
    }

    public HashMap<Integer, Line> getLineMap ()
    {
        return mLineMap;
    }

    public void setLineMap (HashMap<Integer, Line> lineMap)
    {
        // Use lineMap as new map if member mLineMap empty other append
        // key-value pairs existing mLineMap
        if(lineMap == null || lineMap.isEmpty())
            mLineMap = lineMap;
        else
        {
            for(int key : lineMap.keySet())
                mLineMap.put(key, lineMap.get(key));
        }
    }

    public void clear()
    {
        mLineMap.clear();
        invalidate();
    }

    public PaintAreaView (Context context)
    {
        super(context);

        setBackgroundColor(Color.WHITE);
        mColor = Color.BLACK;
        mLineMap = new HashMap<Integer, Line>();
        mKey = -1;
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
            mKey++;
            Line line = new Line();
            if(PaletteView.mActiveColor == 0)
                PaletteView.mActiveColor = Color.BLACK;

                line.setColor(PaletteView.mActiveColor);

            mLineMap.put(mKey, line);
        }
        mLineMap.get(mKey).mPoints.add(new PointF(x / getWidth(), y / getHeight()));

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

            // Prevents null pointer exceptions from being thrown when
            // a color cannot be retrieved.
            Integer color = mLineMap.get(i).getColor();
            if(color != null)
                polylinePaint.setColor(color.intValue());
            else
                polylinePaint.setColor(Color.BLACK);

            if(!mLineMap.isEmpty())
            {
                polylinePath.moveTo(mLineMap.get(i).mPoints.get(0).x * getWidth(),
                        mLineMap.get(i).mPoints.get(0).y * getHeight());

                for(PointF p : mLineMap.get(i).mPoints)
                    polylinePath.lineTo(p.x * getWidth(), p.y * getHeight());
            }

            canvas.drawPath(polylinePath, polylinePaint);
        }
    }
}
