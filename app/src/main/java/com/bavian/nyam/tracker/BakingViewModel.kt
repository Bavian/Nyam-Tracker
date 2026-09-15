package com.bavian.nyam.tracker

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BakingViewModel(
    private val barcodeScanner: BarcodeScanner
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val generativeModel = Firebase.ai.generativeModel(
        modelName = "gemini-flash-latest",
    )

    fun startScan(context: Context) {
        viewModelScope.launch {
            barcodeScanner.startScan(context).collect { state ->
                _uiState.update { it.copy(scanState = state) }
            }
        }
    }

    fun sendPrompt(bitmap: Bitmap, prompt: String) {
        _uiState.update { it.copy(resultState = UiState.ResultState.Loading) }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(
                    content {
                        image(bitmap)
                        text(prompt)
                    }
                )
                response.text?.let { outputContent ->
                    _uiState.update {
                        it.copy(resultState = UiState.ResultState.Success(outputContent))
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(resultState = UiState.ResultState.Error(e.localizedMessage ?: ""))
                }
            }
        }
    }
}
