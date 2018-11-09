package com.dudubaika.ext

import android.content.Context
import android.widget.Toast
import com.dudubaika.base.App

fun Context.getComponent() = App.instance.appComponent

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}


