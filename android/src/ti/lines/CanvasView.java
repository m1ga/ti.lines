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

//    private int circleBorderWidth = 0;
    public String[] circleColors;
    PaintView tiPaintView;
    RectF oval = new RectF(0f, 0f, 100f,100f);
    Boolean drawCircle = false;
    private int originalViewHeight;
    private TiDimension dimensionHeight;
    private int originalViewWidth;
    private final Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CanvasView(TiViewProxy proxy) {
        super(proxy);
        tiPaintView = new PaintView(proxy.getActivity());
        TiDimension dimensionWidth = new TiDimension(TiConvert.toString(proxy.getWidth()), TiDimension.TYPE_WIDTH);
        originalViewWidth = dimensionWidth.getAsPixels(getOuterView());
        dimensionHeight = new TiDimension(TiConvert.toString(proxy.getHeight()), TiDimension.TYPE_HEIGHT);
        originalViewHeight = dimensionHeight.getAsPixels(getOuterView());
        oval = new RectF(0f, 0f, originalViewWidth,dimensionHeight.getAsPixels(tiPaintView));
        setNativeView(tiPaintView);
    }

    public void redraw() {
        tiPaintView.invalidate();
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
                float circleAngle = (float) 360 /slices;

                for (int i=0; i<slices;++i) {
                    circlePaint.setColor(TiConvert.toColor(circleColors[i], TiApplication.getAppCurrentActivity()));
                    canvas.drawArc(oval, circleAngle*i, circleAngle, true, circlePaint);
                }
            }
        }


        public void clear() {
            clearAll = true;
            invalidate();
        }
    }

    public void drawCircle(KrollDict kd) {
        if (kd.containsKeyAndNotNull("circleColors")) {
            circleColors = kd.getStringArray("circleColors");
            redraw();
        }
    }
}
