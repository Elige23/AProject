package com.example.aproject.feature.advice.presentation.viewmodel

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.example.aproject.feature.advice.data.repository.AdviceRepositoryImpl
import com.example.aproject.feature.advice.domain.model.Advice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdviceViewModel @Inject constructor(
    private val repository: AdviceRepositoryImpl
): ViewModel() {

    data class AdviceUiState(
        val currentAdvice: Advice? = null, // ← Текущий совет из API
        val advicesList: List<Advice> = emptyList(),
        val isLoading: Boolean = false,    // ← Индикатор загрузки
        val error: String? = null         // ← Сообщение об ошибке
    )

    private val _uiState = MutableStateFlow(AdviceUiState())
    val uiState: StateFlow<AdviceUiState> = _uiState.asStateFlow()

// если это делать в init блоке, то подписка будет пока жива viewmodel, даже если булет другой экран
//    поэтому лучше так как мы делаем.
//    init {
//        loadAdvices()
//    }
//
//    private fun loadAdvices() {
//        viewModelScope.launch {
//            advicesList.collect { advices ->
//                _uiState.update { it.copy(advices = advices) }
//            }
//        }
//    }



    private val advicesList: StateFlow<List<Advice>> = repository.getAllAdvices().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    suspend fun loadAdvices() {

            advicesList.collect { advices ->
                _uiState.update { it.copy(advicesList = advices) }
            }

    }


    fun getRandomAdvice() = viewModelScope.launch {

        _uiState.update { it.copy(isLoading = true, error = null) }

        val result = repository.getRandomAdvice()

        result.onSuccess { advice ->
            // Сохраняем в БД
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
                    error = error.message ?: "Неизвестная ошибка"
                )
            }
        }
    }




}

////рабочий вариант
//val advicesList: StateFlow<List<Advice>> = repository.getAllAdvices().stateIn(
//    scope = viewModelScope,
//    started = SharingStarted.WhileSubscribed(5000),
//    initialValue = emptyList()
//)

//    private var _currentAdvice: MutableStateFlow<Advice> = MutableStateFlow(Advice(advice = "Tap + to take Advice"))
//    val currentAdvice: StateFlow<Advice> = _currentAdvice.asStateFlow()




//    fun getRandomAdvice() = viewModelScope.launch {
//        val result = repository.getRandomAdvice()
//        result.onSuccess { advice ->
//            // Сохраняем в БД
//            repository.insertAdvice(advice)
//            _currentAdvice.update {
//                advice
//            }
//        }
//            .onFailure { error ->
//                _currentAdvice.update {
//                    it.copy(advice = error.message?:"no error")
//                }
//            }
//    }

//// функция экрана
//@Preview
//@Composable
//fun AdviceScreen(
//    modifier: Modifier = Modifier,
//    viewModel: AdviceViewModel = hiltViewModel()
//) {
//    //  val currentAdvice by viewModel.currentAdvice.collectAsStateWithLifecycle()
//
//
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .padding(50.dp)
//    ) {
//        Text(
//            text = currentAdvice.advice,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        Text(
//            text = "Нажми для получения совета",
//            modifier = Modifier
//                .clickable { viewModel.getRandomAdvice() }
//                .padding(8.dp)
//        )
//
//        val advices by viewModel.advicesList.collectAsStateWithLifecycle()
//
//        // ✅ Используем advices напрямую
//        LazyColumn() {
//            items(advices) { advice ->
//                Column() {
//                    Text(text = advice.advice, modifier = Modifier.padding(bottom = 16.dp).fillMaxSize())
//                    Text(text = advice.timeCreation.toString(), modifier = Modifier.padding(bottom = 16.dp).fillMaxSize())
//                }
//
//            }
//        }
//    }
// }