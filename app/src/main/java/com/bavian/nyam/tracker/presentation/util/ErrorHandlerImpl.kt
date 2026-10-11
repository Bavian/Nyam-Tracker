package com.bavian.nyam.tracker.presentation.util

import android.content.Context
import android.widget.Toast

class ErrorHandlerImpl(
    private val context: Context,
) : ErrorHandler {
    override fun handleError(throwable: Throwable) {
        Toast.makeText(context, throwable.localizedMessage ?: "Scan failed", Toast.LENGTH_SHORT).show()
    }
}
