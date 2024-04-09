package com.example.esempio

import android.content.ContentValues
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.content.Intent
import android.util.Log

class Caricamento : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val DELAY_TIME = 3000
        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(this@Caricamento, Main::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }, DELAY_TIME.toLong()) //  Converte il ritardo in un tipo Long
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(ContentValues.TAG,"distrutto")
    }
}
