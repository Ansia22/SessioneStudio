package com.example.esempio

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.esempio.models.Professor
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale
import kotlin.math.abs


class RisultatoRicerca : AppCompatActivity() {

    private lateinit var firebaseRef: DatabaseReference
    private lateinit var datiProf : ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_risultato_ricerca)

        cercaProf()
        datiProf = arrayListOf()

    }
    fun tornaRicerca(view: View){
        val intent = Intent(this, RicercaPage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun cercaProf(){

        val nomeIn = Professor.getNomeProf().toLowerCase()
        val cognomeIn = Professor.getCognomeProf().toLowerCase()
        val materiaIn = Professor.getMateriaProf().toLowerCase()
        val indirizzoIn = Professor.getIndirizzoProf().toLowerCase()

        firebaseRef = FirebaseDatabase.getInstance().getReference("Professori")
        firebaseRef.orderByChild("id")
            .addValueEventListener(object:ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    datiProf.clear()

                    for (snap in snapshot.children) {
                        try {
                            val professor = snap.getValue(Professor::class.java)
                            val professorNome = professor?.nome
                            val professorCognome = professor?.cognome
                            val professorMateria = professor?.materie
                            val professorIndirizzo = professor?.indirizzo

                            if(professorNome != null && Contains(nomeIn,professorNome.toLowerCase()) &&
                                professorCognome != null && Contains(cognomeIn,professorCognome.toLowerCase()) &&
                                professorMateria != null && Contains(materiaIn,professorMateria.toLowerCase()) &&
                                professorIndirizzo != null && Contains(indirizzoIn,professorIndirizzo.toLowerCase())){

                                datiProf.add("$professorCognome $professorNome")
                            }

                        } catch (e: Exception) {
                            // Gestisci eventuali eccezioni durante il recupero dei dati del professore
                            Toast.makeText(applicationContext, "problema durante la ricerca dei dati", Toast.LENGTH_SHORT).show()
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

    private fun aggiornaLista(){
        val listView = findViewById<ListView>(R.id.listaRisultati)

        val arrayAdapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, datiProf)
        listView.adapter = arrayAdapter
        listView.setOnItemClickListener{adapterView, view, i, l->
            Toast.makeText(this, "item selected " + datiProf[i], Toast.LENGTH_LONG).show()
        }
    }


    private fun Contains(parola:String, stringaDatabase:String): Boolean {
        if(stringaDatabase.length < parola.length){
            return false
        }else {
            var i = 0
            while (i <= abs(stringaDatabase.length - parola.length)) {

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