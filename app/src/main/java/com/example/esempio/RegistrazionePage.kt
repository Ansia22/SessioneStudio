package com.example.esempio

import android.content.Intent
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

/**
 * Schermata dedicata alla registrazione dei professori
 * con email e password.
 *
 * La classe permette di verificare se l'email inserita è
 * già stata registrata, se è stata disabilitata dagli admin,
 * verifica che le due password inserite coincidano e
 * contengano almeno 6 caratteri, crea il nuovo professore nel database
 * creando una chiave casuale e utilizzando l'email indicata
 * ed invia una mail di verifica per attivare l'account.
 */

class RegistrazionePage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseRef: DatabaseReference

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

        isAbilitato(email){ emailPresente ->
            if (emailPresente) {
                Toast.makeText(applicationContext, "La mail inserita è stata disabilitata dai nostri admin, " +
                        "cambiare email o riprovare", Toast.LENGTH_SHORT).show()
            }else{

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
        }

    }
     private fun createUser(email:String, passwordReg:String){
        auth.createUserWithEmailAndPassword(email,passwordReg)
            .addOnCompleteListener(this){task->
                if(task.isSuccessful){

                    saveData()

                    sendVerificationMail()

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

        professorsRef.child(professorId).setValue(professor)

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

    private fun sendVerificationMail(){

        Thread.sleep(500)

        auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {

            Toast.makeText(baseContext,"Verifica la tua email per completare la registrazione!",
                Toast.LENGTH_SHORT).show()

            val intent = Intent(this, Home::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }
    }
}
