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
import com.example.aproject.core.utils.rememberScreenInfo
import com.example.aproject.core.utils.toLocalizedDateTime
import com.example.aproject.feature.advice.domain.model.Advice
import com.example.aproject.feature.advice.presentation.theme.AdviceTheme
import com.example.aproject.feature.advice.presentation.viewmodel.AdviceViewModel
import kotlinx.coroutines.launch

/**
 * Advice screen wrapped in [AdviceTheme] with a top bar, FAB, current advice card,
 * and a list of saved advices.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdviceScreen(
    viewModel: AdviceViewModel = hiltViewModel()
) {
    AdviceTheme { // wrapped the screen in our custom theme, so the parent theme is ignored

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
                // Block with current advice
                CurrentAdviceSection(
                    advice = uiState.currentAdvice,
                    isLoading = uiState.isLoading,
                    error = uiState.error,
                    onRetry = { viewModel.getRandomAdvice() }
                )

                // Block with a list of all advices
                AdviceList(
                    adviceList = uiState.advicesList
                )
            }
        }
    }
}

/**
 * Starts collecting advices while the screen is visible and stops when it leaves the foreground.
 */
@Composable
private fun SetUpData(viewModel: AdviceViewModel) {

    val lifecycleOwner = LocalLifecycleOwner.current

    //DisposableEffect is a special effect in Compose. It runs when the screen appears and
    // always executes the onDispose block when the screen disappears.
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

    val screenSize = rememberScreenInfo()

    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier
            .size(72.dp)
            .offset(
                x = (-10).dp,
                y = -screenSize.heightDp / 8
            ),
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {

        Icon(
            painter = painterResource(id = R.drawable.ic_add_advice_button),
            contentDescription = stringResource(R.string.advice_fab_desc),
            modifier = Modifier.size(56.dp),
            tint = Color.Unspecified // leaves the original icon color
        )
    }
}

@Composable
private fun CurrentAdviceSection(
    advice: Advice?,
    isLoading: Boolean,
    error: String?,
    onRetry: () -> Unit
) {
    val screenSize = rememberScreenInfo()

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
                .fillMaxHeight(fraction = if (screenSize.isLandscape) 0.35f else 0.25f)// takes up 1/4 of the parent Column
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
                        modifier = Modifier.verticalScroll(rememberScrollState()), // in horizontal mode, if the text is long, it scrolls
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
    val screenSize = rememberScreenInfo()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // list Header
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

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            //Arrangement.spacedBy(8.dp) sets the same fixed margin between child elements in containers,
            // but it cuts off the shadow from the cards, so it's better to add margin to the cards.

            // Adds space at the margin that disappears as you scroll, so you can scroll above the button.
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
}