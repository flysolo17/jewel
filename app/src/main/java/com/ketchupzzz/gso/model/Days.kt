package com.ketchupzzz.gso.model

import java.util.*

class Days {
    var id : Int ? = null
    var day : String ? = null
    private lateinit var listDays : MutableList<Days>
    var isClick : Boolean? = null
    constructor(){}
    constructor(id : Int? ,day : String?) {
        this.id = id
        this.day = day
        this.isClick= false
    }
    fun initDays() {
        listDays = mutableListOf()
        listDays.add(Days(0, SUNDAY))
        listDays.add(Days(1, MONDAY))
        listDays.add(Days(2, TUESDAY))
        listDays.add(Days(3, WEDNESDAY))
        listDays.add(Days(4, THURSDAY))
        listDays.add(Days(5, FRIDAY))
        listDays.add(Days(6, SATURDAY))
    }
    fun getDays() : List<Days> {
        return listDays
    }

    companion object {
        const val MONDAY = "Mon"
        const val TUESDAY = "Tue"
        const val WEDNESDAY = "Wed"
        const val THURSDAY= "Thu"
        const val FRIDAY = "Fri"
        const val SATURDAY = "Sat"
        const val SUNDAY = "Sun"
    }
}