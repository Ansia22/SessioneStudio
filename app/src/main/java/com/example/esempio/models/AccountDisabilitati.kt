package com.example.esempio.models

data class AccountDisabilitati(
    val idProf: String,
    val mailProf: String
){
    constructor() : this("", "")
    companion object {}
}
