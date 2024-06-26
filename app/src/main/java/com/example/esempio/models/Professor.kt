package com.example.esempio.models

/**
 * Data class utilizzata per la gestione dei professor.
 * La classe è dotata di metodi get e set utilizzati per leggere e scrivere dati.
 */
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
        private lateinit var nomeProf: String
        private lateinit var cognomeProf: String
        private lateinit var materiaProf: String
        private lateinit var indirizzoProf: String

        fun setVariabiliLogin(idInput: String, emailInput: String) {
            idProf = idInput
            emailProf = emailInput
        }
        fun setId(idInput: String) {
            idProf = idInput
        }
        fun setVariabiliRicerca(nomeInput:String,cognomeInput:String,materiaInput:String,indirizzoInput:String){
            nomeProf = nomeInput
            cognomeProf =cognomeInput
            materiaProf = materiaInput
            indirizzoProf = indirizzoInput
        }

        fun getIdProf(): String {
            return idProf

        }
        fun getEmailProf(): String {
            return emailProf
        }
        fun getNomeProf(): String {
            return nomeProf
        }
        fun getCognomeProf(): String {
            return cognomeProf
        }
        fun getMateriaProf(): String {
            return materiaProf
        }
        fun getIndirizzoProf(): String {
            return indirizzoProf
        }

    }
}

