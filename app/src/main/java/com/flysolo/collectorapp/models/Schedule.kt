package com.flysolo.collectorapp.models



data class Schedule(
    val id : String? = null,
    val gsoID : String? = null,
    val collectorID : String? = null,
    val days: List<String> ? = mutableListOf(),
    val startTime : Time? = null,
    val routes : List<String> = mutableListOf())
{
    companion object {
        const val TABLE_NAME = "Schedule"
        const val COLLECTOR_ID = "collectorID"
        const val ID = "id"
    }
}