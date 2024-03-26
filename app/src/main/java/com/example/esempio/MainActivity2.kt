package com.example.esempio

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        findViewById<View>(R.id.fullScreenView).setOnClickListener {
            val intent = Intent(this, home::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }


        val flashingIcon = findViewById<TextView>(R.id.flashingIcon)
        // Carica l'animazione di blinking
        val blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink_animation)
        // Applica l'animazione alla TextView
        flashingIcon.startAnimation(blinkAnimation)

        val textView = findViewById<TextView>(R.id.titoloApp)
        val animation = AnimationUtils.loadAnimation(this, R.anim.title)
        textView.startAnimation(animation)

    }
    override fun onDestroy() {
        super.onDestroy()
        Log.v(ContentValues.TAG,"distrutto")
    }
}