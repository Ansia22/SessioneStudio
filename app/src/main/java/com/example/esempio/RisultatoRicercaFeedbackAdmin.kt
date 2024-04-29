package com.example.esempio

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.esempio.models.Feedback
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Pagina utilizzata dagli admin contenente i dati
 * dei feedback del prof selezionato.
 *
 * La classe permette di eliminare un feedback, in particolare premendo
 * il bottone "elimina", il feedback verrà eliminato dal database.
 */

class RisultatoRicercaFeedbackAdmin : AppCompatActivity() {

    private lateinit var firebaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_risultato_ricerca_feedback_admin)

        cercaFeed()
    }

    fun tornaElencoFeedback(view: View){
        val intent = Intent(this, RicercaAdminFeedback::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
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
                            val idFeedback = feedback?.id
                            val nomeCognome = feedback?.nomeCognome
                            val stelle = feedback?.numeroStelle
                            val descrizione = feedback?.descrizioneFeed
                            val data = feedback?.dataFeed

                            if (nomeCognome != null && descrizione != null &&
                                data != null && stelle != null && idFeedback!=null) {
                                aggiornaDati(idFeedback,nomeCognome, stelle, descrizione, data)
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
    private fun aggiornaDati(id:String, nomeCognome: String, stelle: Float, descrizione: String, data: String){
        val idText = findViewById<TextView>(R.id.idFeedAdmin)
        val nomeText = findViewById<TextView>(R.id.nomeTextAdmin)
        val dataText = findViewById<TextView>(R.id.dataTextAdmin)
        val descrizioneText = findViewById<TextView>(R.id.descrizioneTextAdmin)
        val ratingBar = findViewById<RatingBar>(R.id.stelleFeedAdmin)

        ratingBar.rating = stelle
        ratingBar.stepSize = .5f

        idText.text = id
        nomeText.text = nomeCognome
        dataText.text = data
        descrizioneText.text = descrizione
    }

    fun eliminaFeedback(view:View){
        AlertDialog.Builder(this)
            .setMessage("Sicuro di voler eliminare definitivamente l'account e i suoi feedback?")
            .setCancelable(true)
            .setPositiveButton("Ok") { _, _ ->
                val intent = Intent(this, RicercaAdminFeedback::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)

                eliminaFeedbackSelezionato()
                finish()

            }
            .setNegativeButton("Annulla", null)
            .show()
    }

    private fun eliminaFeedbackSelezionato(){
        firebaseRef = FirebaseDatabase.getInstance().getReference("Feedback")
        firebaseRef.orderByChild("id")
        val idDaEliminare = Feedback.getIdFeed()

        firebaseRef.child(idDaEliminare).removeValue()
            .addOnSuccessListener {
                // Eliminazione riuscita
                Toast.makeText(applicationContext, "Dati eliminati correttamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { _ ->
                //eliminazione fallisce
                Toast.makeText(applicationContext, "Problema eliminazione dei dati", Toast.LENGTH_SHORT).show()
            }
    }
}
