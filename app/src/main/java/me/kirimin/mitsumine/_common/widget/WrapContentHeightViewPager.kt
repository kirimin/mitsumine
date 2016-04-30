package me.kirimin.mitsumine._common.widget

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

        for (i in 0..childCount - 1) {
            val view = getChildAt(i)
            view.measure(widthMeasureSpec, heightMeasureSpec)
            if (i == 0) {
                setMeasuredDimension(getMeasuredWidth(), measureHeight(heightMeasureSpec, getChildAt(0)))
            }
        }
    }

    private fun measureHeight(measureSpec: Int, view: View): Int {
        var result = 0
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = view.measuredHeight
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }
        return result
    }
}
