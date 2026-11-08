package com.example.aproject

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.aproject.feature.advice.domain.model.Advice
import com.example.aproject.feature.advice.domain.repository.AdviceRepository
import com.example.aproject.feature.advice.presentation.viewmodel.AdviceViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for [AdviceViewModel].
 *
 * Verifies that the ViewModel correctly updates its [AdviceViewModel.AdviceUiState]
 * in response to repository results (success and error scenarios), and also checks the
 * initial state when creating the ViewModel.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AdviceViewModelTest {

    // Replaces the main thread executor with a synchronous one to allow
    // StateFlow updates to happen immediately during tests.
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Fake dispatcher for coroutines (replaces Dispatchers.Main)
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repository: AdviceRepository
    private lateinit var viewModel: AdviceViewModel

    /**
     * Replace the main dispatcher with the test dispatcher. Create a mock repository.
     * Provide a default answer for getAllAdvices() called in the ViewModel's init block.
     * Initialize the ViewModel with the mock repository.
     */
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        repository = mockk()

        // Provide a default answer for getAllAdvices()
        every { repository.getAllAdvices() } returns flowOf(emptyList())

        viewModel = AdviceViewModel(repository)
    }

    @After
    fun tearDown() {
        // Reset the dispatcher after tests to its original state.
        Dispatchers.resetMain()
    }

    /**
     * Verify the ViewModel starts with an empty state and no loading.
     */
    @Test
    fun `initial state is correct`() = runTest {

        val state = viewModel.uiState.value
        assertEquals(null, state.currentAdvice)
        assertTrue(state.advicesList.isEmpty())
        assertFalse(state.isLoading)
        assertEquals(null, state.error)
    }

    @Test
    fun `getRandomAdvice updates state with success`() = runTest {
        // Prepare fake data.
        val fakeAdvice = Advice(id = 1, advice = "Test advice")
        println("Fake advice: $fakeAdvice")

        // Configure the mock to return success.
        coEvery { repository.getRandomAdvice() } returns Result.success(fakeAdvice)
        coEvery { repository.insertAdvice(any()) } returns Unit // Mock for saving in the database

        viewModel.getRandomAdvice()

        // Advance the test time so the coroutine completes.
        advanceUntilIdle()

        // Verify the state is updated correctly.
        val state = viewModel.uiState.value
        println("State after success: $state")

        assertEquals(fakeAdvice, state.currentAdvice)
        assertFalse(state.isLoading)
        assertEquals(null, state.error)
    }

    @Test
    fun `getRandomAdvice updates state with error`() = runTest {
        // Configure the mock to return failure.
        val errorMessage = "No internet connection"
        coEvery { repository.getRandomAdvice() } returns Result.failure(Exception(errorMessage))

        viewModel.getRandomAdvice()

        // Advance the test dispatcher to complete the coroutine.
        advanceUntilIdle()

        // Verify the state reflects the error.т
        val state = viewModel.uiState.value
        assertEquals(null, state.currentAdvice)
        assertFalse(state.isLoading)
        assertEquals(errorMessage, state.error)
    }
}