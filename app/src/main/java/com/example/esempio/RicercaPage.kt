package com.example.esempio

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.esempio.models.Professor


class RicercaPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ricerca_page)
    }

    fun tornaHome(view: View){
        val intent = Intent(this, Home::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
    fun passaRisultati(view: View){
        val nomeText = findViewById<EditText>(R.id.nome_input)
        val cognomeText = findViewById<EditText>(R.id.cognome_input)
        val materiaText =findViewById<EditText>(R.id.materia_input)
        val indirizzoText =findViewById<EditText>(R.id.indirizzo_input)

        val nomeInput = nomeText.text.toString()
        val cognomeInput = cognomeText.text.toString()
        val materiaInput = materiaText.text.toString()
        val indirizzoInput = indirizzoText.text.toString()

        Professor.setVariabiliRicerca(nomeInput,cognomeInput,materiaInput,indirizzoInput)
        val intent = Intent(this, RisultatoRicerca::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, Home::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

}