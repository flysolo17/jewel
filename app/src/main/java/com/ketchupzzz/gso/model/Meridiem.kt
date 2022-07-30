package com.ketchupzzz.gso.model


class Meridiem {
    var id : Int? = null
    var name : String? = null
    private lateinit var meridiemArray : MutableList<Meridiem>
    constructor()
    constructor(id : Int?,name : String?) {
        this.id = id
        this.name = name
    }
    fun initMeridiem() {
        meridiemArray = mutableListOf()
        val am = Meridiem(0,"AM")
        meridiemArray.add(am)
        val pm = Meridiem(1,"PM")
        meridiemArray.add(pm)
    }
    fun getMeridiemName() : MutableList<String> {
            val nameList = mutableListOf<String>()
            meridiemArray.map { meridiem ->
                nameList.add(meridiem.name!!)
            }
            return nameList
    }
    fun getMeridiemList() : List<Meridiem> {
        return meridiemArray
    }

}