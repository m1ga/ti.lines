package ti.lines;

import android.graphics.Canvas;
import android.graphics.Paint;

import org.appcelerator.kroll.common.Log;

public class CustomText {
    private final float x;
    private final float y;
    private final String text;
    private final float offsetX;

    public CustomText(String text, float x, float y, float offsetX) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.offsetX = offsetX;
    }

    public void draw(Canvas canvas, Paint paint) {
        float center = (int) (y - ((paint.descent() + paint.ascent()) / 2));
        canvas.drawText(this.text, this.x + this.offsetX, center, paint);
    }
}
