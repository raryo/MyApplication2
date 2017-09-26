package com.esakadaiki.myapplication

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView


// SurfaceView
class CustomSurfaceView(context: Context) : SurfaceView(context), SurfaceHolder.Callback, Runnable {
    var thread: Thread? = null
    var isAttached: Boolean = false

    private var dx = 5f
    private var dy = 5f
    private var screenWidth: Float = 0.toFloat()
    private var screenHeight: Float = 0.toFloat()
    private var xOffset = 0f
    private var yOffset = 0f

    var res = this.context.resources
    var curry_dish = BitmapFactory.decodeResource(res, R.drawable.curry_dish2)
    var curryR: Int = 0
    var curryG: Int = 0
    var curryB: Int = 0
    var curry_color = 0


    init {

        holder.addCallback(this)
    }

    // Bitmapの拡大率を計算する
    private fun calcBitmapScale(canvasWidth: Int, canvasHeight: Int, bmpWidth: Int, bmpHeight: Int): Float {
        var scale: Float = canvasWidth.toFloat() / bmpWidth.toFloat()
        var tmp = bmpHeight * scale
/*
if(tmp < canvasHeight){
scale = canvasHeight.toFloat() / bmpHeight.toFloat()
return scale
}
*/
        return scale
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int,
                                height: Int) {
        screenWidth = width.toFloat()
        screenHeight = height.toFloat()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        isAttached = true
        thread = Thread(this)
        thread?.start()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        isAttached = false
        while (thread!!.isAlive);
    }

    override fun run() {
        while (isAttached) {
            Log.v("SurfaceViewSample3", "run, color=${curry_color}")


            if (xOffset < 0 || xOffset + rectWidth > screenWidth)
                dx = -dx
            if (yOffset < 0 || yOffset + rectHeight > screenHeight)
                dy = -dy
            xOffset += dx
            yOffset += dy

            doDraw(holder)
        }
    }

    private fun doDraw(holder: SurfaceHolder) {
        val canvas = holder.lockCanvas()

// この間にグラフィック描画のコードを記述する。

// 画像のサイズを画面に合わせる
        var canvasWidth = canvas.getWidth()
        var canvasHeight = canvas.getHeight()

        val bmpWidth = curry_dish.getWidth()
        val bmpHeight = curry_dish.getHeight()

        val toCanvasScale = calcBitmapScale(canvasWidth, canvasHeight, bmpWidth, bmpHeight)

        val diffX: Float = (bmpWidth * toCanvasScale - canvasWidth)
        val diffY: Float = (bmpHeight * toCanvasScale - canvasHeight)

        val addX: Float = (diffX / toCanvasScale) / 2
        val addY: Float = (diffY / toCanvasScale) / 2
// 画像のサイズ合わせここまで

        val paint = Paint()
        paint.color = Color.rgb(150, 150, curry_color)

        canvas.drawColor(Color.WHITE)
        canvas.drawRect(xOffset, yOffset, xOffset + rectWidth, yOffset + rectHeight, paint)

        val rSrc = Rect(addX.toInt(), addY.toInt(), ((canvasWidth / toCanvasScale) + addX).toInt(), ((canvasHeight / toCanvasScale) + addY).toInt())
        val rDest = RectF(0f, 0f, canvasWidth.toFloat(), canvasHeight.toFloat())



        paint.color = Color.rgb(150, 150, curry_color)
        paint.setStyle(Paint.Style.FILL)
        var rectf = RectF(((canvasWidth / 2) - bmpWidth * toCanvasScale / 3).toFloat(), ((canvasHeight / 2) - bmpHeight * toCanvasScale / 3).toFloat(), ((canvasWidth / 2) + bmpWidth * toCanvasScale / 3).toFloat(), ((canvasHeight / 2) + bmpHeight * toCanvasScale / 3).toFloat())
        canvas.drawOval(rectf, paint)

        canvas.drawBitmap(curry_dish, rSrc, rDest, paint)

// この間にグラフィック描画のコードを記述する。

        holder.unlockCanvasAndPost(canvas)
    }


    fun add_spice () {
        curry_color++
    }

    companion object {


        private val rectWidth = 50f
        private val rectHeight = 50f
    }


}
