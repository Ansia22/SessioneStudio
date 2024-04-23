package com.example.esempio

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.esempio.models.AccountDisabilitati
import com.example.esempio.models.Professor
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RisultatoRicercaAdminProf : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var professorId: String
    private lateinit var professorMail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_risultato_ricerca_admin_prof)

        firebaseAuth = Firebase.auth
        cercaDatiProf()
    }

    fun tornaRicercaAdminProf(view: View){
        val intent = Intent(this, RicercaAdminProf::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun cercaDatiProf(){
        firebaseRef = FirebaseDatabase.getInstance().getReference("Professori")
        val idProf = Professor.getIdProf()

        firebaseRef.orderByChild("id").equalTo(idProf)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (snap in snapshot.children) {
                        try {
                            val professor = snap.getValue(Professor::class.java)
                            val professorNome = professor?.nome
                            val professorCognome = professor?.cognome
                            val professorMateria = professor?.materie
                            val professorIndirizzo = professor?.indirizzo
                            professorId = professor?.id.toString()
                            val professorOrario = professor?.orari
                            professorMail = professor?.email.toString()

                            if(professorNome != null &&
                                professorCognome != null && professorMateria != null &&
                                professorIndirizzo != null && professorOrario != null){
                                impostaDatiProf(professorNome, professorCognome, professorMateria, professorIndirizzo, professorOrario)
                            }

                        } catch (e: Exception) {
                            // Gestisci eventuali eccezioni durante il recupero dei dati del professore
                            Toast.makeText(applicationContext, "problema durante la ricerca dei dati", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Gestisci eventuali errori durante la lettura dei dati
                    Toast.makeText(applicationContext, "onCanceled: errore lettura dati", Toast.LENGTH_SHORT).show()
                }
            })

    }

    private fun impostaDatiProf(nome:String,cognome:String,materie:String,indirizzo:String,orari:String,){
        val idText = findViewById<TextView>(R.id.idProfessore)
        val mailText = findViewById<TextView>(R.id.mailProfessore)
        val orariText = findViewById<TextView>(R.id.orariProfessore)
        val nomeText = findViewById<TextView>(R.id.nomeProfessore)
        val cognomeText = findViewById<TextView>(R.id.cognomeProfessore)
        val materieText =findViewById<TextView>(R.id.materieProfessore)
        val indirizzoText =findViewById<TextView>(R.id.indirizzoProfessore)

        idText.setText(professorId)
        mailText.setText(professorMail)
        orariText.setText(orari)
        nomeText.setText(nome)
        cognomeText.setText(cognome)
        materieText.setText(materie)
        indirizzoText.setText(indirizzo)

    }
    fun cancellaAccount(view: View){
        AlertDialog.Builder(this)
            .setMessage("Sicuro di voler disabilitare l'account ed eliminare i suoi feedback?")
            .setCancelable(true)
            .setPositiveButton("Ok") { _, _ ->
                val intent = Intent(this, RicercaAdminProf::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)

                disabilitaAccount()
                eliminaData()
                eliminaFeedbackProf()

                finish()

            }
            .setNegativeButton("Annulla", null)
            .show()
    }
    private fun disabilitaAccount() {

        val database = FirebaseDatabase.getInstance()
        val firebaseRef = database.getReference("AccountDisabilitati")

        val accountDisabilitato = AccountDisabilitati(professorId, professorMail)

        firebaseRef.child(professorId).setValue(accountDisabilitato)


    }

    private fun eliminaData() {
        firebaseRef = FirebaseDatabase.getInstance().getReference("Professori")
        firebaseRef.orderByChild("id")
        val idDaEliminare = Professor.getIdProf()

        firebaseRef.child(idDaEliminare).removeValue()
            .addOnSuccessListener {
                // Eliminazione riuscita
                Toast.makeText(applicationContext, "Dati eliminati correttamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // errore se l'eliminazione fallisce
                Toast.makeText(applicationContext, "Problema eliminazione dei dati", Toast.LENGTH_SHORT).show()
            }
    }

    private fun eliminaFeedbackProf(){
        firebaseRef = FirebaseDatabase.getInstance().getReference("Feedback")
        val idProf = Professor.getIdProf()

        firebaseRef.orderByChild("idProf").equalTo(idProf).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(snapshot in dataSnapshot.children){
                    snapshot.ref.removeValue()
                        .addOnSuccessListener {
                            // Eliminazione riuscita
                            Toast.makeText(applicationContext, "Feedback eliminati correttamente", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            // errore se l'eliminazione fallisce
                            Toast.makeText(applicationContext, "Problema eliminazione dei feedback", Toast.LENGTH_SHORT).show()
                        }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Problema accesso database", Toast.LENGTH_SHORT).show()
            }
        })
    }
}


