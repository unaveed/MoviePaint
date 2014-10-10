package edu.utah.cs4962.moviepaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

public class MovieView extends View
{
    private long mSeekPosition;

    public MovieView (Context context)
    {
        super(context);
        setBackgroundColor(Color.rgb(255,255,255));
    }

    public long getSeekPosition ()
    {
        return mSeekPosition;
    }

    public void setSeekPosition (long seekPosition)
    {
        mSeekPosition = seekPosition;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
    }
}
