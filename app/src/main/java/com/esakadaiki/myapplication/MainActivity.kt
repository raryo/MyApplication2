package com.esakadaiki.myapplication

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import java.util.Calendar
import android.view.GestureDetector.SimpleOnGestureListener
import android.os.Handler
import android.view.*
import android.graphics.PorterDuff
import android.view.SurfaceHolder
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.FILL_PARENT
import android.widget.LinearLayout.LayoutParams;
import android.widget.LinearLayout
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.RectF
import android.app.Application












class MainActivity : AppCompatActivity() {

    var touch_count = 0
    // Y軸最低スワイプ距離
    private val SWIPE_MIN_DISTANCE = 50
    // Y軸最低スワイプスピード
    private val SWIPE_THRESHOLD_VELOCITY = 200
    // X軸の移動距離 これ以上なら縦移動を判定しない
    private val SWIPE_MAX_OFF_PATH = 200
    private var g_View: View? = null

    // タッチイベントを処理するためのインタフェース
    private var mGestureDetector: GestureDetector? = null

    val handler = Handler()
    var timeValue = 0


    var mSurfaceView: CustomSurfaceView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("check", "onCreate")

        // View
        mSurfaceView = CustomSurfaceView(this)
        setContentView(mSurfaceView)

        Log.v("check", "setContentView")

        val view = this.layoutInflater.inflate(R.layout.activity_main, null)
        g_View = view
        addContentView(view, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))


        mGestureDetector = GestureDetector(this, mOnGestureListener)

        Log.v("check", "GestureDetector")



        // SurfaceViewの用意
        /*
        val view = layoutInflater.inflate(R.layout.activity_main, null)
        addContentView(view, LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT))
                */

        val touchCount = findViewById(R.id.touchCount) as TextView
        val timeText = findViewById(R.id.timeText) as TextView
        val startButton = findViewById(R.id.startButton) as Button
        val touchResult = findViewById(R.id.touchResult) as TextView

        // タッチ回数のカウントに関するテキスト表示
        val runnableCountText = object : Runnable {
            override fun run() {
                touchCount.text = touch_count.toString()
                startButton.text = "RESTART"
                handler.postDelayed(this, 1)
            }
        }

        // 時間計測
        val runnableTime = object : Runnable {
                override fun run() {
                timeValue++
                timeToText(timeValue)?.let {
                    timeText.text = it
                }
                handler.postDelayed(this, 1)
                Log.v("check", "runnableTime")
                if(timeValue >= 15*60){
                    handler.removeCallbacks(this)
                    handler.removeCallbacks(runnableCountText)
                    touchResult.text = touch_count.toString()
                    touchCount.text = ""
                }
            }
        }

        // startボタン
        startButton.setOnClickListener(){
            handler.removeCallbacks(runnableTime)
            handler.removeCallbacks(runnableCountText)
            touchResult.text = ""

            handler.post(runnableTime)
            touch_count = 0
            timeValue = 0
            handler.post(runnableCountText)
        }

    }

    /*
    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d("TouchEvent", "X:" + event.x + ",Y:" + event.y)
        return true
    }
    */

    // タッチイベント
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return mGestureDetector!!.onTouchEvent(event)
    }

    // タッチイベントのリスナー
    private val mOnGestureListener = object : GestureDetector.SimpleOnGestureListener() {

        /*
        // フリックイベント
        override fun onFling(event1: MotionEvent, event2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            try {
                // 移動距離・スピードを出力
                val distance_y = Math.abs(event1.y - event2.y)
                val velocity_y = Math.abs(velocityY)
                Log.d("onFling", "縦の移動距離:$distance_y 縦の移動スピード:$velocity_y" )

                // X軸の移動距離が大きすぎる場合
                if (Math.abs(event1.x - event2.x) > SWIPE_MAX_OFF_PATH) {
                    Log.d("onFling","横の移動距離が大きすぎます")

                    // 開始位置から終了位置の移動距離が指定値より大きい
                    // Y軸の移動速度が指定値より大きい
                } else if (event2.y - event1.y > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    Log.d("onFling","上から下")

                    // 終了位置から開始位置の移動距離が指定値より大きい
                    // Y軸の移動速度が指定値より大きい
                } else if (event1.y - event2.y > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    Log.d("onFling","下から上")
                }

            } catch (e: Exception) {
                // TODO
            }

            return false
        }
        // 長押し
        override fun onLongPress(e: MotionEvent?) {
            Log.v("check","longtap")
            super.onLongPress(e)
        }

        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            Log.v("check", "singletap")
            return super.onSingleTapConfirmed(e)
        }
        */

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            Log.v("check", "singletapup")
            touch_count++
            mSurfaceView?.curry_color?.plus(20)
            Log.v("check", "${touch_count}")
            return super.onSingleTapUp(e)
        }
    }

    private fun timeToText(time: Int = 0): String? {
        return if(time<0) {
            null
        } else if(time == 0) {
            "00:00:00"
        } else {
            var remainingTime = 15*60 - time
            val h = remainingTime / 3600
            val m = remainingTime % 3600 / 60
            val s = remainingTime % 60
            "%1$02d:%2$02d:%3$02d".format(h, m ,s)
        }
    }
    
    fun getCount(): Int {
        return touch_count
    }

}

