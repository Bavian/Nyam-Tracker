package com.bavian.nyam.tracker.presentation.setweight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bavian.nyam.tracker.domain.model.WeightsInfo
import com.bavian.nyam.tracker.domain.usecase.weight.DeleteWeightUseCase
import com.bavian.nyam.tracker.domain.usecase.weight.GetNearestWeightUseCase
import com.bavian.nyam.tracker.domain.usecase.weight.SetWeightUseCase
import com.bavian.nyam.tracker.presentation.navigation.AppNavigation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.Instant

class SetWeightViewModel(
    initialTimestamp: Long? = null,
    private val getNearestWeightUseCase: Lazy<GetNearestWeightUseCase>,
    private val setWeightUseCase: Lazy<SetWeightUseCase>,
    private val deleteWeightUseCase: Lazy<DeleteWeightUseCase>,
    private val appNavigation: AppNavigation,
) : ViewModel() {
    private val initialDate: LocalDate
    private val initialTime: LocalTime

    init {
        if (initialTimestamp != null) {
            val dateTime =
                Instant
                    .fromEpochMilliseconds(initialTimestamp)
                    .toLocalDateTime(TimeZone.currentSystemDefault())
            initialDate = dateTime.date
            initialTime = dateTime.time
        } else {
            initialDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
            initialTime =
                Clock.System
                    .now()
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .time
        }
    }

    private val _uiState =
        MutableStateFlow(
            SetWeightViewModelState(
                date = initialDate,
                time = initialTime,
            ),
        )
    val uiState: StateFlow<SetWeightViewModelState> = _uiState.asStateFlow()

    init {
        loadWeight(initialTimestamp ?: combineDateAndTime(initialDate, initialTime))
    }

    private fun loadWeight(timestamp: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            val result = getNearestWeightUseCase.value(timestamp)
            result
                .onSuccess { item ->
                    if (item != null) {
                        val weightStr = formatWeight(item.weight)
                        _uiState.update {
                            it.copy(
                                weight = weightStr,
                                canDelete = true,
                                recordTimestamp = item.timestamp,
                                loading = false,
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                canDelete = false,
                                recordTimestamp = null,
                                loading = false,
                            )
                        }
                    }
                }.onFailure {
                    _uiState.update { it.copy(loading = false) }
                }
        }
    }

    fun onEvent(event: SetWeightViewModelEvent) {
        when (event) {
            is SetWeightViewModelEvent.WeightChanged -> {
                _uiState.update { it.copy(weight = event.value) }
            }

            is SetWeightViewModelEvent.DateChanged -> {
                _uiState.update { it.copy(date = event.value) }
                loadWeight(combineDateAndTime(event.value, _uiState.value.time))
            }

            is SetWeightViewModelEvent.TimeChanged -> {
                _uiState.update { it.copy(time = event.value) }
                loadWeight(combineDateAndTime(_uiState.value.date, event.value))
            }

            is SetWeightViewModelEvent.SavePressed -> saveWeight()
            is SetWeightViewModelEvent.BackClicked -> appNavigation.back()
            is SetWeightViewModelEvent.DeleteClicked -> _uiState.update { it.copy(deleteConfirmation = true) }
            is SetWeightViewModelEvent.DeleteConfirmed -> deleteWeight()
            is SetWeightViewModelEvent.DeleteCancelled -> _uiState.update { it.copy(deleteConfirmation = false) }
        }
    }

    private fun saveWeight() {
        val state = _uiState.value
        val weightFloat = state.weight.toFloatOrNull() ?: return
        val timestamp = combineDateAndTime(state.date, state.time)
        viewModelScope.launch {
            setWeightUseCase.value(
                WeightsInfo.Item(
                    weight = weightFloat,
                    timestamp = timestamp,
                ),
            )
            appNavigation.back()
        }
    }

    private fun deleteWeight() {
        val state = _uiState.value
        val timestamp = state.recordTimestamp ?: combineDateAndTime(state.date, state.time)
        viewModelScope.launch {
            _uiState.update { it.copy(deleteConfirmation = false) }
            deleteWeightUseCase.value(timestamp)
            appNavigation.back()
        }
    }

    private fun combineDateAndTime(
        date: LocalDate,
        time: LocalTime,
    ): Long {
        val localDateTime =
            LocalDateTime(
                date.year,
                date.month.number,
                date.day,
                time.hour,
                time.minute,
                time.second,
            )
        return localDateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    private fun formatWeight(weight: Float): String =
        if ((weight % 1f) == 0f) {
            weight.toInt().toString()
        } else {
            weight.toString()
        }
}
