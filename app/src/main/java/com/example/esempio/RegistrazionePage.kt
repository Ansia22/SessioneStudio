package com.example.esempio

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.esempio.models.Professor
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase


class RegistrazionePage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrazione_page)
        auth = Firebase.auth
    }

    fun tornaHome(view: View){
        val intent = Intent(this, Home::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
    fun RegistraUtente(view: View){

        val emailAdressInput = findViewById<EditText>(R.id.EmailAddress)
        val passwordInput = findViewById<EditText>(R.id.Password)
        val passwordRipetutaInput = findViewById<EditText>(R.id.PasswordRipetuta)

        val email = emailAdressInput.text.toString()
        val passwordReg = passwordInput.text.toString()
        val passwordRegRip = passwordRipetutaInput.text.toString()

        if (email.isEmpty() || passwordReg.isEmpty() || passwordRegRip.isEmpty()) {
            Toast.makeText(applicationContext, "Inserisci tutti i dati richiesti", Toast.LENGTH_SHORT)
                .show()
        }else if(passwordReg != passwordRegRip){
            Toast.makeText(applicationContext, "Le password inserite non coincidono", Toast.LENGTH_SHORT)
                .show()

        }else if(passwordReg.length<6){
            Toast.makeText(applicationContext, "La password deve contenere almeno 6 caratteri!", Toast.LENGTH_SHORT).show()
        }else{
            createUser(emailAdressInput.text.toString(),passwordInput.text.toString())
        }

    }
     private fun createUser(email:String, passwordReg:String){
        auth.createUserWithEmailAndPassword(email,passwordReg)
            .addOnCompleteListener(this){task->
                if(task.isSuccessful){
                    Toast.makeText(baseContext,"Utente registrato correttamente",
                        Toast.LENGTH_SHORT).show()

                        saveData()

                        val intent = Intent(this, ProfiloPage::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)

                }else{
                    Toast.makeText(baseContext,"Mail non valida o già utilizzata",
                        Toast.LENGTH_SHORT).show()
                }

            }
    }

    private fun saveData(){

        val database = FirebaseDatabase.getInstance()
        val professorsRef = database.getReference("Professori")

        val newProfessorRef = professorsRef.push()
        val professorId = newProfessorRef.key!!

        val emailAdressInput = findViewById<EditText>(R.id.EmailAddress)
        val professorEmail = emailAdressInput.text.toString()

        val professorNome = ""
        val professorCognome = ""
        val professorMaterie = ""
        val professorIndirizzo = ""
        val professorDescrizione = ""

        val professor = Professor(professorId,professorEmail, professorNome, professorCognome, professorMaterie, professorIndirizzo, professorDescrizione)

        Professor.setVariabiliLogin(professorId, professorEmail)

        professorsRef.child(professorId).setValue(professor)

    }
}
