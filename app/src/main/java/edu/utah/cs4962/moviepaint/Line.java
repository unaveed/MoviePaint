package edu.utah.cs4962.moviepaint;

import android.graphics.PointF;
import java.util.ArrayList;

/**
 * Represents a drawn line, containing information about
 * each point in the line, including the color.
 */
public class Line
{
    ArrayList<PointF> mPoints = new ArrayList<PointF>();    // Store the location of points
    int mColor; // Stores the color of each line

    public int getColor ()
{
    return mColor;
}
    public void setColor (int color)
{
    this.mColor = color;
}
}
