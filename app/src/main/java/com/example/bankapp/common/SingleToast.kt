package com.example.bankapp.common

import android.content.Context
import android.widget.Toast

object SingleToast {

    fun show(context: Context?, text: String?, duration: Int) {
        if (this::mToast.isInitialized) {
            mToast.cancel()
        }
        mToast = Toast.makeText(context, text, duration)
        mToast.show()
    }
    private lateinit var mToast: Toast

}