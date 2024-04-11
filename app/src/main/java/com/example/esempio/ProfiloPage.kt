package com.example.esempio

import android.annotation.SuppressLint
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

class ProfiloPage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    @SuppressLint("SetTextI18n")
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
        super.onBackPressed()
        AlertDialog.Builder(this)
            .setMessage("Effettuare il logout?")
            .setCancelable(true)
            .setPositiveButton("Ok") { _, _ ->
                tornaLogin()
            }
            .setNegativeButton("Annulla", null)
            .show()
    }

    private fun tornaLogin() {
        val intent = Intent(this, LoginPage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

        auth.signOut()
    }

    fun passaFeedback(view: View){
        val intent = Intent(this, FeedbackPersonali::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}