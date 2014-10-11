package edu.utah.cs4962.moviepaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.View;

import java.util.HashMap;

public class MovieView extends View
{
    private int mSeekPosition;
    private HashMap<Integer, Line> mLineMap;

    // Delete this shit
    private long mStartTime;
    private boolean mPauseAnimation;
    private boolean mPlayAnimation;

    public MovieView (Context context)
    {
        super(context);
        setBackgroundColor(Color.rgb(255, 255, 255));
        mLineMap = PaintAreaView.mLineMap;
        mSeekPosition = 0;
    }

    public MovieView (Context context, HashMap<Integer, Line> lineMap)
    {
        super(context);
        setBackgroundColor(Color.rgb(255,255,255));
        mLineMap = lineMap;
    }

    public int getSeekPosition ()
    {
        return mSeekPosition;
    }

    public void setSeekPosition (int seekPosition)
    {
        mSeekPosition = seekPosition;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        drawBySeekPosition(canvas, mSeekPosition);
    }

    /**
     * Draws points based on a percentage determined by the
     * position of the seek bar.
     */
    private void drawBySeekPosition(Canvas canvas, int seekPosition)
    {
        long pointCount = calculateAmountOfPoints(seekPosition);
        long pointsDrawn = 0;
        for(int i = 0; i < mLineMap.size(); i++)
        {
            Paint polylinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            polylinePaint.setStyle(Paint.Style.STROKE);
            polylinePaint.setStrokeWidth(4.0f);

            Path polylinePath = new Path();

            // Prevents null pointer exceptions from being thrown when
            // a color cannot be retrieved.
            Integer color = mLineMap.get(i).getColor();
            if (color != null)
                polylinePaint.setColor(color.intValue());
            else
                polylinePaint.setColor(Color.BLACK);

            if (!mLineMap.isEmpty())
            {
                polylinePath.moveTo(mLineMap.get(i).mPoints.get(0).x * getWidth(),
                        mLineMap.get(i).mPoints.get(0).y * getHeight());

                // Determines how many lines to actually draw
                for (PointF p : mLineMap.get(i).mPoints)
                {
                    if(pointsDrawn <= pointCount)
                    {
                        polylinePath.lineTo(p.x * getWidth(), p.y * getHeight());
                        pointsDrawn++;
                    }
                }
            }
            canvas.drawPath(polylinePath, polylinePaint);
        }
    }

    /**
     * Based on the seekPosition, calculates a percentage
     * of points that should be drawn.
     */
    private long calculateAmountOfPoints(int seekPosition)
    {
        long pointCount = 0;
        for(Line lines : mLineMap.values())
            pointCount += lines.mPoints.size();

        float percent = (float)seekPosition / 100.0f;
        float points = (float) pointCount * percent;

        return (long) points;
    }

    public void play()
    {
        mStartTime = System.currentTimeMillis();
        mPauseAnimation = false;
        mPlayAnimation = true;

        postInvalidate();
    }

    public void pause()
    {
        mPauseAnimation = true;
        mPlayAnimation = false;
    }
}
