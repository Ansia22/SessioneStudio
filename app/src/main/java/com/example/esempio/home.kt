package com.example.esempio

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.esempio.models.Professor
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class home : AppCompatActivity() {

    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val prova = findViewById<TextView>(R.id.prova)
        leggidb(prova)
    }


    fun leggidb(prova: TextView){

        val myRef = database.reference.child("Professori")

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Toast.makeText(applicationContext, "dentro prima prof", Toast.LENGTH_SHORT).show()
                val value = dataSnapshot.getValue(String::class.java)
                Toast.makeText(applicationContext, "dentro dopo prof", Toast.LENGTH_SHORT).show()
                val idprof = value
                prova.text = "ecco $idprof"
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })


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

    fun passaFeedback (view: View){
        val intent = Intent(this, FeedbackPage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
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
