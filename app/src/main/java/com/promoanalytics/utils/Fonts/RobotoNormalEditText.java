package com.promoanalytics.utils.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class RobotoNormalEditText extends android.support.v7.widget.AppCompatEditText {

    public RobotoNormalEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RobotoNormalEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobotoNormalEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto_regular.ttf");
            setTypeface(tf);
        }
    }
}