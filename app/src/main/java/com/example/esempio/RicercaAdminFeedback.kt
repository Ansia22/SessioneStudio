package com.example.esempio

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
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
 * Pagina usata dagli amministratori per visualizzare i feedback
 * associati ad un professore.
 *
 * I feedback legati all'account del prof contenuti in firebase,
 * verranno inseriti in una ListView e visualizzati a video.
 */

class RicercaAdminFeedback : AppCompatActivity() {

    private lateinit var firebaseRef: DatabaseReference
    private lateinit var idFeedback : ArrayList<String>
    private lateinit var listView: ListView
    private lateinit var searchView: SearchView
    private lateinit var nomeList: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ricerca_admin_feedback)


        idFeedback = arrayListOf()
        nomeList = arrayListOf()
        ricercaNome()
        }

    fun tornaAdminPage(view: View){
        val intent = Intent(this, RicercaAdminProf::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, RicercaAdminProf::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)

    }

    private fun ricercaNome(){

        firebaseRef = FirebaseDatabase.getInstance().getReference("Feedback")
        val idProf = Professor.getIdProf()

        firebaseRef.orderByChild("idProf").equalTo(idProf)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    nomeList.clear()
                    idFeedback.clear()

                    for (snap in snapshot.children) {
                        try {
                            val feedback = snap.getValue(Feedback::class.java)
                            val feedbackName = feedback?.nomeCognome
                            val feedbackId = feedback?.id

                            if(feedbackName != null && feedbackId != null){
                                nomeList.add(feedbackName)
                                idFeedback.add(feedbackId)
                            }

                        } catch (e: Exception) {
                            // Gestisci eventuali eccezioni durante il recupero dei dati del professore
                            Toast.makeText(applicationContext, "problema durante la ricerca dei dati", Toast.LENGTH_SHORT).show()
                        }
                    }
                    ricercaBarra()
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Gestisci eventuali errori durante la lettura dei dati
                    Toast.makeText(applicationContext, "onCanceled: errore lettura dati", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun ricercaBarra() {

        listView = findViewById(R.id.listaFeedbackAdmin)
        searchView = findViewById(R.id.barraRicercaFeedbackAdmin)

        val userAdapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, nomeList)
        listView.adapter = userAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                userAdapter.filter.filter(newText)
                return false
            }

        })
        listView.setOnItemClickListener{ _, _, i, _ ->
            passaFeedbackSelezionato()
            Feedback.setId(idFeedback[i])
        }
    }
    private fun passaFeedbackSelezionato(){
        val intent = Intent(this, RisultatoRicercaFeedbackAdmin::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

    }

}
