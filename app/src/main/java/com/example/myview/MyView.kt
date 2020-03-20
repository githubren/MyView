package com.example.myview

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View

class MyView @JvmOverloads constructor(context: Context,attrs: AttributeSet? = null,defStyleAttr: Int = 0) : View(context,attrs,defStyleAttr) {

    //数据源
    var dataList: MutableList<Int> = mutableListOf()
    var hintList: MutableList<String> = mutableListOf()
    //颜色
    private var pureColors: MutableList<String> = mutableListOf()

    var heights: Int = 0
    //大圆圆环宽度
    val strokeWidth: Float =26f
    //大圆上下总间距
    val DISTANCE:Float =40f
    //用于计算半分比
    var total: Int = 0
    //大圆画笔
    lateinit var bigCirclePaint: Paint
    lateinit var processCirclePaint:Paint
    lateinit var halfProcessCirclePaint:Paint
    lateinit var smallCirclePaint:Paint
    lateinit var txtPaint: TextPaint
    lateinit var totalPaint: TextPaint

    init {
        //初始化数据
        initData()
        //初始化画笔
        initPaint()
    }

    private fun initPaint() {
        //底圆画笔
        bigCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        bigCirclePaint.style = Paint.Style.STROKE
        bigCirclePaint.strokeWidth = strokeWidth
        bigCirclePaint.color = Color.parseColor("#D81B60")
        //        bigCirclePaint.setColor(Color.parseColor("#e9ecee"))
        //圆弧画笔
        processCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        processCirclePaint.style = Paint.Style.STROKE
        processCirclePaint.strokeWidth = 70f
        processCirclePaint.color = Color.parseColor("#D81B60")
        //右边文字画笔
        txtPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        txtPaint.textAlign = Paint.Align.LEFT
        txtPaint.color = Color.parseColor("#333333")
        txtPaint.textSize = 30f
        //中间大字画笔
        totalPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        totalPaint.textAlign = Paint.Align.CENTER
        totalPaint.textSize = 40f

        halfProcessCirclePaint= Paint(Paint.ANTI_ALIAS_FLAG)
        smallCirclePaint= Paint(Paint.ANTI_ALIAS_FLAG)
    }

    private fun initData() {
        dataList.add(1251600)
        dataList.add(500000)
        dataList.add(600000)
        dataList.add(130200)
        dataList.add(500000)

        hintList.add("网店售价")
        hintList.add("消保金")
        hintList.add("技术年费")
        hintList.add("居间费")
        hintList.add("品牌授权押金")

        pureColors.add("#85b4ff")
        pureColors.add("#4ef498")
        pureColors.add("#3e34ff")
        pureColors.add("#fed74f")
        pureColors.add("#ff4091")

        for (item in dataList){
            total+=item
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //绘制右边文字部分
        drawRightInfo(canvas)
        //绘制总费用
        drawTotal(canvas)
        //根据比例绘制圆弧
        drawArc(canvas)
    }

    private fun drawArc(canvas: Canvas?) {
        var rectF = RectF(90f, 90f, 400f, 400f)
        var recordAngle = 0f
        dataList.forEachIndexed { index, i ->
            var percent: Float
            if (index == dataList.size - 1) {
                percent = 360 - recordAngle
                if (total == 0)
                    percent = 0f
            } else {
                percent = i * 1f / total * 360
            }
            processCirclePaint.color = Color.parseColor(pureColors.get(index))
            canvas?.drawArc(rectF, recordAngle, percent, false, processCirclePaint)
            recordAngle += percent
        }
    }

    private fun drawTotal(canvas: Canvas?) {
        totalPaint.typeface = Typeface.DEFAULT_BOLD
        canvas?.drawText("￥" + total.toString(), 240F, 240F, totalPaint)
        totalPaint.typeface = Typeface.DEFAULT
        canvas?.drawText("总费用", 240f, 290f, totalPaint)
    }

    private fun drawRightInfo(canvas: Canvas?) {
        //每行文字之间的间距
        var littleDis: Float = (heights - dip2px(DISTANCE)) * 1f / dataList.size
        //文字初始高度  行间距分上下两个部分  整个文字区域相对圆环还有个间距 也是分上下两部分
        var startY: Float = littleDis / 2 + dip2px(DISTANCE) / 2
        //文字起始x坐标
        var cx: Float = 250 + 100 + strokeWidth + 150
        //最大文字宽度
        var txtWidth = txtPaint.measureText("12516000")
        dataList.forEachIndexed { index, i ->
            //index 索引  i是value
            var newY = startY + index * littleDis
            smallCirclePaint.color = Color.parseColor(pureColors.get(index))
            canvas?.drawCircle(cx, newY, 20f, smallCirclePaint)
            //画笔从左开始
            txtPaint.textAlign = Paint.Align.LEFT
            canvas?.drawText(
                "￥" + i,
                cx + 20 + dip2px(5f),
                newY - ((txtPaint.descent() + txtPaint.ascent()) / 2f),
                txtPaint
            )
            //画笔从右开始
            txtPaint.textAlign = Paint.Align.RIGHT
            canvas?.drawText(
                hintList[index],
                cx + 20 + dip2px(5f) + txtWidth + dip2px(100f),
                newY - ((txtPaint.descent() + txtPaint.ascent()) / 2f),
                txtPaint
            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        Log.e("TAG", h.toString())
        heights = h
    }

    fun dip2px(dpValue : Float) : Int{
        var scale: Float = context.resources.displayMetrics.density
        return (dpValue*scale+0.5f).toInt()
    }
}