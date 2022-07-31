package com.ketchupzzz.gso.model

data class Collector(
    var id : String? = null,
    val firstName : String? = null,
    val lastName : String? = null,
    val phone : String? = null,
    val plateNumber : String? = null,
    val email : String? = null) {
    companion object {
        const val TABLE_NAME = "Collector"
    }
}
