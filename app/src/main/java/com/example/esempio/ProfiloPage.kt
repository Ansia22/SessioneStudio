package com.example.esempio

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class ProfiloPage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profilo_page)

        auth = Firebase.auth
    }

    fun tornaLogin(view: View){
        AlertDialog.Builder(this)
            .setMessage("Effettuare il logout?")
            .setCancelable(true)
            .setPositiveButton("Ok") { _, _ ->
                val intent = Intent(this, LoginPage::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

                auth.signOut()
            }
            .setNegativeButton("Annulla", null)
            .show()
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setMessage("Effettuare il logout?")
            .setCancelable(true)
            .setPositiveButton("Ok") { _, _ ->
                super.onBackPressed()

                auth.signOut()
            }
            .setNegativeButton("Annulla", null)
            .show()
    }
}