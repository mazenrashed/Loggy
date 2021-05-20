package com.mazenrashed.auth_lib.utils

import android.content.Context
import android.widget.Toast


fun String.toast(context: Context?) {
    context?.let { context ->
        Toast.makeText(
            context,
            this,
            Toast.LENGTH_SHORT
        ).show()
    }

}