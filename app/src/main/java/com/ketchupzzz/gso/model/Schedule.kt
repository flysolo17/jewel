package com.ketchupzzz.gso.model

class Schedule(
    val id : String? = null,
    val gsoID : String? = null,
    val collectorID : String? = null,
    val days: List<Days> ? = mutableListOf(),
    val startTime : Long? = null,
    val routes : List<String> = mutableListOf())
{
}