package diggate.xpertise.com.diggateapk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.io.ByteArrayOutputStream;

public class CaptureBitmapView extends View {
    private Bitmap _Bitmap;
    private Canvas _Canvas;
    private Path _Path;
    private Paint _BitmapPaint;
    private Paint _paint;
    private float _mX;
    private float _mY;
    private float TouchTolerance = 4;
    private float LineThickness = 4;
    public CaptureBitmapView(Context context, AttributeSet attr) {
        super(context, attr);
        _Path = new Path();
        _BitmapPaint = new Paint(Paint.DITHER_FLAG);
        _paint = new Paint();
        _paint.setAntiAlias(true);
        _paint.setDither(true);
        _paint.setColor(Color.argb(255, 0, 0, 0));
        _paint.setStyle(Paint.Style.STROKE);
        _paint.setStrokeJoin(Paint.Join.ROUND);
        _paint.setStrokeCap(Paint.Cap.ROUND);
        _paint.setStrokeWidth(LineThickness);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        _Bitmap = Bitmap.createBitmap(w, (h > 0 ? h : ((View) this.getParent()).getHeight()), Bitmap.Config.ARGB_8888);
        _Canvas = new Canvas(_Bitmap);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        //canvas.drawColor(Color.argb(255, 237, 239, 250));
        canvas.drawBitmap(_Bitmap, 0, 0, _BitmapPaint);
        canvas.drawPath(_Path, _paint);
    }
    private void TouchStart(float x, float y) {
        _Path.reset();
        _Path.moveTo(x, y);
        _mX = x;
        _mY = y;
    }
    private void TouchMove(float x, float y) {
        float dx = Math.abs(x - _mX);
        float dy = Math.abs(y - _mY);
        if (dx >= TouchTolerance || dy >= TouchTolerance) {
            _Path.quadTo(_mX, _mY, (x + _mX) / 2, (y + _mY) / 2);
            _mX = x;
            _mY = y;
        }
    }
    private void TouchUp() {
        if (!_Path.isEmpty()) {
            _Path.lineTo(_mX, _mY);
            _Canvas.drawPath(_Path, _paint);
        } else {
            _Canvas.drawPoint(_mX, _mY, _paint);
        }
        _Path.reset();
    }
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        super.onTouchEvent(e);
        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                TouchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                TouchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                TouchUp();
                invalidate();
                break;
        }
        return true;
    }
    public void ClearCanvas() {
        _Canvas.drawColor(Color.WHITE);
        //_Canvas.drawColor(Color.argb(255, 237, 239, 250));
        invalidate();
    }
    public byte[] getBytes() {
        Bitmap b = getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    public Bitmap getBitmap() {
        View v = (View) this.getParent();
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }
}
