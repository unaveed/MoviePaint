package edu.utah.cs4962.moviepaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class PaintView extends View {

    private RectF mContentRect;
    private float mRadius;
    private int mColor = Color.CYAN;
    private Path mPath;
    private ArrayList<PointF> mPoints = new ArrayList<PointF>();
    public boolean mActive = false;

    public interface OnSplotchTouchListener{
        public void onSplotchTouched(PaintView v);
    }
    OnSplotchTouchListener _onSplotchTouchListener = null;

    public PaintView(Context context) {
        super(context);
        setMinimumHeight(50);
        setMinimumWidth(50);
    }

    public float getRadius ()
    {
        return mRadius;
    }

    public boolean isActive()
    {
        return mActive;
    }

    public void setActive(boolean activeColor)
    {
        mActive = activeColor;
        invalidate();
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int _color) {
        this.mColor = _color;
        invalidate();
    }

    public void setOnSplotchTouchListener(OnSplotchTouchListener listener){
        _onSplotchTouchListener = listener;
    }

    public OnSplotchTouchListener getOnSplotchTouchListener(){
        return _onSplotchTouchListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        float CircleCenterX = mContentRect.centerX();
        float CircleCenterY = mContentRect.centerY();
        float distance = (float) Math.sqrt(Math.pow(CircleCenterX - x, 2) + Math.pow(CircleCenterY - y, 2));
        if (distance < mRadius) {
//            Log.i("paint_view", "Touched inside the circle");
            this.mActive = true;
            setActive();
            invalidate();
            if (_onSplotchTouchListener != null)
            {
                _onSplotchTouchListener.onSplotchTouched(this);
            }
        }

        return super.onTouchEvent(event);
    }

    private void setActive()
    {
        ArrayList<PaintView> children = PaletteView.mSplotches;

        for(int i = 0; i < children.size(); i++)
        {
            if(children.get(i) != this)
            {
                children.get(i).mActive = false;
                invalidate();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getColor());

        if(mPath == null)
        {
            mPath = new Path();

            mContentRect = new RectF();
            mContentRect.left = getPaddingLeft();
            mContentRect.top = getPaddingTop();
            mContentRect.right = getWidth() - getPaddingRight();
            mContentRect.bottom = getHeight() - getPaddingBottom();

            PointF center = new PointF(mContentRect.centerX(), mContentRect.centerY());
            mPoints.add(center);

            float maxRadius = Math.min(mContentRect.width() * 0.45f, mContentRect.height() * 0.45f);
            float minRadius = 0.5f * maxRadius;
            mRadius = minRadius + (maxRadius - minRadius) * 0.5f;
            int pointCount = 60;

            for (int pointIndex = 0; pointIndex < pointCount; pointIndex += 3)
            {
                // Control 1
                PointF control1 = new PointF();
                float control1Radius = mRadius + (float)(Math.random() - 0.5) * 6.0f * 10.0f;
                control1.x = center.x + control1Radius *
                        (float)Math.cos(((double)pointIndex / (double)pointCount) * 2.0 * Math.PI);
                control1.y = center.y + control1Radius *
                        (float)Math.sin(((double)pointIndex / (double)pointCount) * 2.0 * Math.PI);

                // Control 2
                PointF control2 = new PointF();
                float control2Radius = mRadius + (float)(Math.random() - 0.5) * 6.0f * 12.0f;
                control2.x = center.x + control2Radius *
                        (float)Math.cos(((double)pointIndex / (double)pointCount) * 2.0 * Math.PI);
                control2.y = center.y + control2Radius *
                        (float)Math.sin(((double)pointIndex / (double)pointCount) * 2.0 * Math.PI);

                // Point
                PointF point = new PointF();
                point.x = center.x + mRadius *
                        (float)Math.cos(((double)pointIndex / (double)pointCount) * 2.0 * Math.PI);
                point.y = center.y + mRadius *
                        (float)Math.sin(((double)pointIndex / (double)pointCount) * 2.0 * Math.PI);

                if(pointIndex == 0)
                    mPath.moveTo(point.x, point.y);
                else
                    mPath.cubicTo(control1.x, control1.y, control2.x, control2.y, point.x, point.y);
            }
        }

        canvas.drawPath(mPath, paint);

        if(mActive)
        {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(8.0f);
            // Black will not show inverse in the other case
            if(getColor() == Color.BLACK)
                paint.setColor(Color.YELLOW);
            else
                paint.setColor(Color.argb(255, 255 - Color.red(getColor()), 255 - Color.green(getColor()), 255 - Color.blue(getColor())));
            canvas.drawPath(mPath, paint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);

        int width = getSuggestedMinimumWidth();
        int height = getSuggestedMinimumHeight();

        if (widthMode == MeasureSpec.AT_MOST){
            width = widthSpec;
        }
        if (heightMode == MeasureSpec.AT_MOST){
            height = heightSpec;
        }

        if (widthMode ==  MeasureSpec.EXACTLY){
            width = widthSpec;
            height = width;
        }
        if (heightMode == MeasureSpec.EXACTLY){
            height = heightSpec;
            width = height;
        }

        // TODO; RESPECT THE PADDING!
        if (width > height && widthMode != MeasureSpec.EXACTLY){
            width = height;
        }
        if (height > width && heightMode != MeasureSpec.EXACTLY){
            height = width;
        }

        setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec,
                        width < getSuggestedMinimumWidth() ? MEASURED_STATE_TOO_SMALL: 0),
                resolveSizeAndState(height, heightMeasureSpec,
                        height < getSuggestedMinimumHeight() ? MEASURED_STATE_TOO_SMALL: 0));
    }
}
