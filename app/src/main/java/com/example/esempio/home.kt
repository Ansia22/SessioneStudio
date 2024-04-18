package com.example.esempio

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.esempio.models.Professor
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Home : AppCompatActivity() {

    private lateinit var firebaseRef : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        firebaseRef = FirebaseDatabase.getInstance().getReference("Professori")
        fetchData()
    }

    private fun fetchData(){
        firebaseRef.addValueEventListener(object:ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                }
                override fun onCancelled(databaseError: DatabaseError) {
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
            .setMessage("Benvenuti in SessioneStudio! Una applicazione sviluppata per studenti e professori. \n\n" +
                    " Grazie a SessioneStudio potrai cercare il professore più adatto alle tue esigenze e migliorare " +
                    "i risultati dei tuoi test. Scuole medie? Superiori? Università? Cosa aspetti! " +
                    "Premi il tasto 'Cerca' e inizia a studiare! \n\n Se invece sei un professore " +
                    "disposto a trasmettere i tuoi insegnamenti ai nuovi studenti, nessun problema! registrati, " +
                    "inserisci i tuoi dati e potrai iniziare!")
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
