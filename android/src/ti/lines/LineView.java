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
    private final Paint paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintAxis = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintYLines = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final TiDimension dimensionHeight;
    public PaintView tiPaintView;
    public int startAt = 0;
    public double startPosition;
    public float maxValue = -1;
    public float origMaxValue = -1;
    public int yLines = 0;
    public int xLines = 0;
    public int axisWidth = 1;
    public int fillColorTop = Color.WHITE;
    public int fillColorBottom = Color.WHITE;
    public int axisColor = Color.WHITE;
    public boolean showXAis = false;
    public boolean showYAis = false;
    public boolean yScale = false;
    public boolean fillSpace = false;
    public int strokeType = TiLinesModule.STROKE_NORMAL;
    public int lineType = TiLinesModule.TYPE_CURVED;
    public int paddingLeft = 0;
    public int paddingRight = 0;
    public int paddingTop = 0;
    public int paddingBottom = 0;
    private Object[] pointsArray;
    private int originalViewHeight;
    private int originalViewWidth;
    private int viewHeight;
    private int viewWidth;
    private PointF[] points;
    private PointF[] pointsCon1;
    private PointF[] pointsCon2;
    private Path pathArray = new Path();
    private Path pathFillArray = new Path();
    private Path pathAxis = new Path();
    private Path pathXYLines = new Path();
    private float pathHeight = 0;
    public int pathColorFrom = Color.WHITE;
    public int pathColorTo = Color.WHITE;

    public LineView(TiViewProxy proxy) {
        super(proxy);
        tiPaintView = new PaintView(proxy.getActivity());
        TiDimension dimensionWidth = new TiDimension(TiConvert.toString(proxy.getWidth()), TiDimension.TYPE_WIDTH);
        originalViewWidth = dimensionWidth.getAsPixels(getOuterView());
        dimensionHeight = new TiDimension(TiConvert.toString(proxy.getHeight()), TiDimension.TYPE_HEIGHT);
        originalViewHeight = dimensionHeight.getAsPixels(getOuterView());
        startPosition = originalViewHeight * 0.5;

        setNativeView(tiPaintView);
    }

    public void setLineColor(int color) {
        paintLine.setColor(color);
    }

    public void setLineWidth(int value) {
        TiDimension dim = new TiDimension(TiConvert.toString(value), TiDimension.TYPE_WIDTH);
        paintLine.setStrokeWidth(dim.getAsPixels(getOuterView()));
    }

    public void setPoints(Object[] pointObject) {
        clear();
        maxValue = origMaxValue;
        viewWidth = originalViewWidth - paddingLeft - paddingRight;
        viewHeight = originalViewHeight - paddingTop - paddingBottom;
        startPosition = originalViewHeight * 0.5;
        pointsArray = pointObject;
        points = new PointF[pointObject.length];
        pointsCon1 = new PointF[pointObject.length];
        pointsCon2 = new PointF[pointObject.length];

        float localMax = 0;

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
            startPosition = originalViewHeight * 0.5;
            localMax /= 0.5;
            maxValue /= 0.5;
        } else if (startAt == TiLinesModule.START_BOTTOM) {
            startPosition = originalViewHeight;
        }

        // draw below line - setup shader
        if (fillSpace) {
            pathHeight = (viewHeight / maxValue) * localMax + 10;
            paintBackground.setStyle(Paint.Style.FILL);
            paintBackground.setShader(new LinearGradient(0, viewHeight - pathHeight + paddingTop, 0, viewHeight + paddingTop, new int[]{
                    fillColorTop,
                    fillColorBottom
            }, null, Shader.TileMode.CLAMP));
        }

        float stepX = (viewWidth / (points.length - 1));

        float baseX = paddingLeft;
        int pos = 0;
        pathFillArray.moveTo(paddingLeft, viewHeight + paddingBottom);

        // set points
        for (Object point : pointObject) {
            if (point instanceof Number) {
                // just value - draw a graph
                float hPoint = TiConvert.toFloat(point);

                if (yScale) {
                    hPoint = (viewHeight / maxValue) * hPoint;
                }
                points[pos].set(baseX, (float) (-hPoint + startPosition - paddingBottom));
                if (pos < pointObject.length - 1) {
                    baseX += stepX;
                } else {
                    // last point
                    baseX = viewWidth;
                }
            } else {
                // normal lines with coordinates
                HashMap<String, Object> pointValues = (HashMap<String, Object>) point;
                TiDimension d1 = new TiDimension(TiConvert.toString(pointValues.get("x")), TiDimension.TYPE_WIDTH);
                TiDimension d2 = new TiDimension(TiConvert.toString(pointValues.get("y")), TiDimension.TYPE_HEIGHT);
                float x = TiConvert.toFloat(d1.getAsPixels(getOuterView()));
                float y = TiConvert.toFloat(d2.getAsPixels(getOuterView()));

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
        pathFillArray.lineTo(viewWidth + paddingLeft, viewHeight + paddingTop);

        // stroke type
        if (strokeType == TiLinesModule.STROKE_DASHED) {
            paintLine.setPathEffect(new DashPathEffect(new float[]{5, 10}, 0));
        }

        if (pathColorFrom != Color.WHITE && pathColorTo != Color.WHITE) {
            paintLine.setColor(Color.WHITE);
            paintLine.setShader(new LinearGradient(0, 0, viewWidth, 0, pathColorFrom, pathColorTo, Shader.TileMode.CLAMP));
        }

        // create axis
        drawAxis(showXAis, showYAis);
        tiPaintView.invalidate();
    }

    private void drawAxis(boolean drawX, boolean drawY) {
        TiDimension dim = new TiDimension(TiConvert.toString(axisWidth), TiDimension.TYPE_WIDTH);

        paintAxis.setColor(axisColor);
        paintAxis.setStrokeWidth(dim.getAsPixels(getOuterView()));

        if (drawX) {
            if (startAt == TiLinesModule.START_CENTER) {
                pathAxis.moveTo(paddingLeft, viewHeight * 0.5f);
                pathAxis.lineTo(viewWidth + paddingLeft, viewHeight * 0.5f);
            } else {
                pathAxis.moveTo(paddingLeft, viewHeight + paddingTop);
                pathAxis.lineTo(viewWidth + paddingLeft, viewHeight + paddingTop);
            }
        }
        if (drawY) {
            pathAxis.moveTo(paddingLeft, viewHeight + paddingBottom);
            pathAxis.lineTo(paddingLeft, paddingTop);
        }

        if (yLines > 0) {
            // dashed horizontal lines
            float steps = (viewHeight / yLines);
            if (yScale) {
                steps = (viewHeight / maxValue) * yLines;
            }
            if (steps > 0) {
                float yPos = viewHeight - steps + paddingBottom;
                while (yPos > paddingTop) {
                    pathXYLines.moveTo(paddingLeft, yPos);
                    pathXYLines.lineTo(viewWidth + paddingLeft, yPos);
                    yPos -= steps;
                }
            }
        }

        if (xLines > 0) {
            //  dashed vertical lines
            float steps = (viewWidth / (points.length - 1)) * xLines;
            float xPos = paddingLeft;
            if (steps > 0) {
                while (xPos <= viewWidth) {
                    pathXYLines.moveTo(xPos, paddingTop);
                    pathXYLines.lineTo(xPos, viewHeight + paddingBottom);
                    xPos += steps;
                }
                pathXYLines.moveTo(xPos, paddingTop);
                pathXYLines.lineTo(xPos, viewHeight + paddingBottom);
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
        pathXYLines = new Path();
    }

    public class PaintView extends View {
        private boolean clearAll = false;

        public PaintView(Context c) {
            super(c);

            paintAxis.setStyle(Paint.Style.STROKE);
            paintAxis.setStrokeWidth(1);
            paintAxis.setColor(Color.WHITE);
            setLayerType(View.LAYER_TYPE_HARDWARE, paintAxis);

            paintLine.setStrokeWidth(1);
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

            if ((originalViewWidth == 0 && (proxy.getWidth().equals("fill") || proxy.getWidth().equals("size"))) || originalViewWidth != w) {
                originalViewWidth = w;
                setPoints(pointsArray);
            }
            if ((originalViewHeight == 0 && (proxy.getHeight().equals("fill") || proxy.getHeight().equals("size"))) || originalViewHeight != h) {
                originalViewHeight = h;
                setPoints(pointsArray);
            }
        }


        @Override
        protected void onDraw(Canvas canvas) {
            if (clearAll) {
                Paint clearPaint = new Paint();
                clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                canvas.drawRect(0, 0, 0, 0, clearPaint);
                clearAll = false;
            } else {
                canvas.drawPath(pathXYLines, paintYLines);
                canvas.drawPath(pathAxis, paintAxis);

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