package me.kirimin.mitsumine.ui.view

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View

public class WrapContentHeightViewPager : ViewPager {

    public constructor(context: Context) : super(context) {
    }

    public constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        for (i in 0..getChildCount() - 1) {
            val view = getChildAt(i)
            view.measure(widthMeasureSpec, heightMeasureSpec)
            if (i == 0) {
                setMeasuredDimension(getMeasuredWidth(), measureHeight(heightMeasureSpec, getChildAt(0)))
            }
        }
    }

    private fun measureHeight(measureSpec: Int, view: View): Int {
        var result = 0
        val specMode = View.MeasureSpec.getMode(measureSpec)
        val specSize = View.MeasureSpec.getSize(measureSpec)

        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = view.getMeasuredHeight()
            if (specMode == View.MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }
        return result
    }
}
