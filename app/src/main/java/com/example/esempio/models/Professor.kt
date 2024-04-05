package com.example.esempio.models

data class Professor(
    val id: String,
    val email: String,
    val nome: String,
    val cognome: String,
    val materie: String,
    val indirizzo: String,
    val descrizione: String
){
    companion object {
        private lateinit var idProf: String

        fun setVariabili(idInput: String) {
            idProf = idInput
        }
        fun getIdProf(): String {
            return idProf
        }
    }
}

