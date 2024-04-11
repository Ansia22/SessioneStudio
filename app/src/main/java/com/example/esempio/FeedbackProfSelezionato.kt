package com.example.esempio

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
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
import org.w3c.dom.Text

class FeedbackProfSelezionato : AppCompatActivity() {

    private lateinit var firebaseRef: DatabaseReference
    private lateinit var datiFeed : ArrayList<String>
    private lateinit var mediaStelline : ArrayList<Float>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback_prof_selezionato)

        datiFeed = arrayListOf()
        mediaStelline = arrayListOf()
        setStelle()
        //cercaFeed()

    }

    private fun setStelle(){

        firebaseRef = FirebaseDatabase.getInstance().getReference("Feedback")
        val idProfessore = Professor.getIdProf()

        firebaseRef.orderByChild("idProf").equalTo(idProfessore)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Toast.makeText(applicationContext, "dentro $idProfessore", Toast.LENGTH_SHORT).show()
                    mediaStelline.clear()

                    for (snap in snapshot.children) {
                        try {
                            Toast.makeText(applicationContext, "dentro for", Toast.LENGTH_SHORT).show()
                            val feedback = snap.getValue(Feedback::class.java)
                            Toast.makeText(applicationContext, "dentro feedback", Toast.LENGTH_SHORT).show()
                            val valutazione = feedback?.numeroStelle
                            Toast.makeText(applicationContext, "dentro numstelle", Toast.LENGTH_SHORT).show()

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


    private fun calcolaMedia(){

        val stelleText = findViewById<TextView>(R.id.stelle)
        val mediaText = findViewById<TextView>(R.id.media_valutazioni)

        if (mediaStelline.isEmpty()) {
            stelleText.setText("Nessun feedback trovato per il professore")
            mediaText.setText(" ")
        }else{

            var somma = 0f
            for (elemento in mediaStelline) {
                somma += elemento
            }

            val media = (somma/mediaStelline.size).toString()

            stelleText.text = media
            }
    }

    fun tornaProfessoreSelezionato(view: View){
        val intent = Intent(this, ProfessoreSelezionato::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}