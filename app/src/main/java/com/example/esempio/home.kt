package com.example.esempio

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setMessage("Uscire da Sessione Studio?")
            .setCancelable(true)
            .setPositiveButton("Ok") { _, _ ->
                super.onBackPressed() // Chiusura solo dopo conferma
            }
            .setNegativeButton("Annulla", null)
            .show()
    }
}
