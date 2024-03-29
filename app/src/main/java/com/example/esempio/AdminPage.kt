package com.example.esempio

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class AdminPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_page)
    }
    fun tornaLogin(view: View){
        AlertDialog.Builder(this)
            .setMessage("Effettuare il logout?")
            .setCancelable(true)
            .setPositiveButton("Ok") { _, _ ->
                val intent = Intent(this, LoginPage::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
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
            }
            .setNegativeButton("Annulla", null)
            .show()
    }
}