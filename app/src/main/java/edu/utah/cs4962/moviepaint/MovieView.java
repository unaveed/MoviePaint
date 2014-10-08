package edu.utah.cs4962.moviepaint;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

public class MovieView extends View
{
    public MovieView (Context context)
    {
        super(context);
        setBackgroundColor(Color.rgb(255,255,255));
    }
}
