package com.example.myview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View

class ProgressView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context,attrs,defStyleAttr){

    //大圆画笔
    lateinit var bigCirclePaint:Paint
    //底圆画笔
    lateinit var smallCirclePaint:Paint
    //文字画笔
    lateinit var txtPaint: TextPaint
    //中间起填充作用的画笔
    lateinit var fillCirclePaint: Paint
    //最外层圆弧画笔
    lateinit var arcPaint: Paint
    //当前进度
    var current: Float = 0f

    init {
        //初始化画笔
        initPaint()
    }

    private fun initPaint() {
        //大圆画笔 紫色
        bigCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        bigCirclePaint.style = Paint.Style.STROKE
        bigCirclePaint.strokeWidth = 108f
        bigCirclePaint.color = Color.parseColor("#FF6118E6")
        //底圆画笔 灰色
        smallCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        smallCirclePaint.style = Paint.Style.STROKE
        smallCirclePaint.strokeWidth = 36f
        smallCirclePaint.color = Color.parseColor("#B9B8BB")
        //文字画笔 紫色
        txtPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        txtPaint.textSize = 150f
        txtPaint.color = Color.parseColor("#FF6118E6")
        txtPaint.textAlign = Paint.Align.CENTER
        txtPaint.typeface = Typeface.DEFAULT_BOLD
        //最外层圆弧画笔 白色
        fillCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        fillCirclePaint.color = Color.parseColor("#ffffff")
        //最外层圆弧画笔 取值不同 颜色不同
        arcPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        arcPaint.style = Paint.Style.STROKE
        arcPaint.strokeWidth = 36f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //绘制灰色底圆
        canvas?.drawCircle(520f,500f,260f,smallCirclePaint)//350
        //绘制最外层圆弧
        /*
            将一个圆分成150份  遍历150个数 偶数时画笔颜色设置为白色 奇数时画笔颜色设置成灰色（进度没走的时候）
            进度开始走之后 将当前进度（360份）和150份做个转换  在进度内重新绘制 偶数为白色 奇数为紫色
         */
        drawArcCircle(canvas)
        //绘制被选中紫色圆弧
        var rectF = RectF(260f,240f,780f,760f)
        canvas?.drawArc(rectF,-90f,current,false,bigCirclePaint)
        //绘制中间起填充作用的白色圆
        canvas?.drawCircle(520f,500f,242f,fillCirclePaint)
        //绘制文字
        canvas?.drawText((current/360*100).toInt().toString()+"%",520f,500f-(txtPaint.ascent()+txtPaint.descent())/2,txtPaint)
    }

    private fun drawArcCircle(canvas: Canvas?) {
        var rectF0 = RectF(170f, 150f, 870f, 850f)
        for (index in 1..150) {
            if (index % 2 == 0)//偶数为白色 奇数为灰色
                arcPaint.color = Color.parseColor("#ffffff")
            else
                arcPaint.color = Color.parseColor("#B9B8BB")
            canvas?.drawArc(rectF0, -90f + index * 2.4f, 2.4f, false, arcPaint)
        }

        var count: Int = (current / 360 * 150).toInt()
        for (index in 1..count) {
            if (index % 2 == 0)//偶数为白色 奇数为紫色
                arcPaint.color = Color.parseColor("#ffffff")
            else
                arcPaint.color = Color.parseColor("#FF6118E6")
            canvas?.drawArc(rectF0, -90f + index * 2.4f, 2.4f, false, arcPaint)
        }
    }

    /**
     * 动画
     */
    fun startAnim() {
        current = 0f
        val valueAnimator = ValueAnimator.ofFloat(0f, 360f)
        valueAnimator.addUpdateListener {
            current = it.getAnimatedValue() as Float
            invalidate()
        }
        valueAnimator.duration = 5000
        valueAnimator.start()
    }
}