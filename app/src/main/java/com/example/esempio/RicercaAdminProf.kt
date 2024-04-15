package com.example.esempio


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
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

    fun tornaAdminPage(view: View) {
        val intent = Intent(this, AdminPage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, AdminPage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)

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
            passaProfessoreSelezionato()
            Professor.setVariabiliLogin(idProf[i], mailList[i])
        }
    }

    private fun ricercaEmail(){

        firebaseRef = FirebaseDatabase.getInstance().getReference("Professori")
        firebaseRef.orderByChild("email")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    mailList.clear()

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
   private fun passaProfessoreSelezionato(){
       val intent = Intent(this, RisultatoRicercaAdminProf::class.java)
       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
       startActivity(intent)
   }

}