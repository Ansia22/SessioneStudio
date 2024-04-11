package com.example.esempio

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import com.example.esempio.R.id.ratingBar
import com.example.esempio.models.Feedback
import com.example.esempio.models.Professor
import com.google.firebase.database.FirebaseDatabase


class FeedbackPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback_page)
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        ratingBar.rating = 1f
        ratingBar.stepSize = .5f



    }

    fun tornaHome(view: View){
        val intent = Intent(this, Home::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    fun salvaFeed(view: View){
        val database = FirebaseDatabase.getInstance()
        val feedbackRef = database.getReference("Feedback")

        val newfeedbackRef = feedbackRef.push()
        val feedId = newfeedbackRef.key!!

        val nomeCognomeText = findViewById<EditText>(R.id.nomeFeed)
        val nomeCognome = nomeCognomeText.text.toString()
        val descrizioneFeedText = findViewById<EditText>(R.id.descrizioneFeed)
        val descrizioneFeed = descrizioneFeedText.text.toString()
        val dataFeedText = findViewById<EditText>(R.id.dataFeed)
        val dataFeed = dataFeedText.text.toString()
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val numeroStelle = ratingBar.rating
        val idProf = Professor.getIdProf()
        val feedback = Feedback(feedId,idProf,nomeCognome,numeroStelle,descrizioneFeed,dataFeed)

        feedbackRef.child(feedId).setValue(feedback)

        passaProfessoreSelezionato()

    }
    fun passaProfessoreSelezionato(){
        val intent = Intent(this, ProfessoreSelezionato::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}