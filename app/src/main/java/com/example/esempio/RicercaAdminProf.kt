package com.example.esempio


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.esempio.models.Professor
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RicercaAdminProf : AppCompatActivity() {

    private lateinit var firebaseRef: DatabaseReference
    private lateinit var idProf : ArrayList<String>
    private lateinit var listView: ListView
    private lateinit var searchView: SearchView
    private lateinit var mailList: ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ricerca_admin)
        idProf = arrayListOf()
        mailList = arrayListOf()
        ricercaEmail()
    }

    fun tornaAdminPage(view: View){
        AlertDialog.Builder(this)
            .setMessage("Effettuare il logout?")
            .setCancelable(true)
            .setPositiveButton("Ok") { _, _ ->
                val intent = Intent(this, LoginPage::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .setNegativeButton("Annulla", null).show()
    }
    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setMessage("Effettuare il logout?")
            .setCancelable(true)
            .setNegativeButton("Ok") { _, _ ->
                val intent = Intent(this, LoginPage::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                super.onBackPressed()
            }
            .setPositiveButton("Annulla", null).show()

    }
    private fun ricercaEmail(){

        firebaseRef = FirebaseDatabase.getInstance().getReference("Professori")
        firebaseRef.orderByChild("email")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    mailList.clear()
                    idProf.clear()

                    for (snap in snapshot.children) {
                        try {
                            val professor = snap.getValue(Professor::class.java)
                            val professorMail = professor?.email
                            val professorId = professor?.id

                            if(professorMail != null && professorId != null){
                                mailList.add(professorMail)
                                idProf.add(professorId)
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

        listView = findViewById(R.id.listaProfAdmin)
        searchView = findViewById(R.id.barraRicercaAdmin)



        val userAdapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, mailList)
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

        listView.setOnItemClickListener{adapterView, view, i, l->
            AlertDialog.Builder(this)
                .setMessage("Cosa si desidera visualizzare?")
                .setCancelable(true)
                .setPositiveButton("Leggi feedback") { _, _ ->

                    passaFeedbackAdmin()
                }
                .setNegativeButton("Leggi dati professore"){ _, _ ->

                    passaProfessoreSelezionato()
                }.show()
            Professor.setId(idProf[i])

        }
    }

    private fun passaProfessoreSelezionato(){
        val intent = Intent(this, RisultatoRicercaAdminProf::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun passaFeedbackAdmin(){
        val intent = Intent(this, RicercaAdminFeedback::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)


    }
}

