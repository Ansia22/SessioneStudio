package com.example.esempio

import android.content.ContentValues
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.content.Intent
import android.util.Log

/**
 * Classe per la gestione del caricamento iniziale.
 * L'icona di caricamento è puramente "estetica", la durata è di 3 secondi.
 * L'activity viene distrutta alla fine del caricamento
 */
class Caricamento : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val delayTime = 3000
        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(this@Caricamento, Main::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }, delayTime.toLong()) //  Converte il ritardo in un tipo Long
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(ContentValues.TAG,"distrutto")
    }
}
