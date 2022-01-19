package com.example.randomiser

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rollButton=findViewById<Button>(R.id.rollButton)
        val result=findViewById<TextView>(R.id.Result)
        val seekbar=findViewById<SeekBar>(R.id.seekBar)


        rollButton.setOnClickListener {
            val rand= Random.nextInt(seekbar.progress+1)
            result.text=rand.toString()
        }

    }
}