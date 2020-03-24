package com.example.myview

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View

class ChartView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context,attrs,defStyleAttr) {

    //数据源
    private var dataList: MutableList<Double> = mutableListOf()
    private var hintList: MutableList<String> = mutableListOf()
    //颜色
    private var pureColors: MutableList<String> = mutableListOf()

    var heights: Int = 0
    var widths: Int = 0
    //圆环在容器中到左边界的距离和到右边界的距离一样
    val ringOffset:Float = 90f
    //圆环半径
    var ringRadius:Float = 160f
    //右边小圆圈半径
    val smallCircleRadius = 20f
    //大圆圆环宽度
    val strokeWidth: Float =70f
    //大圆上下总间距
    val DISTANCE:Float =40f
    //用于计算半分比
    var total: Double = 0.0
    //总费用金额文字大小
    var totalTextSize = 40f

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
        //圆弧画笔
        processCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        processCirclePaint.style = Paint.Style.STROKE
        processCirclePaint.strokeWidth = strokeWidth
        processCirclePaint.color = Color.parseColor("#D81B60")
        //右边文字画笔
        txtPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        txtPaint.textAlign = Paint.Align.LEFT
        txtPaint.color = Color.parseColor("#333333")
        txtPaint.textSize = 30f
        //中间大字画笔
        totalPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        totalPaint.textAlign = Paint.Align.CENTER
        totalPaint.textSize = totalTextSize

        halfProcessCirclePaint= Paint(Paint.ANTI_ALIAS_FLAG)
        smallCirclePaint= Paint(Paint.ANTI_ALIAS_FLAG)
    }

    private fun initData() {
        //颜色集  暂定五种  可以根据业务需求加
        pureColors.add("#85b4ff")
        pureColors.add("#4ef498")
        pureColors.add("#3e34ff")
        pureColors.add("#fed74f")
        pureColors.add("#ff4091")
        pureColors.add("#ff9081")

    }

    fun setDataList(dataList:MutableList<Double>){
        this.dataList = dataList

        /*  最开始求和这一步放在init里面的  init是在加载布局后就调用了  此时由于activity中数据还未加载完成 所以total会为0
            所以在设置数据源后进行一个数据的处理  放在这来
        * */
        for (item in dataList){
            total+=item
        }
    }

    fun setHintList(hintList:MutableList<String>){
        this.hintList = hintList
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //绘制右边文字部分
        drawRightInfo(canvas)
        //绘制总费用
        drawTotal(canvas)
        //根据比例绘制圆弧
        drawArc(canvas)

//        canvas?.drawCircle(ringOffset,heights/2*1f,35f,halfProcessCirclePaint)
    }

    private fun drawArc(canvas: Canvas?) {
        //圆弧所切的矩形区域  y方向在容器的中心
        val rectF = RectF(ringOffset, heights/2-ringRadius, ringOffset+ringRadius*2, heights/2+ringRadius)
        var recordAngle = 0f
        dataList.forEachIndexed { index, i ->
            var percent: Float
            if (index == dataList.size - 1) {
                percent = 360 - recordAngle
                if (total == 0.0)
                    percent = 0f
            } else {
                percent = (i * 1f / total * 360).toFloat()
            }
            processCirclePaint.color = Color.parseColor(pureColors.get(index))
            canvas?.drawArc(rectF, recordAngle, percent, false, processCirclePaint)
            recordAngle += percent
        }
    }

    private fun drawTotal(canvas: Canvas?) {
        totalPaint.typeface = Typeface.DEFAULT_BOLD
        //超出时改变字体大小
        changeTotalTxtSize()
        canvas?.drawText("￥" + total.toString(), ringOffset+ringRadius, heights/2*1f, totalPaint)
        totalPaint.typeface = Typeface.DEFAULT
        canvas?.drawText("总费用", ringOffset+ringRadius, heights/2*1f+dip2px(16f), totalPaint)
    }

    /**
     * 总费用文字宽度超出圆环时 改变文字大小
     */
    private fun changeTotalTxtSize() {
        val totalTxtWidth: Float = totalPaint.measureText("￥" + total.toString())
        if (totalTxtWidth >= ringRadius * 2 - strokeWidth) {
            totalPaint.textSize = totalPaint.textSize - 4
            val totalTxtWidth1: Float = totalPaint.measureText("￥" + total.toString())
            if (totalTxtWidth1 >= ringRadius * 2 - strokeWidth) {
                totalPaint.textSize = totalPaint.textSize - 4
            }
        }
    }

    private fun drawRightInfo(canvas: Canvas?) {
        //每行文字之间的间距
        val littleDis: Float = (heights - dip2px(DISTANCE)) * 1f / dataList.size
        //文字初始高度  行间距分上下两个部分  整个文字区域相对圆环还有个间距 也是分上下两部分
        val startY: Float = littleDis / 2 + dip2px(DISTANCE) / 2
        //右边起始x坐标  距离圆环40dp
        val cx: Float = ringOffset+ringRadius*2+dip2px(40f)
        dataList.forEachIndexed { index, i ->
            //index 索引  i是value
            val newY = startY + index * littleDis
            smallCirclePaint.color = Color.parseColor(pureColors[index])
            canvas?.drawCircle(cx, newY, smallCircleRadius, smallCirclePaint)
            //画笔从左开始  画金额
            txtPaint.textAlign = Paint.Align.LEFT
            canvas?.drawText(
                "￥" + i,
                cx + dip2px(15f),
                newY - ((txtPaint.descent() + txtPaint.ascent()) / 2f),
                txtPaint
            )
            //画笔从右开始  画文字
            txtPaint.textAlign = Paint.Align.RIGHT
            canvas?.drawText(
                hintList[index],
                cx + dip2px(155f),
                newY - ((txtPaint.descent() + txtPaint.ascent()) / 2f),
                txtPaint
            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        heights = h
        widths = w
        //圆环半径设置为固定值  宽度的40%  超出时改字体大小
        ringRadius = widths*0.4f/2 - dip2px(10f)
    }

    fun dip2px(dpValue : Float) : Int{
        val scale: Float = context.resources.displayMetrics.density
        return (dpValue*scale+0.5f).toInt()
    }
}