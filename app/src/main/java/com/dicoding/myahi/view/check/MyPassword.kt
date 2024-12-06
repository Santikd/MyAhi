package com.dicoding.myahi.view.check

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class MyPassword : AppCompatEditText {
    // Constructor for initialization
    constructor(context: Context) : super(context) {
        initializeWatcher()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initializeWatcher()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initializeWatcher()
    }

    private fun initializeWatcher() {
        // Attach a TextWatcher to monitor password input
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
                // No action required here
            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                val password = text.toString()
                if (password.length < 8) {
                    this@MyPassword.error = "Password must be at least 8 characters"
                } else {
                    this@MyPassword.error = null
                }
            }

            override fun afterTextChanged(editable: Editable?) {
                // No action required here
            }
        })
    }
}
