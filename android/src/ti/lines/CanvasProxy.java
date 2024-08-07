package ti.lines;

import android.app.Activity;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiDimension;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.view.TiUIView;

@Kroll.proxy(creatableInModule = TiLinesModule.class)
public class CanvasProxy extends TiViewProxy {
    CanvasView view;
    private String[] circleColors;
    private int startRotation = -90;
    private boolean directionCw = true;
    private float borderWidth = 1;

    public CanvasProxy() {
        super();
    }

    @Override
    public TiUIView createView(Activity activity) {
        view = new CanvasView(this);
        updateView();
        return view;
    }

    private void updateView() {
        view.circleColors = circleColors;
        view.startRotation = startRotation;
        view.directionCw = directionCw;
        view.borderWidth = new TiDimension(borderWidth, TiDimension.TYPE_WIDTH).getAsPixels(view.tiPaintView);
        view.redraw();
    }

    @Override
    public void handleCreationDict(KrollDict options) {
        super.handleCreationDict(options);
        if (options.containsKeyAndNotNull("circleColors")) {
            circleColors = options.getStringArray("circleColors");
        }
        if (options.containsKeyAndNotNull("startRotation")) {
            startRotation = options.getInt("startRotation") - 90;
        }
        if (options.containsKeyAndNotNull("direction")) {
            directionCw = (options.getInt("direction") == TiLinesModule.DIRECTION_CW);
        }
        if (options.containsKeyAndNotNull("circleBorderWidth")) {
            borderWidth = options.getInt("circleBorderWidth");
        }
    }

    @Kroll.method
    private void drawCircle(KrollDict kd) {
        if (view != null) {
            view.drawCircle(kd);
        }
    }

}
