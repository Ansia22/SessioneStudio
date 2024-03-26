package com.example.esempio

import android.content.ContentValues.TAG
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.os.Handler
import android.content.Intent
import android.nfc.Tag
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val DELAY_TIME = 3000 // Imposta il ritardo desiderato in millisecondi
        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(this@MainActivity, MainActivity2::class.java)
            startActivity(intent)
        }, DELAY_TIME.toLong()) //  Converte il ritardo in un tipo Long
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG,"distrutto")
    }
}
