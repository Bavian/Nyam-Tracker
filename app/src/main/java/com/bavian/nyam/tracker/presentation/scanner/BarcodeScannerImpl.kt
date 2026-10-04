package com.bavian.nyam.tracker.presentation.scanner

import android.content.Context
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class BarcodeScannerImpl(
    private val context: Context,
) : BarcodeScanner {
    override suspend fun startScan(): Result<BarcodeScanner.ScannerState> =
        suspendCancellableCoroutine { continuation ->
            val options =
                GmsBarcodeScannerOptions
                    .Builder()
                    .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                    .enableAutoZoom()
                    .build()
            val scanner: GmsBarcodeScanner = GmsBarcodeScanning.getClient(context, options)
            val moduleInstallClient = ModuleInstall.getClient(context)

            moduleInstallClient
                .areModulesAvailable(scanner)
                .addOnSuccessListener { response ->
                    if (response.areModulesAvailable()) {
                        executeScan(scanner, continuation)
                    } else {
                        val moduleInstallRequest =
                            ModuleInstallRequest
                                .newBuilder()
                                .addApi(scanner)
                                .build()

                        moduleInstallClient
                            .installModules(moduleInstallRequest)
                            .addOnSuccessListener {
                                executeScan(scanner, continuation)
                            }.addOnFailureListener { e ->
                                if (continuation.isActive) {
                                    continuation.resume(Result.failure(e))
                                }
                            }
                    }
                }.addOnFailureListener {
                    executeScan(scanner, continuation)
                }
        }

    private fun executeScan(
        scanner: GmsBarcodeScanner,
        continuation: CancellableContinuation<Result<BarcodeScanner.ScannerState>>,
    ) {
        scanner
            .startScan()
            .addOnSuccessListener { barcode ->
                val result =
                    barcode.displayValue ?: barcode.rawValue ?: "No barcode value detected"
                if (continuation.isActive) {
                    continuation.resume(Result.success(BarcodeScanner.ScannerState(result)))
                }
            }.addOnFailureListener { e ->
                if (continuation.isActive) {
                    continuation.resume(Result.failure(e))
                }
            }.addOnCanceledListener {
                if (continuation.isActive) {
                    continuation.resume(Result.failure(Exception("Scan canceled by user")))
                }
            }
    }
}
