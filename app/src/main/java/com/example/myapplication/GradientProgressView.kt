package com.example.myapplication

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

/**

    <com.example.myapplication.GradientProgressView
    android:id="@+id/my_progress"
    android:layout_width="260dp"
    android:layout_height="260dp"
    android:layout_centerInParent="true"
    android:layout_centerHorizontal="true"
    tools:mbsb_colors="#5A87F6_#BE59A2_#FF5D88" />



    var my_progress: GradientProgressView = findViewById<GradientProgressView>(R.id.my_progress)
    my_progress.maxCount = 100
    my_progress.setProgress(66f)
*/



class GradientProgressView constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    var maxCount = 0f
    private var currentCount = 0f
    var score = 0
        set(score) {
            field = score
            invalidate()
        }
    var crrentLevel: String? = null
    private var mPaint: Paint? = null
    private var mCirclePaint: Paint? = null
    private var mTextPaint: Paint? = null
    private var mWidth = 0
    private var mHeight = 0
    private val DEFAULT_ARC_COLOR = Color.parseColor("#E9F3FF")
    private val mArcColor = DEFAULT_ARC_COLOR
    private fun init(context: Context) {
        mPaint = Paint()
        mTextPaint = Paint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        initPaint()
        mCirclePaint!!.style = Paint.Style.STROKE
        val rectBlackBg = RectF(30f, 30f, (mWidth - 30).toFloat(), (mHeight - 30).toFloat())
        canvas.drawArc(rectBlackBg, -180f, -180f, false, mCirclePaint!!)
        mPaint!!.color = Color.parseColor("#FD5D89")
        val section = currentCount / maxCount
        if (section <= 1.0f / 3.0f) {
            if (section != 0.0f) {
                mPaint!!.color = SECTION_COLORS[0]
            } else {
                mPaint!!.color = Color.TRANSPARENT
            }
        } else {
            val count = if (section <= 1.0f / 3.0f * 2) 2 else 3
            val colors = IntArray(count)
            System.arraycopy(SECTION_COLORS, 0, colors, 0, count)
            val positions = FloatArray(count)
            if (count == 2) {
                positions[0] = 0.0f
                positions[1] = 1.0f - positions[0]
            } else {
                positions[0] = 0.0f
                positions[1] = maxCount / 3 / currentCount
                positions[2] = 1.0f - positions[0] * 2
            }
            positions[positions.size - 1] = 1.0f
            val linearGradient = LinearGradient(0f, 0f, mWidth.toFloat(), mHeight.toFloat(), colors, null, Shader.TileMode.MIRROR)
            mPaint!!.shader = linearGradient
        }
        canvas.drawArc(rectBlackBg, -180f, -section * 180, false, mPaint!!)
    }

    private fun initPaint() {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        mCirclePaint = Paint()
        mCirclePaint!!.color = mArcColor
        mCirclePaint!!.strokeWidth = 30.0f
        mCirclePaint!!.isDither = true
        mCirclePaint!!.isAntiAlias = true
        mCirclePaint!!.style = Paint.Style.STROKE
        mCirclePaint!!.strokeCap = Paint.Cap.ROUND
        mPaint!!.isAntiAlias = true
        mPaint!!.strokeWidth = 30.0.toFloat()
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.strokeCap = Paint.Cap.ROUND
        mPaint!!.color = Color.TRANSPARENT
        mTextPaint!!.isAntiAlias = true
        mTextPaint!!.strokeWidth = 3.0.toFloat()
        mTextPaint!!.textAlign = Paint.Align.CENTER
        mTextPaint!!.textSize = 50f
        mTextPaint!!.color = Color.parseColor("#FD5D89")
    }

    private fun dipToPx(dip: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dip * scale + 0.5f * if (dip >= 0) 1 else -1).toInt()
    }

    fun getCurrentCount(): Float {
        return currentCount
    }

    fun setProgress(newValue: Float) {
        val waveShiftAnim = ObjectAnimator.ofFloat(this, "CurrentCount", currentCount, newValue)
        waveShiftAnim.repeatCount = 0
        waveShiftAnim.duration = 500
        waveShiftAnim.interpolator = LinearInterpolator()
        waveShiftAnim.start()
    }

    fun setCurrentCount(currentCount: Float) {
        this.currentCount = if (currentCount > maxCount) maxCount else currentCount
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        mWidth = if (widthSpecMode == MeasureSpec.EXACTLY
                || widthSpecMode == MeasureSpec.AT_MOST) {
            widthSpecSize
        } else {
            0
        }
        mHeight = if (heightSpecMode == MeasureSpec.AT_MOST
                || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            dipToPx(15)
        } else {
            heightSpecSize
        }
        setMeasuredDimension(mWidth, mHeight)
    }

    companion object {
        private val SECTION_COLORS = intArrayOf(Color.parseColor("#5A87F6"), Color.parseColor("#BE59A2"),
                Color.parseColor("#FF5D88"))
    }

    init {
        init(context)
    }
}