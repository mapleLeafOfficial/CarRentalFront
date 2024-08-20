package com.example.carrentalapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.carrentalapp.R;

public class CircularImageView extends AppCompatImageView {
    private Bitmap bitmap;
    private Paint paint;
    private int radius;

    public CircularImageView(Context context) {
        super(context);
        init();
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avator);
        this.bitmap=bitmap;
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bitmap != null) {
            radius = Math.min(getWidth(), getHeight()) / 2;
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, radius * 2, radius * 2, false);
            BitmapShader shader = new BitmapShader(scaledBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint);
        } else {
            super.onDraw(canvas);
        }
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        invalidate();
    }
}