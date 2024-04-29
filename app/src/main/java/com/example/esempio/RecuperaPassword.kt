package com.example.esempio

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.esempio.models.Professor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Classe implementata per la gestione del recupero della password di un account specifico.
 *
 * Inserendo la propria mail personale, il professore riceverà un messaggio
 * contenente un link (gestito da firebase) per il reset della password.
 *
 * Viene inoltre verificato che la mail inserita sia esistente e registrata correttamente.
 */

class RecuperaPassword : AppCompatActivity() {

    private lateinit var firebaseRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recupera_password)
    }

    fun tornaLogin(view: View){
        val intent = Intent(this, LoginPage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)

    }

    fun inviaMail(view: View){

        val auth = FirebaseAuth.getInstance()

        val emailText = findViewById<EditText>(R.id.emailRecupero)
        val email = emailText.text.toString()

        checkIfEmailExists(email) { emailProf ->
            if (emailProf) {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(applicationContext, "Mail di recupero inviata con successo", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Errore durante l'invio della mail, riprovare", Toast.LENGTH_SHORT).show()
                        }
                    }
                tornaLoginPage()
            } else {
                Toast.makeText(applicationContext, "La mail inserita non corrisponde a nessun account abilitato", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun checkIfEmailExists(email: String, callback: (Boolean) -> Unit) {

        firebaseRef = FirebaseDatabase.getInstance().getReference("Professori")

        firebaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var isEmailFound = false
                for (snapshot in dataSnapshot.children) {
                    val professor = snapshot.getValue(Professor::class.java)
                    val professorEmail = professor?.email

                    if (professorEmail == email) {
                        isEmailFound = true
                        break
                    }
                }
                callback(isEmailFound)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(false)
            }
        })
    }

    private fun tornaLoginPage(){
        val intent = Intent(this, LoginPage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)

    }
}