package ti.lines;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiDimension;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.view.TiUIView;

public class CanvasView extends TiUIView {

    private final Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final int originalViewHeight;
    private final TiDimension dimensionHeight;
    private final int originalViewWidth;
    public String[] circleColors;
    public int startRotation = -90;
    public float borderWidth = 0;
    public float originalRadius = 0;
    public boolean directionCw = true;
    PaintView tiPaintView;
    RectF oval = new RectF(0f, 0f, 100f, 100f);

    public CanvasView(TiViewProxy proxy) {
        super(proxy);
        tiPaintView = new PaintView(proxy.getActivity());
        TiDimension dimensionWidth = new TiDimension(TiConvert.toString(proxy.getWidth()), TiDimension.TYPE_WIDTH);
        originalViewWidth = dimensionWidth.getAsPixels(getOuterView());
        dimensionHeight = new TiDimension(TiConvert.toString(proxy.getHeight()), TiDimension.TYPE_HEIGHT);
        originalViewHeight = dimensionHeight.getAsPixels(getOuterView());
        originalRadius = (float) (dimensionHeight.getAsPixels(getOuterView()) * 0.5);
        oval = new RectF(0f, 0f, originalViewWidth, dimensionHeight.getAsPixels(tiPaintView) + borderWidth);
        setNativeView(tiPaintView);
    }

    public void redraw() {
        tiPaintView.invalidate();
    }

    public void drawCircle(KrollDict kd) {
        if (kd.containsKeyAndNotNull("circleColors")) {
            circleColors = kd.getStringArray("circleColors");
        }
        if (kd.containsKeyAndNotNull("startRotation")) {
            startRotation = kd.getInt("startRotation") - 90;
        }
        if (kd.containsKeyAndNotNull("direction")) {
            directionCw = (kd.getInt("direction") == TiLinesModule.DIRECTION_CW);
        }
        redraw();
    }

    public class PaintView extends View {
        private boolean clearAll = false;

        public PaintView(Context c) {
            super(c);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            int slices = circleColors.length;
            if (slices > 0) {
                circlePaint.setStyle(Paint.Style.FILL);

                circlePaint.setColor(TiConvert.toColor(circleColors[0], TiApplication.getAppCurrentActivity()));
                float circleAngle = (float) 360 / slices;
                int val = -1;
                if (directionCw) {
                    val = 1;
                } else {
                    startRotation -= circleAngle;
                }

                for (int i = 0; i < slices; ++i) {
                    circlePaint.setColor(TiConvert.toColor(circleColors[i], TiApplication.getAppCurrentActivity()));
                    canvas.drawArc(oval, val * circleAngle * i + startRotation, circleAngle, true, circlePaint);
                }

                if (proxy.hasPropertyAndNotNull("circleBorderColor")) {
                    oval.set(
                            (float) originalViewWidth / 2 - originalRadius + (borderWidth * 0.5f),
                            (float) originalViewHeight / 2 - originalRadius + (borderWidth * 0.5f),
                            (float) originalViewWidth / 2 + originalRadius - (borderWidth * 0.5f),
                            (float) originalViewHeight / 2 + originalRadius - (borderWidth * 0.5f)
                    );

                    circlePaint.setStyle(Paint.Style.STROKE);
                    circlePaint.setColor(TiConvert.toColor((String) proxy.getProperty("circleBorderColor"), TiApplication.getAppCurrentActivity()));
                    circlePaint.setStrokeWidth(borderWidth);

                    circleAngle = (float) 360 / slices;

                    for (int i = 0; i < slices; ++i) {
                        canvas.drawArc(oval, circleAngle * i + startRotation, circleAngle, true, circlePaint);
                    }
                }
            }
        }


        public void clear() {
            clearAll = true;
            invalidate();
        }
    }
}
