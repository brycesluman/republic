package org.sluman.republic.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sluman.republic.data.MainUiState
import org.sluman.republic.domain.FetchDriversAndRoutesUseCase
import org.sluman.republic.domain.SortDriversUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sortDriversUseCase: SortDriversUseCase,
    private val fetchDriversAndRoutesUseCase: FetchDriversAndRoutesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = uiState.value.copy(
            isLoading = true
        )
        viewModelScope.launch {
            try {
                val drivers = fetchDriversAndRoutesUseCase()
                _uiState.value = uiState.value.copy(
                    isError = false,
                    isLoading = false,
                    drivers = drivers
                )
            } catch (e: Exception) {
                println("error caught: $e")
                _uiState.value = uiState.value.copy(
                    isError = true,
                    errorMessage = "An error occurred",
                    isLoading = false
                )
            }
        }
    }

    fun sortList() {
        println("sortList")
        viewModelScope.launch {
            val drivers = sortDriversUseCase()
            println("drivers $drivers")
            _uiState.value = uiState.value.copy(
                isError = false,
                isLoading = false,
                drivers = drivers
            )
        }
    }
}