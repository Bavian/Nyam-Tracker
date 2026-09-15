package com.bavian.nyam.tracker

import android.content.Context
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class BarcodeScannerImpl : BarcodeScanner {
    override fun startScan(context: Context): Flow<BarcodeScanner.ScannerState> = callbackFlow {
        trySend(BarcodeScanner.ScannerState.Loading)

        val options = GmsBarcodeScannerOptions.Builder().build()
        val scanner = GmsBarcodeScanning.getClient(context, options)

        scanner.startScan()
            .addOnSuccessListener { barcode ->
                val result = barcode.displayValue ?: barcode.rawValue ?: "No barcode value detected"
                trySend(BarcodeScanner.ScannerState.Success(result))
                close()
            }
            .addOnFailureListener { e ->
                trySend(BarcodeScanner.ScannerState.Error(e.localizedMessage ?: "Scan failed"))
                close()
            }
            .addOnCanceledListener {
                trySend(BarcodeScanner.ScannerState.Error("Scan canceled by user"))
                close()
            }

        awaitClose {
            // Clean up if needed, though GmsBarcodeScanner doesn't have an explicit stop
        }
    }
}
