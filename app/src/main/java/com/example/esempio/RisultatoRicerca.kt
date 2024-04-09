package com.example.esempio

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.esempio.models.Professor
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class RisultatoRicerca : AppCompatActivity() {

    private lateinit var firebaseRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_risultato_ricerca)

        cercaProf()
    }
    fun tornaRicerca(view: View){
        val intent = Intent(this, RicercaPage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun cercaProf(){

        val nomeIn = Professor.getNomeProf()
        val cognomeIn = Professor.getCognomeProf()
        val materiaIn = Professor.getMateriaProf()
        val indirizzoIn = Professor.getIndirizzoProf()

        firebaseRef = FirebaseDatabase.getInstance().getReference("Professori")
        firebaseRef.orderByChild("id")
            .addValueEventListener(object:ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snap in snapshot.children) {
                        try {
                            val professor = snap.getValue(Professor::class.java)
                            val professorNome = professor?.nome
                            val professorCognome = professor?.cognome
                            val professorMateria = professor?.materie
                            val professorIndirizzo = professor?.indirizzo

                            if(professorNome != null && Contains(nomeIn,professorNome) &&
                                professorCognome != null && Contains(cognomeIn,professorCognome) &&
                                professorMateria != null && Contains(materiaIn,professorMateria) &&
                                professorIndirizzo != null && Contains(indirizzoIn,professorIndirizzo)){
                                //INSERIRE QUI IL METODO PER FAR VEDERE ALL'UTENTE I RISULTATI DELLA RICERCA(RIMUOVERE IL TOAST ALLA FINE)
                                Toast.makeText(applicationContext, "$professorNome, $professorIndirizzo", Toast.LENGTH_SHORT).show()
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
    private fun Contains(parola:String, stringaDatabase:String): Boolean {
        if(stringaDatabase.length < parola.length){
            return false
        }else {
            var i = 0
            while (i <= Math.abs(stringaDatabase.length - parola.length)) {

                val stringaDaConfrontare = stringaDatabase.substring(i, parola.length + i)

                if (parola == stringaDaConfrontare) {
                    return true
                }
                i++
            }
            return false
        }
    }


}