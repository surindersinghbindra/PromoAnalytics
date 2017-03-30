package com.promoanalytics.utils.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class RobotoLightTextView extends android.support.v7.widget.AppCompatTextView {

    public RobotoLightTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RobotoLightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobotoLightTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/robotolight.ttf");
            setTypeface(tf);

    }
}