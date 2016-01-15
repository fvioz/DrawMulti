package fvioz.drawmulti;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class DrawView extends View {

    class Point {
        float X, Y;
        public Point(float X0, float Y0) {
            X = X0;
            Y = Y0;
        }
    }

    class Line extends ArrayList<Point>{
        int color;
        public Line(Point p, int color0) {
            color = color0;
            this.add(p);
        }
    }

    Paint paint = new Paint();

    HashMap<Integer, Line> lines = new HashMap<>();

    private int[] colors = { Color.BLUE, Color.GREEN, Color.MAGENTA,
            Color.CYAN, Color.GRAY, Color.RED, Color.DKGRAY,
            Color.LTGRAY, Color.YELLOW, Color.BLACK };

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    public void onDraw(Canvas canvas) {

        for (Line points : lines.values()) {

            int cont = 0;
            float prevX = 0;
            float prevY = 0;

            for(Point point : points) {

                if(cont != 0) {
                    paint.setColor(points.color);
                    canvas.drawLine(prevX, prevY, point.X, point.Y, paint);
                }

                prevX = point.X;
                prevY = point.Y;

                cont += 1;
            }

        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // Point
        Point point;

        // Line
        Line line;

        // get pointer index from the event object
        int pointerIndex = event.getActionIndex();

        // get pointer ID
        int pointerId = event.getPointerId(pointerIndex);

        // get masked (not specific to a pointer) action
        int maskedAction = event.getActionMasked();

        switch (maskedAction) {

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:

                int selectedColor = colors[0];
                for(int color : colors) {
                    for (Line currentLine : lines.values()) {
                        if(currentLine.color == color) {
                            continue;
                        } else {
                            selectedColor = color;
                            break;
                        }
                    }
                }

                point = new Point(event.getX(), event.getY());
                line = new Line(point, selectedColor);
                lines.put(pointerId, line);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                lines.remove(pointerId);
                break;

            case MotionEvent.ACTION_MOVE:
                point = new Point(event.getX(), event.getY());
                line = lines.get(pointerId);
                line.add(point);
                break;
        }

        this.invalidate();

        return true;
    }

}