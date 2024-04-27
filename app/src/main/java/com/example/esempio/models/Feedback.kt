package com.example.esempio.models

/**
 * Data class utilizzata per la gestione dei feedback.
 * La classe è dotata di metodi get e set utilizzati per leggere e scrivere dati.
 */

data class Feedback(
    val id:String,
    val idProf:String,
    val nomeCognome:String,
    val numeroStelle:Float,
    val descrizioneFeed:String,
    val dataFeed:String
){
    constructor() : this("", "", "", 0F,"","")

    companion object {
        private lateinit var idFeed: String

        fun setId(idInput: String) {
            idFeed = idInput
        }

        fun getIdFeed(): String {
            return idFeed

        }
    }
}
