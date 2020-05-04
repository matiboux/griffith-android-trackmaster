package com.matiboux.griffith.trackmaster;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import java.util.List;

public class SpeedGraphView extends View {
    private long minX;
    private long maxX;
    private double minY;
    private double maxY;
    private int canvasWidth;
    private int canvasHeight;

    private List<Pair<Long, Double>> averageSpeeds;

    private Paint dataPointPaint, dataPointFillPaint, dataPointLinePaint, axisLinePaint;
    private Paint textPaint;

    public SpeedGraphView(Context c) {
        super(c);
        if (!isInEditMode()) init();
    }

    public SpeedGraphView(Context c, AttributeSet as) {
        super(c, as);
        if (!isInEditMode()) init();
    }

    public SpeedGraphView(Context c, AttributeSet as, int default_style) {
        super(c, as, default_style);
        if (!isInEditMode()) init();
    }

    private void init() {
        // Paint objects
        dataPointPaint = new Paint();
        dataPointPaint.setColor(Color.BLUE);
        dataPointPaint.setStrokeWidth(7f);
        dataPointPaint.setStyle(Paint.Style.STROKE);

        dataPointFillPaint = new Paint();
        dataPointFillPaint.setColor(Color.WHITE);

        dataPointLinePaint = new Paint();
        dataPointLinePaint.setColor(Color.BLUE);
        dataPointLinePaint.setStrokeWidth(7f);
        dataPointLinePaint.setAntiAlias(true);

        axisLinePaint = new Paint();
        axisLinePaint.setColor(Color.RED);
        axisLinePaint.setStrokeWidth(10f);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(20f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        canvasWidth = Math.min(width, height);
        canvasHeight = canvasWidth / 2;
        setMeasuredDimension(canvasWidth, canvasHeight);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float prevX = -1;
        float prevY = -1;

        System.out.println("Axis from " + minX + " " + maxX + " to " + minY + " " + maxY);
        System.out.println("Canvas of " + canvasWidth + " to " + canvasHeight);

        System.out.println("Size of " + averageSpeeds.size());
        for (Pair<Long, Double> pair : averageSpeeds) {
            long time = pair.first;
            double averageSpeed = pair.second * 3.6; // Convert m/s to km/h
            System.out.println("- " + time + ", " + averageSpeed);

            float valueX = (float) (time - minX) / (maxX - minX) * canvasWidth;
            float valueY = (float) (1 - (averageSpeed - minY) / (maxY - minY)) * canvasHeight;

            if(prevX >= 0 && prevY >= 0) {
                canvas.drawLine(prevX, prevY, valueX, valueY, dataPointLinePaint);
                System.out.println("Draw from " + prevX + " " + prevY + " to " + valueX + " " + valueY);
            }

            canvas.drawCircle(valueX, valueY, 7f, dataPointFillPaint);
            canvas.drawCircle(valueX, valueY, 7f, dataPointPaint);

            prevX = valueX;
            prevY = valueY;
        }

        canvas.drawLine(0f, 0f, 0f, canvasHeight, axisLinePaint);
        canvas.drawLine(0f, canvasHeight, canvasWidth, canvasHeight, axisLinePaint);
        canvas.drawText(maxY + " km/h", axisLinePaint.getStrokeWidth(), textPaint.getTextSize(), textPaint);
        canvas.drawText(minY + " km/h", axisLinePaint.getStrokeWidth(), canvasHeight - axisLinePaint.getStrokeWidth(), textPaint);
    }

    public void setData(List<Pair<Long, Double>> averageSpeeds) {
        this.averageSpeeds = averageSpeeds;

        minX = Long.MAX_VALUE;
        maxX = Long.MIN_VALUE;
        minY = 0;
        maxY = 10;

        for (Pair<Long, Double> pair : averageSpeeds) {
            long time = pair.first;
            double averageSpeed = pair.second * 3.6; // Convert m/s to km/h

            if (minX > time) minX = time;
            if (maxX < time) maxX = time;
            if (minY > averageSpeed) minY = averageSpeed;
            if (maxY < averageSpeed) maxY = averageSpeed;
        }

        invalidate();
    }
}
