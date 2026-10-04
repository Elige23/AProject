package com.example.aproject.feature.advice.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aproject.feature.advice.domain.model.Advice
import com.example.aproject.feature.advice.domain.repository.AdviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the advice screen.
 *
 * Holds the UI state, loads saved advices from Room, and fetches random advice
 * from the API, saving it locally.
 */
@HiltViewModel
class AdviceViewModel @Inject constructor(
    private val repository: AdviceRepository
) : ViewModel() {

    /**
     * UI state of the advice screen.
     *
     * @property currentAdvice The current advice from the API.
     * @property advicesList The list of saved advices from the database.
     * @property isLoading Indicates whether a network request is in progress.
     * @property error The error message if the last request failed, or `null`.
     */
    data class AdviceUiState(
        val currentAdvice: Advice? = null,
        val advicesList: List<Advice> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(AdviceUiState())
    val uiState: StateFlow<AdviceUiState> = _uiState.asStateFlow()

    private val advicesList: StateFlow<List<Advice>> = repository.getAllAdvices().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    /**
     * Collects saved advices from Room into the UI state while the screen is visible.
     */
    suspend fun loadAdvices() {

        advicesList.collect { advices ->
            _uiState.update { it.copy(advicesList = advices) }
        }
    }

    /**
     * Fetches random advice from the API, saves it to Room, and updates the UI state.
     */
    fun getRandomAdvice() = viewModelScope.launch {

        _uiState.update { it.copy(isLoading = true, error = null) }

        val result = repository.getRandomAdvice()

        result.onSuccess { advice ->
            // Save to the database
            repository.insertAdvice(advice)
            _uiState.update {
                it.copy(
                    currentAdvice = advice,
                    isLoading = false,
                    error = null
                )
            }
        }.onFailure { error ->
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = error.message ?: "Unknown error"
                )
            }
        }
    }
}