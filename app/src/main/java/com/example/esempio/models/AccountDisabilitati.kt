package com.example.esempio.models

/**
 * Data class utilizzata per la gestione degli account disabilitati.
 */
data class AccountDisabilitati(
    val idProf: String,
    val mailProf: String
){
    constructor() : this("", "")
    companion object {}
}
