package com.dudubaika.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.dudubaika.R
import kotlinx.android.synthetic.main.view_me_item.view.*

/**
 * Created by Administrator on 2017/7/26.
 */
class MeItemView : FrameLayout {
     constructor(context: Context) : super(context) {
        init()
    }
    var view : View? = null
    var ivIcon : ImageView? = null
    var tvDesc : TextView? = null

     constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

     constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    fun init(): Unit {
        var view = LayoutInflater.from(context).inflate(R.layout.view_me_item, this, false);
        ivIcon = view.iv_me_icon
        tvDesc = view.tv_me_desc
        addView(view)
    }

    public fun setData(iconId: Int, desc: String):MeItemView {
        ivIcon?.setImageResource(iconId)
        tvDesc?.text = desc;
        return this
    }
}