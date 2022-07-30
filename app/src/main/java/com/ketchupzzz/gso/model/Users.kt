package com.ketchupzzz.gso.model

class User {
    var userID: String? = null
    var userFirstName: String? = null
    var userLastName: String? = null
    var userAddress: String? = null
    var userEmail: String? = null
    var userPhoneNumber: String? = null
    var userType: String? = null

    constructor() {}
    constructor(
        userID: String?,
        userFirstName: String?,
        userLastName: String?,
        userAddress: String?,
        userEmail: String?,
        userPhoneNumber: String?,
        userType: String?
    ) {
        this.userID = userID
        this.userFirstName = userFirstName
        this.userLastName = userLastName
        this.userAddress = userAddress
        this.userEmail = userEmail
        this.userPhoneNumber = userPhoneNumber
        this.userType = userType
    }

    companion object {
        const val TABLE_NAME = "Users"
        const val ARG_USER_TYPE = "userType"
        const val ARG_FIRST_NAME = "userFirstName"
        const val ARG_LAST_NAME = "userLastname"
        const val ARG_USER_ADDRESS = "userAddress"
        const val ARG_EMAIL = "userEmail"
        const val ARG_USER_ID = "userID"
        const val USER_PHONE_NUMBER = "userPhoneNumber"
    }
}