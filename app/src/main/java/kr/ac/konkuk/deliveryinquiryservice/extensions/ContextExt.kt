package kr.ac.konkuk.deliveryinquiryservice.extensions

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context.color(@ColorRes colorResId: Int): Int = ContextCompat.getColor(this, colorResId)