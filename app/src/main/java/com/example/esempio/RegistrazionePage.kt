package com.example.esempio

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class RegistrazionePage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrazione_page)
        auth = Firebase.auth
    }

    fun tornaHome(view: View){
        val intent = Intent(this, home::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
    fun RegistraUtente(view: View){

        val EmailAdressInput = findViewById<EditText>(R.id.EmailAddress)
        val PasswordInput = findViewById<EditText>(R.id.Password)
        val PasswordRipetutaInput = findViewById<EditText>(R.id.PasswordRipetuta)

        val email = EmailAdressInput.text.toString()
        val passwordReg = PasswordInput.text.toString()
        val passwordRegRip = PasswordRipetutaInput.text.toString()

        if (email.isEmpty() || passwordReg.isEmpty() || passwordRegRip.isEmpty()) {
            Toast.makeText(applicationContext, "Inserisci tutti i dati richiesti", Toast.LENGTH_SHORT)
                .show()
        }
        else if(passwordReg != passwordRegRip){
            Toast.makeText(applicationContext, "Le password inserite non coincidono", Toast.LENGTH_SHORT)
                .show()

        }
        else{
            createUser(EmailAdressInput.text.toString(),PasswordInput.text.toString())
        }

    }
     fun createUser(email:String,passwordReg:String){
        auth.createUserWithEmailAndPassword(email,passwordReg)
            .addOnCompleteListener(this){task->
                if(task.isSuccessful){
                    Log.d(TAG,"Utente creato correttamente")
                    Toast.makeText(baseContext,"Utente registrato correttamente",
                        Toast.LENGTH_SHORT).show()

                        saveData()

                        val intent = Intent(this, ProfiloPage::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)

                }else{
                    Log.d(TAG,"Errore nella creazione dell'utente")
                    Toast.makeText(baseContext,"Mail non valida o già utilizzata",
                        Toast.LENGTH_SHORT).show()
                }

            }
    }

    fun saveData(){

        firebaseRef = FirebaseDatabase.getInstance().getReference().child("professori")

    }
}
