package kr.ac.konkuk.deliveryinquiryservice.extensions

import android.view.View
import androidx.annotation.ColorRes

fun View.toVisible() {
    visibility = View.VISIBLE
}

fun View.toInvisible() {
    visibility = View.INVISIBLE
}

fun View.toGone() {
    visibility = View.GONE
}


// view 의 색상을 가져오는 함수
fun View.color(@ColorRes colorResId: Int) = context.color(colorResId)