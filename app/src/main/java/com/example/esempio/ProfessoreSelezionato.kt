package com.example.esempio

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.esempio.models.Professor
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Classe usata per la gestione delle informazioni dei professori cercati.
 * L'utente utilizzando lo strumento di ricerca potrà individuare il professore
 * più adatto alle sue esigienze e selezionandolo ne visualizzerà le informazioni.
 *
 * La classe sfrutta l'id del prof per cercare le informazioni nel database
 * e renderle disponibili agli studenti.
 */

class ProfessoreSelezionato : AppCompatActivity() {

    private lateinit var firebaseRef : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_professore_selezionato)

        setTextViews()
    }

    private fun setTextViews(){

        firebaseRef = FirebaseDatabase.getInstance().getReference("Professori")
        val idProf = Professor.getIdProf()

        firebaseRef.orderByChild("id").equalTo(idProf)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (snap in snapshot.children) {
                        try {
                            val professor = snap.getValue(Professor::class.java)
                            val email = professor?.email
                            val nome = professor?.nome
                            val cognome = professor?.cognome
                            val materie = professor?.materie
                            val indirizzo = professor?.indirizzo
                            val orari = professor?.orari

                            if (email!= null && nome != null && cognome != null && materie != null && indirizzo != null && orari != null) {
                                setDati(email,nome,cognome,materie,indirizzo,orari)
                            }

                        } catch (e: Exception) {
                            // Gestisci eventuali eccezioni durante il recupero dei dati del professore
                            Toast.makeText(applicationContext, "problema durante lettura dati", Toast.LENGTH_SHORT).show()
                        }
                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Gestisci eventuali errori durante la lettura dei dati
                    Toast.makeText(applicationContext, "onCanceled: errore lettura dati", Toast.LENGTH_SHORT).show()

                }
            })

    }

    private fun setDati(email:String, nome:String,cognome:String,materie:String,indirizzo:String,orari:String){

        val emailText = findViewById<TextView>(R.id.emailProf)
        val nomeText = findViewById<TextView>(R.id.nomeProf)
        val cognomeText = findViewById<TextView>(R.id.cognomeProf)
        val materieText = findViewById<TextView>(R.id.materieProf)
        val indirizzoText = findViewById<TextView>(R.id.indirizzoProf)
        val orariText = findViewById<TextView>(R.id.orariProf)

        emailText.text = email
        nomeText.text = nome
        cognomeText.text = cognome
        materieText.text = materie
        indirizzoText.text = indirizzo
        orariText.text = orari

    }
    fun tornaRisultatiRicerca(view: View){
        val intent = Intent(this, RisultatoRicerca::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, RisultatoRicerca::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    fun passaFeedback (view: View){
        val intent = Intent(this, FeedbackPage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun passaFeedbackProfSelezionato(view: View){
        val intent = Intent(this, FeedbackProfSelezionato::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}