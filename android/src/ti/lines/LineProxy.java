/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * TiDev Titanium Mobile
 * Copyright TiDev, Inc. 04/07/2022-Present
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
package ti.lines;

import android.app.Activity;
import android.graphics.Color;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiDimension;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.view.TiUIView;


@Kroll.proxy(creatableInModule = TiLinesModule.class)
public class LineProxy extends TiViewProxy {
    static final int MSG_POINTS = KrollProxy.MSG_LAST_ID + 101;
    private static final String LCAT = "LineProxy";
    private LineView view;
    private Object[] points;
    private int lineColor = Color.BLACK;
    private int lineWidth = 1;
    private int axisWidth = 1;
    private int startAt = 0;
    private int maxValue = -1;
    private int strokeType = TiLinesModule.STROKE_NORMAL;
    private int xLines = 0;
    private int yLines = 0;
    private int paddingLeft = 0;
    private int paddingRight = 0;
    private int paddingTop = 0;
    private int paddingBottom = 0;
    private int fillColorTop = Color.WHITE;
    private int fillColorBottom = Color.WHITE;
    private int axisColor = Color.WHITE;
    private int lineType = TiLinesModule.TYPE_CURVED;
    private boolean showXAxis = false;
    private boolean showYAxis = false;
    private boolean fillSpace = false;

    // Constructor
    public LineProxy() {
        super();
    }

    @Override
    public TiUIView createView(Activity activity) {
        view = new LineView(this);
        updateView();
        return view;
    }

    private void updateView() {
        if (view != null) {

            view.paddingLeft = new TiDimension(TiConvert.toString(paddingLeft), TiDimension.TYPE_WIDTH).getAsPixels(view.tiPaintView);
            view.paddingRight = new TiDimension(TiConvert.toString(paddingRight), TiDimension.TYPE_WIDTH).getAsPixels(view.tiPaintView);
            view.paddingTop = new TiDimension(TiConvert.toString(paddingTop), TiDimension.TYPE_WIDTH).getAsPixels(view.tiPaintView);
            view.paddingBottom = new TiDimension(TiConvert.toString(paddingBottom), TiDimension.TYPE_WIDTH).getAsPixels(view.tiPaintView);
            view.setLineColor(lineColor);
            view.setLineWidth(lineWidth);
            view.startAt = startAt;
            view.origMaxValue = maxValue;
            view.showXAis = showXAxis;
            view.showYAis = showYAxis;
            view.yLines = yLines;
            view.axisWidth = axisWidth;
            view.fillSpace = fillSpace;
            view.xLines = xLines;
            view.axisColor = axisColor;
            view.lineType = lineType;
            view.strokeType = strokeType;
            view.fillColorTop = fillColorTop;
            view.fillColorBottom = fillColorBottom;

            if (points != null) {
                view.setPoints(points);
            }
        }
    }

    @Override
    public void handleCreationDict(KrollDict options) {
        super.handleCreationDict(options);
        if (options.containsKey("values")) {
            points = (Object[]) options.get("values");
        }
        if (options.containsKey("lineColor")) {
            lineColor = TiConvert.toColor(options.getString("lineColor"), TiApplication.getAppCurrentActivity());
        }
        if (options.containsKey("lineWidth")) {
            lineWidth = TiConvert.toInt(options.get("lineWidth"));
        }
        if (options.containsKey("startAt")) {
            startAt = TiConvert.toInt(options.get("startAt"));
        }
        if (options.containsKey("maxValue")) {
            maxValue = TiConvert.toInt(options.get("maxValue"));
        }
        if (options.containsKey("xAxis")) {
            showXAxis = TiConvert.toBoolean(options.get("xAxis"), false);
        }
        if (options.containsKey("yAxis")) {
            showYAxis = TiConvert.toBoolean(options.get("yAxis"), false);
        }
        if (options.containsKey("yLines")) {
            yLines = TiConvert.toInt(options.get("yLines"));
        }
        if (options.containsKey("xLines")) {
            xLines = TiConvert.toInt(options.get("xLines"));
        }
        if (options.containsKey("axisWidth")) {
            axisWidth = TiConvert.toInt(options.get("axisWidth"));
        }
        if (options.containsKey("fillSpace")) {
            fillSpace = TiConvert.toBoolean(options.get("fillSpace"), false);
        }
        if (options.containsKey("lineType")) {
            lineType = TiConvert.toInt(options.get("lineType"));
        }
        if (options.containsKey("strokeType")) {
            strokeType = TiConvert.toInt(options.get("strokeType"));
        }
        if (options.containsKey("padding")) {
            if (options.get("padding") instanceof Integer) {
                paddingTop = options.getInt("padding");
                paddingRight = options.getInt("padding");
                paddingBottom = options.getInt("padding");
                paddingLeft = options.getInt("padding");
            } else {
                Object[] padding = (Object[]) options.get("padding");
                if (padding.length == 4) {
                    paddingTop = TiConvert.toInt(padding[0]);
                    paddingRight = TiConvert.toInt(padding[1]);
                    paddingBottom = TiConvert.toInt(padding[2]);
                    paddingLeft = TiConvert.toInt(padding[3]);
                }
            }
        }
        if (options.containsKey("fillColorTop")) {
            fillColorTop = TiConvert.toColor(options.getString("fillColorTop"), TiApplication.getAppCurrentActivity());
        }
        if (options.containsKey("axisColor")) {
            axisColor = TiConvert.toColor(options.getString("axisColor"), TiApplication.getAppCurrentActivity());
        }
        if (options.containsKey("fillColorBottom")) {
            fillColorBottom = TiConvert.toColor(options.getString("fillColorBottom"), TiApplication.getAppCurrentActivity());
        }
        updateView();
    }

    @Kroll.setProperty
    private void setValues(Object[] obj) {
        points = obj;
    }

    @Kroll.setProperty
    private void setLineColor(Object obj) {
        lineColor = TiConvert.toColor(obj, TiApplication.getAppCurrentActivity());
        if (view != null) view.setLineColor(lineColor);
    }
    @Kroll.setProperty
    private void setAxisColor(Object obj) {
        axisColor = TiConvert.toColor(obj, TiApplication.getAppCurrentActivity());
        if (view != null) view.axisColor = axisColor;
    }

    @Kroll.setProperty
    private void setStrokeType(int value) {
        strokeType = TiConvert.toInt(value);
        if (view != null) view.strokeType = strokeType;
    }
    @Kroll.setProperty
    private void setAxisWidth(int value) {
        axisWidth = TiConvert.toInt(value);
        if (view != null) view.axisWidth = axisWidth;
    }

    @Kroll.setProperty
    private void setLineWidth(Object obj) {
        lineWidth = TiConvert.toInt(obj);
        if (view != null) view.setLineWidth(lineWidth);
    }

    @Kroll.setProperty
    private void setFillSpace(Object obj) {
        fillSpace = TiConvert.toBoolean(obj, false);
        if (view != null) view.fillSpace = fillSpace;
    }

    @Kroll.setProperty
    private void fillColorTop(String obj) {
        fillColorTop = TiConvert.toColor(obj, TiApplication.getAppCurrentActivity());
        if (view != null) view.fillColorTop = fillColorTop;
    }

    @Kroll.setProperty
    private void setFillColorBottom(String obj) {
        fillColorTop = TiConvert.toColor(obj, TiApplication.getAppCurrentActivity());
        if (view != null) view.fillColorBottom = fillColorBottom;
    }

    @Kroll.setProperty
    private void setLineType(Object obj) {
        lineType = TiConvert.toInt(obj);
        if (view != null) view.lineType = lineType;
    }

    @Kroll.setProperty
    private void setXLines(Object obj) {
        xLines = TiConvert.toInt(obj);
        if (view != null) view.xLines = xLines;
    }

    @Kroll.setProperty
    private void setYLines(Object obj) {
        yLines = TiConvert.toInt(obj);
        if (view != null) view.yLines = yLines;
    }

    @Kroll.setProperty
    private void setXAxis(Object obj) {
        showXAxis = TiConvert.toBoolean(obj, false);
        if (view != null) view.showXAis = showXAxis;
    }

    @Kroll.setProperty
    private void setYAxis(Object obj) {
        showYAxis = TiConvert.toBoolean(obj, false);
        if (view != null) view.showYAis = showYAxis;
    }

    @Kroll.setProperty
    private void setMaxValue(Object obj) {
        maxValue = TiConvert.toInt(obj);
        if (view != null) view.maxValue = maxValue;
    }

    @Kroll.setProperty
    private void setStartAt(Object obj) {
        startAt = TiConvert.toInt(obj);
        if (view != null) view.startAt = startAt;
    }

    @Kroll.setProperty
    private void setPaddingTop(Object obj) {
        paddingTop = TiConvert.toInt(obj);
        if (view != null) view.paddingTop = paddingTop;
    }

    @Kroll.setProperty
    private void setPadding(Object obj) {
        if (obj instanceof Integer) {
            paddingLeft = paddingBottom = paddingRight = paddingTop = TiConvert.toInt(obj);
        } else {
            Object[] padding = (Object[]) obj;
            if (padding.length == 4) {
                paddingTop = TiConvert.toInt(padding[0]);
                paddingRight = TiConvert.toInt(padding[1]);
                paddingBottom = TiConvert.toInt(padding[2]);
                paddingLeft = TiConvert.toInt(padding[3]);
            }
        }
    }

    @Kroll.method
    private void clear() {
        if (view != null) {
            view.clear();
        }
    }

    @Kroll.method
    private void redraw() {
        if (view != null) {
            view.clear();
            view.setPoints(points);
        }
    }
}
