package jti.jasminsa.mycustomview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class MyEditText : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(pass: CharSequence, start: Int, before: Int, count: Int) {
                error = if(pass.isNotEmpty()){
                    if(pass.length < 8){
                        "Pasword harus lebih dari 8 karakter"
                    }else{
                        null
                    }
                }else{
                    "Masukan password"
                }
            }
            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }
}