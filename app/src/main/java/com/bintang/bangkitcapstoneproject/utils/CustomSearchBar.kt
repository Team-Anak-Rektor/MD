package com.bintang.bangkitcapstoneproject.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.bintang.bangkitcapstoneproject.R

class CustomSearchBar : AppCompatEditText, View.OnTouchListener {

    private lateinit var searchButtonImage: Drawable

    var showSearchButton: Boolean = false

    var searchText: String = " "

    constructor(context: Context) : super(context) { init() }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { init() }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init() }

    private fun init() {
        searchButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_search) as Drawable
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotEmpty()) showSearchButton() else hideSearchButton()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }


    private fun showSearchButton() {
        setSearchButtonDrawables(endOfTheText = searchButtonImage)
        showSearchButton = true
    }

    private fun hideSearchButton() {
        setSearchButtonDrawables()
        showSearchButton = false
    }

    private fun setSearchButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null,
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText, topOfTheText, endOfTheText, bottomOfTheText
        )
    }

    override fun onTouch(p0: View?, p1: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val searchButtonStart: Float
            val searchButtonEnd: Float
            var isSearchButtonClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                searchButtonEnd = (searchButtonImage.intrinsicWidth + paddingStart).toFloat()
                when {
                    p1.x < searchButtonEnd -> isSearchButtonClicked = true
                }
            } else {
                searchButtonStart = (width - paddingEnd - searchButtonImage.intrinsicWidth).toFloat()
                when {
                    p1.x > searchButtonStart -> isSearchButtonClicked = true
                }
            }
            if (isSearchButtonClicked) {
                return when (p1.action) {
                    MotionEvent.ACTION_DOWN -> {
                        searchButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_search) as Drawable
                        showSearchButton()
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        searchButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_search) as Drawable
                        when {
                            text != null -> searchText = text.toString()
                        }
                        true
                    }
                    else -> false
                }
            } else return false
        }
        return false
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
}
