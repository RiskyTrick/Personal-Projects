package com.example.flip

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.rotationMatrix
import kotlin.math.round
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val flipButton = findViewById<Button>(R.id.flipButton)
        val flip = findViewById<TextView>(R.id.flip)
        val imageView= findViewById<ImageView>(R.id.img)
        val count = findViewById<TextView>(R.id.count)
        var myInt: Int = 0
        val animationRotate = AnimationUtils.loadAnimation(this, R.anim.rotate)





        flipButton.setOnClickListener {
            myInt++
            count.text = myInt.toString()
            val rand = Random.nextInt(9)
            if (rand % 2 == 0) {

                imageView.startAnimation(animationRotate)
                flip.text = "Heads"
                 imageView.setImageResource(R.drawable.heads)



            } else {
                imageView.startAnimation(animationRotate)
                flip.text = "Tails"
                imageView.setImageResource(R.drawable.tails)

            }
        }

    }
}