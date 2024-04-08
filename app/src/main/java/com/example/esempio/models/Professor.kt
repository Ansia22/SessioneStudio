package com.example.esempio.models

data class Professor(
    val id: String,
    val email: String,
    val nome: String,
    val cognome: String,
    val materie: String,
    val indirizzo: String,
    val orari: String
){

    constructor() : this("", "", "", "","","","")
    companion object {
        private lateinit var idProf: String
        private lateinit var emailProf: String

        fun setVariabili(idInput: String, emailInput: String) {
            idProf = idInput
            emailProf = emailInput
        }
        fun getIdProf(): String {
            return idProf
        }
        fun getEmailProf(): String {
            return emailProf
        }
    }
}

