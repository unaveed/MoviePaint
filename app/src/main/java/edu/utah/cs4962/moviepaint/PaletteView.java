package edu.utah.cs4962.moviepaint;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

public class PaletteView extends ViewGroup implements PaintView.OnSplotchTouchListener
{
    public static ArrayList<PaintView> mSplotches = new ArrayList<PaintView>();
    private HashMap<PaintView, PointF> mSplotchLocations = new HashMap<PaintView, PointF>();
    public static int mActiveColor;

    public interface OnActiveColorChangedListener
    {
        public void onActiveColorChanged(PaletteView v);
    }

    OnActiveColorChangedListener mOnActiveColorChangedListener;

    public PaletteView(Context context) {
        super(context);
        setBackgroundColor(Color.WHITE);

        addColor(Color.RED);
        addColor(Color.YELLOW);
        addColor(Color.GREEN);
        addColor(Color.CYAN);
        addColor(Color.MAGENTA);
        addColor(Color.BLUE);
        addColor(Color.BLACK);
        addColor(Color.GRAY);
    }

    public OnActiveColorChangedListener getOnActiveColorChangedListener ()
    {
        return mOnActiveColorChangedListener;
    }

    public void setOnActiveColorChangedListener (OnActiveColorChangedListener onActiveColorChangedListener)
    {
        mOnActiveColorChangedListener = onActiveColorChangedListener;
    }

    public int getActiveColor()
    {
        for(int paintViewIndex = 0; paintViewIndex < getChildCount(); paintViewIndex++)
        {
            PaintView paintView = (PaintView)getChildAt(paintViewIndex);
            if(paintView.isActive())
                return paintView.getColor();
        }
        return Color.BLACK;
    }

    public void setActiveColor(int activeColor)
    {
        for(int paintViewIndex = 0; paintViewIndex < getChildCount(); paintViewIndex++)
        {
            PaintView paintView = (PaintView)getChildAt(paintViewIndex);
            if(paintView.getColor() == activeColor)
                paintView.setActive(true);
            else
                paintView.setActive(false);
        }
        if(mOnActiveColorChangedListener != null)
            mOnActiveColorChangedListener.onActiveColorChanged(this);
    }

    public int[] getColors()
    {
        int[] colors = new int[getChildCount()];
        for(int paintViewIndex = 0; paintViewIndex < getChildCount(); paintViewIndex++)
        {
            PaintView paintView = (PaintView)getChildAt(paintViewIndex);
            colors[paintViewIndex] = paintView.getColor();
        }
        return colors;
    }

    public void addColor (int color)
    {
        for(int matchingColor : getColors())
        {
            if(matchingColor == color)
                return;
        }

        PaintView paintView = new PaintView(getContext());
        paintView.setColor(color);
        paintView.setOnSplotchTouchListener(this);
        // Put the boolean here to prevent user from adding paint view
        addView(paintView);
    }

    public void removeColor(int color)
    {
        if(getChildCount() < 3)
            return;

        int activeColor = getActiveColor();
        if(activeColor == color)
            setActiveColor(Color.DKGRAY);

        for(int paintViewIndex = getChildCount() - 1; paintViewIndex >= 0; paintViewIndex--)
        {
            PaintView paintView = (PaintView)getChildAt(paintViewIndex);
            if(paintView.getColor() == color)
            {
                removeView(paintView);
                if(mSplotchLocations.containsKey(paintView))
                    mSplotchLocations.remove(paintView);
            }
        }

        if(activeColor != getActiveColor())
        {
            if(mOnActiveColorChangedListener != null)
                mOnActiveColorChangedListener.onActiveColorChanged(this);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState()
    {
        Bundle instanceState = new Bundle();

        instanceState.putInt("activeColor", getActiveColor());
        instanceState.putParcelable("superState", super.onSaveInstanceState());
        return instanceState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        if(state.getClass() != Bundle.class)
            return;

        Bundle instanceState = (Bundle)state;

        if(instanceState.containsKey("superState"))
        {
            super.onRestoreInstanceState(instanceState.getParcelable("superState"));
        }

        if(instanceState.containsKey("activeColor"))
        {
            int activeColor = instanceState.getInt("activeColor");
            setActiveColor(activeColor);
        }
    }

    @Override
    public void addView(View child)
    {
        if(!(child instanceof  PaintView))
            Log.e("PaletteView", "Can't add views to palette view.");
        else
            super.addView(child);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);

        int width = Math.max(widthSpec, getSuggestedMinimumWidth());
        int height = Math.max(heightSpec, getSuggestedMinimumHeight());

        int childState = 0;
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            View child = getChildAt(childIndex);
            child.measure(MeasureSpec.AT_MOST | 75, MeasureSpec.AT_MOST | 75);
            childState = combineMeasuredStates(childState, child.getMeasuredState());
        }

        setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, childState),
                resolveSizeAndState(height, heightMeasureSpec, childState));
    }

    @Override
    protected void onLayout(boolean b, int i, int i2, int i3, int i4) {

        int childWidthMax = 0;
        int childHeightMax = 0;

        mSplotchLocations.clear();
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            View child = getChildAt(childIndex);
            childHeightMax = Math.max(childWidthMax, child.getMeasuredWidth());
            childWidthMax = Math.max(childHeightMax, child.getMeasuredHeight());
        }

        Rect layoutRect = new Rect();
        int size = 75;
        // Set the area of the oval
        layoutRect.left = getPaddingLeft() + size;
        layoutRect.top = getPaddingTop() + size;
        layoutRect.right = getWidth() - getPaddingRight() - size;
        layoutRect.bottom = getHeight() - getPaddingBottom() - size;

        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            double angle = (double) childIndex / (double) getChildCount() * 2.0 * Math.PI;
            int childCenterX = (int)(layoutRect.centerX() +  layoutRect.width() * 0.5 * Math.cos(angle));
            int childCenterY = (int)(layoutRect.centerY() + layoutRect.height() * 0.5 * Math.sin(angle));

            // Set the size of the boxes
            View child = getChildAt(childIndex);
            Rect childLayout = new Rect();
            childLayout.left = childCenterX - size;
            childLayout.top = childCenterY - size;
            childLayout.right = childCenterX + size;
            childLayout.bottom = childCenterY + size;

            mSplotchLocations.put((PaintView) child, new PointF(childCenterX, childCenterY));
            child.layout(childLayout.left, childLayout.top, childLayout.right, childLayout.bottom);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        float startX = event.getX();
        float startY = event.getY();

        PaintView child;
        for(int pointIndex = 0; pointIndex < getChildCount(); pointIndex++)
        {
            child = (PaintView) getChildAt(pointIndex);
            if(child.mActive)
            {
                mActiveColor = child.getColor();
                if(event.getActionMasked() == MotionEvent.ACTION_MOVE)
                {
                    child.setX(startX - child.getWidth() / 2);
                    child.setY(startY - child.getHeight()  / 2);
                }
                if(event.getActionMasked() == MotionEvent.ACTION_UP)
                {
                    // Check if child was taken off the palette
                    if(!inBoundary(startX,startY))
                        removeColor(child.getColor());

                    // Check if active splotch was dropped on another splotch
                    PaintView droppedOnSplotch = onTopOfSplotch(child, startX, startY);
                    if(droppedOnSplotch != null)
                        addColor(mixColors(child.getColor(), droppedOnSplotch.getColor()));

                    // Snap splotch back to original position if it's still on palette
                    if(mSplotchLocations.containsKey(child))
                        snapSplotchToOrigin(child, startX, startY);
                }
            }
        }
        return true;
    }

    @Override
    public void onSplotchTouched (PaintView v)
    {
        setActiveColor(v.getColor());
    }

    private int mixColors(int color1, int color2)
    {
        int r1 = Color.red(color1);
        int r2 = Color.red(color2);

        int g1 = Color.green(color1);
        int g2 = Color.green(color2);

        int b1 = Color.blue(color1);
        int b2 = Color.blue(color2);

        int r = (r1+r2) / 2;
        int g = (g1+g2) / 2;
        int b = (b1+b2) / 2;

        return Color.rgb(r,g,b);
    }

    private boolean inBoundary(float x, float y)
    {
        double centerX = getWidth() / 2;
        double centerY = getHeight() / 2 ;

        double topX = Math.pow(x - centerX, 2);
        double topY = Math.pow(y - centerY, 2);

        double eX = Math.pow(getWidth() / 2 , 2);
        double eY = Math.pow(getHeight() / 2, 2);

        return topX / eX + topY / eY <= 1;
    }

    private PaintView onTopOfSplotch(PaintView activeSplotch, float x, float y)
    {
        for(PaintView splotch : mSplotchLocations.keySet())
        {
            if(splotch != activeSplotch)
            {
                float circleCenterX = mSplotchLocations.get(splotch).x;
                float circleCenterY = mSplotchLocations.get(splotch).y;

                float distance = (float) Math.sqrt(Math.pow(circleCenterX - x, 2) +
                        Math.pow(circleCenterY - y, 2));

                if (splotch.getRadius() > distance)
                {
                    return splotch;
                }
            }
        }
        return null;
    }

    private void snapSplotchToOrigin(PaintView splotch, float x, float y)
    {
        PointF point = mSplotchLocations.get(splotch);
        ObjectAnimator animator = new ObjectAnimator();
        animator.setDuration(500);
        animator.setTarget(splotch);

        float centerX = splotch.getWidth() / 2;
        float centerY = splotch.getHeight() / 2;

        float adjustedX = x - centerX;
        float adjustedY = y - centerY;

        float adjustedSplotchX = point.x - centerX;
        float adjustedSplotchY = point.y - centerY;

        float[] setX = new float[]{adjustedX, adjustedSplotchX};
        float[] setY = new float[]{adjustedY, adjustedSplotchY};

        animator.setValues(PropertyValuesHolder.ofFloat("x", setX),
                PropertyValuesHolder.ofFloat("y", setY));

        animator.start();
    }
}

