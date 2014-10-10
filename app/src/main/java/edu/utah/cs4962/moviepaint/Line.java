package edu.utah.cs4962.moviepaint;

import android.graphics.PointF;
import java.util.ArrayList;

public class Line
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
