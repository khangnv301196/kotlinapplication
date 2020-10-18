package com.testing.kotlinapplication.util.view

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.testing.kotlinapplication.R


class MyInCreaseView: LinearLayout {
    internal var context: Context
    private lateinit var attrs: AttributeSet
    private var styleAttr = 0
    private lateinit var mListener: OnClickListener
    private var initialNumber = 0
    private var lastNumber = 0
    private var currentNumber = 0
    private var finalNumber = 0
    private lateinit var textView: TextView
    private lateinit var mOnValueChangeListener: OnValueChangeListener
    private lateinit var addBtn: Button
    private lateinit var subtractBtn: Button

    constructor(context: Context) : super(context) {
        this.context = context
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.context = context
        this.attrs = attrs!!
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        this.context = context
        this.attrs = attrs!!
        styleAttr = defStyleAttr
        initView()
    }

    private fun initView() {
        View.inflate(context, R.layout.layout_increase, this)
        val res: Resources = resources
        val defaultColor: Int = res.getColor(R.color.colorPrimary)
        val defaultTextColor: Int = res.getColor(R.color.colorText)
        val defaultDrawable: Drawable = res.getDrawable(R.drawable.background)
        val a: TypedArray = context.obtainStyledAttributes(
            attrs, R.styleable.ElegantNumberButton,
            styleAttr, 0
        )
        initialNumber = a.getInt(R.styleable.ElegantNumberButton_initialNumber, 0)
        finalNumber = a.getInt(R.styleable.ElegantNumberButton_finalNumber, Int.MAX_VALUE)
        val textSize = a.getDimension(R.styleable.ElegantNumberButton_textSize, 13f)
        val color = a.getColor(R.styleable.ElegantNumberButton_backGroundColor, defaultColor)
        val textColor = a.getColor(R.styleable.ElegantNumberButton_textColor, defaultTextColor)
        var drawable = a.getDrawable(R.styleable.ElegantNumberButton_backgroundDrawable)
        subtractBtn = findViewById(R.id.subtract_btn)
        addBtn = findViewById(R.id.add_btn)
        textView = findViewById(R.id.number_counter)
        val mLayout = findViewById<LinearLayout>(R.id.layout)
        subtractBtn.setTextColor(textColor)
        addBtn.setTextColor(textColor)
        textView.setTextColor(textColor)
        subtractBtn.setTextSize(textSize)
        addBtn.setTextSize(textSize)
        textView.setTextSize(textSize)
        if (drawable == null) {
            drawable = defaultDrawable
        }
        assert(drawable != null)
        drawable.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC)
        mLayout.background = drawable
        textView.setText(initialNumber.toString())
        currentNumber = initialNumber
        lastNumber = initialNumber
        subtractBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(mView: View?) {
                val num = Integer.valueOf(textView.getText().toString())
                setNumber((num - 1).toString(), true)
            }
        })
        addBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(mView: View?) {
                val num = Integer.valueOf(textView.getText().toString())
                setNumber((num + 1).toString(), true)
            }
        })
        a.recycle()
    }

    private fun callListener(view: View) {
            mListener?.onClick(view)
        if (mOnValueChangeListener != null) {
            if (lastNumber != currentNumber) {
                mOnValueChangeListener!!.onValueChange(this, lastNumber, currentNumber)
            }
        }
    }

    var number: String
        get() = currentNumber.toString()
        set(number) {
            lastNumber = currentNumber
            currentNumber = number.toInt()
            if (currentNumber > finalNumber) {
                currentNumber = finalNumber
            }
            if (currentNumber < initialNumber) {
                currentNumber = initialNumber
            }
            textView!!.text = currentNumber.toString()
        }

    fun setNumber(number: String, notifyListener: Boolean) {
        this.number = number
        if (notifyListener) {
            callListener(this)
        }
    }

    fun setOnClickListener(onClickListener: OnClickListener?) {
        mListener = onClickListener!!
    }

    fun setOnValueChangeListener(onValueChangeListener: OnValueChangeListener?) {
        mOnValueChangeListener = onValueChangeListener!!
    }

    @FunctionalInterface
    interface OnClickListener {
        fun onClick(view: View?)
    }

    interface OnValueChangeListener {
        fun onValueChange(
            view: MyInCreaseView?,
            oldValue: Int,
            newValue: Int
        )
    }

    fun setRange(startingNumber: Int, endingNumber: Int) {
        initialNumber = startingNumber
        finalNumber = endingNumber
    }

    fun updateColors(backgroundColor: Int, textColor: Int) {
        textView!!.setBackgroundColor(backgroundColor)
        addBtn?.setBackgroundColor(backgroundColor)
        subtractBtn?.setBackgroundColor(backgroundColor)
        textView!!.setTextColor(textColor)
        addBtn?.setTextColor(textColor)
        subtractBtn?.setTextColor(textColor)
    }

    fun updateTextSize(unit: Int, newSize: Float) {
        textView!!.setTextSize(unit, newSize)
        addBtn?.setTextSize(unit, newSize)
        subtractBtn?.setTextSize(unit, newSize)
    }
}