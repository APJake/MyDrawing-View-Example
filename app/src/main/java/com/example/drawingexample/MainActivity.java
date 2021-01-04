package com.example.drawingexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import top.defaults.colorpicker.ColorPickerPopup;

public class MainActivity extends AppCompatActivity {

    MyDrawing drawing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawing = new MyDrawing(this);
        setContentView(drawing);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.draw_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        switch (id){
            case R.id.mn_clean:
                drawing.clearPath();
                return true;
            case R.id.mn_change_bg_color:
                showColorPicker(true);
                return true;
            case R.id.mn_change_pen_color:
                showColorPicker(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    void showColorPicker(boolean isBg){
        new ColorPickerPopup.Builder(this)
                .initialColor(Color.RED)
                .enableBrightness(true)
                .enableAlpha(true)
                .okTitle("Pick")
                .cancelTitle("Cancel")
                .showIndicator(true)
                .showValue(true)
                .build()
                .show(new ColorPickerPopup.ColorPickerObserver() {
                    @Override
                    public void onColorPicked(int color) {
                        if(isBg) drawing.changeBgColor(color);
                        else drawing.changePathColor(color);
                    }
                });
    }

    class MyDrawing extends View{

        Paint paint;
        Bitmap bitmap;
        Path path=new Path();
        Rect rect;
        int bgColor, pathColor;

        public MyDrawing(Context context) {
            super(context);

            paint = new Paint();
            bgColor = Color.WHITE;
            pathColor = Color.parseColor("#000000");
        }

        void changeBgColor(int c){
            bgColor = c;
            invalidate();
        }

        public void changePathColor(int color) {
            pathColor = color;
            invalidate();
        }

        void clearPath(){
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(bgColor);
            canvas.drawPaint(paint);

            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(pathColor);
            paint.setStrokeWidth(5f);
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(x,y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    path.lineTo(x,y);
                    break;
            }
            invalidate();

            return true;
        }
    }

}