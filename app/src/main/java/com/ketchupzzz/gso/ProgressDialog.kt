package com.ketchupzzz.gso

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProgressDialog(private val activity: Activity?) {
    private var alertDialog: AlertDialog? = null
    fun isLoading() {
        val materialAlertDialogBuilder =
            MaterialAlertDialogBuilder(
                activity!!,
                com.google.android.material.R.style.Theme_MaterialComponents_Light_Dialog_Alert
            )
        val layoutInflater = activity.layoutInflater
        materialAlertDialogBuilder.setView(
            layoutInflater.inflate(
                R.layout.progress_dialog,
                null
            )
        )
        materialAlertDialogBuilder.setCancelable(false)
        alertDialog = materialAlertDialogBuilder.create()
        alertDialog!!.show()
    }

    fun stopLoading() {
        alertDialog!!.dismiss()
    }
}