package ti.lines;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.view.View;

import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiDimension;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.view.TiUIView;

import java.util.HashMap;

class LineView extends TiUIView {

    public PaintView tiPaintView;

    public PointF[] points;
    public PointF[] pointsCon1;
    public PointF[] pointsCon2;

    public int startAt = 0;
    Path pathArray = new Path();
    Path pathFillArray = new Path();
    Path pathAxis = new Path();
    Path pathYLines = new Path();
    int viewWidth;
    int viewHeight;
    int startPosition;
    int maxValue = -1;
    int yLines = 0;
    int xLines = 0;
    int fillColorTop = Color.WHITE;
    int fillColorBottom = Color.WHITE;
    boolean showXAis = false;
    boolean showYAis = false;
    Paint paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint paintBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint paintAxis = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint paintYLines = new Paint(Paint.ANTI_ALIAS_FLAG);
    boolean yScale = false;
    boolean fillSpace = false;
    TiDimension dim2;
    int pathHeight = 0;
    int strokeType = TiLinesModule.STROKE_NORMAL;
    int lineType = TiLinesModule.TYPE_CURVED;

    public LineView(TiViewProxy proxy) {
        super(proxy);
        TiDimension dim1 = new TiDimension(TiConvert.toString(proxy.getWidth()), TiDimension.TYPE_WIDTH);
        dim2 = new TiDimension(TiConvert.toString(proxy.getHeight()), TiDimension.TYPE_HEIGHT);
        viewWidth = dim1.getAsPixels(getOuterView());
        viewHeight = dim2.getAsPixels(getOuterView());
        startPosition = (int) (dim2.getAsPixels(getOuterView()) * 0.5);
        setNativeView(tiPaintView = new PaintView(proxy.getActivity()));
    }

    public void setLineColor(int color) {
        paintLine.setColor(color);
    }

    public void setLineWidth(int value) {
        paintLine.setStrokeWidth(value);
    }

    public void setPoints(Object[] pointObject) {
        points = new PointF[pointObject.length];
        pointsCon1 = new PointF[pointObject.length];
        pointsCon2 = new PointF[pointObject.length];

        int localMax = 0;

        for (int i = 0; i < points.length; i++) {
            points[i] = new PointF();
            pointsCon1[i] = new PointF();
            pointsCon2[i] = new PointF();

            // find max value
            if (pointObject[i] instanceof Number) {
                if (localMax < TiConvert.toInt(pointObject[i])) {
                    localMax = TiConvert.toInt(pointObject[i]);
                }
            }
        }

        if (maxValue == -1) {
            maxValue = localMax;
        } else {
            yScale = true;
        }

        if (startAt == TiLinesModule.START_CENTER) {
            startPosition = (int) (dim2.getAsPixels(getOuterView()) * 0.5);
            localMax /= 0.5;
            maxValue /= 0.5;
        } else if (startAt == TiLinesModule.START_BOTTOM) {
            startPosition = dim2.getAsPixels(getOuterView());
        }

        // draw below line - setup shader
        if (fillSpace) {
            pathHeight = (viewHeight / maxValue) * localMax + 10;
            paintBackground.setStyle(Paint.Style.FILL);
            paintBackground.setShader(new LinearGradient(0, viewHeight - pathHeight, 0, viewHeight, new int[]{
                    fillColorTop,
                    fillColorBottom
            }, null, Shader.TileMode.CLAMP));
        }

        int stepX = (viewWidth / (points.length - 1));
        int baseX = 0;
        int pos = 0;
        pathFillArray.moveTo(0, viewHeight);

        // set points
        for (Object point : pointObject) {
            if (point instanceof Number) {
                // just value - draw a graph
                Float hPoint = TiConvert.toFloat(point);

                if (yScale) {
                    hPoint = (viewHeight / maxValue) * hPoint;
                }
                points[pos].set(baseX, -hPoint + startPosition);
                baseX += stepX;
            } else {
                // normal lines with coordinates
                HashMap pointValues = (HashMap) point;
                TiDimension d1 = new TiDimension(TiConvert.toString(pointValues.get("x")), TiDimension.TYPE_WIDTH);
                TiDimension d2 = new TiDimension(TiConvert.toString(pointValues.get("y")), TiDimension.TYPE_HEIGHT);
                Float x = TiConvert.toFloat(d1.getAsPixels(getOuterView()));
                Float y = TiConvert.toFloat(d2.getAsPixels(getOuterView()));

                points[pos].set(x, y);
            }
            pos++;
        }

        // set bezier points
        for (int i = 1; i < points.length; i++) {
            pointsCon1[i].set((points[i].x + points[i - 1].x) / 2, points[i - 1].y);
            pointsCon2[i].set((points[i].x + points[i - 1].x) / 2, points[i].y);
        }

        // draw path
        pathArray.moveTo(points[0].x, points[0].y);
        pathFillArray.lineTo(points[0].x, points[0].y);
        for (int i = 1; i < points.length; i++) {
            if (lineType == TiLinesModule.TYPE_CURVED) {
                pathArray.cubicTo(pointsCon1[i].x, pointsCon1[i].y, pointsCon2[i].x, pointsCon2[i].y, points[i].x, points[i].y);
                pathFillArray.cubicTo(pointsCon1[i].x, pointsCon1[i].y, pointsCon2[i].x, pointsCon2[i].y, points[i].x, points[i].y);
            } else {
                pathArray.lineTo(points[i].x, points[i].y);
                pathFillArray.lineTo(points[i].x, points[i].y);
            }
        }
        pathFillArray.lineTo(viewWidth, viewHeight);

        if (strokeType == TiLinesModule.STROKE_NORMAL) {

        } else if (strokeType == TiLinesModule.STROKE_DASHED) {
            paintLine.setPathEffect(new DashPathEffect(new float[]{5, 10}, 0));
        }

        // create axis
        drawAxis(showXAis, showYAis);
        tiPaintView.invalidate();
    }

    private void drawAxis(boolean drawX, boolean drawY) {
        if (drawX) {
            pathAxis.moveTo(0, viewHeight);
            pathAxis.lineTo(viewWidth, viewHeight);
        }
        if (drawY) {
            pathAxis.moveTo(0, viewHeight);
            pathAxis.lineTo(0, 0);
        }

        if (yLines > 0) {
            int steps = (viewHeight / yLines);
            if (yScale) {
                steps = (viewHeight / maxValue) * yLines;
            }

            int yPos = viewHeight - steps;

            while (yPos > 0) {
                pathYLines.moveTo(0, yPos);
                pathYLines.lineTo(viewWidth, yPos);
                yPos -= steps;
            }
        }

        if (xLines > 0) {
            int steps = (viewWidth / (points.length - 1)) * xLines;
            int xPos = viewWidth;

            while (xPos > 0) {
                pathYLines.moveTo(xPos, 0);
                pathYLines.lineTo(xPos, viewHeight);
                xPos -= steps;
            }
        }
    }

    public void clear() {
        points = new PointF[0];
        pointsCon1 = new PointF[0];
        pointsCon2 = new PointF[0];

        pathArray = new Path();
        pathFillArray = new Path();
        pathAxis = new Path();
        pathYLines = new Path();

        //tiPaintView.clear();
    }

    public class PaintView extends View {
        private boolean clearAll = false;

        public PaintView(Context c) {
            super(c);

            paintAxis.setStyle(Paint.Style.STROKE);
            paintAxis.setStrokeWidth(2);
            paintAxis.setColor(Color.WHITE);
            setLayerType(View.LAYER_TYPE_HARDWARE, paintAxis);

            paintLine.setStrokeWidth(5);
            paintLine.setStyle(Paint.Style.STROKE);
            setLayerType(View.LAYER_TYPE_HARDWARE, paintLine);

            paintYLines.setStyle(Paint.Style.STROKE);
            paintYLines.setStrokeWidth(2);
            paintYLines.setPathEffect(new DashPathEffect(new float[]{5, 10}, 0));
            paintYLines.setColor(0x99FFFFFF);
            setLayerType(View.LAYER_TYPE_HARDWARE, paintYLines);

            setLayerType(View.LAYER_TYPE_HARDWARE, paintBackground);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
        }


        @Override
        protected void onDraw(Canvas canvas) {
            if (clearAll) {
                Paint clearPaint = new Paint();
                clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                canvas.drawRect(0, 0, 0, 0, clearPaint);
                clearAll = false;
            } else {
                canvas.drawPath(pathAxis, paintAxis);
                canvas.drawPath(pathYLines, paintYLines);

                if (fillSpace) {
                    canvas.drawPath(pathFillArray, paintBackground);
                }

                canvas.drawPath(pathArray, paintLine);
            }
        }

        public void clear() {
            clearAll = true;
            invalidate();
        }
    }
}