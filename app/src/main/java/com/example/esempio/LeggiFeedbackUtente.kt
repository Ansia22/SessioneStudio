package com.example.esempio

import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.esempio.models.Feedback
import com.example.esempio.models.Professor
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LeggiFeedbackUtente : AppCompatActivity() {

    private lateinit var firebaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leggi_feedback_utente)

        cercaFeed()
    }

    private fun cercaFeed(){
        firebaseRef = FirebaseDatabase.getInstance().getReference("Feedback")
        val idFeed = Feedback.getIdFeed()

        firebaseRef.orderByChild("id").equalTo(idFeed)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (snap in snapshot.children) {
                        try {
                            val feedback = snap.getValue(Feedback::class.java)
                            val nomeCognome = feedback?.nomeCognome
                            val stelle = feedback?.numeroStelle
                            val descrizione = feedback?.descrizioneFeed
                            val data = feedback?.dataFeed

                            if (nomeCognome != null && descrizione != null && data != null && stelle != null) {
                                aggiornaDati(nomeCognome, stelle, descrizione, data)
                            }

                        } catch (e: Exception) {
                            // Gestisci eventuali eccezioni durante il recupero dei dati del professore
                            Toast.makeText(applicationContext, "problema durante la ricerca dei feedback, riprovare", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Gestisci eventuali errori durante la lettura dei dati
                    Toast.makeText(applicationContext, "onCanceled: errore lettura dati", Toast.LENGTH_SHORT).show()

                }
            })
    }

    private fun aggiornaDati(nomeCognome: String, stelle: Float, descrizione: String, data: String){
        val nomeText = findViewById<TextView>(R.id.nomeText)
        val dataText = findViewById<TextView>(R.id.dataText)
        val descrizioneText = findViewById<TextView>(R.id.descrizioneText)
        val ratingBar = findViewById<RatingBar>(R.id.stelleFeed)

        ratingBar.rating = stelle
        ratingBar.stepSize = .5f

        nomeText.setText(nomeCognome)
        dataText.setText(data)
        descrizioneText.setText(descrizione)
    }

    fun tornaPaginaPrecedente(view: View){
        finish()
    }
}