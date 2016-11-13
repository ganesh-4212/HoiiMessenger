package com.kempasolutions.app.hoiimessanger.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;

/**
 * Created by madhur on 17/01/15.
 */
public class ChatLayout extends RelativeLayout {


    public ChatLayout(Context context) {
        super(context);
    }

    public ChatLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public ChatLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final float adjustVal = (float) 12.667;

        if (getChildCount() < 3)
            return;
        if (getChildCount() == 3) {
            int imageViewWidth = getChildAt(0).getMeasuredWidth();
            int timeWidth = getChildAt(1).getMeasuredWidth();

            int messageHeight = getChildAt(2).getMeasuredHeight();
            int messageWidth = getChildAt(2).getMeasuredWidth();

            int layoutWidth = (int) (imageViewWidth + timeWidth + messageWidth + convertDpToPixel(adjustVal, getContext()));

            setMeasuredDimension(layoutWidth, messageHeight);
        } else if (getChildCount() == 4) {
            int imageViewWidth = getChildAt(0).getMeasuredWidth();
            int timeWidth = getChildAt(1).getMeasuredWidth();

            int messageHeight = getChildAt(2).getMeasuredHeight();
            int messageWidth = getChildAt(2).getMeasuredWidth();
            int imageHeight = getChildAt(3).getMeasuredHeight();
            int imageWidth = getChildAt(3).getMeasuredWidth();

            int layoutWidth = (int) (imageViewWidth + timeWidth + imageWidth + convertDpToPixel(adjustVal, getContext()));

            setMeasuredDimension(layoutWidth, messageHeight + imageHeight);
        }
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

}
