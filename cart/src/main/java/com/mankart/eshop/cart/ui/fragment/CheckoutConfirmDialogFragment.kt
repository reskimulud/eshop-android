package com.mankart.eshop.cart.ui.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class CheckoutConfirmDialogFragment: DialogFragment() {
    private lateinit var listener: CheckoutConfirmDialogListener

    interface CheckoutConfirmDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            listener = childFragmentManager.findFragmentByTag(CartFragment::class.java.simpleName) as CheckoutConfirmDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("Calling fragment must implement CheckoutConfirmDialogListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it)

            builder.setMessage("Process to Checkout?")
                .setPositiveButton("Checkout") { _, _ ->
                    listener.onDialogPositiveClick(this)
                }
                .setNegativeButton("Cancel") { _, _ ->
                    listener.onDialogNegativeClick(this)
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}