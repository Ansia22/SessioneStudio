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
import kotlin.math.abs

/**
 * Pagina contenente i risultati della ricerca di professori.
 *
 * Dopo aver inserito i dati richiesti nella pagina precedente
 * ed aver premuto il bottone "cerca", lo studente avrà a sua disposizione
 * l'elenco di prof idonei.
 *
 * I metodi della classe hanno lo scopo di leggere i datin nel database
 * e far visualizzare all'utente nome e cognome degli account.
 *
 * Premendo su un professore si possono visualizzare nel dettaglio i dati
 * messi a disposizione da quest'ultimo
 * (tra cui mail, orari, indirizzo,...)
 */

class RisultatoRicerca : AppCompatActivity() {

    private lateinit var firebaseRef: DatabaseReference
    private lateinit var datiProf : ArrayList<String>
    private lateinit var idProf : ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_risultato_ricerca)

        cercaProf()
        datiProf = arrayListOf()
        idProf = arrayListOf()

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
                            val professorId = professor?.id

                            if(professorId != null && professorNome != null && contains(nomeIn,professorNome.toLowerCase()) &&
                                professorCognome != null && contains(cognomeIn,professorCognome.toLowerCase()) &&
                                professorMateria != null && contains(materiaIn,professorMateria.toLowerCase()) &&
                                professorIndirizzo != null && contains(indirizzoIn,professorIndirizzo.toLowerCase())){

                                datiProf.add("$professorCognome $professorNome")
                                idProf.add(professorId)

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
        listView.setOnItemClickListener{ _, _, i, _ ->
            passaProfessoreSelezionato()
            Professor.setId(idProf[i])
        }
    }


    private fun passaProfessoreSelezionato(){
        val intent = Intent(this, ProfessoreSelezionato::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    /**
     * il metodo "contains" confronta la stringa inserita dallo studente
     * con quella contenuta nel database per verificare se i dati del professore
     * siano idonei agli standard ricercati.
     *
     * In particolare prende la lunghezza delle 2 stringhe da paragonare.
     * Se quella contenuta nel database ha lunghezza minore di quella cercata
     * allora ritornerà il valore "false".
     * All'interno del ciclo, viene estratta una sottostringa dalla stringa
     * del database, iniziando dalla posizione i e con una lunghezza uguale
     * alla lunghezza della parola cercata.
     * Viene verificato se la parola cercata è uguale alla sottostringa estratta.
     * Se sì, la funzione restituisce true, indicando che la parola è stata trovata.
     * Se la parola cercata non corrisponde alla sottostringa, l'indice i viene
     * incrementato di uno e il ciclo continua.
     * Se il ciclo termina senza trovare la parola in nessuna delle sottostringhe,
     * la funzione restituisce false, indicando che la parola non è stata trovata
     * in nessuna parte della stringa del database
     */
    private fun contains(parola:String, stringaDatabase:String): Boolean {
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

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, RicercaPage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

}