package com.example.lib_common_ui.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.example.lib_common_ui.R;

/**
 * @author Sherlock
 * 自定义圆形控件
 */
public class MyCircleView extends androidx.appcompat.widget.AppCompatImageButton {
    /**
     * view大小
     */
    private int viewWidth;
    private int viewHeight;

    /**
     * 大圆半径
     */
    private int bigCircleRadius = 100;

    private final float RATIO = 0.92f;

    /**
     * 小圆半径
     */
    private float smallCircleRadius = (float)(RATIO * bigCircleRadius);

    /**
     * 大圆画笔
     */
    private Paint bigCirclePaint;

    /**
     * 小圆画笔
     */
    private Paint smallCirclePaint;

    /**
     * 圆心坐标
     */
    private int centerX;

    private int centerY;

    /**
     * icon图标
     */
    private Drawable iconSrc;
    private Bitmap resource;

    /**
     * bitmapPaint画笔
     */
    private Paint bitmapPaint;

    /**
     * 图标大小
     */
    private int resourceWidth;
    private int resourceHeight;

    private Rect mSrcRect;
    private Rect mDestRect;


    public MyCircleView(Context context) {
        this(context,null,0);
    }

    public MyCircleView(Context context,@Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public MyCircleView(Context context,@Nullable AttributeSet attrs,int defStyleAttr) {
        super(context,attrs,defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * 初始化控件属性及相关画图工具
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr){
        initAttrs(context, attrs, defStyleAttr);
        //初始化画笔
        initPaint();
    }

    /**
     * 初始化自定义控件属性
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr){
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.MyCircleView, defStyleAttr, 0);
        bigCircleRadius = attr.getDimensionPixelSize(R.styleable.MyCircleView_radius, bigCircleRadius);
        smallCircleRadius = bigCircleRadius * RATIO;
        iconSrc = attr.getDrawable(R.styleable.MyCircleView_icon_src);
        resourceWidth = iconSrc.getIntrinsicWidth();
        resourceHeight = iconSrc.getIntrinsicHeight();
        attr.recycle();
    }

    /**
     * 初始化画笔
     */
    private void initPaint(){
        //初始化画笔
        bigCirclePaint = new Paint();
        bigCirclePaint.setColor(Color.WHITE);
        bigCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        bigCirclePaint.setAntiAlias(true);

        smallCirclePaint = new Paint();
        smallCirclePaint.setColor(Color.rgb(128,200,169));
        bigCirclePaint.setAntiAlias(true);

        bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmapPaint.setFilterBitmap(true);
        bitmapPaint.setDither(true);
    }

    @Override
    protected void onSizeChanged(int w,int h,int oldw,int oldh) {
        viewWidth = w;
        viewHeight = h;
        centerX = w / 2;
        centerY = h / 2;
        bigCircleRadius = w / 2;
        smallCircleRadius = bigCircleRadius * RATIO;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        /**
         * AT_MODE: wrap_content
         * EXACTLY(确切的): match_parent 100.dp
         *
         */
        if(widthMode == MeasureSpec.AT_MOST){
            width = (int)(1.0f * bigCircleRadius * 2);
        }
        else if(widthMode == MeasureSpec.EXACTLY){

        }
        else if(widthMode == MeasureSpec.UNSPECIFIED){
            //尽可能大的
        }

        if(heightMode == MeasureSpec.AT_MOST){
            height = (int)(1.0f * bigCircleRadius * 2);
        }
        else if(heightMode == MeasureSpec.EXACTLY){

        }
        else if(heightMode == MeasureSpec.UNSPECIFIED){
            //尽可能大的
        }

        //设置自定义View的宽度和高度
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(centerX,centerY,bigCircleRadius,bigCirclePaint);
        canvas.drawCircle(centerX,centerY,smallCircleRadius,smallCirclePaint);
        if(iconSrc != null){
            initBitmap();
            canvas.drawBitmap(resource,null,mDestRect,bitmapPaint);
        }
    }

    private void initBitmap(){
        mSrcRect = new Rect(0,0,resourceWidth,resourceHeight);
        int left = (viewWidth - resourceWidth) / 2;
        int top = (viewHeight - resourceHeight) / 2;
        mDestRect = new Rect(left,top,viewWidth - left,viewHeight-top);
        resource = drawableToBitmap(iconSrc,resourceWidth,resourceHeight);
    }

    /**
     * 将drawable转换为bitmap
     * @param drawable
     * @param width
     * @param height
     * @return
     */
    public Bitmap drawableToBitmap(Drawable drawable, int width, int height) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getMinimumWidth(), drawable.getMinimumHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


}
