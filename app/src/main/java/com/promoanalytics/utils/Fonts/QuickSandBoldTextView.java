package com.promoanalytics.utils.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class QuickSandBoldTextView extends android.support.v7.widget.AppCompatTextView {

    public QuickSandBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public QuickSandBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QuickSandBoldTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/quicksand_bold.ttf");
            setTypeface(tf);

    }
}