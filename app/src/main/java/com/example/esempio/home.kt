package com.example.esempio

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    fun passaRicerca(view: View){

        val intent = Intent(this, RicercaPage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

    }
    fun passaLogin (view: View){
        val intent = Intent(this, LoginPage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun passaRegistrazione (view: View){
        val intent = Intent(this, RegistrazionePage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }


    fun infoAlert(view: View){
        AlertDialog.Builder(this)
            .setMessage("ciao a tutti!")
            .setCancelable(true)
            .setPositiveButton("ok", null)
            .show()
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
