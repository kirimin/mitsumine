package me.kirimin.mitsumine.ui.event

import android.text.Layout
import android.text.Selection
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.widget.TextView

public class IfNeededLinkMovementMethod : LinkMovementMethod() {
    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
        val action = event.getAction()

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
            var x = event.getX().toInt()
            var y = event.getY().toInt()

            x -= widget.getTotalPaddingLeft()
            y -= widget.getTotalPaddingTop()

            x += widget.getScrollX()
            y += widget.getScrollY()

            val layout = widget.getLayout()
            val line = layout.getLineForVertical(y)
            val off = layout.getOffsetForHorizontal(line, x.toFloat())

            val link = buffer.getSpans<ClickableSpan>(off, off, javaClass<ClickableSpan>())

            if (link.size() != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    link[0].onClick(widget)
                } else {
                    Selection.setSelection(buffer, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]))
                }

                return true
            } else {
                Selection.removeSelection(buffer)
            }
        }

        return false
    }
}
