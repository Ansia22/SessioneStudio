package com.example.esempio

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.esempio.models.Professor
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

/**
 * HomePage del profilo del professore.
 *
 * Effettuando il login verrà visualizzata questa schermata che
 * permette di passare all'activity relativa alla gestione delle
 * informazioni personali dell'account oppure di leggere
 * i feedback personali del professore.
 */

class ProfiloPage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profilo_page)

        auth = Firebase.auth

        val textId = findViewById<TextView>(R.id.textIdUtente)
        val idProf = Professor.getIdProf()
        textId.text= "Il tuo id:  $idProf"
    }

    fun tornaLogin(view: View){
        AlertDialog.Builder(this)
            .setMessage("Effettuare il logout?")
            .setCancelable(true)
            .setPositiveButton("Ok") { _, _ ->
                val intent = Intent(this, LoginPage::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)

                auth.signOut()
            }
            .setNegativeButton("Annulla", null)
            .show()
    }

    fun passaInformazioniProfessore(view: View){
        val intent = Intent(this, InformazioniProfessore::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setMessage("Effettuare il logout?")
            .setCancelable(true)
            .setNegativeButton("Ok") { _, _ ->
                val intent = Intent(this, LoginPage::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                super.onBackPressed()

                auth.signOut()
            }
            .setPositiveButton("Annulla", null)
            .show()
    }
    fun passaFeedback(view: View){
        val intent = Intent(this, FeedbackProfSelezionato::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}