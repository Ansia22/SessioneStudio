package com.example.esempio

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.esempio.models.Professor
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class LoginPage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        auth = Firebase.auth
    }

    fun tornaHome(view: View){
        val intent = Intent(this, home::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    fun passaProfiloPage(view: View){
        val EmailEditText = findViewById<EditText>(R.id.email_input)
        val passwordEditText = findViewById<EditText>(R.id.password_input)

        val email = EmailEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(applicationContext, "Inserisci username e password", Toast.LENGTH_SHORT).show()
        }else if(password.length<6){
            Toast.makeText(applicationContext, "La password deve contenere almeno 6 caratteri!", Toast.LENGTH_SHORT).show()
        }else if (email == "admin" && password == "admin") {
            val intent = Intent(this, AdminPage::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        } else {
            auth.signInWithEmailAndPassword(EmailEditText.text.toString(), passwordEditText.text.toString())
                .addOnCompleteListener(this){task->
                    if(task.isSuccessful){
                        Toast.makeText(applicationContext, "Login eseguito correttamente con $email", Toast.LENGTH_SHORT).show()

                        //DOBBIAMO PRENDERE LA MAIL INSERITA DAL PROF, LA CERCO NEL DATABASE, ESTRAGGO L'ID CORRISPONDENTE E USO IL setVariabile PER IMPOSTARE L'ID

                        getProfessorIdByEmail(email)

                        passwordEditText.setText("")
                        val intent = Intent(this, ProfiloPage::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }else{
                        Toast.makeText(applicationContext, "Credenziali non valide", Toast.LENGTH_SHORT).show()
                        passwordEditText.setText("")
                        passwordEditText.setHintTextColor(Color.RED)
                    }
                }

        }

    }

    fun getProfessorIdByEmail(email: String) {
        val professorsRef = database.getReference("Professori")

        Toast.makeText(applicationContext, "dentro il metodo", Toast.LENGTH_SHORT).show()
        professorsRef.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Toast.makeText(applicationContext, "dentro metodo 2 $email", Toast.LENGTH_SHORT).show()
                    for (snapshot in dataSnapshot.children) {
                        Toast.makeText(applicationContext, "dentro for$email", Toast.LENGTH_SHORT).show()
                        try {
                            val professor = snapshot.getValue(Professor::class.java)
                            val professorId = professor?.id
                            Toast.makeText(applicationContext, "dentro $professorId", Toast.LENGTH_SHORT).show()

                            if (professorId != null) {
                                Professor.setVariabili(professorId)
                                Toast.makeText(applicationContext, "trovato $professorId", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            // Gestisci eventuali eccezioni durante il recupero dei dati del professore
                            Toast.makeText(applicationContext, "eccezione", Toast.LENGTH_SHORT).show()
                        }
                    }
                    // Se non viene trovato alcun professore con l'email specificata
                    Toast.makeText(applicationContext, "non trovato $email", Toast.LENGTH_SHORT).show()

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Gestisci eventuali errori durante la lettura dei dati
                    Toast.makeText(applicationContext, "onCanceled errore lettura dati", Toast.LENGTH_SHORT).show()

                }
            })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, home::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}