package com.promoanalytics.utils.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class RobotoLightEditText extends android.support.v7.widget.AppCompatEditText {

    public RobotoLightEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RobotoLightEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobotoLightEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/robotolight.ttf");
            setTypeface(tf);
        }
    }
}