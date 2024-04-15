package com.example.esempio

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.esempio.models.Professor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RisultatoRicercaAdminProf : AppCompatActivity() {

    private lateinit var firebaseRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_risultato_ricerca_admin_prof)

    }

    fun tornaRicercaAdminProf(view: View){
        val intent = Intent(this, RicercaAdminProf::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    fun cancellaAccount(view: View){
        AlertDialog.Builder(this)
            .setMessage("Sicuro di voler eliminare definitivamente?")
            .setCancelable(true)
            .setPositiveButton("Ok") { _, _ ->
                val intent = Intent(this, RicercaAdminProf::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                eliminaAccount()
                eliminaData()
                finish()

            }
            .setNegativeButton("Annulla", null)
            .show()
    }
    private fun eliminaAccount() {

        val auth = FirebaseAuth.getInstance()

        auth.getUserByEmail(email)
            .addOnSuccessListener { userRecord ->
                // Ottenuto l'utente, ora puoi eliminare l'account
                auth.deleteUser(userRecord.uid)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            println("Account eliminato con successo")
                        } else {
                            println("Errore durante l'eliminazione dell'account: ${task.exception?.message}")
                        }
                    }
            }
            .addOnFailureListener { exception ->
                println("Errore durante il recupero dell'utente: $exception")
            }



            }
    }

    private fun eliminaData() {
        firebaseRef = FirebaseDatabase.getInstance().getReference("Professori")
        firebaseRef.orderByChild("id")
        val idDaEliminare = Professor.getIdProf()

        firebaseRef.child(idDaEliminare).removeValue()
            .addOnSuccessListener {
                // Eliminazione riuscita
                // Puoi eseguire ulteriori azioni se necessario
                Toast.makeText(applicationContext, "dati eliminati correttamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // Gestisci l'errore se l'eliminazione fallisce
                Toast.makeText(applicationContext, "problema eliminazione dei dati", Toast.LENGTH_SHORT).show()
            }
    }
}

