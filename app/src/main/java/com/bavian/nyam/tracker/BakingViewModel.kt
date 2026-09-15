package com.bavian.nyam.tracker

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.content
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ScannerUiState {
    data object Idle : ScannerUiState
    data object Loading : ScannerUiState
    data class Success(val barcodeValue: String) : ScannerUiState
    data class Error(val errorMessage: String) : ScannerUiState
}

class BakingViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> =
        _uiState.asStateFlow()

    private val _scanState = MutableStateFlow<ScannerUiState>(ScannerUiState.Idle)
    val scanState: StateFlow<ScannerUiState> = _scanState.asStateFlow()

    private val generativeModel = Firebase.ai.generativeModel(
        modelName = "gemini-flash-latest",
    )

    fun startScan(context: Context) {
        _scanState.value = ScannerUiState.Loading
        val options = GmsBarcodeScannerOptions.Builder().build()
        val scanner = GmsBarcodeScanning.getClient(context, options)

        scanner.startScan()
            .addOnSuccessListener { barcode ->
                val result = barcode.displayValue ?: barcode.rawValue ?: "No barcode value detected"
                _scanState.value = ScannerUiState.Success(result)
            }
            .addOnFailureListener { e ->
                _scanState.value = ScannerUiState.Error(e.localizedMessage ?: "Scan failed")
            }
            .addOnCanceledListener {
                _scanState.value = ScannerUiState.Error("Scan canceled by user")
            }
    }

    fun sendPrompt(bitmap: Bitmap, prompt: String) {
        _uiState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(
                    content {
                        image(bitmap)
                        text(prompt)
                    }
                )
                response.text?.let { outputContent ->
                    _uiState.value = UiState.Success(outputContent)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "")
            }
        }
    }
}