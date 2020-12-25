package com.example.stopwatchactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.view.View
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity() {

    private var seconds = 0
    private var running =  false
    private var wasRunning = false

    private var timeTextView : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        timeTextView = findViewById(R.id.text_time)

        if(savedInstanceState != null){
            seconds = savedInstanceState.getInt("seconds")
            running = savedInstanceState.getBoolean("running")
            wasRunning = savedInstanceState.getBoolean("wasRunning")
        }

        runTimer()
    }

    private fun runTimer() {
        val handler = Handler()

        val stopWatchUpdateRunnable : Runnable = object : Runnable{
            override fun run(){
                val hours = seconds / 3600
                val minutes = seconds % 3600/60
                val secs = seconds % 60
                val time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs)
                timeTextView!!.text = time
                if(running){
                    seconds++
                }

                handler.postDelayed(this,1000)
            }
        }
        handler.post(stopWatchUpdateRunnable)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("seconds",seconds)
        outState.putBoolean("running", running)
        outState.putBoolean("wasRunning", wasRunning)
    }

    fun onClickStart(view: View){
        running =  true
    }
    fun onClickStop(view: View){
        running = false
    }
    fun onClickReset(view: View){
        running = false
        seconds = 0
    }

    override fun onPause() {
        super.onPause()
        wasRunning = running
        running = false
    }

    override fun onResume() {
        super.onResume()
        if(wasRunning){
            running = true
        }
    }
    fun onClickShare(view: View){
        val intentOpenApp = Intent(Intent.ACTION_SEND)
        val textToShare = "My time is " + seconds.toString() + "seconds"
        intent.putExtra("time", textToShare)
        intentOpenApp.type = "text/plain"

        startActivity(intentOpenApp)
    }
}