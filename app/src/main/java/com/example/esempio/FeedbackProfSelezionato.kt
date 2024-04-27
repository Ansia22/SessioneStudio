package com.example.esempio

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
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

/**
 * Classe usata per la lettura di feedback.
 * Grazie ad una ListView, l'utente può leggere tutti i feedback del professore
 * selezionato letti dal database.
 * Viene inolstre stampato a video la media delle valutazioni del professore.
 * L'utente può senezionare i singoli feedback e leggere la sua descrizione.
 */

class FeedbackProfSelezionato : AppCompatActivity() {

    private lateinit var firebaseRef: DatabaseReference
    private lateinit var datiFeed : ArrayList<String>
    private lateinit var idFeed : ArrayList<String>
    private lateinit var mediaStelline : ArrayList<Float>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback_prof_selezionato)

        datiFeed = arrayListOf()
        mediaStelline = arrayListOf()
        idFeed = arrayListOf()
        setStelle()
        cercaFeed()

    }

    private fun setStelle(){

        firebaseRef = FirebaseDatabase.getInstance().getReference("Feedback")
        val idProfessore = Professor.getIdProf()

        firebaseRef.orderByChild("idProf").equalTo(idProfessore)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    mediaStelline.clear()

                    for (snap in snapshot.children) {
                        try {
                            val feedback = snap.getValue(Feedback::class.java)
                            val valutazione = feedback?.numeroStelle

                            if (valutazione != null) {
                                mediaStelline.add(valutazione)
                            }

                        } catch (e: Exception) {
                            // Gestisci eventuali eccezioni durante il recupero dei dati del professore
                            Toast.makeText(applicationContext, "problema durante la ricerca dei feedback, riprovare", Toast.LENGTH_SHORT).show()
                        }
                    }

                    calcolaMedia()

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Gestisci eventuali errori durante la lettura dei dati
                    Toast.makeText(applicationContext, "onCanceled: errore lettura dati", Toast.LENGTH_SHORT).show()

                }
            })

    }

    private fun cercaFeed(){
        firebaseRef = FirebaseDatabase.getInstance().getReference("Feedback")
        val idProfessore = Professor.getIdProf()

        firebaseRef.orderByChild("idProf").equalTo(idProfessore)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (snap in snapshot.children) {
                        try {
                            val feedback = snap.getValue(Feedback::class.java)
                            val nomeCognome = feedback?.nomeCognome
                            val numeroStelle = feedback?.numeroStelle.toString()
                            val idFeedback = feedback?.id

                            if(nomeCognome!=null && idFeedback != null){

                                datiFeed.add("$nomeCognome, valutazione: $numeroStelle" )
                                idFeed.add(idFeedback)

                            }

                        } catch (e: Exception) {
                            // Gestisci eventuali eccezioni durante il recupero dei dati del professore
                            Toast.makeText(applicationContext, "problema durante la ricerca dei feedback, riprovare", Toast.LENGTH_SHORT).show()
                        }
                    }
                    aggiornaLista()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Gestisci eventuali errori durante la lettura dei dati
                    Toast.makeText(applicationContext, "onCanceled: errore lettura dati", Toast.LENGTH_SHORT).show()

                }
            })
    }


    private fun calcolaMedia(){

        val stelleText = findViewById<TextView>(R.id.stelle)
        val mediaText = findViewById<TextView>(R.id.media_valutazioni)
        val ratingBar = findViewById<RatingBar>(R.id.mediaStelleFeed)

        if (mediaStelline.isEmpty()) {
            stelleText.setText("Nessun feedback trovato per il professore")
            mediaText.setText(" ")
            ratingBar.visibility = View.INVISIBLE
        }else{

            var somma = 0f
            for (elemento in mediaStelline) {
                somma += elemento
            }

            val media = (somma/mediaStelline.size)
            val mediaArrotondata = String.format("%.1f", media)

            stelleText.text = mediaArrotondata

            ratingBar.rating = mediaArrotondata.toFloat()
            ratingBar.stepSize = .1f
        }
    }

    private fun aggiornaLista(){
        val listView = findViewById<ListView>(R.id.listViewFeedback)

        val arrayAdapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, datiFeed)
        listView.adapter = arrayAdapter
        listView.setOnItemClickListener{adapterView, view, i, l->
            passaFeedSelezionato()
            Feedback.setId(idFeed[i])
        }
    }

    private fun passaFeedSelezionato(){
        val intent = Intent(this, LeggiFeedbackUtente::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun tornaPaginaPrecedente(view: View){
        finish()
    }
}