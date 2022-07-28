package com.flysolo.collectorapp.models

class User(
    var userID: String? = "",
    var userFirstName: String? = "",
    var userLastName: String? = "",
    var userAddress: String?= "",
    var userEmail: String? = "",
    var userPhoneNumber: String?= "",
    var userType: String? = ""
) {
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