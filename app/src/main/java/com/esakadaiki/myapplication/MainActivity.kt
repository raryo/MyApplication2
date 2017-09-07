package com.esakadaiki.myapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.view.View
import android.content.Intent
import android.util.Log
import android.view.MotionEvent
import java.util.Calendar
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.GestureDetector
import android.os.Handler





class MainActivity : AppCompatActivity() {

    // Y軸最低スワイプ距離
    private val SWIPE_MIN_DISTANCE = 50
    // Y軸最低スワイプスピード
    private val SWIPE_THRESHOLD_VELOCITY = 200
    // X軸の移動距離 これ以上なら縦移動を判定しない
    private val SWIPE_MAX_OFF_PATH = 200

    // タッチイベントを処理するためのインタフェース
    private var mGestureDetector: GestureDetector? = null

    var touch_count = 0
    val handler = Handler()
    var timeValue = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mGestureDetector = GestureDetector(this, mOnGestureListener)

        val touchCount = findViewById(R.id.touchCount) as TextView
        val timeText = findViewById(R.id.timeText) as TextView
        val startButton = findViewById(R.id.startButton) as Button
        val touchResult = findViewById(R.id.touchResult) as TextView

        // タッチ回数のカウントに関するテキスト表示
        val runnableCountText = object : Runnable {
            override fun run() {
                touchCount.text = touch_count.toString()
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
            handler.post(runnableTime)
            touch_count = 0
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

}
