package com.example.aproject.feature.advice.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.aproject.R
import com.example.aproject.core.utils.screenSizeInfo
import com.example.aproject.core.utils.toLocalizedDateTime
import com.example.aproject.feature.advice.domain.model.Advice
import com.example.aproject.feature.advice.presentation.theme.AdviceTheme
import com.example.aproject.feature.advice.presentation.viewmodel.AdviceViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdviceScreen(
    viewModel: AdviceViewModel = hiltViewModel()
) {
    AdviceTheme { // обернули экран в нашу кастомную тему и тем самым родительская не учитывается

    SetUpData(viewModel)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AdviceTopBar()
        },
        floatingActionButton = {
            AdviceFab { viewModel.getRandomAdvice() }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 1. Блок с текущим советом
            CurrentAdviceSection(
                advice = uiState.currentAdvice,
                isLoading = uiState.isLoading,
                error = uiState.error,
                onRetry = { viewModel.getRandomAdvice() }
            )

            // 2. Список всех советов
            AdviceList(
                adviceList = uiState.advicesList
            )
        }
    }
}
}


    @Composable
    private fun SetUpData(viewModel: AdviceViewModel) {
        //LocalLifecycleOwner — это специальная переменная в Compose. Она всегда содержит текущий
        // жизненный цикл (Lifecycle) того места, где сейчас находится ваша функция.
        val lifecycleOwner = LocalLifecycleOwner.current

        //DisposableEffect — это специальный эффект в Compose. Он запускается, когда экран
        // появляется, и обязательно выполняет блок onDispose, когда экран исчезает.
        DisposableEffect(lifecycleOwner) {

            val job = lifecycleOwner.lifecycleScope.launch {
                lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.loadAdvices()
                }
            }

            onDispose { job.cancel() }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun AdviceTopBar() {
        TopAppBar(
            title = {
                Text(
                    stringResource(R.string.advice_title_top_bar),
                    fontSize = 20.sp
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        )

    }

    @Composable
    private fun AdviceFab(
        onClick: () -> Unit
    ) {

        val screenSize = screenSizeInfo()

        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier
                .size(72.dp)
                .offset(
                    x = (-10).dp,
                    y = -screenSize.heightDp / 8
                ), //offset сдвигает кнопку относительно её положения без этого сдвига
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {

            //добавить иконку шара с предсказанием
            Icon(
                painter = painterResource(id = R.drawable.ic_add_advice_button),
                contentDescription = stringResource(R.string.advice_fab_desc),
                modifier = Modifier.size(56.dp),
                tint = Color.Unspecified
            ) //tint = Color.Unspecified ОСТАВЛЯЕТ ОРИГИНАЛЬНЫЙ ЦВЕТ SVG
            // Icon(Icons.Default.Add, contentDescription = "Add Advice")
        }
    }


    @Composable
    private fun CurrentAdviceSection(
        advice: Advice?,
        isLoading: Boolean,
        error: String?,
        onRetry: () -> Unit
    ) {
        val screenSize = screenSizeInfo()

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .padding(10.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),

            ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fraction = if (screenSize.isLandscape) 0.35f else 0.25f)// ← ЗАНИМАЕТ 1/4 от родительского Column!
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isLoading -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(R.string.advice_text_loading),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    error != null -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(R.string.advice_text_error, error),
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = onRetry) {
                                Text(stringResource(R.string.advice_action_repeat))
                            }
                        }
                    }

                    advice != null -> {
                        Text(
                            modifier = Modifier.verticalScroll(rememberScrollState()),//в горизонтальном режиме, если текст длинный, то теперь он прокручивается
                            text = advice.advice,
                            fontSize = 20.sp,
                            lineHeight = 28.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }

                    else -> {
                        Text(
                            text = stringResource(R.string.advice_text_empty_state),
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }
    }


    @Composable
    private fun AdviceItem(
        advice: Advice
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp, vertical = 5.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Text(
                    advice.advice,
                    Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(
                    modifier = Modifier
                        .height(8.dp)
                        .background(Color.Transparent)
                )

                Text(
                    advice.timeCreation.toLocalizedDateTime(),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

            }
        }

    }

    @Composable
    private fun AdviceList(
        adviceList: List<Advice>
    ) {
        val screenSize = screenSizeInfo()

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Заголовок списка
            if (adviceList.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.advice_title_list_advices, adviceList.size),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(
                        top = 16.dp,
                        bottom = 8.dp,
                        start = 8.dp,
                        end = 8.dp
                    )
                )
            }


        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            //Arrangement.spacedBy(8.dp) задает одинаковый фиксированный отступ (зазор) между дочерними элементами в контейнерах,
            // но он обрезает тень от карточек, поэтому лучше добавить отступ к карточкам
            //   verticalArrangement = Arrangement.spacedBy(15.dp),
            //Adds space at the that disappears as you scroll, чтобы можно было прокрутить выше кнопки
            contentPadding = PaddingValues(bottom = screenSize.heightDp / 5),
        ) {

            items(adviceList) { advice ->
                AdviceItem(advice = advice)
            }

            if (adviceList.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.advice_empty_list),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }


        }
    }
