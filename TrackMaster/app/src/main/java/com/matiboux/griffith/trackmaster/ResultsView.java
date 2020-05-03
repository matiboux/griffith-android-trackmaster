package com.matiboux.griffith.trackmaster;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class ResultsView extends View {
    private Context context;

    private Paint coveredPaint, uncoveredPaint, markedPaint, minefieldPaint;
    private Paint gridPaint, textPaint;
    private Paint lostPaint, wonPaint, strokePaint;

    // default constructor for the class that takes in a context
    public ResultsView(Context c) {
        super(c);
        if (!isInEditMode()) init();
    }

    // constructor that takes in a context and also a list of attributes
    // that were set through XML
    public ResultsView(Context c, AttributeSet as) {
        super(c, as);
        if (!isInEditMode()) init();
    }

    // constructor that take in a context, attribute set and also a default
    // style in case the view is to be styled in a certain way
    public ResultsView(Context c, AttributeSet as, int default_style) {
        super(c, as, default_style);
        if (!isInEditMode()) init();
    }

    // refactored init method as most of this code is shared by all the constructors
    private void init() {
        // Get the current context
        context = getContext();

        // Cell State Paint objects
        coveredPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        coveredPaint.setColor(Color.BLACK);
        uncoveredPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        uncoveredPaint.setColor(Color.GRAY);
        markedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        markedPaint.setColor(Color.YELLOW);
        minefieldPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        minefieldPaint.setColor(Color.RED);

        // Grid Paint objects
        gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridPaint.setColor(Color.WHITE);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);

        // Game Over Paint objects
        lostPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lostPaint.setColor(Color.RED);
        wonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        wonPaint.setColor(Color.GREEN);
        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(10);
        strokePaint.setColor(Color.BLACK);

        //gameAI = new GameAI(this); // Initialize the AI
        //initializeGrid(); // Initialize the game board
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = 0;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        size = Math.min(width, height);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        /*
        // Calculate the width & height of a cell
        cellWidth = getWidth() / gridSize;
        cellHeight = getHeight() / gridSize;
        */

        // Updated the size of texts
        textPaint.setTextSize((int) (getWidth() / 100 * 8));
        int gameOverTextSize = getWidth() / 100 * 16;
        wonPaint.setTextSize(gameOverTextSize);
        lostPaint.setTextSize(gameOverTextSize);
        strokePaint.setTextSize(gameOverTextSize);

        invalidate();
    }

    // public method that needs to be overridden to draw the contents of this widget
    @Override
    public void onDraw(Canvas canvas) {
        // call the superclass method
        super.onDraw(canvas);

        // Black background
        canvas.drawColor(Color.BLACK);

        Rect rect = new Rect(0, 0, getWidth(), getHeight());
        drawCenterText(canvas, "Hello", rect, wonPaint);
    }

    // public method that needs to be overridden to handle the touches from a user
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // ...

        // If we have not handled the touch event, ask the system to do it
        return super.onTouchEvent(event);
    }

    private void drawCenterText(Canvas canvas, String text, Rect rect, Paint paint) {
        //paint.setTextAlign(Paint.Align.LEFT);

        // Get center of the space
        float centerX = rect.left + rect.width() / 2f;
        float centerY = rect.top + rect.height() / 2f;

        // Reuse rect to get the size of the text
        paint.getTextBounds(text, 0, text.length(), rect);

        // Compute coordinates for the text & Draw the text
        float x = centerX - rect.width() / 2f - rect.left;
        float y = centerY + rect.height() / 2f - rect.bottom;
        canvas.drawText(text, x, y, paint);
    }

    private void drawCenterText(Canvas canvas, String text, Rect rect, Paint textPaint, Paint strokePaint) {
        // Using a copy of rect for the first call so that the second call use the same initial rect
        drawCenterText(canvas, text, new Rect(rect), strokePaint);
        drawCenterText(canvas, text, rect, textPaint);
    }
}
