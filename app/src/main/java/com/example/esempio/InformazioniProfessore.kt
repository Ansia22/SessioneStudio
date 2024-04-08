package com.example.esempio

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.esempio.models.Professor
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class InformazioniProfessore : AppCompatActivity() {

    private lateinit var firebaseRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informazioni_professore)
        estraiDati()
    }

    fun tornaProfilo(view: View){
        val intent = Intent(this, ProfiloPage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, ProfiloPage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun estraiDati(){
        firebaseRef = FirebaseDatabase.getInstance().getReference("Professori")
        val idProf = Professor.getIdProf()

        firebaseRef.orderByChild("id").equalTo(idProf)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snap in snapshot.children) {
                        try {
                            val professor = snap.getValue(Professor::class.java)
                            val nome = professor?.nome
                            val cognome = professor?.cognome
                            val materie = professor?.materie
                            val indirizzo = professor?.indirizzo
                            val descrizione = professor?.descrizione

                            if (nome != null && cognome != null && materie != null && indirizzo != null && descrizione != null) {
                                setDati(nome,cognome,materie,indirizzo,descrizione)
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
    private fun setDati(nome:String,cognome:String,materie:String,indirizzo:String,descrizione:String){

        val nomeText = findViewById<EditText>(R.id.nome)
        val cognomeText = findViewById<EditText>(R.id.cognome)
        val materieText = findViewById<EditText>(R.id.materie)
        val indirizzoText = findViewById<EditText>(R.id.indirizzo)
        val descrizioneText = findViewById<EditText>(R.id.descrizione)

        nomeText.setText("$nome")
        cognomeText.setText("$cognome")
        materieText.setText("$materie")
        indirizzoText.setText("$indirizzo")
        descrizioneText.setText("$descrizione")

    }

    fun salvaDati(view: View){
        firebaseRef = FirebaseDatabase.getInstance().getReference("Professori")

        val nomeText = findViewById<EditText>(R.id.nome)
        val cognomeText = findViewById<EditText>(R.id.cognome)
        val materieText = findViewById<EditText>(R.id.materie)
        val indirizzoText = findViewById<EditText>(R.id.indirizzo)
        val descrizioneText = findViewById<EditText>(R.id.descrizione)

        val newNome = nomeText.text.toString()
        val newCognome = cognomeText.text.toString()
        val newMaterie = materieText.text.toString()
        val newIndirizzo = indirizzoText.text.toString()
        val newDescrizione = descrizioneText.text.toString()

        val professorId = Professor.getIdProf()
        val updateprofessor = Professor(professorId,)




    }


}