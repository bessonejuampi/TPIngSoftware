package com.example.tpingsoftware.utils

import android.app.AlertDialog
import android.content.Context

class DialogHelper {

    companion object {
        fun showConfirmationDialog(context: Context, message: String, onAccept: () -> Unit, onCancel: () -> Unit = {}) {
            val builder = AlertDialog.Builder(context)

            builder.setMessage(message)

            builder.setPositiveButton(android.R.string.ok) { dialog, which ->
                onAccept()
                dialog.dismiss()
            }

            builder.setNegativeButton(android.R.string.cancel) { dialog, which ->
                onCancel()
                dialog.dismiss()
            }

            builder.show()
        }
    }
}