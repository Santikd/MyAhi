package com.dicoding.myahi.view.check

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class MyEmail : AppCompatEditText {

    constructor(context: Context) : super(context) {
        setupTextWatcher()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setupTextWatcher()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setupTextWatcher()
    }

    private fun setupTextWatcher() {

        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {
                            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                val input = sequence.toString()
                val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
                if (input.isNotEmpty() && !input.matches(emailRegex.toRegex())) {
                    this@MyEmail.error = "Invalid email format"
                } else {
                    this@MyEmail.error = null
                }
            }

            override fun afterTextChanged(editable: Editable?) {

            }
        })
    }
}
