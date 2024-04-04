package com.example.esempio

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth




class LoginPage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
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
        } else if (email == "admin" && password == "admin") {
            val intent = Intent(this, AdminPage::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        } else {
            auth.signInWithEmailAndPassword(EmailEditText.text.toString(), passwordEditText.text.toString())
                .addOnCompleteListener(this){task->
                    if(task.isSuccessful){
                        Log.d(TAG,"Login eseguito correttamente")
                        Toast.makeText(applicationContext, "Login eseguito correttamente", Toast.LENGTH_SHORT).show()

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

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, home::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}