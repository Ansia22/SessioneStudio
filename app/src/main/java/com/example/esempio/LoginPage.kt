package com.example.esempio

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
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


class LoginPage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        auth = Firebase.auth
    }

    fun passaRecuperoPassword(view: View){
        val intent = Intent(this, RecuperaPassword::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun tornaHome(view: View){
        val intent = Intent(this, Home::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    fun passaProfiloPage(view: View){
        
        val emailEditText = findViewById<EditText>(R.id.email_input)
        val passwordEditText = findViewById<EditText>(R.id.password_input)

        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        isAbilitato(email){ emailPresente ->
            if (emailPresente) {
                Toast.makeText(applicationContext, "La mail inserita è stata disabilitata dai nostri admin, " +
                        "cambiare account o riprovare", Toast.LENGTH_SHORT).show()
            }else{

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(applicationContext, "Inserisci email e password", Toast.LENGTH_SHORT).show()

                }else if (email == "admin" && password == "admin") {
                    val intent = Intent(this, RicercaAdminProf::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)

                } else if(password.length<6){
                    Toast.makeText(applicationContext, "La password deve contenere almeno 6 caratteri!", Toast.LENGTH_SHORT).show()

                } else {
                    auth.signInWithEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString())
                        .addOnCompleteListener(this){task->
                            if(task.isSuccessful){

                                if(auth.currentUser?.isEmailVerified == true){

                                    Toast.makeText(applicationContext, "Login eseguito correttamente con $email", Toast.LENGTH_SHORT).show()

                                    getProfessorIdByEmail(email)

                                    passwordEditText.setText("")
                                    val intent = Intent(this, ProfiloPage::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)

                                }else{
                                    Toast.makeText(applicationContext, "Verificare la mail proma di effettuare il login!", Toast.LENGTH_SHORT).show()
                                }
                            }else{
                                Toast.makeText(applicationContext, "Credenziali non valide", Toast.LENGTH_SHORT).show()
                                passwordEditText.setText("")
                                passwordEditText.setHintTextColor(Color.RED)
                            }
                        }
                }

            }
        }
    }

    private fun getProfessorIdByEmail(email: String) {
        firebaseRef = FirebaseDatabase.getInstance().getReference("Professori")

        firebaseRef.orderByChild("email").equalTo(email)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snap in snapshot.children) {
                        try {
                            val professor = snap.getValue(Professor::class.java)
                            val professorId = professor?.id

                            if (professorId != null) {
                                Professor.setVariabiliLogin(professorId, email)
                            }
                        } catch (e: Exception) {
                            // Gestisci eventuali eccezioni durante il recupero dei dati del professore
                            Toast.makeText(applicationContext, "problema durante il login, riprovare", Toast.LENGTH_SHORT).show()
                        }
                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Gestisci eventuali errori durante la lettura dei dati
                    Toast.makeText(applicationContext, "onCanceled: errore lettura dati", Toast.LENGTH_SHORT).show()

                }
            })
    }

    private fun isAbilitato(mailProf:String, callback: (Boolean) -> Unit){

        firebaseRef = FirebaseDatabase.getInstance().getReference("AccountDisabilitati")

        firebaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var isEmailFound = false

                for (snapshot in dataSnapshot.children) {

                    val dati = snapshot.getValue(AccountDisabilitati::class.java)
                    val professorEmail = dati?.mailProf

                    if (professorEmail == mailProf) {
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

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, Home::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}