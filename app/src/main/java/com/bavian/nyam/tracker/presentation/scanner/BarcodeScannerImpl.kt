package com.bavian.nyam.tracker.presentation.scanner

import android.content.Context
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class BarcodeScannerImpl : BarcodeScanner {
    override suspend fun startScan(context: Context): Result<BarcodeScanner.ScannerState> =
        suspendCancellableCoroutine { continuation ->
            val options = GmsBarcodeScannerOptions.Builder().build()
            val scanner = GmsBarcodeScanning.getClient(context, options)

            scanner
                .startScan()
                .addOnSuccessListener { barcode ->
                    val result =
                        barcode.displayValue ?: barcode.rawValue ?: "No barcode value detected"
                    continuation.resume(Result.success(BarcodeScanner.ScannerState(result)))
                }.addOnFailureListener { e ->
                    continuation.resume(Result.failure(e))
                }.addOnCanceledListener {
                    continuation.resume(Result.failure(Exception("Scan canceled by user")))
                }
        }
}
