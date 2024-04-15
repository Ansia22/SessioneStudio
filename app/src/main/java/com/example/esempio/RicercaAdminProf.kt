package com.example.esempio


import MailAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity

class RicercaAdminProf : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var searchView: SearchView
    private lateinit var mailList: ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ricerca_admin)

        ricercaBarra()
    }

    fun tornaAdminPage(view: View) {
        val intent = Intent(this, AdminPage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, AdminPage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)

    }

    private fun ricercaBarra() {

        listView = findViewById(R.id.listaProfAdmin)
        searchView = findViewById(R.id.barraRicercaAdmin)

        mailList = ArrayList()
        mailList.add("example1@mail.com")
        mailList.add("example2@mail.com")
        mailList.add("example3@mail.com")

        val arrayAdapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, mailList)
        val mailAdapter = MailAdapter(this, mailList)
        listView.adapter = mailAdapter

    }
}