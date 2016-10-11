package me.kirimin.mitsumine.bookmarklist

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.PopupWindow
import me.kirimin.mitsumine.R

object BookmarkPopupWindowBuilder {

    public val INDEX_SHARE: Int = 0
    public val INDEX_BROWSER: Int = 1
    public val INDEX_SEARCH: Int = 2

    fun build(context: Context): PopupWindow {
        val popupWindow = PopupWindow(context)
        popupWindow.contentView = LayoutInflater.from(context).inflate(R.layout.popup_list, null)
        val list = popupWindow.contentView.findViewById(R.id.popupWindowListView) as ListView
        list.adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, listOf("共有", "ブラウザで見る", "ユーザーを検索"))
        val width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160f, context.resources.displayMetrics)
        popupWindow.height = WindowManager.LayoutParams.WRAP_CONTENT
        popupWindow.width = width.toInt()
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true
        popupWindow.isTouchable = true
        popupWindow.setBackgroundDrawable(ColorDrawable(context.resources.getColor(android.R.color.transparent)))
        return popupWindow
    }
}